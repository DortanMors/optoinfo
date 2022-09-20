package com.ssau.optoinformatic.lab2.math

import com.ssau.optoinformatic.common.math.DoubleRange
import space.kscience.kmath.complex.Complex
import space.kscience.kmath.complex.r
import space.kscience.kmath.complex.theta

fun Array<Array<Complex>>.args() = map { row -> row.map { it.theta } }

fun Array<Array<Complex>>.absolutes() = map { row -> row.map { it.r } }

operator fun ((Double, Double) -> Complex).invoke(xRange: DoubleRange, yRange: DoubleRange) = SampledSquare(
        numbers = Array(xRange.n) { xi ->
            Array(yRange.n) { yi ->
                invoke(xRange[xi], yRange[yi])
            }
        },
        rangeX = xRange,
        rangeY = yRange,
    )