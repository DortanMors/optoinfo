package lab2

import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
import jetbrains.letsPlot.label.ggtitle
import jetbrains.letsPlot.letsPlot
import lab1.*
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.plus
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.ComplexField.unaryMinus
import space.kscience.kmath.operations.pow
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

val inputF: ((Double) -> Complex) = { x -> Complex(exp(-abs(x))) } // Variant 12
val inputF2: ((Double, Double) -> Complex) = { x, y -> Complex(exp(-abs(x) -abs(y))) } // Variant 12
val gaussF: ((Double) -> Complex) = { x -> Complex(exp(-x.pow(2))) }
val gauss2F: ((Double, Double) -> Complex) = { x, y -> Complex(exp(-x.pow(2) -y.pow(2))) }

fun main() {
// Task 2
    val n = 100
    val c = 5.0
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val area = SampledArea(gaussF(xRange), xRange)
    plotToFile(area, "exp(-($X_LABEL^2))", "gauss beam (Task2)")
// Task 3
    val m = 1024
    val fourierArea = area.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea, "$FKSI_LABEL[exp(-($X_LABEL^2))]", "fourier gauss beam (Task3)")
// Task 4
    val b = fourierArea.range.right
    val ksi4Range = DoubleRange(-b, b, n, KSI_LABEL)
    val x4Range = DoubleRange(-c, c, n, X_LABEL)
    val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range)
    plotToFile(rectFourierArea, "$FKSI_LABEL($KSI_LABEL)", "rect fourier gauss beam (Task4)")
// Task 5
    task5(ksi4Range, fourierArea, rectFourierArea)
// Task 6
    val area6 = SampledArea(inputF(xRange), xRange)
    val fourierArea6 = area6.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea6, "$FKSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam (Task6)")
//// Task 7 Analytics
    val analSolution: (Double) -> Complex =
        { ksi -> 2 / ((- i + 2 * ksi * PI) * (i + 2 * ksi * PI)) }
    val analData = analSolution(fourierArea.range)
    task7(ksi4Range, fourierArea, rectFourierArea, analData)
//// Task 8
    task8(32, 32)
    task8(64, 64)
    task8(128, 128)
    task8(128, 512)
    task8(128, 2048)
//// Task 9.2
//    run {
//        val xs = DoubleRange(-c, c, n, X_LABEL)
//        val ys = DoubleRange(-c, c, n, Y_LABEL)
//        val gaussValues = gauss2F(xs, ys)
//        plotHeat(gaussValues, "gauss 2 values") //TODO
//// Task 9.3
//        val gaussTransform = fastFourierTransform(gaussValues)
//        plotHeat(gaussTransform, "gauss 2 transform") //TODO
//// Task 9.6
//        val variantValues = inputF2(xs, ys)
//        plotHeat(variantValues, "variant 2 values")
//        val variantTransformed = fastFourierTransform(variantValues)
//        plotHeat(variantValues, "variant 2 transform")
//    }
}

private operator fun Number.div(complex: Complex): Complex = times(complex.pow(-1.0))


fun task8(n: Int, m: Int) {
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val area = SampledArea(inputF.invoke(xRange.asArray()), xRange)
    val fourierArea = area.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea, "$FKSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam (n=$n, m=$m)")
}

fun plotToFile(area: SampledArea, fLabel: String, title: String) {
    plotOnRange(area.range, area.numbers.toList().args(), "|$fLabel|", "$title args")
    plotOnRange(area.range, area.numbers.toList().absolutes(), "Arg $fLabel", "$title absolutes")
}

fun DoubleRange.asArray() = Array(size) { index ->
    left + index * step
}

fun task5(range: DoubleRange, firstArea: SampledArea, secondArea: SampledArea) {
    twoPlots(
        "Task 5 double absolutes",
        range,
        firstArea.numbers.absolutes(),
        secondArea.numbers.absolutes(),
        "|F1($KSI_LABEL)|",
        "|F2($KSI_LABEL)|"
    )
    twoPlots(
        "Task 5 double args",
        range,
        firstArea.numbers.args(),
        secondArea.numbers.args(),
        "Args F1($KSI_LABEL)",
        "Args F2($KSI_LABEL)"
    )
}

fun task7(range: DoubleRange, firstArea: SampledArea, secondArea: SampledArea, thirdList: Array<Complex>) {
    threePlots(
        range,
        "Task 7 triple absolutes",
        firstArea.numbers.absolutes(), "|F1($KSI_LABEL)|",
        secondArea.numbers.absolutes(), "|F2($KSI_LABEL)|",
        thirdList.absolutes(), "|F3($KSI_LABEL)|"
    )

    threePlots(
        range,
        "Task 7 triple args",
        firstArea.numbers.args(), "Args F1($KSI_LABEL)",
        secondArea.numbers.args(), "Args F2($KSI_LABEL)",
        thirdList.args(), "Args F3($KSI_LABEL)"
    )
}

fun twoPlots(title: String, range: DoubleRange, firstArea: List<Double>, secondArea: List<Double>, firstAreaTitle: String, secondAreaTitle: String) {
    val data = mutableMapOf<String, Any>(
        range.label to range,
        firstAreaTitle to firstArea,
        secondAreaTitle to secondArea
    )
    ggsave(
        letsPlot(data) +
                geomLine(color = Color.GREEN, size = 5.0) {
                    data.keys.run {
                        x = range.label
                        y = firstAreaTitle
                    }
                } +
                geomLine(color = Color.RED, size = 1.0) {
                    data.keys.run {
                        x = range.label
                        y = secondAreaTitle
                    }
                } +
                ggtitle(title),
        title.png
    )
}

fun threePlots(
    range: DoubleRange,
    title: String,
    first: List<Double>,
    firstTitle: String,
    second: List<Double>,
    secondTitle: String,
    third: List<Double>,
    thirdTitle: String
) {
    val data = mutableMapOf<String, Any>(
        range.label to range,
        firstTitle to first,
        secondTitle to second,
        thirdTitle to third
    )
    ggsave(
        letsPlot(data) +
                geomLine(color = Color.GREEN, size = 6.0) {
                    data.keys.run {
                        x = range.label
                        y = firstTitle
                    }
                } +
                geomLine(color = Color.RED, size = 4.0) {
                    data.keys.run {
                        x = range.label
                        y = secondTitle
                    }
                } +
                geomLine(color = Color.BLUE, size = 2.0) {
                    data.keys.run {
                        x = range.label
                        y = thirdTitle
                    }
                } +
                ggtitle(title),
        title.png
    )
}

fun plotHeat(matrix: Array<Array<Complex>>, title: String) {
    println(title + matrix)
}

val String.png: String
    get() = this + EXT