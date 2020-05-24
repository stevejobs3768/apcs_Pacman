package pacman;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

public class GraphicsOptions extends JFrame {
    // I don't really understand what this is, but its absence causes a warning
    private static final long serialVersionUID = 1L;

    public static final Assets assets = new Assets(); // image assets

    private DrawCanvas canvas; // gameplay object

    public GraphicsOptions() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(DrawCanvas.GAME_SIZE);

        // this is supposed to control the app icon but for some reason it doesn't work?
        // idk why but it's not that important
        this.setIconImage(assets.image_player_small_mouth_left);

        Container cp = getContentPane(); // gets the contents of the JFrame
        cp.setLayout(new BorderLayout()); // uses a BorderLayout to organize the contents of the JFrame
        // adds the canvas to the center of the JFrame (canvas is the same size as
        // JFrame so it doesn't really make a difference where it's placed, but if they
        // were different sizes this would matter more)
        cp.add(canvas, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() { // get keys pressed
            // fairly straightforward: when a certain key is pressed, attempt to change the
            // player's direction
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_LEFT || evt.getKeyCode() == KeyEvent.VK_A) {
                    // left arrow key or A
                    canvas.shiftPlayerLeft();
                } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT || evt.getKeyCode() == KeyEvent.VK_D) {
                    // right arrow key or D
                    canvas.shiftPlayerRight();
                } else if (evt.getKeyCode() == KeyEvent.VK_UP || evt.getKeyCode() == KeyEvent.VK_W) {
                    // up arrow key or W
                    canvas.shiftPlayerUp();
                } else if (evt.getKeyCode() == KeyEvent.VK_DOWN || evt.getKeyCode() == KeyEvent.VK_S) {
                    // down arrow key or S
                    canvas.shiftPlayerDown();
                }
            }
        });
    }

    /**
     * can be used by any class to resize a BufferedImage into a smaller
     * BufferedImage. All the asset files are 512x512 (except the background), but
     * that's pretty massive compared to the size of the game itself (615x785), so
     * this method is necessary to shrink those images down to a usable size
     * 
     * @param img  BufferedImage object to resize
     * @param newW new width
     * @param newH new height
     * @return resized BufferedImage object with dimensions newW and newH
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }
}