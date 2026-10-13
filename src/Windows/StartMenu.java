package Windows;

import GameStatus.Status;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class StartMenu extends JFrame implements KeyListener {
    private static StartMenu mainMenu;

    //menu主图片Label
    private JLabel menuLabel;

    //menu主图片Icon
    private ImageIcon menuImg0;

    //选择指示器Icon
    private ImageIcon pointerImg0;
    private ImageIcon pointerImg1;
    private ImageIcon pointerImg2;


    //选择指示器Label
    private JLabel pointer0;
    private JLabel pointer1;
    private JLabel pointer2;


    //选择状态
    private boolean isSelecting;
    private int sizeSelect;
    private int frameSelect;
    private int diffSelect;

    public static void main(String[] args) {
        mainMenu = new StartMenu();
        mainMenu.init();

        System.out.println("Windows.StartMenu : show window");
        mainMenu.setVisible(true);
        mainMenu.setFocusable(true);


    }

    public void init() {
        //初始化开始菜单窗口
        System.out.println("------------Initiate Windows.StartMenu Window------------");
        System.out.println("Windows.StartMenu : set window");
        this.setSize(1280, 720);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setTitle("Circle Knight Menu");
        this.setLocationRelativeTo(null);
        Image iconImg = new ImageIcon(".//img//Icon.png").getImage();
        this.setIconImage(iconImg);


        //初始化菜单状态
        System.out.println("Windows.StartMenu : set default status");
        isSelecting = false;
        sizeSelect = Status.WIN_MEDIUM;
        frameSelect = Status.FRAME_MEDIUM;
        diffSelect = Status.DIFF_NORMAL;


        //初始化图片资源
        System.out.println("Windows.StartMenu : load image");
        menuImg0 = new ImageIcon(".//img//Start_Menu0.jpg");
        pointerImg0 = new ImageIcon(".//img//Pointer0.png");
        pointerImg1 = new ImageIcon(".//img//Pointer1.png");
        pointerImg2 = new ImageIcon(".//img//Pointer2.png");
        menuLabel = new JLabel(new ImageIcon(".//img//Start_Menu.jpg"));
        menuLabel.setBounds(0, 0, 1280, 720);
        pointer0 = new JLabel();
        pointer0.setBounds(695, 34, pointerImg0.getIconWidth(), pointerImg0.getIconWidth());
        pointer1 = new JLabel();
        pointer1.setBounds(616, 197, pointerImg1.getIconWidth(), pointerImg1.getIconHeight());
        pointer2 = new JLabel();
        pointer2.setBounds(480, 399, pointerImg0.getIconWidth(), pointerImg2.getIconHeight());


        //添加图片组件
        System.out.println("Windows.StartMenu : add widget");
        this.getContentPane().add(pointer0);
        this.getContentPane().add(pointer1);
        this.getContentPane().add(pointer2);
        this.getContentPane().add(menuLabel);


        //添加键盘监视器
        this.addKeyListener(this);
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println("Windows.StartMenu : " + key);
        if(isSelecting) {
            switch (key) {
                case 81:
                    this.pointer0.setLocation(423, 34);
                    this.sizeSelect = Status.WIN_SMALL;
                    break;
                case 87:
                    this.pointer0.setLocation(695, 34);
                    this.sizeSelect = Status.WIN_MEDIUM;
                    break;
                case 69:
                    this.pointer0.setLocation(965, 34);
                    this.sizeSelect = Status.WIN_LARGE;
                    break;


                case 65:
                    this.pointer1.setLocation(437, 197);
                    this.frameSelect = Status.FRAME_SLOW;
                    break;
                case 83:
                    this.pointer1.setLocation(616, 197);
                    this.frameSelect = Status.FRAME_MEDIUM;
                    break;
                case 68:
                    this.pointer1.setLocation(800, 197);
                    this.frameSelect = Status.FRAME_HIGH;
                    break;
                case 70:
                    this.pointer1.setLocation(977, 197);
                    this.frameSelect = Status.FRAME_EXTRA;
                    break;


                case 90:
                    this.pointer2.setLocation(480, 399);
                    this.diffSelect = Status.DIFF_NORMAL;
                    break;
                case 88:
                    this.pointer2.setLocation(763, 399);
                    this.diffSelect = Status.DIFF_HARD;
                    break;
                case 67:
                    this.pointer2.setLocation(1029, 399);
                    this.diffSelect = Status.DIFF_EXTRA;
                    break;


                case 32:
                    this.CloseMenu();
            }
        }else {
            if(key == 72) {
                System.out.println("Windows.StartMenu : start Selecting");
                this.isSelecting = true;
                this.menuLabel.setIcon(menuImg0);
                this.pointer0.setIcon(pointerImg0);
                this.pointer1.setIcon(pointerImg1);
                this.pointer2.setIcon(pointerImg2);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void CloseMenu() {
        System.out.println("Windows.StartMenu : close Windows.StartMenu");
        mainMenu.setFocusable(false);
        mainMenu.setVisible(false);

        Status.init(this.sizeSelect, this.frameSelect, this.diffSelect);
        GameMain.StartGame();
    }
}
