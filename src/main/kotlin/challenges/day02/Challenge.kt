package challenges.day02

import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(2)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<EncodedInput> {
        return input.map {
            val firstChar = it.trim().first()
            val lastChar = it.trim().last()

            EncodedInput(firstChar, lastChar)
        }
    }

    @Task("ex1")
    fun ex1(input: List<EncodedInput>): String {
        return input.map {
            val opponent = parseLetter(it.first)
            val you = parseLetter(it.second, 'X', 'Y', 'Z')

            Game(opponent, you)
        }.sumOf { it.points() }.toString()
    }

    @Task("ex2")
    fun ex2(input: List<EncodedInput>): String {
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
