package org.purple.floats

import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import kotlin.Double.Companion.MAX_VALUE
import kotlin.Double.Companion.MIN_VALUE
import kotlin.Double.Companion.NEGATIVE_INFINITY
import kotlin.Double.Companion.NaN
import kotlin.Double.Companion.POSITIVE_INFINITY

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestDoubleApproximation {

    // Tests the zero special case: must be exactly 0 as opposed to a subnormal number
    // BigDecimal has no concept of positive or negative zero
    @Test fun `+0`() = assertEquals(ZERO, 0.0.toDecimalApproximation())
    @Test fun `-0`() = assertEquals(ZERO, (-0.0).toDecimalApproximation())

    // Exercises the subnormal number special case (stored exponent is 0 but fraction part is not 0)
    @Test fun min() = assertEquals(MIN_VALUE, MIN_VALUE.toDecimalApproximation().toDouble())
    @Test fun max() = assertEquals(MAX_VALUE, MAX_VALUE.toDecimalApproximation().toDouble())

    // Precise representation

    @Test fun `+1`() = assertEquals(ONE, 1.0.toDecimalApproximation())
    @Test fun `-1`() = assertEquals(-ONE, (-1.0).toDecimalApproximation())

    @Test fun `0_25`() =
        assertEquals("0.25".toBigDecimal(), 0.25.toDecimalApproximation())

    @Test fun `-0_25`() =
        assertEquals("-0.25".toBigDecimal(), (-0.25).toDecimalApproximation())

    @Test fun `+256_25`() =
        assertEquals(
            "256.25".toBigDecimal(),
            256.25f.toDecimalApproximation()
        )

    @Test fun `-256_25`() =
        assertEquals(
            "-256.25".toBigDecimal(),
            (-256.25).toDecimalApproximation()
        )

    // Imprecise representation

    @Test fun `0_1`() =
        assertEquals(
            "0.10000000000000000555111512312578270211815834045406875".toBigDecimal(), 
            0.1.toDecimalApproximation()
        )

    @Test fun `-0_1`() =
        assertEquals(
            "-0.10000000000000000555111512312578270211815834045406875".toBigDecimal(),
            (-0.1).toDecimalApproximation()
        )

    @Test fun `33_1`() =
        assertEquals(
            "33.1000000000000014210854715202003717422485351562336".toBigDecimal(),
            33.1.toDecimalApproximation()
        )

    @Test fun `-33_1`() =
        assertEquals(
            "-33.1000000000000014210854715202003717422485351562336".toBigDecimal(),
            (-33.1).toDecimalApproximation()
        )

    // Unsupported special numbers (no corresponding concept in BigDecimal)

    @Suppress("RemoveRedundantBackticks")
    @Test fun `NaN`() {
        assertFailsWith<IllegalArgumentException> { NaN.toDecimalApproximation() }
    }

    @Test fun `+Infinity`() {
        assertFailsWith<IllegalArgumentException> {
            (POSITIVE_INFINITY).toDecimalApproximation()
        }
    }

    @Test fun `-Infinity`() {
        assertFailsWith<IllegalArgumentException> {
            (NEGATIVE_INFINITY).toDecimalApproximation()
        }
    }
}
