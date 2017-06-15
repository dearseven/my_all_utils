package problems

fun main(args: Array<String>) {
    loop@ for (i in 1..5) {
        // ...
        for (j in 1..5) {
            if (i * j > 10) break@loop
        }
        print("${i} ");
    }

    println("\n======================================");

    val ints = listOf(1, 2, 0,3, 4, 5);
    fun foo() {
        ints.forEach {
            if (it == 0) return
            print("${it} ")
        }
    }

    fun foo1() {
        ints.forEach lit@ {
            if (it == 0) return@lit
            print(it)
        }
    }

    fun foo2() {
        ints.forEach {
            if (it == 0) return@forEach
            print(it)
        }
    }

    fun foo3() {
        ints.forEach(fun(value: Int) {
            if (value == 0) return
            print(value)
        })
    }
    foo1()
}


