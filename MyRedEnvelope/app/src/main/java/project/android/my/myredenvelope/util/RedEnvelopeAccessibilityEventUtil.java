package project.android.my.myredenvelope.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class RedEnvelopeAccessibilityEventUtil {
    public static List<AccessibilityNodeInfo> contains(AccessibilityNodeInfo parentNodeInfo, String buttonName) {

        List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText = parentNodeInfo.findAccessibilityNodeInfosByText(buttonName);
        List<AccessibilityNodeInfo> arrayList = new ArrayList<AccessibilityNodeInfo>();
        for (AccessibilityNodeInfo accessibilityNodeInfo : findAccessibilityNodeInfosByText) {
            CharSequence text = accessibilityNodeInfo.getText();

            if (!TextUtils.isEmpty(text) && text.toString().equals(buttonName)) {
                arrayList.add(accessibilityNodeInfo);
            }
        }

        return arrayList;
    }

    public static boolean viewPerformOnclick(AccessibilityNodeInfo node) {

        if (node == null || !node.isEnabled()) {
            return false;
        }

        return node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }


    public static boolean parentPerformOnclick(AccessibilityNodeInfo node) {

        if (node == null) {
            return false;
        }
        AccessibilityNodeInfo parentNodeInfo = node.getParent();
        if (parentNodeInfo == null || !parentNodeInfo.isEnabled()) {
            return false;
        }
        return parentNodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
    }
}
