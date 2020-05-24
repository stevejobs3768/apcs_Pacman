package pacman;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

public class DrawCanvas extends JPanel {
    // I don't really understand what this is, but its absence causes a warning
    private static final long serialVersionUID = 1L;
    
    public static final Dimension GAME_SIZE = new Dimension(615, 785); // size of playable game window
    // size of playable game window + height of title bar
    public static final Dimension CANVAS_SIZE = new Dimension(615, 785 + 22);
    public static final Color CANVAS_BACKGROUND = Color.black; // color of game background

    public static final int PLAYER_DIMENSION = 32; // width/height of player

    private final Point player_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) (GAME_SIZE.getHeight() * 1.49 / 2)); // default starting position of player

    private final Assets assets = GraphicsOptions.assets; // class containing all images
    private final BufferedImage backgroundImage = GraphicsOptions.resize(assets.image_background,
            (int) GAME_SIZE.getWidth(), (int) GAME_SIZE.getHeight()); // drawing of pacman walls, borders, etc

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

    private final int SHIFT = 5; // each time the code runs, # of pixels the player moves
    private final int THRESHOLD = 4; // how close to an intersection do you have to be to be "at" that intersection

    private BufferedImage current_player = full_circle; // image currently displayed as player
    // true when large_mouth image is displayed, false when small_mouth image is
    // displayed
    private boolean current_large_mouth = false;

    private int state = 0; // stopped = 0, up = 1, down = 2, left = 3, right = 4

    // directions of movement available to player:
    private boolean upAvail = false;
    private boolean downAvail = false;
    private boolean leftAvail = true;
    private boolean rightAvail = true;

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

    // stores current coordinates from the last intersection point (starts with {0,
    // 0})
    private int[] currentCoords = { 0, 0 };

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // <insert shrug emoji>
        setBackground(CANVAS_BACKGROUND); // black background
        g.drawImage(backgroundImage, 0, 0, this); // draw the background image

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
                break;
            }
        }

        // g.setColor(Color.orange);
        for (int[] coords : upPoints) {
            // g.fillOval(coords[0] - 5, coords[1] - 10, 10, 10);

            if (inRange(coords, player_position)) {
                upAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                break;
            }
        }

        // g.setColor(Color.cyan);
        for (int[] coords : rightPoints) {
            // g.fillOval(coords[0], coords[1] - 5, 10, 10);

            if (inRange(coords, player_position)) {
                rightAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                break;
            }
        }

        // g.setColor(Color.magenta);
        for (int[] coords : downPoints) {
            // g.fillOval(coords[0] - 5, coords[1], 10, 10);

            if (inRange(coords, player_position)) {
                downAvail = true;
                currentCoords[0] = coords[0];
                currentCoords[1] = coords[1];
                break;
            }
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

        // System.out.println(upAvail + " " + downAvail + " " + leftAvail + " " +
        // rightAvail);

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

        if (state == 1 && upAvail) { // if user wants to go up and up is available
            player_position.translate(0, -1 * SHIFT); // shift up
        } else if (state == 2 && downAvail) { // same for down
            player_position.translate(0, SHIFT);
        } else if (state == 3 && leftAvail) { // same for left
            player_position.translate(-1 * SHIFT, 0);
        } else if (state == 4 && rightAvail) { // same for right
            player_position.translate(SHIFT, 0);
        }

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

        g.drawImage(current_player, (int) player_position.getX() - PLAYER_DIMENSION / 2,
                (int) player_position.getY() - PLAYER_DIMENSION / 2, this); // draw the player

        try {
            Thread.sleep(100); // wait 100 milliseconds (the code is just too fast)
        } catch (InterruptedException e) { // Thread.sleep throws an InterruptedException so this is necessary
            e.printStackTrace();
        }

        // call the entire paintComponent function again (causes an infinite loop of
        // gameplay)
        repaint();
    }

    /**
     * Check if the given player position is close to the intersection coordinates
     * 
     * @param coords intersection coordinates being checked
     * @param position player's position object
     * @return whether the player is within THRESHOLD pixels of the intersection coordinates
     */
    public boolean inRange(int[] coords, Point position) {
        return (position.getX() > coords[0] - THRESHOLD) && (position.getX() < coords[0] + THRESHOLD) && (position.getY() > coords[1] - THRESHOLD)
                && (position.getY() < coords[1] + THRESHOLD);
    }

    /**
     * Called when user tries to move up, if this is available, player is given "up" state
     */
    public void shiftPlayerUp() {
        if (upAvail) {
            state = 1;
        }
    }

    /**
     * Called when user tries to move down, if this is available, player is given "down" state
     */
    public void shiftPlayerDown() {
        if (downAvail) {
            state = 2;
        }
    }

    /**
     * Called when user tries to move left, if this is available, player is given "left" state
     */
    public void shiftPlayerLeft() {
        if (leftAvail) {
            state = 3;
        }
    }

    /**
     * Called when user tries to move right, if this is available, player is given "right" state
     */
    public void shiftPlayerRight() {
        if (rightAvail) {
            state = 4;
        }
    }

}