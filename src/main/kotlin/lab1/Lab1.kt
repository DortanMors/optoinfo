package lab1

import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.ggplot
import space.kscience.kmath.complex.ComplexField.exp
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.times
import kotlin.math.exp
import kotlin.math.pow

var B = 0.1
const val A = 1
val xRange = DoubleRange(-5.0, 5.0, 1000)
val ksiRange = DoubleRange(-25.0, 25.0, 1000)

val f = { x: Double -> exp(i * B * x) }

val kor = { ksi: Double, x: Double ->
    i * exp(-A * (x - ksi).pow(2))
} // Fomin: variant 12

fun main() {
    f.integralTransform(ksiRange, xRange, kor).run {
        println(ggsave(ggplot(ksiRange to args()), "Args of f default.png"))
        println(ggsave(ggplot(ksiRange to absolutes()), "Absolutes of f default.png"))
    }
}