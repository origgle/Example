package project.android.my.myredenvelope.obtain;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.android.my.myredenvelope.R;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public abstract class BaseRedEnvelopeClickObtain {
    protected static final String TAG = "ClickObtain";

    protected String receiveRedEnvelope, messageIcon, examineRedenvelope;
    //获取红包群标题
    private static final String REDENVELOPE_GROUP_TITLE_ID = "com.tencent.mm:id/ew";
    //获取红包发送者的名称
    private static final String redEnvelopeOwnerId = "com.tencent.mm:id/h3";
    //获取红包标题
    private static final String REDENVELOPE_TITLE_ID = "com.tencent.mm:id/yo";
    protected Context mContext;
    public BaseRedEnvelopeClickObtain(Context context) {
        this.mContext = context;
        receiveRedEnvelope = context.getString(R.string.luckymoney_receive_redenvelope);
        messageIcon = context.getString(R.string.luckymoney_message_icon);
        examineRedenvelope = context.getString(R.string.luckymoney_examine_redenvelope);
    }

    public abstract void handleAccessibilityServiceEvent(AccessibilityService service, AccessibilityEvent event);
    public abstract void cleanObtainRedEnvelope();
    protected AccessibilityNodeInfo getSource(AccessibilityEvent accessibilityEvent) {
        AccessibilityNodeInfo nodeInfo = null;
        if (accessibilityEvent != null) {
            nodeInfo = accessibilityEvent.getSource();
        }
        return nodeInfo;
    }

    protected AccessibilityNodeInfo getRootInActiveWindow(AccessibilityService accessibilityService) {
        AccessibilityNodeInfo nodeInfo = null;
        if (accessibilityService != null) {
            nodeInfo = accessibilityService.getRootInActiveWindow();
        }
        return nodeInfo;
    }

    protected void printNodeInfo(AccessibilityNodeInfo info) {
//        Log.i(TAG, "info ：" + info.toString());
        Log.i(TAG, "Text：" + info.getText());
        Log.i(TAG, "widget name：" + info.getClassName());
        Log.i(TAG, "Description：" + (TextUtils.isEmpty(info.getContentDescription()) ? "null" : info
                .getContentDescription().toString()));
    }

    protected void recycle(AccessibilityNodeInfo info) {
        if (info == null) {
            return;
        }
        if (info.getChildCount() == 0) {
            printNodeInfo(info);
        } else {
            for (int i = 0; i < info.getChildCount(); i++) {
                if (info.getChild(i) != null) {
                    recycle(info.getChild(i));
                }
            }
        }
    }

    protected void recycleNode(AccessibilityNodeInfo node) {
        Log.i(TAG, ">>>>>>>>>>>>>>>Start");
        recycle(node);
        Log.i(TAG, ">>>>>>>>>>>>>>>End");
    }

    protected String getGroupTitle(AccessibilityService accessibilityService) {
        String strOwner = "";
        try {
            AccessibilityNodeInfo rootNode = getRootInActiveWindow(accessibilityService);
            List<AccessibilityNodeInfo> ownerNodes = rootNode.findAccessibilityNodeInfosByViewId
                    (REDENVELOPE_GROUP_TITLE_ID);
            if (ownerNodes.size() != 0) {
                strOwner = ownerNodes.get(0).getText().toString();
                if (strOwner != null) {
                    int index = strOwner.lastIndexOf("(");
                    if (index != -1) {
                        strOwner = strOwner.substring(0, index);
                    }
                } else {
                    strOwner = "";
                }
            }
        } catch (NullPointerException npr) {
            strOwner = "";
        }
        return strOwner;
    }

    protected String getHongBaoTitle(AccessibilityNodeInfo node) {
        String title = "";
        try {
            List<AccessibilityNodeInfo> titleList = node.getParent().findAccessibilityNodeInfosByViewId
                    (REDENVELOPE_TITLE_ID);
            title = titleList.get(0).getText().toString();
            if (title == null) {
                title = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            title = "";
        }
        return title;
    }

    protected String getNodeId(AccessibilityNodeInfo node) {
        /* 用正则表达式匹配节点Object */
        Pattern objHashPattern = Pattern.compile("(?<=@)[0-9|a-z]+(?=;)");
        Matcher objHashMatcher = objHashPattern.matcher(node.toString());

        // AccessibilityNodeInfo必然有且只有一次匹配，因此不再作判断
        objHashMatcher.find();

        return objHashMatcher.group(0);
    }

    /**
     * 获取节点聊天群或者对象，通过正则表达式匹配
     * com.tencent.mm:id/h3
     *
     * @param node AccessibilityNodeInfo对象
     * @return owner字符串
     */

    protected String getEnvelopeOwner(AccessibilityNodeInfo node) {
        String caishen = "";
        try {
            List<AccessibilityNodeInfo> list = node.getParent().getParent().findAccessibilityNodeInfosByViewId
                    (redEnvelopeOwnerId);
            caishen = list.get(0).getContentDescription().toString();
            if (caishen != null) {
                caishen = caishen.substring(0, caishen.indexOf(messageIcon));
            }
        } catch (Exception npr) {
            caishen = "";
        }
        return caishen;
    }

    protected boolean isEffectiveRedEnvelopeNode(AccessibilityNodeInfo node) {
        try {
            if (node.getParent() == null) {
                return false;
            }
            List<AccessibilityNodeInfo> obtainList = node.getParent().findAccessibilityNodeInfosByText(receiveRedEnvelope);//领取红包
            List<AccessibilityNodeInfo> examineList = node.getParent().findAccessibilityNodeInfosByText(examineRedenvelope);//查看红包
            if (obtainList.size() > 0 || examineList.size() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
