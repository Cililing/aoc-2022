package challenges.day3

import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(3)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<String> {
        return input
    }

    @Task("ex1")
    fun ex1(input: List<String>): String {
        return input
            .flatMap {
                val pivot = it.length / 2
                val parts = it.chunked(pivot)

                parts[0].toSet().intersect(parts[1].toSet())
            }.sumOf { it.internalCode() }
            .toString()
    }

    @Task("ex2")
    fun ex2(input: List<String>): String {
        return input
            .chunked(3)
            .flatMap {
                val sets = it.map { it.toSet() }
                val common = sets.reduce { acc, s ->
                    acc.intersect(s)
                }

                common
            }
            .sumOf { it.internalCode() }
            .toString()
    }

    companion object {
        private val internalCodes = run {
            val lowercase = ('a'..'z').mapIndexed { index, c ->
                c to index + 1
            }
            val uppercase = ('A'..'Z').mapIndexed { index, c ->
                c to index + 26 + 1
            }
            (lowercase + uppercase).toMap()
        }

        private fun Char.internalCode(): Int {
            return internalCodes[this] ?: throw IllegalArgumentException("letter unknown")
        }
    }
}
