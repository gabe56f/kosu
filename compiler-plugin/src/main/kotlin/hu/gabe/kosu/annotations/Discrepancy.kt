package hu.gabe.kosu.annotations

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class Discrepancy(val original: String, val new: String, val message: String)
