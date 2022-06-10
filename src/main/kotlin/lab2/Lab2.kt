package lab2

import jetbrains.datalore.base.values.Color
import jetbrains.letsPlot.export.ggsave
import jetbrains.letsPlot.geom.geomLine
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
val gaussF: ((Double) -> Complex) = { x -> Complex(exp(-x.pow(2))) }

fun main() {
// Task 2
    val n = 100
    val c = 5.0
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val args = xRange.asArray()
    val area = SampledArea(gaussF.invoke(args), xRange, xRange.step)
    plotToFile(area, "exp(-($X_LABEL^2))", "gauss beam (Task2)")
// Task 3
    val m = 1024
    val fourierArea = area.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea, "$FKSI_LABEL[exp(-($X_LABEL^2))]", "fourier gauss beam (Task3)")
// Task 4
    val b = fourierArea.range.right
    val ksi4Range = DoubleRange(-b, b, m, KSI_LABEL)
    val x4Range = DoubleRange(-c, c, n, X_LABEL)
    val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range, ksi4Range.step)
    plotToFile(rectFourierArea, "$FKSI_LABEL($KSI_LABEL)", "rect fourier gauss beam (Task4)")
// Task 5
    task5(fourierArea, rectFourierArea)
// Task 6
    val area6 = SampledArea(inputF.invoke(args), xRange, xRange.step)
    val fourierArea6 = area6.scaledFiniteFourierTransform(m)
    plotToFile(fourierArea6, "$FKSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam (Task6)")
// Task 7 Analytics
    val analSolution: (Double) -> Complex =
        { ksi -> 2 / ((- i + 2 * ksi * PI) * (i + 2 * ksi * PI)) }
    val analData = analSolution.invoke(fourierArea.range)
    task7(fourierArea, rectFourierArea, analData)
// Task 8
    task8(32, 32)
    task8(64, 64)
    task8(128, 128)
    task8(128, 512)
    task8(128, 2048)
// Task 9

// Test
    println("TODO ${(10 / Complex(5))}")
}

private operator fun Number.div(complex: Complex): Complex = times(complex.pow(-1.0))


fun task8(n: Int, m: Int) {
    val xRange = DoubleRange(-c, c, n, X_LABEL)
    val area = SampledArea(inputF.invoke(xRange.asArray()), xRange, xRange.step)
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

fun task5(firstArea: SampledArea, secondArea: SampledArea) {
    ggsave(
        twoPlots(
            firstArea.run { range.to(numbers.toList().absolutes(), "|F1($KSI_LABEL)|") },
            secondArea.run { range.to(numbers.toList().absolutes(), "|F2($KSI_LABEL)|") },
            "Absolutes FFT & classic FT"
        ),
        "absolutes дабл (Task5)$EXT"
    )
    ggsave(
        twoPlots(
            firstArea.run { range.to(numbers.toList().args(), "Arg F1($KSI_LABEL)") },
            secondArea.run { range.to(numbers.toList().args(), "Arg F2($KSI_LABEL)") },
            "Args FFT & classic FT"
        ),
        "args дабл (Task5)$EXT"
    )
}

fun task7(firstArea: SampledArea, secondArea: SampledArea, thirdList: List<Complex>) {
    ggsave(
        threePlots(
            firstArea.run { range.to(numbers.toList().absolutes(), "|F1($KSI_LABEL)|") },
            secondArea.run { range.to(numbers.toList().absolutes(), "|F2($KSI_LABEL)|") },
            firstArea.range.to(thirdList.absolutes(), "|F3($KSI_LABEL)|"),
            "Absolutes FFT & classic FT & analytics"
        ),
        "absolutes трипл (Task7)$EXT"
    )
    ggsave(
        threePlots(
            firstArea.run { range.to(numbers.toList().args(), "Arg F1($KSI_LABEL)") },
            secondArea.run { range.to(numbers.toList().args(), "Arg F2($KSI_LABEL)") },
            firstArea.range.to(thirdList.args(), "Arg F3($KSI_LABEL)"),
            "Args FFT & classic FT & analytics"
        ),
        "args трипл (Task7)$EXT"
    )
}

fun twoPlots(first: Map<String, Any>, second: Map<String, Any>, title: String) =
    plot(first, title) +
    geomLine(
        data = second,
        color = Color.RED
    )

fun threePlots(
    first: Map<String, Any>,
    second: Map<String, Any>,
    third: Map<String, Any>,
    title: String
) = twoPlots(first, second, title) +
    geomLine(
        data = third,
        color = Color.BLUE
    )