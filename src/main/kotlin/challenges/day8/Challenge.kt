package challenges.day8

import ndarray.toDown
import ndarray.toLeft
import ndarray.toRight
import ndarray.toUp
import org.jetbrains.kotlinx.multik.api.toNDArray
import org.jetbrains.kotlinx.multik.ndarray.data.D2Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.foldMultiIndexed
import org.jetbrains.kotlinx.multik.ndarray.operations.mapMultiIndexed
import org.jetbrains.kotlinx.multik.ndarray.operations.max
import runner.Benchmark
import runner.Challenge
import runner.Mapper
import runner.Task
import types.toInt

typealias Input = D2Array<Int>

@Challenge(8)
@Benchmark(1000)
class Challenge {

    @Mapper
    fun parse(input: List<String>): D2Array<Int> {
        return input.map {
            it.map { it.digitToInt() }
        }.toNDArray().asD2Array()
    }

    @Task("ex1")
    fun ex1(input: Input): Int {
        val maxIndex = input.shape[0] - 1
        return input.foldMultiIndexed(0) { dim, acc, v ->
            val isShorter = { it: Pair<Int, Int> ->
                input[it.first][it.second] < v
            }

            acc + (
                (dim[0] == 0 || dim[1] == 0 || dim[0] == maxIndex || dim[1] == maxIndex) ||
                    dim.toUp().all(isShorter) ||
                    dim.toDown(maxIndex).all(isShorter) ||
                    dim.toLeft().all(isShorter) ||
                    dim.toRight(maxIndex).all(isShorter)
                )
                .toInt()
        }
    }

    @Task("ex2")
    fun ex2(input: Input): Int {
        val maxIndex = input.shape[0] - 1
        return input.mapMultiIndexed { dim, v ->
            if (dim[0] == 0 || dim[1] == 0 || dim[0] == maxIndex || dim[1] == maxIndex) {
                return@mapMultiIndexed 0
            }

            val viewCounter = { it: Pair<Int, Int> ->
                input[it.first][it.second] < v
            }

            val viewUp = dim.toUp()
            val viewDown = dim.toDown(maxIndex)
            val viewLeft = dim.toLeft()
            val viewRight = dim.toRight(maxIndex)

            val viewUpDistance = viewUp.takeWhile(viewCounter).count()
            val viewDownDistance = viewDown.takeWhile(viewCounter).count()
            val viewLeftDistance = viewLeft.takeWhile(viewCounter).count()
            val viewRightDistance = viewRight.takeWhile(viewCounter).count()

            val incIfNeeded = { allSize: Int, size: Int -> // if allSize=size all trees visible, no need to inc
                if (allSize == size) size else size + 1
            }

            incIfNeeded(viewUp.size, viewUpDistance) *
                incIfNeeded(viewDown.size, viewDownDistance) *
                incIfNeeded(viewLeft.size, viewLeftDistance) *
                incIfNeeded(viewRight.size, viewRightDistance)
        }.max()!!
    }
}
