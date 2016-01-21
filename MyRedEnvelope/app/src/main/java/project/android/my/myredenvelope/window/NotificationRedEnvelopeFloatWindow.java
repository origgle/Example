package project.android.my.myredenvelope.window;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import project.android.my.myredenvelope.R;


public class NotificationRedEnvelopeFloatWindow {
    public static int WEBCHAT_COMMEND_REMOVE_FLOAT_WINDOW = 0;
    public static int WEBCHAT_COMMEND_REDPACKET_WAITTING = 1;
    public static int WEBCHAT_COMMEND_REDPACKET_COMMING = 2;
    public static int WEBCHAT_COMMEND_REDPACKET_SNATCH_SUCCESS = 3;
    public static int WEBCHAT_COMMEND_REDPACKET_SNATCH_FAIL = 4;
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams mWindowParams;
    private static NotificationRedEnvelopeFloatWindow mNotificationRedenvelopeInstance;
    private Context mContext;
    private View mRedPacketView;
    private TextView mMainStrTextView;
    private TextView mSubStrTextView;
    private Button mSnatchButton;
    private ImageView mIcon;
    private RelativeLayout mBackgroundView;
    private boolean mIsShow;
    private int mState;
    private Runnable mShowWaittingRedpacketFloatWindowRunnable;

    private Handler mHandler;
    private boolean mIsSnatchRedpacketTime;

