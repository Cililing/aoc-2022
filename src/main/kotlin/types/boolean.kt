package types

fun Boolean.toInt() = if (this) 1 else 0

fun Boolean?.toComparingInt(): Int {
    return when (this) {
        null -> 0
        true -> 1
        false -> -1
    }
}
