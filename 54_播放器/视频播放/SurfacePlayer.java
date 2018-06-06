
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Formatter;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by wx on 2018/5/17.
 */
public class SurfacePlayer extends SurfaceView implements SurfaceHolder.Callback, MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnInfoListener, View.OnKeyListener, MediaPlayer.OnErrorListener {
    private TexturePlayerEvent playerEvent;
    /**
     * 包裹texture的view，用相对布局不错，比tv最好大一点点。用于显示焦点
     */
    private View containerView = null;

    private boolean focused = false;

    private MediaController tempMc = null;

    private Context context = null;

    private SurfaceHolder mSurfaceHolder = null;

    public TexturePlayer.State mState = TexturePlayer.State.non;

    private MediaPlayer mMediaPlayer;

    private Surface surface;

    @Deprecated
    public MediaController mc;

    /**
     * 记录播放的url用于探针分析
     */
    public String playingUrl = null;
    /**
     * pare状态
     */
    public boolean isPreprareOk = false;

    /**
     * 记录一下视频的总体长度，用于探针分析
     */
    public long totalDurationProbe = -1;

    public LinearLayout mPlayerController = null;

    public static SurfacePlayer _this;

    public void _init(Context ctx, TexturePlayerEvent playerEvent, View view, MediaController mc) {
        _this = this;
        mSurfaceHolder = getHolder();
        getHolder().addCallback(this);
        this.context = ctx;
        this.playerEvent = playerEvent;
        containerView = view;
        tempMc = mc;
    }

