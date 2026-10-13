package Monsters;

import Player.LittleKnight;

import javax.swing.*;
import java.awt.*;

public class SilverReptile extends Monster{
    public SilverReptile(int num) {
        super(num);

        //内部属性
        this.setName("SilverReptile");

        //游戏属性
        this.setMaxHealth(20.0);
        this.setAttack(5.0);
        this.setSpeed(150.0 / 1000.0);
        this.setBackRate(1.0);

        //仇恨属性
        this.setPatrolRange(500);
        this.setHateDistance(-1);
        this.setDeHateDistance(9999);




        //移动图片
        this.setStrollIcon(new ImageIcon[][][]{{
                {new ImageIcon(".//img//Monster//SilverReptile_0_0_0.png"), new ImageIcon(".//img//Monster//SilverReptile_0_0_1.png")},
                {new ImageIcon(".//img//Monster//SilverReptile_0_1_0.png"), new ImageIcon(".//img//Monster//SilverReptile_0_1_1.png")}
        }, {
                {new ImageIcon(".//img//Monster//SilverReptile_1_0_0.png"), new ImageIcon(".//img//Monster//SilverReptile_1_0_1.png")},
                {new ImageIcon(".//img//Monster//SilverReptile_1_1_0.png"), new ImageIcon(".//img//Monster//SilverReptile_1_1_1.png")}
        }});

        //攻击图片
        this.setAttackIcon(null);

        //死亡图片
        this.setDeadIcon(new ImageIcon[]{
                new ImageIcon(".//img//Monster//SilverReptile_Dead.png"),
                new ImageIcon(".//img//Monster//SilverReptile_Dead1.png")
        });




        //碰撞箱
        this.setBoxWidth(50);
        this.setBoxHeight(40);
    }


    @Override
    public void attack() {
        this.ifDeHate();
    }

    @Override
    public void deadAction() {
        LittleKnight.moneyAdd(5);
    }
}
