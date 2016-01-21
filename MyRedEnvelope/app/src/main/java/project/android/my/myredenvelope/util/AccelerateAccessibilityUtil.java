package project.android.my.myredenvelope.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.Locale;

public class AccelerateAccessibilityUtil {

    private final static int ACCESSIBILITY_ENABLE_SDK_VERSION = 16;
    private final static String LANGUAGE_ZHCN = "zh_CN";
    public static boolean isSdkUnder16(){
        return Build.VERSION.SDK_INT < ACCESSIBILITY_ENABLE_SDK_VERSION;
    }

    public static boolean isLanguageZhCH(){
        String language = Locale.getDefault().toString();
        return language != null && language.equals(LANGUAGE_ZHCN);
    }
    public static boolean isAccessibilitySettingsOn(Context context) {
        if (context == null) {
            return false;
        }

        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getApplicationContext().getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        String packageName = context.getPackageName();
        String accessibilityServicePath = "ks.cm.antivirus.applock.accessibility.AppLockAccessibilityService";
        final String serviceStr = packageName + "/" + accessibilityServicePath;
        if (accessibilityEnabled == 1) {
            TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

            String settingValue = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    String accessabilityService = splitter.next();

                    if (accessabilityService.equalsIgnoreCase(serviceStr)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
