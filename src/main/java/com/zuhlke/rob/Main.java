package com.zuhlke.rob;

import javax.sound.sampled.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Main main = new Main();
        MultiClipPlayer multiClipPlayer = new MultiClipPlayer(Semaphore.primitiveSemaphore(), FullUniClipPlayer::new);
        UniAudioClip c = main.clip("ff3C");
        UniAudioClip e = main.clip("ff3E");
        UniAudioClip g = main.clip("ff3G");
        MultiAudioClip cMajor = new MultiAudioClip(c, e, g);
        multiClipPlayer.play(cMajor);
    }

    private UniAudioClip clip(String noteSpecification) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getClassLoader().getResourceAsStream("iowa/" + noteSpecification + ".aiff"));
        Clip clip = (Clip) AudioSystem.getLine(new DataLine.Info(Clip.class, audioInputStream.getFormat()));
        return new UniAudioClip(audioInputStream, clip);
    }
}
