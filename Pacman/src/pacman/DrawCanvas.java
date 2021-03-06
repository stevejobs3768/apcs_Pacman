package pacman;

import java.awt.*;
import javax.swing.*;

public class DrawCanvas extends JPanel {
    // I don't really understand what this is, but its absence causes a warning
    private static final long serialVersionUID = 1L;

    // size of playable game window + height of title bar
    public static final Dimension MAC_CANVAS_SIZE = new Dimension(615, 785 + 30 + 6);
    public static final Dimension WINDOWS_CANVAS_SIZE = new Dimension(615, 785 + 30 + 25);
    public static final Dimension GAME_SIZE = new Dimension(615, 785 + 30); // size of playable game window
    
    // how close to an intersection do you have to be to be "at" that intersection
    private static final int THRESHOLD = 4;
    private final int DELAY = 10; // millisecond delay between iterations of paintComponent
    private final int COUNTER_MAX = 100;
    private final int TILE_SIZE = 22;

    private final Board board = new Board();
    private final Assets assets = GraphicsOptions.assets; // class containing all images
    private final Cherry cherry = new Cherry();
    private final Player player = new Player();
    
    private final Ghost red = new Ghost(307, 323, assets.image_red_up_body1, assets.image_red_down_body1,
            assets.image_red_left_body1, assets.image_red_right_body1, assets.image_red_up_body2,
            assets.image_red_down_body2, assets.image_red_left_body2, assets.image_red_right_body2, false, false, true,
            true, "red");

    private final Ghost pink = new Ghost(307, 388, assets.image_pink_up_body1, assets.image_pink_down_body1,
            assets.image_pink_left_body1, assets.image_pink_right_body1, assets.image_pink_up_body2,
            assets.image_pink_down_body2, assets.image_pink_left_body2, assets.image_pink_right_body2, true, false,
            false, false, "pink");

    private final Ghost cyan = new Ghost(264, 388, assets.image_cyan_up_body1, assets.image_cyan_down_body1,
            assets.image_cyan_left_body1, assets.image_cyan_right_body1, assets.image_cyan_up_body2,
            assets.image_cyan_down_body2, assets.image_cyan_left_body2, assets.image_cyan_right_body2, false, false,
            false, true, "cyan");

    private final Ghost yellow = new Ghost(350, 388, assets.image_yellow_up_body1, assets.image_yellow_down_body1,
            assets.image_yellow_left_body1, assets.image_yellow_right_body1, assets.image_yellow_up_body2,
            assets.image_yellow_down_body2, assets.image_yellow_left_body2, assets.image_yellow_right_body2, false,
            false, true, false, "yellow");

    private int attemptedState = 0;
    private int counter = 0;
    private int score = 0;
    private int cherryCount = 0;
    private int deathCount = 0;
    
    private int ghostsEaten = 0;
    private boolean frightenedMode = false;
    public static int frightenedCounter = 0;

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

        if (frightenedMode) {
            frightenedCounter++;
        }

        if (frightenedCounter > 200) {
            red.stopFrightened();
            pink.stopFrightened();
            cyan.stopFrightened();
            yellow.stopFrightened();
            frightenedMode = false;
            frightenedCounter = 0;
        } else if (frightenedCounter > 150) {
            red.endFrightened();
            pink.endFrightened();
            cyan.endFrightened();
            yellow.endFrightened();
        }

        player.reset();

        red.reset();
        pink.reset();
        cyan.reset();
        yellow.reset();

        // iterate through the intersections. If the player is at an intersection in one
        // of the arrays, it is known that movement in certain directions is available
        // to the player

        intersections(board.upPoints, 1);
        intersections(board.downPoints, 2);
        intersections(board.leftPoints, 3);
        intersections(board.rightPoints, 4);

        if (frightenedMode) {
            confirmFrightenedPositions();
        }

        dots(g);

        if (cherry.draw(g, player.state > 0, player.position, this)) {
            score += 20;
            cherryCount++;
        }

        player.confirmPosition(attemptedState);

        int redState = 0;
        int pinkState = 0;
        int cyanState = 0;
        int yellowState = 0;

        double playerX = player.position.getX();
        double playerY = player.position.getY();

        redState = target(red, playerX, playerY);

        // PINK DIRECTION
        double pinkX = playerX;
        double pinkY = playerY;

        switch (pink.state) {
            case 1:
                pinkX -= 4 * TILE_SIZE;
                pinkY -= 4 * TILE_SIZE;
                break;
            case 2:
                pinkY += 2 * TILE_SIZE;
                break;
            case 3:
                pinkX -= 2 * TILE_SIZE;
                break;
            case 4:
                pinkX += 2 * TILE_SIZE;
                break;
        }

        pinkState = target(pink, pinkX, pinkY);

