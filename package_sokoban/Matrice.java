package package_sokoban;

import java.util.Arrays;
import java.util.Stack;

import javax.lang.model.util.ElementScanner14;

import java.util.Scanner;
import java.util.Objects;
import java.util.Random;
import java.awt.Color;
import java.lang.Character;


/**
 * pour l'instant la grille de la matrice sera un tableau de tableau puis après je crée un autre classe grille qui va implémenter la classe matrix de java
 * pos_x et pos_y sont la position du player je vais voir aprés si ya une meilleur facons de le faire
 */

public class Matrice extends Element{
    private String name;
    private int size;
    private int pos_x, pos_x_copie;
    private int pos_y, pos_y_copie;
	private Element[][] level;
	private int[] pos_x_cible, pos_y_cible;
	private int nb_cible;
	private int r,g,b;
	private Color color; //la couleur du niveau
	
	//pour savoir si c'est le premier monde (matrice principale) (matrice qui contient les autres)
	private boolean is_main, is_main_copie;
	
	// pour savoir si le joueur est dans cette matrice
	private boolean is_here, is_here_copie;
	
	// pour savoir dans quelle monde est le joueur pos_x du monde pos_y du monde
	// si le joueur est dans ce monde alors wrld_x = -1 et wrld_y = -1
	// sinon si 
	private int wrld_x, wrld_x_copie;
	private int wrld_y, wrld_y_copie;
	
	// les attributs ci-dessous sont utilisé pour stocker les ancien mouvement ainsi que la disposition de base de la matrice afin de permettre de refaire le niveau en cas de bloquage
	private Stack<Element[][]> last_move;// pile qui contient l'ancien matrice

    private Stack<Integer> stack_x;
	private Stack<Integer> stack_y;
	private Stack<Matrice> m;

	public Matrice(){
    	super('M',true,false,'m');
        this.name="";
        this.size=0;
        this.level=new Element[0][0];
		pos_x_cible=new int[0];
		pos_y_cible=new int[0];

		nb_cible=0;

        this.pos_x=pos_x_copie=0;
        this.pos_y=pos_y_copie=0;
        this.last_move=new Stack<Element[][]>();
		stack_x=new Stack<Integer>();
		stack_y=new Stack<Integer>();
		m=new Stack<Matrice>();
        this.is_here=true;
        this.wrld_x= wrld_x_copie=-1;
        this.wrld_y= wrld_y_copie=-1;

		//on genere 3 nombres aleatoires qui feront la couleur du niveau
        Random random = new Random();
        r=random.nextInt(256);
        g=random.nextInt(256);
        b=random.nextInt(256);
        color = (new Color(r, g, b));
    }
    
    //  constructeur qui initialise pos_x et pos_y a 0 utilisé par nandan pour la lecture de niveau
    public Matrice(String name, char sign , boolean on_target, int size){
        super(Character.toUpperCase(sign),true,on_target,sign);// On donne au constructeur un signe en majuscule mais l'affichage de base sera en minuscule et sur la cible sera en majuscule
    	this.name=name;
        this.size=size;
        this.pos_x=pos_x_copie=0;
        this.pos_y=pos_y_copie=0;
        this.last_move=new Stack<Element[][]>();
		stack_x=new Stack<Integer>();
		stack_y=new Stack<Integer>();
		m=new Stack<Matrice>();
        this.is_here= is_here_copie=true;
        this.wrld_x=wrld_x_copie= -1;
        this.wrld_y=wrld_y_copie= -1;
		pos_x_cible=new int[11];
		pos_y_cible=new int[11];
		nb_cible=0;

		//on genere 3 nombres aleatoires qui feront la couleur du niveau
        Random random = new Random();
        r=random.nextInt(256);
        g=random.nextInt(256);
        b=random.nextInt(256);
        color = (new Color(r, g, b));
    }
    
