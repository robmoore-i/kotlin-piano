package com.zuhlke.rob;

import java.io.File;

public class PianoSampleFactory {
    private final File pianoSamplesDirectory;

    public PianoSampleFactory(File pianoSamplesDirectory) {
        this.pianoSamplesDirectory = pianoSamplesDirectory;
    }

    public PianoSample create(String noteSpecification) {
        return new PianoSample(new File(pianoSamplesDirectory, noteSpecification + ".aiff"));
    }
}
