package Model;

public class ProcesManualni extends Proces{
    private int pocetLide;
    public ProcesManualni(String id,int pocetLide,int cas) {
        super(id, cas);
        this.pocetLide = pocetLide;
    }

    public int getPocetLide() {
        return pocetLide;
    }

    public void setPocetLide(int pocetLide) {
        this.pocetLide = pocetLide;
    }

    @Override
    public String toString() {
        return "ProcesManualni: " + super.toString() +
                ", pocetLide = " + pocetLide;
    }
}
