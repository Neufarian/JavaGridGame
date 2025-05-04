/**
 * Provides static utility method for playing game sound effects.
 * Handles loading and playing audio file when a coin is collected.
 */

package ui;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    public static void playSound(String fileName) {
        try (AudioInputStream audioIn = AudioSystem.getAudioInputStream(SoundManager.class.getResource("/ui/coinCollected.wav"))) {
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
