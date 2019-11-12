package com.zuhlke.rob;

import javax.sound.sampled.Clip;
import javax.sound.sampled.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        UniClip c = main.clip("ff3C");
        UniClip e = main.clip("ff3E");
        UniClip g = main.clip("ff3G");
        MultiClip cMajor = new MultiClip(c, e, g);
        cMajor.play(Semaphore.primitiveSemaphore(), UniClipPlayer::new);
    }

    private UniClip clip(String noteSpecification) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream("iowa/" + noteSpecification + ".aiff"));
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
        return new UniClip(audioInputStream, clip);
    }
}
