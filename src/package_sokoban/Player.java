package package_sokoban;

public class Player extends Element{
    private boolean on_target;

    public Player(boolean target) {
        super('A',true);
        setOn_target(target);
    }
    public boolean isOn_target() {
        if(on_target){
            return on_target;
        }
        else{
            return false;
        }
    }
    public void setOn_target(boolean on_target) {
        if(isOn_target()==on_target){
            return;
        }else{
            this.on_target=on_target;
            if(on_target==true){
                setSign('a');
            }
            else{
                setSign('A');
            }
        }
    }

    
}
