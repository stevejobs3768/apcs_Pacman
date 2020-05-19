/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author ishanmadan
 */
public class Pacman {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImageIcon icon = new ImageIcon("assets/cherries.png");
        // JLabel label = new JLabel(System.getProperty("user.dir"));
        JLabel label = new JLabel(icon);
        frame.add(label);
        frame.setLocation(new Point(100, 100));
        frame.setSize(new Dimension(615, 785 + 22)); */

        JFrame frame = new GraphicsOptions();
        frame.setTitle("Pacman");
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.requestFocus();
    }

}
