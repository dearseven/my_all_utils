
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

/**
 * 播放页面
 */
public class PlayerActivity extends AppCompatActivity implements SurfacePlayer.TexturePlayerEvent {
    public static final int FINISH_WHEN_PLAY_END = 0;
    public static final int FINISH_WHEN_USER_BACK = 1;
    public static final String FINISH_WHEN_USER_BACK_CURR_POSITION_KEY = "FINISH_WHEN_USER_BACK_CURR_POSITION_KEY";

    public static final String SP_NAME = "sp_main";
    /**
     * 进入播放页面的类型
     */
    public static final String IN_WAY_TYPE = "in_type";
    /**
     * 从双列表进入
     */
    public static final int IN_WAY_DOUBLE = 1;

    /**
     * 从主页的精选页面的4个推荐位1个专题位中的某一个打开这个页面
     */
    public static final int IN_WAY_MAIN_ITEM_PROG_N_SPEC = 2;
    /**
     * 从其他入口进入播放
     */
    public static final int IN_WAY_OTHER = 0;
    /**
     * 双列表页回调
     */
    public static final int RESULT_DOUBLE_ID = 101;
    /**
     * 专题页回调
     */
    public static final int RESULT_ID = 100;
    /**
     * 播放器
     */
    private SurfacePlayer mVideo;
    /**
     * 播放器上面这层图片
     */
    private ImageView topIv;
    /**
     * 播放器是否初始化完成
     */
    private boolean screenCreateOK = false;
    /**
     * 从其它入口进入数据
     */
    List<Episode> episodes = new ArrayList<Episode>();
    /**
     * 从双列表进入数据(取消了)，
     * 从主页的精选页面的4个推荐位1个专题位中的某一个打开这个页面
     */
    List<IndexProgListItem> integerNames = new ArrayList<IndexProgListItem>();
    /**
     * 播放列表下标
     */
    int index = 0;
    /**
     * 播放条目进度
     */
    long timepoint = 0;
    /**
     * 栏目ID
     *
     * @deprecated
     */
    String progId;
    /**
     * 是否在网络交互
     */
    public boolean isLoad = false;
    /**
     * 是否要结束,用户back
     */
    public boolean isFinsh = false;
    /**
     * 入口类型
     */
    private int inType;
    /**
     * 播放进度
     */
    public long currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenCreate(ppp);
        setContentView(R.layout.ttfh_activity_test_video_view);
        inType = getIntent().getIntExtra(IN_WAY_TYPE, IN_WAY_OTHER);
        //从哪里进来的
        if (inType == IN_WAY_DOUBLE) {
            integerNames = getIntent().getParcelableArrayListExtra("play_list");
        } else if (inType == IN_WAY_MAIN_ITEM_PROG_N_SPEC) {///
            integerNames = getIntent().getParcelableArrayListExtra("play_list");
        } else {
            episodes = getIntent().getParcelableArrayListExtra("play_list");
            integerNames = ClassConvert.Companion.convertEpisodesToIndexProgListItems(episodes);
        }
        index = getIntent().getIntExtra("index", 0);
        timepoint = getIntent().getLongExtra("timepoint", 0);
        progId = getIntent().getStringExtra("progId");
        mVideo = (SurfacePlayer) findViewById(R.id.ttfh_video1);
        mVideo._init(this, this, findViewById(R.id.ttfh_video1_parent), new MediaController(this));
        mVideo.setmPlayerController((LinearLayout) findViewById(R.id.play_controller));
        topIv = (ImageView) findViewById(R.id.show_fragment_top_iv);
        topIv.setVisibility(View.VISIBLE);
        h.sendEmptyMessageDelayed(MSG_TRY_PLAYER_INITED, 1000);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        Debug.log4op(getClass(),"dispatchKeyEvent 1");
//        switch (event.getKeyCode()) {
//            case KeyEvent.KEYCODE_DPAD_CENTER:
//            case KeyEvent.KEYCODE_ENTER://确定
//                Debug.log4op(getClass(),"dispatchKeyEvent 2 ");
//                if (mVideo != null) {
//                    if (mVideo.mc != null) {
//                        mVideo.mc.show(5000);
//                    }
//                }
//                break;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    private final int MSG_TRY_PLAYER_INITED = 1;
    Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TRY_PLAYER_INITED:
                    if (screenCreateOK) {
                        Debug.iwx(getClass(), "screenCreateok!");
                        //#http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8
                        // mVideo._playVideo("http://223.82.250.3:8080/ysten-businessmobile/live/fhchinese/dujuejiami.m3u8");
                        //mVideo._playVideo("http://120.24.80.215:80/20170507/hfh/1.ts");
                        //TODO 记得改地址
                        String url = SomeUtil.getPlayUrl(integerNames, index, PlayerActivity.this);
                        Debug.iwxdbapi(getClass(), "playVideo:" + url);
                        mVideo._playVideo(url);
                        // mVideo._playVideo("http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4");
                        //跳转到上次播放的位置
                    } else {
                        Debug.iwx(getClass(), "!!!!not screenCreateok ,");
                        h.sendEmptyMessageDelayed(MSG_TRY_PLAYER_INITED, 1500);
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenDestroy(ppp);
        if (mVideo != null)
            mVideo._release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenResume(ppp);
    }

