@Challenge(1)
private class Day1 : BaseChallenge<List<Int?>>() {
    override val inputPath: String = "input/day1.txt"

    override fun parse(input: List<String>): List<Int?> {
        return input.map { it.toIntOrNull() }
    }

    override fun ex1(): String {
        return input
            .runningReduce { acc, i -> if (i == null) 0 else acc!! + i }
            .mapNotNull { it }
            .maxBy { it }
            .toString()
    }

    override fun ex2(): String {
        return input
            .runningReduce { acc, i -> if (i == null) 0 else acc!! + i }
            .mapNotNull { it }
            .sorted()
            .takeLast(3)
            .sum()
            .toString()
    }
}
