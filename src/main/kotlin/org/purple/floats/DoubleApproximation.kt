package org.purple.floats

import java.math.BigDecimal
import java.math.BigDecimal.ONE
import java.math.BigDecimal.ZERO
import java.math.MathContext

private const val FRAC_BITS = 52
private const val EXP_BITS = Double.SIZE_BITS - 1 - FRAC_BITS
private const val EXP_BIAS = (1 shl EXP_BITS - 1) - 1
private const val SIGN_BIT = Double.SIZE_BITS - 1

private val TWO = 2.toBigDecimal()

fun Double.toDecimalApproximation(): BigDecimal {
    require(this.isFinite()) { "Infinite or NaN" }

    val bits = toBits()
    val storedExp = bits.sliceBits(FRAC_BITS, EXP_BITS).toInt()
    val exponent = if (storedExp == 0) 1 - EXP_BIAS else storedExp - EXP_BIAS
    val leading = if (storedExp == 0) ZERO else ONE

    val frac = (1..FRAC_BITS)
        .filter { bits.isBitSet(FRAC_BITS - it) }
        .map { TWO.pow(-it, MathContext.DECIMAL128) }
        .fold(ZERO) { s, t -> s + t }

    val value =
        if (leading == ZERO && frac == ZERO) ZERO
        else (leading + frac) * TWO.pow(exponent, MathContext.DECIMAL128)

    return (if (bits.isBitSet(SIGN_BIT)) -value else value).stripTrailingZeros()
}

private fun Long.isBitSet(i: Int) = this and (1L shl i) != 0L
private fun Long.sliceBits(s: Int, n: Int) = (this and bitmask(s, n)) shr s
private fun bitmask(s: Int, n: Int): Long = ((1L shl n) - 1) shl s
