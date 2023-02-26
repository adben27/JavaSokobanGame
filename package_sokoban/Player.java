package package_sokoban;

public class Player extends Element{

    public Player(boolean on_target) {
        super('A',true,on_target,'a');
        this.on_target=on_target;
        super.setOn_target(on_target);
    }
    
}
