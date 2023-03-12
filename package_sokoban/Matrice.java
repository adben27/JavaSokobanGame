package package_sokoban;

import java.util.Arrays;
import java.util.Stack;
import java.util.Scanner;
import java.util.Objects;
import java.lang.Character;


/**
 * pour l'instant la grille de la matrice sera un tableau de tableau puis après je crée un autre classe grille qui va implémenter la classe matrix de java
 * pos_x et pos_y sont la position du player je vais voir aprés si ya une meilleur facons de le faire
 */

public class Matrice extends Element{
    private String name;
    private int size;
    private int pos_x;
    private int pos_y;
	private Element[][] level;
	
	//pour savoir si c'est le premier monde (matrice principale) (matrice qui contient les autres)
	private boolean is_main;
	
	// pour savoir si le joueur est dans cette matrice
	private boolean is_here;
	
	// pour savoir dans quelle monde est le joueur pos_x du monde pos_y du monde
	// si le joueur est dans ce monde alors wrld_x = -1 et wrld_y = -1
	// sinon si 
	private int wrld_x;
	private int wrld_y;
	
	// les attributs ci-dessous sont utilisé pour stocker les ancien mouvement ainsi que la disposition de base de la matrice afin de permettre de refaire le niveau en cas de bloquage
	private Stack<Character> last_move;  // char is one of the primitive datatypes in Java, which cannot be used in generics. You can, however, substitute the wrapper java.lang.Character
    
	public Matrice(){
    	super('M',true,false,'m');
        this.name="";
        this.size=0;
        this.level=new Element[0][0];
        this.pos_x=0;
        this.pos_y=0;
        this.last_move=new Stack<Character>();
        this.is_here=true;
        this.wrld_x= -1;
        this.wrld_y= -1;
    }
    
    //  constructeur qui initialise pos_x et pos_y a 0 utilisé par nandan pour la lecture de niveau
    public Matrice(String name, char sign , boolean on_target, int size){
        super(Character.toUpperCase(sign),true,on_target,sign);// On donne au constructeur un signe en majuscule mais l'affichage de base sera en minuscule et sur la cible sera en majuscule
    	this.name=name;
        this.size=size;
        this.pos_x=0;
        this.pos_y=0;
        this.last_move=new Stack<Character>();
        this.is_here=true;
        this.wrld_x= -1;
        this.wrld_y= -1;
    }
    
    public Matrice(String name,char sign , boolean on_target, int size, Element[][] level, int x, int y, boolean is_here, boolean is_main, int wrld_x, int wrld_y){
        super(Character.toUpperCase(sign),true,on_target,sign);// On donne au constructeur un signe en majuscule mais l'affichage de base sera en minuscule et sur la cible sera en majuscule
    	this.name=name;
        this.size=size;
        this.level=level;
        this.pos_x=x;
        this.pos_y=y;
        this.last_move=new Stack<Character>();
        this.is_main=is_main;
        this.is_here=is_here;
        this.wrld_x= wrld_x;
        this.wrld_y= wrld_y;
    }
    
    // première version des fonctions de fin de niveau pas opti mais bon on verra aprés pour ca
    
    public boolean estFini() {
    	if(all_ontarget()) {
    		return true;
    	}
    	return false;
    }
    
