package challenges.day02

enum class Shape(val points: Int) {
    Rock(1), Paper(2), Scissors(3);

    fun getWinningOpposite(): Shape {
        return when (this) {
            Rock -> Paper
            Paper -> Scissors
            Scissors -> Rock
        }
    }

    fun getLoosingOpposite(): Shape {
        return when (this) {
            Rock -> Scissors
            Paper -> Rock
            Scissors -> Paper
        }
    }
}
