package challenges.day12

import kotlin.math.abs

data class Vertex(val x: Int, val y: Int, val c: Char) {
    val id = "$x:$y"

    override fun toString(): String {
        return "$x:$y:$c"
    }

    fun possibleMoves(): Set<Pair<Int, Int>> {
        return listOf(
            Pair(x - 1, y),
            Pair(x, y - 1),
            Pair(x, y + 1),
            Pair(x + 1, y),
        ).filter { it.first >= 0 && it.second >= 0 }.toSet()
    }

    private val actualCode = when (c) {
        'E' -> 'z'.code
        'S' -> 'a'.code
        else -> c.code
    }

    fun canGo(to: Vertex): Boolean {
        return abs(to.actualCode - this.actualCode) in setOf(0, 1) || to.actualCode <= this.actualCode
    }
}
