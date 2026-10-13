package Player;

import GameStatus.Status;
import Maps.MyMap;
import Monsters.Monster;
import Windows.GameMain;

import javax.swing.*;
import java.awt.*;

public class LittleKnight {
    //移动方向
    public static final int DIR_FRONT = 0, DIR_LEFT = 1, DIR_RIGHT = 2, DIR_BACK = 3;
    //攻击方向
    public static final int ATTACK_LEFT = 0, ATTACK_RIGHT = 1;
    //是否处于灵魂装填
    public static final int NOT_SOUL = 0, IS_SOUL = 1;


    //小骑士图片标签
    private static JLabel knightLabel;
    private static JLabel soulLabel;
    private static JLabel hurtLabel;

    //小骑士状态栏标签
    private static JLabel barImgLabel;
    private static JLabel healthLabel;
    private static JLabel moneyLabel;
    private static JLabel healthMaskLabel;

    //小骑士图片源
    private static ImageIcon[] standIcon;
    private static ImageIcon[][] attackIcon;
    private static ImageIcon[] barIcon;
    private static ImageIcon[] hurtIcon;
    private static ImageIcon[] soulIcon;
    private static ImageIcon[] soulAttackIcon;
    private static ImageIcon[] healthMaskIcon;

    //小骑士当前图片
    private static ImageIcon iconNow;


    //小骑士属性
    private static double maxHealth;
    private static double health;
    private static double attack;
    private static int hitBack;
    private static int money;
    private static double speed = 400.0 / 1000.0;
    private static int soulTime;



    //显示位置
    private static final int x = GameMain.centreX - 150;
    private static final int y = GameMain.centreY - 150;
    private static final int boxWidth = 36;
    private static final int boxHeight = 60;
    //在摄像机模式下就不进行碰撞检测了
    private static Rectangle crashBox;


    //小骑士移动状态
    private static boolean isMoveIconChangeDisable;
    private static boolean isMoving;
    private static boolean isLeft;
    private static boolean isRight;
    private static boolean isFront;
    private static boolean isBack;
    private static int direction;

    //小骑士攻击状态
    private static int attackDir;
    private static boolean isAttackAble;
    private static Rectangle[] attackCase;
    private static Rectangle soulCase;

    //小骑士灵魂模式状态
    private static boolean canSoul;
    private static boolean isSoul;
    private static boolean isSoulAttack;
    private static boolean isInvincible;





