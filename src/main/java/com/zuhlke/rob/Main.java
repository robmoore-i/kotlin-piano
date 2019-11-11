package com.zuhlke.rob;

import javax.sound.sampled.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        MultiClipPlayer multiClipPlayer = new MultiClipPlayer(Semaphore.primitiveSemaphore(), () -> new FullSingleClipPlayer(Lock.primitiveLock()));
        SingleAudioClip c = main.clip("ff3C");
        SingleAudioClip e = main.clip("ff3E");
        SingleAudioClip g = main.clip("ff3G");
        MultiAudioClip multiAudioClip = new MultiAudioClip(c, e, g);
        multiClipPlayer.play(multiAudioClip);
    }

    private SingleAudioClip clip(String noteSpecification) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream("iowa/" + noteSpecification + ".aiff"));
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
        return new SingleAudioClip(audioInputStream, clip);
    }
}