    public boolean all_ontarget() {
    	Element e;
    	
    	if(is_main) {
    		if(is_here) {
    			if(!getElem(pos_x,pos_y).isOn_target()) {
        			return false;
        		}
    		}
    		if(!is_here) {
    			Matrice n = (Matrice) getElem(wrld_x,wrld_y);
    			if(!n.getElem(pos_x,pos_y).isOn_target()) {
    				return false;
    			}
    		}
    		
    	}
    	
    	for(int i = 0; i < this.getSize(); i++) {
    		for(int j = 0; j < this.getSize(); j++) {
    			e = getElem(i,j);
    			if(e instanceof Matrice) {
    				Matrice m = (Matrice) e;
    				if(!m.all_ontarget()) {
    					return false;
    				}
    			}
    			if(e.isOn_target()) {
    				if(e instanceof Vide)
    					return false;
    			}
    		}
    	}
    	
    	return true;
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
	/*
	 * Série de fonctions qui vérifient si le mouvement est possible dans les 4 directions cardinales 
	 */
	public boolean can_move_up() {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_move_up();
		}
		int i = getPos_x();
		int j = getPos_y();
		if (this.getElem(i - 1, j) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(i - 1, j) instanceof Box) {
				if (this.getElem(i - 2, j) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			if (this.getElem(i - 1, j) instanceof Matrice) {
				if(((Matrice) getElem(i - 1,j)).can_enter_up()) {
					return true;
				}
			}
			return true;
		}
	}
    
	public boolean can_move_down() {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_move_down();
		}
		int i = getPos_x();
		int j = getPos_y();
		if (this.getElem(i + 1, j) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(i + 1, j) instanceof Box) {
				if (this.getElem(i + 2, j) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			if (this.getElem(i + 1, j) instanceof Matrice) {
				if(((Matrice) getElem(i + 1,j)).can_enter_up()) {
					return true;
				}
			}
			return true;
		}
	}
    
	public boolean can_move_right() {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_move_right();
		}
		int i = getPos_x();
		int j = getPos_y();
		if (this.getElem(i, j + 1) instanceof Wall) {
			return false;
		} else {
			if (this.getElem(i, j + 1) instanceof Box) {
				if (this.getElem(i, j + 2) instanceof Vide) {
					return true;
				} else {
					return false;
				}
			}
			if (this.getElem(i , j + 1) instanceof Matrice) {
				if(((Matrice) getElem(i , j + 1)).can_enter_up()) {
					return true;
				}
			}
			return true;
		}
	}
	