    //初始化方法
    public static void init() {
        //读入图片
        standIcon = new ImageIcon[]{
                new ImageIcon(".//img//Little_Knight//Front.png"),
                new ImageIcon(".//img//Little_Knight//Left.png"),
                new ImageIcon(".//img//Little_Knight//Right.png"),
                new ImageIcon(".//img//Little_Knight//Back.png"),
        };

        attackIcon = new ImageIcon[][]{{
                new ImageIcon(".//img//Little_Knight//Left_Attack_0.png"),
                new ImageIcon(".//img//Little_Knight//Left_Attack_1.png"),
                new ImageIcon(".//img//Little_Knight//Left_Attack_2.png")
        }, {
                new ImageIcon(".//img//Little_Knight//Right_Attack_0.png"),
                new ImageIcon(".//img//Little_Knight//Right_Attack_1.png"),
                new ImageIcon(".//img//Little_Knight//Right_Attack_2.png")
        }};

        barIcon = new ImageIcon[]{
                new ImageIcon(".//img//Little_Knight//Bar_Normal.png"),
                new ImageIcon(".//img//Little_Knight//Bar_Soul.png"),
                new ImageIcon(".//img//Little_Knight//Bar_Ready0.png"),
                new ImageIcon(".//img//Little_Knight//Bar_Ready1.png"),
                new ImageIcon(".//img//Little_Knight//Bar_Break.png")
        };

        hurtIcon = new ImageIcon[]{
                new ImageIcon(".//img//Little_Knight//Little_Knight_Hurt_0.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Hurt_1.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Hurt_2.png")
        };

        soulIcon = new ImageIcon[]{
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_Left.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_Right.png")
        };

        soulAttackIcon = new ImageIcon[] {
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_0.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_1.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_2.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_3.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_4.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_5.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_6.png"),
                new ImageIcon(".//img//Little_Knight//Little_Knight_Soul_7.png")
        };

        healthMaskIcon = new ImageIcon[] {
                new ImageIcon(new ImageIcon(".//img//Little_Knight//healthMask_0.png").getImage().getScaledInstance(Status.getWinWidth(), Status.getWinHeight(), Image.SCALE_FAST)),
                new ImageIcon(new ImageIcon(".//img//Little_Knight//healthMask_Dead.png").getImage().getScaledInstance(Status.getWinWidth(), Status.getWinHeight(), Image.SCALE_FAST)),
        };




        //设置血量等信息
        if(Status.getDifficulty() == Status.DIFF_EXTRA) {
            maxHealth = 1.0;
        }else {
            maxHealth = 50.0;
        }
        health = maxHealth;
        money = 0;

        attack = 9.0;
        hitBack = 80;

        soulTime = 5000;



        //初始化攻击判定框
        attackCase = new Rectangle[] {
                new Rectangle(GameMain.centreX - 145, GameMain.centreY - 88, 218, 88),
                new Rectangle(GameMain.centreX - 72, GameMain.centreY - 88, 218, 88)
        };
        isAttackAble = true;
        attackDir = ATTACK_LEFT;


        //初始化碰撞箱
        crashBox = new Rectangle(GameMain.centreX - boxWidth / 2, GameMain.centreY - boxHeight, boxWidth, boxHeight);


        //初始化状态栏
        Font textFont = new Font("宋体", Font.BOLD, 30);

        barImgLabel = new JLabel(barIcon[0]);
        barImgLabel.setBounds(20, 20, barIcon[0].getIconWidth(), barIcon[0].getIconHeight());

        healthLabel = new JLabel();
        healthLabel.setBounds(225, 78, 300, 40);
        healthLabel.setText(String.valueOf((int)health) + "/" + String.valueOf((int)maxHealth));
        healthLabel.setForeground(Color.white);
        healthLabel.setHorizontalAlignment(SwingConstants.LEFT);
        healthLabel.setFont(textFont);

        moneyLabel = new JLabel();
        moneyLabel.setBounds(225, 115, 300, 40);
        moneyLabel.setText(String.valueOf(money));
        moneyLabel.setForeground(Color.white);
        moneyLabel.setHorizontalAlignment(SwingConstants.LEFT);
        moneyLabel.setFont(textFont);


        //初始化健康蒙版
        healthMaskLabel = new JLabel();
        healthMaskLabel.setVisible(false);
        healthMaskLabel.setIcon(healthMaskIcon[0]);
        healthMaskLabel.setBounds(0, 0, healthMaskIcon[0].getIconWidth(), healthMaskIcon[0].getIconHeight());





        //初始化受伤相关
        isInvincible = false;
        hurtLabel = new JLabel(hurtIcon[0]);
        hurtLabel.setBounds(GameMain.centreX - 200, GameMain.centreY - 200, 400, 400);
        hurtLabel.setVisible(false);


        //初始化灵魂相关
        soulCase = new Rectangle(GameMain.centreX - 160, GameMain.centreY - 260, 320, 260);
        isSoul = false;
        isSoulAttack = false;
        canSoul = true;
        soulLabel = new JLabel();
        soulLabel.setVisible(false);
        soulLabel.setIcon(soulIcon[attackDir]);
        soulLabel.setBounds(GameMain.centreX - 250, GameMain.centreY - 400, 500, 500);

        //设置主图标
        direction = DIR_FRONT;
        iconNow = standIcon[direction];
        knightLabel = new JLabel(iconNow);
        knightLabel.setBounds(x, y, 300, 300);
    }

    public static void addWidget() {
        GameMain.getGameMain().getContentPane().add(barImgLabel);
        GameMain.getGameMain().getContentPane().add(hurtLabel);
        GameMain.getGameMain().getContentPane().add(soulLabel);
        GameMain.getGameMain().getContentPane().add(healthMaskLabel);


        GameMain.getGameMain().getContentPane().add(knightLabel);
        GameMain.getGameMain().getContentPane().add(healthLabel);
        GameMain.getGameMain().getContentPane().add(moneyLabel);
    }