    public void setmPlayerController(LinearLayout mPlayerController) {
        this.mPlayerController = mPlayerController;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            focused = true;
            containerView.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            focused = false;
            containerView.setBackgroundColor(getResources().getColor(R.color.ttfh_main_dark_grey));
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (focused && KeyEvent.ACTION_DOWN == keyEvent.getAction()) {
            switch (keyEvent.getKeyCode()) {
//                case KeyEvent.KEYCODE_DPAD_UP://向上
//                    Debug.iwx(MainActivity.class, "－－－－－向上－－－－－");
//                    break;
//                case KeyEvent.KEYCODE_DPAD_DOWN://向下
//                    Debug.iwx(MainActivity.class, "－－－－－向下－－－－－");
//                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT://向左
                    if (_this != null && mPlayerController != null && isPreprareOk) {
                        mPlayerController.setVisibility(View.VISIBLE);
                        h.removeMessages(MSG_HIDE_PLAYER_CONTROLLER);
                        h.sendEmptyMessageDelayed(MSG_HIDE_PLAYER_CONTROLLER, MSG_HIDE_PLAYER_CONTROLLER_DELAY);
                        showNowAndDuration();
                        moveTime(-5 * 1000);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT://向右
                    if (_this != null && mPlayerController != null && isPreprareOk) {
                        mPlayerController.setVisibility(View.VISIBLE);
                        h.removeMessages(MSG_HIDE_PLAYER_CONTROLLER);
                        h.sendEmptyMessageDelayed(MSG_HIDE_PLAYER_CONTROLLER, MSG_HIDE_PLAYER_CONTROLLER_DELAY);
                        showNowAndDuration();
                        moveTime(5 * 1000);
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER://确定
                    Debug.log4op(getClass(), "－－－－－确定－－－－－");
//                    if (mc != null) {
//                        mc.show();
//                    }
                    if (_this != null && mPlayerController != null && isPreprareOk) {
                        mPlayerController.setVisibility(View.VISIBLE);
                        h.removeMessages(MSG_HIDE_PLAYER_CONTROLLER);
                        h.sendEmptyMessageDelayed(MSG_HIDE_PLAYER_CONTROLLER, MSG_HIDE_PLAYER_CONTROLLER_DELAY);
                        showNowAndDuration();
                        pauseOrStart();
                    }
                    break;
//                case KeyEvent.KEYCODE_BACK://返回
//                    Debug.iwx(MainActivity.class, "－－－－－返回－－－－－");
//                    break;
//                case KeyEvent.KEYCODE_HOME://房子
//                    Debug.iwx(MainActivity.class, "－－－－－房子－－－－－");
//                    break;
//                case KeyEvent.KEYCODE_MENU://菜单
//                    Debug.iwx(MainActivity.class, "－－－－－菜单－－－－－");
//                    break;
            }
        }
        return false;
    }

    private void pauseOrStart() {
        //Toast.makeText(context, "pauseOrStart", Toast.LENGTH_SHORT).show();
        ImageView iv = mPlayerController.findViewById(R.id.play_controller_play_pause);
        int status = iv.getTag(R.id.view_tag_content_id) == null ? 1 : (Integer) iv.getTag(R.id.view_tag_content_id);
        if (status == 1) {
            iv.setTag(R.id.view_tag_content_id, 0);
            iv.setImageResource(R.mipmap.play);
            _pause();
        } else {
            iv.setTag(R.id.view_tag_content_id, 1);
            //mMediaPlayer.start();
            start();
            iv.setImageResource(R.mipmap.pause);
        }
    }

    private void moveTime(int i) {
        //+-10s
        if (mMediaPlayer.getCurrentPosition() + i > 0 && mMediaPlayer.getCurrentPosition() + i < mMediaPlayer.getDuration()) {
            seekTo(mMediaPlayer.getCurrentPosition() + i);
        }
    }

    private void showNowAndDuration() {
        Debug.log4op(getClass(), "showNowAndDuration:" + mMediaPlayer.getCurrentPosition());
        h.removeMessages(MSG_SHOW_CURRENT);
        TextView tv = mPlayerController.findViewById(R.id.play_controller_now_tv);
        tv.setText(stringForTime(mMediaPlayer.getCurrentPosition()));

        ProgressBar pb = mPlayerController.findViewById(R.id.play_controller_progressBar);
        int position = mMediaPlayer.getCurrentPosition();
        int duration = mMediaPlayer.getDuration();
        if (pb != null) {
            if (duration > 0) {
                // use long to avoid overflow
               // long pos = 1000L * position / duration;
               // pb.setProgress((int) pos);
                pb.setMax(mMediaPlayer.getDuration());
                pb.setProgress(position);
            }
            //     int percent = mPlayer.getBufferPercentage();
//            pb.setSecondaryProgress(percent * 10);
        }
        h.sendEmptyMessageDelayed(MSG_SHOW_CURRENT, MSG_SHOW_CURRENT_DELAY);

    }
//---------------------------------------------------

    /**
     * 用户点击的时候进行鉴权,
     *
     * @param dataSource 这个其实不是播放的url，而是鉴权的cid和supercid，鉴权以后才会播放
     */
    public void _playVideo(String dataSource) {
        //dataSource = dataSource + "/5.mp4";
        Debug.log4op(getClass(), "Context:" + context + "\n_playVideo:" + dataSource);
        //Intent it = H5YanHuangOrderActivity.Companion.createIntent(context, "userid", "usertoken", "channelId", "productDescription", "productlist", "contentId", "backurl", "mac", dataSource);
//        if (false) {//这个判断为true的意思就是api判定不通过 福建的流程
//            //呼出购买的页面
//            Intent it = H5YanHuangOrderActivity.Companion.createIntent(context, "userid", "usertoken", "channelId", "productDescription", "productlist", "contentId", "backurl", "mac", dataSource);
//            ((AppCompatActivity) context).startActivityForResult(it, H5YanHuangOrderActivity.Companion.getREQ_CODE_ORDER());
//            //会调用这个接口的页面有如下
//            /*
//             *com.teetaa.fblib.activity.MainActivity@42109f28
//             *Context:com.teetaa.fblib.activity.InformationDetailsActivity@41fdad70
//             *Context:com.teetaa.fblib.activity.PlayerActivity@426cad68
//             */
//            return;
//        }
        CSON cson = new CSON(dataSource);
        String cid = cson.getSpecificType("cid", String.class);
        String supercid = cson.getSpecificType("supercid", String.class);
        Probes.videoTitle = cson.getSpecificType("videoTitle", String.class);

//        cid = "1526035495435";
//        supercid = "";
//        final String _dataSource = "{\"supercid\":\"\",\"cid\":\"1526035495435\"}";

        final String _dataSource = dataSource;

        Observable<ResponseBody> o = ClientAPIUtil.getInstance().playAuthorize(cid, "-1", supercid, "1", "0", "1", "1");
        o.subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                String res = responseBody.string().trim();
                Debug.log4op(getClass(), "responseBody:" + res);
                CSON cson = new CSON(res);
                String returnCode = cson.getSpecificType("returncode", String.class);
                String description = cson.getSpecificType("description", String.class);
                if (returnCode.equals("0")) {
                    //鉴权成功可以播放
                    //Toast.makeText(context,"即将开始播放",Toast.LENGTH_SHORT).show();
                    String playUrl = cson.getSpecificType("urls", JSONArray.class).getJSONObject(0).getString("playurl");
                    realPlay(playUrl);
                } else {
                    //鉴权失败
                    if (returnCode.equals("117571586") || description.equals("Authorization Failed, begin Subscription.")) {
                        Intent it = SubscribeJavaActivity.createIntent(context, _dataSource, cson.getSpecificType("productlist", JSONArray.class).getJSONObject(0).getString("price"));
                        ((AppCompatActivity) context).startActivityForResult(it, H5YanHuangOrderActivity.Companion.getREQ_CODE_ORDER());
                    } else {
                        Toast.makeText(context, "视频无法播放", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Debug.log4op(getClass(), "Throwable:" + throwable.getMessage());
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                Debug.log4op(getClass(), "on complete net request");
            }
        });

        //播放流程
        if (mMediaPlayer == null) {
            return;
        }
    }

    public void realPlay(String dataSource) {
        if(true){
            return;
        }
        //dataSource = "http://39.134.213.85/wh7f454c46tw3656949904_576458232/iptv.cdn.ha.chinamobile.com/88888888/16/20180511/269750697/269750697.ts/index.m3u8?rrsip=111.7.174.11&fmt=ts2hls&servicetype=0&icpid=&accounttype=1&limitflux=-1&limitdur=-1&accountinfo=:20180517175900,1573896879001,119.39.248.41,20180517175900,1526035495435,F0B0C486872682464DE113A023D00EC8,,1,0,-1,,1,11811500,221,,1,END&icpid=88888888&RTS=1526551559&from=1&hms_devid=223";
        Debug.log4op(getClass(), "播放文件：" + dataSource);
        //dataSource=dataSource.substring(0,end);
//        Debug.log4op(getClass(), "播放文件：" + dataSource);
        if (playerEvent == null) {
            try {
                throw new NoPlayerEventInstanceException();
            } catch (NoPlayerEventInstanceException e) {
                StringWriter writer = new StringWriter();
                e.printStackTrace(new PrintWriter(writer));
                final String exInfo = writer.getBuffer().toString();
                Debug.log4op(getClass(), exInfo);
                e.printStackTrace();
            }
        } else {
            if (mState == TexturePlayer.State.idle) {
                mMediaPlayer.reset();
            } else if (mState == TexturePlayer.State.working) {
                //mMediaPlayer.stop();
                _stop();
                mState = TexturePlayer.State.idle;
                mMediaPlayer.reset();
            } else {
                return;
            }
            try {
                Debug.log4op(getClass(), "pv _playVideo:" + dataSource);
                h.removeMessages(MSG_PLAY_STATUS);
                h.sendEmptyMessageDelayed(MSG_PLAY_STATUS, DELAY_MS);
                playingUrl = dataSource;
                totalDurationProbe = -1;
                makeBeginProbeBean();
                mMediaPlayer.setDataSource(dataSource);
                mMediaPlayer.setOnPreparedListener(this);
                mMediaPlayer.setOnInfoListener(this);
                mMediaPlayer.setOnErrorListener(this);
                bufferingListener = new MyBufferingUpdateListener();
                mMediaPlayer.setOnBufferingUpdateListener(bufferingListener);
                mState = TexturePlayer.State.working;
                Debug.log4op(getClass(), "开始缓冲");
                isPreprareOk = false;
                mMediaPlayer.prepareAsync();
                mMediaPlayer.setOnCompletionListener(this);
            } catch (IOException e) {
                Debug.log4op(getClass(), "IOEXCEPTION");
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建开始播放探针
     */
    private void makeBeginProbeBean() {
//        AsynQueue.getInstance().add(
//                ProbeBean.makePlayStartBean(context, makeProbeParam(), 0, 0));
        String[] cv = getVC();
        PlayStatuList.Companion.getInstance().addOne(cv[1], cv[0], 1, 0, 0, context, Probes.videoTitle);
    }

    public void _pause() {
        if (mMediaPlayer == null) {
            return;
        }
        if (mState == TexturePlayer.State.working) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    Debug.log4op(getClass(), "pv _pause:" + playingUrl);
                    mMediaPlayer.pause();
                    playerEvent.preparePause(mMediaPlayer.getCurrentPosition());
                }
            } catch (Exception ex) {
            }
            Debug.log4op(getClass(), "_pause");

        }
    }

    public void _stop() {
        Debug.pv(getClass(), "pv _stop:" + playingUrl);
        h.removeMessages(MSG_HIDE_PLAYER_CONTROLLER);
        h.removeMessages(MSG_PLAY_STATUS);
        h.removeMessages(MSG_SHOW_CURRENT);
        if (mMediaPlayer == null) {
            return;
        }
        if (mState == TexturePlayer.State.working) {
            //if(mMediaPlayer.isPlaying()){
            if (playingUrl != null)
                makeStopProbeBean();
            mMediaPlayer.stop();
            mState = TexturePlayer.State.idle;
            Debug.log4op(getClass(), "_stop");
            //}
        }
    }

    /**
     * 创建停止的探针
     */
    private void makeStopProbeBean() {
//        AsynQueue.getInstance().add(
//                ProbeBean.makePlayEndBean(context, makeProbeParam(), mMediaPlayer.getCurrentPosition(), totalDurationProbe));
        String[] cv = getVC();
        PlayStatuList.Companion.getInstance().addOne(cv[1], cv[0], 3, mMediaPlayer.getCurrentPosition(), totalDurationProbe, context, Probes.videoTitle);
    }

    /**
     * 创建播放完成的探针
     */
    private void makePlayCompleteProbeBean() {
//        AsynQueue.getInstance().add(
//                ProbeBean.makePlayEndBean(context, makeProbeParam(), totalDurationProbe, totalDurationProbe));
        String[] cv = getVC();
        PlayStatuList.Companion.getInstance().addOne(cv[1], cv[0], 3, totalDurationProbe, totalDurationProbe, context, Probes.videoTitle);
    }

    public void _release() {
        try {
            if (mMediaPlayer == null) {
                return;
            }
            if (mState == TexturePlayer.State.working) {
                //if(mMediaPlayer.isPlaying()){
                //mMediaPlayer.stop();
                _stop();
                Debug.log4op(getClass(), "_release1");
                //}
            }
            mMediaPlayer.release();
            Debug.log4op(getClass(), "_release2");

        } catch (Exception e) {
        }
        mMediaPlayer = null;
        mState = TexturePlayer.State.non;
        _this = null;
    }

    public void _setPlayerControl(MediaController mc, View anchror) {
        mc.setAnchorView(anchror);
        mc.setMediaPlayer(this);
        mc.setEnabled(true);
        this.mc = mc;
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        isPreprareOk = true;
        Debug.log4op(getClass(), "开始播放!");
        //Toast.makeText(context, "开始播放", Toast.LENGTH_SHORT).show();

        mp.start();
        totalDurationProbe = mp.getDuration();
        playerEvent.prepareOK();
        if (tempMc != null)
            _setPlayerControl(tempMc, containerView);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Debug.log4op(getClass(), this + ",onCompletion");
        mState = TexturePlayer.State.idle;
        makePlayCompleteProbeBean();
        mMediaPlayer.stop();
        mMediaPlayer.setOnCompletionListener(null);
        mMediaPlayer.reset();
        playerEvent.onPlayEnd();
        isPreprareOk = false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
//             showPlayerLoading.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(),
//                    "MEDIA_INFO_BUFFERING_START ", Toast.LENGTH_SHORT).show();
            playerEvent.onInfoBufferingStart();
            // Toast.makeText(context, "start_buffering", Toast.LENGTH_SHORT).show();
            Debug.log4op(getClass(), "start_buffering");
        } else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
//            Toast.makeText(getActivity(),
//                    "MEDIA_INFO_BUFFERING_END ", Toast.LENGTH_SHORT).show();
            // 此接口每次回调完START就回调END,若不加上判断就会出现缓冲图标一闪一闪的卡顿现象
            if (mp.isPlaying()) {
                playerEvent.onInfoBufferingStart();
                //  Toast.makeText(context, "end_buffering", Toast.LENGTH_SHORT).show();
                Debug.log4op(getClass(), "end_buffering");
//                showPlayerLoading.setVisibility(View.GONE);
            }
        }
        return true;
    }

