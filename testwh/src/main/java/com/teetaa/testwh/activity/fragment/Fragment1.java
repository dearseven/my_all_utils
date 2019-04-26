package com.teetaa.testwh.activity.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teetaa.testwh.Main2Activity;
import com.teetaa.testwh.R;
import com.teetaa.testwh.bean.menuBean;
import com.teetaa.testwh.eventbus_param.ChangeFragmentShowHide;
import com.teetaa.testwh.eventbus_param.MainViewPageItenSelected;
import com.teetaa.testwh.focus_anim.FocusFollowImageView;
import com.teetaa.testwh.focus_anim.FocusFollowImageView2;
import com.teetaa.testwh.focus_anim.ViewPositionAndSizeGetter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/14.
 */

public class Fragment1 extends Fragment implements View.OnFocusChangeListener {
    public View mMainView;
    ListView lv;
    MyAdapter adapter;
    ImageView seachIv;
    TextView menu1, menu2, menu3, menu4;
    ArrayList<menuBean> menuBeens = new ArrayList<menuBean>();
    int isSelect = 1;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment1, container, false);
        lv = (ListView) mMainView.findViewById(R.id.lv);
        seachIv = (ImageView) mMainView.findViewById(R.id.item_ico_seach);
        menu1 = (TextView) mMainView.findViewById(R.id.item_title_1);
        menu2 = (TextView) mMainView.findViewById(R.id.item_title_2);
        menu3 = (TextView) mMainView.findViewById(R.id.item_title_3);
        menu4 = (TextView) mMainView.findViewById(R.id.item_title_4);
        seachIv.setFocusable(true);
        menu1.setFocusable(true);
        menu2.setFocusable(true);
        menu3.setFocusable(true);
        menu4.setFocusable(true);
        menu1.requestFocus();
        seachIv.setTag(R.id.view_tag_content_id, 1);
        seachIv.setOnFocusChangeListener(this);
        menu1.setOnFocusChangeListener(this);
        menu1.setTag(R.id.view_tag_content_id, 2);
        menu2.setOnFocusChangeListener(this);
        menu2.setTag(R.id.view_tag_content_id, 3);
        menu3.setOnFocusChangeListener(this);
        menu3.setTag(R.id.view_tag_content_id, 4);
        menu4.setOnFocusChangeListener(this);
        menu4.setTag(R.id.view_tag_content_id, 5);
        //TODO 菜单
        menuBeens.add(new menuBean("搜索", false, R.mipmap.search));
        menuBeens.add(new menuBean("推荐", true, R.mipmap.search));
        menuBeens.add(new menuBean("专题", true, R.mipmap.search));
        menuBeens.add(new menuBean("分类", true, R.mipmap.search));
        menuBeens.add(new menuBean("我的", true, R.mipmap.search));

//        adapter = new MyAdapter(getContext());
//        lv.setAdapter(adapter);
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                isSelect = position;
//                adapter.notifyDataSetChanged();
//                Intent mIntent = new Intent(MainActivity.ACTION_NAME);
//                mIntent.putExtra("index", position);
//                //发送广播
//                getContext().sendBroadcast(mIntent);
//
//            }
//        });
        return mMainView;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            //Debug.i(Fragment1.class, ((TextView)v).getText() + "," + hasFocus);
            EventBus.getDefault().post(new ChangeFragmentShowHide((int) v.getTag(R.id.view_tag_content_id)));
        }
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局

        /**
         * 构造函数
         */
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return menuBeens.size();//返回数组的长度
        }

        @Override
        public Object getItem(int position) {
            return menuBeens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 书中详细解释该方法
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            //观察convertView随ListView滚动情况
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.lv_item, null);
                holder = new ViewHolder();
                /**得到各个控件的对象*/
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.ico = (ImageView) convertView.findViewById(R.id.item_ico);
                convertView.setTag(holder);//绑定ViewHolder对象
            } else {
                holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            }
            menuBean bean = menuBeens.get(position);
            /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
            holder.title.setText(bean.title);
            if (bean.isTitle) {
                holder.title.setVisibility(View.VISIBLE);
                holder.ico.setVisibility(View.GONE);
            } else {
                holder.ico.setVisibility(View.VISIBLE);
                holder.title.setVisibility(View.GONE);
            }
