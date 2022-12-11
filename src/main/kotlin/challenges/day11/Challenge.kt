package challenges.day11

import extractor.extractor
import runner.Challenge
import runner.Mapper
import runner.Task
import java.math.BigInteger

data class Monkey(
    val id: Int,
    val items: List<Long>,
    val operation: (Long) -> Long,

    val dividableBy: Long,
    val ifTrue: Int,
    val ifFalse: Int,

    val inspectedItems: Int = 0
)

@Challenge(11)
class Challenge {

    @Mapper
    fun parse(input: List<String>): Map<Int, Monkey> {
        val monkeys = input.chunked(7).map { line ->
            val id = line[0].extractor().nextInt()
            val startingItems = line[1].extractor().nextLongs()

            val operationExtractor = line[2].extractor()
            val operationAction = operationExtractor.nextChar(setOf('+', '*'))
            val operationNumber = operationExtractor.nextLongOrNull()
            val operation = when (operationAction) {
                '+' -> { x: Long ->
                    x + (operationNumber ?: x)
                }

                '*' -> { x: Long ->
                    x * (operationNumber ?: x)
                }

                else -> throw IllegalArgumentException("illegal char")
            }

            val divBy = line[3].extractor().nextLong()
            val ifTrue = line[4].extractor().nextInt()
            val ifFalse = line[5].extractor().nextInt()

            Monkey(id, startingItems.toMutableList(), operation, divBy, ifTrue, ifFalse)
        }
        return monkeys.associateBy { it -> it.id }
    }

    @Task("ex1")
    fun ex1(input: Map<Int, Monkey>): Int {
        val lcm = lcm(input)

        return (0 until 20).fold(input) { acc, _ ->
            processRound(acc, lcm, 3)
        }.values
            .sortedByDescending { it.inspectedItems }
            .take(2)
            .fold(1) { acc, monkey -> acc * monkey.inspectedItems }
    }

    @Task("ex2")
    fun ex2(input: Map<Int, Monkey>): BigInteger {
        val lcm = lcm(input)
        return (0 until 10000).fold(input) { acc, _ ->
            processRound(acc, lcm)
        }.values
            .sortedByDescending { it.inspectedItems }
            .take(2)
            .map { it.inspectedItems.toBigInteger() }
            .fold(BigInteger.ONE) { acc, big -> acc * big }
    }

    private fun processRound(input: Map<Int, Monkey>, lcm: Long, divBy: Long = 1): Map<Int, Monkey> {
        return input.keys.fold(input) { acc, v ->
            process(v, acc, lcm, divBy)
        }
    }

    private fun process(monkeyNo: Int, input: Map<Int, Monkey>, lcm: Long, divBy: Long): Map<Int, Monkey> {
        val monkey = input[monkeyNo] ?: throw IllegalArgumentException("no such a monkey")
        val monkeyStack = input.mapValues { it.value.items }

        val resultStacks = monkey.items.fold(monkeyStack) { stacks, item ->
            // we need to know only the possible dividers, not the actual worry lvl
            val worryLvl = (monkey.operation(item) / divBy) % lcm
            val addTo = if (worryLvl % monkey.dividableBy == 0L) monkey.ifTrue else monkey.ifFalse

            stacks.toMutableMap().apply {
                this[monkeyNo] = listOf()
                this[addTo] = this[addTo]?.plus(worryLvl) ?: throw IllegalArgumentException("no such a monkey")
            }
        }

        return input.mapValues {
            it.value.copy(
                inspectedItems = it.value.inspectedItems + if (it.key == monkeyNo) it.value.items.size else 0,
                items = resultStacks[it.key] ?: throw IllegalArgumentException("no such a monkey")
            )
        }
    }

    private fun lcm(input: Map<Int, Monkey>): Long {
        return input.map { it.value.dividableBy }.reduce { acc, v -> acc * v }
    }
}