    public Matrice(String name,char sign , boolean on_target, int size, Element[][] level, int x, int y, boolean is_here, boolean is_main, int wrld_x, int wrld_y){
        super(Character.toUpperCase(sign),true,on_target,sign);// On donne au constructeur un signe en majuscule mais l'affichage de base sera en minuscule et sur la cible sera en majuscule
    	this.name=name;
        this.size=size;
        this.level=level;
		pos_x_cible=new int[11];
		pos_y_cible=new int[11];

		nb_cible=0;
		for (int i = 0; i < level.length; i++) {
			for (int j = 0; j < level.length; j++) {
				if(level[i][j] instanceof Vide && level[i][j].on_target){			
					pos_x_cible[nb_cible]=j;
					pos_y_cible[nb_cible]=i;
					nb_cible++;
				}
			}
		}

        this.pos_x=pos_x_copie=x;
        this.pos_y=pos_y_copie=y;
        this.last_move=new Stack<Element[][]>();
		stack_x=new Stack<Integer>();
		stack_y=new Stack<Integer>();
		m=new Stack<Matrice>();
        this.is_main=is_main_copie= is_main;
        this.is_here=is_here_copie=is_here;
        this.wrld_x=wrld_x_copie= wrld_x;
        this.wrld_y=wrld_y_copie= wrld_y;

		//on genere 3 nombres aleatoires qui feront la couleur du niveau
        Random random = new Random();
        r=random.nextInt(256);
        g=random.nextInt(256);
        b=random.nextInt(256);
        color = new Color(r, g, b);
    }
    
    // première version des fonctions de fin de niveau pas opti mais bon on verra aprés pour ca
    
    public boolean estFini() {
    	if(all_ontarget()) {
    		return true;
    	}
    	return false;
    }
    /*
	 * ('estFini' fait la meme chose que 'all_ontarget' autant la supprimer)
	 * pas au point, bug des fois en fonction de ou le joueur va dans une cible et en sort (meme bug qu'avant mais en moins grave)
	 * (rentre par la droite (de la cible) et sort par la gauche (de la cible))
	 */
    public boolean all_ontarget() {
    	if(nb_cible==0)
			return false;

		boolean res=true;

		for (int i = 0; i < nb_cible; i++) {
			//Si il y a une/plusieurs case vide avec on_target a true alors en va renvoier false sinon true
			if(level[pos_y_cible[i]][pos_x_cible[i]].on_target && (level[pos_y_cible[i]][pos_x_cible[i]] instanceof Vide))
				res=false;
		}
		
    	return res;
    }
    

    public Element[][] lvlCopie(){
		Element[][] res = new Element[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				res[i][j]=level[i][j];
			}
		}

