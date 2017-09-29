
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cc.m2u.ifengbigdata.R;

import java.util.Iterator;
import java.util.List;

/**
 * 定制化的底部菜单
 */
public class CustomBottomNavigation extends LinearLayout implements View.OnTouchListener {
    private Context ctx;
    private List<NavItem> items;
    private BottomNavigationItemClick listener;
    private List<RelativeLayout> rls = new ArrayList<RelativeLayout>();


    public CustomBottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        LayoutInflater.from(context).inflate(R.layout.main_bottom_nav, this, true);
    }

    public void initViews(List<NavItem> list, BottomNavigationItemClick listener) {
        this.listener = listener;
        items = list;
        Iterator<NavItem> it = list.iterator();
        int count = 1;
        while (it.hasNext()) {
            NavItem ni = it.next();
            RelativeLayout rl = (RelativeLayout) findViewById(ResourceGetter.getViewID(ctx, "mbn_" + (count)));
            rl.setTag(ni.tag);
            count++;
			rls.add(rl);
            //
            ImageView iv = (ImageView) rl.getChildAt(0);
            iv.setImageResource(ni.img4Idle);
            //
            TextView tv = (TextView) rl.getChildAt(1);
            tv.setText(ni.showName);
            tv.setTextColor(ni.color4Idle);
            //
            rl.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        RelativeLayout rl = null;
        int index = -1;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).tag.equals(v.getTag())) {
                rl = (RelativeLayout) v;
                index = i;
                break;
            }
        }
        //
        if (index != -1) {
            NavItem ni = items.get(index);
            if (event.getAction() == 1) {//松开
                ImageView iv = (ImageView) rl.getChildAt(0);
                iv.setImageResource(ni.img4Idle);
                TextView tv = (TextView) rl.getChildAt(1);
                tv.setTextColor(ni.color4Idle);
                if (isTouchPointInView(rl, (int) event.getRawX(), (int) event.getRawY())) {//松开的位置在空间内，刷新
                    listener.onBottomNavigationItemClick(ni.tag);
                }
            } else {
                ImageView iv = (ImageView) rl.getChildAt(0);
                iv.setImageResource(ni.img4Action);
                TextView tv = (TextView) rl.getChildAt(1);
                tv.setTextColor(ni.color4Action);
            }
            return true;
        }
        return false;
    }

   public void setCurrentItem(String tag) {
        for(int i=0;i<rls.size();i++){
            if(rls.get(i).getTag().equals(tag)){
                rls.get(i).setBackgroundColor(Color.parseColor("#eeeeee"));
            }else{
                rls.get(i).setBackgroundColor(Color.parseColor("#ffffff"));

            }
        }
    }
	
    public static class NavItem {
        /**
         * tag，点击事件会把这个传递回去
         */
        public String tag;
        /**
         * 文本显示的名字
         */
        public String showName;
        /**
         * 闲置的时候显示的图像
         */
        public int img4Idle;
        /**
         * 有行为的时候，显示的图像
         */
        public int img4Action;
        /**
         * 闲置的时候文本的颜色
         */
        public int color4Idle;
        /**
         * 有行为的时候文本的颜色
         */
        public int color4Action;

        /**
         * 初始化
         *
         * @param tag
         * @param showName
         * @param img4Idle
         * @param img4Action
         * @param color4Idle
         * @param color4Action
         */
        public NavItem(String tag, String showName, int img4Idle, int img4Action, int color4Idle, int color4Action) {
            this.tag = tag;
            this.showName = showName;
            this.img4Idle = img4Idle;
            this.img4Action = img4Action;
            this.color4Idle = color4Idle;
            this.color4Action = color4Action;
        }
    }

    public interface BottomNavigationItemClick {
        void onBottomNavigationItemClick(String tag);
    }

    /**
     * 用于判断时间是否在控件内触发
     *
     * @param view
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }
}
