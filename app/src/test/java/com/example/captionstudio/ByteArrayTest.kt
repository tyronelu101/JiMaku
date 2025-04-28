package com.example.captionstudio

import org.junit.Test


class ByteArrayTest {

    @Test
    fun combiningTwoElementsInByteArrayReturnsCorrectValue() {
        val byteArray = ByteArray(2)
        //Least sig
        byteArray[0] = -7
        //Most sig
        byteArray[1] = -1

        val expected = -1523
        val leastSigDecimal = byteArray[0].toInt() and 0XFF
        val mostSigDecimal = byteArray[1].toInt() and 0XFF
        //Subtract 65536 because 16th bit is used as sign bit
        val result = (mostSigDecimal shl (8)) or leastSigDecimal - 65536
        assert(result == expected)
    }
}