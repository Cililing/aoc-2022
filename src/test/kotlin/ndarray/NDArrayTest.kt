package ndarray

import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.assertEquals

class NDArrayTest {

    companion object {
        private val d2MatrixSquared = listOf(
            listOf(0, 1, 2, 3, 4),
            listOf(5, 6, 7, 8, 9),
            listOf(10, 11, 12, 13, 14),
            listOf(15, 16, 17, 18, 19),
            listOf(20, 21, 22, 23, 24)
        ).toNDArray().asD2Array()

        private val d2MatrixSquaredD = d2MatrixSquared.shape[0] - 1

        @Suppress("ArrayInDataClass") // won't be compared, just definition
        data class TestCase(
            val startPos: IntArray = intArrayOf(1, 1),
            val input: D2Array<Int> = d2MatrixSquared,
            val inputD: Int = d2MatrixSquaredD,
            val expectedUpValues: List<Int> = listOf(1),
            val expectedDownValues: List<Int> = listOf(11, 16, 21),
            val expectedLeftValues: List<Int> = listOf(5),
            val expectedRightValues: List<Int> = listOf(7, 8, 9),
            val expectedUpLeftValues: List<Int> = listOf(0),
            val expectedUpRightValues: List<Int> = listOf(2),
            val expectedDownLeftValues: List<Int> = listOf(10),
            val expectedDownRightValues: List<Int> = listOf(12, 18, 24)
        )

        private val pairMapper = { p: Pair<Int, Int> ->
            d2MatrixSquared[p.first][p.second]
        }

        @JvmStatic
        fun getTestData(): Iterable<TestCase> {
            return listOf(
                TestCase(),
                TestCase(
                    startPos = intArrayOf(2, 2),
                    expectedUpValues = listOf(7, 2),
                    expectedDownValues = listOf(17, 22),
                    expectedLeftValues = listOf(11, 10),
                    expectedRightValues = listOf(13, 14),
                    expectedUpLeftValues = listOf(6, 0),
                    expectedUpRightValues = listOf(8, 4),
                    expectedDownLeftValues = listOf(16, 20),
                    expectedDownRightValues = listOf(18, 24)
                ),
                TestCase(
                    startPos = intArrayOf(0, 0),
                    expectedUpValues = listOf(),
                    expectedDownValues = listOf(5, 10, 15, 20),
                    expectedLeftValues = listOf(),
                    expectedRightValues = listOf(1, 2, 3, 4),
                    expectedUpLeftValues = listOf(),
                    expectedUpRightValues = listOf(),
                    expectedDownLeftValues = listOf(),
                    expectedDownRightValues = listOf(6, 12, 18, 24)
                )
            )
        }
    }

    @ParameterizedTest()
    @MethodSource("getTestData")
    fun runTests(tc: TestCase) {
        assertEquals(tc.expectedUpValues, tc.startPos.toUp().map(pairMapper))
        assertEquals(tc.expectedDownValues, tc.startPos.toDown(tc.inputD).map(pairMapper))
        assertEquals(tc.expectedLeftValues, tc.startPos.toLeft().map(pairMapper))
        assertEquals(tc.expectedRightValues, tc.startPos.toRight(tc.inputD).map(pairMapper))
        assertEquals(tc.expectedUpLeftValues, tc.startPos.toUpLeft().map(pairMapper))
        assertEquals(tc.expectedUpRightValues, tc.startPos.toUpRight(tc.inputD).map(pairMapper))
        assertEquals(tc.expectedDownLeftValues, tc.startPos.toDownLeft(tc.inputD).map(pairMapper))
        assertEquals(tc.expectedDownRightValues, tc.startPos.toDownRight(tc.inputD).map(pairMapper))
    }
}
