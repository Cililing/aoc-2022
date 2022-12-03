import java.io.File
import java.net.URL
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Challenge(val day: Int)

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Benchmark(val n: Int)

class Main

@OptIn(ExperimentalTime::class)
fun main(args: Array<String>) {
    val enableBenchmark = System.getenv("ENABLE_BENCHMARK")?.toBooleanStrictOrNull()

    getAllChallenges()
        .associateBy { (it.javaClass.annotations.find { it is Challenge } as Challenge) }
        .toSortedMap(compareBy { it.day })
        .forEach {
            println("----------------------------------------")
            println("Running challenge for day: ${it.key.day}")

            val testResult = it.value.runTest()
            if (testResult.first != BaseChallenge.Result.invalid) {
                println("Result 1/Test: ${testResult.first.result} finished in ${testResult.first.executionTime}")
            }
            if (testResult.second != BaseChallenge.Result.invalid) {
                println("Result 2/Test: ${testResult.second.result} finished in ${testResult.second.executionTime}")
            }

            val benchmark = it.value.javaClass.annotations.find { it is Benchmark } as? Benchmark
            val tries = if (enableBenchmark == true) benchmark?.n ?: 1 else 1

            val preparation = it.value.prepare()
            println("Parsing data time: $preparation")

            val results = (0 until tries).map { _ ->
                measureTimedValue {
                    it.value.run()
                }
            }

            val avg1 = results.map { it.value.first.executionTime.inWholeNanoseconds }.average()
                .let { d -> d.nanoseconds }
            val avg2 = results.map { it.value.second.executionTime.inWholeNanoseconds }.average()
                .let { d -> d.nanoseconds }

            println("Result 1: ${results[0].value.first.result}, finished in ${results[0].value.first.executionTime}")
            println("Result 2: ${results[0].value.second.result} finished in ${results[0].value.second.executionTime}")
            println("Day ${it.key.day} finished in ${results[0].duration}.")
            println("Average time ($tries runs): [$avg1, $avg2]")
            println()
        }
}

// based on: https://www.infoworld.com/article/2077477/java-tip-113--identify-subclasses-at-runtime.html
fun getAllChallenges(packageName: String = ""): List<BaseChallenge<*>> {
    // Translate the package name into an absolute path
    var name = packageName
    if (!name.startsWith("/")) {
        name = "/$name"
    }
    name = name.replace('.', '/')

    // Get a File object for the package
    val url: URL = Main::class.java.getResource(name) as URL
    val directory = File(url.file)

    val result = mutableListOf<BaseChallenge<*>>()

    if (directory.exists()) {
        directory.walk()
            .filter { f -> f.isFile && !f.name.contains('$') && f.name.endsWith(".class") }
            .forEach {
                val fullyQualifiedClassName = packageName + it.canonicalPath.removePrefix(directory.canonicalPath)
                    .dropLast(6) // remove .class
                    .replace('/', '.')
                    .removePrefix(".") // handle root
                try {
                    val clazz = Class.forName(fullyQualifiedClassName)

                    if (clazz.annotations.any { ann -> ann is Challenge }) {
                        val instance = Class.forName(fullyQualifiedClassName).getDeclaredConstructor().newInstance()
                        result.add(
                            instance as? BaseChallenge<*>
                                ?: throw IllegalArgumentException("only base challanges may be annotated with Challenge")
                        )
                    }
                } catch (cnfex: ClassNotFoundException) {
                    System.err.println(cnfex)
                } catch (iex: InstantiationException) {
                    // We try to instantiate an interface
                    // or an object that does not have a
                    // default constructor
                } catch (iaex: IllegalAccessException) {
                    // The class is not public
                }
            }
    }

    return result
}
