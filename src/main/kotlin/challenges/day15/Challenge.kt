package challenges.day15

import runner.Challenge
import runner.Mapper
import runner.Task

typealias Input = List<String>

@Challenge(15)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Input {
        return listOf()
    }

    @Task("ex1")
    fun ex1(input: Input): String {
        return input.toString()
    }

    @Task("ex2")
    fun ex2(input: Input): String {
        return input.toString()
    }
}
