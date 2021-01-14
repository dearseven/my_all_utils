package com.teetaa.testwh.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teetaa.testwh.R;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Fragment_zhuanti extends Fragment{
    public  View mMainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView=inflater.inflate(R.layout.fragment_zhuanti, container, false);
        return mMainView;
    }

}
