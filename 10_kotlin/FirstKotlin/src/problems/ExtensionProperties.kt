package problems

val String.lastChar: Char
    get() = get(length - 1)
var StringBuilder.lastChar: Char
    get() = get(length - 1)
    set(value: Char) {
        this.setCharAt(length - 1, value)
    }

fun main(args: Array<String>) {
    println("Kotlin".lastChar)
    val sb = StringBuilder("Kotlin?")
    sb.lastChar = '!'
    println(sb)

   "".filterOdd("Kotlin");

    val b: String? = null;
    println("b?.length=${b?.length}");

}

fun String.filterOdd(str:String) = {

}