        double cyanX = playerX;
        double cyanY = playerY;

        switch (cyan.state) {
            case 1:
                cyanX -= 2 * TILE_SIZE;
                cyanY -= 2 * TILE_SIZE;
                break;
            case 2:
                cyanY += 2 * TILE_SIZE;
                break;
            case 3:
                cyanX -= 2 * TILE_SIZE;
                break;
            case 4:
                cyanX += 2 * TILE_SIZE;
                break;
        }

        cyanX += cyanX - red.position.getX();
        cyanY += cyanY - red.position.getY();
        cyanState = target(cyan, cyanX, cyanY);

        double yellowX = playerX;
        double yellowY = playerY;

        if (Math.sqrt(Math.pow(yellow.position.getX() - yellowX, 2) + Math.pow(yellow.position.getY() - yellowY, 2)) < 8
                * TILE_SIZE) {
            yellowX = 35;
            yellowY = 715;
        }
        yellowState = target(yellow, yellowX, yellowY);

        red.confirmPosition();
        pink.confirmPosition();
        cyan.confirmPosition();
        yellow.confirmPosition();

        red.move(redState);
        pink.move(pinkState);
        cyan.move(cyanState);
        yellow.move(yellowState);

        checkRestart();

        player.draw(g, this);

        red.draw(g, this);
        pink.draw(g, this);
        cyan.draw(g, this);
        yellow.draw(g, this);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(assets.pacmanFont.deriveFont(Font.PLAIN, 20));
        FontMetrics fontMetrics = g2.getFontMetrics();
        String s = "" + score * 10;
        String c = "" + cherryCount;
        g2.drawString(c, (int) GAME_SIZE.getWidth() - fontMetrics.stringWidth(c) - 10,
                (int) GAME_SIZE.getHeight() - 35);
        g2.drawImage(GraphicsOptions.resize(assets.image_cherries, 25, 25),
                (int) GAME_SIZE.getWidth() - fontMetrics.stringWidth(c) - 43, (int) GAME_SIZE.getHeight() - 56, this);
        g2.drawString(s, 100 - fontMetrics.stringWidth(s), 50);

        for (int i = 0; i < player.lives; i++) {
            g.drawImage(GraphicsOptions.resize(assets.image_player_small_mouth_left, 16, 16), 16 + 24 * i,
                    (int) (GAME_SIZE.getHeight() - 52), this);
        }

        if (player.lives < 1) {
            exitGame(g2);
        }

        try {
            Thread.sleep((3 - player.lives) * DELAY); // wait 100 milliseconds (the code is just too fast)
        } catch (InterruptedException e) { // Thread.sleep throws an InterruptedException so this is necessary
            e.printStackTrace();
        }

