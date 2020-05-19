package pacman;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphicsOptions extends JFrame {
    private DrawCanvas canvas;

    public GraphicsOptions() {
        canvas = new DrawCanvas();
        canvas.setPreferredSize(canvas.PREFERRED_SIZE);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(canvas, BorderLayout.CENTER);

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
}