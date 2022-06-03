package lab2

import jetbrains.datalore.base.math.ipow
import lab1.DoubleRange
import lab1.KSI_LABEL
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField
import space.kscience.kmath.complex.ComplexField.minus
import space.kscience.kmath.complex.ComplexField.plus
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.conjugate
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.sin

operator fun ((Double) -> Complex).invoke(x: Array<Double>) = Array(x.size) { index -> invoke(x[index]) }

data class SampledArea (val numbers: Array<Complex>, val range: DoubleRange, val delta: Double) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as SampledArea
        if (!numbers.contentEquals(other.numbers)) return false
        if (range != other.range) return false
        if (delta != other.delta) return false
        return true
    }

    override fun hashCode(): Int {
        var result = numbers.contentHashCode()
        result = 31 * result + range.hashCode()
        result = 31 * result + delta.hashCode()
        return result
    }
}

fun SampledArea.scaledFiniteFourierTransform(m: Int): SampledArea {
    require(numbers.size % 2 == 0) { "Can`t inflate zeros" }
    val scaledSamples = SampledArea(numbers.inflateZeros(m), range, delta)
    val result = scaledSamples.finiteFourierTransform() // b = N^2 / 4aM   =>    hu = 1 / (hx M)
    val b = numbers.size.ipow(2) / (4 * m * range.right)
    val hu = 1 / (delta * m)
    val outRange = DoubleRange(-b, b, numbers.size, KSI_LABEL)
    return SampledArea(result.clipZeros(numbers.size), outRange, hu)
}

fun SampledArea.finiteFourierTransform(): Array<Complex> {
    require(numbers.size.isPowerOf2()) { "FourierTransform: the N must be a power of 2" }
    var result = numbers.halfSwap()
    result = fastFourierTransform(result)
    result *= delta
    result = result.halfSwap()
    return result
}

fun fastFourierTransform(x: Array<Complex>): Array<Complex> {
    val n = x.size
    if (n == 1) return arrayOf(x[0])
    val even = Array(n / 2) { index ->
        x[2 * index]
    }
    val evenFFT = fastFourierTransform(even)
    for (k in 0 until n / 2) {
        even[k] = x[2 * k + 1]
    }
    val oddFFT = fastFourierTransform(even)
    val y = Array(n) { index ->
        val kth = -2 * index * Math.PI / n
        val wk = Complex(cos(kth), sin(kth))
        if (index < n / 2)
            evenFFT[index] + (wk * oddFFT[index])
        else
            evenFFT[index] - (wk * oddFFT[index])
    }
    return y
}

// compute the inverse FFT of x[], assuming its length n is a power of 2
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
        y[i] = y[i] * (1.0 / n) // TODO scale
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
    require(m > size) { "Can`t inflate to lesser array" }
    val result = Array(m) { ComplexField.zero }
    val gain = (m-size) / 2
    for (i in indices) {
        result[gain + i] = this[i]
    }
    return result
}

private fun Array<Complex>.clipZeros(n: Int): Array<Complex> {
    require(size > n) { "Can`t cut to bigger array" }
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
