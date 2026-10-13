package Monsters;

import GameStatus.Status;
import Maps.MyMap;
import Player.LittleKnight;
import Windows.GameMain;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public abstract class Monster {
    //TODO:要有一个攻击状态，在攻击状态下播放攻击图片，此时受到攻击不会结束攻击进程，同时也不会出现流血动画，同时在攻击状态下造成的伤害更少
    //怪物状态
    public static final int MONSTER_STROLL = 0, MONSTER_HATING = 1, MONSTER_ATTACK = 2, MONSTER_DEAD = 4;
    //怪物方向
    public static final int MON_DIR_LEFT = 0, MON_DIR_RIGHT = 1, MON_DIR_UP = 2, MON_DIR_DOWN = 3, MON_DIR_STOP = 4;
    public static final int MONSTER_COUNT = 10;


    private static final Random rd = new Random();



    //怪物列表
    private static Monster[] monsterList;
    private static JLabel[] monsterLabel;

    private static MyMap mapNow;
    private static final Rectangle knightBox = LittleKnight.getCrashBox();
    ;




    //怪物属性
    private String name;
    private int listNum;
    private int status;
    private double maxHealth;
    private double health;
    private double attack;
    private double speed;
    private double hateDistance;
    private double deHateDistance;
    private double backRate;


    //怪物图标
    private JLabel label;
      //非攻击时使用的图片，三个下标分别表示是否是右，是否被攻击，是否灵魂状态
    private ImageIcon[] iconNow;
    private ImageIcon[][][] strollIcon;
      //攻击时使用的图片，三个下标分别表示是否是右，攻击第几帧，是否灵魂状态
    private ImageIcon[][][] attackIcon;
    private ImageIcon[] deadIcon;
    private int iconWidth, iconHeight;
    private int deviationX, deviationY;


    //怪物移动
     //怪物相对于地图左上角的位置
    private int monsterX, monsterY;
    private int direction;
    private int xDirection;


    //怪物攻击
    private Rectangle crashBox;
    private int boxWidth, boxHeight;
    private int boxDevX, boxDevY;


    //控制各个控制器是否有权限更改图标，从上至下权限依次升高
    private boolean isMoveIconEnable;
    private boolean isHurtIconEnable;
    private boolean isAttackIconEnable;






    //初始化方法
    public Monster(int num) {
        this.listNum = num;
        this.label = monsterLabel[num];
        this.label.setVisible(false);
    }

    public static void init() {
        System.out.println("Monster -> init : creating new JLabels");
        monsterLabel = new JLabel[]{
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
                new JLabel(),
        };
    }

    public static void addWidget() {
        for(int i = 0; i < monsterList.length; i++) {
            GameMain.getGameMain().getContentPane().add(monsterLabel[i]);
        }
    }



    //事件方法
    public static void newMap(MyMap mapInput) {
        System.out.println("Monster -> newMap() : create monsters for a new map");
        Monster.mapNow = mapInput;


        monsterList = new Monster[] {
                create(0),
                create(1),
                create(2),
                create(3),
                create(4),
                create(5),
                create(6),
                create(7),
                create(8),
                create(9),
        };

        System.out.println("Monster -> newMap() : checking : " + monsterList.length + " " + monsterLabel.length);
        if(monsterList.length != monsterLabel.length){
            System.out.println("----------------------------------------");
            System.out.println("|   Monster -> newMap : count ERROR!   |");
            System.out.println("----------------------------------------");
        }
    }

    public static Monster create(int num) {
        int[][] rateList = mapNow.getMonstersRate();
        Monster newMonster = null;
        int monsterType = 0;

        //通过随机确定新生成的怪物种类
        for(int i = 0; i < rateList.length; i++) {
            if(rd.nextInt(100) < rateList[i][1]) {
                monsterType = i;
                break;
            }
        }

        //System.out.println("Monster -> create() : new monster, type : " + monsterType);
        switch (monsterType) {
            case 0 -> newMonster = new LifeSeed(num);
            case 1 -> newMonster = new SilverReptile(num);
            default -> {
                System.out.println("--------------------------------------------------");
                System.out.println("|    Monster : monster create -> class ERROR!    |");
                System.out.println("--------------------------------------------------");

            }
        }


        assert newMonster != null;
        newMonster.selfSetting();
        newMonster.locate();
        newMonster.label.setVisible(true);

        newMonster.stroll();
        return newMonster;
    }

    public void selfSetting() {
        this.health = this.maxHealth * Status.getDiffRate();
        this.attack = this.attack * Status.getDiffRate();

        this.iconWidth = this.strollIcon[0][0][0].getIconWidth();
        this.iconHeight = this.strollIcon[0][0][0].getIconHeight();

        this.deviationX = this.iconWidth / 2;
        this.deviationY = this.iconHeight / 2;

        this.iconNow = this.strollIcon[0][0];

        this.boxDevX = this.boxWidth / 2;
        this.boxDevY = this.boxHeight;
        this.crashBox = new Rectangle(
                MyMap.getMapX() + this.monsterX - this.boxDevX,
                MyMap.getMapY() + this.monsterY - this.boxDevY,
                this.boxWidth,
                this.boxHeight);

        this.isMoveIconEnable = true;
        this.isHurtIconEnable = true;
        this.isAttackIconEnable = true;
    }

    public void locate() {
        this.monsterX = (int)((double)mapNow.getMapWidth() * rd.nextDouble());
        this.monsterY = (int)((double)mapNow.getMapHeight() * rd.nextDouble());
        //System.out.println("Monster -> ready() : at : " + this.monsterX + " " + this.monsterY);

        this.label.setBounds(
                MyMap.getMapX() + this.monsterX - this.deviationX,
                MyMap.getMapY() + this.monsterY - this.deviationY,
                this.iconWidth,
                this.iconHeight);
    }

    public static void applyChanges() {
        for(int i = 0; i < monsterList.length; i++) {
            monsterList[i].applyChange();
        }
    }

    public void applyChange() {
        this.label.setLocation(
                MyMap.getMapX() + this.monsterX - this.deviationX,
                MyMap.getMapY() + this.monsterY - this.deviationY);


        this.crashBox.setLocation(
                MyMap.getMapX() + this.monsterX - this.boxDevX,
                MyMap.getMapY() + this.monsterY - this.boxDevY);


        if(LittleKnight.isSoul()) {
            this.label.setIcon(this.iconNow[1]);
        }else {
            this.label.setIcon(this.iconNow[0]);
        }
    }

    public static void checkCrash(){
        for(int i = 0; i < monsterList.length; i++) {
            if(knightBox.intersects(monsterList[i].crashBox) && monsterList[i].status != MONSTER_DEAD) {
                LittleKnight.underAttack(monsterList[i].attack, monsterList[i].monsterX, 50);
            }
        }
    }




    //怪物行为
    private int patrolRange;
    private int patrolDistance;


    //此处为怪物巡逻逻辑，只负责改变坐标值以及运动图标，具体的显示刷新由刷新器控制
    public void stroll() {
        int delay = Status.getFrameInterval();
        int step = (int)(this.getSpeed() * (delay + 2));


        new Thread(() -> {
            try {



                //线程主要工作部分
                while (status == MONSTER_STROLL){
                    this.direction = rd.nextInt(5);
                    //System.out.println("Monster -> stroll : hanging, direction : " + this.direction + ", step : " + step);
                    this.patrolDistance = this.patrolRange;


                    switch (this.direction) {
                        case MON_DIR_LEFT -> {
                            while(this.monsterX > 0 && this.patrolDistance > 0 && status == MONSTER_STROLL) {
                                if(isMoveIconEnable) {
                                    iconNow = strollIcon[MON_DIR_LEFT][0];
                                }
                                xDirection = MON_DIR_LEFT;
                                monsterX -= step;
                                this.patrolDistance -= step;
                                Thread.sleep(delay);
                            }
                        }

                        case MON_DIR_RIGHT -> {
                            while(this.monsterX < mapNow.getMapWidth() && this.patrolDistance > 0 && status == MONSTER_STROLL) {
                                if(isMoveIconEnable) {
                                    iconNow = strollIcon[MON_DIR_RIGHT][0];
                                }
                                xDirection = MON_DIR_RIGHT;
                                monsterX += step;
                                this.patrolDistance -= step;
                                Thread.sleep(delay);
                            }
                        }

                        case MON_DIR_UP -> {
                            while(this.monsterY > 0 && this.patrolDistance > 0 && status == MONSTER_STROLL) {
                                if(isMoveIconEnable) {
                                    iconNow = strollIcon[xDirection][0];
                                }
                                monsterY -= step;
                                this.patrolDistance -= step;
                                Thread.sleep(delay);
                            }
                        }

                        case MON_DIR_DOWN-> {
                            while(this.monsterY < mapNow.getMapHeight() && this.patrolDistance > 0 && status == MONSTER_STROLL) {
                                if(isMoveIconEnable) {
                                    iconNow = strollIcon[xDirection][0];
                                }
                                monsterY += step;
                                this.patrolDistance -= step;
                                Thread.sleep(delay);
                            }
                        }

                        case MON_DIR_STOP -> {
                            Thread.sleep(2000);
                        }
                    }
                    ifGetHate();
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void ifGetHate() {
        if(LittleKnight.getX() > this.monsterX - this.hateDistance && LittleKnight.getX() < this.monsterX + this.hateDistance) {
            this.status = MONSTER_HATING;
            attack();
            return;
        }
        if(LittleKnight.getY() > this.monsterY - this.hateDistance && LittleKnight.getY() < this.monsterY + this.hateDistance) {
            this.status = MONSTER_HATING;
            attack();
        }
    }

    public void ifDeHate() {
        if(LittleKnight.getX() < this.monsterX - this.deHateDistance || LittleKnight.getX() > this.monsterX + this.deHateDistance) {
            this.status = MONSTER_HATING;
            stroll();
            return;
        }
        if(LittleKnight.getY() < this.monsterY - this.deHateDistance || LittleKnight.getY() > this.monsterY + this.deHateDistance) {
            this.status = MONSTER_HATING;
            stroll();
        }
    }

    public static void checkHits(Rectangle sword, double attack) {
        for(int i = 0; i < monsterList.length; i++) {
            if(sword.intersects(monsterList[i].crashBox) && monsterList[i].status != MONSTER_DEAD) {
                monsterList[i].underAttack(attack, MyMap.getKnightX(), LittleKnight.getHitBack());
            }
        }
    }

    public void underAttack(double damage, int attackerX, int back) {
        if(this.health - damage < GameMain.DELTA) {
            this.dead();
        }else {
            //没有死亡则执行以下语句

            health -= damage;

            //击退
            if(monsterX < attackerX) {
                monsterX -= (int)((double)back * backRate);
            }else {
                monsterX += (int)((double)back * backRate);
            }

            if(monsterX < 0 || monsterX > mapNow.getMapWidth()){
                this.dead();
                return;
            }

            //图标变化
            if(isHurtIconEnable) {
                isMoveIconEnable = false;
                iconNow = strollIcon[xDirection][1];

                //受伤图片变更线程
                new Thread(() -> {
                    try {


                        //线程主要工作部分
                        Thread.sleep(150);
                        iconNow = strollIcon[xDirection][0];
                        isMoveIconEnable = true;


                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }

    public void dead(){
        System.out.println("Monster -> dead : dead monster : " + this.listNum);
        this.status = MONSTER_DEAD;
        this.isMoveIconEnable = false;
        this.isAttackIconEnable = false;
        this.iconNow = deadIcon;
        this.deadAction();

        //死亡图片变更及重生线程
        new Thread(() -> {
            try {


                //线程主要工作部分
                Thread.sleep(5000);

                //此处调用生成单独怪物的方法
                monsterList[this.listNum] = create(this.listNum);


            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public abstract void attack();
    public abstract void deadAction();










    //每个怪物自己属性的getter和setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getListNum() {
        return listNum;
    }

    public void setListNum(int listNum) {
        this.listNum = listNum;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getHateDistance() {
        return hateDistance;
    }

    public void setHateDistance(double hateDistance) {
        this.hateDistance = hateDistance;
    }

    public double getDeHateDistance() {
        return deHateDistance;
    }

    public void setDeHateDistance(double deHateDistance) {
        this.deHateDistance = deHateDistance;
    }

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public ImageIcon[][][] getStrollIcon() {
        return strollIcon;
    }

    public void setStrollIcon(ImageIcon[][][] strollIcon) {
        this.strollIcon = strollIcon;
    }

    public ImageIcon[][][] getAttackIcon() {
        return attackIcon;
    }

    public ImageIcon[] getDeadIcon() {
        return deadIcon;
    }

    public void setDeadIcon(ImageIcon[] deadIcon) {
        this.deadIcon = deadIcon;
    }

    public void setAttackIcon(ImageIcon[][][] attackIcon) {
        this.attackIcon = attackIcon;
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public void setIconWidth(int iconWidth) {
        this.iconWidth = iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public void setIconHeight(int iconHeight) {
        this.iconHeight = iconHeight;
    }

    public int getMonsterX() {
        return monsterX;
    }

    public void setMonsterX(int monsterX) {
        this.monsterX = monsterX;
    }

    public int getMonsterY() {
        return monsterY;
    }

    public void setMonsterY(int monsterY) {
        this.monsterY = monsterY;
    }

    public int getPatrolRange() {
        return patrolRange;
    }

    public void setPatrolRange(int patrolRange) {
        this.patrolRange = patrolRange;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ImageIcon[] getIconNow() {
        return iconNow;
    }

    public void setIconNow(ImageIcon[] iconNow) {
        this.iconNow = iconNow;
    }

    public double getBackRate() {
        return backRate;
    }

    public void setBackRate(double backRate) {
        this.backRate = backRate;
    }

    public boolean isMoveIconEnable() {
        return isMoveIconEnable;
    }

    public void setMoveIconEnable(boolean moveIconEnable) {
        isMoveIconEnable = moveIconEnable;
    }

    public boolean isAttackIconEnable() {
        return isAttackIconEnable;
    }

    public void setAttackIconEnable(boolean attackIconEnable) {
        isAttackIconEnable = attackIconEnable;
    }

    public int getDeviationX() {
        return deviationX;
    }

    public void setDeviationX(int deviationX) {
        this.deviationX = deviationX;
    }

    public int getDeviationY() {
        return deviationY;
    }

    public void setDeviationY(int deviationY) {
        this.deviationY = deviationY;
    }

    public Rectangle getCrashBox() {
        return crashBox;
    }

    public void setCrashBox(Rectangle crashBox) {
        this.crashBox = crashBox;
    }

    public void setBoxWidth(int boxWidth) {
        this.boxWidth = boxWidth;
    }

    public void setBoxHeight(int boxHeight) {
        this.boxHeight = boxHeight;
    }
}
