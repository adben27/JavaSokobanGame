package package_sokoban;

import java.util.Objects;

public class Element {
    

    private char sign, altsign1, altsign2;
    private boolean moveable;
    protected boolean on_target;

    
    
    public Element(char sign, boolean moveable, boolean on_target, char altsign) {
    	if(on_target)
        this.sign=altsign;
    	else
    	this.sign=sign;
    	this.altsign1=sign;
        this.altsign2=altsign;
        this.moveable=moveable;
        this.on_target=on_target;
        setOn_target(on_target);
        
    }
    
    public void setOn_target(boolean on_target) {
    	if(!isMoveable()) {
    		return;
    	}
        if(isOn_target()==on_target){
            return;
        }else{
            this.on_target=on_target;
            if(on_target==true){
                setSign(altsign2);
            }
            else{
                setSign(altsign1);
            }
        }
    }
    
    public boolean isOn_target() {
        return on_target;
    }
    
    public char getSign(){
        return sign;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

	@Override
	public int hashCode() {
		return Objects.hash(moveable, on_target, sign);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Element other = (Element) obj;
		return moveable == other.moveable && on_target == other.on_target && sign == other.sign;
	}

	@Override
	public String toString() {
		return "Element [sign=" + sign + "]";
	}

	
    
}