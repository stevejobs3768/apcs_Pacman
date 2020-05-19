package pacman;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785+22);
    public static final Dimension GAME_SIZE = new Dimension(615, 785);
    public static final Color CANVAS_BACKGROUND = Color.BLACK;

    public static final int PLAYER_DIMENSION = 32;

    private Point playerStart = new Point((int)((GAME_SIZE.getWidth() - PLAYER_DIMENSION) / 2), (int)((GAME_SIZE.getHeight() - PLAYER_DIMENSION) * 1.47 / 2));
    
    private BufferedImage backgroundImage;
    private BufferedImage player;
    private Assets assets = new Assets();

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(CANVAS_BACKGROUND);
        backgroundImage = GraphicsOptions.resize(assets.image_background, (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight());

        player = GraphicsOptions.resize(assets.image_player_small_mouth, PLAYER_DIMENSION, PLAYER_DIMENSION);

        g.drawImage(backgroundImage, 0, 0, this);
        g.drawImage(player, (int) playerStart.getX(), (int) playerStart.getY(), this);
    }
}