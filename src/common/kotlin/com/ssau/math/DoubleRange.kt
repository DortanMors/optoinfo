package com.ssau.math

import kotlin.math.roundToInt

class DoubleRange(val left: Double, val right: Double, val n: Int, val label: String = "x") : ArrayList<Double>(n) {
    val step = (right - left) / n
    init {
        for (i in 0 until n) {
            add(left + i * step)
        }
    }

    constructor(left: Double, right: Double, step: Double, label: String = "x"): this(left, right, ((right - left) / step).roundToInt(), label)

    fun to(ordinates: List<Double>, yLabel: String) =
       mapOf<String, Any>(label to this as List<Double>, yLabel to ordinates)

}