	public boolean can_move_left() {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_move_left();
		}
		int i = getPos_x();
		int j = getPos_y();
		if(this.getElem(i , j - 1) instanceof Wall){
			return false;
        }else{
            if(this.getElem(i , j - 1) instanceof Box){
                if(this.getElem(i , j - 2) instanceof Vide){
                    return true;
                }else{
                    return false;
                }
            }
            if (this.getElem(i , j - 1) instanceof Matrice) {
				if(((Matrice) getElem(i , j - 1)).can_enter_up()) {
					return true;
				}
			}
            return true;
        }
    }
    /*
     * fonction qui regroupe les quatre fonctions de mouvement ainsi qu'un scanner pour effectuer le mouvement en fonction de l'input du joueur
     */
    public void move(){
    		System.out.println("Enter a move (z/q/s/d): ");
            Scanner console = new Scanner(System.in);
            char c = console.nextLine().charAt(0);
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
    /*
	 * Série de fonctions qui  effectuent le mouvement dans les 4 directions cardinales, sont utilisé dans le fonction move()
	 */
    public void move_up(){
    	if(!can_move_up()) {
    		System.out.print("can't move there\n");
    		return;
    	}
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			m.move_up();
			return;
		}
    	
    	if(last_move.isEmpty()) {
    		last_move.push('z');
    	
    	}else {
    		if(last_move.peek().equals('r')) {
    			last_move.pop();
    		}else {
    			last_move.push('z');
    		}
    	}

    	
        if(this.getElem(getPos_x()-1,getPos_y()) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x()-1,getPos_y());
        }else{
            if(this.getElem(getPos_x()-1,getPos_y()) instanceof Box){
                if(this.getElem(getPos_x()-2,getPos_y()) instanceof Vide){
                	last_move.push('+');
                    swap(getPos_x()-1,getPos_y(),getPos_x()-2,getPos_y());
                    swap(getPos_x(),getPos_y(),getPos_x()-1,getPos_y());
                }
            }
            if(this.getElem(getPos_x()-1,getPos_y()) instanceof Matrice) {
            	this.enter_up((Matrice)getElem(getPos_x()-1,getPos_y()));
            }
        }
    }
   
    public void move_down(){
    	
    	if(!can_move_down()) {
    		System.out.print("can't move there\n");
    		return;
    	}
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			m.move_down();
			return;
		}
    	
    	if(last_move.isEmpty()) {
    		last_move.push('s');
    	
    	}else {
    		if(last_move.peek().equals('r')) {
    			last_move.pop();
    		}else {
    			last_move.push('s');
    		}
    	}

        if(this.getElem(getPos_x()+1,getPos_y()) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x()+1,getPos_y());
        }else{
            if(this.getElem(getPos_x()+1,getPos_y()) instanceof Box){
                if(this.getElem(getPos_x()+2,getPos_y()) instanceof Vide){
                	last_move.push('+');
                    swap(getPos_x()+1,getPos_y(),getPos_x()+2,getPos_y());
                    swap(getPos_x(),getPos_y(),getPos_x()+1,getPos_y());
                }
            }
            if(this.getElem(getPos_x()+1,getPos_y()) instanceof Matrice) {
            	this.enter_up((Matrice)getElem(getPos_x()+1,getPos_y()));
            }
        }
    }

    public void move_right(){
    	if(!can_move_right()) {
    		System.out.print("can't move there\n");
    		return;
    	}
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			m.move_right();
			return;
		}
    	
    	if(last_move.isEmpty()) {
    		last_move.push('d');
    	}else {
    		if(last_move.peek().equals('r')) {
    			last_move.pop();
    		}else {
    			last_move.push('d');
    		}
    	}
    	
    	
        if(this.getElem(getPos_x(),getPos_y()+1) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()+1);
        }else{
            if(this.getElem(getPos_x(),getPos_y()+1) instanceof Box){
                if(this.getElem(getPos_x(),getPos_y()+2) instanceof Vide){
                	last_move.push('+');
                    swap(getPos_x(),getPos_y()+1,getPos_x(),getPos_y()+2);
                    swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()+1);}
            }
            if(this.getElem(getPos_x(),getPos_y()+1) instanceof Matrice) {
            	this.enter_up((Matrice)getElem(getPos_x(),getPos_y()+1));
            }
        }
    }

    public void move_left(){
    	if(!can_move_left()) {
    		System.out.print("can't move there\n");
    		return;
    	}
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			m.move_left();
			return;
		}
    	
    	if(last_move.isEmpty()) {
    		last_move.push('q');
    	}else {
    		if(last_move.peek().equals('r')) {
    			last_move.pop();
    		}else {
    			last_move.push('q');
    		}
    	}

        if(this.getElem(getPos_x(),getPos_y()-1) instanceof Vide){
            swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()-1);
        }else{
            if(this.getElem(getPos_x(),getPos_y()-1) instanceof Box){
                if(this.getElem(getPos_x(),getPos_y()-2) instanceof Vide){
                	last_move.push('+');
                    swap(getPos_x(),getPos_y()-1,getPos_x(),getPos_y()-2);
                    swap(getPos_x(),getPos_y(),getPos_x(),getPos_y()-1);
                }
            }
            if(this.getElem(getPos_x(),getPos_y()-1) instanceof Matrice) {
            	this.enter_up((Matrice)getElem(getPos_x(),getPos_y()-1));
            }
        }
    }
    /*
     * fonction qui annule le dernier mouvement même si il y'a eu un déplacement de boite ou de monde causé
     * utilise la pile last_move vérifie le dernier char donné par la pile qui représente le mouvement
     * char =  '+' alors il y'a eu un déplacement de boite ou de monde (voir les fonctions move_dir)
     * dans ce cas la il y'a un appel recursif afin d'annuler le mouvement du joueur puis d'annuler le mouvement de l'autre element
     * char = 'z' || 's'  || 'q' || 'd' mouvement dans l'une des directions
     * char = 'r' pour indiquer aux fonctions move de ne pas remettre encore le mouvement dans le pile quand on reset
     * 
     * la fonction retourne e si la pile est empty cela est utilisé dans la fonction reset pour revenir a l'etat de base du niveau
     */
    public Character ctrl_z() {
    	
    	// ici ctrl_z récrusif pas encore au point
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.ctrl_z();
		}
    	
    	if(last_move.empty()) {
    		return 'e';
    	}
    	switch(last_move.pop()) {
    		case '+':
    				switch(ctrl_z()) {
    					case 'z':
    						swap(getPos_x()-1,getPos_y(),getPos_x()-2,getPos_y());
    		    			return 'z';
    					case 's':
    						swap(getPos_x()+1,getPos_y(),getPos_x()+2,getPos_y());
    		    			return 's';
    					case 'q':
    						swap(getPos_x(),getPos_y()-1,getPos_x(),getPos_y()-2);
    		    			return 'q';
    					case 'd':
    						swap(getPos_x(),getPos_y()+1,getPos_x(),getPos_y()+2);
    		    			return 'd';
    					default:
    						System.err.println("ERREUR FUNC CTRL_Z");
    						return ' ';
    				}
    		case 'z':
    			last_move.push('r');
    			move_down();
    			return 'z';
    		case 's':
    			last_move.push('r');
    			move_up();
    			return 's';
    		case 'q':
    			last_move.push('r');
    			move_right();
    			return 'q';
    		case 'd':
    			last_move.push('r');
    			move_left();
    			return 'd';
    		default:
				System.err.println("ERREUR FUNC CTRL_Z");
				return ' ';
    	}
    }
    /*
     * implémentation naive de la fonction reset qui appel la fonction ctrl_z jusqu'a ce qu'elle rende le char 'e' qui signifie que la pile last_move est vide
     * en gros ca annule les mouvement un par un jusqu'a ce que le niveau soit reset
     * 
     * mais avec les monde récursive j'ai du ajouter un appel récursif pour reset les positions dans tout les niveau
     */
    public void reset() {
    	for (int i=0;i<getSize();i++) {
    		for (int j=0;j<getSize();j++) {
        		if (getElem(i,j)instanceof Matrice) {
        			((Matrice) getElem(i,j)).reset();
        		}
        	}
    	}
    	while(ctrl_z()!='e');

    }
    
    /*
     * Serie de fonctions can_enter_dir qui verifient si l'on peut rentrer dans ce monde par la direction indiqué 
     * ex : can_enter_up() verifie si on peut rentrer par le bas, car si le joueur veut monter dans un monde qui est au-dessus de lui
     * on vérifie si il y'a des mur en bas du monde
     * pour l'instant j'ai fait le cas ou dans le point d'entrée il y'a un vide
     * pas encore le cas ou c'est une boite normale ou un monde qu'on peut bouger 
     * j'ai pas fait le cas ou on rentre directement dans un autre monde
     */
    
    public boolean can_enter_up() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_enter_up();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if (!(getElem(getSize()-1,z) instanceof Wall)) {
    			return false;
    		}
    	}
    	return true;
    }

    public boolean can_enter_down() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_enter_down();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if (!(getElem(0,z) instanceof Wall)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean can_enter_left() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_enter_left();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if (!(getElem(z,getSize()-1) instanceof Wall)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public boolean can_enter_right() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.can_enter_right();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if (!(getElem(z,0) instanceof Wall)) {
    			return false;
    		}
    	}
    	return true;
    }
    
    /*
     * fonctions qui donnent la coordonee du point d'entrée en fonction de la direction
     * utilisé dans les fonctions enter_dir uniquement
     * on connait deja l'autre coordonee car elle depend de la direction (voir fonctions enter_dir)
     * c'est la meme chose que les fonction can_enter_dir mais il retourne le vide par ou le joueur peut rentrer
     */
    public int get_entry_up() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.get_entry_up();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if ((getElem(getSize()-1,z) instanceof Vide)) {
    			return z;
    		}
    	}
    	return 0;
    }
    
    public int get_entry_down() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.get_entry_down();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if ((getElem(0,z) instanceof Vide)) {
    			return z;
    		}
    	}
    	return 0;
    }

    public int get_entry_left() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.get_entry_left();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if ((getElem(z,getSize()-1) instanceof Vide)) {
    			return z;
    		}
    	}
    	return 0;
    }
    
    public int get_entry_right() {
    	
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_x, wrld_y);
			return m.get_entry_right();
		}
    	
    	for(int z = 0; z < getSize();z++) {
    		if ((getElem(z,0) instanceof Vide)) {
    			return z;
    		}
    	}
    	return 0;
    }
    
    /*
     * serie de fonctions pour rentrer dans un monde
     * est utilise dans les fonctions de type move_dir uniquement
     * si il ya une boite en dans le monde a l'entree alors on la pousse
     * sinon on echange le vide et le joueur
     * pas besoin de faire un test can_enter_dir car il est déja fait dans la fonction move_dir
     */
    
    public void enter_up(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_x, wrld_y);
			n.get_entry_up();
			return;
		}
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(m.getSize()-1,get_entry_up()).isOn_target()||getElem(getPos_x(), getPos_y()).isOn_target()) {
    		m.getElem(m.getSize()-1,get_entry_up()).setOn_target(!m.getElem(m.getSize()-1,get_entry_up()).isOn_target());
    		getElem(getPos_x(), getPos_y()).setOn_target(!getElem(getPos_x(), getPos_y()).isOn_target());
        }
    	
    	Element temp = m.getElem(m.getSize()-1,get_entry_up());
    	m.setElem(m.getSize()-1,get_entry_up(),this.getElem(getPos_x(), getPos_y()));
    	this.setElem(getPos_x(), getPos_y(), temp);
    }
    
    public void enter_down(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_x, wrld_y);
			n.get_entry_down();
			return;
		}
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(0,get_entry_up()).isOn_target()||getElem(getPos_x(), getPos_y()).isOn_target()) {
    		m.getElem(0,get_entry_up()).setOn_target(!m.getElem(0,get_entry_up()).isOn_target());
    		getElem(getPos_x(), getPos_y()).setOn_target(!getElem(getPos_x(), getPos_y()).isOn_target());
        }
    	
    	Element temp = m.getElem(0,get_entry_up());
    	m.setElem(0,get_entry_up(),this.getElem(getPos_x(), getPos_y()));
    	this.setElem(getPos_x(), getPos_y(), temp);
    }
    
    public void enter_left(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_x, wrld_y);
			n.get_entry_left();
			return;
		}
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(get_entry_up(),m.getSize()-1).isOn_target()||getElem(getPos_x(), getPos_y()).isOn_target()) {
    		m.getElem(get_entry_up(),m.getSize()-1).setOn_target(!m.getElem(get_entry_up(),m.getSize()-1).isOn_target());
    		getElem(getPos_x(), getPos_y()).setOn_target(!getElem(getPos_x(), getPos_y()).isOn_target());
        }
    	
    	Element temp = m.getElem(get_entry_up(),m.getSize()-1);
    	m.setElem(get_entry_up(),m.getSize()-1,this.getElem(getPos_x(), getPos_y()));
    	this.setElem(getPos_x(), getPos_y(), temp);
    }
    
    public void enter_right(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_x, wrld_y);
			n.get_entry_right();
			return;
		}
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(get_entry_up(),0).isOn_target()||getElem(getPos_x(), getPos_y()).isOn_target()) {
    		m.getElem(get_entry_up(),0).setOn_target(!m.getElem(get_entry_up(),0).isOn_target());
    		getElem(getPos_x(), getPos_y()).setOn_target(!getElem(getPos_x(), getPos_y()).isOn_target());
        }
    	
    	Element temp = m.getElem(get_entry_up(),0);
    	m.setElem(get_entry_up(),0,this.getElem(getPos_x(), getPos_y()));
    	this.setElem(getPos_x(), getPos_y(), temp);
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
