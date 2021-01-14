package problems

fun indexOfMax(a: IntArray): Int? {
    return a.size
}

fun main(args: Array<String>) {
    val ia = listOf<Int>(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).toIntArray();
    print(indexOfMax(ia));
}