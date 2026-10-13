package Windows;

import javax.swing.*;

public class Guide {
    public static JLabel guideLabel;
    public static ImageIcon[] imgIcon;
    public static boolean isVisible;
    public static int pageNow;

    public static void init() {
        imgIcon = new ImageIcon[] {
                new ImageIcon(".//img//guide//Guide0.png"),
                new ImageIcon(".//img//guide//Guide1.png"),
                new ImageIcon(".//img//guide//Guide2.png"),
                new ImageIcon(".//img//guide//Guide3.png"),
                new ImageIcon(".//img//guide//Guide4.png"),
                new ImageIcon(".//img//guide//Guide5.png"),
                new ImageIcon(".//img//guide//Guide6.png"),
                new ImageIcon(".//img//guide//Guide7.png"),
                new ImageIcon(".//img//guide//Guide8.png")
        };

        guideLabel = new JLabel(imgIcon[0]);
        guideLabel.setBounds(550, 20, 700, 300);
        isVisible = true;
        guideLabel.setVisible(true);
    }

    public static void openClose() {
        if(isVisible){
            guideLabel.setVisible(false);
            isVisible = false;
        }else {
            guideLabel.setVisible(true);
            isVisible = true;
        }
    }

    public static void down() {
        if(!isVisible)
            return;

        if(pageNow < 8) {
            pageNow++;
            guideLabel.setIcon(imgIcon[pageNow]);
        }
    }

    public static void up() {
        if(!isVisible)
            return;

        if(pageNow > 0) {
            pageNow--;
            guideLabel.setIcon(imgIcon[pageNow]);
        }
    }

    public static void addWidget() {
        GameMain.getGameMain().getContentPane().add(guideLabel);
    }
}
