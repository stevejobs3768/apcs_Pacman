package pacman;

import java.awt.*;
import java.awt.image.*;

public class Ghost {
    public Point position;
    private Point initial_position;
    private final int dimension = 32;
    private double shift = 5;
    private double shiftChange = 0.001;

    private BufferedImage up1;
    private BufferedImage down1;
    private BufferedImage left1;
    private BufferedImage right1;

    private BufferedImage up2;
    private BufferedImage down2;
    private BufferedImage left2;
    private BufferedImage right2;

    private BufferedImage current;
    private boolean image1 = false;

    public boolean upAvail;
    public boolean downAvail;
    public boolean leftAvail;
    public boolean rightAvail;
    public int state = 0;

    public String name;

    private int[] currentCoords = { 0, 0 };

    public Ghost(double xFactor, double yFactor, BufferedImage up1, BufferedImage down1, BufferedImage left1,
            BufferedImage right1, BufferedImage up2, BufferedImage down2, BufferedImage left2, BufferedImage right2,
            boolean up, boolean down, boolean left, boolean right, String name) {

        initial_position = new Point((int) (DrawCanvas.GAME_SIZE.getWidth() * xFactor / 2),
                (int) ((DrawCanvas.GAME_SIZE.getHeight() - 30) * yFactor / 2));
        this.position = new Point((int) (DrawCanvas.GAME_SIZE.getWidth() * xFactor / 2),
                (int) ((DrawCanvas.GAME_SIZE.getHeight() - 30) * yFactor / 2));

        this.up1 = GraphicsOptions.resize(up1, dimension, dimension);
        this.down1 = GraphicsOptions.resize(down1, dimension, dimension);
        this.left1 = GraphicsOptions.resize(left1, dimension, dimension);
        this.right1 = GraphicsOptions.resize(right1, dimension, dimension);

        this.up2 = GraphicsOptions.resize(up2, dimension, dimension);
        this.down2 = GraphicsOptions.resize(down2, dimension, dimension);
        this.left2 = GraphicsOptions.resize(left2, dimension, dimension);
        this.right2 = GraphicsOptions.resize(right2, dimension, dimension);

        current = down2;

        upAvail = up;
        downAvail = down;
        leftAvail = left;
        rightAvail = right;

        this.name = name;
    }

    public int[] getCoords() {
        int[] coords = { (int) position.getX(), (int) position.getY() };
        return coords;
    }

    public void restart() {
        position.setLocation(initial_position);
        currentCoords = new int[2];
        reset();
        state = 0;
    }

    public void reset() {
        if (state > 0) {
            upAvail = false;
            downAvail = false;
            leftAvail = false;
            rightAvail = false;
        }
    }

    public void intersect(int[] coords, int direction) {
        if (DrawCanvas.inRange(coords, position)) {
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
            }

            currentCoords[0] = coords[0];
            currentCoords[1] = coords[1];
        }
    }

    public void confirmPosition(int attemptedState) {
        if (!leftAvail && !rightAvail && !upAvail && !downAvail) {
            if (state == 1 || state == 2) {
                upAvail = true;
                downAvail = true;
            } else if (state == 3 || state == 4) {
                leftAvail = true;
                rightAvail = true;
            }
        }

        shift += shiftChange;
        if (shift > 7) {
            shift = 5;
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

        if (state == 1 && upAvail) {
            position.translate(0, -1 * (int) Math.round(shift));
        } else if (state == 2 && downAvail) {
            position.translate(0, (int) Math.round(shift));
        } else if (state == 3 && leftAvail) {
            position.translate(-1 * (int) Math.round(shift), 0);
        } else if (state == 4 && rightAvail) {
            position.translate((int) Math.round(shift), 0);
        }

        if (currentCoords[0] != 0 && (((state == 1 || state == 2) && position.getX() != currentCoords[0])
                || ((state == 3 || state == 4) && position.getY() != currentCoords[1]))) {
            position.setLocation(currentCoords[0], currentCoords[1]);
        }

        if (position.getX() > DrawCanvas.GAME_SIZE.getWidth()) {
            position.setLocation(0, position.getY());
        } else if (position.getX() < 0) {
            position.setLocation(DrawCanvas.GAME_SIZE.getWidth(), position.getY());
        }

        // System.out.println(upAvail + " " + downAvail + " " + leftAvail + " " +
        // rightAvail);
    }

    public void draw(Graphics g, DrawCanvas canvas) {
        if (image1) {
            switch (state) {
                case 1:
                    current = up2;
                    break;
                case 2:
                    current = down2;
                    break;
                case 3:
                    current = left2;
                    break;
                case 4:
                    current = right2;
                    break;
                default:
                    current = left2;
                    break;
            }

            image1 = false;
        } else {
            switch (state) {
                case 1:
                    current = up1;
                    break;
                case 2:
                    current = down1;
                    break;
                case 3:
                    current = left1;
                    break;
                case 4:
                    current = right1;
                    break;
                default:
                    current = right1;
                    break;
            }

            image1 = true;
        }

        g.drawImage(current, (int) position.getX() - dimension / 2, (int) position.getY() - dimension / 2, canvas);
    }
}