package pacman;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785+22);
    public static final Dimension GAME_SIZE = new Dimension(615, 785);
    public static final Color CANVAS_BACKGROUND = Color.black;

    public static final int PLAYER_DIMENSION = 32;

    private Point player_position = new Point((int)((GAME_SIZE.getWidth() - PLAYER_DIMENSION) / 2), (int)((GAME_SIZE.getHeight() - PLAYER_DIMENSION) * 1.51 / 2));
    
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

    private final int SHIFT = 10;

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(CANVAS_BACKGROUND);
        backgroundImage = GraphicsOptions.resize(assets.image_background, (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight());
        
        g.drawImage(backgroundImage, 0, 0, this);
        g.drawImage(current_player, (int) player_position.getX(), (int) player_position.getY(), this);

        g.setColor(Color.red);

        // dots at every intersection of paths

        /* g.fillOval(35-5, 105-5, 10, 10);
        g.fillOval(35-5, 192-5, 10, 10);
        g.fillOval(35-5, 257-5, 10, 10);
        g.fillOval(35-5, 520-5, 10, 10);
        g.fillOval(35-5, 584-5, 10, 10);
        g.fillOval(35-5, 650-5, 10, 10);
        g.fillOval(35-5, 715-5, 10, 10);

        g.fillOval(79-5, 584-5, 10, 10);
        g.fillOval(79-5, 650-5, 10, 10);

        g.fillOval(144-5, 105-5, 10, 10);
        g.fillOval(144-5, 192-5, 10, 10);
        g.fillOval(144-5, 257-5, 10, 10);
        g.fillOval(144-5, 388-5, 10, 10);
        g.fillOval(144-5, 520-5, 10, 10);
        g.fillOval(144-5, 584-5, 10, 10);
        g.fillOval(144-5, 650-5, 10, 10);

        g.fillOval(210-5, 192-5, 10, 10);
        g.fillOval(210-5, 257-5, 10, 10);
        g.fillOval(210-5, 323-5, 10, 10);
        g.fillOval(210-5, 388-5, 10, 10);
        g.fillOval(210-5, 454-5, 10, 10);
        g.fillOval(210-5, 520-5, 10, 10);
        g.fillOval(210-5, 584-5, 10, 10);
        g.fillOval(210-5, 650-5, 10, 10);
        
        g.fillOval(275-5, 105-5, 10, 10);
        g.fillOval(275-5, 192-5, 10, 10);
        g.fillOval(275-5, 257-5, 10, 10);
        g.fillOval(275-5, 323-5, 10, 10);
        g.fillOval(275-5, 454-5, 10, 10);
        g.fillOval(275-5, 520-5, 10, 10);
        g.fillOval(275-5, 584-5, 10, 10);
        g.fillOval(275-5, 650-5, 10, 10);
        g.fillOval(275-5, 715-5, 10, 10);
        
        g.fillOval(340-5, 105-5, 10, 10);
        g.fillOval(340-5, 192-5, 10, 10);
        g.fillOval(340-5, 257-5, 10, 10);
        g.fillOval(340-5, 323-5, 10, 10);
        g.fillOval(340-5, 454-5, 10, 10);
        g.fillOval(340-5, 520-5, 10, 10);
        g.fillOval(340-5, 584-5, 10, 10);
        g.fillOval(340-5, 650-5, 10, 10);
        g.fillOval(340-5, 715-5, 10, 10);
        
        g.fillOval(406-5, 192-5, 10, 10);
        g.fillOval(406-5, 257-5, 10, 10);
        g.fillOval(406-5, 323-5, 10, 10);
        g.fillOval(406-5, 388-5, 10, 10);
        g.fillOval(406-5, 454-5, 10, 10);
        g.fillOval(406-5, 520-5, 10, 10);
        g.fillOval(406-5, 584-5, 10, 10);
        g.fillOval(406-5, 650-5, 10, 10);
        
        g.fillOval(471-5, 105-5, 10, 10);
        g.fillOval(471-5, 192-5, 10, 10);
        g.fillOval(471-5, 257-5, 10, 10);
        g.fillOval(471-5, 388-5, 10, 10);
        g.fillOval(471-5, 520-5, 10, 10);
        g.fillOval(471-5, 584-5, 10, 10);
        g.fillOval(471-5, 650-5, 10, 10);

        g.fillOval(536-5, 584-5, 10, 10);
        g.fillOval(536-5, 650-5, 10, 10);
        
        g.fillOval(580-5, 105-5, 10, 10);
        g.fillOval(580-5, 192-5, 10, 10);
        g.fillOval(580-5, 257-5, 10, 10);
        g.fillOval(580-5, 520-5, 10, 10);
        g.fillOval(580-5, 584-5, 10, 10);
        g.fillOval(580-5, 650-5, 10, 10);
        g.fillOval(580-5, 715-5, 10, 10); */
        
        g.setColor(Color.green);
        for (int[] coords : leftPoints) {
            g.fillOval(coords[0]-10, coords[1]-5, 10, 10);
        }
        
        g.setColor(Color.orange);
        for (int[] coords : upPoints) {
            g.fillOval(coords[0]-5, coords[1]-10, 10, 10);
        }
        
        g.setColor(Color.cyan);
        for (int[] coords : rightPoints) {
            g.fillOval(coords[0], coords[1]-5, 10, 10);
        }

        g.setColor(Color.magenta);
        for (int[] coords : downPoints) {
            g.fillOval(coords[0]-5, coords[1], 10, 10);
        }

        /* g.fillRect(0, 282, 120, 81);
        g.fillRect(0, 70, 11, 212);
        g.fillRect(0, 70, 615, 11);
        g.fillRect(604, 70, 11, 212);
        g.fillRect(496, 282, 120, 82); */

        /* g.fillOval(0, 363, 5, 5);
        g.drawLine(0, 363, 120, 363);
        g.fillOval(120, 363, 5, 5);
        g.drawLine(120, 363, 120, 282);
        g.fillOval(120, 282, 5, 5);
        g.drawLine(120, 282, 11, 282);
        g.fillOval(11, 282, 5, 5);
        g.drawLine(11, 282, 11, 81);
        g.fillOval(11, 81, 5, 5);
        g.drawLine(11, 81, 299, 81);
        g.fillOval(299, 81, 5, 5);*/

        // g.fillOval(0, 412, 5, 5);

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

        if (up) {
            player_position.translate(0, -1 * SHIFT);
        } else if (down) {
            player_position.translate(0, SHIFT);
        } else if (left) {
            player_position.translate(-1 * SHIFT, 0);
        } else if (right) {
            player_position.translate(SHIFT, 0);
        }
        repaint();
    }

    public void shiftPlayerUp() {
        if (upAvail) {
            up = true;
            down = false;
            left = false;
            right = false;
        }
    }
    
    public void shiftPlayerDown() {
        if (downAvail) {
            up = false;
            down = true;
            left = false;
            right = false;
        }
    }
    
    public void shiftPlayerLeft() {
        if (leftAvail) {
            up = false;
            down = false;
            left = true;
            right = false;
        }
    }
    
    public void shiftPlayerRight() {
        if (rightAvail) {
            up = false;
            down = false;
            left = false;
            right = true;
        }
    }

}