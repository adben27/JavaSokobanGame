package package_sokoban;

public class Box extends Element{
	
    public Box(boolean on_target, char sign) {
        super(sign,true,on_target, (char) (sign+32));
        this.on_target=on_target;
        super.setOn_target(on_target);
    }

    public Box(boolean on_target) {
        super('B',true,on_target, 'b');
        this.on_target=on_target;
        super.setOn_target(on_target);
    }
}