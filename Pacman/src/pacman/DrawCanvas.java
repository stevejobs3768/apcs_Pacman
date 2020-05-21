package pacman;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785+22);
    public static final Dimension GAME_SIZE = new Dimension(615, 785);
    public static final Color CANVAS_BACKGROUND = Color.black;

    public static final int PLAYER_DIMENSION = 32;

    private Point player_position = new Point((int)(GAME_SIZE.getWidth() / 2), (int)(GAME_SIZE.getHeight() * 1.49 / 2));
    
    private BufferedImage backgroundImage;
    private Assets assets = new Assets();

    private BufferedImage full_circle = GraphicsOptions.resize(assets.image_player_full_circle, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage small_mouth_left = GraphicsOptions.resize(assets.image_player_small_mouth_left, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage large_mouth_left = GraphicsOptions.resize(assets.image_player_large_mouth_left, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage small_mouth_right = GraphicsOptions.resize(assets.image_player_small_mouth_right, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage large_mouth_right = GraphicsOptions.resize(assets.image_player_large_mouth_right, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage small_mouth_up = GraphicsOptions.resize(assets.image_player_small_mouth_up, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage large_mouth_up = GraphicsOptions.resize(assets.image_player_large_mouth_up, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage small_mouth_down = GraphicsOptions.resize(assets.image_player_small_mouth_down, PLAYER_DIMENSION, PLAYER_DIMENSION);
    private BufferedImage large_mouth_down = GraphicsOptions.resize(assets.image_player_large_mouth_down, PLAYER_DIMENSION, PLAYER_DIMENSION);

    private BufferedImage current_player = full_circle;

    private boolean current_large_mouth = false;

    private final int SHIFT = 5;
    private final int THRESHOLD = 5;

    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;

    private boolean upAvail = true;
    private boolean downAvail = true;
    private boolean leftAvail = true;
    private boolean rightAvail = true;

    private final int[][] leftPoints = {{79, 584}, {79, 650}, 
    {144, 105}, {144, 192}, {144, 257}, {144, 388}, {144, 520}, {144, 650}, 
    {210, 192}, {210, 388}, {210, 520}, {210, 584}, 
    {275, 105}, {275, 192}, {275, 257}, {275, 323}, {275, 454}, {275, 520}, {275, 584}, {275, 650}, {275, 715}, 
    {340, 192}, {340, 323}, {340, 454}, {340, 584}, {340, 715}, 
    {406, 192}, {406, 257}, {406, 323}, {406, 454}, {406, 520}, {406, 584}, {406, 650}, 
    {471, 105}, {471, 192}, {471, 257}, {471, 388}, {471, 520}, {471, 584}, 
    {536, 650}, 
    {580, 105}, {580, 192}, {580, 257}, {580, 520}, {580, 584}, {580, 650}, {580, 715}};
    private final int[][] rightPoints = {{536, 584}, {536, 650}, 
    {471, 105}, {471, 192}, {471, 257}, {471, 388}, {471, 520}, {471, 650}, 
    {406, 192}, {406, 388}, {406, 520}, {406, 584}, 
    {340, 105}, {340, 192}, {340, 257}, {340, 323}, {340, 454}, {340, 520}, {340, 584}, {340, 650}, {340, 715}, 
    {275, 192}, {275, 323}, {275, 454}, {275, 584}, {275, 715}, 
    {210, 192}, {210, 257}, {210, 323}, {210, 454}, {210, 520}, {210, 584}, {210, 650}, 
    {144, 105}, {144, 192}, {144, 257}, {144, 388}, {144, 520}, {144, 584}, 
    {79, 650}, 
    {35, 105}, {35, 192}, {35, 257}, {35, 520}, {35, 584}, {35, 650}, {35, 715}};
    private final int[][] upPoints = {{35, 192}, {144, 192}, {210, 192}, {275, 192}, {340, 192}, {406, 192}, {471, 192}, {580, 192}, 
    {35, 257}, {144, 257}, {210, 257}, {406, 257}, {471, 257}, {580, 257}, 
    {275, 323}, {340, 323}, 
    {144, 388}, {210, 388}, {406, 388}, {471, 388}, 
    {210, 454}, {406, 454}, 
    {144, 520}, {210, 520}, {406, 520}, {471, 520}, 
    {35, 584}, {144, 584}, {275, 584}, {340, 584}, {471, 584}, {580, 584}, 
    {79, 650}, {144, 650}, {210, 650}, {406, 650}, {471, 650}, {536, 650}, 
    {35, 715}, {275, 715}, {340, 715}, {580, 715}};
    private final int[][] downPoints = {{35, 105}, {144, 105}, {275, 105}, {340, 105}, {471, 105}, {580, 105}, 
    {35, 192}, {144, 192}, {210, 192}, {406, 192}, {471, 192}, {580, 192}, 
    {144, 257}, {275, 257}, {340, 257}, {471, 257}, 
    {210, 323}, {406, 323}, 
    {144, 388}, {210, 388}, {406, 388}, {471, 388}, 
    {210, 454}, {406, 454}, 
    {35, 520}, {144, 520}, {275, 520}, {340, 520}, {471, 520}, {580, 520}, 
    {79, 584}, {144, 584}, {210, 584}, {406, 584}, {471, 584}, {536, 584}, 
    {35, 650}, {275, 650}, {340, 650}, {580, 650}};

    private int[] currentCoords = new int[2];

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(CANVAS_BACKGROUND);
        backgroundImage = GraphicsOptions.resize(assets.image_background, (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight());
        
        g.drawImage(backgroundImage, 0, 0, this);
        g.drawImage(current_player, (int) player_position.getX() - PLAYER_DIMENSION/2, (int) player_position.getY() - PLAYER_DIMENSION/2, this);

        System.out.println(player_position);

        currentCoords[0] = 0;
        currentCoords[1] = 0;
        
        if (up || down || left || right) {
            upAvail = false;
            downAvail = false;
            leftAvail = false;
            rightAvail = false;
        }
        
        g.setColor(Color.green);
        for (int[] coords : leftPoints) {
            g.fillOval(coords[0]-10, coords[1]-5, 10, 10);
            if (inRange(coords, player_position.getX(), player_position.getY())) {
                leftAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
            }
        }
        
        g.setColor(Color.orange);
        for (int[] coords : upPoints) {
            g.fillOval(coords[0]-5, coords[1]-10, 10, 10);
            if (inRange(coords, player_position.getX(), player_position.getY())) {
                upAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
            }
        }
        
        g.setColor(Color.cyan);
        for (int[] coords : rightPoints) {
            g.fillOval(coords[0], coords[1]-5, 10, 10);
            if (inRange(coords, player_position.getX(), player_position.getY())) {
                rightAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
            }
        }
        
        g.setColor(Color.magenta);
        for (int[] coords : downPoints) {
            g.fillOval(coords[0]-5, coords[1], 10, 10);
            if (inRange(coords, player_position.getX(), player_position.getY())) {
                downAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
            }
        }
        
        if (!leftAvail && !rightAvail && !upAvail && !downAvail) {
            if (up || down) {
                upAvail = true;
                downAvail = true;
            } else if (left || right) {
                leftAvail = true;
                rightAvail = true;
            }
        }
        
        System.out.println(upAvail + " " + downAvail + " " + leftAvail + " " + rightAvail);
        
        if (current_large_mouth) {
            if (up) {
                current_player = small_mouth_up;
            } else if (down) {
                current_player = small_mouth_down;
            } else if (left) {
                current_player = small_mouth_left;
            } else if (right) {
                current_player = small_mouth_right;
            }
            current_large_mouth = false;
        } else {
            if (up) {
                current_player = large_mouth_up;
            } else if (down) {
                current_player = large_mouth_down;
            } else if (left) {
                current_player = large_mouth_left;
            } else if (right) {
                current_player = large_mouth_right;
            }
            current_large_mouth = true;
        }

        if (up && upAvail) {
            player_position.translate(0, -1 * SHIFT);
        } else if (down && downAvail) {
            player_position.translate(0, SHIFT);
        } else if (left && leftAvail) {
            player_position.translate(-1 * SHIFT, 0);
        } else if (right && rightAvail) {
            player_position.translate(SHIFT, 0);
        }
        repaint();
    }

    public boolean inRange(int[] coords, double x, double y) {
        return (x > coords[0] - THRESHOLD) && (x < coords[0] + THRESHOLD) && (y > coords[1] - THRESHOLD) && (y < coords[1] + THRESHOLD);
    }

    public void shiftPlayerUp() {
        if (upAvail) {
            up = true;
            down = false;
            left = false;
            right = false;
            
            if (currentCoords[0] != 0 && currentCoords[1] != 0) {
                player_position.setLocation(currentCoords[0], currentCoords[1]);
            }
        }
    }
    
    public void shiftPlayerDown() {
        if (downAvail) {
            up = false;
            down = true;
            left = false;
            right = false;

            if (currentCoords[0] != 0 && currentCoords[1] != 0) {
                player_position.setLocation(currentCoords[0], currentCoords[1]);
            }
        }
    }
    
    public void shiftPlayerLeft() {
        if (leftAvail) {
            up = false;
            down = false;
            left = true;
            right = false;
            
            if (currentCoords[0] != 0 && currentCoords[1] != 0) {
                player_position.setLocation(currentCoords[0], currentCoords[1]);
            }
        }
    }
    
    public void shiftPlayerRight() {
        if (rightAvail) {
            up = false;
            down = false;
            left = false;
            right = true;
            
            if (currentCoords[0] != 0 && currentCoords[1] != 0) {
                player_position.setLocation(currentCoords[0], currentCoords[1]);
            }
        }
    }

}