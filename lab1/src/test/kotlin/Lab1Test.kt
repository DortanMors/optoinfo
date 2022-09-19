import com.ssau.optoinformatic.lab1.plot.plotDefaultFToFile
import com.ssau.optoinformatic.lab1.plot.plotTransformToFile
import org.junit.jupiter.api.Test
import space.kscience.kmath.complex.ComplexField
import space.kscience.kmath.complex.ComplexField.times
import kotlin.math.exp
import kotlin.math.pow

private const val beta = 0.1
private const val alpha = 1.0
private const val c = 5.0
private const val p = -25.0
private const val q = 25.0
private const val n = 1000
private const val m = 1000

internal class Lab1Test {

    private val f = { x: Double -> ComplexField.exp(ComplexField.i * beta * x) }

    private val kor = { ksi: Double, x: Double ->
        ComplexField.i * exp(-alpha * (x - ksi).pow(2))
    } // Fomin: variant 12

    @Test
    fun task1() {
        // Построить график исходного оптического сигнала, изменяя параметр
        listOf(0.1, 100.0, 0.001, -0.1, -0.001).forEach(::plotDefaultFToFileDefault)
    }

    @Test
    fun task2() {
        // В соответствии с вариантом реализовать численный расчёт
        // интегрального преобразования над одномерным сигналом по формулам (7) или (10),
        //везде alpha принять равным 1. Число m можно также задать равным 1000.
        plotTransformToFileDefault(p1 = p, q1 =q)
    }

    @Test
    fun task3() {
        // Изменяя параметры выходной области [p , q] сделать выводы о том, как они
        // влияют на график результата преобразования.
        plotTransformToFileDefault(p1 = p * 2, q1 = q * 2)
        plotTransformToFileDefault(p1 = p * 0.5, q1 = q * 0.5)
    }

    @Test
    fun task4() {
        // Варьируя параметр alpha
        // (по аналогии с beta, но рассматривая только положительные значения)
        // исследовать, как меняется результат преобразования.
        // Важно: для того, чтобы сделать вывод, может понадобиться изменить размеры выходной области [p , q]
        listOf(0.1, 100.0, 0.001).forEach { plotTransformToFileDefault(a = it) }
    }

    @Test
    fun task5() {
        // Варьируя параметр c > 0 и, следовательно, изменяя область интегрирования,
        // исследовать, как меняются график исходной функции и результат преобразования.
        // Важно: для того, чтобы
        //сделать вывод, может понадобиться изменить размеры выходной области [p , q].
        listOf(c, 1.0, 100.0).forEach { plotTransformToFileDefault(c1 = it) }
    }

    private fun plotTransformToFileDefault(a: Double = alpha, p1: Double = p, q1: Double = q, c1: Double = c) =
        plotTransformToFile(a, p1, q1, c1, n, m, f, kor)

    private fun plotDefaultFToFileDefault(b: Double) = plotDefaultFToFile(b, c, n, f)

}
