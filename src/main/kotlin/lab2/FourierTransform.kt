package lab2

import jetbrains.datalore.base.math.ipow
import lab1.*
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField
import space.kscience.kmath.complex.ComplexField.exp
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.minus
import space.kscience.kmath.complex.ComplexField.plus
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.conjugate
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.sin

operator fun ((Double) -> Complex).invoke(x: Array<Double>) = Array(x.size) { index -> invoke(x[index]) }
operator fun ((Double) -> Complex).invoke(x: List<Double>) = Array(x.size) { index -> invoke(x[index]) }


data class SampledArea (val numbers: Array<Complex>, val range: DoubleRange) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SampledArea
        if (!numbers.contentEquals(other.numbers)) return false
        if (range != other.range) return false
        if (range.step != range.step) return false
        return true
    }

    override fun hashCode(): Int {
        var result = numbers.contentHashCode()
        result = 31 * result + range.hashCode()
        result = 31 * result + range.step.hashCode()
        return result
    }
}

val fourierKor: (Double, Double) -> Complex = { ksi, x -> ComplexField.exp(-2 * PI * ComplexField.i * ksi * x) }

fun ((Double) -> Complex).rectFourierTransform(ksiRange: DoubleRange, xRange: DoubleRange): Array<Complex> =
    integralTransform(ksiRange, xRange, fourierKor).toTypedArray()

// Task 9
fun fastFourierTransform(matrix: Array<Array<Complex>>): Array<Array<Complex>> {
    val transformedByRow = Array(matrix.size) { row ->
        fastFourierTransform(matrix[row])
    }
    val transposed = transformedByRow.transpose()
    val transformedByColumn = Array(transposed.size) { column ->
        fastFourierTransform(transposed[column])
    }
    return transformedByColumn.transpose()
}

// Task9
fun ((Double, Double) -> Complex).rectFourierTransform(
    uRange: DoubleRange,
    vRange: DoubleRange,
    xRange: DoubleRange,
    yRange: DoubleRange
) = uRange.map {  u ->
        vRange.map {  v ->
            val tempFy: (Double) -> Complex = { y ->
                    val fourier2Kor: (Double, Double) -> Complex = { ksi, x -> exp(-2 * PI * i * (ksi * x + v * y)) }
                    xRange.integrate { x -> this.invoke(x, y) * fourier2Kor(u, x) }
                }
            yRange.integrate(tempFy)
        }
    }

fun SampledArea.scaledFiniteFourierTransform(m: Int): SampledArea {
    require(numbers.size % 2 == 0) { "Can`t inflate zeros" }
    val scaledSamples = SampledArea(numbers.inflateZeros(m), range)
    val result = scaledSamples.finiteFourierTransform() // b = N^2 / 4aM   =>    hu = 1 / (hx M)
    val b = numbers.size.ipow(2) / (4 * m * range.right)
    val hu = 1 / (range.step * m) //todo question
    val outRange = DoubleRange(-b, b, numbers.size, KSI_LABEL)
    return SampledArea(result.clipZeros(numbers.size), outRange)
}

fun SampledArea.finiteFourierTransform(): Array<Complex> {
    require(numbers.size.isPowerOf2()) { "FourierTransform: the N must be a power of 2" }
    var result = numbers.halfSwap()
    result = fastFourierTransform(result)
    result *= range.step
    result = result.halfSwap()
    return result
}

fun fastFourierTransform(x: Array<Complex>): Array<Complex> {
    val n = x.size
    if (n == 1) return arrayOf(x[0])
    // compute FFT of even terms
    val even = Array(n/2) {
        x[2*it]
    }
    val evenFFT = fastFourierTransform(even)
    val odd = even
    for (k in 0 until n/2) {
        odd[k] = x[2*k + 1]
    }
    val oddFFT = fastFourierTransform(odd)
    val y = Array(n) { ComplexField.zero }
    for (k in 0 until n/2) {
        val kth = -2 * k * Math.PI / n
        val wk = Complex(cos(kth), sin(kth))
        y[k]       = evenFFT[k].plus (wk.times(oddFFT[k]))
        y[k + n/2] = evenFFT[k].minus(wk.times(oddFFT[k]))
    }
    return y
}

fun inverseFFT(x: Array<Complex>): Array<Complex> {
    val n = x.size
    require(n.isPowerOf2()) { "FFT: n is not a power of 2" }
    var y = Array(n) { index ->
        x[index].conjugate
    }
    y = fastFourierTransform(y)
    for (i in y.indices) {
        y[i] = y[i].conjugate
    }
    for (i in y.indices) {
        y[i] = y[i].scale(1.0 / n)
    }
    return y
}

private fun Int.isPowerOf2() = log2(this.toDouble()) % 1.0 == 0.0

private fun Array<Complex>.halfSwap(): Array<Complex> {
    require(size % 2 == 0) { "Swap: array size is odd" }
    val result = Array(size) { ComplexField.zero }
    for (i in 0 until size / 2) {
        result[i] = this[size / 2 + i]
        result[size / 2 + i] = this[i]
    }
    return result
}

private fun Array<Complex>.inflateZeros(m: Int): Array<Complex> {
    require(m >= size) { "Can`t inflate to lesser array" }
    val result = Array(m) { ComplexField.zero }
    val gain = (m-size) / 2
    for (i in indices) {
        result[gain + i] = this[i]
    }
    return result
}

private fun Array<Complex>.clipZeros(n: Int): Array<Complex> {
    require(size >= n) { "Can`t cut to bigger array" }
    val result = Array(n) { ComplexField.zero }
    val gain = (size - n) / 2
    for (i in result.indices) {
        result[i] = this[gain + i]
    }
    return result
}

private operator fun Array<Complex>.timesAssign(value: Double) {
    for (i in indices) {
        this[i] = this[i] * value
    }
}

private fun Complex.scale(value: Double) = Complex(re * value, im * value)

private inline fun <reified T> Array<Array<T>>.transpose(): Array<Array<T>> =
    Array(first().size) { column ->
        Array(size) { row ->
            this[row][column]
        }
    }

