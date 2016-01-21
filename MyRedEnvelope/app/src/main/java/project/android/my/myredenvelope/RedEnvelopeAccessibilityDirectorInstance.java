package project.android.my.myredenvelope;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

public class RedEnvelopeAccessibilityDirectorInstance {
    private static RedEnvelopeAccessibilityDirectorInstance instance = new RedEnvelopeAccessibilityDirectorInstance();
    private boolean startHandleEvent = true;
    private RedEnvelopeObtainFacade redEnvelopeClickObtain;

    public static RedEnvelopeAccessibilityDirectorInstance getInstance() {
        return instance;
    }

    public void init(Context context) {
        redEnvelopeClickObtain = new RedEnvelopeObtainFacade(context);
    }

    public void onAccessibilityEvent(AccessibilityService service, AccessibilityEvent event) {
        if (!startHandleEvent) {
            return;
        }
        if (redEnvelopeClickObtain != null) {
            redEnvelopeClickObtain.handleAccessibilityEvent(service, event);
        }
    }

    public void cleanObtainRedEnvelope(){
        if (redEnvelopeClickObtain != null) {
            redEnvelopeClickObtain.cleanObtainRedEnvelope();
        }
    }

    public void startObtainRedEnvelope() {
        startHandleEvent = true;
        cleanObtainRedEnvelope();
    }

    public void stopObtainRedEnvelope(){
        startHandleEvent = false;
    }
}
