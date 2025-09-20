package hu.gabe.kosu.child

interface IKosuVisitable {
    // Easier to do this with Collection<Any>
    fun getVisitableChildren(): Collection<Any>
}