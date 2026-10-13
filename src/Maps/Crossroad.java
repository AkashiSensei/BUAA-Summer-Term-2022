package Maps;


public class Crossroad extends MyMap {
    public Crossroad() {
        this.setName("CrossRoad");
        this.setPath(".//img//Scene//Map_Crossroad.png");
        this.setNum(0);
        this.setMapWidth(4000);
        this.setMapHeight(4000);


        this.setMonstersRate(new int[][]{
                {0, 50},
                {1, 100}
        });
        this.setBoss(0);


        this.setGate0X(-1000);
        this.setGate0Y(-1000);
        this.setBenchX(2000);
        this.setBenchY(2000);
        this.setGate1X(3500);
        this.setGate1Y(3500);
    }
}
