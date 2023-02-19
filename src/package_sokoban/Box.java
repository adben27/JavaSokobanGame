package package_sokoban;

public class Vide extends Element{
    
    public Vide(boolean on_target) {
        super(' ',true,on_target,'@');
        this.on_target=on_target;
        super.setOn_target(on_target);
    }
}
