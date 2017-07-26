package cc.m2u.lottery.utils

import android.view.View

/**
 * Created by wx on 2017/7/25.
 */
/**
 * 扩展方法
 */
fun <T> View.findView(id: Int): T {
    return findViewById(id) as T
}