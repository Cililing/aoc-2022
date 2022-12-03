private enum class Shape(val points: Int) {
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

private data class EncodedInput(val first: Char, val second: Char)

private data class Game(val opponent: Shape, val you: Shape) {
    fun points(): Int {
        return when {
            opponent == you -> 3
            opponent.getWinningOpposite() == you -> 6
            else -> 0
        } + you.points
    }
}

@Challenge(2)
@Benchmark(1000)
private class Day2 : BaseChallenge<List<EncodedInput>>() {

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
            val opponent = parseLetter(it.first)
            val you = parseLetter(it.second, 'X', 'Y', 'Z')

            Game(opponent, you)
        }.sumOf { it.points() }.toString()
    }

    override fun ex2(): String {
        return input.map {
            val opponent = parseLetter(it.first)
            val you = when (it.second) {
                'X' -> opponent.getLoosingOpposite()
                'Y' -> opponent
                'Z' -> opponent.getWinningOpposite()
                else -> throw IllegalArgumentException("must be one of X/Y/Z")
            }

            Game(opponent, you)
        }.sumOf { it.points() }.toString()
    }

    private fun parseLetter(letter: Char, rock: Char = 'A', paper: Char = 'B', scissors: Char = 'C'): Shape {
        return when (letter) {
            rock -> Shape.Rock
            paper -> Shape.Paper
            scissors -> Shape.Scissors
            else -> throw IllegalArgumentException("must be one of A/B/C")
        }
    }
}
