package ndarray

// return all positions that are up in the order from x,x to up
fun IntArray.allUp(): List<Pair<Int, Int>> {
    return (0 until this[0]).toList().map { Pair(it, this[1]) }.reversed()
}

// return all positions that are down in the order from x,x to down
fun IntArray.allDown(max: Int): List<Pair<Int, Int>> {
    return (this[0].inc()..max).toList().map { Pair(it, this[1]) }
}

// return all positions that are on the left in the order from x,x to left
fun IntArray.allLeft(): List<Pair<Int, Int>> {
    return (0 until this[1]).toList().map { Pair(this[0], it) }.reversed()
}

// return all positions that are on the right in the order from x,x to right
fun IntArray.allRight(max: Int): List<Pair<Int, Int>> {
    return (this[1].inc()..max).toList().map { Pair(this[0], it) }
}