		return res;
	}
    
    /*
     * permet d'échanger les elements de deux cellules
     * si l'un des elements est le player actualise les parametres pos_x et pos_y de la matrice
     * @param i pos_y de la cellule 1
     * @param j pos_x de la cellule 1
     * @param a pos_y de la cellule 2
     * @param b pos_x de la cellule 2
     */
    public void swap(int i, int j, int a, int b){
        Element tmp = level[i][j];
        if(!tmp.isMoveable()||!level[a][b].isMoveable()) {
        	return;
        }
        if(tmp instanceof Player){/*il ne peut pas y avoir deux joueurs normalement faudrait ajouter un test peut être apres*/
            pos_x=b;
            pos_y=a;
        }
        if(level[a][b] instanceof Player){
            pos_x=j;
            pos_y=i;
        }
        if(tmp.isOn_target()!=level[a][b].isOn_target()) {
        	setElem(i, j, level[a][b]);
			setElem(a, b, tmp);
			
			//on inverse les 'on_target'
			level[a][b].setOn_target(!level[a][b].on_target);
			level[i][j].setOn_target(!level[i][j].on_target);

			return;
        }
        setElem(i, j,level[a][b]);
        setElem(a, b, tmp);

        return;
    }
    /*fonction qui prend comme argument un char qui représente la direction du mouvement z : haut, q : gauche, s : bas, d : droite, autre que ceux la il
     *return false d'ailleurs j'en profite pour modifier les autres fonctions move_direction pour qu'elles utilisent celle-ci pour check le mouvement
     */
	public boolean can_move(char c) {
		if (Character.compare(c, 'z') == 0) {
			return can_move_up(pos_y, pos_x);
		}
		if (Character.compare(c, 's') == 0) {
			return can_move_down(pos_y, pos_x);
		}
		if (Character.compare(c, 'q') == 0) {
			return can_move_left(pos_y, pos_x);
		}
		if (Character.compare(c, 'd') == 0) {
			return can_move_right(pos_y, pos_x);
		}
		return false;
	}
	/*
	 * Série de fonctions qui vérifient si le mouvement est possible dans les 4 directions cardinales 
	 */
	public boolean can_move_up(int x, int y) {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_y, wrld_x);
			return m.can_move_up(x, y);
		}

		if(y-1<0)
			return false;
		else if(this.getElem(y-1, x).getClass() == Wall.class) //si c'est un mur ou qu'on depasse les indices de la matrice renvoie false 
			return false;
		//si c'est une boite ou une matrice on verifie si elle peuvent bouger, si oui renvoie true sinon false 
		else if(this.getElem(y-1, x).getClass() == Box.class || this.getElem(y-1, x).getClass() == Matrice.class)
			return can_move_up(x, y-1);
		else //si c'est du vide on renvoie true
			return true;
	}
	/*
	 * meme raisonnement que can_move_up mais dans des directions differentes
	 */
	public boolean can_move_down(int x, int y) {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_y, wrld_x);
			return m.can_move_down(x, y);
		}
		
		if(y+1>size-1)
			return false;
		if(this.getElem(y+1, x).getClass() == Matrice.class || this.getElem(y+1, x).getClass() == Box.class)
			return can_move_down(x, y+1);
		else if (this.getElem(y+1, x).getClass() == Wall.class || y>size-1)
			return false;
		else
			return true;
	}
    
	public boolean can_move_right(int x, int y) {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_y, wrld_x);
			return m.can_move_right(x, y);
		}

		if(x+1>size-1)
			return false;
		if (this.getElem(y, x+1).getClass() == Wall.class)
			return false;
		else if(this.getElem(y, x+1).getClass() == Box.class || this.getElem(y, x+1).getClass() == Matrice.class)
			return can_move_right(x+1, y);
		else
			return true;
	}
	
	public boolean can_move_left(int x, int y) {
		if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_y, wrld_x);
			return m.can_move_left(x, y);
		}

		if(x-1<0)
			return false;
		if (this.getElem(y, x-1).getClass() == Wall.class)
			return false;
		else if(this.getElem(y, x-1).getClass() == Box.class || this.getElem(y, x-1).getClass() == Matrice.class)
			return can_move_left(x-1, y);
		else
			return true;
    }
    /*
     * fonction qui regroupe les quatre fonctions de mouvement ainsi qu'un scanner pour effectuer le mouvement en fonction de l'input du joueur
     */
    public void move(){
    		System.out.println("Enter a move (z/q/s/d): ");
            Scanner console = new Scanner(System.in);
            char c = console.nextLine().charAt(0);
            if(Character.compare(c,'z')==0){
                move_up(pos_x, pos_y);
                return;
            }
            if(Character.compare(c,'s')==0){
                move_down(pos_x, pos_y);
                return;
            }
            if(Character.compare(c,'q')==0){
                move_left(pos_x, pos_y);
                return;
            }
            if(Character.compare(c,'d')==0){
                move_right(pos_x, pos_y);
                return;
            }
        }
    /*
	 * Série de fonctions qui  effectuent le mouvement dans les 4 directions cardinales, sont utilisé dans le fonction move()
	 */
    public void move_up(int x, int y){
		if(!can_move_up(x, y)) {
			if(getElem(y-1, x).getClass() == Matrice.class){
				if (!can_enter_down((Matrice) getElem(y-1, x))) {
					System.out.print("can't move there\n");
					return;
				}
			}else{
				System.out.print("can't move there\n");
				return;
			}
    	}
    	
		//on met l'ancienne pos (x,y) du joueur dans des piles et on met l'ancienne matrice dans une pile
    	last_move.push(lvlCopie());
		stack_x.push(pos_x);
		stack_y.push(pos_y);
    	
		//si c'est du vide on swap
        if(this.getElem(y-1, x) instanceof Vide){
            swap(y, x, y-1, x);
        }else{
            if(this.getElem(y-1, x) instanceof Box){
				/*
				 * Si c'est une boite on la fait bouger, une fois qu'on a bouger la boite le joueur va se retrouver
				 * en face du vide puis on swap le joueur et le vide
				 */
				move_up(x, y-1);
				swap(y, x, y-1, x);
            }
            if(this.getElem(y-1, x) instanceof Matrice) {
				/*
				 * meme chose que pour les boites mais cette fois, si on ne peut pas bouger la matrice mais 
				 * qu'on peut rentrer dans celle ci alors on rentre et on modifie wrld_x et wrld_y et is_here
				 */
				if (can_move_up(x, y-1)){
					move_up(x, y-1);
					swap(y, x, y-1, x);
				}else{
					m.push(this);
					enter_down((Matrice)getElem(y-1, x));
				}
			}
        }
    }
   
	//meme raisonement que move up mais avec des directions differentes
    public void move_down(int x, int y){
		if(!can_move_down(x, y)) {
			if(getElem(y+1, x).getClass() == Matrice.class){
				if (!can_enter_up((Matrice) getElem(y+1, x))) {
					System.out.print("can't move there\n");
					return;
				}
			}else{
				System.out.print("can't move there\n");
				return;
			}
    	}
    	
		last_move.push(lvlCopie());
		stack_x.push(pos_x);
		stack_y.push(pos_y);

        if(this.getElem(y+1, x) instanceof Vide){
            swap(y, x, y+1, x);
        }else{
            if(this.getElem(y+1, x) instanceof Box){
				move_down(x, y+1);
				swap(y, x, y+1, x);
            }
            if(this.getElem(y+1, x) instanceof Matrice) {
				if (can_move_down(x, y+1)){
					move_down(x, y+1);
					swap(y, x, y+1, x);
				}else{
					m.push(this);
					enter_up((Matrice)getElem(y+1, x));
				}
            }
		}
    }

    public void move_right(int x, int y){
		if(!can_move_right(x, y)) {
			if(getElem(y, x+1).getClass() == Matrice.class){
				if (!can_enter_left((Matrice) getElem(y, x+1))) {
					System.out.print("can't move there\n");
					return;
				}
			}else{
				System.out.print("can't move there\n");
				return;
			}
    	}

		last_move.push(lvlCopie());
		stack_x.push(pos_x);
		stack_y.push(pos_y);

        if(this.getElem(y, x+1) instanceof Vide){
            swap(y, x, y, x+1);
        }else{
            if(this.getElem(y, x+1) instanceof Box){
				move_right(x+1, y);
				swap(y, x, y, x+1);   
            }
            if(this.getElem(y, x+1) instanceof Matrice) {
            	if (can_move_right(x+1, y)){
					move_right(x+1, y);
					swap(y, x, y, x+1);
				}else{
					m.push(this);
					enter_left((Matrice)getElem(y, x+1));
				}
			}
        }
    }

    public void move_left(int x, int y){
		if(!can_move_left(x, y)) {
			if(getElem(y, x-1).getClass() == Matrice.class){
				if (!can_enter_right((Matrice) getElem(y, x-1))) {
					System.out.print("can't move there\n");
					return;
				}
			}else{
				System.out.print("can't move there\n");
				return;
			}
    	}
    	
    	last_move.push(lvlCopie());
		stack_x.push(pos_x);
		stack_y.push(pos_y);

        if(this.getElem(y, x-1) instanceof Vide){
            swap(y, x, y, x-1);
        }else{
            if(this.getElem(y, x-1) instanceof Box){
				move_left(x-1, y);
                swap(y, x, y, x-1);
            }
            if(this.getElem(y, x-1) instanceof Matrice) {
            	if (can_move_left(x-1, y)){
					move_left(x-1, y);
					swap(y, x, y, x-1);
				}else{
					m.push(this);
					enter_right((Matrice)getElem(y, x-1));
				}
            }
        }
    }
    /*
     * Si last_move est vide alors
     */
    public void ctrl_z() {
    	
    	// ici ctrl_z récrusif pas encore au point
    	if(!this.is_here) {
			Matrice m =(Matrice) this.getElem(wrld_y, wrld_x);
			m.ctrl_z();
			return;
		}
    	
    	if(last_move.empty()) //si last_move est vide aucun mouvement n'a été fait donc on return
    		return;

		for (int i = 0; i < nb_cible; i++)
			/*
			 * si l'element en (pos_x_cible[i], pos_y_cible[i]) n'est pas du vide et a 'on_target' 
			 * en true alors on change 'on_target' 
			 */
			if(level[pos_y_cible[i]][pos_x_cible[i]].on_target && !(level[pos_y_cible[i]][pos_x_cible[i]] instanceof Vide))
				level[pos_y_cible[i]][pos_x_cible[i]].setOn_target(false);

    	level=last_move.pop(); //on recupère l'ancienne matrice

		//on remet les cases vide qui on étaient mis a false (1er for) a true
		for (int i = 0; i < nb_cible; i++)
			level[pos_y_cible[i]][pos_x_cible[i]].setOn_target(true);
		
		//on recupère l'ancienne pos (x, y) du joueur
		pos_x=stack_x.pop();
		pos_y=stack_y.pop();
    }
    
	//Comme sont nom l'indique ca reset la matrice
    public void reset() {
		while(!last_move.isEmpty())
			ctrl_z();

		pos_x=pos_x_copie;
		pos_y=pos_y_copie;
		wrld_x=wrld_x_copie;
		wrld_y=wrld_y_copie;
		is_here=is_here_copie;
		is_main=is_main_copie;
		last_move.clear();
		stack_x.clear();
		stack_y.clear();
    }
    
    /*
     * Serie de fonctions can_enter_dir qui verifient si l'on peut rentrer dans ce monde par la direction indiqué 
     * ex : can_enter_up() verifie si on peut rentrer par le bas, car si le joueur veut monter dans un monde qui est au-dessus de lui
     * on vérifie si il y'a des mur en bas du monde
     * pour l'instant j'ai fait le cas ou dans le point d'entrée il y'a un vide
     * pas encore le cas ou c'est une boite normale ou un monde qu'on peut bouger 
     * j'ai pas fait le cas ou on rentre directement dans un autre monde
     */
    //meme commentaire que en haut
    public boolean can_enter_up(Matrice m) {
    	for(int z = 0; z < m.size; z++)
    		if (m.getElem(0,z) instanceof Vide)
    			return true;
    		
    	return false;
    }

    public boolean can_enter_down(Matrice m) {
		for(int z = 0; z < m.size; z++)
    		if (m.getElem(m.size-1, z) instanceof Vide)
    			return true;
 		
		return false;
    }
    
    public boolean can_enter_left(Matrice m) {
    	for(int z = 0; z < m.size; z++)
    		if (m.getElem(z, 0) instanceof Vide)
    			return true;

    	return false;
    }
    
    public boolean can_enter_right(Matrice m) {
    	for(int z = 0; z < m.size;z++)
    		if (m.getElem(z, m.size-1) instanceof Vide)
    			return true;
    		
    	return false;
    }
    
    /*
     * fonctions qui donnent la coordonee du point d'entrée en fonction de la direction
     * utilisé dans les fonctions enter_dir uniquement
     * on connait deja l'autre coordonee car elle depend de la direction (voir fonctions enter_dir)
     * c'est la meme chose que les fonction can_enter_dir mais il retourne le vide par ou le joueur peut rentrer
     */
	//retourne -1 si il n'y a pas de vide
    public int get_entry_up(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice a =(Matrice) this.getElem(wrld_y, wrld_x);
			return a.get_entry_up(a);
		}

		//sa me mettait une exeption (out uf bounds -1 blabla taille du tab x) donc j'ai rajouter une variable pour eviter ca
		if(m.size==1)
			return -1;
    	
    	for(int z = 0; z < m.size; z++) {
    		if ((m.getElem(0, z) instanceof Vide)) {
    			return z;
    		}
    	}
    	return -1;
    }
    
    public int get_entry_down(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice a =(Matrice) this.getElem(wrld_y, wrld_x);
			return a.get_entry_down(a);
		}

		if(m.size==1)
			return -1;
    	
    	for(int z = 0; z < m.size; z++) {
    		if ((m.getElem(m.size-1, z) instanceof Vide)) {
    			return z;
    		}
    	}
    	return -1;
    }

    public int get_entry_left(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice a =(Matrice) this.getElem(wrld_y, wrld_x);
			return a.get_entry_left(a);
		}

		if(m.size==1)
			return -1;
    	
    	for(int z = 0; z < m.size;z++) {
    		if ((m.getElem(z, 0) instanceof Vide)) {
    			return z;
    		}
    	}
    	return -1;
    }
    
    public int get_entry_right(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice a =(Matrice) this.getElem(wrld_y, wrld_x);
			return a.get_entry_right(a);
		}

		if(m.size==1)
			return -1;
    	
    	for(int z = 0; z < m.size;z++) {
    		if ((m.getElem(z, m.size -1) instanceof Vide)) {
    			return z;
    		}
    	}
    	return -1;
    }
    
    /*
     * serie de fonctions pour rentrer dans un monde
     * est utilise dans les fonctions de type move_dir uniquement
     * si il ya une boite en dans le monde a l'entree alors on la pousse
     * sinon on echange le vide et le joueur
     * pas besoin de faire un test can_enter_dir car il est déja fait dans la fonction move_dir
     */
    //la matrice 'm' est la matrice ou le joueur va rentrer
    public void enter_up(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_y, wrld_x);
			n.get_entry_up(n);
			return;
		}

		//on set la pos (x, y) du joueur dans la matrice m et on set wrld_x et wrld_y dans la matrice courante
		int x=get_entry_up(m);
		m.setPos_y(0);
		m.setPos_x(x);
		wrld_x=pos_x;
		wrld_y=pos_y+1;

		if(!can_enter_up(m) || x==-1)
			return;
		
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(0, x).isOn_target() || getElem(pos_y, pos_x).isOn_target()) {
    		m.getElem(0, x).setOn_target(!m.getElem(0, x).isOn_target());
    		getElem(pos_y, pos_x).setOn_target(!getElem(pos_y, pos_x).isOn_target());
        }
    	
    	Element temp = m.getElem(0, x);
    	m.setElem(0, x, this.getElem(pos_y, pos_x));
    	this.setElem(pos_y, pos_x, temp);

		//is_here de 'm' devient true et is_here de this devient false
		m.is_here=true;
		is_here=false;
    }
    
	//meme raisonement que enter down mais avec des direcrtions differentes
    public void enter_down(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_y, wrld_x);
			n.get_entry_down(n);
			return;
		}

		int x=get_entry_down(m);
		m.setPos_y(m.size-1);
		m.setPos_x(x);
		wrld_x=pos_x;
		wrld_y=pos_y-1;

		if(!can_enter_down(m) || x==-1)
			return;
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(m.size-1, x).isOn_target() || getElem(pos_y, pos_x).isOn_target()) {
    		m.getElem(m.size-1, x).setOn_target(!m.getElem(m.size-1, x).isOn_target());
    		getElem(pos_y, pos_x).setOn_target(!getElem(pos_y, pos_x).isOn_target());
        }
    	
    	Element temp = m.getElem(m.size-1, x);
    	m.setElem(m.size-1, x,this.getElem(pos_y, pos_x));
    	this.setElem(pos_y, pos_x, temp);

		m.is_here=true;
		is_here=false;
    }
    
    public void enter_left(Matrice m) {
    	
    	if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_y, wrld_x);
			n.get_entry_left(n);
			return;
		}

		int y=get_entry_left(m);
		m.setPos_y(y);
		m.setPos_x(0);
		wrld_x=pos_x+1;
		wrld_y=pos_y;

		if(!can_enter_left(m) || y==-1)
			return;
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(y,0).isOn_target()||getElem(pos_y, pos_x).isOn_target()) {
    		m.getElem(y,0).setOn_target(!m.getElem(y,0).isOn_target());
    		getElem(pos_y, pos_x).setOn_target(!getElem(pos_y, pos_x).isOn_target());
        }
    	
    	Element temp = m.getElem(y,0);
    	m.setElem(y,0,this.getElem(pos_y, pos_x));
    	this.setElem(pos_y, pos_x, temp);

		m.is_here=true;
		is_here=false;
    }
    
    public void enter_right(Matrice m) {
    	
    	/*if(!this.is_here) {
			Matrice n =(Matrice) this.getElem(wrld_y, wrld_x);
			n.get_entry_right(n);
			return;
		}*/

		int y=get_entry_right(m);
		m.setPos_y(y);
		m.setPos_x(m.size-1);
		wrld_x=pos_x-1;
		wrld_y=pos_y;

		if(!can_enter_right(m) || y==-1)
			return;
    	
    	//pour échanger l'attribut on_target si a l'entré du sous-monde il y'a une cible
    	if(m.getElem(y, m.size-1).isOn_target()||getElem(pos_y, pos_x).isOn_target()) {
    		m.getElem(y, m.size-1).setOn_target(!m.getElem(y, m.size-1).isOn_target());
    		getElem(pos_y, pos_x).setOn_target(!getElem(pos_y, pos_x).isOn_target());
        }
    	
    	Element temp = m.getElem(y, m.size-1);
    	m.setElem(y, m.size-1, getElem(pos_y, pos_x));
    	setElem(pos_y, pos_x, temp);

		m.is_here=true;
		is_here=false;
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

    public Element getElem(int y, int x) {
        return level[y][x];
    }

    public void setElem(int y, int x, Element elem) {
        this.level[y][x] = elem;
    }
    
    public void setIs_main(){
    	is_main=true;
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

	public void setIs_main(Boolean main) {
		is_main=main;
	}

	public Color getColor() {
		return color;
	}

	public int getWrldX(){
		return wrld_x;
	}

	public int getWrldY(){
		return wrld_y;
	}

	public void setWrldX(int x){
		wrld_x=x;
	}

	public void setWrldY(int y){
		wrld_y=y;
	}

	public boolean isHere(){
		return is_here;
	}

	public void setIsHere(boolean a){
		is_here=a;
	}

	public Stack<Matrice> getStackM(){
		return m;
	}
}
