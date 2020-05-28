package pacman;

import java.awt.*;
import java.awt.image.*;

public class Cherry {
    private final int chance = 300;

    private int dimension = 27;
    
    private final BufferedImage image = GraphicsOptions.resize(GraphicsOptions.assets.image_cherries, dimension,
            dimension);
    
    private final Point position = new Point((int) (DrawCanvas.GAME_SIZE.getWidth() / 2),
            (int) ((DrawCanvas.GAME_SIZE.getHeight() - 30) * 1.165 / 2)); // spawn position of cherries
    
    private boolean showCherry = false;

    public boolean draw(Graphics g, boolean state, Point player_position, DrawCanvas canvas) {
        if (showCherry) {
            g.drawImage(image, (int) position.getX() - dimension / 2,
                    (int) position.getY() - dimension / 2, canvas);

            int[] coords = { (int) position.getX(), (int) position.getY() };

            if (DrawCanvas.inRange(coords, player_position)) {
                showCherry = false;
                return true;
            }
        } else {
            showCherry = 0 == (int) (Math.random() * chance);
        }

        return false;
    }
}