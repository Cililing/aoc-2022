package runner

/**
 * Prepare methods are called after instance creation, but before challenge execution
 * Use it for example for runtime-data preparation
 *
 * Method must have no arguments.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Prepare
