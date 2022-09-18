package com.ssau.optoinformatic.math

import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.r
import space.kscience.kmath.complex.theta

operator fun ((Double) -> Complex).invoke(x: Array<Double>) = Array(x.size) { index -> invoke(x[index]) }
operator fun ((Double) -> Complex).invoke(x: List<Double>) = Array(x.size) { index -> invoke(x[index]) }

fun List<Complex>.args() = map { it.theta }
fun Array<Complex>.args() = map { it.theta }

fun List<Complex>.absolutes() = map { it.r }
fun Array<Complex>.absolutes() = map { it.r }