    private NotificationRedEnvelopeFloatWindow(Context context) {
        mContext = context;
        setCustomLayoutParams();
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public static synchronized NotificationRedEnvelopeFloatWindow getInstance(Context context) {
        if (mNotificationRedenvelopeInstance == null) {
            mNotificationRedenvelopeInstance = new NotificationRedEnvelopeFloatWindow(context);
        }
        return mNotificationRedenvelopeInstance;
    }

    public void showNotificationRedEnvelopeFloatWindow(View view) {
        mRedPacketView = checkView(view);
        mWindowManager.addView(mRedPacketView, mWindowParams);
        mIsShow = true;
    }

    public void showWebChatRedEnvelopeFloatWindow() {
        if (mIsShow) {
            mWindowManager.updateViewLayout(mRedPacketView, mWindowParams);
            return;
        }
        mWindowManager.addView(mRedPacketView, mWindowParams);
        mIsShow = true;
    }

    public void removeRedPacketFloatWindow() {
        if (!mIsShow) {
            return;
        }
        if (mRedPacketView != null) {
            mWindowManager.removeView(mRedPacketView);
        }
        mIsShow = false;
    }

    private View checkView(View mCustomsReadPacketView) {
        if (mCustomsReadPacketView == null) {
            //传进来是空值启用默认
            mCustomsReadPacketView = LayoutInflater.from(mContext).inflate(R.layout.notification_intercept_redenvelope_title, null);
        }
        return mCustomsReadPacketView;
    }

    private void setCustomLayoutParams() {
        if (mWindowParams == null) {
            mWindowParams = new WindowManager.LayoutParams();
            mWindowParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mWindowParams.format = PixelFormat.TRANSLUCENT;
            mWindowParams.flags = WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams
                    .FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
            mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
    }

    private void removeShowWaittingRedpacketFloatWindowRunnable() {
        if (mHandler != null && mShowWaittingRedpacketFloatWindowRunnable != null) {
            mHandler.removeCallbacks(mShowWaittingRedpacketFloatWindowRunnable);
        }
    }

    private void showWaittingRedpacketFloatWindowRunnable() {
        if (mHandler == null) {
            mHandler = new Handler();
        }

        if (mShowWaittingRedpacketFloatWindowRunnable == null) {
            mShowWaittingRedpacketFloatWindowRunnable = new Runnable() {
                @Override
                public void run() {
                    mIsSnatchRedpacketTime = false;
                    receiveCommend(WEBCHAT_COMMEND_REDPACKET_WAITTING);
                }
            };
        }
        mHandler.postDelayed(mShowWaittingRedpacketFloatWindowRunnable, 5000);
        mIsSnatchRedpacketTime = true;
    }

    public void receiveCommend(int commend) {
        if (true) {
            return;
        }
        if (WEBCHAT_COMMEND_REDPACKET_COMMING == commend) {
            mIsSnatchRedpacketTime = false;
            removeShowWaittingRedpacketFloatWindowRunnable();
            setRedPacketCommingView();
        } else if (WEBCHAT_COMMEND_REDPACKET_SNATCH_SUCCESS == commend) {
            setRedPacketSnatchSuccessView();
            showWaittingRedpacketFloatWindowRunnable();
        } else if (WEBCHAT_COMMEND_REDPACKET_SNATCH_FAIL == commend) {
            setRedPacketSnatchFailView();
            showWaittingRedpacketFloatWindowRunnable();
        } else if (WEBCHAT_COMMEND_REDPACKET_WAITTING == commend) {
            if (mState == WEBCHAT_COMMEND_REDPACKET_WAITTING) {
                return;
            }

            if (mIsSnatchRedpacketTime) {
                return;
            }

            setRedPacketWaittingView();
        } else if (WEBCHAT_COMMEND_REMOVE_FLOAT_WINDOW == commend) {
            mIsSnatchRedpacketTime = false;
            if (mState == WEBCHAT_COMMEND_REMOVE_FLOAT_WINDOW) {
                return;
            }

            mState = commend;
            removeRedPacketFloatWindow();
            removeShowWaittingRedpacketFloatWindowRunnable();
            return;
        }

        if (mRedPacketView != null) {
            showWebChatRedEnvelopeFloatWindow();
        }

        mState = commend;
    }

    private void setRedPacketWaittingView() {
        initRedPacketDefaultView(R.string.cn_notification_webchat_redpacket_waitting_mianstr,
                R.string.cn_notification_webchat_redpacket_waitting_substr,
                R.drawable.notification_intercept_redpacket_waitting_icon,
                R.color.notification_intercept_redpacket_waitting_background);
        if (mSnatchButton.getVisibility() == View.GONE) {
            mSnatchButton.setVisibility(View.VISIBLE);
        }
    }

    private void setRedPacketSnatchFailView() {
         initRedPacketDefaultView(R.string.cn_notification_webchat_redpacket_snatch_fail_mianstr,
                 R.string.cn_notification_webchat_redpacket_snatch_fail_substr,
                 R.drawable.notification_intercept_redpacket_fly,
                 R.color.notification_intercept_redpacket_snatch_fail_background);
    }

    private void setRedPacketSnatchSuccessView() {
         initRedPacketDefaultView(R.string.cn_notification_webchat_redpacket_snatch_success_mianstr,
                 R.string.cn_notification_webchat_redpacket_snatch_success_substr,
                 R.drawable.notification_checkbox_title_notifiy,
                 R.color.notification_intercept_redpacket_snatch_success_background);
    }

    private void setRedPacketCommingView() {
         initRedPacketDefaultView(R.string.cn_notification_webchat_redpacket_comming_mainstr,
                 R.string.cn_notification_webchat_redpacket_comming_substr,
                 R.drawable.notification_intercept_redpacket_icon,
                 R.color.notification_intercept_redpacket_comming_background);
    }

    private void initRedPacketDefaultView(int mainStrId, int subStrId, int iconId, int backgroundId) {
        String mainStr = mContext.getString(mainStrId);
        String subStr = mContext.getString(subStrId);
        if (mRedPacketView == null) {
            mRedPacketView = View.inflate(mContext, R.layout.notification_intercept_redenvelope_title, null);
        }
        if (mBackgroundView == null) {
            mBackgroundView = (RelativeLayout) mRedPacketView.findViewById(R.id.childview_layout);
        }
        if (mMainStrTextView == null) {
            mMainStrTextView = (TextView) mRedPacketView.findViewById(R.id.child_specif_main_txt);
        }
        if (mSubStrTextView == null) {
            mSubStrTextView = (TextView) mRedPacketView.findViewById(R.id.child_sub_specif_main_txt);
        }
        if (mSnatchButton == null) {
            mSnatchButton = (Button) mRedPacketView.findViewById(R.id.snatch_redpacket_button);
        }

        Drawable colorDrawable = new ColorDrawable(mContext.getResources().getColor(backgroundId));
        mBackgroundView.setBackgroundDrawable(colorDrawable);

        if (mSnatchButton.getVisibility() == View.VISIBLE) {
            mSnatchButton.setVisibility(View.GONE);
        }

        if (mIcon == null) {
            mIcon = (ImageView) mRedPacketView.findViewById(R.id.child_image_icon);
        }

        mIcon.setImageResource(iconId);

        mMainStrTextView.setText(mainStr);
        mSubStrTextView.setText(subStr);
    }
}
