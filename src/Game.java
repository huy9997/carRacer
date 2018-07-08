import items.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Game extends Canvas implements Runnable{

    private final Window window;
    private Thread thread;
    private boolean run = false;

    private Game() throws IOException {
        this.window = new Window(640, 480);
        start();
    }

    private void start() {
        thread = new Thread(this);
//        Thread audio = new Thread(() -> {
//            Audio audio1 = new Audio();
//            audio1.init();
//        });
//        audio.start();
        thread.run();
    }

    public synchronized void stop() {
        try{
            run = false;
            thread.join(); // kill the thread
        }catch(Exception e){
            System.out.println(e);
        }
    }

    public void run() {
        run = true;
        boolean render;
        double firstTime;
        double lastTime = System.nanoTime() * 0.000000000001;
        double passedTime;
        double unprocessedTime = 0;
        double frameTime = 0;
        int frames = 0;
        int fps;

        while(run) {
            render = false;
            firstTime = System.nanoTime() * 0.00000001;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            double UPDATE_CAP = 1.0 * 0.6;
            while(unprocessedTime >= UPDATE_CAP) {
                // when thread freezes still want to update.
                unprocessedTime -= UPDATE_CAP;
                frames++;

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    frames = 0;
//                    System.out.println("FPS: "+ fps);
                    // Update Game here
                    updateGame();
                    render = true;
                }
            }
            if (render) {
                renderGame();
            } else {
                try {
                    Thread.sleep(1);
                    // sleep nothing to do. use less of the cpu.
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateGame() {
        // update the logic
        this.window.p.updateMobiles();
        this.window.collisionDetector.checkCollisions();
    }

    private void renderGame() {
        // update the images
        this.window.worldPanel.repaint();
    }

    public static void main(String args[]) throws IOException {
        new Game();
    }
}