package com.teetaa.testwh.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teetaa.Debug;
import com.teetaa.testwh.R;
import com.teetaa.testwh.widgets.CyanGridView;

/**
 * gridview平滑翻页demo
 */
public class GridViewTestActivity extends AppCompatActivity {
    private Integer[] integers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
    private CyanGridView cyanGridView;
    private CyanGridViewAdapter cyanGridViewAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view_test);

        cyanGridView = (CyanGridView) findViewById(R.id.grid_view_1);
        cyanGridViewAdapter = new CyanGridViewAdapter();
        cyanGridView.setAdapter(cyanGridViewAdapter);
        //cyanGridViewAdapter.notifyDataSetChanged();
        initGridPageEvent();
    }

    private class CyanGridViewAdapter extends BaseAdapter {
        private LayoutInflater li;

        public CyanGridViewAdapter() {
            li = LayoutInflater.from(GridViewTestActivity.this);
        }

        @Override
        public int getCount() {
            Debug.i(GridViewTestActivity.class, "integers.length:" + integers.length);
            return integers.length;
        }

        @Override
        public Object getItem(int position) {
            return integers[position];
        }

        @Override
        public long getItemId(int position) {
            return integers[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //grid_view_1_item_tv_name
            if (convertView == null) {
                //这里不能用convertView= li.inflate(R.layout.material_grid_item, null)
                //是因为gridview1_item的宽高是在item的顶层布局设定的，如果不在顶层布局设定是可以用上面那个
                convertView = li.inflate(R.layout.gridview1_item, parent, false);
                ViewHolder vh = new ViewHolder();
                vh.tvName = (TextView) convertView.findViewById(R.id.grid_view_1_item_tv_name);
                convertView.setTag(R.id.view_tag_view_holder, vh);
            }
            convertView.setTag(R.id.view_tag_content_id, integers[position]);
            ViewHolder vh = (ViewHolder) convertView.getTag(R.id.view_tag_view_holder);
            vh.tvName.setText("凤凰专区" + integers[position]);
            return convertView;
        }
    }

    private class ViewHolder {
        public TextView tvName;
    }

    //-----
    /**
     * 选择的position的值
     */
    int selected;
    /**
     * 是否有向上的动画
     */
    boolean isUp = false;
    /**
     * 是否有向下的动画
     */
    boolean isDown = false;

    int pageSize = 10;
    int columnSize = 5;


    private void initGridPageEvent() {
        cyanGridView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selected = position;

                if (isDown) {
                    isDown = false;
                    cyanGridView.smoothScrollToPositionFromTop(position, 0, 300);
                }
                if (isUp) {
                    isUp = false;
                    // 让该页的第一行对齐最顶边
                    cyanGridView.smoothScrollToPositionFromTop(position - columnSize, 0, 300);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cyanGridView.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // 如果在某一页的完整最后一行，并且点击向下按钮
                if (isAllPageDown(cyanGridView.getCount()) && keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                        && event.getAction() == KeyEvent.ACTION_DOWN) {

                    isDown = true;
                }

                // 如果在每一页的第一行并且点击向上按钮
                if (isAllPageUp(cyanGridView.getCount()) && keyCode == KeyEvent.KEYCODE_DPAD_UP
                        && event.getAction() == KeyEvent.ACTION_DOWN) {
                    isUp = true;
                }

                return false;
            }
        });
    }

    // 是否在某一页的完整最后一行
    private boolean isAllPageUp(int dataSize) {
        // 求出一共有多少页，这里不需要-1，因为向上滑动需要最后的那一行
        int rawNum = dataSize / pageSize + 1;
        for (int i = 1; i <= rawNum; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (selected == pageSize * i + j) {
                    return true;
                }
            }
//            if (selected == pageSize * i) {
//                return true;
//            }
//            if (selected == pageSize * i + 1) {
//                return true;
//            }
//            if (selected == pageSize * i + 2) {
//                return true;
//            }
//            if (selected == pageSize * i + 3) {
//                return true;
//            }
//            if (selected == pageSize * i + 4) {
//                return true;
//            }

        }
        return false;
    }

    // 如果在某一页的完整最后一行，并且点击向下按钮
    private boolean isAllPageDown(int dataSize) {
        // 求出一共有多少页-1，因为向下滑动不需要最后的那一行
        int rawNum = dataSize / pageSize;
        for (int i = 1; i <= rawNum; i++) {
            for (int j = 0; j < columnSize; j++) {
                if (selected == columnSize + pageSize * (i - 1) + j) {
                    return true;
                }
            }
//            if (selected == columnSize + pageSize* (i - 1)) {
//                return true;
//            }
//            if (selected == columnSize + pageSize * (i - 1) + 1) {
//                return true;
//            }
//            if (selected == columnSize + pageSize * (i - 1) + 2) {
//                return true;
//            }
//            if (selected == columnSize + pageSize * (i - 1) + 3) {
//                return true;
//            }
//            if (selected == columnSize + pageSize * (i - 1) + 4) {
//                return true;
//            }

        }
        return false;
    }
}
