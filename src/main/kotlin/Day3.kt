import java.lang.IllegalArgumentException
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@Challenge(3)
private class Day3 : BaseChallenge<List<String>>() {

    override val inputPath = "input/day3.txt"

    override fun parse(input: List<String>): List<String> {
        return input
    }

    override fun ex1(input: List<String>): String {
        return input
            .flatMap {
                val pivot = it.length / 2
                val parts = it.chunked(pivot)

                parts[0].toSet().intersect(parts[1].toSet())
            }.sumOf { it.internalCode() }
            .toString()
    }

    override fun ex2(input: List<String>): String {
        return input
            .chunked(3)
            .flatMap {
                val sets = it.map { it.toSet() }
                val common = sets.reduce { acc, s ->
                    acc.intersect(s)
                }

                common
            }
            .sumOf { it.internalCode() }
            .toString()
    }

    @OptIn(ExperimentalTime::class)
    override fun prepare(): Duration {
        return super.prepare().plus(measureTime {
            'a'.internalCode()
        })
    }

    companion object {
        private val internalCodes by lazy {
            val lowercase = ('a'..'z').mapIndexed { index, c ->
                c to index + 1
            }
            val uppercase = ('A'..'Z').mapIndexed { index, c ->
                c to index + 26 + 1
            }

            (lowercase + uppercase).toMap()
        }

        private fun Char.internalCode(): Int {
            return internalCodes[this] ?: throw IllegalArgumentException("letter unknown")
        }
    }
}