        // call the entire paintComponent function again (causes an infinite loop of
        // gameplay)
        repaint();
    }

    private void confirmFrightenedPositions() {
        if (red.frightened) {
            red.confirmPosition();
        }
        if (pink.frightened) {
            pink.confirmPosition();
        }
        if (cyan.frightened) {
            cyan.confirmPosition();
        }
        if (yellow.frightened) {
            yellow.confirmPosition();
        }
    }

    public void exitGame(Graphics2D g2) {
        final int fontSize = 25;
        cherry.showCherry = false;
        g2.setFont(assets.pacmanFont.deriveFont(Font.PLAIN, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(Color.red);
        g2.drawString("GAME OVER", (int) (GAME_SIZE.getWidth() - fontMetrics.stringWidth("GAME OVER")) / 2,
                (int) (GAME_SIZE.getHeight() - fontSize / 2 + 130) / 2);

        if (deathCount > 0) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }
        deathCount++;
    }

    public void checkRestart() {
        if (!frightenedMode && (inRange(player.position, red.position, 10)
                || inRange(player.position, pink.position, 10) || inRange(player.position, cyan.position, 10)
                || inRange(player.position, yellow.position, 10))) {
            player.restart();
            red.restart();
            pink.restart();
            cyan.restart();
            yellow.restart();
        } else if (inRange(player.position, red.position, 10)) {
            if (!red.frightened) {
                player.restart();
                pink.restart();
                cyan.restart();
                yellow.restart();
            } else {
                score += 10 * (int) Math.pow(2, ghostsEaten + 1);
            }
            red.restart();
        } else if (inRange(player.position, pink.position, 10)) {
            if (!pink.frightened) {
                player.restart();
                red.restart();
                cyan.restart();
                yellow.restart();
            } else {
                score += 10 * (int) Math.pow(2, ghostsEaten + 1);
            }
            pink.restart();
        } else if (inRange(player.position, cyan.position, 10)) {
            if (!cyan.frightened) {
                player.restart();
                red.restart();
                pink.restart();
                yellow.restart();
            } else {
                score += 10 * (int) Math.pow(2, ghostsEaten + 1);
            }
            cyan.restart();
        } else if (inRange(player.position, yellow.position, 10)) {
            if (!yellow.frightened) {
                player.restart();
                red.restart();
                pink.restart();
                cyan.restart();
            } else {
                score += 10 * (int) Math.pow(2, ghostsEaten + 1);
            }
            yellow.restart();
        }
    }

    public void dots(Graphics g) {
        g.setColor(Color.white);
        for (int[] coords : board.dots) {
            if (board.hiddenDots.indexOf(coords) < 0) {
                g.fillOval(coords[0] - 3, coords[1] - 3, 6, 6);

                if (player.intersect(coords, 5)) {
                    score++;
                    board.hiddenDots.add(coords);
                }
            }
        }

        for (int[] coords : board.largeDots) {
            if (board.hiddenDots.indexOf(coords) < 0) {
                g.fillOval(coords[0] - 10, coords[1] - 10, 20, 20);

                if (player.intersect(coords, 5)) {
                    score += 5;
                    board.hiddenDots.add(coords);
                    frightenedMode = true;
                    frightenedCounter = 0;
                    red.frightened();
                    pink.frightened();
                    cyan.frightened();
                    yellow.frightened();
                }
            }
        }

        board.checkReset();
    }

    public int target(Ghost ghost, double playerX, double playerY) {
        if (ghost.position.getY() < 390 && ghost.position.getY() > 328 && ghost.position.getX() < 311
                && ghost.position.getX() > 304 && ghost.position.getY() != 323) {
            return 1;
        } else if (ghost.position.getY() > 380 && ghost.position.getY() < 395 && ghost.position.getX() > 262
                && ghost.position.getX() < 360) {
            if (ghost.name.equals("cyan")) {
                return 4;
            } else if (ghost.name.equals("yellow")) {
                return 3;
            }
        }

        if (!ghost.frightened) {
            if (Math.abs(ghost.getSlope(playerX, playerY)) >= 1) { // if the slope is negative (positive to human eyes)
                if (ghost.position.getY() < playerY) { // if the ghost is higher than the player
                    if (ghost.downAvail) {
                        return 2;
                    } else if (ghost.rightAvail) {
                        return 4;
                    } else if (ghost.upAvail) {
                        return 1;
                    } else if (ghost.leftAvail) {
                        return 3;
                    }
                } else if (ghost.position.getY() > playerY) { // if the ghost is lower the player
                    if (ghost.upAvail) {
                        return 1;
                    } else if (ghost.leftAvail) {
                        return 3;
                    } else if (ghost.downAvail) {
                        return 2;
                    } else if (ghost.rightAvail) {
                        return 4;
                    }
                }
            } else if (Math.abs(red.getSlope(playerX, playerY)) < 1) { // if the slope is positive
                if (ghost.position.getX() < playerX) {
                    if (ghost.rightAvail) {
                        return 4;
                    } else if (ghost.downAvail) {
                        return 2;
                    } else if (ghost.leftAvail) {
                        return 3;
                    } else if (ghost.upAvail) {
                        return 1;
                    }
                } else if (ghost.position.getX() > playerX) { // if the ghost is lower the player
                    if (ghost.leftAvail) {
                        return 3;
                    } else if (ghost.upAvail) {
                        return 1;
                    } else if (ghost.rightAvail) {
                        return 4;
                    } else if (ghost.downAvail) {
                        return 2;
                    }
                }
            } else if (ghost.position.getX() == playerX) {
                if (playerY < ghost.position.getY() && ghost.upAvail) {
                    return 1;
                } else if (playerY > ghost.position.getY() && ghost.downAvail) {
                    return 2;
                } else if (ghost.leftAvail) {
                    return 3;
                } else {
                    return 4;
                }
            }
        } else {
            if (!((ghost.state == 1 && ghost.upAvail) || (ghost.state == 2 && ghost.downAvail)
                    || (ghost.state == 3 && ghost.leftAvail) || (ghost.state == 4 && ghost.rightAvail))) {
                return (int) (Math.random() * 4 + 1);
            }
        }

        return 0;
    }

    public void intersections(int[][] points, int direction) {
        for (int[] coords : points) {
            player.intersect(coords, direction);

            red.intersect(coords, direction);
            pink.intersect(coords, direction);
            cyan.intersect(coords, direction);
            yellow.intersect(coords, direction);
        }
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

    public static boolean inRange(Point position1, Point position2, int threshold) {
        return (position1.getX() > position2.getX() - threshold) && (position1.getX() < position2.getX() + threshold)
                && (position1.getY() > position2.getY() - threshold)
                && (position1.getY() < position2.getY() + threshold);
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