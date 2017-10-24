
import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * 路由
 * Created by wx on 2017/10/24.
 */
class RRouter {
    companion object {
        val DATA_BUNDLE = "bundleData"
        fun sendBroadCast(ctx: Context, cls: Class<out BroadcastReceiver>, data: Bundle?): Unit {
            val it = Intent(ctx, cls)
            it.putExtra(DATA_BUNDLE, data)
            ctx.sendBroadcast(it)
        }

        fun startService(ctx: Context, cls: Class<out Service>, data: Bundle?): Unit {
            val it = Intent(ctx, cls)
            it.putExtra(DATA_BUNDLE, data)
            ctx.startService(it)
        }

        fun fromContextToView(ctx: Context, cls: Class<out AppCompatActivity>, flagNew: Boolean = true, data: Bundle? = null): Unit {
            val it = Intent(ctx, cls)
            it.putExtra(DATA_BUNDLE, data)
            if (flagNew) {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            ctx.startActivity(it)
        }

        fun fromViewToView(ctx: Activity, cls: Class<out AppCompatActivity>, data: Bundle? = null, finish: Boolean = true): Unit {
            val it = Intent(ctx, cls)
            it.putExtra(DATA_BUNDLE, data)
            ctx.startActivity(it)
            if (finish) {
                ctx.finish()
            }
        }

        fun fromViewToViewForResult(ctx: Activity, cls: Class<out AppCompatActivity>, reqId: Int, data: Bundle? = null): Unit {
            val it = Intent(ctx, cls)
            it.putExtra(DATA_BUNDLE, data)
            ctx.startActivityForResult(it, reqId);
        }
    }


}