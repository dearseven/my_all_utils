package com.teetaa.testwh.activity.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teetaa.testwh.R;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Fragment2 extends Fragment{
    public  View mMainView;
    TextView title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView=inflater.inflate(R.layout.fragment2, container, false);

        //title=(TextView)mMainView.findViewById(R.id.show_title);
        return mMainView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}
