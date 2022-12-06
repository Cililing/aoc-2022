package challenges.day6

import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task
import java.lang.IllegalStateException

@Challenge(6)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): String {
        return input.first()
    }

    @Task("ex1")
    fun ex1(input: String): Int {
        return findMarker(input, 4).position
    }

    @Task("ex2")
    fun ex2(input: String): Int {
        return findMarker(input, 14).position
    }

    private fun findMarker(input: String, dataSize: Int): DataPackage {
        val firstPackage = DataPackage(input.take(dataSize).toList(), dataSize, dataSize)
        input.drop(dataSize).fold(firstPackage) { acc, c ->
            if (acc.isMarker()) {
                return acc
            }

            acc.next(c)
        }

        throw IllegalStateException("marker not found")
    }
}
