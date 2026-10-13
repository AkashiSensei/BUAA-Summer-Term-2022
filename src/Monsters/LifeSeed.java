package Monsters;

import Player.LittleKnight;

import javax.swing.*;

public class LifeSeed extends Monster{
    public LifeSeed(int num) {
        super(num);

        //内部属性
        this.setName("LifeSeed");

        //游戏属性
        this.setMaxHealth(10);
        this.setAttack(1.0);
        this.setSpeed(600.0 / 1000.0);
        this.setBackRate(2.0);

        //仇恨属性
        this.setPatrolRange(200);
        this.setHateDistance(-1);
        this.setDeHateDistance(9999);




        //移动图片
        this.setStrollIcon(new ImageIcon[][][]{{
                {new ImageIcon(".//img//Monster//LifeSeed_0_0_0.png"), new ImageIcon(".//img//Monster//LifeSeed_0_0_1.png")},
                {new ImageIcon(".//img//Monster//LifeSeed_0_1_0.png"), new ImageIcon(".//img//Monster//LifeSeed_0_1_1.png")}
        }, {
                {new ImageIcon(".//img//Monster//LifeSeed_1_0_0.png"), new ImageIcon(".//img//Monster//LifeSeed_1_0_1.png")},
                {new ImageIcon(".//img//Monster//LifeSeed_1_1_0.png"), new ImageIcon(".//img//Monster//LifeSeed_1_1_1.png")}
        }});

        //攻击图片
        this.setAttackIcon(null);

        //死亡图片
        this.setDeadIcon(new ImageIcon[]{
                new ImageIcon(".//img//Monster//LifeSeed_Dead.png"),
                new ImageIcon(".//img//Monster//LifeSeed_Dead.png")
        });




        //碰撞箱
        this.setBoxWidth(40);
        this.setBoxHeight(40);
    }


    @Override
    public void attack() {
        this.ifDeHate();
    }

    @Override
    public void deadAction() {
        LittleKnight.maxHealthAdd(1);
        LittleKnight.healthAdd(10);
        LittleKnight.moneyAdd(1);
    }
}
