package test 

/**
 * Created by wx on 2017/6/23.
 */

fun main(args: Array<String>) {
    //初始化数组, {(0+1),(1+1),(2+1)}={1,2,3}
	val WHEELVALUES = Array(3, { i -> i + 1 })
    //------------聚合操作--------------
    //1 any 如果至少有一个元素与指定条件相符，则返回true。
    val list = listOf(1, 2, 3, 4, 5)
    val slist = listOf("A", "B", "C", "D", "E")
    val sslist = listOf(1, 2, 1, 2, 3, 4)

    println("any: ${list.any { it > 10 }}")

    //2 all 如果所有元素与指定条件相符，则返回true。
    println("all: ${list.all { it < 10 }}")

    //3 count 返回与指定条件相符的元素个数。
    println("count: ${list.count { it <= 3 }}")

    //4  将对集合从第一个到最后一个元素的操作结果进行累加，并加上初始值。
    println("fold: ${slist.fold("F") { total, next -> total + next }}")//FABCDE

    //5 foldRight  貌似是吧初始值最后加上
    println("foldRight: ${slist.foldRight("F") { total, next -> total + next }}")//ABCDEF

    //6 forEach
    list.forEach { print("foreach:$it ") }
    println()

    //7 forEachIndexed  同forEach，不过同时还获得元素的索引。
    list.forEachIndexed { index, i -> print("forEachIndexed:${list.get(index)}:${slist[index]} ") }
    println()

    //8max 返回最大元素。如果没有元素，则返回null。  还有一个min
    println("max: ${list.max()}")

    //9 maxBy 返回使指定函数产生最大值的第一个元素。如果没有元素，则返回null。还有一个minBy
    println("maxBy:${list.maxBy { x -> -x }}")

    //10 none 如果没有元素与指定条件相符，则返回true。
    println("none:${list.none { x -> x == 6 }}")

    //11 reduce 同fold，但是不包括初始值。只是将对集合从第一个元素到最后一个元素的操作结果进行累加。
    println("fold: ${slist.reduce { total, next -> total + next }}")//ABCED

    //12 reduceRight 同reduce，但是，是从最后一个元素到第一个元素。
    println("reduceRight: ${slist.reduceRight { total, next -> total + next }}")//ABCED

    //13 sumBy 和
    println("sumBy:${list.sumBy { it }}")

    //--------------筛选操作--------------
    //1 drop 返回所有元素列表，但不包括前N个元素。
    println("drop:${slist.drop(1)}")

    //2 dropWhile 返回所有元素列表，但不包括第一个满足指定条件的元素
    println("dropWhile:${sslist.dropWhile { it == 2 }}")

    //3 filter 返回所有与指定条件相符的元素列表。
    println("filter:${slist.filter { it.equals("A") }}")

    //4 filterNot 返回所有与指定条件不符的元素列表
    println("filterNot  :${sslist.filterNot { it == 2 }}")

    //5 filterNotNull 返回所有元素列表，但不包括null元素。
    println("filterNotNull  :${sslist.filterNotNull()}")

    //6 slice 返回
    // 指定索引的元素列表。
    println("slice  :${slist.slice(listOf(0, 1, slist.size - 1))}")

    //7 take 返回前N个元素
    println("take:${slist.take(3)}")

    //8 takeLast 返回最后N个元素
    println("takeLast:${slist.takeLast(3)}")

    //9  takeWhile 返回满足指定条件第一个元素列表。
    println("takeWhile:${sslist.takeLastWhile { it > 2 }}")

//-----------------映射操作----------------
    //1 flatMap 通过遍历每个元素创建一个新集合，最后，把所有集合整合到包含所有元素的唯一列表中。
    var newList = listOf(list, slist).flatMap {
        it.filter { x ->
            if (x is Int) {
                (x as Int) >= 3
            } else if (x is String) {
                x.equals("A") || x.equals("C") || x.equals("E")
            } else {
                false
            }
        }
    }
    println("before flatMap")
    listOf(list, slist).forEachIndexed { index, x -> print("{index:$index,value:$x} ") }
    println("\nafter flatMap but not filter")
    listOf(list, slist).flatMap { it }.forEachIndexed { index, x -> print("{index:$index,value:$x} ") }
    println("\nafter flatMap and  filter")
    newList.forEachIndexed { index, x -> print("{index:$index,value:$x} ") }

    //2 groupby 返回一个映射表，该表包括经指定函数对原始集合中元素进行分组后的元素。
    println("\ngroupby:${list.groupBy { if (it % 2 == 0) "even" else "odd" }}")

    //3 map 返回一个列表，该列表包含对原始集合中每个元素进行转换后结果。
    println("map:${list.map { it * 2 }}")

    //4 mapIndexed 返回一个列表，该列表包含对原始集合中每个元素进行转换后结果和它们的索引。
    println("mapIndexed:${list.mapIndexed { index, it -> index * it }}")

    //5 mapNotNull
    println("mapNotNull:${list.mapNotNull { it }}")

    //--------------生成操作----------------
    //1 partition  将原始集合拆分一对集合，一个集合包含判断条件为true的元素，另一个集合包含判断条件为false的元素。
    println("partition:${list.partition { it >= 3 }}")

    //2 plus  返回一个列表，该列表包含原始集合的所有元素和指定集合的所有元素。由于函数名称原因，我们可以使用“+”操作符。
    println("plus:${list.plus(slist)}")

    //3 zip 返回一个列表，该列表由两个集合中相同索引元素建立的元素对。这个列表长度为最短集合的长度。
    println("zip:${list.zip(slist)}")

    //------排序-----
    var p6 = Person(6, "WX")
    var p1 = Person(1, "ZXL")
    var p3 = Person(3, "XHX")
    var p2 = Person(2, "LXY")
    var p4 = Person(4, "JLJ")
    var p5 = Person(5, "WWW")

    val ps = listOf(p6, p1, p3, p4, p5)

    println("排序前:${ps}")
    println("逆序:${ps.reversed()}")
    println("sortBy:${ps.sortedBy { -it.index }}")
    println("sortedByDescending:${ps.sortedByDescending { -it.index }}")

}

data class Person(val index: Int, val name: String)


