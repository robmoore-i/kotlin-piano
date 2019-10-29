package com.zuhlke.rob;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PianoSample {
    private final File audioFile;

    public PianoSample(File audioFile) {
        this.audioFile = audioFile;
    }

    public void play() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
        DataLine.Info info = new DataLine.Info(Clip.class, audioInputStream.getFormat());
        Clip clip = (Clip) AudioSystem.getLine(info);
        clip.open(audioInputStream);
        ClipLineListener clipLineListener = new ClipLineListener();
        clip.addLineListener(clipLineListener);
        clip.start();

        while (!clipLineListener.completed) {
            try {
                clipLineListener.wait();
            } catch (IllegalMonitorStateException | InterruptedException ignored) {
            }
        }

        clip.close();
        audioInputStream.close();
    }
}
