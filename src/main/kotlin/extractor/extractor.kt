package extractor

class NoMoreResults : Exception("no more result present")

/**
 * IntExtractor process the input to extract ints from it.
 * For example, with a string: `21papierz37` calling `nextInt` twice will return 21 and 37.
 * If there is no more results present the exception will be called.
 */
class IntExtractor(input: String) {
    private var chars = input.toList()
    private var currentIndex = 0

    fun nextInt(): Int {
        val buf = StringBuilder()

        var current = '0'
        while (current.isDigit() && currentIndex < chars.size) {
            current = chars[currentIndex]
            currentIndex++
            if (current.isDigit()) {
                buf.append(current)
            }
        }

        if (buf.isEmpty()) {
            throw NoMoreResults()
        }

        return buf.toString().toInt()
    }
}
