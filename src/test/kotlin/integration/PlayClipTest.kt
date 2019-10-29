package integration

import com.zuhlke.rob.PianoSampleFactory
import org.junit.Ignore
import org.junit.Test
import java.io.File
import java.time.Duration
import java.time.temporal.ChronoUnit

class PlayClipTest {
    @Test
    fun canPlay_ff3G() {
        val pianoSamplesFolder = File(javaClass.classLoader.getResource("iowa")!!.toURI())
        val pianoSampleFactory = PianoSampleFactory(pianoSamplesFolder)
        val noteSpecification = "ff3G"
        val pianoSample = pianoSampleFactory.create(noteSpecification)

        pianoSample.play()
    }

    @Test
    @Ignore
    fun canPlayFirstSecondOf_ff3C() {
        val pianoSamplesFolder = File(javaClass.classLoader.getResource("iowa")!!.toURI())
        val pianoSampleFactory = PianoSampleFactory(pianoSamplesFolder)
        val noteSpecification = "ff3C"
        val pianoSample = pianoSampleFactory.create(noteSpecification)

        pianoSample.play(Duration.of(1, ChronoUnit.SECONDS))
    }
}