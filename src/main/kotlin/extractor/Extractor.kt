package extractor

/**
 * IntExtractor process the input to extract ints from it.
 * For example, with a string: `21papierz37` calling `nextInt` twice will return 21 and 37.
 * If there is no more results present the exception will be called.
 */
class Extractor(private val originalInput: String) {

    private var input = originalInput.toList()

    fun nextLetters(): String = next { it.isLetter() }

    fun nextInt(): Int = next { it.isDigit() }.toInt()

    fun nextIntOrNull(): Int? = next { it.isDigit() }.toIntOrNull()

    fun nextInts(): List<Int> = generateSequence { nextIntOrNull() }.toList()

    fun nextLong(): Long = next { it.isDigit() }.toLong()

    fun nextLongOrNull(): Long? = next { it.isDigit() }.toLongOrNull()

    fun nextLongs(): List<Long> = generateSequence { nextLongOrNull() }.toList()

    fun nextChar(possibleChars: Set<Char>): Char = next { it in possibleChars }.first()

    fun reset() {
        this.input = originalInput.toList()
    }

    private fun next(take: (Char) -> Boolean): String {
        val prefix = input.dropWhile { !take(it) }
        val next = prefix.takeWhile(take)
        input = prefix.drop(next.size)
        return next.toList().joinToString("")
    }
}

fun String.extractor(): Extractor {
    return Extractor(this)
}
