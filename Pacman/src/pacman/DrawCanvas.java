package pacman;

import java.awt.*;
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
    private final Ghost red = new Ghost(1, 0.825, assets.image_red_up_body1, assets.image_red_down_body1,
            assets.image_red_left_body1, assets.image_red_right_body1, assets.image_red_up_body2,
            assets.image_red_down_body2, assets.image_red_left_body2, assets.image_red_right_body2, false, false, true,
            true);

    private final Ghost pink = new Ghost(1, 0.985, assets.image_pink_up_body1, assets.image_pink_down_body1,
            assets.image_pink_left_body1, assets.image_pink_right_body1, assets.image_pink_up_body2,
            assets.image_pink_down_body2, assets.image_pink_left_body2, assets.image_pink_right_body2, true, false,
            false, false);

    private final Ghost cyan = new Ghost(0.88, 0.985, assets.image_cyan_up_body1, assets.image_cyan_down_body1,
            assets.image_cyan_left_body1, assets.image_cyan_right_body1, assets.image_cyan_up_body2,
            assets.image_cyan_down_body2, assets.image_cyan_left_body2, assets.image_cyan_right_body2, false, false,
            false, true);

    private final Ghost yellow = new Ghost(1.12, 0.985, assets.image_yellow_up_body1, assets.image_yellow_down_body1,
            assets.image_yellow_left_body1, assets.image_yellow_right_body1, assets.image_yellow_up_body2,
            assets.image_yellow_down_body2, assets.image_yellow_left_body2, assets.image_yellow_right_body2, false,
            false, true, false);

    private static final int THRESHOLD = 4; // how close to an intersection do you have to be to be "at" that
                                            // intersection

    private int attemptedState = 0;
    private int counter = 0;

    private ArrayList<int[]> hiddenDots = new ArrayList<int[]>();

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

        player.reset();

        red.reset();
        pink.reset();
        cyan.reset();
        yellow.reset();

        // iterate through the intersections. If the player is at an intersection in one
        // of the arrays, it is known that movement in certain directions is available
        // to the player

        // used when displaying colored dots at intersections to indicate available
        // directions (for troubleshooting purposes)
        // g.setColor(Color.green);
        for (int[] coords : board.leftPoints) { // iterates through all intersections where left is an option
            // g.fillOval(coords[0] - 10, coords[1] - 5, 10, 10); // draws the colored dot

            player.intersect(coords, 3);

            red.intersect(coords, 3);
            pink.intersect(coords, 3);
            cyan.intersect(coords, 3);
            yellow.intersect(coords, 3);
        }

        // g.setColor(Color.orange);
        for (int[] coords : board.upPoints) {
            // g.fillOval(coords[0] - 5, coords[1] - 10, 10, 10);

            player.intersect(coords, 1);

            red.intersect(coords, 1);
            pink.intersect(coords, 1);
            cyan.intersect(coords, 1);
            yellow.intersect(coords, 1);
        }

        // g.setColor(Color.cyan);
        for (int[] coords : board.rightPoints) {
            // g.fillOval(coords[0], coords[1] - 5, 10, 10);

            player.intersect(coords, 4);

            red.intersect(coords, 4);
            pink.intersect(coords, 4);
            cyan.intersect(coords, 4);
            yellow.intersect(coords, 4);
        }

        // g.setColor(Color.magenta);
        for (int[] coords : board.downPoints) {
            // g.fillOval(coords[0] - 5, coords[1], 10, 10);

            player.intersect(coords, 2);

            red.intersect(coords, 2);
            pink.intersect(coords, 2);
            cyan.intersect(coords, 2);
            yellow.intersect(coords, 2);
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

        // TODO: Add actual tracking code
        int redState = (int) (Math.random() * 3) + 1;
        int pinkState = (int) (Math.random() * 3) + 1;
        int cyanState = (int) (Math.random() * 3) + 1;
        int yellowState = (int) (Math.random() * 3) + 1;

        System.out.println("" + redState + pinkState + cyanState + yellowState);

        red.confirmPosition(redState);
        pink.confirmPosition(pinkState);
        cyan.confirmPosition(cyanState);
        yellow.confirmPosition(yellowState);

        player.draw(g, this);

        red.draw(g, this);
        pink.draw(g, this);
        cyan.draw(g, this);
        yellow.draw(g, this);

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