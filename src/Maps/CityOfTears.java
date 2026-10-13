package Maps;


public class CityOfTears extends MyMap {
    public CityOfTears() {
        this.setName("CityOfTears");
        this.setPath(".//img//Scene//Map_CityOfTears.png");
        this.setNum(2);
        this.setMapWidth(8000);
        this.setMapHeight(1000);


        this.setMonstersRate(new int[][]{
                {0, 5},
                {1, 100}
        });
        this.setBoss(0);


        this.setGate0X(500);
        this.setGate0Y(500);
        this.setBenchX(4000);
        this.setBenchY(500);
        this.setGate1X(-1000);
        this.setGate1Y(-1000);
    }
}
