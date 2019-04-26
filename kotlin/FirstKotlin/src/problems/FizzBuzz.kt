package problems

import java.util.TreeMap

fun fizzBuzz(i: Int) = when {
    i % 15 == 0 -> "${i}: FizzBuzz "
    i % 3 == 0 -> "${i}:Fizz "
    i % 5 == 0 -> "${i}:Buzz "
    else -> "$i:nothing "
}

fun main(args: Array<String>) {
    for (i in 1..20) {
        print(fizzBuzz(i))
    }
    println()
    for (i in 20 downTo 1 step 2) {
        print(fizzBuzz(i))
    }

    ////TreeMap is  a Java Class
    val binaryReps = TreeMap<Char, String>()

    for (c in 'A'..'F') {
        val binary = Integer.toBinaryString(c.toInt())
        binaryReps[c] = binary
    }

    for ((letter, binary) in binaryReps) {
        println("$letter = $binary")
    }
}