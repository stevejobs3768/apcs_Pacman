package pacman;

import java.awt.*;
import java.awt.image.*;

public class Player {
    private final Point initial_position = new Point((int) (DrawCanvas.GAME_SIZE.getWidth() / 2),
            (int) ((DrawCanvas.GAME_SIZE.getHeight() - 30) * 1.49 / 2)); // default starting position of player
    public Point position = new Point((int) (DrawCanvas.GAME_SIZE.getWidth() / 2),
            (int) ((DrawCanvas.GAME_SIZE.getHeight() - 30) * 1.49 / 2)); // position of player
    private final Assets assets = GraphicsOptions.assets;
    private final int dimension = 32;
    private final int shift = 5; // each time the code runs, # of pixels the player moves

    // various states of the player image
    private final BufferedImage full_circle = GraphicsOptions.resize(assets.image_player_full_circle, dimension,
            dimension);
    private final BufferedImage small_mouth_left = GraphicsOptions.resize(assets.image_player_small_mouth_left,
            dimension, dimension);
    private final BufferedImage large_mouth_left = GraphicsOptions.resize(assets.image_player_large_mouth_left,
            dimension, dimension);
    private final BufferedImage small_mouth_right = GraphicsOptions.resize(assets.image_player_small_mouth_right,
            dimension, dimension);
    private final BufferedImage large_mouth_right = GraphicsOptions.resize(assets.image_player_large_mouth_right,
            dimension, dimension);
    private final BufferedImage small_mouth_up = GraphicsOptions.resize(assets.image_player_small_mouth_up, dimension,
            dimension);
    private final BufferedImage large_mouth_up = GraphicsOptions.resize(assets.image_player_large_mouth_up, dimension,
            dimension);
    private final BufferedImage small_mouth_down = GraphicsOptions.resize(assets.image_player_small_mouth_down,
            dimension, dimension);
    private final BufferedImage large_mouth_down = GraphicsOptions.resize(assets.image_player_large_mouth_down,
            dimension, dimension);

    private BufferedImage current_player = full_circle; // image currently displayed as player
    // true when large_mouth image is displayed, false when small_mouth image is
    // displayed
    private boolean current_large_mouth = false;

    public int state = 0; // stopped = 0, up = 1, down = 2, left = 3, right = 4

    // directions of movement available to player:
    private boolean upAvail = false;
    private boolean downAvail = false;
    private boolean leftAvail = true;
    private boolean rightAvail = true;

    // stores current coordinates from the last intersection point (starts with {0,
    // 0})
    private int[] currentCoords = { 0, 0 };

    public int lives = 3;

    public int[] getCoords() {
        int[] coords = { (int) position.getX(), (int) position.getY() };
        return coords;
    }

    public void restart() {
        position.setLocation(initial_position);
        currentCoords = new int[2];
        lives--;
        reset();
        state = 0;
    }

    public void reset() {
        // if game has not started, state == 0. If game has started, state is somewhere
        // on [1, 4], and availability booleans must be reset before determining their
        // values
        if (state > 0) {
            upAvail = false;
            downAvail = false;
            leftAvail = false;
            rightAvail = false;
        } else {
            upAvail = false;
            downAvail = false;
            leftAvail = true;
            rightAvail = true;
        }
    }

    public boolean intersect(int[] coords, int direction) {
        // checks if the player is within THRESHOLD pixels of the intersection
        if (DrawCanvas.inRange(coords, position)) {
            // if the player is at one of the intersections in leftPoints, left must be a
            // viable option
            switch (direction) {
                case 1:
                    upAvail = true;
                    break;
                case 2:
                    downAvail = true;
                    break;
                case 3:
                    leftAvail = true;
                    break;
                case 4:
                    rightAvail = true;
                    break;
                case 5:
                    return true;
            }

            currentCoords[0] = coords[0]; // save the coordinates of the intersection in currentCoords
            currentCoords[1] = coords[1];
        }
        return false;
    }

    public void confirmPosition(int attemptedState) {
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

        if (attemptedState == 1 && upAvail) { // if user wants to go up and up is available
            state = attemptedState;
        } else if (attemptedState == 2 && downAvail) { // same for down
            state = attemptedState;
        } else if (attemptedState == 3 && leftAvail) { // same for left
            state = attemptedState;
        } else if (attemptedState == 4 && rightAvail) { // same for right
            state = attemptedState;
        }

        if (state == 1 && upAvail) { // if player will be going up and up is available
            position.translate(0, -1 * shift); // shift up
        } else if (state == 2 && downAvail) { // same for down
            position.translate(0, shift);
        } else if (state == 3 && leftAvail) { // same for left
            position.translate(-1 * shift, 0);
        } else if (state == 4 && rightAvail) { // same for right
            position.translate(shift, 0);
        }

        /*
         * currentCoords either contains {0, 0} or the coordinates of an intersection.
         * {0, 0} indicates that the player has not yet been to an intersection, meaning
         * they haven't had a chance to get off of the "tracks", so this is unnecessary
         * anyway in that case. However, when currentCoords does contain the coordinates
         * of an intersection, and the player is moving on a specific axis, they should
         * not be offset from the track on the other axis. This if statement is true
         * when the player is offset on the axis they are not moving on
         */
        if (currentCoords[0] != 0 && (((state == 1 || state == 2) && position.getX() != currentCoords[0])
                || ((state == 3 || state == 4) && position.getY() != currentCoords[1]))) {
            position.setLocation(currentCoords[0], currentCoords[1]); // move the player to the coordinates of
                                                                      // the last intersection the player was at
        }

        // if the player has passed the right side of the board (it moved through the
        // tunnel thing)
        if (position.getX() > DrawCanvas.GAME_SIZE.getWidth()) {
            // move the player to the very left edge of the screen (it warped around the
            // board)
            position.setLocation(0, position.getY());
        } else if (position.getX() < 0) { // if the player has passed the left side of the board
            position.setLocation(DrawCanvas.GAME_SIZE.getWidth(), position.getY()); // move them to the right
        }
    }

    public void draw(Graphics g, DrawCanvas canvas) {
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

        g.drawImage(current_player, (int) position.getX() - dimension / 2, (int) position.getY() - dimension / 2,
                canvas); // draw the player
    }
}