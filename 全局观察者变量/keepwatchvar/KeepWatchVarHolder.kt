package com.teetaa.outsourcing.carinspection.util.keepwatchvar

class KeepWatchVarHolder {
    private object Holder {
        val instance = KeepWatchVarHolder()
    }

    private constructor()

    companion object {
        val instance = Holder.instance
    }

    //---------在这里申明可以被全局观察的变量，这个测试用的消息队列的时间变化-
    var rabbitMqTsVar = KeepWatchVar<Long>(0L)
}