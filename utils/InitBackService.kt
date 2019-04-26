package cc.m2u.hidrogen.service

import android.app.IntentService
import android.content.Intent
import android.content.Context

class InitBackService : IntentService("InitBackService") {

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val action = intent.action
            if (ACTION_DO_INIT == action) {
                executeBackInit()
            }
        }
    }

    /**
     * 执行后台的初始化任务
     */
    private fun executeBackInit() {}

    companion object {

        private val ACTION_DO_INIT = "cc.m2u.hidrogen.service.action.FOO"

        fun startActionInit(context: Context) {
            //先直接模拟埋一个最大最小的那个什么预警值设定值
            val intent = Intent(context, InitBackService::class.java)
            intent.action = ACTION_DO_INIT
            context.startService(intent)
        }
    }

}
