package org.purple.floats

import java.lang.Float.MAX_VALUE
import java.lang.Float.NEGATIVE_INFINITY
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import kotlin.Float.Companion.MIN_VALUE
import kotlin.Float.Companion.NaN
import kotlin.Float.Companion.POSITIVE_INFINITY
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class TestFloatApproximation {
    
    // Tests the zero special case: must be exactly 0 as opposed to a subnormal number
    // BigDecimal has no concept of positive or negative zero
    @Test fun `+0`() = assertEquals(ZERO, 0f.toDecimalApproximation())
    @Test fun `-0`() = assertEquals(ZERO, (-0f).toDecimalApproximation())
    
    // Exercises the subnormal number special case (stored exponent is 0 but fraction part is not 0)
    @Test fun min() = assertEquals(MIN_VALUE, MIN_VALUE.toDecimalApproximation().toFloat())
    @Test fun max() = assertEquals(MAX_VALUE, MAX_VALUE.toDecimalApproximation().toFloat())

    // Precise representation

    @Test fun `+1`() = assertEquals(ONE, 1f.toDecimalApproximation())
    @Test fun `-1`() = assertEquals(-ONE, (-1f).toDecimalApproximation())
    
    @Test fun `0_25`() =
        assertEquals("0.25".toBigDecimal(), 0.25f.toDecimalApproximation())

    @Test fun `-0_25`() =
        assertEquals("-0.25".toBigDecimal(), (-0.25f).toDecimalApproximation())

    @Test fun `+256_25`() =
        assertEquals(
            "256.25".toBigDecimal(),
            256.25f.toDecimalApproximation()
        )

    @Test fun `-256_25`() =
        assertEquals(
            "-256.25".toBigDecimal(),
            (-256.25f).toDecimalApproximation()
        )

    // Imprecise representation
    
    @Test fun `0_1`() =
        assertEquals(
            "0.100000001490116119384765625".toBigDecimal(), 
            0.1f.toDecimalApproximation()
        )

    @Test fun `-0_1`() =
        assertEquals(
            "-0.100000001490116119384765625".toBigDecimal(), 
            (-0.1f).toDecimalApproximation()
        )

    @Test fun `33_1`() =
        assertEquals(
            "33.09999847412109375".toBigDecimal(),
            33.1f.toDecimalApproximation()
        )

    @Test fun `-33_1`() =
        assertEquals(
            "-33.09999847412109375".toBigDecimal(),
            (-33.1f).toDecimalApproximation()
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
