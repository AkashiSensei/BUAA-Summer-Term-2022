package Maps;


public class Greenpath extends MyMap {
    public Greenpath() {
        this.setName("Greenpath");
        this.setPath(".//img//Scene//Map_Greenpath.png");
        this.setNum(1);
        this.setMapWidth(6000);
        this.setMapHeight(6000);


        this.setMonstersRate(new int[][]{
                {0, 25},
                {1, 100}
        });
        this.setBoss(0);


        this.setGate0X(3000);
        this.setGate0Y(3000);
        this.setBenchX(1250);
        this.setBenchY(4750);
        this.setGate1X(5000);
        this.setGate1Y(1000);
    }
}
