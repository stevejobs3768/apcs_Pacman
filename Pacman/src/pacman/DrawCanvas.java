package pacman;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    // I don't really understand what this is, but its absence causes a warning
    private static final long serialVersionUID = 1L;

    public static final Dimension GAME_SIZE = new Dimension(615, 785 + 30); // size of playable game window
    // size of playable game window + height of title bar
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785 + 30 + 25);
    public static final Color CANVAS_BACKGROUND = Color.black; // color of game background

    public static final int PLAYER_DIMENSION = 32; // width/height of player

    public static final int DELAY = 50; // millisecond delay between iterations of paintComponent

    public static final int COUNTER_MAX = 100;

    public static final int CHERRY_CHANCE = 300;

    private final Point player_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 1.49 / 2)); // default starting position of player

    private final Point red_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.825 / 2)); // default starting position of red ghost

    private final Point pink_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final Point cyan_position = new Point((int) (GAME_SIZE.getWidth() * 0.88 / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final Point yellow_position = new Point((int) (GAME_SIZE.getWidth() * 1.12 / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final Point cherry_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 1.165 / 2)); // spawn position of cherries

    private final Assets assets = GraphicsOptions.assets; // class containing all images
    private final BufferedImage backgroundImage = GraphicsOptions.resize(assets.image_background,
            (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight() - 30); // drawing of pacman walls, borders, etc

    // various states of the player image
    private final BufferedImage full_circle = GraphicsOptions.resize(assets.image_player_full_circle, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage small_mouth_left = GraphicsOptions.resize(assets.image_player_small_mouth_left,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage large_mouth_left = GraphicsOptions.resize(assets.image_player_large_mouth_left,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage small_mouth_right = GraphicsOptions.resize(assets.image_player_small_mouth_right,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage large_mouth_right = GraphicsOptions.resize(assets.image_player_large_mouth_right,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage small_mouth_up = GraphicsOptions.resize(assets.image_player_small_mouth_up,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage large_mouth_up = GraphicsOptions.resize(assets.image_player_large_mouth_up,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage small_mouth_down = GraphicsOptions.resize(assets.image_player_small_mouth_down,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage large_mouth_down = GraphicsOptions.resize(assets.image_player_large_mouth_down,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final BufferedImage cherries = GraphicsOptions.resize(assets.image_cherries, PLAYER_DIMENSION - 5,
            PLAYER_DIMENSION - 5);

    private final BufferedImage red_left_body = GraphicsOptions.resize(assets.image_red_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_left_body = GraphicsOptions.resize(assets.image_pink_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_left_body = GraphicsOptions.resize(assets.image_cyan_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_left_body = GraphicsOptions.resize(assets.image_yellow_left_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final BufferedImage red_right_body = GraphicsOptions.resize(assets.image_red_right_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_right_body = GraphicsOptions.resize(assets.image_pink_right_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage cyan_right_body = GraphicsOptions.resize(assets.image_cyan_right_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);
    private final BufferedImage yellow_right_body = GraphicsOptions.resize(assets.image_yellow_right_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final BufferedImage red_up_body = GraphicsOptions.resize(assets.image_red_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_up_body = GraphicsOptions.resize(assets.image_pink_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_up_body = GraphicsOptions.resize(assets.image_cyan_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_up_body = GraphicsOptions.resize(assets.image_yellow_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_down_body = GraphicsOptions.resize(assets.image_red_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_down_body = GraphicsOptions.resize(assets.image_pink_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_down_body = GraphicsOptions.resize(assets.image_cyan_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_down_body = GraphicsOptions.resize(assets.image_yellow_down_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final int SHIFT = 5; // each time the code runs, # of pixels the player moves
    private final int THRESHOLD = 4; // how close to an intersection do you have to be to be "at" that intersection

    private BufferedImage current_red = red_left_body;
    private BufferedImage current_pink = pink_up_body;
    private BufferedImage current_cyan = cyan_left_body;
    private BufferedImage current_yellow = yellow_right_body;

    private BufferedImage current_player = full_circle; // image currently displayed as player
    // true when large_mouth image is displayed, false when small_mouth image is
    // displayed
    private boolean current_large_mouth = false;

    private int state = 0; // stopped = 0, up = 1, down = 2, left = 3, right = 4
    private int attemptedState = 0;
    private int counter = 0;

    // directions of movement available to player:
    private boolean upAvail = false;
    private boolean downAvail = false;
    private boolean leftAvail = true;
    private boolean rightAvail = true;

    // Red Ghost
    private boolean upAvail_red = false;
    private boolean downAvail_red = false;
    private boolean leftAvail_red = true;
    private boolean rightAvail_red = true;
    private int red_state = 0;

    // Pink Ghost
    private boolean upAvail_pink = true;
    private boolean downAvail_pink = false;
    private boolean leftAvail_pink = false;
    private boolean rightAvail_pink = false;
    private int pink_state = 0;

    // Cyan
    private boolean upAvail_cyan = false;
    private boolean downAvail_cyan = false;
    private boolean leftAvail_cyan = true;
    private boolean rightAvail_cyan = true;
    private int cyan_state = 0;

    // yellow
    private boolean upAvail_yellow = false;
    private boolean downAvail_yellow = false;
    private boolean leftAvail_yellow = true;
    private boolean rightAvail_yellow = true;
    private int yellow_state = 0;

    // intersection points: each point in each array is an intersection where the
    // direction in the name of the array is available
    private final int[][] leftPoints = { { 79, 584 }, { 79, 650 }, { 144, 105 }, { 144, 192 }, { 144, 257 },
            { 144, 388 }, { 144, 520 }, { 144, 650 }, { 210, 192 }, { 210, 388 }, { 210, 520 }, { 210, 584 },
            { 275, 105 }, { 275, 192 }, { 275, 257 }, { 275, 323 }, { 275, 454 }, { 275, 520 }, { 275, 584 },
            { 275, 650 }, { 275, 715 }, { 340, 192 }, { 340, 323 }, { 340, 454 }, { 340, 584 }, { 340, 715 },
            { 406, 192 }, { 406, 257 }, { 406, 323 }, { 406, 454 }, { 406, 520 }, { 406, 584 }, { 406, 650 },
            { 471, 105 }, { 471, 192 }, { 471, 388 }, { 471, 520 }, { 471, 584 }, { 536, 650 }, { 580, 105 },
            { 580, 192 }, { 580, 257 }, { 580, 520 }, { 580, 584 }, { 580, 650 }, { 580, 715 } };

    private final int[][] rightPoints = { { 536, 584 }, { 536, 650 }, { 471, 105 }, { 471, 192 }, { 471, 257 },
            { 471, 388 }, { 471, 520 }, { 471, 650 }, { 406, 192 }, { 406, 388 }, { 406, 520 }, { 406, 584 },
            { 340, 105 }, { 340, 192 }, { 340, 257 }, { 340, 323 }, { 340, 454 }, { 340, 520 }, { 340, 584 },
            { 340, 650 }, { 340, 715 }, { 275, 192 }, { 275, 323 }, { 275, 454 }, { 275, 584 }, { 275, 715 },
            { 210, 192 }, { 210, 257 }, { 210, 323 }, { 210, 454 }, { 210, 520 }, { 210, 584 }, { 210, 650 },
            { 144, 105 }, { 144, 192 }, { 144, 388 }, { 144, 520 }, { 144, 584 }, { 79, 650 }, { 35, 105 }, { 35, 192 },
            { 35, 257 }, { 35, 520 }, { 35, 584 }, { 35, 650 }, { 35, 715 } };

    private final int[][] upPoints = { { 35, 192 }, { 144, 192 }, { 275, 192 }, { 340, 192 }, { 471, 192 },
            { 580, 192 }, { 35, 257 }, { 144, 257 }, { 210, 257 }, { 406, 257 }, { 471, 257 }, { 580, 257 },
            { 275, 323 }, { 340, 323 }, { 144, 388 }, { 210, 388 }, { 406, 388 }, { 471, 388 }, { 210, 454 },
            { 406, 454 }, { 144, 520 }, { 210, 520 }, { 406, 520 }, { 471, 520 }, { 35, 584 }, { 144, 584 },
            { 275, 584 }, { 340, 584 }, { 471, 584 }, { 580, 584 }, { 79, 650 }, { 144, 650 }, { 210, 650 },
            { 406, 650 }, { 471, 650 }, { 536, 650 }, { 35, 715 }, { 275, 715 }, { 340, 715 }, { 580, 715 } };

    private final int[][] downPoints = { { 35, 105 }, { 144, 105 }, { 275, 105 }, { 340, 105 }, { 471, 105 },
            { 580, 105 }, { 35, 192 }, { 144, 192 }, { 210, 192 }, { 406, 192 }, { 471, 192 }, { 580, 192 },
            { 144, 257 }, { 275, 257 }, { 340, 257 }, { 471, 257 }, { 210, 323 }, { 406, 323 }, { 144, 388 },
            { 210, 388 }, { 406, 388 }, { 471, 388 }, { 210, 454 }, { 406, 454 }, { 35, 520 }, { 144, 520 },
            { 275, 520 }, { 340, 520 }, { 471, 520 }, { 580, 520 }, { 79, 584 }, { 144, 584 }, { 210, 584 },
            { 406, 584 }, { 471, 584 }, { 536, 584 }, { 35, 650 }, { 275, 650 }, { 340, 650 }, { 580, 650 } };

    private final int[][] dots = { { 35, 105 }, { 57, 105 }, { 79, 105 }, { 101, 105 }, { 123, 105 }, { 144, 105 },
            { 166, 105 }, { 188, 105 }, { 210, 105 }, { 232, 105 }, { 254, 105 }, { 275, 105 }, { 340, 105 },
            { 362, 105 }, { 384, 105 }, { 406, 105 }, { 427, 105 }, { 449, 105 }, { 471, 105 }, { 492, 105 },
            { 514, 105 }, { 536, 105 }, { 558, 105 }, { 580, 105 }, { 35, 127 }, { 35, 149 }, { 35, 170 }, { 144, 127 },
            { 144, 149 }, { 144, 170 }, { 275, 127 }, { 275, 149 }, { 275, 170 }, { 340, 127 }, { 340, 149 },
            { 340, 170 }, { 471, 127 }, { 471, 149 }, { 471, 170 }, { 580, 127 }, { 580, 149 }, { 580, 170 },
            { 35, 192 }, { 57, 192 }, { 79, 192 }, { 101, 192 }, { 123, 192 }, { 144, 192 }, { 166, 192 }, { 188, 192 },
            { 210, 192 }, { 232, 192 }, { 254, 192 }, { 275, 192 }, { 297, 192 }, { 319, 192 }, { 340, 192 },
            { 362, 192 }, { 384, 192 }, { 406, 192 }, { 427, 192 }, { 449, 192 }, { 471, 192 }, { 492, 192 },
            { 514, 192 }, { 536, 192 }, { 558, 192 }, { 580, 192 }, { 35, 214 }, { 35, 235 }, { 144, 214 },
            { 144, 235 }, { 210, 214 }, { 210, 235 }, { 406, 214 }, { 406, 235 }, { 471, 214 }, { 471, 235 },
            { 580, 214 }, { 580, 235 }, { 35, 257 }, { 57, 257 }, { 79, 257 }, { 101, 257 }, { 123, 257 }, { 144, 257 },
            { 210, 257 }, { 232, 257 }, { 254, 257 }, { 275, 257 }, { 340, 257 }, { 362, 257 }, { 384, 257 },
            { 406, 257 }, { 471, 257 }, { 492, 257 }, { 514, 257 }, { 536, 257 }, { 558, 257 }, { 580, 257 },
            { 144, 279 }, { 144, 301 }, { 471, 279 }, { 471, 301 }, { 144, 323 }, { 471, 323 }, { 144, 345 },
            { 144, 366 }, { 471, 345 }, { 471, 366 }, { 144, 388 }, { 471, 388 }, { 144, 410 }, { 144, 432 },
            { 471, 410 }, { 471, 432 }, { 144, 454 }, { 471, 454 }, { 144, 476 }, { 144, 498 }, { 471, 476 },
            { 471, 498 }, { 35, 520 }, { 57, 520 }, { 79, 520 }, { 101, 520 }, { 123, 520 }, { 144, 520 }, { 166, 520 },
            { 188, 520 }, { 210, 520 }, { 232, 520 }, { 254, 520 }, { 275, 520 }, { 340, 520 }, { 362, 520 },
            { 384, 520 }, { 406, 520 }, { 427, 520 }, { 449, 520 }, { 471, 520 }, { 492, 520 }, { 514, 520 },
            { 536, 520 }, { 558, 520 }, { 580, 520 }, { 35, 541 }, { 35, 563 }, { 144, 541 }, { 144, 563 },
            { 275, 541 }, { 275, 563 }, { 340, 541 }, { 340, 563 }, { 471, 541 }, { 471, 563 }, { 580, 541 },
            { 580, 563 }, { 35, 584 }, { 57, 584 }, { 79, 584 }, { 144, 584 }, { 166, 584 }, { 188, 584 }, { 210, 584 },
            { 232, 584 }, { 254, 584 }, { 275, 584 }, { 340, 584 }, { 362, 584 }, { 384, 584 }, { 406, 584 },
            { 427, 584 }, { 449, 584 }, { 471, 584 }, { 536, 584 }, { 558, 584 }, { 580, 584 }, { 79, 606 },
            { 79, 628 }, { 144, 606 }, { 144, 628 }, { 210, 606 }, { 210, 628 }, { 406, 606 }, { 406, 628 },
            { 471, 606 }, { 471, 628 }, { 536, 606 }, { 536, 628 }, { 35, 650 }, { 57, 650 }, { 79, 650 }, { 101, 650 },
            { 123, 650 }, { 144, 650 }, { 210, 650 }, { 232, 650 }, { 254, 650 }, { 275, 650 }, { 340, 650 },
            { 362, 650 }, { 384, 650 }, { 406, 650 }, { 471, 650 }, { 492, 650 }, { 514, 650 }, { 536, 650 },
            { 558, 650 }, { 580, 650 }, { 35, 672 }, { 35, 693 }, { 275, 672 }, { 275, 693 }, { 340, 672 },
            { 340, 693 }, { 580, 672 }, { 580, 693 }, { 35, 715 }, { 57, 715 }, { 79, 715 }, { 101, 715 }, { 123, 715 },
            { 144, 715 }, { 166, 715 }, { 188, 715 }, { 210, 715 }, { 232, 715 }, { 254, 715 }, { 275, 715 },
            { 297, 715 }, { 319, 715 }, { 340, 715 }, { 362, 715 }, { 384, 715 }, { 406, 715 }, { 427, 715 },
            { 449, 715 }, { 471, 715 }, { 492, 715 }, { 514, 715 }, { 536, 715 }, { 558, 715 }, { 580, 715 }, };

    private ArrayList<int[]> hiddenDots = new ArrayList<int[]>();

    // stores current coordinates from the last intersection point (starts with {0,
    // 0})
    private int[] currentCoords = { 0, 0 };

    private int[] red_currentCoords = { 0, 0 };
    private int[] pink_currentCoords = { 0, 0 };
    private int[] cyan_currentCoords = { 0, 0 };
    private int[] yellow_currentCoords = { 0, 0 };

    private int score = 0;

    private int cherryCount = 0;
    private boolean showCherry = false;

    private final Font font = assets.pacmanFont;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // <insert shrug emoji>
        setBackground(CANVAS_BACKGROUND); // black background
        g.drawImage(backgroundImage, 0, 0, this); // draw the background image

        counter++;
        if (counter > COUNTER_MAX) {
            counter = 0;
            attemptedState = 0;
        }

        // System.out.println(player_position);

        // if game has not started, state == 0. If game has started, state is somewhere
        // on [1, 4], and availability booleans must be reset before determining their
        // values
        if (state > 0) {
            upAvail = false;
            downAvail = false;
            leftAvail = false;
            rightAvail = false;
        }

        if (red_state > 0) {
            upAvail_red = false;
            downAvail_red = false;
            leftAvail_red = false;
            rightAvail_red = false;
        }
        
        if (pink_state > 0) {
            upAvail_pink = false;
            downAvail_pink = false;
            leftAvail_pink = false;
            rightAvail_pink = false;
        }
        
        if (cyan_state > 0) {
            upAvail_cyan = false;
            downAvail_cyan = false;
            leftAvail_cyan = false;
            rightAvail_cyan = false;
        }

        if (yellow_state > 0) {
            upAvail_yellow = false;
            downAvail_yellow = false;
            leftAvail_yellow = false;
            rightAvail_yellow = false;
        }


        // iterate through the intersections. If the player is at an intersection in one
        // of the arrays, it is known that movement in certain directions is available
        // to the player

        // used when displaying colored dots at intersections to indicate available
        // directions (for troubleshooting purposes)
        // g.setColor(Color.green);
        for (int[] coords : leftPoints) { // iterates through all intersections where left is an option
            // g.fillOval(coords[0] - 10, coords[1] - 5, 10, 10); // draws the colored dot

            // checks if the player is within THRESHOLD pixels of the intersection
            if (inRange(coords, player_position)) {
                // if the player is at one of the intersections in leftPoints, left must be a
                // viable option
                leftAvail = true;
                currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                currentCoords[1] = coords[1];
                // after finding the correct intersection point, there's no need to iterate
                // through more; we already know the rest are all going to be invalid
                // break;
            }

            if (inRange(coords, red_position)) {
                leftAvail_red = true;
                red_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                red_currentCoords[1] = coords[1];
            }
            if (inRange(coords, pink_position)) {
                leftAvail_pink = true;
                pink_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                pink_currentCoords[1] = coords[1];
            }
            if (inRange(coords, cyan_position)) {
                leftAvail_cyan = true;
                cyan_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                cyan_currentCoords[1] = coords[1];
            }
            if (inRange(coords, yellow_position)) {
                leftAvail_yellow = true;
                yellow_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                yellow_currentCoords[1] = coords[1];
            }
        }

        // g.setColor(Color.orange);
        for (int[] coords : upPoints) {
            // g.fillOval(coords[0] - 5, coords[1] - 10, 10, 10);

            if (inRange(coords, player_position)) {
                upAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                // break;
            }
            if (inRange(coords, red_position)) {
                upAvail_red = true;
                red_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                red_currentCoords[1] = coords[1];
            }
            if (inRange(coords, pink_position)) {
                upAvail_pink = true;
                pink_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                pink_currentCoords[1] = coords[1];
            }
            if (inRange(coords, cyan_position)) {
                upAvail_cyan = true;
                cyan_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                cyan_currentCoords[1] = coords[1];
            }
            if (inRange(coords, yellow_position)) {
                upAvail_yellow = true;
                yellow_currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
                yellow_currentCoords[1] = coords[1];
            }
        }

        // g.setColor(Color.cyan);
        for (int[] coords : rightPoints) {
            // g.fillOval(coords[0], coords[1] - 5, 10, 10);

            if (inRange(coords, player_position)) {
                rightAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                // break;
            }
            if (inRange(coords, red_position)) {
                rightAvail_red = true;
                red_currentCoords[0] = coords[0];
                red_currentCoords[1] = coords[1];
            }
            if (inRange(coords, pink_position)) {
                rightAvail_pink = true;
                pink_currentCoords[0] = coords[0];
                pink_currentCoords[1] = coords[1];
            }
            if (inRange(coords, cyan_position)) {
                rightAvail_cyan = true;
                cyan_currentCoords[0] = coords[0];
                cyan_currentCoords[1] = coords[1];
            }
            if (inRange(coords, yellow_position)) {
                rightAvail_yellow = true;
                yellow_currentCoords[0] = coords[0];
                yellow_currentCoords[1] = coords[1];
            }

        }

        // g.setColor(Color.magenta);
        for (int[] coords : downPoints) {
            // g.fillOval(coords[0] - 5, coords[1], 10, 10);

            if (inRange(coords, player_position)) {
                downAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                // break;
            }
            if (inRange(coords, red_position)) {
                downAvail_red = true;
                red_currentCoords[0] = coords[0];
                red_currentCoords[1] = coords[1];
            }
            if (inRange(coords, pink_position)) {
                downAvail_pink = true;
                pink_currentCoords[0] = coords[0];
                pink_currentCoords[1] = coords[1];
            }
            if (inRange(coords, cyan_position)) {
                downAvail_cyan = true;
                cyan_currentCoords[0] = coords[0];
                red_currentCoords[1] = coords[1];
            }
            if (inRange(coords, yellow_position)) {
                downAvail_yellow = true;
                yellow_currentCoords[0] = coords[0];
                yellow_currentCoords[1] = coords[1];
            }
        }

        g.setColor(Color.white);
        for (int[] coords : dots) {
            if (hiddenDots.indexOf(coords) < 0) {
                g.fillOval(coords[0] - 3, coords[1] - 3, 6, 6);

                if (inRange(coords, player_position)) {
                    score++;
                    hiddenDots.add(coords);
                }
            }

        }

        if (showCherry) {
            g.drawImage(cherries, (int) cherry_position.getX() - PLAYER_DIMENSION / 2,
                    (int) cherry_position.getY() - PLAYER_DIMENSION / 2, this);

            int[] coords = { (int) cherry_position.getX(), (int) cherry_position.getY() };

            if (inRange(coords, player_position)) {
                score += 20;
                cherryCount++;
                showCherry = false;
            }
        } else if (state > 0) {
            showCherry = 0 == (int) (Math.random() * CHERRY_CHANCE);
        }

        // if the player is not at any intersection
        if (!leftAvail && !rightAvail && !upAvail && !downAvail) {
            if (state == 1 || state == 2) { // if the player is moving up or down
                upAvail = true; // up and down are still available
                downAvail = true;
            } else if (state == 3 || state == 4) { // if the player is moving left or right
                leftAvail = true; // left and right are still available
                rightAvail = true;
            }
        }

        if (!leftAvail_red && !rightAvail_red && !upAvail_red && !downAvail_red) {
            if (red_state == 1 || red_state == 2) {
                upAvail_red = true;
                downAvail_red = true;
            } else if (red_state == 3 || red_state == 4) {
                leftAvail_red = true;
                rightAvail_red = true;
            }
        }

        if (!leftAvail_pink && !rightAvail_pink && !upAvail_pink && !downAvail_pink) {
            if (pink_state == 1 || pink_state == 2) {
                upAvail_pink = true;
                downAvail_pink = true;
            } else if (pink_state == 3 || pink_state == 4) {
                leftAvail_pink = true;
                rightAvail_pink = true;
            }
        }

        if (!leftAvail_cyan && !rightAvail_cyan && !upAvail_cyan && !downAvail_cyan) {
            if (cyan_state == 1 || cyan_state == 2) {
                upAvail_cyan = true;
                downAvail_cyan = true;
            } else if (cyan_state == 3 || cyan_state == 4) {
                leftAvail_cyan = true;
                rightAvail_cyan = true;
            }
        }

        if (!leftAvail_yellow && !rightAvail_yellow && !upAvail_yellow && !downAvail_yellow) {
            if (yellow_state == 1 || yellow_state == 2) {
                upAvail_yellow = true;
                downAvail_yellow = true;
            } else if (yellow_state == 3 || yellow_state == 4) {
                leftAvail_yellow = true;
                rightAvail_yellow = true;
            }
        }

        // determines which direction the RED ghost should go

        if (leftAvail_red == true /* || player_position.getX() > red_position.getX() */) {
            red_state = 3;
            current_red = red_left_body;
            red_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_red == true /* || player_position.getX() < red_position.getX() */) {
            red_state = 4;
            current_red = red_right_body;
            red_position.translate(SHIFT, 0);// right
        } else if (upAvail_red == true /* || player_position.getY() < red_position.getY() */) {
            red_state = 1;
            current_red = red_up_body;
            red_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_red == true /* || player_position.getY() > red_position.getY() */) {
            red_state = 2;
            current_red = red_down_body;
            red_position.translate(0, SHIFT); // down
        }

        System.out.println(red_position);
        System.out.println(upAvail_red + " " + downAvail_red + " " + leftAvail_red + " " + rightAvail_red);
        System.out.println(red_state);
        System.out.println(red_currentCoords[0] + red_currentCoords[1]);
        // PINK Ghost
        if (leftAvail_pink == true /* || player_position.getX() > pink_position.getX() */) {
            pink_state = 3;
            current_pink = pink_left_body;
            pink_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_pink == true /* || player_position.getX() < pink_position.getX() */) {
            pink_state = 4;
            current_pink = pink_right_body;
            pink_position.translate(SHIFT, 0);// right
        } else if (upAvail_pink == true /* || player_position.getY() < pink_position.getY() */) {
            pink_state = 1;
            current_pink = pink_up_body;
            pink_position.translate(0, -1 * SHIFT); // shift up

        } else if (downAvail_pink == true /* || player_position.getY() > pink_position.getY() */) {
            pink_state = 2;
            current_pink = pink_down_body;
            pink_position.translate(0, SHIFT); // down
        }

        // CYAN Ghost

        if (leftAvail_cyan == true /* || player_position.getX() > cyan_position.getX() */) {
            cyan_state = 3;
            current_cyan = cyan_left_body;
            cyan_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_cyan == true /* || player_position.getX() < cyan_position.getX() */) {
            cyan_state = 4;
            current_cyan = cyan_right_body;
            cyan_position.translate(SHIFT, 0);// right
        } else if (upAvail_cyan == true /* || player_position.getY() < cyan_position.getY() */) {
            cyan_state = 1;
            current_cyan = cyan_up_body;
            cyan_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_cyan == true /* || player_position.getY() > cyan_position.getY() */) {
            cyan_state = 2;
            current_cyan = cyan_down_body;
            cyan_position.translate(0, SHIFT); // down
        }

        // YELLOW Ghost

        if (leftAvail_yellow == true /* || player_position.getX() > yellow_position.getX() */) {
            yellow_state = 3;
            current_yellow = yellow_left_body;
            yellow_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_yellow == true /* || player_position.getX() < yellow_position.getX() */) {
            yellow_state = 4;
            current_yellow = yellow_right_body;
            yellow_position.translate(SHIFT, 0);// right
        } else if (upAvail_yellow == true /* || player_position.getY() < yellow_position.getY() */) {
            yellow_state = 1;
            current_yellow = yellow_up_body;
            yellow_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_yellow == true /* || player_position.getY() > yellow_position.getY() */) {
            yellow_state = 2;
            current_yellow = yellow_down_body;
            yellow_position.translate(0, SHIFT); // down
        }

        // System.out.println(upAvail + " " + downAvail + " " + leftAvail + " " +
        // rightAvail);

        if (attemptedState == 1 && upAvail) { // if user wants to go up and up is available
            state = attemptedState;
        } else if (attemptedState == 2 && downAvail) { // same for down
            state = attemptedState;
        } else if (attemptedState == 3 && leftAvail) { // same for left
            state = attemptedState;
        } else if (attemptedState == 4 && rightAvail) { // same for right
            state = attemptedState;
        }

        if (current_large_mouth) { // if the player image is currently a large mouth
            switch (state) { // find the direction the player is currently moving in
                case 1:
                    current_player = small_mouth_up; // give the player the corresponding small mouth direction
                    break;
                case 2:
                    current_player = small_mouth_down;
                    break;
                case 3:
                    current_player = small_mouth_left;
                    break;
                case 4:
                    current_player = small_mouth_right;
                    break;
                default:
                    // if for whatever reason the state variable has some other value, give the
                    // player the full circle image
                    current_player = full_circle;
                    break;
            }

            current_large_mouth = false;
        } else { // if the player image is currently a small mouth or full circle
            // pretty much the same thing as the previous switch statement except now it's
            // small mouth
            switch (state) {
                case 1:
                    current_player = large_mouth_up;
                    break;
                case 2:
                    current_player = large_mouth_down;
                    break;
                case 3:
                    current_player = large_mouth_left;
                    break;
                case 4:
                    current_player = large_mouth_right;
                    break;
                default:
                    current_player = full_circle;
                    break;
            }

            current_large_mouth = true;
        }

        if (state == 1 && upAvail) { // if player will be going up and up is available
            player_position.translate(0, -1 * SHIFT); // shift up
        } else if (state == 2 && downAvail) { // same for down
            player_position.translate(0, SHIFT);
        } else if (state == 3 && leftAvail) { // same for left
            player_position.translate(-1 * SHIFT, 0);
        } else if (state == 4 && rightAvail) { // same for right
            player_position.translate(SHIFT, 0);
        }

        // System.out.println(attemptedState + " " + state + "\n");

        // currentCoords either contains {0, 0} or the coordinates of an intersection.
        // {0, 0} indicates that the player has not yet been to an intersection, meaning
        // they haven't had a chance to get off of the "tracks", so this is unnecessary
        // anyway in that case. However, when currentCoords does contain the coordinates
        // of an intersection, and the player is moving on a specific axis, they should
        // not be offset from the track on the other axis. This if statement is true
        // when the player is offset on the axis they are not moving on
        if (currentCoords[0] != 0 && (((state == 1 || state == 2) && player_position.getX() != currentCoords[0])
                || ((state == 3 || state == 4) && player_position.getY() != currentCoords[1]))) {
            player_position.setLocation(currentCoords[0], currentCoords[1]); // move the player to the coordinates of
                                                                             // the last intersection the player was at
            // System.out.println("set to: " + player_position);
        }

        // if the player has passed the right side of the board (it moved through the
        // tunnel thing)
        if (player_position.getX() > GAME_SIZE.getWidth()) {
            // move the player to the very left edge of the screen (it warped around the
            // board)
            player_position.setLocation(0, player_position.getY());
        } else if (player_position.getX() < 0) { // if the player has passed the left side of the board
            player_position.setLocation(GAME_SIZE.getWidth(), player_position.getY()); // move them to the right
        }

        if (red_currentCoords[0] != 0 && (((red_state == 1 || red_state == 2) && red_position.getX() != red_currentCoords[0]) || ((state == 3 || state == 4) && red_position.getY() != red_currentCoords[1]))) {
            red_position.setLocation(red_currentCoords[0], red_currentCoords[1]);
        }

        if (pink_currentCoords[0] != 0 && (((pink_state == 1 || pink_state == 2) && pink_position.getX() != pink_currentCoords[0]) || ((state == 3 || state == 4) && pink_position.getY() != pink_currentCoords[1]))) {
            pink_position.setLocation(pink_currentCoords[0], pink_currentCoords[1]);
        }

        if (cyan_currentCoords[0] != 0 && (((cyan_state == 1 || cyan_state == 2) && cyan_position.getX() != cyan_currentCoords[0]) || ((state == 3 || state == 4) && cyan_position.getY() != cyan_currentCoords[1]))) {
            cyan_position.setLocation(cyan_currentCoords[0], cyan_currentCoords[1]);
        }

        if (yellow_currentCoords[0] != 0 && (((yellow_state == 1 || yellow_state == 2) && yellow_position.getX() != yellow_currentCoords[0]) || ((state == 3 || state == 4) && yellow_position.getY() != yellow_currentCoords[1]))) {
            yellow_position.setLocation(yellow_currentCoords[0], yellow_currentCoords[1]);
        }

        if (red_position.getX() > GAME_SIZE.getWidth()) {
            red_position.setLocation(0, red_position.getY());
        } else if (red_position.getX() < 0) {
            red_position.setLocation(GAME_SIZE.getWidth(), red_position.getY());
        }

        if (pink_position.getX() > GAME_SIZE.getWidth()) {
            pink_position.setLocation(0, pink_position.getY());
        } else if (pink_position.getX() < 0) {
            pink_position.setLocation(GAME_SIZE.getWidth(), pink_position.getY());
        }

        if (cyan_position.getX() > GAME_SIZE.getWidth()) {
            cyan_position.setLocation(0, cyan_position.getY());
        } else if (cyan_position.getX() < 0) {
            cyan_position.setLocation(GAME_SIZE.getWidth(), cyan_position.getY());
        }

        if (yellow_position.getX() > GAME_SIZE.getWidth()) {
            yellow_position.setLocation(0, yellow_position.getY());
        } else if (yellow_position.getX() < 0) {
            yellow_position.setLocation(GAME_SIZE.getWidth(), yellow_position.getY());
        }

        g.drawImage(current_player, (int) player_position.getX() - PLAYER_DIMENSION / 2,
                (int) player_position.getY() - PLAYER_DIMENSION / 2, this); // draw the player

        g.drawImage(current_red, (int) red_position.getX() - PLAYER_DIMENSION / 2,
                (int) red_position.getY() - PLAYER_DIMENSION / 2, this); // draw the red ghost

        g.drawImage(current_pink, (int) pink_position.getX() - PLAYER_DIMENSION / 2,
                (int) pink_position.getY() - PLAYER_DIMENSION / 2, this);

        g.drawImage(current_cyan, (int) cyan_position.getX() - PLAYER_DIMENSION / 2,
                (int) cyan_position.getY() - PLAYER_DIMENSION / 2, this);

        g.drawImage(current_yellow, (int) yellow_position.getX() - PLAYER_DIMENSION / 2,
                (int) yellow_position.getY() - PLAYER_DIMENSION / 2, this);
        try {
            Thread.sleep(DELAY); // wait 100 milliseconds (the code is just too fast)
        } catch (InterruptedException e) { // Thread.sleep throws an InterruptedException so this is necessary
            e.printStackTrace();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(font);
        FontMetrics fontMetrics = g2.getFontMetrics();
        String s = "" + score * 10;
        String c = "" + cherryCount;
        g2.drawString(c, (int) GAME_SIZE.getWidth() - fontMetrics.stringWidth(c) - 10,
                (int) GAME_SIZE.getHeight() - 35);
        g2.drawImage(GraphicsOptions.resize(assets.image_cherries, 25, 25),
                (int) GAME_SIZE.getWidth() - fontMetrics.stringWidth(c) - 43, (int) GAME_SIZE.getHeight() - 56, this);
        g2.drawString(s, 100 - fontMetrics.stringWidth(s), 50);

        // call the entire paintComponent function again (causes an infinite loop of
        // gameplay)
        repaint();
    }

    /**
     * Check if the given player position is close to the intersection coordinates
     * 
     * @param coords   intersection coordinates being checked
     * @param position player's position object
     * @return whether the player is within THRESHOLD pixels of the intersection
     *         coordinates
     */
    public boolean inRange(int[] coords, Point position) {
        return (position.getX() > coords[0] - THRESHOLD) && (position.getX() < coords[0] + THRESHOLD)
                && (position.getY() > coords[1] - THRESHOLD) && (position.getY() < coords[1] + THRESHOLD);
    }

    /**
     * Called when user tries to move up, if this is available, player is given "up"
     * state
     */
    public void shiftPlayerUp() {
        attemptedState = 1;
    }

    /**
     * Called when user tries to move down, if this is available, player is given
     * "down" state
     */
    public void shiftPlayerDown() {
        attemptedState = 2;
    }

    /**
     * Called when user tries to move left, if this is available, player is given
     * "left" state
     */
    public void shiftPlayerLeft() {
        attemptedState = 3;
    }

    /**
     * Called when user tries to move right, if this is available, player is given
     * "right" state
     */
    public void shiftPlayerRight() {
        attemptedState = 4;
    }
}