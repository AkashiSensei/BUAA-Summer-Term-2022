package Windows;

import javax.swing.*;
import java.awt.*;

public class Over extends JFrame {
    public Over() {
        setSize(1280, 720);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);
        setTitle("Circle Knight Game Over");
        setLocationRelativeTo(null);
        Image iconImg = new ImageIcon(".//img//Icon.png").getImage();
        setIconImage(iconImg);

        JLabel imgLabel = new JLabel(new ImageIcon(".//img//Game_over.png"));
        imgLabel.setBounds(0, 0, 1280, 720);
        imgLabel.setVisible(true);

        getContentPane().add(imgLabel);
        setVisible(true);
    }
}
