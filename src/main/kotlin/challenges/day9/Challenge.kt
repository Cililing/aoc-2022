package challenges.day9

import runner.Challenge
import runner.Mapper
import runner.Task
import java.awt.geom.Point2D
import kotlin.math.sqrt

enum class Direction {
    Left, Right, Up, Down
}

data class Position(val x: Int, val y: Int) {

    companion object {
        private val sqrt2 = sqrt(2.0)
    }

    fun moveTo(to: Position): Position {
        return when {
            // is nearby (up to one field in x/y including diagonal)
            Point2D.distance(
                this.x.toDouble(),
                this.y.toDouble(),
                to.x.toDouble(),
                to.y.toDouble()
            ) <= sqrt2 -> this
            // same row/column
            this.x == to.x && this.y > to.y -> Position(x, y - 1)
            this.x == to.x && this.y < to.y -> Position(x, y + 1)
            this.x > to.x && this.y == to.y -> Position(x - 1, y)
            this.x < to.x && this.y == to.y -> Position(x + 1, y)
            // diagonal
            this.x > to.x && this.y < to.y -> Position(x - 1, y + 1) // 1st quarter
            this.x > to.x && this.y > to.y -> Position(x - 1, y - 1) // 2nd quarter
            this.x < to.x && this.y < to.y -> Position(x + 1, y + 1) // 3rd quarter
            this.x < to.x && this.y > to.y -> Position(x + 1, y - 1) // 4th quarter
            else -> throw IllegalArgumentException("illegal move")
        }
    }

    fun move(direction: Direction): Position {
        return when (direction) {
            Direction.Left -> Position(x, y - 1)
            Direction.Right -> Position(x, y + 1)
            Direction.Up -> Position(x + 1, y)
            Direction.Down -> Position(x - 1, y)
        }
    }
}

data class LineState(
    val start: Position = Position(0, 0),
    val head: Position = Position(0, 0),
    val tail: Position = Position(0, 0),
)

data class LongLineState(
    val start: Position = Position(0, 0),
    val head: Position = Position(0, 0),
    val n1: Position = Position(0, 0),
    val n2: Position = Position(0, 0),
    val n3: Position = Position(0, 0),
    val n4: Position = Position(0, 0),
    val n5: Position = Position(0, 0),
    val n6: Position = Position(0, 0),
    val n7: Position = Position(0, 0),
    val n8: Position = Position(0, 0),
    val tail: Position = Position(0, 0),
)

@Challenge(9)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<Pair<Direction, Int>> {
        return input.map {
            val split = it.split(" ")
            when (split[0][0]) {
                'L' -> Direction.Left
                'R' -> Direction.Right
                'U' -> Direction.Up
                'D' -> Direction.Down
                else -> throw IllegalArgumentException("direction unknown")
            } to split[1].toInt()
        }
    }

    private fun List<Pair<Direction, Int>>.normalize(): List<Direction> {
        return this.flatMap { generateSequence { it.first }.take(it.second) }
    }

    @Task("ex1")
    fun ex1(input: List<Pair<Direction, Int>>): Int {
        val allMoves = input.normalize().fold(Pair(LineState(), listOf<LineState>())) { stateWithHistory, move ->
            val state = stateWithHistory.first
            val history = stateWithHistory.second

            val newHead = state.head.move(move)
            val newTail = state.tail.moveTo(newHead)
            state.copy(
                head = newHead,
                tail = newTail
            ) to history + state
        }

        val tailUnique = (allMoves.second.map { it.tail } + allMoves.first.tail).toSet()
        return tailUnique.size
    }

    @Task("ex2")
    fun ex2(input: List<Pair<Direction, Int>>): Int {
        val allMoves =
            input.normalize().fold(Pair(LongLineState(), listOf<LongLineState>())) { stateWithHistory, move ->
                val state = stateWithHistory.first
                val history = stateWithHistory.second

                val newHead = state.head.move(move)
                val newN1 = state.n1.moveTo(newHead)
                val newN2 = state.n2.moveTo(newN1)
                val newN3 = state.n3.moveTo(newN2)
                val newN4 = state.n4.moveTo(newN3)
                val newN5 = state.n5.moveTo(newN4)
                val newN6 = state.n6.moveTo(newN5)
                val newN7 = state.n7.moveTo(newN6)
                val newN8 = state.n8.moveTo(newN7)
                val newTail = state.tail.moveTo(newN8)

                val newState = state.copy(
                    head = newHead,
                    n1 = newN1,
                    n2 = newN2,
                    n3 = newN3,
                    n4 = newN4,
                    n5 = newN5,
                    n6 = newN6,
                    n7 = newN7,
                    n8 = newN8,
                    tail = newTail
                )

                newState to history + state
            }

        val tailUnique = (allMoves.second.map { it.tail } + allMoves.first.tail).toSet()
        return tailUnique.size
    }
}
