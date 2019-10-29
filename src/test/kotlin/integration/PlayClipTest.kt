package integration

import com.zuhlke.rob.PianoSampleFactory
import org.junit.Test
import java.io.File

class PlayClipTest {
    @Test
    fun canPlay_ff3G() {
        val pianoSamplesFolder = File(javaClass.classLoader.getResource("iowa")!!.toURI())
        val pianoSampleFactory = PianoSampleFactory(pianoSamplesFolder)
        val noteSpecification = "ff3G"
        val pianoSample = pianoSampleFactory.create(noteSpecification)

        pianoSample.play()
    }
}