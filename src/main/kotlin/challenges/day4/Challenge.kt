package challenges.day4

import extractor.Extractor
import ranges.fullyContains
import ranges.overlap
import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

typealias Input = List<Pair<IntRange, IntRange>>

@Challenge(4)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Input {
        return input.map {
            val extractor = Extractor(it)
            (extractor.nextInt()..extractor.nextInt()) to
                (extractor.nextInt()..extractor.nextInt())
        }
    }

    @Task("ex1")
    fun ex1(input: Input): Int {
        return input.count {
            it.first.fullyContains(it.second) || it.second.fullyContains(it.first)
        }
    }

    @Task("ex2")
    fun ex2(input: Input): Int {
        return input.count {
            it.first.overlap(it.second) || it.second.overlap(it.first)
        }
    }
}
