import runner.flow
import runner.getAllChallenges

object Main

fun main(args: Array<String>) {
    val enableBenchmark = System.getenv("ENABLE_BENCHMARK")?.toBooleanStrictOrNull()
    getAllChallenges(Main)
        .sorted()
        // .filter { it == "challenges.day14.Challenge" }
        .onEach { println("found class: $it") }
        .forEach { flow(it, enableBenchmark ?: false) }
}
