package project.android.my.myredenvelope;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import project.android.my.myredenvelope.util.AccelerateAccessibilityUtil;

public class MainActivity extends AppCompatActivity {
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        findViewById(R.id.jump_accessibility).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
//                通知读取权限
//                startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
            }
        });
        findViewById(R.id.singleGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccelerateAccessibilityUtil.isAccessibilitySettingsOn(mActivity)) {

                } else {
                    Toast.makeText(MainActivity.this, "请先开启，本项目的辅助功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.pluralGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccelerateAccessibilityUtil.isAccessibilitySettingsOn(mActivity)) {

                } else {
                    Toast.makeText(MainActivity.this, "请先开启，本项目的辅助功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
        findViewById(R.id.send_noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notification noti = new Notification(R.drawable.notification_intercept_redpacket_fly, "[微信红包]....", System.currentTimeMillis());
                CharSequence contentTitle = "[微信红包]....";
                CharSequence contentText = "Context....";
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0);
                noti.setLatestEventInfo(getApplicationContext(), contentTitle, contentText, pendingIntent);

                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, noti);

                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.notification_intercept_redpacket_fly);;
                Notification notification = new NotificationCompat.Builder(mActivity).setLargeIcon(icon).setSmallIcon(R.drawable.notification_intercept_redpacket_fly)
                        .setTicker("[微信红包]....").setContentInfo("[微信红包]....").setContentTitle("[微信红包]....").setContentText("[微信红包]....")
                        .setNumber(++messageNum).setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL).build();
                manager.notify(NOTIFICATION_ID_1++, notification);
            }
        });
    }

}
