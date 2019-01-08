package pl.wmi.ino.catrecognizer

import android.util.Log
import java.util.Random
import kotlin.math.absoluteValue

object RandomResult {
    private var accumulator=0

    fun nextResult() : Boolean {
        val randomNumber : Double = Random().nextDouble()
        val pivot = 0.5 + (accumulator / 8.0)
        val result = randomNumber > pivot

        if (accumulator >= 0 && result) accumulator++
        else if (accumulator <= 0 && !result) accumulator--
        else if (result) accumulator=1
        else accumulator=-1

        return result
    }
}