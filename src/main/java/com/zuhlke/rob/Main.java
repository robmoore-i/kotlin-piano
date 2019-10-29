package com.zuhlke.rob;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        String pianoSamplesPath = "/Users/romo/IdeaProjects/sounds/piano-samples/iowa/";
        PianoSampleFactory pianoSampleFactory = new PianoSampleFactory(new File(pianoSamplesPath));

        String noteSpecification = "ff3G";

        PianoSample pianoSample = pianoSampleFactory.create(noteSpecification);

        pianoSample.play();
    }
}
