package challenges.day14

import runner.Challenge
import runner.Mapper
import runner.Task
import stack.Stack
import stack.pop
import stack.push

@Challenge(14)
class Challenge {

    companion object {
        private val possiblePaths = setOf(Point(0, 1), Point(-1, 1), Point(1, 1))
        private val startingPoint = Point(500, 0)
    }

    @Mapper
    fun parse(input: List<String>): Set<Point> {
        return input.fold(setOf<Point>()) { l, path ->
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
    }

    @Task("ex1")
    fun ex1(input: Set<Point>): Int {
        val mutableInput = input.toMutableSet()
        while (true) {
            val fallPoint = startingPoint.goDown(mutableInput) ?: break
            mutableInput.add(fallPoint)
        }

        return mutableInput.size - input.size
    }

    @Task("ex2")
    fun ex2(input: Set<Point>): Int {
        val maxY = input.maxOf { it.y } + 2
        val xRange = (500 - (maxY * 2)..500 + (maxY * 2)) // pyramid shape
        val falseBottom = xRange.map { Point(it, maxY) }

        val all = allPaths(input + falseBottom)

        return all.size
    }

    private fun allPaths(startingOccupied: Set<Point>): Set<Point> {
        val occupied = startingOccupied.toMutableSet()
        val stack = Stack<Point>()
        val visited = mutableSetOf<Point>()

        // start falling down to fill the stack with starting path
        val fallPoint =
            startingPoint.goDown(occupied) ?: throw IllegalStateException("the first fall point must exists")
        val firstFallPath = (startingPoint.y..fallPoint.y).map { Point(startingPoint.x, it) }  // falls down by y
        firstFallPath.forEach {
            stack.push(it)
        }

        while (stack.isNotEmpty()) {
            val currentSand = stack.pop()

            // process current
            visited.add(currentSand)
            occupied.add(currentSand)

            // add possible paths to process
            possiblePaths.map { currentSand + it }
                .filter { !occupied.contains(it) }
                .forEach { stack.push(it) }
        }

        return visited
    }

    // recursively go down (simulate one sand piece fall)
    // not very efficient
    private tailrec fun Point.goDown(occupied: Set<Point>): Point? {
        if (this.y > occupied.maxOf { it.y }) {
            return null
        }

        val possibleDown = this.copy(y = this.y + 1)
        if (!occupied.contains(possibleDown)) {
            return possibleDown.goDown(occupied)
        }

        val possibleDownLeft = possibleDown.copy(x = possibleDown.x - 1)
        if (!occupied.contains(possibleDownLeft)) {
            return possibleDownLeft.goDown(occupied)
        }

        val possibleDownRight = possibleDown.copy(x = possibleDown.x + 1)
        if (!occupied.contains(possibleDownRight)) {
            return possibleDownRight.goDown(occupied)
        }

        return this // cannot move
    }
}
