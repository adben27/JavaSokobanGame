package package_sokoban;

public class Box extends Element{
	
    public Box(boolean on_target, char name) {
        super(name,true,on_target, (char)(name + 32));
        this.on_target=on_target;
        super.setOn_target(on_target);
    }
}
