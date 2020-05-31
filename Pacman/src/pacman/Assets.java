package pacman;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Contains all image files used by Pacman as BufferedImage objects
 */
public class Assets {
    private final BufferedImage DEFAULT_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    
    public BufferedImage image_player_full_circle = DEFAULT_IMAGE;
    public BufferedImage image_player_small_mouth_left = DEFAULT_IMAGE;
    public BufferedImage image_player_large_mouth_left = DEFAULT_IMAGE;
    public BufferedImage image_player_small_mouth_right = DEFAULT_IMAGE;
    public BufferedImage image_player_large_mouth_right = DEFAULT_IMAGE;
    public BufferedImage image_player_small_mouth_up = DEFAULT_IMAGE;
    public BufferedImage image_player_large_mouth_up = DEFAULT_IMAGE;
    public BufferedImage image_player_small_mouth_down = DEFAULT_IMAGE;
    public BufferedImage image_player_large_mouth_down = DEFAULT_IMAGE;
    
    public BufferedImage image_cherries = DEFAULT_IMAGE;
    public BufferedImage image_background = DEFAULT_IMAGE;
    
    public BufferedImage image_red_left_body1 = DEFAULT_IMAGE;
    public BufferedImage image_red_left_body2 = DEFAULT_IMAGE;
    public BufferedImage image_red_right_body1 = DEFAULT_IMAGE;
    public BufferedImage image_red_right_body2 = DEFAULT_IMAGE;
    public BufferedImage image_red_up_body1 = DEFAULT_IMAGE;
    public BufferedImage image_red_up_body2 = DEFAULT_IMAGE;
    public BufferedImage image_red_down_body1 = DEFAULT_IMAGE;
    public BufferedImage image_red_down_body2 = DEFAULT_IMAGE;
    
    public BufferedImage image_cyan_left_body1 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_left_body2 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_right_body1 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_right_body2 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_up_body1 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_up_body2 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_down_body1 = DEFAULT_IMAGE;
    public BufferedImage image_cyan_down_body2 = DEFAULT_IMAGE;
    
    public BufferedImage image_pink_left_body1 = DEFAULT_IMAGE;
    public BufferedImage image_pink_left_body2 = DEFAULT_IMAGE;
    public BufferedImage image_pink_right_body1 = DEFAULT_IMAGE;
    public BufferedImage image_pink_right_body2 = DEFAULT_IMAGE;
    public BufferedImage image_pink_up_body1 = DEFAULT_IMAGE;
    public BufferedImage image_pink_up_body2 = DEFAULT_IMAGE;
    public BufferedImage image_pink_down_body1 = DEFAULT_IMAGE;
    public BufferedImage image_pink_down_body2 = DEFAULT_IMAGE;
    
    public BufferedImage image_yellow_left_body1 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_left_body2 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_right_body1 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_right_body2 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_up_body1 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_up_body2 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_down_body1 = DEFAULT_IMAGE;
    public BufferedImage image_yellow_down_body2 = DEFAULT_IMAGE;

    public Font pacmanFont;

