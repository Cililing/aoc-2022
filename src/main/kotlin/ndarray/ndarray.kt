package ndarray

/**
 * assume matrix:
 * 0  1  2  3  4
 * 5  6  7  8  9
 * 10 11 12 13 14
 * 15 16 17 18 19
 * 20 21 22 23 24
 *
 * and point 2, 2 (value: 12)
 * then:
 *      toUp -> 7, 2
 *      toDown -> 17, 22
 *      toLeft -> 11, 10
 *      toRight -> 13, 14
 *      toUpLeft -> 6, 0
 *      toUpDown -> 8, 4
 *      toDownLeft -> 16, 20
 *      toDownRight -> 18, 24
 *
 * for another point 1, 1 (6)
 * then:
 *      ...
 *      toUpLeft -> 0
 *      toUpRight -> 2
 *      toDownLeft -> 10
 *      toDownRight -> 12, 18, 24
 */
private fun IntArray.require2D() {
    require(this.size == 2) {
        "array must be 2d"
    }
}

// return all positions that are up in the order from x,x to up
fun IntArray.toUp(): List<Pair<Int, Int>> {
    require2D()
    return (0 until this[0]).toList().map { Pair(it, this[1]) }.reversed()
}

// return all positions that are down in the order from x,x to down
fun IntArray.toDown(max: Int): List<Pair<Int, Int>> {
    require2D()
    return (this[0].inc()..max).toList().map { Pair(it, this[1]) }
}

// return all positions that are on the left in the order from x,x to left
fun IntArray.toLeft(): List<Pair<Int, Int>> {
    require2D()
    return (0 until this[1]).toList().map { Pair(this[0], it) }.reversed()
}

// return all positions that are on the right in the order from x,x to right
fun IntArray.toRight(max: Int): List<Pair<Int, Int>> {
    require2D()
    return (this[1].inc()..max).toList().map { Pair(this[0], it) }
}

fun IntArray.toUpLeft(): List<Pair<Int, Int>> {
    require2D()

    val up = toUp()
    val left = toLeft()
    val zipped = up.zip(left)

    return zipped.map { Pair(it.first.first, it.second.second) }
}

fun IntArray.toUpRight(max: Int): List<Pair<Int, Int>> {
    require2D()

    val up = toUp()
    val right = toRight(max)
    val zipped = up.zip(right)

    return zipped.map { Pair(it.first.first, it.second.second) }
}

fun IntArray.toDownLeft(max: Int): List<Pair<Int, Int>> {
    require2D()

    val down = toDown(max)
    val left = toLeft()
    val zipped = down.zip(left)

    return zipped.map { Pair(it.first.first, it.second.second) }
}

fun IntArray.toDownRight(max: Int): List<Pair<Int, Int>> {
    require2D()

    val down = toDown(max)
    val right = toRight(max)
    val zipped = down.zip(right)

    return zipped.map { Pair(it.first.first, it.second.second) }
}
