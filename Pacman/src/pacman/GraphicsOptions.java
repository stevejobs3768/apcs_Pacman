package pacman;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

import javax.swing.*;

public class GraphicsOptions extends JFrame {
    private DrawCanvas canvas;
    private Assets assets;

    public GraphicsOptions() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(canvas.GAME_SIZE);

        assets = new Assets();

        this.setIconImage(assets.image_player_small_mouth);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);
        // cp.add(backgroundLabel, BorderLayout.CENTER);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
                    // canvas.changeX1(-10);
                    canvas.repaint();
                } else if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // canvas.changeX1(10);
                    canvas.repaint();
                } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
                    // canvas.changeY1(-10);
                    canvas.repaint();
                } else if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                    // canvas.changeY1(10);
                    canvas.repaint();
                }
            }
        });
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) { 
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
    
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
    
        return dimg;
    }  
}