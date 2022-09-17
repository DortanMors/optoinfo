package lab1

import com.ssau.plot.plotDefaultFToFile
import com.ssau.plot.plotTransformToFile
import space.kscience.kmath.complex.ComplexField.exp
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.times
import kotlin.math.exp
import kotlin.math.pow

var beta = 0.1
var alpha = 1.0
var c = 5.0
var p = -25.0
var q = 25.0
var n = 1000
var m = 1000

val f = { x: Double -> exp(i * beta * x) }

val kor = { ksi: Double, x: Double ->
    i * exp(-alpha * (x - ksi).pow(2))
} // Fomin: variant 12

fun main() {
    // Task 1
    listOf(0.1, 100.0, 0.001, -0.1, -0.001).forEach(::plotDefaultFToFileDefault)

    // Task 2
    plotTransformToFileDefault(p1 = p, q1 =q)

    // Task 3
    plotTransformToFileDefault(p1 = p * 2, q1 = q * 2)
    plotTransformToFileDefault(p1 = p * 0.5, q1 = q * 0.5)

    // Task 4
    listOf(0.1, 100.0, 0.001).forEach { plotTransformToFileDefault(a = it) }

    // Task 5
    listOf(c, 1.0, 100.0).forEach { plotTransformToFileDefault(c1 = it) }

}

fun plotTransformToFileDefault(a: Double = alpha, p1: Double = p, q1: Double = q, c1: Double = c) =
    plotTransformToFile(a, p1, q1, c1, n, m, f, kor)

fun plotDefaultFToFileDefault(b: Double) = plotDefaultFToFile(b, c, n, f)