    //事件方法
    public static void startSoul() {
        canSoul = false;
        isSoul = true;
        isInvincible = true;
        isAttackAble = false;

        soulLabel.setIcon(soulIcon[attackDir]);
        barImgLabel.setIcon(barIcon[1]);
        knightLabel.setVisible(false);
        soulLabel.setVisible(true);

        new Thread(() -> {
            try {


                //线程主要工作部分
                Thread.sleep(soulTime);
                if(isSoul && !isSoulAttack) {
                    endSoul();
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void endSoul() {
        isSoul = false;
        isInvincible = false;
        isAttackAble = true;
        knightLabel.setVisible(true);

        soulLabel.setIcon(soulAttackIcon[6]);
        barImgLabel.setIcon(barIcon[2]);
        new Thread(() -> {
            try {


                //线程主要工作部分
                Thread.sleep(7000);

                if(Status.getGame() == Status.GAMING) {
                    soulLabel.setIcon(soulAttackIcon[7]);
                    barImgLabel.setIcon(barIcon[3]);
                    Thread.sleep(3000);
                }


                if(Status.getGame() == Status.GAMING){
                    soulLabel.setVisible(false);
                    barImgLabel.setIcon(barIcon[0]);
                    canSoul = true;
                }


            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void soulAttack() {
        isSoulAttack = true;
        soulLabel.setIcon(soulAttackIcon[0]);
        new Thread(() -> {
            try {


                //线程主要工作部分
                Thread.sleep(80);

                soulLabel.setIcon(soulAttackIcon[1]);
                Thread.sleep(80);

                soulLabel.setIcon(soulAttackIcon[2]);
                Monster.checkHits(soulCase, attack * 3);
                Thread.sleep(80);

                soulLabel.setIcon(soulAttackIcon[3]);
                Thread.sleep(300);

                soulLabel.setIcon(soulAttackIcon[4]);
                Thread.sleep(120);

                soulLabel.setIcon(soulAttackIcon[5]);
                Thread.sleep(120);

                isSoulAttack = false;
                endSoul();


            }catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void soulIconChange() {
        soulLabel.setIcon(soulIcon[attackDir]);
    }

    //在移动途中改变小骑士图标
    public static void changeMoveIcon() {
        if(isSoul && !isSoulAttack){
            soulIconChange();
        }
        if(isMoveIconChangeDisable)
            return;

        if(isLeft) {
            isMoving = true;
            direction = DIR_LEFT;
        }else if(isRight) {
            isMoving = true;
            direction = DIR_RIGHT;
        }else if(isFront) {
            isMoving = true;
            direction = DIR_FRONT;
        }else if(isBack) {
            isMoving = true;
            direction = DIR_BACK;
        }else {
            isMoving = false;
        }
        iconNow = standIcon[direction];
    }

    public static void applyIcon() {
        knightLabel.setIcon(iconNow);
    }

    public static void changeLocation() {
        knightLabel.setLocation(x - Status.getCameraX(), y - Status.getCameraY());
    }

    public static void attack() {
        //System.out.println("LittleKnight : attack!!!");

        new Thread(() -> {
            //设定当前攻击状态为不可开始攻击，同时拒绝移动图标控制方法控制图标
            isAttackAble = false;
            isMoveIconChangeDisable = true;

            //延时修改图片
            try {
                iconNow = attackIcon[attackDir][0];
                Thread.sleep(80);
                iconNow = attackIcon[attackDir][1];
                Thread.sleep(120);
                iconNow = attackIcon[attackDir][2];
                Thread.sleep(240);
            }catch (Exception e) {
                e.printStackTrace();
            }

            //此处不再需要将图标设置为原来的图标，只需要通过取消disable将控制权交还给移动图标控制器即可
            /*
            //攻击结束时根据此时朝向选定图标
            if(attackDir == ATTACK_LEFT) {
                iconNow = standIcon[DIR_LEFT];
            }else {
                iconNow = standIcon[DIR_RIGHT];
            }
             */

            //可以开始下一次攻击，同时允许移动图标控制方法控制图标
            isAttackAble = true;
            isMoveIconChangeDisable = false;
        }).start();

        Monster.checkHits(attackCase[attackDir], attack);
    }

    //注意这里面攻击者的x位置是相对于地图左上角的x值
    public static void underAttack(double damage, int attackerX, int back) {
        if(isInvincible){
            return;
        }


        //受伤方法，包含死亡判定以及被击退
        if(health - damage < GameMain.DELTA) {
            dead();
        }else {
            healthAdd(-damage);


            if(attackerX > MyMap.getKnightX()) {
                MyMap.setKnightX(MyMap.getKnightX() - back);
            }else {
                MyMap.setKnightX(MyMap.getKnightX() + back);
            }


            hurtLabel.setIcon(hurtIcon[0]);
            hurtLabel.setVisible(true);
            isInvincible = true;
            new Thread(() -> {
                try {


                    //线程主要工作部分
                    Thread.sleep(100);


                    hurtLabel.setIcon(hurtIcon[1]);
                    Thread.sleep(600);


                    hurtLabel.setIcon(hurtIcon[2]);
                    Thread.sleep(80);


                    isInvincible = false;
                    hurtLabel.setVisible(false);





                }catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }


    public static void maxHealthAdd(double change) {
        maxHealth += change;
        if(health < maxHealth * 0.2) {
            healthMaskLabel.setIcon(healthMaskIcon[0]);
            healthMaskLabel.setVisible(true);
        }else {
            healthMaskLabel.setVisible(false);
        }
        healthLabel.setText(String.valueOf((int)health) + "/" + String.valueOf((int)maxHealth));
    }

    public static void healthAdd(double change) {
        if(health + change > maxHealth) {
            health = maxHealth;
        }else {
            health += change;
        }

        if(health < maxHealth * 0.2) {
            healthMaskLabel.setIcon(healthMaskIcon[0]);
            healthMaskLabel.setVisible(true);
        }else {
            healthMaskLabel.setVisible(false);
        }
        healthLabel.setText(String.valueOf((int)health) + "/" + String.valueOf((int)maxHealth));
    }

    public static void moneyAdd(int change) {
        money += change;
        moneyLabel.setText(String.valueOf(money));
    }

    public static void dead() {
        isMoving = false;
        isLeft = false;
        isRight = false;
        isFront = false;
        isBack = false;
        healthLabel.setText("0" + "/" + String.valueOf((int)maxHealth));
        healthMaskLabel.setIcon(healthMaskIcon[1]);
        healthMaskLabel.setVisible(true);
        barImgLabel.setIcon(barIcon[4]);

        GameMain.playerDead();
        //TODO:此处添加小骑士死亡动画效果，逻辑部分在GameMain中
    }


    public static void reset() {
        isMoving = false;
        isLeft = false;
        isRight = false;
        isFront = false;
        isBack = false;
        isSoul = false;
        isSoulAttack = false;
        canSoul = true;
    }

    public static void revive() {
        health = maxHealth;
        healthLabel.setText(String.valueOf((int)health) + "/" + String.valueOf((int)maxHealth));
        healthMaskLabel.setVisible(false);
        money /= 2;
        moneyLabel.setText(String.valueOf(money));

        canSoul = false;
        endSoul();
    }






    //getter和setter
    //小骑士移动状态的getter和setter
    public static boolean isLeft() {
        return isLeft;
    }

    public static void setIsMoveIconChangeDisable(boolean isMoveIconChangeDisable) {
        LittleKnight.isMoveIconChangeDisable = isMoveIconChangeDisable;
    }

    public static void setLeft(boolean left) {
        isLeft = left;
    }

    public static boolean isRight() {
        return isRight;
    }

    public static void setRight(boolean right) {
        isRight = right;
    }

    public static boolean isFront() {
        return isFront;
    }

    public static void setFront(boolean front) {
        isFront = front;
    }

    public static boolean isBack() {
        return isBack;
    }

    public static void setBack(boolean back) {
        isBack = back;
    }

    public static int getX() {
        return x;
    }

    public static int getY() {
        return y;
    }

    public static boolean isMoving() {
        return isMoving;
    }

    public static void setMoving(boolean moving) {
        isMoving = moving;
    }

    public static Rectangle getCrashBox() {
        return crashBox;
    }

    public static int getHitBack() {
        return hitBack;
    }

    public static void setHitBack(int hitBack) {
        LittleKnight.hitBack = hitBack;
    }

    //小骑士属性的getter和setter
    public static double getMaxHealth() {
        return maxHealth;
    }

    public static void setMaxHealth(double maxHealth) {
        LittleKnight.maxHealth = maxHealth;
    }

    public static double getHealth() {
        return health;
    }

    public static void setHealth(double health) {
        LittleKnight.health = health;
    }

    public static double getAttack() {
        return attack;
    }

    public static void setAttack(double attack) {
        LittleKnight.attack = attack;
    }

    public static double getSpeed() {
        return speed;
    }

    public static void setSpeed(double speed) {
        LittleKnight.speed = speed;
    }

    public static int getMoney() {
        return money;
    }

    public static void setMoney(int money) {
        LittleKnight.money = money;
    }

    //小骑士攻击状态的getter和setter
    public static boolean isAttackAble() {
        return isAttackAble;
    }

    public static void setAttackAble(boolean isAttackAble) {
        LittleKnight.isAttackAble = isAttackAble;
    }

    public static void setAttackDir(int attackDir) {
        LittleKnight.attackDir = attackDir;
    }

    public static void setDirection(int direction) {
        LittleKnight.direction = direction;
    }

    //小骑士灵魂状态的getter和setter
    public static boolean isSoul() {
        return isSoul;
    }

    public static void setSoul(boolean isSoul) {
        LittleKnight.isSoul = isSoul;
    }

    public static boolean isCanSoul() {
        return canSoul;
    }

    public static boolean isSoulAttack() {
        return isSoulAttack;
    }
}