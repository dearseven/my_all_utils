package com.teetaa.testwh.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teetaa.testwh.R;
import com.teetaa.testwh.eventbus_param.Down;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/14.
 */

public class FragmentBottom extends Fragment {
    public View mMainView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_bottom, container, false);
        mMainView.findViewById(R.id.down_arrow).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER)) {
                    clickDown(v);
                }
                return false;
            }
        });
        return mMainView;
    }


    public void clickDown(View view) {
        EventBus.getDefault().post(new Down());
    }

}
