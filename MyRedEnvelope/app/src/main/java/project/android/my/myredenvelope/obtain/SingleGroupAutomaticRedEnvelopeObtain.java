package project.android.my.myredenvelope.obtain;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.android.my.myredenvelope.R;
import project.android.my.myredenvelope.util.RedEnvelopeAccessibilityEventUtil;
import project.android.my.myredenvelope.window.NotificationRedEnvelopeFloatWindow;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class SingleGroupAutomaticRedEnvelopeObtain extends BaseRedEnvelopeClickObtain {

    private HashMap<String,Long> clickedNodemap = new HashMap<String,Long>();
    //红包详情
    private String hongBaoDetailUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    //聊天UI
    private String hongBaoLauncherUI = "com.tencent.mm.ui.LauncherUI";
    //红包弹出对话框界面
    private String hongBaoReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    //相同id红包抢夺的间隔时间
    private static final long REDENVELOPINTERVALTIME = 1000 * 20;
    private boolean hasOpenRedEnvelope = false;
    private static final String WEIXIN_PACKAGE = "com.tencent.mm";
    private static final String MONEY_ID = "com.tencent.mm:id/ayy";
    private AccessibilityService service;
    private AccessibilityEvent event;
    private int openRedEnvelopeTryTimes = 0;
    private int openRedEnvelopeTryMaxTimes = 2;
    private String splitFinish, weixinRedEnvelope, openRedEnvelope;
    private List<String> showWinPages = new ArrayList<String>();
    private String LINEAR_LAYOUT_NAME = "android.widget.LinearLayout";
    private String VIEW_TEXTVIEW_NAME = "android.widget.TextView";
    private String VIEW_LISTVIEW_NAME = "android.widget.ListView";
    private String VIEW_IMAGEBUTTON_NAME = "android.widget.ImageButton";
    private String VIEW_IMAGEVIEW_NAME = "android.widget.ImageView";
    private String VIEW_EDITTEXT_NAME = "android.widget.EditText";
    private String RELATVE_LAYOUT_NAME = "android.widget.RelativeLayout";
    private String FRAME_LAYOUT_NAME = "android.widge.FrameLayout";
    private String FRAME_VIEW_NAME = "android.view.View";
    private String FRAME_TENCENTBASE_NAME = "com.tencent.mm.ui.base.o";
    public SingleGroupAutomaticRedEnvelopeObtain(Context context) {
        super(context);
        splitFinish = context.getString(R.string.luckymoney_split_finish);
        weixinRedEnvelope = context.getString(R.string.luckymoney_weixin_redenvelope);
        openRedEnvelope = context.getString(R.string.luckymoney_open_redenvelope);
        showWinPages.add(hongBaoDetailUI);
        showWinPages.add(hongBaoLauncherUI);
        showWinPages.add(hongBaoReceiveUI);
        showWinPages.add(VIEW_TEXTVIEW_NAME);
        showWinPages.add(VIEW_LISTVIEW_NAME);
        showWinPages.add(LINEAR_LAYOUT_NAME);
        showWinPages.add(VIEW_IMAGEBUTTON_NAME);
        showWinPages.add(VIEW_IMAGEVIEW_NAME);
        showWinPages.add(VIEW_EDITTEXT_NAME);
        showWinPages.add(RELATVE_LAYOUT_NAME);
        showWinPages.add(FRAME_LAYOUT_NAME);
        showWinPages.add(FRAME_VIEW_NAME);
        showWinPages.add(FRAME_TENCENTBASE_NAME);
    }


    @Override
    public void handleAccessibilityServiceEvent(AccessibilityService service, AccessibilityEvent event) {
        this.service = service;
        this.event = event;
        if (!isEventEffective(event)) {
            NotificationRedEnvelopeFloatWindow.getInstance(mContext).receiveCommend(NotificationRedEnvelopeFloatWindow.WEBCHAT_COMMEND_REMOVE_FLOAT_WINDOW);
            return;
        }

        checkWebChatIsInLauncherUI(event);
        AccessibilityNodeInfo info = getSource(event);
        redEnvelopeObtain(info);
    }

    private void checkWebChatIsInLauncherUI(AccessibilityEvent event) {
        NotificationRedEnvelopeFloatWindow.getInstance(mContext).receiveCommend(NotificationRedEnvelopeFloatWindow
                .WEBCHAT_COMMEND_REDPACKET_WAITTING);
    }

    @Override
    public void cleanObtainRedEnvelope() {
        clickedNodemap.clear();
    }

    private boolean isEventEffective(AccessibilityEvent event) {
        if (event.getPackageName().equals(WEIXIN_PACKAGE)) {
            return true;
        }

        return false;
    }

    protected synchronized void redEnvelopeObtain(AccessibilityNodeInfo info) {
        if (info == null) {
            return;
        }
        Log.d(TAG,"redEnvelopeObtain start>>>>>>>>> " );
        /**聊天群**/
        handleLuckyMoneyLauncherUI(info);
        /**红包对话框**/
        handleLuckyMoneyReceiveUI(info);
        /**红包详情页**/
        handleLuckyMoneyDetailUI(info);
        Log.d(TAG, "redEnvelopeObtain end>>>>>>>>> ");
    }


    private void handleLuckyMoneyDetailUI(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> obtainMoneyList = info.findAccessibilityNodeInfosByViewId(MONEY_ID);
        Log.d(TAG,"handleLuckyMoneyDetailUI " + obtainMoneyList.size() +" hasOpenRedEnvelope =" + hasOpenRedEnvelope );
        if (obtainMoneyList.size() > 0 && hasOpenRedEnvelope) {
            hasOpenRedEnvelope = false;
            NotificationRedEnvelopeFloatWindow.getInstance(mContext).receiveCommend
                    (NotificationRedEnvelopeFloatWindow.WEBCHAT_COMMEND_REDPACKET_SNATCH_SUCCESS);
        }
        if (event.getClassName().equals(hongBaoDetailUI)) {
            service.performGlobalAction(service.GLOBAL_ACTION_BACK);
        }
    }

    private void handleLuckyMoneyReceiveUI(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> distributionFinish = info.findAccessibilityNodeInfosByText(splitFinish);
        if (distributionFinish.size() > 0) {
            NotificationRedEnvelopeFloatWindow.getInstance(mContext).receiveCommend
                    (NotificationRedEnvelopeFloatWindow.WEBCHAT_COMMEND_REDPACKET_SNATCH_FAIL);
        }
        for (int i = 0; i < distributionFinish.size(); i++) {

            if (event.getClassName().equals(hongBaoReceiveUI)) {
                service.performGlobalAction(service.GLOBAL_ACTION_BACK);
            }
        }

        List<AccessibilityNodeInfo> openRedEnvelopeClick = info.findAccessibilityNodeInfosByText(openRedEnvelope);
        for (int i = 0; i < openRedEnvelopeClick.size(); i++) {
            NotificationRedEnvelopeFloatWindow.getInstance(mContext).receiveCommend
                    (NotificationRedEnvelopeFloatWindow.WEBCHAT_COMMEND_REDPACKET_COMMING);
            openRedEnvelope(openRedEnvelopeClick.get(i));
        }

    }

    private void openRedEnvelope(AccessibilityNodeInfo info) {
        try {
            boolean openFlag = RedEnvelopeAccessibilityEventUtil.viewPerformOnclick(info);
            if (!openFlag) {
                if (checkNeedOpenAgain()) {
                    openRedEnvelope(info);
                }
            } else {
                hasOpenRedEnvelope = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (checkNeedOpenAgain()) {
                openRedEnvelope(info);
            }
        } finally {
            openRedEnvelopeTryTimes = 0;
        }
    }
    private boolean checkNeedOpenAgain(){
        openRedEnvelopeTryTimes++;
        return openRedEnvelopeTryTimes < openRedEnvelopeTryMaxTimes;
    }
    private String lastObtainRedEnvelopeId = "";
    private void handleLuckyMoneyLauncherUI(AccessibilityNodeInfo info) {
        List<AccessibilityNodeInfo> redEnvelopeNodes = info.findAccessibilityNodeInfosByText(weixinRedEnvelope);
        if (redEnvelopeNodes.size() <= 0) {
            return;
        }
        for (int i = redEnvelopeNodes.size() - 1; i >= 0 ; i--) {
            AccessibilityNodeInfo temNode = redEnvelopeNodes.get(i);
            if (!isEffectiveRedEnvelopeNode(temNode)) {
                continue;
            }

            String idStr = getHongBaoHash(temNode);
            if (shouldObtainThisRedEnvelop(idStr)) {
                boolean obtainFlag = RedEnvelopeAccessibilityEventUtil.parentPerformOnclick(temNode);
                if (obtainFlag) {
                    lastObtainRedEnvelopeId = idStr;
                }
            }
            return;
        }
    }

    private boolean shouldObtainThisRedEnvelop(String redEnvelopeId) {
        return !lastObtainRedEnvelopeId.equals(redEnvelopeId);
    }

    private String getHongBaoHash(AccessibilityNodeInfo node) {
        return super.getEnvelopeOwner(node) + super.getHongBaoTitle(node) + super.getNodeId(node);
    }

}
