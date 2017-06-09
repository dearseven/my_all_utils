package com.teetaa.testwh.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.teetaa.testwh.R;
import com.teetaa.testwh.bean.menuBean;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Fragment_fenlei extends Fragment{
    public  View mMainView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView=inflater.inflate(R.layout.fragment_fenlie, container, false);
        return mMainView;
    }

}
