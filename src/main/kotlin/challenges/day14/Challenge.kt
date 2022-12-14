package challenges.day14

import runner.Challenge
import runner.Mapper
import runner.Task
import kotlin.math.max
import kotlin.math.min

enum class Field {
    Rock,
    Sand
}

data class Point(val x: Int, val y: Int) {
    fun to(other: Point): List<Point> {
        return when {
            // x the same
            this.x == other.x -> (min(this.y, other.y)..max(this.y, other.y)).map { Point(this.x, it) }
            this.y == other.y -> (min(this.x, other.x)..max(this.x, other.x)).map { Point(it, this.y) }
            else -> throw IllegalArgumentException("can move only on straight line")
        }
    }
}

@Challenge(14)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Map<Point, Field> {
        val rockPositions = input.fold(setOf<Point>()) { l, path ->
            val steps = path.split(" -> ").map { step ->
                val positions = step.split(',').map { it.toInt() }
                Point(positions.first(), positions.last())
            }

            val initialEnvMap = steps.first() to listOf<Point>()
            val rocks = steps.drop(1).fold(initialEnvMap) { acc, next ->
                next to acc.second + acc.first.to(next)
            }

            l + rocks.second.toSet()
        }

        return rockPositions.associateWith { Field.Rock }
    }

    @Task("ex1")
    fun ex1(input: Map<Point, Field>): Any {
        val mutableInput = input.toMutableMap()
        while (true) {
            val startPoint = Point(500, 0)
            val fallPoint = startPoint.goDown(mutableInput) ?: break
            mutableInput[fallPoint] = Field.Sand
        }

        return mutableInput.count { it.value == Field.Sand }
    }

    @Task("ex2")
    fun ex2(input: Map<Point, Field>): Any {
        val maxY = input.maxOf { it.key.y } + 2
        // val xRange = (input.minOf { it.key.x } - 1..input.maxOf { it.key.x } + 1)

        val xRange = (0..5000)
        val falseBottom = xRange.map { Point(it, maxY) }.associateWith { Field.Rock }

        val mutableInput = input.toMutableMap()
        mutableInput.putAll(falseBottom)

        while (true) {
            val startPoint = Point(500, 0)
            if (mutableInput.containsKey(startPoint)) break // overflow detected
            val fallPoint = startPoint.goDown(mutableInput) ?: break
            mutableInput[fallPoint] = Field.Sand
        }

        return mutableInput.count { it.value == Field.Sand }
    }

    private tailrec fun Point.goDown(map: Map<Point, Field>): Point? {
        if (this.y > map.maxOf { it.key.y }) {
            return null
        }

        // try go down by one field (decrease y)
        val possibleDown = this.copy(y = this.y + 1)
        if (!map.containsKey(possibleDown)) {
            return possibleDown.goDown(map)
        }

        val possibleDownLeft = possibleDown.copy(x = possibleDown.x - 1)
        if (!map.containsKey(possibleDownLeft)) {
            return possibleDownLeft.goDown(map)
        }

        val possibleDownRight = possibleDown.copy(x = possibleDown.x + 1)
        if (!map.containsKey(possibleDownRight)) {
            return possibleDownRight.goDown(map)
        }

        return this // cannot move
    }
}
