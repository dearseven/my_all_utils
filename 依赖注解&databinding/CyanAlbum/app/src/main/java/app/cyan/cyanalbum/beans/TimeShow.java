package app.cyan.cyanalbum.beans;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.text.SimpleDateFormat;
import java.util.Date;

import app.cyan.cyanalbum.BR;

/**
 * Created by Cyan on 2017/10/28.
 */

public class TimeShow extends BaseObservable {
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public TimeShow() {
    }
    private String timeStr;

    @Bindable
    public String getTimeStr() {
        return timeStr;
    }

    public void updateTimeStr() {
        timeStr = "现在时间:" + format.format(new Date());
        notifyPropertyChanged(BR.timeStr);
    }
}
