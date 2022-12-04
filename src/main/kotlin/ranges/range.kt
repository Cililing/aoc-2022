package ranges

fun IntRange.fullyContains(other: IntRange): Boolean {
    return this.toSet().intersect(other.toSet()).size == other.count()
}

fun IntRange.overlap(other: IntRange): Boolean {
    return this.toSet().intersect(other.toSet()).isNotEmpty()
}
