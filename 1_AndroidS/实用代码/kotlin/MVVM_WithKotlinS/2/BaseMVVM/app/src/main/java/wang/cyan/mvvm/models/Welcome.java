package wang.cyan.mvvm.models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.Observable;

import wang.cyan.mvvm.BR;


/**
 * Created by Administrator on 2018/5/2.
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
