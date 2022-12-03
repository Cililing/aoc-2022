package challenges.day1

import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(1)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<Int?> {
        return input.map { it.toIntOrNull() }
    }

    @Task("ex1")
    fun ex1(input: List<Int?>): String {
        return input
            .runningFold(0) { acc, i -> if (i == null) 0 else acc + i }
            .maxBy { it }
            .toString()
    }

    @Task("ex2")
    fun ex2(input: List<Int?>): String {
        return input
            .asSequence()
            .runningFold(0) { acc, i -> if (i == null) 0 else acc + i }
            .sortedDescending()
            .take(3)
            .sum()
            .toString()
    }
}
