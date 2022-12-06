package challenges.day6

data class DataPackage(val chars: List<Char>, val position: Int, val markerSize: Int = 14) {
    fun isMarker(): Boolean {
        return chars.toSet().size == markerSize
    }

    fun next(char: Char): DataPackage {
        return this.copy(
            chars = chars.drop(1) + char,
            position = position + 1
        )
    }
}