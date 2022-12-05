package challenges.day5

import extractor.Extractor
import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task

@Challenge(5)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Input {
        val inputSplitLine = input.indexOfFirst { it.isBlank() }
        val lines = input[inputSplitLine - 1]
        val linesNo = lines.takeLastWhile { it.isDigit() }.toInt()

        val craneColumns = (1 until linesNo + 1).associateWith {
            mutableListOf<Char>()
        }

        input.take(inputSplitLine - 1)
            .map { it.chunked(4).withIndex().toList() }
            .reversed()
            .onEach { crateRow ->
                crateRow.filter { it.value.isNotBlank() }.onEach { element ->
                    val extractor = Extractor(element.value)
                    craneColumns[element.index + 1]!!.add(0, extractor.nextLetters()[0])
                }
            }

        val procedures = input.drop(inputSplitLine + 1)
            .map {
                val extractor = Extractor(it)
                Procedure(extractor.nextInt(), extractor.nextInt(), extractor.nextInt())
            }

        return Input(craneColumns.mapValues { Line(it.value.toList()) }, procedures)
    }

    @Task("ex1")
    fun ex1(input: Input): Any {
        return proceedProcedures(input, true).values
            .map { it.stack.first() }
            .joinToString("")
    }

    @Task("ex2")
    fun ex2(input: Input): String {
        return proceedProcedures(input, false).values
            .map { it.stack.first() }
            .joinToString("")
    }

    private fun proceedProcedures(input: Input, cratesReverseOrder: Boolean): Map<Int, Line> {
        return input.procedures.fold(input.lines) { acc, procedure ->
            val crates = acc[procedure.from]!!.stack.take(procedure.cratesNo).let {
                if (cratesReverseOrder) it.reversed() else it
            }
            val fromLine = acc[procedure.from]!!.stack.drop(procedure.cratesNo)
            val toLine = crates + acc[procedure.to]!!.stack

            return@fold acc.toMutableMap().apply {
                this[procedure.from] = Line(fromLine)
                this[procedure.to] = Line(toLine)
            }.toMap()
        }
    }
}
