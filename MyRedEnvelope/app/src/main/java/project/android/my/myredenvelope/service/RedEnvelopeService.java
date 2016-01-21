package project.android.my.myredenvelope.service;

import android.accessibilityservice.AccessibilityService;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import project.android.my.myredenvelope.RedEnvelopeAccessibilityDirectorInstance;

public class RedEnvelopeService extends AccessibilityService {
    private String TAG = RedEnvelopeService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        RedEnvelopeAccessibilityDirectorInstance.getInstance().init(this);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.i(TAG, event.getClassName().toString());
        RedEnvelopeAccessibilityDirectorInstance.getInstance().onAccessibilityEvent(this, event);
    }

    @Override
    public void onInterrupt() {

    }
}
