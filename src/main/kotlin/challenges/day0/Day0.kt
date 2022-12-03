package challenges.day0

import runner.Challenge
import runner.Mapper
import runner.Prepare
import runner.Task

@Challenge(0)
class Day0 {

    private val computedValue by lazy {
        (0..100).map { it * it }
    }

    @Prepare
    fun calculateComputedValue() {
        computedValue.size
    }

    @Mapper
    fun parse(input: List<String>): List<Set<Char>> {
        return input.map { it.toSet() }
    }

    @Task("ex1")
    fun ex1(input: List<Set<Char>>): String {
        return input.toString()
    }
}
