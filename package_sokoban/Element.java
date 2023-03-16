package package_sokoban;

import java.util.Objects;
import java.util.HashMap;


public class Element{
    

    private char sign, altsign1, altsign2;
    private boolean moveable;
    protected boolean on_target;

    public Element(){
        
    }
    
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

	public static Element[][] remplirTableauGrille(String sousMap, int size, HashMap<String,Matrice> mondes,String nom){
        Element[][] grille=new Element[size][size];
        Element[] elements=caracelement(sousMap,mondes);        
        int z=0;
        for(int i=0;i<size;i++){
            for(int j=0;j<size;j++){
                grille[i][j]=elements[z++];
                if(grille[i][j].getSign() == 'A'){
                    mondes.get(nom).setPos_x(i);
                    mondes.get(nom).setPos_y(j);
                }
            }
        }
        return grille;
    }

    public static Element[] caracelement(String sousMap,HashMap<String,Matrice> mondes){
        char[] caracteres= sousMap.toCharArray();
        Element[] elements= new Element[sousMap.length()];
        for(int i=0;i<elements.length;i++){
            switch(caracteres[i]){
                case '#':   elements[i]=new Wall();
                            break;
                case '@':   elements[i]=new Vide(true);    
                            break;                       
                case 'A':   elements[i]=new Player(false);
                            break;                          
                case 'a':   elements[i]=new Player(true);
                            break;                           
                case ' ':   elements[i]=new Vide(false);
                            break;
                case 'B':   elements[i]=new Box(false);
                            break;
                case 'b':   elements[i]=new Box(true);
                            break;
                default :   elements[i]=mondes.get(String.valueOf(caracteres[i]));  
            }
        }
        return elements;
    }

    public static void afficherElements(Element[][] elements){
        for(int i=0;i<elements.length;i++){
            for(int j=0;j<elements.length;j++){
                System.out.print(elements[i][j].getSign() + " ");
            }
            System.out.println();
        }
    }
}