package pacman;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785+22);
    public static final Dimension GAME_SIZE = new Dimension(615, 785);
    public static final Color CANVAS_BACKGROUND = Color.BLACK;

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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(CANVAS_BACKGROUND);
        backgroundImage = GraphicsOptions.resize(assets.image_background, (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight());
        
        g.drawImage(backgroundImage, 0, 0, this);
        g.drawImage(current_player, (int) player_position.getX(), (int) player_position.getY(), this);

        g.setColor(Color.RED);
        g.fillOval(0, 363, 5, 5);
        g.drawLine(0, 363, 120, 363);
        g.fillOval(120, 363, 5, 5);
        g.drawLine(120, 363, 120, 282);
        g.fillOval(120, 282, 5, 5);
        g.drawLine(120, 282, 11, 282);
        g.fillOval(11, 282, 5, 5);
        g.drawLine(11, 282, 11, 81);
        g.fillOval(11, 81, 5, 5);
        g.drawLine(11, 81, 299, 81);
        g.fillOval(299, 81, 5, 5);

        g.fillOval(0, 412, 5, 5);

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