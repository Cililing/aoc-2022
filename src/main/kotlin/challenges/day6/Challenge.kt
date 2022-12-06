package challenges.day6

import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(6)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Sequence<Char> {
        return input.first().asSequence()
    }

    @Task("ex1")
    fun ex1(input: Sequence<Char>): Int {
        return findMarker(input, 4)
    }

    @Task("ex2")
    fun ex2(input: Sequence<Char>): Int {
        return findMarker(input, 14)
    }

    private fun findMarker(input: Sequence<Char>, dataSize: Int): Int {
        return input.windowed(dataSize).indexOfFirst {
            it.toSet().size == dataSize
        }
    }
}
