package runner

import stats.allTheSame
import stats.stats
import java.io.File
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@OptIn(ExperimentalTime::class)
fun flow(className: String, enableBenchmark: Boolean = false) {
    println("------------------------------------------------")
    println("Start run for $className")

    // Create instance
    val constructor = Class.forName(className).declaredConstructors.minByOrNull {
        it.parameterCount
    } ?: throw IllegalArgumentException("class must have the default (0-parameters) constructor enabled")
    constructor.isAccessible = true
    val instance = constructor.newInstance()

    // Get challenge metadata
    val challenge = instance.javaClass.annotations.find { it is Challenge } as? Challenge
        ?: throw IllegalArgumentException("class is not a Challenge")
    println("Challenge data: $challenge")

    // Find and call prepare methods
    instance.javaClass.methods.filter { method ->
        method.annotations.any { it is Prepare }
    }.forEach {
        println("Preparing challenge: [${instance.javaClass.canonicalName}#${it.name}]")
        it.invoke(instance)
    }

    // Obtain data for test
    val testDataFile = File("input/day${challenge.day}_test.txt")
    val inputDataFile = File("input/day${challenge.day}.txt").apply {
        if (!this.exists()) {
            throw IllegalStateException("input data must exists (input/dayX.txt)")
        }
    }

    // If file exists parse them with mapper.
    val mapper = instance.javaClass.methods.find { method ->
        method.annotations.any { it is Mapper }
    } ?: throw IllegalStateException("mapper must be defined")

    val testData = if (!testDataFile.exists()) null else {
        measureTimedValue {
            mapper.invoke(instance, testDataFile.readLines())
        }
    }
    val inputData = measureTimedValue {
        mapper.invoke(instance, inputDataFile.readLines())
    }

    println("Test data parsed in ${testData?.duration ?: "[TEST DATA NOT PRESENT]"}")
    println("Input data parsed in ${inputData.duration}")

    // Get run settings (from benchmark, if present)
    val benchmark = challenge.javaClass.annotations.find { it is Benchmark } as? Benchmark
    val tries = if (enableBenchmark) benchmark?.n ?: 1 else 1

    // Now, find tasks and run them with input data.
    val runs = instance.javaClass.methods.filter { method ->
        method.annotations.any { it is Task }
    }.associateBy {
        it.annotations.find { it is Task } as Task
    }

//    val result = mapOf<Task, List<Result>>()

    runs.mapValues { task ->
        (0 until tries).map {
            val testResult = if (testData != null) measureTimedValue {
                task.value.invoke(instance, testData.value)
            } else null

            val runResult = measureTimedValue {
                task.value.invoke(instance, inputData.value)
            }

            testResult to runResult
        }
    }.onEach { entry ->
        // verify all results are the same
        val taskId = entry.key.id
        val testResults = entry.value.map { it.first?.value }
        val runResults = entry.value.map { it.second.value }

        if (!testResults.allTheSame()) {
            throw IllegalStateException("results of task $taskId/test are not deterministic: ${testResults.toSet()}")
        }
        if (!runResults.allTheSame()) {
            throw IllegalStateException("results of task $taskId/run are not deterministic: ${testResults.toSet()}")
        }
    }.forEach {
        val testResult = it.value.map { it.first?.value ?: Unit }.firstOrNull()
        val testStats = it.value.map { it.first?.duration ?: Duration.ZERO }.stats()
        val runStats = it.value.map { it.second.duration }.stats()
        val runResult = it.value.map { it.second.value }.first()

        println(
            """
            Task ${it.key.id} results:
                Test result:    [$testResult, ${testStats.avg}]
                Test stats:     [$testStats]
                Run results:    [$runResult, ${runStats.avg}]
                Run stats:      [$runStats]
            """.trimIndent()
        )
    }

    println()
}
