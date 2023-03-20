package Model;

public abstract class Proces {
    private String id;
    private int cas;

    public Proces(String id, int cas) {
        this.id = id;
        this.cas = cas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCas() {
        return cas;
    }

    public void setCas(int cas) {
        this.cas = cas;
    }

    @Override
    public String toString() {
        return "id = "+ id + ", cas = " + cas;
    }
}
