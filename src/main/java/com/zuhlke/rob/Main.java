package com.zuhlke.rob;

import javax.sound.sampled.*;
import java.io.IOException;
import java.time.Duration;
import java.util.Timer;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        Timer timer = new Timer();
        Duration twoSeconds = Duration.ofSeconds(2);
        UniClip c = main.clip("ff3C").withTimer(timer, twoSeconds);
        UniClip e = main.clip("ff3E").withTimer(timer, twoSeconds);
        UniClip g = main.clip("ff3G").withTimer(timer, twoSeconds);
        MultiClip cMajor = new MultiClip(c, e, g);
        cMajor.playInForeground(Semaphore.primitiveSemaphore());
    }

    private UniClip clip(String noteSpecification) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream("iowa/" + noteSpecification + ".aiff"));
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
        return new UniClip(audioInputStream, clip);
    }
}
