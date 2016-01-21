package project.android.my.myredenvelope;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.view.accessibility.AccessibilityEvent;

import project.android.my.myredenvelope.obtain.BaseRedEnvelopeClickObtain;
import project.android.my.myredenvelope.obtain.SingleGroupAutomaticRedEnvelopeObtain;
import project.android.my.myredenvelope.obtain.SingleGroupSemiautomaticRedEnvelopeObtain;

public class RedEnvelopeObtainFacade {

    private BaseRedEnvelopeClickObtain redEnvelopeClickObtain ;

    public RedEnvelopeObtainFacade(Context context){
//        redEnvelopeClickObtain = new SingleGroupAutomaticRedEnvelopeObtain(context);
        redEnvelopeClickObtain = new SingleGroupSemiautomaticRedEnvelopeObtain(context);
    }
    public void handleAccessibilityEvent(AccessibilityService service, AccessibilityEvent accessibilityEvent) {

        try {

            if (!isEventPassedDown(accessibilityEvent)) {
                return;
            }

            if (redEnvelopeClickObtain != null) {
                redEnvelopeClickObtain.handleAccessibilityServiceEvent(service, accessibilityEvent);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }
    public void cleanObtainRedEnvelope(){
        if (redEnvelopeClickObtain != null) {
            redEnvelopeClickObtain.cleanObtainRedEnvelope();
        }
    }
    private boolean isEventPassedDown(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent == null) {
            return false;
        }
        if (accessibilityEvent.getSource() == null) {
            return false;
        }
        return true;
    }
}
