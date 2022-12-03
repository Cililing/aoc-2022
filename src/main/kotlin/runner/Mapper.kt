package runner

/**
 * Mapper is method converting input (List<String>) into required data type.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Mapper
