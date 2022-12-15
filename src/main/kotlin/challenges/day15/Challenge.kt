package challenges.day15

import extractor.extractor
import runner.Challenge
import runner.Mapper
import runner.Task
import kotlin.math.abs

data class Position(val x: Long, val y: Long) {
    fun distance(other: Position): Long {
        return abs(this.x - other.x) + abs(this.y - other.y)
    }
}

data class LogEntry(val sensor: Position, val beacon: Position) {

    val maxDistance by lazy {
        sensor.distance(beacon)
    }
}

@Challenge(15)
class Challenge {

    @Mapper
    fun parse(input: List<String>): List<LogEntry> {
        return input.map {
            val positions = it.extractor().nextLongs()
            LogEntry(
                Position(positions[0], positions[1]),
                Position(positions[2], positions[3])
            )
        }
    }

    @Task("ex1")
    fun ex1(input: List<LogEntry>): Any {
        val yRow = if (input.count() > 20) 2000000 else 10 // stupid idea how to determine if it's test input

        val noBeacon = mutableSetOf<Long>()
        input.forEach {

            val sensorRange = it.maxDistance
            val yDistance = abs(yRow - it.sensor.y)

            if (yDistance <= sensorRange) {
                // https://en.wikipedia.org/wiki/Taxicab_geometry
                // d = abs(x1 - x2) + abs(y1 - y2)
                // abs(x1 - x2) = abs(y1 - y2) - d

                val xDist = sensorRange - yDistance
                ((it.sensor.x - xDist)..(it.sensor.x + xDist)).forEach {
                    noBeacon.add(it)
                }
            }
        }

        // some fields may be occupied by the beacon itself
        val beaconsOnLine = input.map { it.beacon.y }.filter { it == yRow.toLong() }

        return (noBeacon - beaconsOnLine.toSet()).size
    }

    @Task("ex2")
    fun ex2(input: List<LogEntry>): Any? {
        return null
    }
}
