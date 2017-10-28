package app.cyan.cyanalbum.beans;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import app.cyan.cyanalbum.BR;

/**
 * Created by Cyan on 2017/10/28.
 */

public class Welcome extends BaseObservable {
    private String name;
    private String welcome;

    public void setName(String name) {
        this.name = name;;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setWelcome(String welcome) {
        this.welcome = welcome;
        notifyPropertyChanged(BR.welcome);
    }

    @Bindable
    public String getWelcome() {
        return welcome;
    }
}
