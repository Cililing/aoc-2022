import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

abstract class BaseChallenge<Parsed> {

    // Result wrapper (result + execution time)
    data class Result(val executionTime: Duration, val result: String)

    // inputPath relative (to root directory) path to the input file
    protected abstract val inputPath: String

    // read and parsed input (each line is a single item in the list)
    protected val input by lazy {
        parse(File(inputPath).readLines())
    }

    // declare parsing (how to convert the input to the Parsed type
    protected abstract fun parse(input: List<String>): Parsed

    protected abstract fun ex1(): String

    protected abstract fun ex2(): String

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
            ex1()
        }

        val ex2 = measureTimedValue {
            ex2()
        }

        return Pair(Result(ex1.duration, ex1.value), Result(ex2.duration, ex2.value))
    }
}
