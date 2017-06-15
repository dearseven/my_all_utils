/**
 * Created by Administrator on 2017/5/18.
 */
/**
 * We declare a package-level function main which returns Unit and takes
 * an Array of strings as a parameter. Note that semicolons are optional.
 */

fun main(args: Array<String>) {
    val language = if (args.size == 0) "EN" else args[0]
    println(when (language) {
        "EN" -> "Hello!"
        "FR" -> "Salut!"
        "IT" -> "Ciao!"
        else -> "Sorry, I can't greet you in $language yet"
    })
    Greeter("cyan K").greet();

    println(max("2".toInt(), "3".toInt()))

    var x=parseInt("2A");
    var y=parseInt("1");

    var n="aaaxxxbbb";
    println("length of ${n} is :"+getStringLength(n));

    var i = 0
    while (i < n.length)
        println(n[i++])

    for( s in n){
        println("s in n:"+s)
    }
}

class Greeter(val name: String) {
    fun greet() {
        println("Hello, ${name}");
    }
}

fun max(a: Int, b: Int) = if (a > b) a else b

// Return null if str does not hold a number
fun parseInt(str: String ): Int? {
    try {
        return str.toInt()
    } catch (e: NumberFormatException) {
        println("One of the arguments isn't Int")
    }
    return null
}

fun getStringLength(obj: Any): Int? {
    if (obj is String)
        return obj.length // no cast to String is needed
    return null
}