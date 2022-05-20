package lab2

import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.ComplexField.exp
import space.kscience.kmath.complex.ComplexField.i
import space.kscience.kmath.complex.ComplexField.minus
import space.kscience.kmath.complex.ComplexField.plus
import space.kscience.kmath.complex.ComplexField.times
import space.kscience.kmath.complex.conjugate
import space.kscience.kmath.operations.div
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.log2
import kotlin.math.sin

operator fun ((Double) -> Complex).invoke(x: Array<Double>) = Array(x.size) { index -> invoke(x[index]) }

fun classicFourierTransform(f: Array<Complex>) {
    // this: array of ksi
    f.halfSwap()
    require(f.size.isPowerOf2()) { "FourierTransform: the N must be a power of 2" }
    (f.indices).map { k ->
        f.mapIndexed { n, fn ->
            fn * exp(- 2 * PI * i * k * n / f.size)
        }
    }
}

fun fastFourierTransform(x: Array<Complex>): Array<Complex> {
    val n = x.size
    // base case
    if (n == 1) return arrayOf(x[0])
    // radix 2 Cooley-Tukey FFT
//    require(n.isPowerOf2()) { "FFT: n is not a power of 2" }
    // compute FFT of even terms
    val even = Array(n / 2) { index ->
        x[2 * index]
    }
    val evenFFT = fastFourierTransform(even)
    // compute FFT of odd terms
    for (k in 0 until n / 2) {
        even[k] = x[2 * k + 1]
    }
    val oddFFT = fastFourierTransform(even)
    // combine
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

    // compute forward FFT
    y = fastFourierTransform(y)

    // take conjugate again
    for (i in y.indices) {
        y[i] = y[i].conjugate
    }

    // divide by n
    for (i in y.indices) {
        y[i] = y[i] * (1.0 / n) // TODO scale
    }
    return y
}

private fun Int.isPowerOf2() = log2(this.toDouble()) % 1.0 == 0.0

private fun Array<Complex>.halfSwap() {
    require(size % 2 == 0) { "Swap: array size is odd" }
    for (i in 0 until size / 2) {
        this[i] = this[size / 2 + i].also { this[size / 2 + i] = this[i] }
    }
}

private fun Complex.scale(value: Double) = Complex(re * value, im * value)
