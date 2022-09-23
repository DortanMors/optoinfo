package com.ssau.optoinformatic.common.math

import java.util.*
import kotlin.math.roundToInt


class DoubleRange(val left: Double, val right: Double, val n: Int, val label: String = "x") : ArrayList<Double>(n) {
    val step = (right - left) / n
    init {
        for (i in 0 until n) {
            add(left + i * step)
        }
    }

    fun to(ordinates: List<Double>, yLabel: String) =
       mapOf<String, Any>(label to this as List<Double>, yLabel to ordinates)

    fun asArray() = Array(size) { index ->
        left + index * step
    }

}