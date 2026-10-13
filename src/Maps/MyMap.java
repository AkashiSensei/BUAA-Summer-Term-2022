package Maps;

import GameStatus.Status;
import Monsters.Monster;
import Player.LittleKnight;
import Windows.GameMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public abstract class MyMap {
    public static final int MAP_READY_BENCH = 0, MAP_READY_GATE_0 = 1, MAP_READY_GATE_1 = 2;
    public static final int ZOOM_QUALITY = Image.SCALE_FAST;




    //地图类整体属性
     //小骑士图像中点相对屏幕左上角的位置，即屏幕中点
    private static final int centreX = Status.getWinWidth() >> 1;
    private static final int centreY = Status.getWinHeight() >> 1;

    private static MyMap[] mapsList = new MyMap[]{
            new Crossroad(),
            new Greenpath(),
            new CityOfTears(),
            new CityOfTears()
    };
    private static int mapNowNum;
    private static JLabel mapLabel;


    //小骑士相对地图左上角的位置
    private static int knightX, knightY;
    private static int mapX;
    private static int mapY;
    private static int step;


    //每张地图自身属性
    private String name;
    private String path;
    private int num;
    private ImageIcon mapIcon;
    private int mapWidth, mapHeight;

     //两个地图传送们相对地图左上角的位置
    private int gate0X, gate0Y;
    private int gate1X, gate1Y;


     //椅子相对于地图左上角的位置
    private int benchX, benchY;
    private int[][] monstersRate;
    private int boss;



    public MyMap() {
    }




    //初始化方法
    public static void init() {
        mapNowNum = 0;
        mapLabel = new JLabel();
    }


    public static void addWidget() {
        GameMain.getGameMain().getContentPane().add(mapLabel);
    }




    //事件方法
    public static void ready(int code) {

        System.out.println("Map : ready map " + mapsList[mapNowNum].name + "in code " + code);

        //根据不同的滴入进入代码选择出生点
        switch (code) {
            case MAP_READY_BENCH:
                knightX = mapsList[mapNowNum].benchX;
                knightY = mapsList[mapNowNum].benchY;
                break;
            case MAP_READY_GATE_0:
                knightX = mapsList[mapNowNum].gate0X;
                knightY = mapsList[mapNowNum].gate0Y;
                break;
            case MAP_READY_GATE_1:
                knightX = mapsList[mapNowNum].gate1X;
                knightY = mapsList[mapNowNum].gate1Y;
                break;
        }


        //加载图片
        System.out.println("Map : load map image of" + mapsList[mapNowNum].name);
        mapsList[mapNowNum].mapIcon = new ImageIcon(
                new ImageIcon(mapsList[mapNowNum].path).getImage().getScaledInstance(mapsList[mapNowNum].mapWidth, mapsList[mapNowNum].mapHeight, ZOOM_QUALITY));
        mapLabel.setIcon(mapsList[mapNowNum].mapIcon);


        //设定显示位置及大小
        mapX = centreX - Status.getCameraX() - knightX;
        mapY = centreY - Status.getCameraY() - knightY;
        mapLabel.setBounds(mapX, mapY, mapsList[mapNowNum].mapWidth, mapsList[mapNowNum].mapHeight);

        Monster.newMap(mapsList[mapNowNum]);
    }

    public static void changeLocation() {
        if(LittleKnight.isSoulAttack()){
            return;
        }

        step = (int)(LittleKnight.getSpeed() * (Status.getFrameInterval() + 4));
        //System.out.println(step);


        //修改小骑士位置坐标
        if(LittleKnight.isFront()) {
            knightY += step;
        }else if(LittleKnight.isBack()) {
            knightY -= step;
        }

        if(LittleKnight.isLeft()) {
            knightX -= step;
        }else if(LittleKnight.isRight()) {
            knightX += step;
        }


        //根据小骑士位置坐标计算地图位置并应用
        mapX = centreX - Status.getCameraX() - knightX;
        mapY = centreY - Status.getCameraY() - knightY;
        mapLabel.setLocation(mapX, mapY);
    }

    public static void mapRange() {
        if(knightX < 0 || knightY < 0 || knightX > mapsList[mapNowNum].mapWidth || knightY > mapsList[mapNowNum].mapHeight){
            //System.out.println("Map : out of range!");
            LittleKnight.underAttack(99999.0, 0, 0);
        }
    }

    public static void changeMap() {
        if(LittleKnight.isSoul()) {
            return;
        }

        //System.out.println("Map : changeMap " + knightX + knightY + mapsList[mapNowNum].gate1X +  mapsList[mapNowNum].gate1Y);

        if(knightX > mapsList[mapNowNum].gate0X - 100 && knightX < mapsList[mapNowNum].gate0X + 100  && knightY > mapsList[mapNowNum].gate0Y - 50 && knightX < mapsList[mapNowNum].gate0Y + 50) {
            Status.setGame(Status.GAME_PAUSE);

            System.out.println("Map : --");
            mapNowNum -= 1;
            ready(MAP_READY_GATE_1);

            Status.setGame(Status.GAMING);
        }else if(knightX > mapsList[mapNowNum].gate1X - 100 && knightX < mapsList[mapNowNum].gate1X + 100  && knightY > mapsList[mapNowNum].gate1Y - 50 && knightY < mapsList[mapNowNum].gate1Y + 50) {
            Status.setGame(Status.GAME_PAUSE);

            System.out.println("Map : ++");
            mapNowNum += 1;
            ready(MAP_READY_GATE_0);

            Status.setGame(Status.GAMING);
        }
    }

    public static boolean win() {
        if (mapNowNum != 2)
            return false;
        if (knightX < 7375 || knightX > 7625 || knightY < 500|| knightY > 625) {
            return false;
        }
        if(LittleKnight.getMoney() < 200) {
            return false;
        }
        return true;
    }



    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }


    public void setGate0X(int gate0X) {
        this.gate0X = gate0X;
    }

    public void setGate0Y(int gate0Y) {
        this.gate0Y = gate0Y;
    }

    public void setGate1X(int gate1X) {
        this.gate1X = gate1X;
    }

    public void setGate1Y(int gate1Y) {
        this.gate1Y = gate1Y;
    }

    public void setBenchX(int benchX) {
        this.benchX = benchX;
    }

    public void setBenchY(int benchY) {
        this.benchY = benchY;
    }

    public void setMonstersRate(int[][] monstersRate) {
        this.monstersRate = monstersRate;
    }

    public void setBoss(int boss) {
        this.boss = boss;
    }

    public static JLabel getMapLabel() {
        return mapLabel;
    }

    public static void setKnightX(int knightX) {
        MyMap.knightX = knightX;
    }

    public static void setKnightY(int knightY) {
        MyMap.knightY = knightY;
    }

    public static void setMapNowNum(int mapNowNum) {
        MyMap.mapNowNum = mapNowNum;
    }

    public int[][] getMonstersRate() {
        return monstersRate;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public static int getMapX() {
        return mapX;
    }

    public static int getMapY() {
        return mapY;
    }

    public static MyMap[] getMapsList() {
        return mapsList;
    }

    public static int getMapNowNum() {
        return mapNowNum;
    }

    public static int getKnightX() {
        return knightX;
    }

    public static int getKnightY() {
        return knightY;
    }
}
