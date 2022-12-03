import conctract.Pure
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class BaseChallenge<Parsed> {

    // Result wrapper (result + execution time)
    data class Result(val executionTime: Duration, val result: String) {
        companion object {
            val invalid = Result(Duration.ZERO, "invalid-2137")
        }
    }

    // inputPath relative (to root directory) path to the input file
    protected abstract val inputPath: String
    private val testInputPath by lazy { inputPath.replace(".", "_test.") }

    // read and parsed input (each line is a single item in the list)
    private val input by lazy { parse(File(inputPath).readLines()) }

    private val testInput by lazy { parse(File(testInputPath).readLines()) }

    // declare parsing (how to convert the input to the Parsed type
    protected abstract fun parse(input: List<String>): Parsed

    @Pure
    protected abstract fun ex1(input: Parsed): String

    @Pure
    protected abstract fun ex2(input: Parsed): String

    @OptIn(ExperimentalTime::class)
    fun runTest(): Pair<Result, Result> {
        if (!File(testInputPath).exists()) {
            return Pair(Result.invalid, Result.invalid)
        }

        val ex1 = measureTimedValue {
            ex1(testInput)
        }

        val ex2 = measureTimedValue {
            ex2(testInput)
        }

        return Pair(Result(ex1.duration, ex1.value), Result(ex2.duration, ex2.value))
    }

    // call prepare to parse the input (for not poisoning result-time)
    @OptIn(ExperimentalTime::class)
    fun prepare(): Duration {
        return measureTime {
            input.hashCode()
        }
    }

    @OptIn(ExperimentalTime::class)
    fun run(): Pair<Result, Result> {
        val ex1 = measureTimedValue {
            ex1(input)
        }

        val ex2 = measureTimedValue {
            ex2(input)
        }

        return Pair(Result(ex1.duration, ex1.value), Result(ex2.duration, ex2.value))
    }
}
