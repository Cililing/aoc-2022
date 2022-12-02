enum class Shape(val points: Int) {
    Rock(1), Paper(2), Scissors(3);

    fun getWinningOpposite(): Shape {
        return when (this) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }
    }

    fun getLoosingOpposite(): Shape {
        return when (this) {
            Rock -> Scissors
            Paper -> Rock
            Scissors -> Paper
        }
    }
}

data class EncodedInput(val first: Char, val second: Char)

data class Game(val opponent: Shape, val you: Shape) {
    private fun pointsForGame(): Int {
        if (opponent == you) {
            return 3 // draw
        }

        if (opponent.getWinningOpposite() == you) {
            return 6 // win
        }

        return 0 // lost
    }

    fun points() = pointsForGame() + you.points
}

@Challenge(2)
class Day2 : BaseChallenge<List<EncodedInput>>() {

    override val inputPath = "input/day2.txt"

    override fun parse(input: List<String>): List<EncodedInput> {
        return input.map {
            val firstChar = it.trim().first()
            val lastChar = it.trim().last()

            EncodedInput(firstChar, lastChar)
        }
    }

    override fun ex1(): String {
        return input.map {
            val first = when (it.first) {
                'A' -> Shape.Rock
                'B' -> Shape.Paper
                'C' -> Shape.Scissors
                else -> throw IllegalArgumentException("must be one of A/B/C")
            }

            val last = when (it.second) {
                'X' -> Shape.Rock
                'Y' -> Shape.Paper
                'Z' -> Shape.Scissors
                else -> throw IllegalArgumentException("must be one of X/Y/Z")
            }

            Game(first, last)
        }.sumOf { it.points() }.toString()
    }

    override fun ex2(): String {
        return input.map {
            val first = when (it.first) {
                'A' -> Shape.Rock
                'B' -> Shape.Paper
                'C' -> Shape.Scissors
                else -> throw IllegalArgumentException("must be one of A/B/C")
            }

            val last = when (it.second) {
                'X' -> first.getLoosingOpposite()
                'Y' -> first
                'Z' -> first.getWinningOpposite()
                else -> throw IllegalArgumentException("must be one of X/Y/Z")
            }
            Game(first, last)
        }.sumOf { it.points() }.toString()
    }
}
