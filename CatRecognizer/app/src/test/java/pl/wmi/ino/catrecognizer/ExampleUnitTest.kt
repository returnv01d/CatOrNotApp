package pl.wmi.ino.catrecognizer

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun no_long_sequences() {
        val list = (1..1000).map {RandomResult.nextResult()}

        var longest_sequence = 1;
        var current_sequence = 1;
        var last_result = list.first()
        for (i in list.drop(1)) {
            if (i==last_result) {
                current_sequence++
            } else {
                current_sequence=1
                last_result=i
            }
            if (current_sequence>longest_sequence) longest_sequence = current_sequence
        }

        assertTrue(longest_sequence <= 4)
    }
}
