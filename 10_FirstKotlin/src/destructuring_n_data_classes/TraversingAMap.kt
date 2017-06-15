package destructuring_n_data_classes

/**
 *  Kotlin Standard Library provide component functions for Map.Entry
 */

fun main(args: Array<String>) {
    val map = hashMapOf<String, Int>()
    map.put("one", 1)
    map.put("two", 2)

    for ((key, value) in map) {
        println("key = $key, value = $value")
    }

    val fruitsMap = hashMapOf<Int, Fruits>();
    fruitsMap.put(1, Fruits("apple", "苹果"));
    fruitsMap.put(2, Fruits("peal", "梨"));
    for ((k, v) in fruitsMap) {
        println("key=${k},v=${v.eName}:${v.cName}")
        var (e, c) = v;
        println("key=${k},v=$e:${c}")
    }
}

data class Fruits(val eName: String, val cName: String);