    public Assets() {
        try {
            image_player_full_circle = ImageIO.read(getClass().getResource("assets/player/player_full_circle.png"));
            image_player_small_mouth_left = ImageIO.read(getClass().getResource("assets/player/player_small_mouth_left.png"));
            image_player_large_mouth_left = ImageIO.read(getClass().getResource("assets/player/player_large_mouth_left.png"));
            image_player_small_mouth_right = ImageIO.read(getClass().getResource("assets/player/player_small_mouth_right.png"));
            image_player_large_mouth_right = ImageIO.read(getClass().getResource("assets/player/player_large_mouth_right.png"));
            image_player_small_mouth_up = ImageIO.read(getClass().getResource("assets/player/player_small_mouth_up.png"));
            image_player_large_mouth_up = ImageIO.read(getClass().getResource("assets/player/player_large_mouth_up.png"));
            image_player_small_mouth_down = ImageIO.read(getClass().getResource("assets/player/player_small_mouth_down.png"));
            image_player_large_mouth_down = ImageIO.read(getClass().getResource("assets/player/player_large_mouth_down.png"));

            image_cherries = ImageIO.read(getClass().getResource("assets/cherries.png"));
            image_background = ImageIO.read(getClass().getResource("assets/background3.png"));

            image_red_left_body1 = ImageIO.read(getClass().getResource("assets/red/red_left_body1.png"));
            image_red_left_body2 = ImageIO.read(getClass().getResource("assets/red/red_left_body2.png"));
            image_red_right_body1 = ImageIO.read(getClass().getResource("assets/red/red_right_body1.png"));
            image_red_right_body2 = ImageIO.read(getClass().getResource("assets/red/red_right_body2.png"));
            image_red_up_body1 = ImageIO.read(getClass().getResource("assets/red/red_up_body1.png"));
            image_red_up_body2 = ImageIO.read(getClass().getResource("assets/red/red_up_body2.png"));
            image_red_down_body1 = ImageIO.read(getClass().getResource("assets/red/red_down_body1.png"));
            image_red_down_body2 = ImageIO.read(getClass().getResource("assets/red/red_down_body2.png"));

            image_cyan_left_body1 = ImageIO.read(getClass().getResource("assets/cyan/cyan_left_body1.png"));
            image_cyan_left_body2 = ImageIO.read(getClass().getResource("assets/cyan/cyan_left_body2.png"));
            image_cyan_right_body1 = ImageIO.read(getClass().getResource("assets/cyan/cyan_right_body1.png"));
            image_cyan_right_body2 = ImageIO.read(getClass().getResource("assets/cyan/cyan_right_body2.png"));
            image_cyan_up_body1 = ImageIO.read(getClass().getResource("assets/cyan/cyan_up_body1.png"));
            image_cyan_up_body2 = ImageIO.read(getClass().getResource("assets/cyan/cyan_up_body2.png"));
            image_cyan_down_body1 = ImageIO.read(getClass().getResource("assets/cyan/cyan_down_body1.png"));
            image_cyan_down_body2 = ImageIO.read(getClass().getResource("assets/cyan/cyan_down_body2.png"));

            image_pink_left_body1 = ImageIO.read(getClass().getResource("assets/pink/pink_left_body1.png"));
            image_pink_left_body2 = ImageIO.read(getClass().getResource("assets/pink/pink_left_body2.png"));
            image_pink_right_body1 = ImageIO.read(getClass().getResource("assets/pink/pink_right_body1.png"));
            image_pink_right_body2 = ImageIO.read(getClass().getResource("assets/pink/pink_right_body2.png"));
            image_pink_up_body1 = ImageIO.read(getClass().getResource("assets/pink/pink_up_body1.png"));
            image_pink_up_body2 = ImageIO.read(getClass().getResource("assets/pink/pink_up_body2.png"));
            image_pink_down_body1 = ImageIO.read(getClass().getResource("assets/pink/pink_down_body1.png"));
            image_pink_down_body2 = ImageIO.read(getClass().getResource("assets/pink/pink_down_body2.png"));

            image_yellow_left_body1 = ImageIO.read(getClass().getResource("assets/yellow/yellow_left_body1.png"));
            image_yellow_left_body2 = ImageIO.read(getClass().getResource("assets/yellow/yellow_left_body2.png"));
            image_yellow_right_body1 = ImageIO.read(getClass().getResource("assets/yellow/yellow_right_body1.png"));
            image_yellow_right_body2 = ImageIO.read(getClass().getResource("assets/yellow/yellow_right_body2.png"));
            image_yellow_up_body1 = ImageIO.read(getClass().getResource("assets/yellow/yellow_up_body1.png"));
            image_yellow_up_body2 = ImageIO.read(getClass().getResource("assets/yellow/yellow_up_body2.png"));
            image_yellow_down_body1 = ImageIO.read(getClass().getResource("assets/yellow/yellow_down_body1.png"));
            image_yellow_down_body2 = ImageIO.read(getClass().getResource("assets/yellow/yellow_down_body2.png"));

            pacmanFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("assets/Emulogic.ttf").openStream());
            // pacmanFont = pacmanFont.deriveFont(Font.PLAIN, 20);
        } catch (IOException e) {
            System.err.println("IOException ERROR: " + e);
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("ERROR READING FILES: " + e);
            System.exit(-1);
        }
    }
}