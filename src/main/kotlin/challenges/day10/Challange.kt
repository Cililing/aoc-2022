package challenges.day10

import runner.Challenge
import runner.Mapper
import runner.Task

sealed class Command {
    object NOOP : Command()
    data class Add(val x: Int) : Command()
}

@Challenge(10)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<Command> {
        return input.map {
            val commandParts = it.split(" ")
            return@map when {
                it == "noop" -> Command.NOOP
                it.startsWith("addx") -> Command.Add(commandParts[1].toInt())
                else -> throw IllegalArgumentException("invalid")
            }
        }
    }

    @Task("ex1")
    fun ex1(input: List<Command>): Int {
        val toExecute = steps(input)
        val register = toExecute.runningFold(1) { acc, v ->
            acc + v.x
        }

        val multiplies = listOf(20, 60, 100, 140, 180, 220)
        val values = multiplies.map { register[it] }

        return multiplies.zip(values).sumOf { it.first * it.second }
    }

    @Task("ex2")
    fun ex2(input: List<Command.Add>): String {
        val toExecute = steps(input)
        val register = toExecute.runningFold(1) { acc, v -> acc + v.x }.drop(1)
        return printCRT(register)
    }

    private fun printCRT(register: List<Int>): String {
        val buffer = StringBuffer()
        register.indices.forEach { idx ->
            val idxFixed = idx % 40

            if (idxFixed == 0) {
                buffer.appendLine()
            }

            val spritePos = register[idx]
            buffer.append(if (idxFixed in listOf(spritePos - 1, spritePos, spritePos + 1)) '#' else '.')
        }
        return buffer.toString()
    }

    // replace Command.NOOP with Add(0) - simplifies calculations
    private fun steps(input: List<Command>): List<Command.Add> {
        return input.fold(listOf(Command.Add(0))) { acc, x ->
            val commands = when (x) {
                is Command.NOOP -> listOf(Command.Add(0))
                is Command.Add -> listOf(Command.Add(0), x)
            }

            acc + commands
        }
    }
}
