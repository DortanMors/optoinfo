import org.junit.jupiter.api.Test

import com.ssau.optoinformatic.math.*
import com.ssau.optoinformatic.plot.Constants.F_KSI_LABEL
import com.ssau.optoinformatic.plot.Constants.KSI_LABEL
import com.ssau.optoinformatic.plot.Constants.X_LABEL
import com.ssau.optoinformatic.plot.Constants.Y_LABEL
import com.ssau.optoinformatic.plot.plotHeat
import com.ssau.optoinformatic.plot.plotToFile
import com.ssau.optoinformatic.plot.threePlots
import com.ssau.optoinformatic.plot.twoPlots
import org.junit.jupiter.api.BeforeEach
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.plus
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.ComplexField.unaryMinus
import space.kscience.kmath.misc.UnstableKMathAPI
import space.kscience.kmath.operations.pow
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.exp
import kotlin.math.pow

internal class Lab2Test {

    private val inputF: ((Double) -> Complex) = { x -> Complex(exp(-abs(x))) } // Variant 12
    private val inputF2: ((Double, Double) -> Complex) = { x, y -> Complex(exp(-abs(x) -abs(y))) } // Variant 12
    private val gaussF: ((Double) -> Complex) = { x -> Complex(exp(-x.pow(2))) }
    private val gauss2F: ((Double, Double) -> Complex) = { x, y -> Complex(exp(-x.pow(2) -y.pow(2))) }
    private val n = 100
    private val m = 1024
    private val c = 5.0
    private val xRange = DoubleRange(-c, c, n, X_LABEL)
    private val area = SampledArea(gaussF(xRange), xRange)
    private val fourierArea = area.scaledFiniteFourierTransform(m)
    private val xs = DoubleRange(-c, c, n, X_LABEL)
    private val ys = DoubleRange(-c, c, n, Y_LABEL)

    @BeforeEach
    fun setupTest() {

    }

    @Test
    fun task2() {
        plotToFile(area, "exp(-($X_LABEL^2))", "gauss beam (Task2)")
    }

    @Test
    fun task3() {
        plotToFile(fourierArea, "$F_KSI_LABEL[exp(-($X_LABEL^2))]", "fourier gauss beam (Task3)")
    }

    @Test
    fun task4() {
        val b = fourierArea.range.right
        val ksi4Range = DoubleRange(-b, b, n, KSI_LABEL)
        val x4Range = DoubleRange(-c, c, n, X_LABEL)
        val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range)
        plotToFile(rectFourierArea, "$F_KSI_LABEL($KSI_LABEL)", "rect fourier gauss beam (Task4)")
    }

    @Test
    fun task5() {
        val b = fourierArea.range.right
        val ksi4Range = DoubleRange(-b, b, n, KSI_LABEL)
        val x4Range = DoubleRange(-c, c, n, X_LABEL)
        val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range)
        twoPlots(
            "Task 5 double absolutes",
            ksi4Range,
            fourierArea.numbers.absolutes(),
            rectFourierArea.numbers.absolutes(),
            "|F1($KSI_LABEL)|",
            "|F2($KSI_LABEL)|"
        )
        twoPlots(
            "Task 5 double args",
            ksi4Range,
            fourierArea.numbers.args(),
            rectFourierArea.numbers.args(),
            "Args F1($KSI_LABEL)",
            "Args F2($KSI_LABEL)"
        )
    }

    @Test
    fun task6() {
        val area6 = SampledArea(inputF(xRange), xRange)
        val fourierArea6 = area6.scaledFiniteFourierTransform(m)
        plotToFile(fourierArea6, "$F_KSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam (Task6)")
    }

    @Test
    fun task7() {
        val b = fourierArea.range.right
        val ksi4Range = DoubleRange(-b, b, n, KSI_LABEL)
        val x4Range = DoubleRange(-c, c, n, X_LABEL)
        val rectFourierArea = SampledArea(gaussF.rectFourierTransform(ksi4Range, x4Range), ksi4Range)
        val analSolution: (Double) -> Complex = { ksi -> 2 / ((- i + 2 * ksi * PI) * (i + 2 * ksi * PI)) }
        val analData = analSolution(fourierArea.range)
        threePlots(
            ksi4Range,
            "Task 7 triple absolutes",
            fourierArea.numbers.absolutes(), "|F1($KSI_LABEL)|",
            rectFourierArea.numbers.absolutes(), "|F2($KSI_LABEL)|",
            analData.absolutes(), "|F3($KSI_LABEL)|"
        )
        threePlots(
            ksi4Range,
            "Task 7 triple args",
            fourierArea.numbers.args(), "Args F1($KSI_LABEL)",
            rectFourierArea.numbers.args(), "Args F2($KSI_LABEL)",
            analData.args(), "Args F3($KSI_LABEL)"
        )
    }

    @Test
    fun task8() {
        listOf(
            32 to 32,
            64 to 64,
            128 to 128,
            128 to 512,
            128 to 2048,
        ).forEach { doTask8(n = it.first, m = it.second) }
    }

    @Test
    fun task9_2() {
        val gaussValues = gauss2F(xs, ys)
        plotHeat(gaussValues, "gauss 2 values") //TODO
    }

    @Test
    fun task9_3() {
        val gaussValues = gauss2F(xs, ys)
        val gaussTransform = fastFourierTransform(gaussValues)
        plotHeat(gaussTransform, "gauss 2 transform") //TODO
    }

    @Test
    fun task9_6() {
        val variantValues = inputF2(xs, ys)
        plotHeat(variantValues, "variant 2 values")
        val variantTransformed = fastFourierTransform(variantValues)
        plotHeat(variantValues, "variant 2 transform")
    }

    private fun doTask8(n: Int, m: Int) {
        val xRange8 = DoubleRange(-c, c, n, X_LABEL)
        val area8 = SampledArea(inputF.invoke(xRange8.asArray()), xRange8)
        val fourierArea8 = area8.scaledFiniteFourierTransform(m)
        plotToFile(fourierArea8, "$F_KSI_LABEL[exp(-|$X_LABEL|)]", "fourier gauss beam (n=$n, m=$m)")
    }

    @OptIn(UnstableKMathAPI::class)
    private operator fun Number.div(complex: Complex): Complex = times(complex.pow(-1.0))
}
