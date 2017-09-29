
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.*;


/**
 * 自定义没有数据的时候的重新刷新view,使用的时候高宽都用match_parent
 * Created by wx on 2017/9/4.
 */
public class CustomRefreshView extends LinearLayout implements View.OnTouchListener {
	boolean isAnimating = false;
	public static long ANIMATION_DURATION=1000;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.crl_refresh_img_area) {
            if (event.getAction() == 1) {//松开
                // bulb.setImageResource(R.drawable.light_bulb);
                refreshArrow.setImageResource(R.drawable.logo_refresh);
                if (isTouchPointInView(refreshImgArea, (int) event.getRawX(), (int) event.getRawY()) || isTouchPointInView(refreshButton, (int) event.getRawX(), (int) event.getRawY())) {//松开的位置在空间内，刷新
                    if (isEnable&&isAnimating == false) {
                        startAnimate();
                    }
                }
            } else {
                // bulb.setImageResource(R.drawable.light_bulb_blue);
                refreshArrow.setImageResource(R.drawable.logo_refresh);
            }
            return true;
        }
        return false;
    }

    /**
     * 当触发刷新动画的时候，会回调当前接口
     */
    public interface CustomRefreshEvent {
        void whenUserClick();
    }

    public void setCustomRefreshEvent(CustomRefreshEvent listener) {
        this.listener = listener;
    }

    private CustomRefreshEvent listener = null;
    private RelativeLayout refreshImgArea = null;
    private ImageView bulb = null;
    private ImageView refreshArrow = null;
    private Button refreshButton = null;
    private ValueAnimator va = null;
    /**
     * 是否可以触发转动和回调
     */
    private boolean isEnable = true;

    /**
     * 设置是否可以触发转动和回调
     *
     * @param enable
     */
    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public CustomRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_refresh_layout, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refreshImgArea = (RelativeLayout) findViewById(R.id.crl_refresh_img_area);
        bulb = (ImageView) findViewById(R.id.crl_bulb);
        refreshArrow = (ImageView) findViewById(R.id.crl_refresh_arrow);
        refreshButton = (Button) findViewById(R.id.crl_refresh_btn);

        //设置一个触摸事件
        refreshImgArea.setOnTouchListener(this);
        //设置一个按钮事件
        refreshButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEnable) {
                    startAnimate();
                }
            }
        });
    }


    public void stopAnimate() {
        if (va != null) {
            va.cancel();
			va=null;
        }
    }

    private void startAnimate() {
		isAnimating = true;
        if (va == null) {
            va = ValueAnimator.ofFloat(360f, -1);
            //va.setInterpolator(new LinearInterpolator());
            va.setDuration(ANIMATION_DURATION);
            va.setRepeatMode(Animation.RESTART);
            va.setRepeatCount(Animation.INFINITE);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float r = (float) animation.getAnimatedValue();
                    if (r < 0) {
                        r = 0;
                    }
                    refreshArrow.setRotation(r);
                }
            });
			va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isAnimating = false;
					if (listener != null) {
						listener.whenUserClick();
					}
                }
            });
            va.start();
        }
      
    }


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
	
	 public void onlyStartAnimate() {
		isAnimating = true;
        if (va == null) {
            va = ValueAnimator.ofFloat(360f, -1);
            //va.setInterpolator(new LinearInterpolator());
            va.setDuration(ANIMATION_DURATION);
            va.setRepeatMode(Animation.RESTART);
            va.setRepeatCount(Animation.INFINITE);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float r = (float) animation.getAnimatedValue();
                    if (r < 0) {
                        r = 0;
                    }
                    refreshArrow.setRotation(r);
                }
            });
            va.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    isAnimating = false;
                }
            });
            va.start();
        }
    }
	
	  public void setText(String str) {
        refreshButton.setText(str);
    }
}