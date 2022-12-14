package challenges.day14

import kotlin.math.max
import kotlin.math.min

data class Point(val x: Int, val y: Int) {
    fun to(other: Point): List<Point> {
        return when {
            // x the same
            this.x == other.x -> (min(this.y, other.y)..max(this.y, other.y)).map { Point(this.x, it) }
            this.y == other.y -> (min(this.x, other.x)..max(this.x, other.x)).map { Point(it, this.y) }
            else -> throw IllegalArgumentException("can move only on straight line")
        }
    }

    operator fun plus(v: Point): Point {
        return this.copy(x = this.x + v.x, y = this.y + v.y)
    }
}
