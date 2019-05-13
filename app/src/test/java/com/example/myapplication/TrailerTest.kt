package com.example.myapplication

import com.example.myapplication.videos.Videos
import org.junit.Assert
import org.junit.Test

class TrailerTest {

    @Test
    fun getKeyTest() {
        val keyOfTrailer = "SUXWAEX2jlg"
        val trailer = Videos()
        Assert.assertEquals(keyOfTrailer, trailer.getKey())

    }

}