//            if (position == isSelect) {
//                convertView.setBackgroundResource(R.color.yellow);
//                holder.title.setTextColor(Color.BLACK);
//            } else {
//                convertView.setBackgroundColor(Color.TRANSPARENT);
//                holder.title.setTextColor(Color.WHITE);
//
//            }
            return convertView;
        }

    }

    /**
     * 存放控件
     */
    public class ViewHolder {
        public TextView title;
        public ImageView ico;
    }


    /**
     * 这个主要是接受Main2Activity传来的ViewPager切换事件<br><br/>
     * 包括用户在左侧导航栏上下切换，点击右侧的上下箭头切换<br/>
     * 另外，还接受Viewpager最左侧图片点击遥控器的左键或者上键切回到左侧导航的事件<br><br/>
     * 这个代码，有可能触发左侧导航item的焦点跟随动画
     * @param param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MainViewPageItenSelected param) {
        if (param.index == 0) {
            ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(seachIv, getActivity(),getActivity());
            if (param.needAnim == false) {
                //Debug.i(Fragment1.class, r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH());
                RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) param.focusLayer.getLayoutParams();
                rp.topMargin = r.getY();
                rp.leftMargin = r.getX();
                rp.width = r.getW();
                rp.height = r.getH();
                param.focusLayer.setLayoutParams(rp);
            } else {
                if(param.forcedHasFocus){
                    seachIv.requestFocus();
                }
                if (seachIv.hasFocus())
                    changeFocusView(param.focusLayer, r.getX(), r.getY(), r.getW(), r.getH());
            }
            seachIv.setBackgroundResource(R.drawable.left_menu_selected);
            menu1.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu2.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu3.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu4.setBackgroundResource(R.drawable.left_menu_not_selected);
        }else if (param.index == 1) {
            ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(menu1, getActivity(),getActivity());
            if (param.needAnim == false) {
                //Debug.i(Fragment1.class, r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH());
                RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) param.focusLayer.getLayoutParams();
                rp.topMargin = r.getY();
                rp.leftMargin = r.getX();
                rp.width = r.getW();
                rp.height = r.getH();
                param.focusLayer.setLayoutParams(rp);
            } else {
                if(param.forcedHasFocus){
                    menu1.requestFocus();
                }
                if (menu1.hasFocus())
                    changeFocusView(param.focusLayer, r.getX(), r.getY(), r.getW(), r.getH());
            }

            seachIv.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu1.setBackgroundResource(R.drawable.left_menu_selected);
            menu2.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu3.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu4.setBackgroundResource(R.drawable.left_menu_not_selected);
        } else if (param.index == 2) {
            ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(menu2, getActivity(),getActivity());
           // Debug.i(Fragment1.class, r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH());
            if(param.forcedHasFocus){
                menu2.requestFocus();
            }
            if (menu2.hasFocus())
                changeFocusView(param.focusLayer, r.getX(), r.getY(), r.getW(), r.getH());

            seachIv.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu2.setBackgroundResource(R.drawable.left_menu_selected);
            menu1.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu3.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu4.setBackgroundResource(R.drawable.left_menu_not_selected);
        } else if (param.index == 3) {
            ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(menu3, getActivity(),getActivity());
           // Debug.i(Fragment1.class, r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH());
            if(param.forcedHasFocus){
                menu3.requestFocus();
            }
            if (menu3.hasFocus())
                changeFocusView(param.focusLayer, r.getX(), r.getY(), r.getW(), r.getH());

            seachIv.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu3.setBackgroundResource(R.drawable.left_menu_selected);
            menu2.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu1.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu4.setBackgroundResource(R.drawable.left_menu_not_selected);
        } else if (param.index == 4) {
            ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(menu4, getActivity(),getActivity());
            //Debug.i(Fragment1.class, r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH());
            if(param.forcedHasFocus){
                menu4.requestFocus();
            }
            if (menu4.hasFocus())
                changeFocusView(param.focusLayer, r.getX(), r.getY(), r.getW(), r.getH());

            seachIv.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu4.setBackgroundResource(R.drawable.left_menu_selected);
            menu2.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu3.setBackgroundResource(R.drawable.left_menu_not_selected);
            menu1.setBackgroundResource(R.drawable.left_menu_not_selected);
        }
    }

    /**
     * 触发焦点跟随（viewpager的item）
     * @param param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final FocusFollowImageView param) {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(param, getActivity(),getActivity());
                changeFocusView(((Main2Activity) getActivity()).focusLayer, r.getX(), r.getY(), r.getW(), r.getH());
            }
        }, 300);
    }

    /**
     * 触发焦点跟随（viewpager的item）
     * @param param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final FocusFollowImageView2 param) {
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(param, getActivity(),getActivity());
                changeFocusView(((Main2Activity) getActivity()).focusLayer, r.getX(), r.getY(), r.getW(), r.getH());
                param.startGradient();
            }
        }, 300);
    }

    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    /**
     * 执行焦点跟随的动画，其实所谓的焦点跟随并不是真的焦点，而是有一个view移到正确的位置并且调整正确的大小
     * @param focusLayer
     * @param x
     * @param y
     * @param w
     * @param h
     */
    private void changeFocusView(final View focusLayer, final int x, final int y, final int w, final int h) {
        final ViewPositionAndSizeGetter.Result r = new ViewPositionAndSizeGetter().calculate(focusLayer, getActivity(),getActivity());
        ValueAnimator va = ValueAnimator.ofInt(r.getX(), x);
        va.setTarget(focusLayer);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fra = animation.getAnimatedFraction();
                int nx = new BigDecimal((x - r.getX()) * fra).intValue();
                int ny = new BigDecimal((y - r.getY()) * fra).intValue();
                int nw = new BigDecimal((w - r.getW()) * fra).intValue();
                int nh = new BigDecimal((h - r.getH()) * fra).intValue();
               // Debug.i(Fragment1.class, nx + " " + ny + " " + nw + " " + nh);

                RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) focusLayer.getLayoutParams();
                rp.topMargin = r.getY() + ny;
                rp.leftMargin = r.getX() + nx;
                rp.width = r.getW() + nw;
                rp.height = r.getH() + nh;
                focusLayer.setLayoutParams(rp);
            }
        });
        va.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        va.start();
    }
}
