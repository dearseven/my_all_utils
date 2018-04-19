1 按找build文件配置
2 networkutil包中一个是拦截器，一个是一些用于配置参数的类（业务需要）
3 APIFunction是接口的定义，这个接口定了post的数据是json，直接也接受json的返回
4 ClientAPIUtil 封装了Retrofit2的工具类,返回一个该类的单例，改类在构造方法中初始化了Okhtt3和retrofit2，这个单例封装了方法再调用APIFunction定义的接口
5 使用 kotlin代码
val o: Observable<ResponseBody> = ClientAPIUtil.getInstance().playAuthorize("c1", "t2", "s3", "pt4", "ct5", "bt6", "if7")
o.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            Debug.log4op(LauncherInKotlin::class.java, "onAccept:\n${it.string()}")
        }, {
            Debug.log4op(LauncherInKotlin::class.java, "onError:\n${it.message}")
        }, {
            Debug.log4op(LauncherInKotlin::class.java, "on complete")
        })