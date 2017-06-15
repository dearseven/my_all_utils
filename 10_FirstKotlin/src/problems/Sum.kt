package problems

fun main(args: Array<String>) {
    val a= listOf<Int>(1,2,3,4,5,6,7,8,9,10).toIntArray()
    println(sum(a));
}

fun sum(a: IntArray): Int {
    var rsl = 0;
    var index = 0;
    while (index < a.size) {
        rsl += a[index];
        index++;
    }
    return rsl;
}