    private MyBufferingUpdateListener bufferingListener;

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    /**
     * 缓冲监听
     *
     * @author wx
     */
    private class MyBufferingUpdateListener implements
            MediaPlayer.OnBufferingUpdateListener {
        public int nowBuffering = 0;

        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            if (percent % 5 == 0) {
                // Toast.makeText(context, "buffering:" + percent, Toast.LENGTH_SHORT).show();
            }
            //Log2File.log(context, "AAA", "buffing:" + percent);
            //Debug.log4op(getClass(), "buffering:" + percent);
            nowBuffering = percent;
        }
    }

    public class NoPlayerEventInstanceException extends Exception {
    }

    //----start MediaController.MediaPlayerControl

    @Override
    public void start() {
        if (mState == TexturePlayer.State.working) {
            mMediaPlayer.start();
        }
    }

    @Override
    public void pause() {
        _pause();
    }

    @Override
    public int getDuration() {
        return mMediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        Debug.log4op(getClass(), "pv seekTo:" + pos + "," + playingUrl);
        mMediaPlayer.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        if (mMediaPlayer.isPlaying()) {
            return true;
        }
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return bufferingListener.nowBuffering;
    }

    @Override
    public boolean canPause() {
//        if (mState == State.working && mMediaPlayer.isPlaying()) {
//            return true;
//        }
//        return false;
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mMediaPlayer.getAudioSessionId();
    }
    //----end MediaController.MediaPlayerControl

    public static final int MSG_PLAY_STATUS = 1;
    public static final int DELAY_MS = 20000;
    //
    public static final int MSG_HIDE_PLAYER_CONTROLLER = 2;
    public static final int MSG_HIDE_PLAYER_CONTROLLER_DELAY = 3500;
    //
    public static final int MSG_SHOW_CURRENT = 3;
    public static final int MSG_SHOW_CURRENT_DELAY = 1000;

    public static Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_PLAY_STATUS:
                    //添加一个播放状态到队列
                    Debug.log4op(getClass(), "播放状态监听任务");
                    //makeInPlayingProbeBean();
                    //
                    h.sendEmptyMessageDelayed(MSG_PLAY_STATUS, DELAY_MS);
                    break;
                case MSG_HIDE_PLAYER_CONTROLLER:
                    if (_this != null) {
                        _this.mPlayerController.setVisibility(View.GONE);
                    }
                    break;
                case MSG_SHOW_CURRENT:
                    if (_this != null) {
                        _this.showNowAndDuration();
                        h.sendEmptyMessageDelayed(MSG_SHOW_CURRENT, MSG_SHOW_CURRENT_DELAY);
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    /**
     * 创建播放中的探针bean
     */
    private void makeInPlayingProbeBean() {
//        AsynQueue.getInstance().add(
//                ProbeBean.makePlayingBean(context, makeProbeParam(), mMediaPlayer.getCurrentPosition(), totalDurationProbe));
        String[] cv = getVC();
        try {
            PlayStatuList.Companion.getInstance().addOne(cv[1], cv[0], 2, mMediaPlayer.getCurrentPosition(), totalDurationProbe, context, Probes.videoTitle);
        } catch (Exception ex) {
            PlayStatuList.Companion.getInstance().addOne(cv[1], cv[0], 2, 0, totalDurationProbe, context, Probes.videoTitle);
        }
    }

//    /**
//     * @return
//     * @deprecated
//     */
//    private String makeProbeParam() {
//        String ispName = BasicInfoAPI.getInstance().getIspNameForProbe(context);
//        StringBuffer sb = new StringBuffer();
//        if (ispName.equals("jsyd")) {
//            //http://192.168.3.202/1001/vod/353/395?UserToken=1&username=1
//            String s = playingUrl.split("vod")[1];
//            s = s.substring(1, s.lastIndexOf("?"));
//            String[] ss = s.split("/");
//            sb.append("videoId=").append(ss[1]).append("&category=").append(ss[0]);
//        }
//        return sb.toString();
//    }

    /**
     * 获取视频id【1】和cid【0】
     *
     * @return
     */
    private String[] getVC() {
//        try {
//            String ispName = BasicInfoAPI.getInstance().getIspNameForProbe(context);
//            StringBuffer sb = new StringBuffer();
//            if (ispName.equals("jsyd")) {
//                //http://192.168.3.202/1001/vod/353/395?UserToken=1&username=1
//                String s = playingUrl.split("vod")[1];
//                s = s.substring(1, s.lastIndexOf("?"));
//                String[] ss = s.split("/");
//                return ss;
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return new String[]{"-1", "-1"};
        return new String[]{"-1", "-1"};
    }
    //==============================================

//    public enum State {
//        non, idle, working//{还没初始化好，就三个状太没有播放就是idle，缓冲暂停什么的都是working}
//    }

    public interface TexturePlayerEvent {
        /**
         * 播放器完成初始化，可以开始播放了
         */
        public void initOK();

        /**
         * onInfo接口，暂停播放，MEDIA_INFO_BUFFERING_START
         */
        public void onInfoBufferingStart();

        /**
         * onInfo接口， 开始播放，MEDIA_INFO_BUFFERING_END
         */
        public void onInfoBufferingEnd();

        /**
         * OnBufferingUpdateListener，总文件的缓冲百分比
         */
        public void onBufferingUpdate(int percent);

        /**
         * 播放完毕
         */
        public void onPlayEnd();

        /**
         * 销毁
         */
        void destory();

        /**
         * 开始播放
         */
        void prepareOK();

        /**
         * 暂停
         *
         * @param percent 播放进度
         */
        void preparePause(long percent);
    }

    public SurfacePlayer(Context context) {
        super(context);
    }

    public SurfacePlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDisplay(surfaceHolder);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mState = TexturePlayer.State.idle;
        playerEvent.initOK();
        setOnKeyListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        playerEvent.destory();
        if (h != null) {
            h.removeMessages(MSG_PLAY_STATUS);
        }
    }

    /*
    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int i) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return false;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
    */


    StringBuilder mFormatBuilder = null;
    Formatter mFormatter = null;

    public String stringForTime(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
                    .toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }
}
