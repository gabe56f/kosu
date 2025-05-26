package hu.gabe.kosu.annotations

import hu.gabe.kosu.auth.Scope

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER,
    AnnotationTarget.PROPERTY_GETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RequiresOauth(vararg val requiredScopes: Scope = [Scope.PUBLIC])