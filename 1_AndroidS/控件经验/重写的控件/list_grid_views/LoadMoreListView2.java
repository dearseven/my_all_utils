
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;


/**
 * 上拉加载更多,其实在这个里没有用到mFootView
 * Created by wx on 2017/7/24.
 */
public class LoadMoreListView2 extends ListView implements AbsListView.OnScrollListener {
    private View mFootView;
    private int mTotalItemCount;//item总数
    private OnLoadMoreListener mLoadMoreListener;
    private boolean mIsLoading = false;//是否正在加载

    public void setmLoadMoreListener(OnLoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onloadMore();

        void loadMoreAnimation(boolean trueIsShow);
    }

    public LoadMoreListView2(Context context) {
        super(context);
        init(context);
    }

    public LoadMoreListView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public LoadMoreListView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mFootView = LayoutInflater.from(context).inflate(R.layout.load_more_list_foot_view, null);
        setOnScrollListener(this);
    }


    @Override
    public void onScrollStateChanged(AbsListView listView, int scrollState) {
        // 滑到底部后，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        int lastVisibleIndex = listView.getLastVisiblePosition();
        if (!mIsLoading && scrollState == OnScrollListener.SCROLL_STATE_IDLE//停止滚动
                && lastVisibleIndex == mTotalItemCount - 1) {//滑动到最后一项
            mIsLoading = true;
            //addFooterView(mFootView);
            if (mLoadMoreListener != null) {
                mLoadMoreListener.loadMoreAnimation(true);
                mLoadMoreListener.onloadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
    }

    /**
     * loadmore完成，调用这个方法隐藏动画
     */
    public void setLoadCompleted() {
        mIsLoading = false;
        //removeFooterView(mFootView);
        if (mLoadMoreListener != null) {
            mLoadMoreListener.loadMoreAnimation(false);
        }
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
