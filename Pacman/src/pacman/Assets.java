package pacman;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Assets {
    private final BufferedImage DEFAULT_IMAGE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    
    public BufferedImage image_player_full_circle = DEFAULT_IMAGE;
    public BufferedImage image_player_small_mouth = DEFAULT_IMAGE;
    public BufferedImage image_player_large_mouth = DEFAULT_IMAGE;
    
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

    public Assets() {
        try {
            image_player_full_circle = ImageIO.read(getClass().getResource("assets/player_full_circle.png"));
            image_player_small_mouth = ImageIO.read(getClass().getResource("assets/player_small_mouth.png"));
            image_player_large_mouth = ImageIO.read(getClass().getResource("assets/player_large_mouth.png"));

            image_cherries = ImageIO.read(getClass().getResource("assets/cherries.png"));
            image_background = ImageIO.read(getClass().getResource("assets/background2.png"));

            image_red_left_body1 = ImageIO.read(getClass().getResource("assets/red_left_body1.png"));
            image_red_left_body2 = ImageIO.read(getClass().getResource("assets/red_left_body2.png"));
            image_red_right_body1 = ImageIO.read(getClass().getResource("assets/red_right_body1.png"));
            image_red_right_body2 = ImageIO.read(getClass().getResource("assets/red_right_body2.png"));
            image_red_up_body1 = ImageIO.read(getClass().getResource("assets/red_up_body1.png"));
            image_red_up_body2 = ImageIO.read(getClass().getResource("assets/red_up_body2.png"));
            image_red_down_body1 = ImageIO.read(getClass().getResource("assets/red_down_body1.png"));
            image_red_down_body2 = ImageIO.read(getClass().getResource("assets/red_down_body2.png"));

            image_cyan_left_body1 = ImageIO.read(getClass().getResource("assets/cyan_left_body1.png"));
            image_cyan_left_body2 = ImageIO.read(getClass().getResource("assets/cyan_left_body2.png"));
            image_cyan_right_body1 = ImageIO.read(getClass().getResource("assets/cyan_right_body1.png"));
            image_cyan_right_body2 = ImageIO.read(getClass().getResource("assets/cyan_right_body2.png"));
            image_cyan_up_body1 = ImageIO.read(getClass().getResource("assets/cyan_up_body1.png"));
            image_cyan_up_body2 = ImageIO.read(getClass().getResource("assets/cyan_up_body2.png"));
            image_cyan_down_body1 = ImageIO.read(getClass().getResource("assets/cyan_down_body1.png"));
            image_cyan_down_body2 = ImageIO.read(getClass().getResource("assets/cyan_down_body2.png"));

            image_pink_left_body1 = ImageIO.read(getClass().getResource("assets/pink_left_body1.png"));
            image_pink_left_body2 = ImageIO.read(getClass().getResource("assets/pink_left_body2.png"));
            image_pink_right_body1 = ImageIO.read(getClass().getResource("assets/pink_right_body1.png"));
            image_pink_right_body2 = ImageIO.read(getClass().getResource("assets/pink_right_body2.png"));
            image_pink_up_body1 = ImageIO.read(getClass().getResource("assets/pink_up_body1.png"));
            image_pink_up_body2 = ImageIO.read(getClass().getResource("assets/pink_up_body2.png"));
            image_pink_down_body1 = ImageIO.read(getClass().getResource("assets/pink_down_body1.png"));
            image_pink_down_body2 = ImageIO.read(getClass().getResource("assets/pink_down_body2.png"));

            image_yellow_left_body1 = ImageIO.read(getClass().getResource("assets/yellow_left_body1.png"));
            image_yellow_left_body2 = ImageIO.read(getClass().getResource("assets/yellow_left_body2.png"));
            image_yellow_right_body1 = ImageIO.read(getClass().getResource("assets/yellow_right_body1.png"));
            image_yellow_right_body2 = ImageIO.read(getClass().getResource("assets/yellow_right_body2.png"));
            image_yellow_up_body1 = ImageIO.read(getClass().getResource("assets/yellow_up_body1.png"));
            image_yellow_up_body2 = ImageIO.read(getClass().getResource("assets/yellow_up_body2.png"));
            image_yellow_down_body1 = ImageIO.read(getClass().getResource("assets/yellow_down_body1.png"));
            image_yellow_down_body2 = ImageIO.read(getClass().getResource("assets/yellow_down_body2.png"));
        } catch (IOException e) {
            System.err.println("IOException ERROR: " + e);
            System.exit(-1);
        } catch (Exception e) {
            System.err.println("ERROR READING IMAGES: " + e);
            System.exit(-1);
        }
    }
}