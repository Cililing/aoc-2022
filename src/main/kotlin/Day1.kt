@Challenge(1)
@Benchmark(1000)
private class Day1 : BaseChallenge<List<Int?>>() {
    override val inputPath: String = "input/day1.txt"

    override fun parse(input: List<String>): List<Int?> {
        return input.map { it.toIntOrNull() }
    }

    override fun ex1(): String {
        return input
            .runningFold(0) { acc, i -> if (i == null) 0 else acc + i }
            .maxBy { it }
            .toString()
    }

    override fun ex2(): String {
        return input
            .asSequence()
            .runningFold(0) { acc, i -> if (i == null) 0 else acc + i }
            .sortedDescending()
            .take(3)
            .sum()
            .toString()
    }
}
