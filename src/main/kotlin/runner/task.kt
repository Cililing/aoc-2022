package runner

import java.lang.reflect.Method
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

// Task must return a value
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Task(val id: String)

data class TaskResult(val executionTime: Duration, val result: Any)

@OptIn(ExperimentalTime::class)
fun runTask(receiver: Any, method: Method): TaskResult {
    val t = measureTimedValue {
        method.invoke(receiver)
    }
    return TaskResult(t.duration, t.value)
}

fun runTaskN(receiver: Any, method: Method, n: Int) = (0..n).map { runTask(receiver, method) }
