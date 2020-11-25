package com.teetaa.outsourcing.carinspection.thirdpart.rabbitmq

import android.util.Log
import com.rabbitmq.client.*
import com.teetaa.outsourcing.carinspection.frontservice.MyFrontService
import com.teetaa.outsourcing.carinspection.util.ApplicationWeakHandler
import com.teetaa.outsourcing.carinspection.util.NotifyUtil
import com.teetaa.outsourcing.carinspection.util.WeakHolder
import kotlinx.coroutines.*
import java.io.IOException
import java.net.ConnectException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashSet


/*GlobalScopeGlobalScopeGlobalScopeGlobalScopeGlobalScopeGlobalScopeGlobalScopeGlobalScope
    url: 47.107.91.103:5672
    userName: admin
    password: admin123
    virtualHost: /
    routeKey: route-iot
    exchangeName: exchange-iot
    queueName: queue-iot
 */
//GlobalScope
class RabbitMqUtil {
	//完全可以不要 反正只推通知出去
    var weakHandler: ApplicationWeakHandler? = null
    val isRun = false
    private var shutdownStatu = 0//当状态为100的时候表示是正常关闭,0表示需要初始化 1表示初始化好了

    private var factory: ConnectionFactory? = null
    private var channel: Channel? = null
    var conn: Connection? = null

    companion object {
        private val _consumerTagSet: HashSet<String> = HashSet<String>()
        val instance = Holder.instance
    }


    private object Holder {
        val instance = RabbitMqUtil()
    }

    private constructor()

    fun initRabbit() {
        if (factory == null) {
            factory = ConnectionFactory()
            factory!!.setHost("47.107.91.103");
            factory!!.setPort(5672);
            factory!!.setUsername("admin");
            factory!!.setPassword("admin123");
            factory!!.isAutomaticRecoveryEnabled = true
            factory!!.isTopologyRecoveryEnabled = true
            shutdownStatu = 0
        }
    }

    fun isOpen(): Boolean {
        try {
            return channel?.isOpen ?: false && conn?.isOpen ?: false
        } catch (e: Exception) {
            return false
        }
    }

    fun close() {
        Log.i("RabbitSendee", "_consumerTagSet=$_consumerTagSet")

//        Thread {
//
//        }.start()
        runBlocking {
            withContext(Dispatchers.IO) {
                shutdownStatu = 100
                try {
                    Log.i("RabbitSendee", "_consumerTagSet=$_consumerTagSet")
                    _consumerTagSet?.forEach {
                        try {
                            Log.i("RabbitSendee", "item in _consumerTagSet=$it")
                            channel?.basicCancel(it ?: "")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    _consumerTagSet.clear()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    channel?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    conn?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                weakHandler = null
            }
        }
    }

    private fun __closeForConnect() {
//        Thread {
//
//        }.start()

        runBlocking {
            withContext(Dispatchers.IO) {
                try {
                    _consumerTagSet?.forEach {
                        try {
                            Log.i("RabbitSendee", "item in _consumerTagSet=$it")
                            channel?.basicCancel(it ?: "")
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    _consumerTagSet.clear()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    channel?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    conn?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun addObserver(flag: Int) {
//        Thread {
//            _addObserver(flag)
//        }.start()
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                _addObserver(flag)
            }
        }
    }

    private fun _addObserver(flag: Int) {
        val exchangeName = "exchange-iot"
        val routeKey = "route-iot"
        val queueName = "queue-iot"
        Log.i("RabbitSendee", "flag=$flag,_addObserver=${System.currentTimeMillis()}")
        try {
            try {
                __closeForConnect()
                conn = factory!!.newConnection()
            } catch (e: Exception) {
                Log.i("RabbitSendee", "1 shutdownCompleted=$shutdownStatu")
                e.printStackTrace()
                try {
                    if (shutdownStatu == 0 && e is ConnectException) {
                        Thread.sleep(5000)
                        _addObserver(2)
                    }
                    channel?.also {
                        if (it.isOpen) {
                            it.close()
                        }
                    }
                } catch (ex: Exception) {
                    Log.i("RabbitSendee", "2 shutdownCompleted=$shutdownStatu")
                    ex.printStackTrace()
                }
            }
            conn?.also {
                it.removeShutdownListener(shutdownListener)
                it.addShutdownListener(shutdownListener)
                channel = it.createChannel()
                //
                //channel.basicQos(1)
                channel?.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true)
                val mQueueDeclare = channel?.queueDeclare(queueName, true, false, false, null)
                val quene = mQueueDeclare?.queue
                channel?.queueBind(quene, exchangeName, routeKey) //这里要根据实际改
                //
                val consumer: Consumer = object : DefaultConsumer(channel) {
                    @Throws(IOException::class)
                    override fun handleDelivery(
                        consumerTag: String?,
                        envelope: Envelope?,
                        properties: AMQP.BasicProperties?,
                        body: ByteArray?
                    ) {
                        super.handleDelivery(consumerTag, envelope, properties, body)
                        // 通过geiBody方法获取消息中的数据
                        _consumerTagSet.add(consumerTag ?: "")
                        val message = String(body!!)
                        shutdownStatu = 1
                        Log.i("RabbitSendee", "consumerTag=$consumerTag")

//                        weakHandler?.also { handler ->
//                            handler.obtainMessage(handler.MSG_SIMPLE, message).sendToTarget()
//                        }

                        WeakHolder.instance.weakApplication.get()?.also {
                            NotifyUtil.instance.sendSimpleNotify(
                                it,
                                100,
                                "消息队列,${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())},${RabbitMqUtil.instance.isOpen()}"
                            )
                        }
                        if (!MyFrontService.isFront) {
                            close()
                        }
                    }
                }
                channel?.basicConsume(quene, true, consumer)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun publish(message: String) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                __realPublish(message)
            }
        }
    }

    private fun __realPublish(message: String) {
        val exchangeName = "exchange-iot"
        val routeKey = "route-iot"
        val queueName = "queue-iot"
        try {
            //连接
            val connection = factory!!.newConnection()
            //通道
            val channel = connection.createChannel()
            //声明了一个交换和一个服务器命名的队列，然后将它们绑定在一起。
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT, true)

            val mQueueDeclare = channel?.queueDeclare(queueName, true, false, false, null)
            val queue = mQueueDeclare?.queue
            channel.queueBind(queue, exchangeName, routeKey) //将exchange和queue绑定
            //消息发布
            val msg = message.toByteArray()
            channel.basicPublish(exchangeName, routeKey, null, msg)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //----------
    private val shutdownListener = object : ShutdownListener {
        override fun shutdownCompleted(cause: ShutdownSignalException?) {
            Log.i("RabbitSendee", "shutdownCompleted=$shutdownStatu")
            if (shutdownStatu != 100) {
                try {
                    Thread.sleep(5000)
                    _addObserver(3)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}