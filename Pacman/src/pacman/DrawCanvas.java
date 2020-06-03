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
    private static final int THRESHOLD = 4;
    private final int DELAY = 10; // millisecond delay between iterations of paintComponent
    private final int COUNTER_MAX = 100;
    // how close to an intersection do you have to be to be "at" that intersection
    private final int TILE_SIZE = 22;

    private final Board board = new Board();
    private final Assets assets = GraphicsOptions.assets; // class containing all images
    private final Cherry cherry = new Cherry();
    private final Player player = new Player();
    private final Ghost red = new Ghost(1, 0.825, assets.image_red_up_body1, assets.image_red_down_body1,
            assets.image_red_left_body1, assets.image_red_right_body1, assets.image_red_up_body2,
            assets.image_red_down_body2, assets.image_red_left_body2, assets.image_red_right_body2, false, false, true,
            true, "red");

    private final Ghost pink = new Ghost(1, 0.985, assets.image_pink_up_body1, assets.image_pink_down_body1,
            assets.image_pink_left_body1, assets.image_pink_right_body1, assets.image_pink_up_body2,
            assets.image_pink_down_body2, assets.image_pink_left_body2, assets.image_pink_right_body2, true, false,
            false, false, "pink");

    private final Ghost cyan = new Ghost(0.88, 0.985, assets.image_cyan_up_body1, assets.image_cyan_down_body1,
            assets.image_cyan_left_body1, assets.image_cyan_right_body1, assets.image_cyan_up_body2,
            assets.image_cyan_down_body2, assets.image_cyan_left_body2, assets.image_cyan_right_body2, false, false,
            false, true, "cyan");

    private final Ghost yellow = new Ghost(1.12, 0.985, assets.image_yellow_up_body1, assets.image_yellow_down_body1,
            assets.image_yellow_left_body1, assets.image_yellow_right_body1, assets.image_yellow_up_body2,
            assets.image_yellow_down_body2, assets.image_yellow_left_body2, assets.image_yellow_right_body2, false,
            false, true, false, "yellow");

    private int attemptedState = 0;
    private int counter = 0;
    private int score = 0;
    private int cherryCount = 0;
    private int deathCount = 0;

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

        intersections(board.upPoints, 1);
        intersections(board.downPoints, 2);
        intersections(board.leftPoints, 3);
        intersections(board.rightPoints, 4);

        dots(g);

        if (cherry.draw(g, player.state > 0, player.position, this)) {
            score += 20;
            cherryCount++;
        }

        player.confirmPosition(attemptedState);

        int[] playerCoords = player.getCoords();

        int redState = ghostTarget(red, playerCoords);
        if (redState == 0) {
            if (red.state == 1 && red.upAvail) {
                redState = 1;
            } else if (red.state == 2 && red.downAvail) {
                redState = 2;
            } else if (red.state == 3 && red.leftAvail) {
                redState = 3;
            } else if (red.state == 4 && red.rightAvail) {
                redState = 4;
            }
        }
        red.confirmPosition(redState);

        int[] pinkCoords = { playerCoords[0], playerCoords[1] };
        if (player.state == 1) {
            pinkCoords[0] -= 4 * TILE_SIZE;
            pinkCoords[1] -= 4 * TILE_SIZE;
        } else if (player.state == 2) {
            pinkCoords[1] += 2 * TILE_SIZE;
        } else if (player.state == 3) {
            pinkCoords[0] -= 2 * TILE_SIZE;
        } else if (player.state == 4) {
            pinkCoords[0] += 2 * TILE_SIZE;
        }

        int pinkState = ghostTarget(pink, pinkCoords);
        if (pinkState == 0) {
            if (pink.state == 1 && pink.upAvail) {
                pinkState = 1;
            } else if (pink.state == 2 && pink.downAvail) {
                pinkState = 2;
            } else if (pink.state == 3 && pink.leftAvail) {
                pinkState = 3;
            } else if (pink.state == 4 && pink.rightAvail) {
                pinkState = 4;
            }
        }
        pink.confirmPosition(pinkState);

        int[] cyanCoords = { playerCoords[0], playerCoords[1] };
        if (player.state == 1) {
            cyanCoords[0] -= 2 * TILE_SIZE;
            cyanCoords[1] -= 2 * TILE_SIZE;
        } else if (player.state == 2) {
            cyanCoords[1] += 2 * TILE_SIZE;
        } else if (player.state == 3) {
            cyanCoords[0] -= 2 * TILE_SIZE;
        } else if (player.state == 4) {
            cyanCoords[0] += 2 * TILE_SIZE;
        }

        cyanCoords[0] += cyanCoords[0] - red.position.getX();
        cyanCoords[1] += cyanCoords[1] - red.position.getY();

        int cyanState = ghostTarget(cyan, cyanCoords);
        if (cyanState == 0) {
            if (cyan.state == 1 && cyan.upAvail) {
                cyanState = 1;
            } else if (cyan.state == 2 && cyan.downAvail) {
                cyanState = 2;
            } else if (cyan.state == 3 && cyan.leftAvail) {
                cyanState = 3;
            } else if (cyan.state == 4 && cyan.rightAvail) {
                cyanState = 4;
            }
        }
        cyan.confirmPosition(cyanState);

        int[] yellowCoords = { playerCoords[0], playerCoords[1] };

        double yellowDistance = Math.sqrt(Math.pow(yellow.position.getX() - playerCoords[0], 2)
                + Math.pow(yellow.position.getY() - playerCoords[1], 2));
        if (yellowDistance < 8 * TILE_SIZE) {
            yellowCoords[0] = 35;
            yellowCoords[1] = 715;
        }

        int yellowState = ghostTarget(yellow, yellowCoords);

        if (yellowState == 0) {
            if (yellow.state == 1 && yellow.upAvail) {
                yellowState = 1;
            } else if (yellow.state == 2 && yellow.downAvail) {
                yellowState = 2;
            } else if (yellow.state == 3 && yellow.leftAvail) {
                yellowState = 3;
            } else if (yellow.state == 4 && yellow.rightAvail) {
                yellowState = 4;
            }
        }
        yellow.confirmPosition(yellowState);

        // System.out.println("" + redState + pinkState + cyanState + yellowState);

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

    public int ghostTarget(Ghost ghost, int[] playerCoords) {
        double slope = (red.position.getY() - player.position.getY()) / (red.position.getX() - player.position.getX());

        if (ghost.position.getY() < 390 && ghost.position.getY() > 328 && ghost.position.getX() < 310
                && ghost.position.getX() > 305 && ghost.position.getY() != 323) {
            return 1;
        } else if (ghost.position.getY() > 385 && ghost.position.getY() < 390 && ghost.position.getX() > 265
                && ghost.position.getX() < 350) {
            if (ghost.name == "cyan") {
                return 4;
            } else if (ghost.name == "yellow") {
                return 3;
            }
        }

        if ((slope < 1 && slope > 0) || (slope > -1 && slope < 0)) {
            if (ghost.position.getX() > playerCoords[0] && ghost.leftAvail) {
                return 3;
            } else if (ghost.position.getX() < playerCoords[0] && ghost.rightAvail) {
                return 4;
            } else if (ghost.position.getY() > playerCoords[1] && ghost.upAvail) {
                return 1;
            } else if (ghost.position.getY() < playerCoords[1] && ghost.downAvail) {
                return 2;
            } else if (ghost.upAvail) {
                return 1;
            } else if (ghost.downAvail) {
                return 2;
            } else if (ghost.leftAvail) {
                return 3;
            } else if (ghost.rightAvail) {
                return 4;
            }
        } else if (slope < -1 || slope > 1) {
            if (ghost.position.getY() > playerCoords[1] && ghost.upAvail) {
                return 1;
            } else if (ghost.position.getY() < playerCoords[1] && ghost.downAvail) {
                return 2;
            } else if (ghost.position.getX() > playerCoords[0] && ghost.leftAvail) {
                return 3;
            } else if (ghost.position.getX() < playerCoords[0] && ghost.rightAvail) {
                return 4;
            } else if (ghost.leftAvail) {
                return 3;
            } else if (ghost.upAvail) {
                return 4;
            } else if (ghost.upAvail) {
                return 1;
            } else if (ghost.downAvail) {
                return 2;
            }
        }

        return 0;
    }

    public void exitGame(Graphics2D g2) {
        final int fontSize = 50;
        g2.setFont(assets.pacmanFont.deriveFont(Font.PLAIN, fontSize));
        FontMetrics fontMetrics = g2.getFontMetrics();
        g2.setColor(Color.red);
        g2.drawString("GAME OVER", (int) (GAME_SIZE.getWidth() - fontMetrics.stringWidth("GAME OVER")) / 2,
                (int) (GAME_SIZE.getHeight() - fontSize / 2 + 30) / 2);

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
        if (inRange(player.position, red.position, 10) || inRange(player.position, pink.position, 10)
                || inRange(player.position, cyan.position, 10) || inRange(player.position, yellow.position, 10)) {
            player.restart();
            red.restart();
            pink.restart();
            cyan.restart();
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

        board.checkReset();
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