    @Override
    public void onPause() {
        super.onPause();
        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenPause(ppp);
        isLoad = true;
        mVideo._pause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenStart(ppp);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Probes.ProbeParam ppp = new Probes.ProbeParam();
        ppp.context = this;
        ppp.probeClassName = getClass().getSimpleName();
        Probes.whenStop(ppp);
    }

    @Override
    public void initOK() {
        Debug.iwx(getClass(), "init ok!");
        screenCreateOK = true;
    }

    @Override
    public void onInfoBufferingStart() {

    }

    @Override
    public void onInfoBufferingEnd() {

    }

    @Override
    public void onBufferingUpdate(int percent) {

    }

    //private int _index=-1;
    @Override
    public void onPlayEnd() {
//        if(_index==index){
//            return;
//        }
        topIv.setVisibility(View.VISIBLE);
        preparePause(mVideo.getDuration());
        if (inType == IN_WAY_DOUBLE) {
            if (index < integerNames.size() - 1) {
                // _index=index;
                //这里主要是用来记录播放完毕
                index += 1;
                String url = SomeUtil.getPlayUrl(integerNames, index, this);
                mVideo._playVideo(url);
                /// / mVideo._playVideo("http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4");
            } else {
                initFinsh();
            }
        } else {
            if (index < episodes.size() - 1) {
                //_index=index;
                index += 1;
                String url = SomeUtil.getPlayUrl(integerNames, index, this);
                mVideo._playVideo(url);
                //mVideo._playVideo("http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4");
            } else {
                initFinsh();
            }
        }
    }

    @Override
    public void prepareOK() {
        topIv.setVisibility(View.GONE);
        if (timepoint != 0) {
            mVideo.seekTo(new Long(timepoint).intValue());
            timepoint = 0;
        }

    }

