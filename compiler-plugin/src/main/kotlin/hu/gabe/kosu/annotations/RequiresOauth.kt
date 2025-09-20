package hu.gabe.kosu.annotations

import hu.gabe.kosu.Scope

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.SOURCE)
@MustBeDocumented
annotation class RequiresOauth(vararg val requiredScopes: Scope = [Scope.PUBLIC])