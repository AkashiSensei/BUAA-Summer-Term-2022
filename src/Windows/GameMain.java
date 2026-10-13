package Windows;

import GameStatus.Status;
import Maps.MyMap;
import Monsters.Monster;
import Player.LittleKnight;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameMain extends JFrame implements KeyListener {
    public static final double DELTA = 1e-8;

    public static final int centreX = Status.getWinWidth() >> 1;
    public static final int centreY = Status.getWinHeight() >> 1;


    private static GameMain gameMain;

    public static JLabel backGround;


    //初始化并启动游戏主窗口
    public static void StartGame() {
        System.out.println("---------------Initiate Game Window--------------");
        System.out.println("Windows.GameMain : generate game window, player, maps");


        gameMain = new GameMain();
        init();

        LittleKnight.init();
        MyMap.init();
        Guide.init();


        Monster.init();
        readyMap(MyMap.MAP_READY_BENCH);


        addWidget();

        startRefresh();

        gameMain.setVisible(true);
        gameMain.setFocusable(true);
    }





    //用于GameMain初始化的子方法
    public static void init() {
        //初始化窗口
        System.out.println("Windows.GameMain : initiate MainWindow");
        gameMain.setSize(Status.getWinWidth(), Status.getWinHeight());
        gameMain.setResizable(false);
        gameMain.setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameMain.setLayout(null);
        gameMain.setTitle("Circle Knight");
        gameMain.setLocationRelativeTo(null);
        Image iconImg = new ImageIcon(".//img//Icon.png").getImage();
        gameMain.setIconImage(iconImg);

        backGround = new JLabel(new ImageIcon(new ImageIcon(".//img//Back_Ground.png").getImage().getScaledInstance(Status.getWinWidth(), Status.getWinHeight(), Image.SCALE_FAST)));
        backGround.setBounds(0, 0, Status.getWinWidth(), Status.getWinHeight());
        backGround.setVisible(true);

        //test:
        /*
        ImageIcon testIcon = new ImageIcon("img//Little_Knight//Right.png");
        JLabel testLabel = new JLabel(testIcon);
        testLabel.setBounds(
                status.getWinWidth() / 2 - testIcon.getIconWidth() / 2,
                status.getWinHeight() / 2 - testIcon.getIconHeight() / 2,
                testIcon.getIconWidth(),
                testIcon.getIconHeight()
        );

         */
        //this.getContentPane().add(testLabel);

        //添加键盘监视器
        gameMain.addKeyListener(gameMain);
    }


    public static void addWidget() {
        System.out.println("Windows.GameMain : add widget");
        Guide.addWidget();
        LittleKnight.addWidget();
        Monster.addWidget();
        MyMap.addWidget();
        gameMain.getContentPane().add(backGround);
    }


    public static void startRefresh() {
        System.out.println("Windows.GameMain : generate & start refresher");
        Refresher mainThread = new Refresher();
        mainThread.start();
    }






    //事件方法
    public static void refresh() {
        LittleKnight.changeMoveIcon();
        LittleKnight.applyIcon();

        MyMap.changeLocation();
        MyMap.mapRange();

        Monster.applyChanges();
        if(Status.getGame() == Status.GAMING) {
            Monster.checkCrash();
        }
    }


    public static void readyMap(int code) {
        MyMap.ready(code);
    }


    public static void playerDead() {
        //TODO:写处理小骑士死亡，重新开始地图的方法
        Status.setGame(Status.GAME_PAUSE);
        readyMap(MyMap.MAP_READY_BENCH);
        new Thread(() -> {
            try {


                //线程主要工作部分
                Thread.sleep(5000);
                Status.setGame(Status.GAMING);
                LittleKnight.revive();



            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void win() {
        if(MyMap.win()) {
            Status.setGame(Status.GAME_OVER);
            gameMain.setFocusable(false);
            gameMain.setVisible(false);
            new Over();
        }
    }





    //键盘监视器
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println("Windows.GameMain pressed : " + key);
        switch (Status.getGame()) {
            case Status.GAME_SHOP:
            case Status.GAME_PAUSE:
                switch (key) {
                    case 33:
                        Guide.up();
                        break;
                    case 34:
                        Guide.down();
                        break;
                    case 78:
                        Guide.openClose();
                        break;
                }
                break;
            case Status.GAMING:
                switch (key) {
                    //移动
                    //w
                    case 87:
                        LittleKnight.setFront(false);
                        LittleKnight.setBack(true);
                        break;

                    //a
                    case 65:
                        LittleKnight.setLeft(true);
                        LittleKnight.setRight(false);
                        LittleKnight.setAttackDir(LittleKnight.ATTACK_LEFT);
                        break;

                    //s
                    case 83:
                        LittleKnight.setFront(true);
                        LittleKnight.setBack(false);
                        break;

                    //d
                    case 68:
                        LittleKnight.setRight(true);
                        LittleKnight.setLeft(false);
                        LittleKnight.setAttackDir(LittleKnight.ATTACK_RIGHT);
                        break;


                    //进攻
                    //j
                    case 74:
                        //System.out.println(LittleKnight.isAttackAble());
                        if(LittleKnight.isAttackAble()) {
                            LittleKnight.attack();
                        }
                        else if(LittleKnight.isSoul() && !LittleKnight.isSoulAttack()) {
                            LittleKnight.soulAttack();
                        }
                        break;


                    //灵魂
                     //' '
                    case 32:
                        if(LittleKnight.isCanSoul()) {
                            LittleKnight.startSoul();
                        }else if(LittleKnight.isSoul() && !LittleKnight.isSoulAttack()) {
                            LittleKnight.endSoul();
                        }
                        break;


                    //地图
                     //f
                    case 70:
                        MyMap.changeMap();
                        win();
                        break;

                    case 33:
                        Guide.up();
                        break;
                    case 34:
                        Guide.down();
                        break;
                    case 78:
                        Guide.openClose();
                        break;
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        //System.out.println("Windows.GameMain released : " + key);
        switch (Status.getGame()) {
            case Status.GAMING:
                switch (key) {
                    //w
                    case 87:
                        LittleKnight.setBack(false);
                        break;

                    //a
                    case 65:
                        LittleKnight.setLeft(false);
                        break;

                    //s
                    case 83:
                        LittleKnight.setFront(false);
                        break;

                    //d
                    case 68:
                        LittleKnight.setRight(false);
                        break;
                }
        }
    }



    //getter和setter
    public static GameMain getGameMain() {
        return gameMain;
    }
}

class Refresher extends Thread {
    private final int delay = Status.getFrameInterval();


    @Override
    public void run() {
        while (true) {
            //System.out.println("refresher working");
            GameMain.refresh();
            try {
                //休眠给定时长
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


/*
class FootController extends Thread {
    private final Windows.GameMain win;
    public FootController(Windows.GameMain gameMain) {
        win = gameMain;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("footController working");
            try {
                win.setFoot(0);
                Thread.sleep(Const.FOOT_CHANGE_TIME);
                win.setFoot(1);
                Thread.sleep(Const.FOOT_CHANGE_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

 */