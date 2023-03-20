package typy;

public enum TypyProcesu {
    MANUALNI("Manualni proces"),
    ROBOTICKY("Roboticky proces");

    private String nazev;
    TypyProcesu(String nazev) {
        this.nazev=nazev;
    }

    @Override
    public String toString() {
        return nazev;
    }
}
