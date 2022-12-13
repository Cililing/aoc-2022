package challenges.day13

import runner.Challenge
import runner.Mapper
import runner.Task
import stack.peek
import stack.pop
import stack.push
import types.toComparingInt

@Challenge(13)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<SignalPair> {
        val v = input.asSequence()
            .filter { it.isNotBlank() }
            .map(::parseSignal)
            .chunked(2)
            .map { SignalPair(it.first(), it.last()) }
            .toList()

        return v
    }

    @Task("ex1")
    fun ex1(input: List<SignalPair>): Any {
        return input.map {
            ordered(it.signal1, it.signal2)
        }.withIndex()
            .filter { it.value == true }
            .sumOf { it.index + 1 } // indexing from 1
    }

    @Task("ex2")
    fun ex2(input: List<SignalPair>): Any {
        return input.flatMap { listOf(it.signal1, it.signal2) }
            .plus(
                listOf(
                    SignalType.SignalList(
                        label = "2",
                        s = listOf(SignalType.SignalList(listOf(SignalType.SignalInt(2))))
                    ),
                    SignalType.SignalList(
                        label = "6",
                        s = listOf(SignalType.SignalList(listOf(SignalType.SignalInt(6))))
                    )
                )
            )
            .sortedWith { o1, o2 -> ordered(o1, o2).toComparingInt() * -1 } // desc
            .withIndex()
            .filter { it.value.isLabeled() }
            .map { it.index + 1 }
            .fold(1) { acc, i -> acc * i }
    }

    private fun ordered(first: SignalType, second: SignalType): Boolean? { // continue checking on null
        when {
            first is SignalType.SignalInt && second is SignalType.SignalInt -> {
                if (second.s == first.s) {
                    return null
                }

                return second.s >= first.s
            }

            first is SignalType.SignalList && second is SignalType.SignalList -> {
                first.s.zip(second.s).forEach {
                    val ordered = ordered(it.first, it.second)
                    if (ordered != null) {
                        return ordered
                    }
                }

                if (first.s.size == second.s.size) {
                    return null
                }

                return first.s.size < second.s.size
            }

            first is SignalType.SignalInt && second is SignalType.SignalList ->
                return ordered(SignalType.SignalList(listOf(first)), second)

            first is SignalType.SignalList && second is SignalType.SignalInt ->
                return ordered(first, SignalType.SignalList(listOf(second)))

            else -> throw IllegalStateException("should match one of previous cases")
        }
    }

    private fun parseSignal(signal: String): SignalType {
        val stack = ArrayDeque<SignalType>()

        var idx = 0 // skip first
        while (idx < signal.length) {
            // read next char
            var currentChar = signal[idx]

            if (currentChar.isDigit()) { // handle numbers
                val digitBuf = StringBuilder("")
                while (currentChar.isDigit()) {
                    digitBuf.append(currentChar)
                    currentChar = signal[++idx]
                }
                stack.push(SignalType.SignalInt(digitBuf.toString().toInt()))
                continue
            }

            if (currentChar == '[') { // opening, just push
                stack.push(SignalType.SignalOpen)
                idx++
                continue
            }

            if (currentChar == ']') { // handle merging
                val expression = mutableListOf<SignalType>()
                while (stack.peek() != SignalType.SignalOpen) {
                    expression.add(0, stack.pop())
                }
                stack.pop() // drop last ']'
                stack.push(SignalType.SignalList(expression))
                idx++
                continue
            }

            idx++
        }

        return stack.peek()
    }
}
