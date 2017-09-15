
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cc.m2u.ifengbigdata.R;
import cc.m2u.ifengbigdata.activity.base.BaseCompatActivity;

public abstract class BaseLazyFragment extends Fragment {
    protected View selfView = null;
    private String currentFragmentTag;

    /**
     * 是否被hasInited
     */
    public boolean hasInited = false;

    /**
     * fragment根据自己是否被hasInited，然后决定是否执行initViews
     */
    public abstract void initViews();

    /**
     * 这个是fragment被切换到的时候，外部调用的
     */
    public abstract void intoFragment();

    /**
     * fragment所在的activity可以吧back派发到当前的fragment，让fragment决定是不是要拦截来执行自己的行为
     *
     * @param obj
     * @return
     */
    public abstract boolean onBackFilter(Object obj);

    /**
     * 返回布局的id
     *
     * @return
     */
    public abstract int getLayoutId();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        selfView = inflater.inflate(getLayoutId(), container, false);
        return selfView;
    }

    /**
     * 切换fragment,这个是如果fragment已经有了则只会替换，不会再次初始化
     *
     * @param appCompatActivity
     * @param containerId 容器
     * @param claz 准备显示的fragment的类
     * @param toFragment 准备显示的fragment的tag
     */
    private void changeFragment(AppCompatActivity appCompatActivity,int containerId, Class claz, String toFragment) {
        FragmentManager fragmentManager=appCompatActivity.getSupportFragmentManager();
        FragmentTransaction transaction =fragmentManager.beginTransaction();
        Fragment from = fragmentManager.findFragmentByTag(currentFragmentTag);
        if (from != null) {//如果找到了被替换的fragment，证明已经初始化了（其实后面的证明不重要）
            Fragment to = fragmentManager.findFragmentByTag(toFragment);
            if (to == null) {//如果找不到要显示的fragment，则初始化一个，
                Object obj = null;
                try {
                    obj = claz.newInstance();
                    to = (Fragment) obj;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                transaction.add(containerId,to, toFragment);//加载到transaction
            }
            transaction.hide(from).show(to).commit();//切换
        } else {//要被替换的fragment都找不到 就是第一次初始化啦，所以就直接初始化一个加进去
            Object obj = null;
            Fragment to = null;
            try {
                obj = claz.newInstance();
                to = (Fragment) obj;
            }  catch (Exception e) {
                e.printStackTrace();
            }
            transaction.add(containerId,to, toFragment).show(to).commit();
        }
        currentFragmentTag=toFragment;
    }
}
