package package_sokoban;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Objects;


/**
 * pour l'instant la grille de la matrice sera un tableau de tableau puis après je crée un autre classe grille qui va implémenter la classe matrix de java
 * pos_x et pos_y sont la position du player je vais voir aprés si ya une meilleur facons de le faire
 */

public class Matrice{
    private String name;
    private int size;
    private int pos_x;
    private int pos_y;
	private Element[][] level;

    public Matrice(){
        this.name="";
        this.size=0;
        this.level=new Element[0][0];
        this.pos_x=0;
        this.pos_y=0;
    }

    public Matrice(String name, int size, Element[][] level, int x, int y){
        this.name=name;
        this.size=size;
        this.level=level;
        this.pos_x=x;
        this.pos_y=y;
    }

    /**
     * permet d'échanger les elements de deux cellules
     * si l'un des elements est le player actualise les parametres pos_x et pos_y de la matrice
     * @param i pos_x de la cellule 1
     * @param j pos_y de la cellule 1
     * @param a pos_x de la cellule 2
     * @param b pos_y de la cellule 2
     */
    public void swap(int i, int j, int a, int b){
        Element temp = getElem(i, j);
        if(!getElem(i,j).isMoveable()||!getElem(a,b).isMoveable()) {
        	return;/*faut ajouter un catch d'erreur ici*/
        }
        if(getElem(i,j) instanceof Player){/*il ne peut pas y avoir deux joueurs normalement faudrait ajouter un test peut être apres*/
            setPos_x(a);
            setPos_y(b);
        }
        if(getElem(a,b) instanceof Player){
            setPos_x(i);
            setPos_y(j);
        }
        if(getElem(i,j).isOn_target()||getElem(a,b).isOn_target()) {
        	getElem(i,j).setOn_target(!getElem(i,j).isOn_target());
        	getElem(a,b).setOn_target(!getElem(a,b).isOn_target());
        }
        setElem(i, j,getElem(a, b));
        setElem(a, b, temp);
        return;
    }
    /*fonction qui prend comme argument un char qui représente la direction du mouvement z : haut, q : gauche, s : bas, d : droite, autre que ceux la il
     *return false d'ailleurs j'en profite pour modifier les autres fonctions move_direction pour qu'elles utilisent celle-ci pour check le mouvement
     */
	public boolean can_move(char c) {
		if (Character.compare(c, 'z') == 0) {
			return can_move_up();
		}
		if (Character.compare(c, 's') == 0) {
			return can_move_down();
		}
		if (Character.compare(c, 'q') == 0) {
			return can_move_left();
		}
		if (Character.compare(c, 'd') == 0) {
			return can_move_right();
		}
		return false;
	}

	public boolean can_move_up() {
		if (this.getElem(getPos_x() - 1, getPos_y()) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(getPos_x() - 1, getPos_y()) instanceof Box) {
				if (this.getElem(getPos_x() - 2, getPos_y()) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}
    
	public boolean can_move_down() {
		if (this.getElem(getPos_x() + 1, getPos_y()) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(getPos_x() + 1, getPos_y()) instanceof Box) {
				if (this.getElem(getPos_x() + 2, getPos_y()) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}
    
	public boolean can_move_right() {
		if (this.getElem(getPos_x(), getPos_y() + 1) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(getPos_x(), getPos_y() + 1) instanceof Box) {
				if (this.getElem(getPos_x(), getPos_y() + 2) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			return true;
		}
	}
	
	public boolean can_move_left() {
		if(this.getElem(getPos_x(),getPos_y()-1) instanceof Wall){
			return false;
        }else{
            if(this.getElem(getPos_x(),getPos_y()-1) instanceof Box){
                if(this.getElem(getPos_x(),getPos_y()-2) instanceof Vide){
                    return true;
                }else{
                    return false;
                }
            }
            return true;
        }
    }
    
    public void move(){
            Scanner console = new Scanner(System.in);
            char c = console.next().charAt(0);
            if(Character.compare(c,'z')==0){
                move_up();
                return;
            }
            if(Character.compare(c,'s')==0){
                move_down();
                return;
            }
            if(Character.compare(c,'q')==0){
                move_left();
                return;
            }
            if(Character.compare(c,'d')==0){
                move_right();
                return;
            }
        }

    public void move_up(){
    	if(!can_move_up()) {
    		System.out.print("can't move there\n");
    		return;
    	}
        if(this.getElem(getPos_x()-1,getPos_y()) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x()-1,getPos_y());
        }else{
            if(this.getElem(getPos_x()-1,getPos_y()) instanceof Box){
                if(this.getElem(getPos_x()-2,getPos_y()) instanceof Vide){
                    swap(getPos_x()-1,getPos_y(),getPos_x()-2,getPos_y());
                    swap(getPos_x(),getPos_y(),getPos_x()-1,getPos_y());
                }
            }
        }
    }
   
    public void move_down(){
    	if(!can_move_down()) {
    		System.out.print("can't move there\n");
    		return;
    	}
        if(this.getElem(getPos_x()+1,getPos_y()) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x()+1,getPos_y());
        }else{
            if(this.getElem(getPos_x()+1,getPos_y()) instanceof Box){
                if(this.getElem(getPos_x()+2,getPos_y()) instanceof Vide){
                    swap(getPos_x()+1,getPos_y(),getPos_x()+2,getPos_y());
                    swap(getPos_x(),getPos_y(),getPos_x()+1,getPos_y());
                }
            }
        }
    }

    public void move_right(){
    	if(!can_move_right()) {
    		System.out.print("can't move there\n");
    		return;
    	}
        if(this.getElem(getPos_x(),getPos_y()+1) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()+1);
        }else{
            if(this.getElem(getPos_x(),getPos_y()+1) instanceof Box){
                if(this.getElem(getPos_x(),getPos_y()+2) instanceof Vide){
                    swap(getPos_x(),getPos_y()+1,getPos_x(),getPos_y()+2);
                    swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()+1);}
            }
        }
    }

    public void move_left(){
    	if(!can_move_left()) {
    		System.out.print("can't move there\n");
    		return;
    	}
        if(this.getElem(getPos_x(),getPos_y()-1) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()-1);
        }else{
            if(this.getElem(getPos_x(),getPos_y()-1) instanceof Box){
                if(this.getElem(getPos_x(),getPos_y()-2) instanceof Vide){
                    swap(getPos_x(),getPos_y()-1,getPos_x(),getPos_y()-2);
                    swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()-1);
                }
            }
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + size;
        result = prime * result + Arrays.deepHashCode(level);
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Matrice other = (Matrice) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (size != other.size)
            return false;
        if (!Arrays.deepEquals(level, other.level))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Matrice [name=" + name + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Element[][] getLevel(){
        return this.level;
    }

    public void setlevel(Element[][] level){
        this.level=level;
    }

    public Element getElem(int i, int j) {
        return level[i][j];
    }

    public void setElem(int i, int j, Element elem) {
        this.level[i][j] = elem;
    }
    
    public int getPos_x(){
        return pos_x;
    }

    public void setPos_x(int pos_x){
        this.pos_x=pos_x;
    }

    public int getPos_y(){
        return pos_y;
    }

    public void setPos_y(int pos_y){
        this.pos_y=pos_y;
    }

}
