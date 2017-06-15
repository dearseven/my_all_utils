package delegated_properties

interface Base {
    fun print()
}

class BaseImpl(val x: Int) : Base {
    override fun print() {
        println(x)
    }
}

class Derived(a: Base) : Base by a {
}

class Derived2(var a:Base):Base{
    override fun print() {
        println ("d2");
        a.print();
    }
}

fun main(args: Array<String>) {
    val b = BaseImpl(10)
    Derived(b).print() // prints 10
    println()
    Derived2(b).print() // prints what?
}