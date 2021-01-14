package single_instance

/**
 * 基本的，一个线程不安全的懒加载
 * Created by wx on 2017/6/1.
 */
class Lazy1{
    companion object{
        //必须是val
        val instance by lazy(LazyThreadSafetyMode.NONE){
            Lazy1();
        }
    }
    //等价于java的那种
    /*
     public static A getInstace(){
        if(A==null){
         a=new A();
        }
        return a;
     }
     */
}