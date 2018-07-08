
import java.io.*;
import java.util.HashMap;
import javax.sound.sampled.*;

public class Audio implements Runnable{
    int songCounter = 0;
    long songLength = 0;
    long startTime;

    private HashMap<String, Clip> songs = new HashMap<>();
    private HashMap<String, File> sounds = new HashMap<>();

    public void init() {
        for (int i = 0; i < 11; i++) {
            addSong("src/resources/audio/track" + i + ".wav", "track" + i);
        }
        addSound("src/resources/audio/StartCar.wav", "startcar");
        playAudio("startcar");
        startTime = (long) (System.nanoTime() * 0.0000001);
        this.run();
    }

    @Override
    public void run() {
        double firstTime;
        double lastTime = System.nanoTime() * 0.000000000001;
        double passedTime;
        double unprocessedTime = 0;
        double frameTime = 0;

        while(true) {
            firstTime = System.nanoTime() * 0.000000001;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            double UPDATE_CAP = 1.0 * 0.6;
            while(unprocessedTime >= UPDATE_CAP) {
                // when thread freezes still want to update.
                unprocessedTime -= UPDATE_CAP;

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    // Update Game here
                    updateAudio();
                }
            }
            try {
                Thread.sleep(1);
                // sleep nothing to do. use less of the cpu.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAudio() {
        System.out.println("AUDIO | startTime: " + (System.nanoTime() / 10000000 - startTime));
        System.out.println("AUDIO | songLength: " + songLength / 1000);
        if (System.nanoTime() / 10000000 - startTime > songLength / 10000) {
            if (songCounter > 10) {
                songCounter = 0;
            }
            playRadio();
            songCounter++;
            startTime = System.nanoTime() / 10000000;
        }
    }

    private void addSound(String filePath, String name) {
        File file = new File(filePath);
        sounds.putIfAbsent(name, file);
    }

    private void addSong(String filePath, String name) {
        AudioInputStream audioIn;
        try {
            audioIn = AudioSystem.getAudioInputStream(new File(filePath));
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            songs.putIfAbsent(name, clip);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void playAudio(String name) {
        AudioInputStream audioIn;
        try {
            audioIn = AudioSystem.getAudioInputStream(sounds.get(name));
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void playRadio() {
        if (songCounter > 10) {
            songCounter = 0;
        }
        songLength = songs.get("track" + songCounter).getMicrosecondLength();
        songs.get("track" + songCounter).start();
        System.out.println("songLength: " + songLength);
        songCounter++;
    }
}
