package com.zuhlke.rob;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        String pianoSamplesPath = "/Users/romo/IdeaProjects/sounds/piano-samples/iowa/";
        PianoSampleFactory pianoSampleFactory = new PianoSampleFactory(new File(pianoSamplesPath));

        String noteSpecification = "ff3G";

        PianoSample pianoSample = pianoSampleFactory.create(noteSpecification);

        pianoSample.play();
    }
}