    @Override
    public void preparePause(long percent) {
        currentPosition = percent;
        Debug.i(getClass(), "playactivity上载进度：" + currentPosition);
//        if (inType == IN_WAY_DOUBLE) {
//            if (DataAccquiresAPIs.getInstance().saveLastPoint(BasicInfoAPI.getInstance().getUserId(PlayerActivity.this), BasicInfoAPI.getInstance().getIspId(PlayerActivity.this), progId, integerNames.get(index).getCategoryId(), percent / 1000)) {
//                // Toast.makeText(this,"上传进度成功："+percent/1000,Toast.LENGTH_SHORT).show();
//            } else {
//                // Toast.makeText(this,"上传失败："+percent/1000,Toast.LENGTH_SHORT).show();
//            }
//        } else {
        //  if (DataAccquiresAPIs.getInstance().saveLastPoint(BasicInfoAPI.getInstance().getUserId(PlayerActivity.this), BasicInfoAPI.getInstance().getIspId(PlayerActivity.this), progId, episodes.get(index).programId, percent / 1000)) {
        //      // Toast.makeText(this,"上传进度成功："+percent/1000,Toast.LENGTH_SHORT).show();
        //  } else {
        //       // Toast.makeText(this,"上传失败："+percent/1000,Toast.LENGTH_SHORT).show();
        //     }
        //  }

        if (inType == IN_WAY_DOUBLE) {
            String uid = HttpConfig.USER_NAME;//BasicInfoAPI.getInstance().getUserId(PlayerActivity.this);
            String isp = HttpConfig.ISP_ID;//BasicInfoAPI.getInstance().getIspId(PlayerActivity.this);
            DataAccquiresAPIs api = DataAccquiresAPIs.getInstance();
            api.saveLastPoint(uid, isp, integerNames.get(index).programId, integerNames.get(index).getDataId(), percent / 1000);
        } else if (inType == IN_WAY_MAIN_ITEM_PROG_N_SPEC) {///
//            String uid = BasicInfoAPI.getInstance().getUserId(PlayerActivity.this);
//            String isp = BasicInfoAPI.getInstance().getIspId(PlayerActivity.this);
//            DataAccquiresAPIs api = DataAccquiresAPIs.getInstance();
//            api.saveLastPoint(uid, isp, progId, integerNames.get(index).getCategoryId(), percent / 1000);
            String uid = HttpConfig.USER_NAME;// BasicInfoAPI.getInstance().getUserId(PlayerActivity.this);
            String isp = HttpConfig.ISP_ID;//BasicInfoAPI.getInstance().getIspId(PlayerActivity.this);
            DataAccquiresAPIs api = DataAccquiresAPIs.getInstance();
            api.saveLastPoint(uid, isp, integerNames.get(index).programId, integerNames.get(index).getDataId(), percent / 1000);
        } else {
            String uid = HttpConfig.USER_NAME;//BasicInfoAPI.getInstance().getUserId(PlayerActivity.this);
            String isp = HttpConfig.ISP_ID;//BasicInfoAPI.getInstance().getIspId(PlayerActivity.this);
            DataAccquiresAPIs api = DataAccquiresAPIs.getInstance();
            api.saveLastPoint(uid, isp, integerNames.get(index).programId, integerNames.get(index).getDataId(), percent / 1000);
        }

        isLoad = false;
        if (isFinsh == true) {
            finishWhenUserBack();
        }
    }

    /**
     * 结束页面  要结束的结束 要回调的回调
     */
    private void initFinsh() {
        finishWhenPlayEnd();
    }

    @Override
    public void destory() {
        screenCreateOK = false;
    }

    @Override
    public void onBackPressed() {
        //Debug.log4op(getClass(),"onBackPressed isLoad="+isLoad+","+mVideo.mState.name());
        if (isLoad && mVideo.mState == TexturePlayer.State.working) {
            //  Toast.makeText(this,"1",Toast.LENGTH_SHORT).show();
            isFinsh = true;
            finishWhenUserBack();
        } else {
            //Toast.makeText(this,"2",Toast.LENGTH_SHORT).show();
            finishWhenUserBack();
        }
    }

    /**
     * 完成播放
     */
    public void finishWhenPlayEnd() {
        Intent intt = new Intent();
        intt.putExtra("index", index);
        setResult(FINISH_WHEN_PLAY_END, intt);
        //setResult(RESULT_OK, intt);
        finish();
    }

    /**
     * 这个是在双列表的时候用到，因为涉及到继续播
     */
    public void finishWhenUserBack() {
        Intent intt = new Intent();
        intt.putExtra("index", index);
        if (isFinsh) {
            intt.putExtra(FINISH_WHEN_USER_BACK_CURR_POSITION_KEY, timepoint);
        } else {
            intt.putExtra(FINISH_WHEN_USER_BACK_CURR_POSITION_KEY, mVideo.getCurrentPosition());
        }
        setResult(FINISH_WHEN_USER_BACK, intt);
        //setResult(RESULT_OK, intt);
        finish();
    }

    @Override
    public void finish() {
        boolean isPlaying = false;
        if (mVideo != null) {
            try {
                isPlaying = mVideo.isPlaying();
                if (isPlaying) {
                    preparePause(mVideo.getCurrentPosition());
                }
            } catch (IllegalStateException ex) {
                mVideo._stop();
                mVideo = null;
            }
        }
        if (isPlaying) {
            mVideo._stop();
            mVideo._release();
        }
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == H5YanHuangOrderActivity.Companion.getREQ_CODE_ORDER()) {
            OrderParams op = data.getParcelableExtra("OrderParams");
            boolean suc = data.getBooleanExtra("suc", false);
            if (suc) {
                Debug.log4op(getClass(), "回调订购结果2," + mVideo + ",播放:" + op.rawPlayInfo);
                mVideo._playVideo(op.rawPlayInfo);
            } else {
                Debug.log4op(getClass(), "订购失败啊");
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
