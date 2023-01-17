package package_sokoban;

public abstract class Element {
    

    private char sign;
    private boolean moveable;
    
    public Element(char sign, boolean moveable) {
        this.sign = sign;
        this.moveable = moveable;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Element other = (Element) obj;
        if (sign != other.sign)
            return false;
        if (moveable != other.moveable)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + sign;
        result = prime * result + (moveable ? 1231 : 1237);
        return result;
    }

    @Override
    public String toString() {
        return "Element [sign=" + sign + "]";
    }
}