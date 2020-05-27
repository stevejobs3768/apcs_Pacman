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
        Toolkit defToolkit = Toolkit.getDefaultToolkit(); // used to get screenSize
        Dimension screenSize = defToolkit.getScreenSize(); // gets size of the whole screen
        Dimension size; // gets size of the window

        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            size = DrawCanvas.WINDOWS_CANVAS_SIZE;
        } else {
            size = DrawCanvas.MAC_CANVAS_SIZE;
        }

        // defines the top left corner of the Pacman window such that the window is in
        // the exact center of the screen
        Point location = new Point((int) (screenSize.getWidth() - size.getWidth()) / 2,
                (int) (screenSize.getHeight() - size.getHeight()) / 2);

        JFrame frame = new GraphicsOptions(); // creates the window
        // sets the title at the top of the window (ex: the top of VS Code says
        // Pacman.java)
        frame.setTitle("Pacman");
        frame.setSize(size); // sets the size of the window
        // saves the preferred size, used whenever .pack() or .getPreferredSize() are
        // called
        frame.setPreferredSize(size);
        frame.setLocation(location); // puts the window in the center of the screen
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits the program when the window is closed
        frame.pack(); // shrinks the window to the preferred size
        frame.setResizable(false); // user may not resize the window
        frame.setVisible(true); // window appears to the user
        frame.requestFocus(); // window is called to the front of the user's desktop
    }

}
