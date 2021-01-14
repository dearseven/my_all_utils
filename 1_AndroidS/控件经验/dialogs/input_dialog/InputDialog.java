
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cc.m2u.hidrogen.R;

/**
 * 带输入框的dialog
 * Created by wx on 2017/7/6.
 */
public class InputDialog {
    public interface InputDialogOnComplete {
        //void onCancle();

        void onSubmit(String s);
    }

    public void show(String show, final Context ctx, final InputDialogOnComplete listener) {
        //old(show, ctx, listener);
        //获到布局
        LayoutInflater inflater = LayoutInflater.from(ctx);
        final View view = inflater.inflate(R.layout.input_dialog, null);

        //创建dialog
        final AlertDialog dialog = new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.myTransparentDialogTheme)).create();
        dialog.show();
        dialog.setContentView(view);
        //1
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //2
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        //获取对象
        final EditText et = (EditText) view.findViewById(R.id.in_d_input);
        final Button pos = (Button) view.findViewById(R.id.in_d_input_positive);
        final Button neg = (Button) view.findViewById(R.id.in_d_negative);

        //设置显示hint
        et.setHint(show);

        //处理事件
        pos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et.getText().toString();
                if (input.equals("")) {
                    Toast.makeText(ctx, "输入文本不能为空" + input, Toast.LENGTH_LONG).show();
                } else {
                    listener.onSubmit(input);
                    //Toast.makeText(ctx, "修改成功" + input, Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
        });

        neg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Deprecated
    private void old(String show, final Context ctx, final InputDialogOnComplete listener) {
        TypedArray array = ctx.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.colorBackground,
                android.R.attr.textColorPrimary,
        });
        int backgroundColor = array.getColor(0, 0xFF00FF);
        int textColor = array.getColor(1, 0xFF00FF);
        array.recycle();

        LayoutInflater inflater = LayoutInflater.from(ctx);
        final View view = inflater.inflate(R.layout.customview_input_dialog, null);
        EditText et = ((EditText) view.findViewById(R.id.cid_et));
        et.setHint(show);
        et.setBackgroundColor(backgroundColor);
        new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AppTheme_for_input_dialog)).setTitle("编辑")
                //.setIcon(android.R.drawable.ic_dialog_alert)
                .setView(view)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = ((EditText) view.findViewById(R.id.cid_et)).getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(ctx, "输入文本不能为空" + input, Toast.LENGTH_LONG).show();
                        } else {
                            listener.onSubmit(input);
                            //Toast.makeText(ctx, "修改成功" + input, Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
