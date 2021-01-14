
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import cc.m2u.hidrogen.R;
import cc.m2u.hidrogen.autoupdate.view.CustomDialog;


/**
 * 供使用者调用，6.0动态权限需要在activiy追加方法onRequestPermissionsResult追加方法
 * Created by wx on 2017/5/2.
 */

public class AutoUpdater /*implements DialogInterface.OnKeyListener*/ {
    private UpdateHelper updateInfo;
    private AppCompatActivity ctx;

    public static final int REQUEST_WRITE_STORAGE = 18731;
    private CustomDialog mDialog = null;

    private AutoUpdater(UpdateHelper updateInfo) {
        this.updateInfo = updateInfo;
    }

    /**
     * 因为可能要请求权限，所以AppCompatActivity的回调中记得调用方法
     * <p>
     * public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
     * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     * switch (requestCode) {
     * case REQUEST_WRITE_STORAGE:
     * if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
     * //获取到存储权限,进行下载
     * autoUpdater.startUpdate();
     * } else {
     * Toast.makeText(MainActivity.this, "不授予存储权限将无法进行下载!", Toast.LENGTH_SHORT).show();
     * }
     * break;
     * }
     * }
     */
    public void startUpdate(AppCompatActivity appCampat) {
        this.ctx = appCampat;
        mDialog = new CustomDialog(appCampat);
        LayoutInflater inflater = LayoutInflater.from(appCampat);
        View view = inflater.inflate(R.layout.ask_dialog, null);
        mDialog.addContentView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        TextView title = (TextView) view.findViewById(R.id.ttfh_ask_dialog_title);
        TextView content = (TextView) view.findViewById(R.id.ttfh_ask_dialog_content);
        Button okBtn = (Button) view.findViewById(R.id.ttfh_ask_dialog_ok);
        Button backBtn = (Button) view.findViewById(R.id.ttfh_ask_dialog_back);
        title.setText(updateInfo.title);
        content.setText(updateInfo.content);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //请求存储权限
                boolean hasPermission = (ContextCompat.checkSelfPermission(ctx,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(ctx, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
                    ActivityCompat.shouldShowRequestPermissionRationale(ctx,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE);
                } else {
                    //下载
                    startUpdate();
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        mDialog.show();
        okBtn.requestFocus();
    }

    private void close() {
        mDialog.cancel();
        updateInfo = null;
        this.ctx = null;
    }

    public void startUpdate() {
        Intent it = new Intent(this.ctx, UpdateService.class);
        //下载地址
        it.putExtra("ttfh_main", updateInfo.url);
        this.ctx.startService(it);
        close();
    }

//    @Override
//    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//        Debug.iwxdb(getClass(), "11212223");
//        return false;
//    }

    public static class UpdateHelper {
        private String title;
        private String content;
        private String url;

        public UpdateHelper(String title, String content, String url) {
            this.title = title;
            this.content = content;
            this.url = url;
        }

        public AutoUpdater builder() {
            AutoUpdater autoUpdater = new AutoUpdater(this);
            return autoUpdater;
        }
    }
}
