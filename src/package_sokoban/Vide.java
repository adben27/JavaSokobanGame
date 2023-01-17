package package_sokoban;

public class Vide extends Element{
    private boolean target;
    
    public Vide(boolean target) {
        super(' ',false);
        this.target=target;
    }
    public boolean isTarget() {
        return target;
    }

    public void setTarget(boolean target) {
        this.target = target;
    }

}
