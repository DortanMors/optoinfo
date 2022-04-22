package lab1

import kotlin.math.roundToInt

class DoubleRange(val left: Double, val right: Double, val n: Int) : ArrayList<Double>(n) {
    val step = (right - left) / n
    init {
        forEachIndexed { index, _ ->
            this[index] = left + index * step
        }
    }

    constructor(left: Double, right: Double, step: Double): this(left, right, ((right - left) / step).roundToInt())

    infix fun to(ordinates: List<Double>) = zip(ordinates).toMap()

}