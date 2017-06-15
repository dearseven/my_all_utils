/**
 * See http://kotlinlang.org/docs/reference/control-flow.html#when-expression
 */

fun main(args: Array<String>) {
    cases("Hello")
    cases(1)
    cases(0L)
    cases(MyClass())
    cases("hello")
    cases(Xx());
}

fun cases(obj: Any) {
    when (obj) {
        1 -> println("${obj}:One")
        "Hello" -> println("${obj}:Greeting")
        is Long -> println("${obj}:Long")
        is MyClass-> println("${obj}: fun:"+obj.returnMyName());
        !is String  -> println("${obj}:Not a string")
        else -> println("${obj}:Unknown")
    }
}

class MyClass() {
    fun returnMyName():String ?{
        return "MyClass"
    }
}

class Xx(){}