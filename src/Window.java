import events.*;
import items.*;
import javafx.scene.text.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.AttributedString;
import java.util.ArrayList;

public class Window extends JFrame {
    // global objects

    private Car mobile = new Car(0, 248, "src/resources/coupe1.png", 0, (short) 90);
    public Player p;
    public ArrayList<Item> items = new ArrayList<>();
    public ArrayList<Mobile> mobiles = new ArrayList<>();
    public CollisionDetector collisionDetector;
    private int noItems;

    // assets
    private Sprite road = new Sprite("src/resources/road.png");
    private Sprite road1 = new Sprite("src/resources/road1.png");
    private Sprite roadShoulder = new Sprite("src/resources/roadshoulder.png");
    private Sprite road2 = new Sprite("src/resources/road2.png");
    private Sprite road3 = new Sprite("src/resources/road3.png");
    private Sprite road4 = new Sprite("src/resources/road4.png");
    private Sprite fence1 = new Sprite("src/resources/fence1.png");
    private Sprite grass1 = new Sprite("src/resources/grass0.png");
    private Sprite grass2 = new Sprite("src/resources/grass1.png");
    private Sprite grass3 = new Sprite("src/resources/grass2.png");
    private Sprite grass4 = new Sprite("src/resources/grass3.png");
    private Sprite bigSky = new Sprite("src/resources/dreamybackgroundwindows.png");
    private Sprite speedometer = new Sprite("src/resources/speedometer.png");
    private Sprite needle = new Sprite("src/resources/speedometerneedle.png");
    private Sprite gear0 = new Sprite("src/resources/gear0.png");
    private Sprite gear1 = new Sprite("src/resources/gear1.png");
    private Sprite gear2 = new Sprite("src/resources/gear2.png");
    private Sprite gear3 = new Sprite("src/resources/gear3.png");
    private Sprite gear4 = new Sprite("src/resources/gear4.png");
    private Sprite gear5 = new Sprite("src/resources/gear5.png");
    private Sprite gear6 = new Sprite("src/resources/gear6.png");
    private Sprite gameOver = new Sprite("src/resources/GameOver.png");


    // window properties
    private int WIDTHScreenSize;
    private int HEIGHTScreenSize;
    private int map2D[][];
    private ArrayList<Integer> terrain = new ArrayList<>();

    // worldImage dimensions
    private final int WIDTHgameWorld = 26080;
    private final int HEIGHTgameWorld = 960;

    // window image
    public JPanel worldPanel;
    private BufferedImage worldImage;
    private Graphics2D worldGraphics;

    Window(int WIDTHScreenSize, int HEIGHTScreenSize) throws IOException {
        this.readTXT();
        this.WIDTHScreenSize = WIDTHScreenSize;
        this.HEIGHTScreenSize = HEIGHTScreenSize;
        this.worldImage = new BufferedImage(WIDTHgameWorld, HEIGHTgameWorld, BufferedImage.TYPE_INT_RGB);

        worldPanel = new JPanel() {
            @Override
            public void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                drawMap();
                drawScreen((Graphics2D) graphics);
                graphics.dispose();
                worldGraphics.dispose();
            }
        };
        this.add(worldPanel);

        // observer/ observables & key listeners
        this.p = new Player(mobile);
        GameEvents events = new GameEvents();
        events.addObserver(p);
        KeyBoardControl keys = new KeyBoardControl(events);
        this.addKeyListener(keys);

        noItems = 20;
        this.populateMobiles();

        // collision detector
        this.collisionDetector = new CollisionDetector(
                this.mobile,
                this.mobiles,
                events
        );

