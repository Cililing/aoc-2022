package stack

typealias Stack<T> = ArrayDeque<T>

inline fun <T> Stack<T>.push(element: T) = addLast(element)
inline fun <T> Stack<T>.pop() = removeLast()
inline fun <T> Stack<T>.peek() = last()
