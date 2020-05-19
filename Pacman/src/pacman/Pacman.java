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
 * @author ishanmadan & shreyaparikh
 */
public class Pacman {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Toolkit defToolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = defToolkit.getScreenSize();
        Dimension size = DrawCanvas.GAME_SIZE;
        Point location = new Point((int)(screenSize.getWidth() - size.getWidth()) / 2,(int)(screenSize.getHeight() - size.getHeight()) / 2);

        JFrame frame = new GraphicsOptions();
        frame.pack();
        frame.setTitle("Pacman");
        frame.setSize(size);
        frame.setPreferredSize(size);
        frame.setLocation(location);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.requestFocus();
    }

}