        jframeSetup();

    }

    private void drawItems (Graphics gItem) {
        this.mobile.drawMobile(gItem);
        for (Mobile mobile : this.mobiles) {
            mobile.drawMobile(gItem);
        }
    }

    private void populateMobiles() {
        mobiles.clear();
        for (int i = 0; i < noItems; i++ ) {
            int newPositionX = (int) (Math.random() * 23520 + 1280);
            int newPositionY = 170 + (160 / noItems) * i;
            Mobile mobile = new Mobile(newPositionX, newPositionY, "src/resources/coupe3.png", 1, (short) -90);
            mobiles.add(mobile);
        }
    }

    private void jframeSetup() {
        setTitle("The Forever Highway");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits game when you press the x
        setResizable(false);
        setSize(WIDTHScreenSize, HEIGHTScreenSize);
        setLocationRelativeTo(null); // centers the window
        setVisible(true);
        setFocusable(true);
        requestFocusInWindow();
    }

    private void drawScore(Graphics2D g2) {
        Font font = new Font("Score: " + mobile.getScore(), Font.PLAIN, 26);
        g2.setFont(font);

        String score = "Score: " + mobile.getScore();
        AttributedString as1 = new AttributedString(score);
        as1.addAttribute(TextAttribute.FONT, font);
        as1.addAttribute(TextAttribute.FOREGROUND, Color.white, 0, score.length());

        g2.drawString(as1.getIterator(), 0, 450);
    }

    private void readTXT() throws IOException {
        int tileWidth = WIDTHgameWorld / 32;
        map2D = new int[23][tileWidth];
        int[][] spawnZone = new int[23][40];
        try {
            File file = new File("src/resources/map.txt");
            BufferedReader bufferReader = new BufferedReader(new FileReader(file));
            ArrayList<Integer> szterrain = new ArrayList<>();
            String line;

            int lineNo = 0 ;
            int terrainCount = 0;
            while ((line = bufferReader.readLine()) != null) {
                int pos = 0;
                for (int i = 0; i < tileWidth; i++) {
                        if (i > 79) {
                        pos = 0;
                    }
                    int tileType = Integer.parseInt(String.valueOf(line.charAt(pos)));
                    int seed = (int) (Math.round(Math.random() * 7) + 1);

                    map2D[lineNo][i] = tileType;

                    if (i < 40) {
                        spawnZone[lineNo][i] = tileType;
                        if (map2D[lineNo][i] == 2 || map2D[lineNo][i] == 4) {
                            terrain.add(seed);
                            szterrain.add(seed);
                        }
                    }
                    else if (i >= 775) {
                        map2D[lineNo][i] = spawnZone[lineNo][pos];
                        if (map2D[lineNo][i] == 2 || map2D[lineNo][i] == 4) {
                            terrain.add(szterrain.get(terrainCount));
                            terrainCount++;
                        }
                    }
                    else {
                        if (map2D[lineNo][i] == 2 || map2D[lineNo][i] == 4) {
                            terrain.add(seed);
                        }
                    }
                    pos++;
                }
                lineNo++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // TODO DRAW SPEEDOMETER
    private void drawSpeedometer(Graphics2D g2) {
        int width = speedometer.getImage().getWidth();
        g2.drawImage(speedometer.getImage(), 320 + width, 350, this);
        g2.drawImage(speedometer.getImage(), 320 - width * 2, 350, this);

        int speed = this.mobile.getSpeed();
        speed = (int) (Math.round(speed / 2.1) * 2.7) - 45;
        int rev = this.mobile.getRev();
        rev = (int) (rev * 2.7) - 45;

        AffineTransform rotation = AffineTransform.getTranslateInstance(320 + width * 1.41, 350 + width * 0.43);
        rotation.rotate(Math.toRadians(speed) + Math.PI, 8, 5);
        g2.drawImage(this.needle.getImage(), rotation, this);

        AffineTransform rotation2 = AffineTransform.getTranslateInstance(320 - width * 1.59, 350 + width * 0.43);
        rotation2.rotate(Math.toRadians(rev) + Math.PI, 8, 5);
        g2.drawImage(this.needle.getImage(), rotation2, this);
    }

    private void drawScreen(Graphics2D graphics) {
        if (this.mobile.getGameOveR()) {
            graphics.drawImage(gameOver.getImage(), 0, 0, this);
        }
        /*
        worldimagex - width
        worldimagey - height
        keep on the screen do not have the tank follow
         */
        BufferedImage playerView = worldImage.getSubimage(
                mobile.getX(),      // where you start drawing from
                playerViewY(mobile),// where you start drawing from
                WIDTHScreenSize,    // how tall on window of game
                HEIGHTScreenSize);  // how long on window of game
        graphics.drawImage(playerView, 0, 0, null);
        drawSpeedometer(graphics);
        drawGearShift(graphics);
        drawScore(graphics);
    }

    private void drawGearShift(Graphics2D g2) {
        if (this.mobile.getGearbox().getGear() == 0) {
            g2.drawImage(this.gear0.getImage(), 270, 350, this);
        }

        if (this.mobile.getGearbox().getGear() == 1) {
            g2.drawImage(this.gear1.getImage(), 270, 350, this);
        }

        if (this.mobile.getGearbox().getGear() == 2) {
            g2.drawImage(this.gear2.getImage(), 270, 350, this);
        }

        if (this.mobile.getGearbox().getGear() == 3) {
            g2.drawImage(this.gear3.getImage(), 270, 350, this);
        }
        if (this.mobile.getGearbox().getGear() == 4) {
            g2.drawImage(this.gear4.getImage(), 270, 350, this);
        }

        if (this.mobile.getGearbox().getGear() == 5) {
            g2.drawImage(this.gear5.getImage(), 270, 350, this);
        }

        if (this.mobile.getGearbox().getGear() == 6) {
            g2.drawImage(this.gear6.getImage(), 270, 350, this);
        }
    }

    private int playerViewY(Mobile player) {
        int yOffset = (int) (player.getY() + (player.getHeight() * 0.5) - HEIGHTScreenSize);

        if (player.getY() + yOffset <= 0 ) {
            return 0;
        }
        else if (player.getY() + yOffset > 0 && player.getY() + yOffset < HEIGHTgameWorld - HEIGHTScreenSize) {
            return player.getY() + yOffset;
        }
        else if (player.getY() + yOffset >= HEIGHTgameWorld - HEIGHTScreenSize) {
            return HEIGHTgameWorld - HEIGHTScreenSize;
        }
        return 0;
    }

    private void drawMap() {
//        draw everything to a temp world map. This does not display.
//        worldGraphics is connected to Worldimage and we get subimages of bufferedimage
        worldGraphics = worldImage.createGraphics();
        drawTiledMap(worldGraphics);
        drawItems(worldGraphics);
        worldGraphics.dispose();
    }

    private void drawTiledMap(Graphics2D g2){
        int TileWIDTH = WIDTHgameWorld / 32;
        int TileHEIGHT = 23;

        int i = 0;
        drawSky(g2);

        for(int y = 0; y < TileHEIGHT; y++) {
            for (int x = 0; x < TileWIDTH; x++) {
                if (map2D[y][x] == 4) {
                    drawGrass(g2, i, x, y);
                    i++;
                }
                if(map2D[y][x] == 2){
                    drawRoadLane(g2, i, x, y);
                    i++;
                }
                else if (map2D[y][x] == 3) {
                    drawShoulder(g2, x, y);
                }
                else if (map2D[y][x] == 5) {
                    drawRoadCenter(g2, i, x, y);
                }
            }
        }
    }

    private AffineTransform rotate90(int i, int x, int y) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x * 32, y * 32);
        rotation.rotate(Math.toRadians(90 * terrain.get(i)), 16, 16);
        return rotation;
    }

    private void drawSky(Graphics2D g2) {
        int currentPos = (int) Math.ceil(mobile.getX() / 10.34);
        System.out.println("currentPos: " + currentPos);
        g2.drawImage(bigSky.getImage(), mobile.getX(), 0, mobile.getX() + 640, 192, currentPos, 0, currentPos + 640, 192, this);
//        g2d.drawImage(source, x, y, x+width, y+height,
//                  frameX, frameY, frameX+width, frameY+height, this);
    }

    private void drawGrass(Graphics2D g2, int i, int x, int y) {
        g2.drawImage(grass1.getImage(), x * 32, y * 32, this);

        if (terrain.get(i) > 0 && terrain.get(i) <= 2) {
            g2.drawImage(grass4.getImage(), x * 32, y * 32, this);
        }
        else if (terrain.get(i) > 2 && terrain.get(i) <= 4) {
            g2.drawImage(grass2.getImage(), x * 32, y * 32, this);
        }
        else if (terrain.get(i) > 4 && terrain.get(i) <= 6) {
            g2.drawImage(grass3.getImage(), x * 32, y * 32, this);
        }
    }

    private void drawRoadLane(Graphics2D g2, int i, int x, int y) {
        g2.drawImage(road.getImage(), rotate90(i, x, y), this);
        g2.drawImage(road1.getImage(), x * 32, y * 32, this);
    }

    private void drawShoulder(Graphics2D g2, int x, int y) {
        g2.drawImage(grass1.getImage(), x * 32, y * 32, this);
        g2.drawImage(roadShoulder.getImage(), x * 32, y * 32, null);
    }

    private void drawRoadCenter(Graphics2D g2, int i, int x, int y) {
        g2.drawImage(road.getImage(), rotate90(i, x, y), this);
        g2.drawImage(road3.getImage(), x * 32, y * 32, this);
        g2.drawImage(road4.getImage(), x * 32, y * 32 - 32, this);
        g2.drawImage(road2.getImage(), x * 32, y * 32 - 96, this);
        g2.drawImage(fence1.getImage(), x * 32, y * 32 - 128, this);
    }

    // returns true if x, y is likely visible within the screen
    private boolean inView(int x, int y) {
        boolean inView = false;
        if (mobile.getX() - x < 640 || mobile.getX() - x > 0) {
            if (Math.abs(mobile.getY() - y) < 384) {
                inView = true;
            }
        }
        return inView;
    }

}
