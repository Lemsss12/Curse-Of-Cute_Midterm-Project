import java.awt.*;
import java.awt.image.*;
import javax.swing.*;

public class GamePanel extends JPanel {
    // map
    public  int tileSize = 32;
    public final int gamePanelSizeX = 800;
    public final int gamePanelSizeY = 600;
    public int mapX = 13*32;
    public int mapY = 13*32;
    @SuppressWarnings("unused")
    private final int scaleMultiplier = 2;
    public BufferedImage grassTile;
    public BufferedImage tree1;
    PlayerStatus ps = new PlayerStatus(this);
    public int speed = 5;
    public int whatFrame = 0;
    public final int frameSpeed = 3; // higher = slower animation
    public int frameDelay = 0; // counts frames for delay
    public int playerX = 0;
    public int playerY = 0;
    public int playerXCollision = playerX;
    public int playerYCollision = (playerY-tileSize);
    // tile
    public int tileRow = 50; // rows of grid
    public int tileCol = 50; // columns of grid
    public int baseLayerGrid[][] = new int[tileRow][tileCol];
    public int objectLayerGrid[][] = new int[tileRow][tileCol];
    TileManager tiles = new TileManager(this);
    // collisions
    String playerDirection = "";
    Collisions playerCollision;
    Collisions tree1Collision;
    Collisions borderCollision;
    Collisions rockCollision;
    Collisions structureCollision;

    public void entitiesCollision() {
        playerCollision = new Collisions(playerX, playerY, tileSize/2, tileSize/2);
        if (playerDirection == "up") {
            playerCollision = new Collisions(playerX+23, playerY-tileSize/3, tileSize/2, tileSize/2);
        }
        else if (playerDirection == "down") {
            playerCollision = new Collisions(playerX+23, playerY+tileSize+20, tileSize/2, tileSize/2);
        }
        else if (playerDirection == "left") {
            playerCollision = new Collisions(playerX, playerY+23, tileSize/2, tileSize/2);
        }
        else if (playerDirection == "right") {
            playerCollision = new Collisions(playerX+tileSize+10, playerY+23, tileSize/2, tileSize/2);
        }
        if(tree1Collision == null){
            tree1Collision = new Collisions(playerX, playerY, tileSize, tileSize);
        }
        borderCollision = new Collisions(0, 0, 0, 0);
        //rockCollision = new Collisions(0, 0, tileSize, tileSize);
        //structureCollision = new Collisions(0, 0, tileSize * 2, tileSize * 2);
    }

    public GamePanel() {
        loadSprites();
        KeyHandler keyH = new KeyHandler(this);
        this.setFocusable(true);
        this.addKeyListener(keyH);
        this.setPreferredSize(new Dimension(gamePanelSizeX, gamePanelSizeY));
        this.setBackground(Color.gray);
        keyH.startGameLoop();
        playerX = (gamePanelSizeX-ps.playerSizeW)/2;
        playerY = (gamePanelSizeY-ps.playerSizeH)/2;
        // If the map provided a spawn point, center the camera on that spawn
        if (tiles != null && tiles.hasSpawn) {
            mapX = tiles.spawnX * TileManager.SCALE - (gamePanelSizeX / 2);
            mapY = tiles.spawnY * TileManager.SCALE - (gamePanelSizeY / 2);
        }
    };

