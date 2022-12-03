package stats

import org.nield.kotlinstatistics.median
import org.nield.kotlinstatistics.percentile
import org.nield.kotlinstatistics.standardDeviation
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

data class Stats(
    val max: Duration,
    val min: Duration,
    val avg: Duration,
    val percentile: Map<String, Duration>,
    val stdDev: Duration,
    val median: Duration
)

fun List<Duration>.stats(): Stats {
    val nanos = this.map { it.inWholeNanoseconds }
    return Stats(
        max = this.max(),
        min = this.min(),
        avg = nanos.average().nanoseconds,
        percentile = mapOf(
            "0.9" to nanos.percentile(0.9).nanoseconds,
            "0.95" to nanos.percentile(0.95).nanoseconds,
            "0.99" to nanos.percentile(0.99).nanoseconds,
        ),
        stdDev = nanos.standardDeviation().nanoseconds,
        median = nanos.median().nanoseconds
    )
}

fun List<Any?>.allTheSame(): Boolean {
    return this.isEmpty() || this.toSet().size == 1
}
