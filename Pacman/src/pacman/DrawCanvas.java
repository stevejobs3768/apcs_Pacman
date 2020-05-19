package pacman;

import java.awt.*;
import javax.swing.*;

public class DrawCanvas extends JPanel {
    public static final Dimension PREFERRED_SIZE = new Dimension(615, 785+22);
    public static final Color CANVAS_BACKGROUND = Color.BLACK;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        setBackground(CANVAS_BACKGROUND);

        g.setColor(Color.RED);
        g.drawRoundRect(25, 25, (int)PREFERRED_SIZE.getWidth()-50, (int)PREFERRED_SIZE.getHeight()-50, 200, 200);
    }
}