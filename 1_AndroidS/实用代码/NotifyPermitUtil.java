
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationManagerCompat;

import cyan.mine.cyanupdate.views.AskDialog;

public class NotifyPermitUtil {

    public void checkNotifySetting(Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知。
        boolean isOpened = manager.areNotificationsEnabled();

        if (isOpened) {
//            mBinding.tvMsg.setText("通知权限已经被打开" +
//                    "\n手机型号:" + android.os.Build.MODEL +
//                    "\nSDK版本:" + android.os.Build.VERSION.SDK +
//                    "\n系统版本:" + android.os.Build.VERSION.RELEASE +
//                    "\n软件包名:" + getPackageName());
//            Toast.makeText(context, "已打开", Toast.LENGTH_SHORT).show();

        } else {
            //mBinding.tvMsg.setText("还没有开启通知权限，点击去开启");
//            Toast.makeText(context, "没有打开", Toast.LENGTH_SHORT).show();
            new AskDialog().show("为了更及时的收到消息，请打开通知", context, new AskDialog.AnswerListener() {
                @Override
                public void answer(boolean yesOrNo) {
                    if (yesOrNo) {
                        goSetting(context);
                    }
                }
            }, "重要提示", "好的", "待会");
        }
    }

    private void goSetting(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            //8.0及以后版本使用这两个extra.  >=API 26
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            //5.0-7.1 使用这两个extra.  <= API 25, >=API 21
            intent.putExtra("app_package", context.getPackageName());
            intent.putExtra("app_uid", context.getApplicationInfo().uid);

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Uri packageURI= Uri.parse("package:"+ context.getPackageName());
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,packageURI);
            //其他低版本或者异常情况，走该节点。进入APP设置界面
//            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.putExtra("package", context.getPackageName());
            //val uri = Uri.fromParts("package", packageName, null)
            //intent.data = uri
            context.startActivity(intent);
        }

    }

}
