import runner.flow
import runner.getAllChallenges

object Main

fun main(args: Array<String>) {
    val enableBenchmark = System.getenv("ENABLE_BENCHMARK")?.toBooleanStrictOrNull()
    getAllChallenges(Main).sorted().forEach { flow(it, enableBenchmark ?: false) }
}
