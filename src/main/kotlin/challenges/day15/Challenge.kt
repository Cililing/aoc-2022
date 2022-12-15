package challenges.day15

import extractor.extractor
import runner.Challenge
import runner.Mapper
import runner.Task
import types.toComparingInt
import kotlin.math.abs

data class Position(val x: Long, val y: Long) {
    fun distance(other: Position): Long {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }
}

data class Sensor(val sensor: Position, val beacon: Position)

@Challenge(15)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<Sensor> {
        return input.map {
            val positions = it.extractor().nextLongs()
            Sensor(
                Position(positions[0], positions[1]),
                Position(positions[2], positions[3])
            )
        }
    }

    @Task("ex1")
    fun ex1(input: List<Sensor>): Any {
        // https://en.wikipedia.org/wiki/Taxicab_geometry
        val allOccupied = input.map { sensor ->

            // get sensor-line position
            val maxDistance = abs(sensor.sensor.distance(sensor.beacon))
            val moveLeft = sensor.sensor.x > sensor.beacon.x
            val maxLeft = generateSequence(sensor.sensor) {
                Position(it.x - moveLeft.toComparingInt(), it.y)
            }.takeWhile { sensor.sensor.distance(it) <= maxDistance }
                .last()
            val xMaxDistance = abs(sensor.sensor.x) + abs(maxLeft.x)

            val all = ((sensor.sensor.y - xMaxDistance)..(sensor.sensor.y + maxDistance)).mapIndexed { index, l ->
                if (index <= sensor.sensor.y) {
                    // increase ...
                    l to (sensor.sensor.x - l)..(sensor.sensor.x + l)
                } else {
                    l to (sensor.sensor.x - xMaxDistance + l) .. (sensor.sensor.x - maxDistance)
                }
            }


            // generate square
//            val allToCheck = ((sensor.sensor.x - xDistance)..sensor.sensor.x + xDistance).map { x ->
//                ((sensor.sensor.y - xDistance..sensor.sensor.y + xDistance)).map { y ->
//                    Position(x, y)
//                }
//            }
//
//            allToCheck.flatten().filter { sensor.sensor.distance(it) <= maxDistance }
        }

        return input.toString()
    }

    @Task("ex2")
    fun ex2(input: Sensor): Any {
        return input.toString()
    }
}
