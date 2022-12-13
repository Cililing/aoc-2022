package challenges.day13

sealed class SignalType(open val label: String? = null) {
    object SignalOpen : SignalType()

    data class SignalInt(val s: Int, override val label: String? = null) : SignalType()
    data class SignalList(val s: List<SignalType>, override val label: String? = null) : SignalType()

    fun isLabeled(): Boolean {
        return this.label != null
    }
}
