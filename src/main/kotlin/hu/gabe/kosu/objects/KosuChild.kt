package hu.gabe.kosu.objects

import hu.gabe.kosu.Kosu

open class KosuChild {
    @Transient
    lateinit var kosu: Kosu
}