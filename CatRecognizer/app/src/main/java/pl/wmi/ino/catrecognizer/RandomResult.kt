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
        Log.d("accumulator", accumulator.toString())
        Log.d("pivot", pivot.toString())
        if (accumulator >= 0 && result) accumulator++
        else if (accumulator <= 0 && !result) accumulator--
        else accumulator=0

        return result
    }
}