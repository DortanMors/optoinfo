package com.ssau.optoinformatic.common.plot

import com.ssau.optoinformatic.common.math.DoubleRange
import com.ssau.optoinformatic.common.math.plot
import com.ssau.optoinformatic.common.plot.Constants.EXT
import org.jetbrains.letsPlot.export.ggsave

fun plotOnRange(xData: DoubleRange, yData: List<Double>, yLabel: String, title: String) =
    println(
        ggsave(
            plot(
                xData.to(yData, yLabel),
                title
            ),
            "$title$EXT"
        )
    )

val String.png: String
    get() = this + EXT