    // character movement
    public void moveUp() {
        playerDirection = "up";
        // check collision against all map collision rectangles
        // convert map coordinates to screen coordinates for comparison, scaled by TileManager.SCALE
        boolean colliding = false;
        for (Rectangle mapCollision : tiles.getMapCollisions()) {
            Rectangle screenCollision = new Rectangle(
                (mapCollision.x * TileManager.SCALE) - mapX,
                (mapCollision.y * TileManager.SCALE) - mapY,
                mapCollision.width * TileManager.SCALE,
                mapCollision.height * TileManager.SCALE
            );
            if (playerCollision.intersects(screenCollision)) {
                colliding = true;
                break;
            }
        }
        
        if (colliding) {
            System.out.println("Collision Detected");
            playerCollision.setBounds(playerXCollision, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        } else {
            mapY -= speed;
            playerCollision.setBounds(playerXCollision, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        }
        frameDelay++;
        if (frameDelay >= frameSpeed) {
            whatFrame = (whatFrame + 1) % 4;
            ps.currentImage = ps.upAnimation[whatFrame];
            frameDelay = 0;
        }

        repaint();
    }

    public void moveDown() {
        playerDirection = "down";
        // check collision against all map collision rectangles
        // convert map coordinates to screen coordinates for comparison, scaled by TileManager.SCALE
        boolean colliding = false;
        for (Rectangle mapCollision : tiles.getMapCollisions()) {
            Rectangle screenCollision = new Rectangle(
                (mapCollision.x * TileManager.SCALE) - mapX,
                (mapCollision.y * TileManager.SCALE) - mapY,
                mapCollision.width * TileManager.SCALE,
                mapCollision.height * TileManager.SCALE
            );
            if (playerCollision.intersects(screenCollision)) {
                colliding = true;
                break;
            }
        }
        
        if (colliding) {
            System.out.println("Collision Detected");
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        } else {
            mapY += speed;
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        }

        frameDelay++;
        if (frameDelay >= frameSpeed) {
            whatFrame = (whatFrame + 1) % 4;
            ps.currentImage = ps.downAnimation[whatFrame];
            frameDelay = 0;
        }

        repaint();
    }

    public void moveLeft() {
        playerDirection = "left";
        // check collision against all map collision rectangles
        // convert map coordinates to screen coordinates for comparison, scaled by TileManager.SCALE
        boolean colliding = false;
        for (Rectangle mapCollision : tiles.getMapCollisions()) {
            Rectangle screenCollision = new Rectangle(
                (mapCollision.x * TileManager.SCALE) - mapX,
                (mapCollision.y * TileManager.SCALE) - mapY,
                mapCollision.width * TileManager.SCALE,
                mapCollision.height * TileManager.SCALE
            );
            if (playerCollision.intersects(screenCollision)) {
                colliding = true;
                break;
            }
        }
        
        if (colliding) {
            System.out.println("Collision Detected");
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        } else {
            mapX -= speed;
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        }
        frameDelay++;
        if (frameDelay >= frameSpeed) {
            whatFrame = (whatFrame + 1) % 4;
            ps.currentImage = ps.leftAnimation[whatFrame];
            frameDelay = 0;
        }
        repaint();
    }

    public void moveRight() {
        playerDirection = "right";
        // check collision against all map collision rectangles
        // convert map coordinates to screen coordinates for comparison, scaled by TileManager.SCALE
        boolean colliding = false;
        for (Rectangle mapCollision : tiles.getMapCollisions()) {
            Rectangle screenCollision = new Rectangle(
                (mapCollision.x * TileManager.SCALE) - mapX,
                (mapCollision.y * TileManager.SCALE) - mapY,
                mapCollision.width * TileManager.SCALE,
                mapCollision.height * TileManager.SCALE
            );
            if (playerCollision.intersects(screenCollision)) {
                colliding = true;
                break;
            }
        }
        
        if (colliding) {
            System.out.println("Collision Detected");
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        } else {
            mapX += speed;
            playerCollision.setBounds(playerXCollision + 15, playerYCollision, ps.playerSizeW / 2, ps.playerSizeH);
        }
        frameDelay++;
        if (frameDelay >= frameSpeed) {
            whatFrame = (whatFrame + 1) % 4;
            ps.currentImage = ps.rightAnimation[whatFrame];
            frameDelay = 0;
        }
        repaint();
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    // sprite
    private void loadSprites() {
        System.out.println("playerSizeW: " + ps.playerSizeW);
        System.out.println("playerSizeH: " + ps.playerSizeH);
        System.out.println("playerX: " + playerX);
        System.out.println("playerY: " + playerY);
        try {
            System.out.println("Map Width: " + tileCol * tileSize);
            System.out.println("Map Height: " + tileRow * tileSize);
            System.out.println("MapX: " + mapX);
            System.out.println("MapY: " + mapY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        tiles.draw(g);
        ps.draw(g);
        //gridLines
        //for (int x = 0; x < (tileRow * tileSize); x += tileSize) {
        //    g.setColor(Color.red); // Vertical lines
        //    g.drawLine(x - mapX, 0 - mapY, x - mapX, (tileCol * tileSize) - mapY);
        //}
        //for (int y = 0; y <= (tileCol * tileSize); y += tileSize) {
        //    g.setColor(Color.red);// Horizontal lines
        //    g.drawLine(0 - mapX, y - mapY, (tileCol * tileSize) - mapX, y - mapY); 
        //}
        //g.setColor(Color.green);
        //g.drawLine(0, gamePanelSizeY / 2, gamePanelSizeX, gamePanelSizeY / 2);
        //g.setColor(Color.green);
        //g.drawLine(gamePanelSizeX / 2, gamePanelSizeY, gamePanelSizeX / 2, 0);
        
        ////grid coordinates
        //g.setColor(Color.white);
        //for (int row = 0; row < tileCol; row++) {
        //    for (int col = 0; col < tileRow; col++) {
        //        int drawX = (col * tileSize) - mapX;
        //        int drawY = (row * tileSize) - mapY + 1;
        //        g.setColor(Color.white);
        //        g.drawString(col+","+row, drawX, drawY);
        //    }
        //}
    }
}