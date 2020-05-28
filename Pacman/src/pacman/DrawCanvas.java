package pacman;

import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.ArrayList;

public class DrawCanvas extends JPanel {
    // I don't really understand what this is, but its absence causes a warning
    private static final long serialVersionUID = 1L;

    public static final Dimension GAME_SIZE = new Dimension(615, 785 + 30); // size of playable game window
    // size of playable game window + height of title bar
    public static final Dimension MAC_CANVAS_SIZE = new Dimension(615, 785 + 30 + 6);
    public static final Dimension WINDOWS_CANVAS_SIZE = new Dimension(615, 785 + 30 + 25);

    public static final int PLAYER_DIMENSION = 32; // width/height of player

    public static final int DELAY = 50; // millisecond delay between iterations of paintComponent

    public static final int COUNTER_MAX = 100;

    private final Board board = new Board();
    private final Assets assets = GraphicsOptions.assets; // class containing all images
    private final Cherry cherry = new Cherry();
    private final Player player = new Player();    

    private final Point red_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.825 / 2)); // default starting position of red ghost

    private final Point pink_position = new Point((int) (GAME_SIZE.getWidth() / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final Point cyan_position = new Point((int) (GAME_SIZE.getWidth() * 0.88 / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final Point yellow_position = new Point((int) (GAME_SIZE.getWidth() * 1.12 / 2),
            (int) ((GAME_SIZE.getHeight() - 30) * 0.985 / 2));

    private final BufferedImage red_left1 = GraphicsOptions.resize(assets.image_red_left_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_left1 = GraphicsOptions.resize(assets.image_pink_left_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_left1 = GraphicsOptions.resize(assets.image_cyan_left_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_left1 = GraphicsOptions.resize(assets.image_yellow_left_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_right1 = GraphicsOptions.resize(assets.image_red_right_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_right1 = GraphicsOptions.resize(assets.image_pink_right_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_right1 = GraphicsOptions.resize(assets.image_cyan_right_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_right1 = GraphicsOptions.resize(assets.image_yellow_right_body1,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final BufferedImage red_up1 = GraphicsOptions.resize(assets.image_red_up_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_up1 = GraphicsOptions.resize(assets.image_pink_up_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_up1 = GraphicsOptions.resize(assets.image_cyan_up_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_up1 = GraphicsOptions.resize(assets.image_yellow_up_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_down1 = GraphicsOptions.resize(assets.image_red_down_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_down1 = GraphicsOptions.resize(assets.image_pink_down_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_down1 = GraphicsOptions.resize(assets.image_cyan_down_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_down1 = GraphicsOptions.resize(assets.image_yellow_down_body1, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_left2 = GraphicsOptions.resize(assets.image_red_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_left2 = GraphicsOptions.resize(assets.image_pink_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_left2 = GraphicsOptions.resize(assets.image_cyan_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_left2 = GraphicsOptions.resize(assets.image_yellow_left_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_right2 = GraphicsOptions.resize(assets.image_red_right_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_right2 = GraphicsOptions.resize(assets.image_pink_right_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_right2 = GraphicsOptions.resize(assets.image_cyan_right_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_right2 = GraphicsOptions.resize(assets.image_yellow_right_body2,
            PLAYER_DIMENSION, PLAYER_DIMENSION);

    private final BufferedImage red_up2 = GraphicsOptions.resize(assets.image_red_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_up2 = GraphicsOptions.resize(assets.image_pink_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_up2 = GraphicsOptions.resize(assets.image_cyan_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_up2 = GraphicsOptions.resize(assets.image_yellow_up_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final BufferedImage red_down2 = GraphicsOptions.resize(assets.image_red_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage pink_down2 = GraphicsOptions.resize(assets.image_pink_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage cyan_down2 = GraphicsOptions.resize(assets.image_cyan_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);
    private final BufferedImage yellow_down2 = GraphicsOptions.resize(assets.image_yellow_down_body2, PLAYER_DIMENSION,
            PLAYER_DIMENSION);

    private final int SHIFT = 5; // each time the code runs, # of pixels the player moves
    private static final int THRESHOLD = 4; // how close to an intersection do you have to be to be "at" that intersection

    private BufferedImage current_red = red_left2;
    private BufferedImage current_pink = pink_up2;
    private BufferedImage current_cyan = cyan_left2;
    private BufferedImage current_yellow = yellow_right2;

    private boolean red1 = false;
    private boolean pink1 = false;
    private boolean cyan1 = false;
    private boolean yellow1 = false;

    private int attemptedState = 0;
    private int counter = 0;

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
    private boolean leftAvail_cyan = false;
    private boolean rightAvail_cyan = true;
    private int cyan_state = 0;

    // yellow
    private boolean upAvail_yellow = false;
    private boolean downAvail_yellow = false;
    private boolean leftAvail_yellow = true;
    private boolean rightAvail_yellow = false;
    private int yellow_state = 0;

    private ArrayList<int[]> hiddenDots = new ArrayList<int[]>();

    private int[] red_currentCoords = { 0, 0 };
    private int[] pink_currentCoords = { 0, 0 };
    private int[] cyan_currentCoords = { 0, 0 };
    private int[] yellow_currentCoords = { 0, 0 };

    private int score = 0;

    private int cherryCount = 0;

    private final Font font = assets.pacmanFont;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // <insert shrug emoji>
        setBackground(Board.CANVAS_BACKGROUND); // black background
        g.drawImage(board.backgroundImage, 0, 0, this); // draw the background image

        counter++;
        if (counter > COUNTER_MAX) {
            counter = 0;
            attemptedState = 0;
        }

        // System.out.println(player_position);

        player.reset();

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
        for (int[] coords : board.leftPoints) { // iterates through all intersections where left is an option
            // g.fillOval(coords[0] - 10, coords[1] - 5, 10, 10); // draws the colored dot

            player.intersect(coords, 3);

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
        for (int[] coords : board.upPoints) {
            // g.fillOval(coords[0] - 5, coords[1] - 10, 10, 10);

            player.intersect(coords, 1);
            
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
        for (int[] coords : board.rightPoints) {
            // g.fillOval(coords[0], coords[1] - 5, 10, 10);

            player.intersect(coords, 4);

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
        for (int[] coords : board.downPoints) {
            // g.fillOval(coords[0] - 5, coords[1], 10, 10);

            player.intersect(coords, 2);

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
        for (int[] coords : board.dots) {
            if (hiddenDots.indexOf(coords) < 0) {
                g.fillOval(coords[0] - 3, coords[1] - 3, 6, 6);

                if (player.intersect(coords, 5)) {
                    score++;
                    hiddenDots.add(coords);
                }
            }
        }

        if (cherry.draw(g, player.state > 0, player.position, this)) {
            score += 20;
            cherryCount++;
        }

        player.confirmPosition(attemptedState);

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
            red_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_red == true /* || player_position.getX() < red_position.getX() */) {
            red_state = 4;
            red_position.translate(SHIFT, 0);// right
        } else if (upAvail_red == true /* || player_position.getY() < red_position.getY() */) {
            red_state = 1;
            red_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_red == true /* || player_position.getY() > red_position.getY() */) {
            red_state = 2;
            red_position.translate(0, SHIFT); // down
        }

        System.out.println(red_position);
        System.out.println(upAvail_red + " " + downAvail_red + " " + leftAvail_red + " " + rightAvail_red);
        System.out.println(red_state);
        System.out.println(red_currentCoords[0] + red_currentCoords[1]);

        // PINK Ghost
        if (leftAvail_pink == true /* || player_position.getX() > pink_position.getX() */) {
            pink_state = 3;
            pink_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_pink == true /* || player_position.getX() < pink_position.getX() */) {
            pink_state = 4;
            pink_position.translate(SHIFT, 0);// right
        } else if (upAvail_pink == true /* || player_position.getY() < pink_position.getY() */) {
            pink_state = 1;
            pink_position.translate(0, -1 * SHIFT); // shift up

        } else if (downAvail_pink == true /* || player_position.getY() > pink_position.getY() */) {
            pink_state = 2;
            pink_position.translate(0, SHIFT); // down
        }

        // CYAN Ghost

        if (leftAvail_cyan == true /* || player_position.getX() > cyan_position.getX() */) {
            cyan_state = 3;
            cyan_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_cyan == true /* || player_position.getX() < cyan_position.getX() */) {
            cyan_state = 4;
            cyan_position.translate(SHIFT, 0);// right
        } else if (upAvail_cyan == true /* || player_position.getY() < cyan_position.getY() */) {
            cyan_state = 1;
            cyan_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_cyan == true /* || player_position.getY() > cyan_position.getY() */) {
            cyan_state = 2;
            cyan_position.translate(0, SHIFT); // down
        }

        // YELLOW Ghost

        if (leftAvail_yellow == true /* || player_position.getX() > yellow_position.getX() */) {
            yellow_state = 3;
            yellow_position.translate(-1 * SHIFT, 0); // left
        } else if (rightAvail_yellow == true /* || player_position.getX() < yellow_position.getX() */) {
            yellow_state = 4;
            yellow_position.translate(SHIFT, 0);// right
        } else if (upAvail_yellow == true /* || player_position.getY() < yellow_position.getY() */) {
            yellow_state = 1;
            yellow_position.translate(0, -1 * SHIFT); // shift up
        } else if (downAvail_yellow == true /* || player_position.getY() > yellow_position.getY() */) {
            yellow_state = 2;
            yellow_position.translate(0, SHIFT); // down
        }

        // System.out.println(upAvail + " " + downAvail + " " + leftAvail + " " +
        // rightAvail);

        player.draw(g, this);

        if (red1) {
            switch (red_state) {
                case 1:
                    current_red = red_up2;
                    break;
                case 2:
                    current_red = red_down2;
                    break;
                case 3:
                    current_red = red_left2;
                    break;
                case 4:
                    current_red = red_right2;
                    break;
                default:
                    current_red = red_left2;
                    break;
            }

            red1 = false;
        } else {
            switch (red_state) {
                case 1:
                    current_red = red_up1;
                    break;
                case 2:
                    current_red = red_down1;
                    break;
                case 3:
                    current_red = red_left1;
                    break;
                case 4:
                    current_red = red_right1;
                    break;
                default:
                    current_red = red_right1;
                    break;
            }

            red1 = true;
        }

        if (pink1) {
            switch (pink_state) {
                case 1:
                    current_pink = pink_up2;
                    break;
                case 2:
                    current_pink = pink_down2;
                    break;
                case 3:
                    current_pink = pink_left2;
                    break;
                case 4:
                    current_pink = pink_right2;
                    break;
                default:
                    current_pink = pink_left2;
                    break;
            }

            pink1 = false;
        } else {
            switch (pink_state) {
                case 1:
                    current_pink = pink_up1;
                    break;
                case 2:
                    current_pink = pink_down1;
                    break;
                case 3:
                    current_pink = pink_left1;
                    break;
                case 4:
                    current_pink = pink_right1;
                    break;
                default:
                    current_pink = pink_right1;
                    break;
            }

            pink1 = true;
        }

        if (cyan1) {
            switch (cyan_state) {
                case 1:
                    current_cyan = cyan_up2;
                    break;
                case 2:
                    current_cyan = cyan_down2;
                    break;
                case 3:
                    current_cyan = cyan_left2;
                    break;
                case 4:
                    current_cyan = cyan_right2;
                    break;
                default:
                    current_cyan = cyan_left2;
                    break;
            }

            cyan1 = false;
        } else {
            switch (cyan_state) {
                case 1:
                    current_cyan = cyan_up1;
                    break;
                case 2:
                    current_cyan = cyan_down1;
                    break;
                case 3:
                    current_cyan = cyan_left1;
                    break;
                case 4:
                    current_cyan = cyan_right1;
                    break;
                default:
                    current_cyan = cyan_right1;
                    break;
            }

            cyan1 = true;
        }

        if (yellow1) {
            switch (yellow_state) {
                case 1:
                    current_yellow = yellow_up2;
                    break;
                case 2:
                    current_yellow = yellow_down2;
                    break;
                case 3:
                    current_yellow = yellow_left2;
                    break;
                case 4:
                    current_yellow = yellow_right2;
                    break;
                default:
                    current_yellow = yellow_left2;
                    break;
            }

            yellow1 = false;
        } else {
            switch (yellow_state) {
                case 1:
                    current_yellow = yellow_up1;
                    break;
                case 2:
                    current_yellow = yellow_down1;
                    break;
                case 3:
                    current_yellow = yellow_left1;
                    break;
                case 4:
                    current_yellow = yellow_right1;
                    break;
                default:
                    current_yellow = yellow_right1;
                    break;
            }

            yellow1 = true;
        }

        if (red_currentCoords[0] != 0
                && (((red_state == 1 || red_state == 2) && red_position.getX() != red_currentCoords[0])
                        || ((red_state == 3 || red_state == 4) && red_position.getY() != red_currentCoords[1]))) {
            red_position.setLocation(red_currentCoords[0], red_currentCoords[1]);
        }

        if (pink_currentCoords[0] != 0
                && (((pink_state == 1 || pink_state == 2) && pink_position.getX() != pink_currentCoords[0])
                        || ((pink_state == 3 || pink_state == 4) && pink_position.getY() != pink_currentCoords[1]))) {
            pink_position.setLocation(pink_currentCoords[0], pink_currentCoords[1]);
        }

        if (cyan_currentCoords[0] != 0
                && (((cyan_state == 1 || cyan_state == 2) && cyan_position.getX() != cyan_currentCoords[0])
                        || ((cyan_state == 3 || cyan_state == 4) && cyan_position.getY() != cyan_currentCoords[1]))) {
            cyan_position.setLocation(cyan_currentCoords[0], cyan_currentCoords[1]);
        }

        if (yellow_currentCoords[0] != 0
                && (((yellow_state == 1 || yellow_state == 2) && yellow_position.getX() != yellow_currentCoords[0])
                        || ((yellow_state == 3 || yellow_state == 4) && yellow_position.getY() != yellow_currentCoords[1]))) {
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
    public static boolean inRange(int[] coords, Point position) {
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