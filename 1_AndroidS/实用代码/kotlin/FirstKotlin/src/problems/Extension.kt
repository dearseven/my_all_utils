package problems

open class D {
}

class D1 : D() {
}

open class C {
    open fun D.foo() {
        println("${this},D.foo in C")
    }

    open fun D1.foo() {
        println("${this},D1.foo in C")
    }

    fun caller(d: D) {
        d.foo()   // call the extension function
    }
}

class C1 : C() {
    override fun D.foo() {
        println("${this},D.foo in C1")
    }

    override fun D1.foo() {
        println("${this},D1.foo in C1")
    }
}

fun main(args: Array<String>) {
    C().caller(D())   // prints "D.foo in C"
    C1().caller(D())  // prints "D.foo in C1" - dispatch receiver is resolved virtually
    C().caller(D1())  // prints "D.foo in C" - extension receiver is resolved statically
}
