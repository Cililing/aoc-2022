package conctract

/**
 * Such annotated method should have no side effects.
 * This is not checked, just for Documentation
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Pure
