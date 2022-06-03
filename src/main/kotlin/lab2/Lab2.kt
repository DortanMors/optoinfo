package lab2

import lab1.*
import space.kscience.kmath.complex.Complex
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

val inputF: ((Double) -> Complex) = { x -> Complex(exp(-abs(x))) } // Variant 12
val gaussF: ((Double) -> Complex) = { x -> Complex(exp(-x.pow(2))) }

fun main() {
// Task 2
    var n = 100
    var c = 5.0
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val args = xRange.asArray()
    val area = SampledArea(gaussF.invoke(args), xRange, xRange.step)
    plotToFile(area, "exp(-($X_LABEL^2))", "gauss beam")
// Task 3
    var m = 1024
    val fourierArea = area.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea, "$FKSI_LABEL[exp(-($X_LABEL^2))]", "fourier gauss beam")
// Task 4
    var b = fourierArea.range.right
    val ksi4Range = DoubleRange(-b, b, m, KSI_LABEL)
    val x4Range = DoubleRange(-c, c, n, X_LABEL)
    val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range, ksi4Range.step)
    plotToFile(rectFourierArea, "$FKSI_LABEL($KSI_LABEL)", "rect fourier gauss beam")
// Task 5

// Task 6
    val area6 = SampledArea(inputF.invoke(args), xRange, xRange.step)
    val fourierArea6 = area6.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea6, "$FKSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam")
}

fun plotToFile(area: SampledArea, fLabel: String, title: String) {
    plotOnRange(area.range, area.numbers.toList().args(), fLabel, "$title args")
    plotOnRange(area.range, area.numbers.toList().absolutes(), fLabel, "$title absolutes")
}

fun DoubleRange.asArray() = Array(size) { index ->
    left + index * step
}