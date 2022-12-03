package challenges.day2

data class Game(val opponent: Shape, val you: Shape) {
    fun points(): Int {
        return when {
            opponent == you -> 3
            opponent.getWinningOpposite() == you -> 6
            else -> 0
        } + you.points
    }
}
