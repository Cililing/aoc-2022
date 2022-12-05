package extractor

class NoMoreResults : Exception("no more result present")

/**
 * IntExtractor process the input to extract ints from it.
 * For example, with a string: `21papierz37` calling `nextInt` twice will return 21 and 37.
 * If there is no more results present the exception will be called.
 */
class Extractor(input: String) {
    private var chars = input.toList()
    private var currentIndex = 0

    fun nextLetters(): String = next { it.isLetter() }

    fun nextInt(): Int = next { it.isDigit() }.toInt()

    private fun next(take: (Char) -> Boolean): String {
        val buf = StringBuilder()

        // drop until the number is not present
        var current = chars[currentIndex]
        while (!take(current)) {
            current = chars[currentIndex++]
        }

        // number present, into buf
        while (take(current)) {
            buf.append(current)

            if (currentIndex < chars.size) {
                current = chars[currentIndex++]
            } else {
                break
            }
        }

        return buf.toString()
    }
}
