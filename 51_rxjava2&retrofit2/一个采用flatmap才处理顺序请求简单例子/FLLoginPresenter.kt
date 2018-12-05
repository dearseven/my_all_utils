
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.BiConsumer
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import org.json.JSONObject
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class FLLoginPresenter : IFLLoginPresenter {
    /**
     * 标记跳转入口 <br></br>
     * 当flag=0的时候，表示是从启动页接受微信闹钟进入的该页面<br></br>
     * 从 alarmsettingFragment分享闹钟流程跳转过来的时候 flag=1<br></br>
     * 通过Navigator的头像点进来的，flag=2 切换帐号flag=3 or isLoginUseAccoutSwitcher=true
     */
    private var flag = 99
    /**
     * 邀请人的call号，只有是接受了微信的call友邀请，但是没有登录的时候，这个值就不为空，且是一个有效call号
     */
    private var inviter: String? = null
    private var message: String? = null
    //是不是点击了任何一种登陆
    private var isOnClick = false
    //
    private val manager: CompositeDisposable = CompositeDisposable()
    private var v: FLLoginView? = null
    private var appCtx: Context? = null
    private var activity: FLLoginActivity? = null;
    private val loginBean by lazy {
        FLLoginBean();
    }


    constructor(v: FLLoginView, activity: FLLoginActivity) {
        this.v = v
        this.activity=activity
        this.appCtx = activity.applicationContext
        //
        // 获取要分享的voice的字符串
        // inviter = getIntent().getStringExtra("inviter");
        flag = activity.getIntent().getIntExtra("flag", 99)
        // 从启动页面进入该页面
        if (flag == 0) {
            message = activity.getIntent().getStringExtra("message")
        }
        inviter = activity.getIntent().getStringExtra("inviter")
    }

    override fun release() {
        manager!!.clear()
        appCtx = null
        activity = null;
    }

    override fun usePwdLogin() {
        if (loginBean.loginType == 1) return
        loginBean.loginType = 1
        loginBean.ifPwdShow = 0
        //修改ui
        v!!.usePwdLogin()
    }

    override fun useCodeLogin() {
        if (loginBean.loginType == 2) return
        loginBean.loginType = 2
        loginBean.ifPwdShow = 0
        //修改ui
        v!!.useCodeLogin()
    }

    override fun changePwdVisible() {
        loginBean.ifPwdShow = if (loginBean.ifPwdShow == 0) 1 else 0
        v!!.changePwdVisible(loginBean.ifPwdShow == 1)//true可见
    }

    override fun login(context: Context, name: String, pwdOrCode: String) {
        //1 判断网络
        if (NetWorkTester.getNetState(context) == NetWorkTester.NO_INTERNET) {
            v!!.loginResult(1, "")//没有网络
            return
        }
        //2 做简单的字符串检查
        if (name.trim().isEmpty()) {
            v!!.loginResult(4, "")//用户名为空
            return
        }
        if (pwdOrCode.trim().isEmpty()) {
            if (loginBean.loginType == 1) {
                v!!.loginResult(5, "")//密码为空
                return
            } else if (loginBean.loginType == 2) {
                v!!.loginResult(6, "")//验证码为空
                return
            }
        }
        //3 判断登陆类型来登陆
        isOnClick = true
        if (loginBean.loginType == 1) {
            loginByPwd(context, name, pwdOrCode)
        } else if (loginBean.loginType == 2) {
            //没实现呢
        }
    }

    override fun loginWX() {
        //不在这里改,因为用户可能在微信里取消授权登陆,所以应该在微信的回调里做才对
        //loginBean.loginType = 3
    }

    //--------------------private functions---------------------------
    private fun loginByPwd(context: Context, name: String, pwdOrCode: String) {
        v?.showLoading()
        val loginObservable: Observable<ResponseBody> = ClientAPIUtil.getInstance().loginWithPwd(name, pwdOrCode)

        loginObservable.flatMap(Function<ResponseBody, Observable<Map<String, String>>> {
            var map: Map<String, String> = HashMap<String, String>()
            map = BedFriendUserInfoTask.jsonToMapWithNoArray(it.string(), map)
            if (map["status"] != null && map["status"].equals("success", ignoreCase = true)) {
                //登陆未成功
                saveUserInfo(map)
                map.put("login_status", "1")
                //生成返回登陆状态的Observable,并且下载图片
                Observable.create {
                    downUserImage(map["logo"])
                    //这个Observable发送的数据是稍微加工了一下的上一个Observable(ResponseBody的数据加工map了啊)的返回值
                    it.onNext(map)
                }
            } else {
                //登陆未成功
                map.put("login_status", "0")
                //生成返回登陆状态的Observable
                //这个Observable发送的数据是稍微加工了一下的上一个Observable(ResponseBody的数据加工map了啊)的返回值
                Observable.create { it.onNext(map) }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    //下载图片
                    v?.hideLoading()
                    if (it["login_status"].equals("1")) {
                        //本来这里是打算写在doAfterNext不过发现这样就运行在子线程了
                        afterLoginSuc()
                        //这个toast到MeFenglingFragment里面执行
                        //Toast.makeText(appCtx!!, "登陆成功", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(appCtx!!, it["errmsg"], Toast.LENGTH_SHORT).show()
                    }
                }
        //其实这里可以不用这么直接subscribe({}),但其实我是故意的
//        o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Consumer<ResponseBody> {
//            override fun accept(t: ResponseBody?) {
//                DebugLog.logFl(null, "accept:${t?.string()
//                        ?: "accept is null"}", FLLoginPresenter::class.java)
//            }
//        })
//        val disposableObserver=o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<ResponseBody>() {
//            override fun onComplete() {
//
//            }
//
//            override fun onNext(value: ResponseBody?) {
//            }
//
//            override fun onError(e: Throwable?) {
//            }
//        })
//        manager.add(disposableObserver);
    }
//-----------------------------tools------------------------
    /**
     * 登陆成功以后再doAfterNext调用
     */
    private fun afterLoginSuc() {
        CustomPlayContentSubFragment().CustomMenuTask(
                activity!!, null).execute()
        addFriendWithLoginOrReg()
        val intent = Intent(activity!!,
                DeciveBindStatuService::class.java)
        activity!!.startService(intent)
        if (flag == 1) {// 从 alarmsettingFragment跳转过来的时候 flag=1
            //TODO 注销百度推送服务并重启

            if (isOnClick == true) {
                initResultShare()
            }
        } else if (flag == 0) {
            val m1 = UserInfoUtil
                    .getUserInfo(appCtx)
            // 接受微信分享流程1
            // TODO 注销百度推送服务并重启
            // if (flagisFirst == true) {
            // 生成消息数据
            val center = MsgCenterFactory
                    .makeDistance<SharedAlarmMsgCenter>("type_1")
            val nt = center!!
                    .createMsgStrWithWeChatRemoteAlarm(
                            m1[UserInfoUtil.FM_ID], "1", null, null, message)
            // 插入消息
            val amc = MsgCenterFactory
                    .makeDistance<AllMsgCenter>("type_0")
            amc!!.acceptMessageNoNotification(
                    appCtx,
                    nt)
            BedFriendUserInfoActivity6.flagIn = false
//            val intent2 = Intent(
//                    appCtx,
//                    BedFriendV9NotificationsActivity::class.java)
//            activity!!.startActivity(intent2)
            // }
        } else if (flag == 2) {
            // cyan7.1
            val m1 = UserInfoUtil
                    .getUserInfo(appCtx)
            // 接受微信分享流程1
            //TODO 注销百度推送服务并重启
            BedFriendUserInfoActivity6.flagIn = true
//            val intent2 = Intent(
//                    appCtx,
//                    BedFriendUserInfoActivity6::class.java)
//            activity!!.startActivity(intent2)
            // }
        } else {
            // cyan7.1
            val m1 = UserInfoUtil
                    .getUserInfo(appCtx)
            // 接受微信分享流程1
            //TODO 注销百度推送服务并重启
            BedFriendUserInfoActivity6.flagIn = true
//            val intent2 = Intent(
//                    appCtx,
//                    BedFriendUserInfoActivity6::class.java)
//            activity!!.startActivity(intent2)
            // }
        }
        activity!!.setResult(Activity.RESULT_OK)
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.left_in, R.anim.right_out)

    }

    private fun downUserImage(s: String?) {
        val hitPath = UserAvatarTool.hitUserAvatar(appCtx!!)
        if (hitPath == "") {
            val pref = appCtx!!
                    .getSharedPreferences(
                            BedFriendUserInfoTask.BED_FRIEND_USER_INFO_FILE,
                            Context.MODE_PRIVATE)
            val path = pref.getString("AVATAR", null)
//            Thread(Runnable {
            try {
                val url = URL(path!!.trim())
                DebugLog.logFl(null, "没找到头像，下载：" + path, javaClass);
                val connection = url
                        .openConnection()
                val _in = BufferedInputStream(
                        connection.getInputStream())
                val avatar = File(
                        appCtx!!
                                .getDir(Configure.USER_AVATAR_FOLDER,
                                        Context.MODE_PRIVATE),
                        path!!.substring(path!!
                                .lastIndexOf("/") + 1) + ".fmc")
                val os = FileOutputStream(
                        avatar)
                // 写文件
                val buf = ByteArray(1024)
                var count = 0
                do {
                    count = _in.read(buf, 0, buf.size)
                    if (count > -1)
                        os.write(buf, 0, count)
                } while (count > -1)
                os.flush()
                os.close()
                _in.close()
                DebugLog.logFl(null, "头像下载成功" + path, javaClass);
                avatar.renameTo(File(
                        appCtx!!
                                .getDir(Configure.USER_AVATAR_FOLDER,
                                        Context.MODE_PRIVATE),
                        path!!.substring(path!!
                                .lastIndexOf("/") + 1)))
            } catch (e: Exception) {
                e.printStackTrace()
                DebugLog.logFl(null, "下载失败:${e.toString()}", javaClass)
            }
//            }).start()
        }
    }

    /**
     * 网络交互完保存用户信息
     */
    private fun saveUserInfo(map: Map<String, String>) {
        if (map["status"] != null && map["status"].equals("success", ignoreCase = true)) {
            // 重写登录状态
            var pref = appCtx!!.getSharedPreferences(
                    Configure.CONFIGUR_FILE_NAME, Context.MODE_PRIVATE)

            val name = map["username"]
            val id = map["fmnumber"]

            Configure.USERNAME = name
            Configure.USERID = id

            var editor = pref.edit()
            editor.putString("USERNAME", name)
            editor.putString("USERID", id)
            editor.commit()

            pref = appCtx!!.getSharedPreferences(BedFriendUserInfoTask.BED_FRIEND_USER_INFO_FILE,
                    Context.MODE_PRIVATE)
            editor = pref.edit()
            editor.putString("AVATAR", map["logo"])
            editor.putString("USERID", id)
            editor.putString("USERNAME", name)
            editor.putString("NICKNAME", map["nickname"])
            editor.putString("SETPWD", map["setpwd"])
            editor.putString(UserInfoUtil.AGE, map["age_segment"])
            editor.putString(UserInfoUtil.GENDER, map["sex"])

            if (map["WEIXIN"] == "1") {
                editor.putString("PLATFORM_WEIXIN", map["WEIXIN"])
            } else {
                editor.remove("PLATFORM_WEIXIN")
            }
            if (map["QQ"] == "1") {
                editor.putString("PLATFORM_QQ", map["QQ"])
            } else {
                editor.remove("PLATFORM_QQ")
            }
            if (map["SINA"] == "1") {
                editor.putString("PLATFORM_SINA", map["SINA"])
            } else {
                editor.remove("PLATFORM_SINA")
            }
            editor.commit()
        }
    }

    /**
     * 如果inviter不为空，则表示用户是在未登录的情况下，接受了来自微信的好友邀请。所以要生成好友关系
     */
    private fun addFriendWithLoginOrReg() {
        if (inviter != null) {
            Observable.create<Map<String, String>> {
                val remark = ""
                val pref = activity!!
                        .getSharedPreferences(Configure.CONFIGUR_FILE_NAME,
                                Context.MODE_PRIVATE)
                val fmNumber = pref.getString("USERID", "0")
                val sb = StringBuffer()
                sb.append("{\"interfaceName\":\"FriendOperate\",\"parameter\":{\"fmnumber\":\"")
                sb.append(fmNumber)
                sb.append("\",\"friend_fmnumber\":\"")
                sb.append(inviter)
                sb.append("\",\"operate_type\":7,\"remark\":\"")
                        .append(remark).append("\"}}")
                val ret = Tools.postToServer(APIsUrl.API, sb.toString())
                // if (ret != null) {
                // // DebugLog.log(null, "哈哈哈" + ret, getClass());
                var m: Map<String, String> = java.util.HashMap()
                m = JsonToMap().jsonToMapWithNoArray(ret, m)
                it.onNext(m)
            }.observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).subscribe(Consumer<Map<String, String>> { m ->
                val flag = m?.get("status") ?: "failed"
                if (flag.equals("success")) {
                    Toast.makeText(appCtx,
                            R.string.add_new_buddy_suc, Toast.LENGTH_SHORT)
                            .show()
                } else {
                    Toast.makeText(appCtx,
                            m.get("errmsg"), Toast.LENGTH_SHORT).show()
                }
            }, Consumer<Throwable> { }, Action { })
        }
    }

    /**
     * 闹钟分享登录结束
     */
    private fun initResultShare() {
        activity!!.setResult(Activity.RESULT_OK)
        activity!!.finish()
        activity!!.overridePendingTransition(R.anim.left_in, R.anim.right_out)
    }
}