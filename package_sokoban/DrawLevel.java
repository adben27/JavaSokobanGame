package package_sokoban;

import java.awt.*;
import java.util.Stack;

import javax.swing.*;

public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera la boucle de jeu
    private static final int FPS=60;//nombre de FPS du jeu
    private int sizeImg, next;//taille des images et pour savoir si on lance le prochain niveau 

    //image que l'on va afficher, "monde" est un tableau d'images qui contient toute les images
    private Image mur, vide, cible, joueur;
    private Image[] monde;
    
    //pour les explications voir ligne 146
    private char[] nomC; //nom d'un monde dans le tableau

    private boolean haut, bas, gauche, droite, ctrlZ;//pour faire bouger le joueur

    /* Matrice que l'on va utiliser pour la version recursive
     * lvl sera la matrice que l'on va afficher (changera a chaque appel des methodes d'entrer et de sorie de monde)
     * matriceP sera la matrice qui contient le 1er monde lors du démarage du jeu
     */
    public Matrice lvl, matriceP, matriceB, matriceC, matriceD, matriceE,
                    matriceF, matriceG, matriceH, matriceI, matriceJ;

    private Player p;//Le joueur
    private Vide v;
    private Stack<Matrice> m;//pile qui contient les matrices peres

    public DrawLevel() {
        super();
        setLayout(null);

        m=new Stack<Matrice>();

        Box b= new Box(false, 'B');
        Box c= new Box(false, 'C');        
        Box d= new Box(false, 'D');        
        Box e= new Box(false, 'E');        
        Box f= new Box(false, 'F');        
        Box g= new Box(false, 'G');        
        Box h= new Box(false, 'H');        
        Box i= new Box(false, 'I');
        Box j= new Box(false, 'J');
        Wall m= new Wall();

        p = new Player(false);
        
        v = new Vide(false);
        Vide y = new Vide(true);
        Vide x = new Vide(true);
        Vide z = new Vide(true);

        Element[][] tab_b={{m,m,m,m,v,m},
                           {m,v,v,v,v,m},
                           {m,v,v,v,v,m},
                           {m,v,v,b,v,v},
                           {v,v,v,v,v,m},
                           {m,m,m,v,m,m}};

        Element[][] tab_c={{m,v,m,m,m,m,m},
                           {m,v,v,c,v,v,v},
                           {v,v,v,v,v,m,m},
                           {m,m,v,v,v,m,m},
                           {m,v,v,v,v,v,m},
                           {m,v,v,v,v,v,m},
                           {m,m,m,v,m,m,m}};

        Element[][] tab_d={{m,v,m,m,m},
                           {m,v,v,m,m},
                           {v,d,v,v,v},
                           {m,v,v,v,m},
                           {m,m,v,m,m}};

        Element[][] tab_e={{e,v},
                           {v,m}};

        Element[][] tab_f={{m,m,m,v,m},
                           {m,f,v,v,m},
                           {v,v,v,v,v},
                           {m,v,v,v,m},
                           {m,m,v,m,m}};

        Element[][] tab_g={{m,v,m,m,m,m,v,m,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {v,v,v,v,g,v,v,v,v},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,m,m,m,v,m,m,m,m}};

        Element[][] tab_h={{m,v,m},
                           {v,h,v},
                           {m,v,m}};

        Element[][] tab_i={{i}};

        Element[][] tab_j={{m,v,m,m,m},
                           {v,j,v,m,m},
                           {m,v,v,v,v},
                           {m,v,v,m,m},
                           {m,m,v,m,m}};

        matriceB=new Matrice("B", 'b', false, tab_b.length, tab_b, 0, 0, false, false,-1,-1);
        matriceC=new Matrice("C", 'c', false, tab_c.length, tab_c, 0, 0, false, false,-1,-1);
        matriceD=new Matrice("D", 'd', false, tab_d.length, tab_d, 0, 0, false, false,-1,-1);
        matriceE=new Matrice("E", 'e', false, tab_e.length, tab_e, 0, 0, false, false,-1,-1);
        matriceF=new Matrice("F", 'f', false, tab_f.length, tab_f, 0, 0, false, false,-1,-1);
        matriceG=new Matrice("G", 'g', false, tab_g.length, tab_g, 0, 0, false, false,-1,-1);
        matriceH=new Matrice("H", 'h', false, tab_h.length, tab_h, 0, 0, false, false,-1,-1);
        matriceI=new Matrice("I", 'i', false, tab_i.length, tab_i, 0, 0, false, false,-1,-1);
        matriceJ=new Matrice("J", 'j', false, tab_j.length, tab_j, 0, 0, false, false,-1,-1);

        tab_b[1][2]=matriceD;
        tab_d[0][1]=matriceJ;
        tab_j[3][3]=matriceC;
        tab_c[4][4]=matriceE;

        matriceB.setlevel(tab_b);
        matriceC.setlevel(tab_c);
        matriceD.setlevel(tab_d);
        matriceJ.setlevel(tab_j);

        Element[][] tab_lvl={{m,m,m,m,m,m},
                             {m,v,p,v,v,v},
                             {m,v,v,v,v,m},
                             {m,m,matriceB,m,v,m},
                             {m,v,m,v,v,m},
                             {m,m,m,m,m,m}};
        
        lvl=new Matrice("lvl", 'l', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);
        matriceP=new Matrice("P", 'p', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);

        //taille des images
        sizeImg=(int)getToolkit().getScreenSize().getHeight()/(2*lvl.getSize());

        /*on recupère les images qu'on va utiliser
         * nomC[0]='B' et monde[0]=l'image du mondeB
         * nomC[1]='C' et monde[1]=l'image du mondeC
         * etc...
         * 
         * nomC -> char du monde[i] (i=nomI)
        */
        monde = new Image[9];
        nomC = new char[9];

        for (int l=0, k=66 ; l < 9; l++, k++)
            nomC[l]=(char) k;

        monde[0] = getToolkit().getImage("package_sokoban/Image/mondeB.png");
        monde[1] = getToolkit().getImage("package_sokoban/Image/mondeC.png");
        monde[2] = getToolkit().getImage("package_sokoban/Image/mondeD.png");
        monde[3] = getToolkit().getImage("package_sokoban/Image/mondeE.png");
        monde[4] = getToolkit().getImage("package_sokoban/Image/mondeF.png");
        monde[5] = getToolkit().getImage("package_sokoban/Image/mondeG.png");
        monde[6] = getToolkit().getImage("package_sokoban/Image/mondeH.png");
        monde[7] = getToolkit().getImage("package_sokoban/Image/mondeI.png");
        monde[8] = getToolkit().getImage("package_sokoban/Image/mondeJ.png");

        joueur = getToolkit().getImage("package_sokoban/Image/joueur.png");
        mur = getToolkit().getImage("package_sokoban/Image/mur.png");
        vide = getToolkit().getImage("package_sokoban/Image/vide.png");
        cible = getToolkit().getImage("package_sokoban/Image/cible.png");

        //on met tout a false pour pas bouger le joueur
        haut=bas=gauche=droite=ctrlZ=false;
    }

    /*METHODE A MODIFIER POUR LIRE UN FICHIER QUI CONTIENT UN NIVEAU (PRIORITE AU SOKOBAN CLASSIQUE) */
    public void loadLvl() {
        System.out.println("lvl charger");
    }

    //debut du thread qui s'occupe de la boucle de jeu
    public void startGame() {
        game = new Thread(this);
        game.start();
    }

    //methode qui permet de mettre a jour l'affichage du jeu
    @Override
    public void run() {
        //System.nanoTime() donne l'heure en nano secondes
        double interval_dessin=1000000000/FPS;
        double dessin_suivant=System.nanoTime()+interval_dessin;
        
        while (game!=null) {
            repaint();//repeint le niveau dans le panel
            update();//met à jour le niveau en memoire
            
            try {
                double tps_restant = dessin_suivant - System.nanoTime();
                //Thread.sleep() prend en argument des millis secondes donc on divise par un million le temps restant pour l'avoir en millis secondes
                tps_restant/=1000000;

                if (tps_restant<0)
                    tps_restant= 0;

                Thread.sleep((long) tps_restant);

                dessin_suivant+=interval_dessin;
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //permet de mettre a jour le niveau
    public void update() {
        int x=lvl.getPos_x(), y=lvl.getPos_y();
        setPrincipale();

        if (bas) {
            if(lvl.getPos_y()==lvl.getSize()-1){
                sort_bas();
                bas=false;
                return;
            }
            if (lvl.getElem(y+1, x).getClass() == Matrice.class)
                if(lvl.can_enter_up((Matrice) lvl.getElem(y+1, x)) && !lvl.can_move_down(x, y))
                    m.push(lvl);
            lvl.move_down(x, y);
            bas=false;
        }
        if (haut) {
            /*on verifie si le joueur est en y=0 si oui on le fait sotir de la matrice courante sinon on move normalement
             *meme raisonement pour les autres mouvement
             */
            if(lvl.getPos_y()==0){
                sort_haut();
                haut=false;
                return;
            }
            if (lvl.getElem(y-1, x).getClass() == Matrice.class)
                if(lvl.can_enter_down((Matrice) lvl.getElem(y-1, x)) && !lvl.can_move_up(x, y))
                    m.push(lvl);

            lvl.move_up(x, y);
            haut=false;
        }
        if (gauche) {
            if(lvl.getPos_x()==0){
                sort_gauche();
                gauche=false;
                return;
            }
            if (lvl.getElem(y, x-1).getClass() == Matrice.class)
                if(lvl.can_enter_right((Matrice) lvl.getElem(y, x-1)) && !lvl.can_move_left(x, y))
                    m.push(lvl);
            lvl.move_left(x, y);
            gauche=false;
        }
        if (droite) { 
            if(lvl.getPos_x()==lvl.getSize()-1){
                sort_droite();
                droite=false;
                return;
            }
            if (lvl.getElem(y, x+1).getClass() == Matrice.class)
                if(lvl.can_enter_left((Matrice) lvl.getElem(y, x+1)) && !lvl.can_move_right(x, y))
                    m.push(lvl);
            lvl.move_right(x, y);
            droite=false;
        }
        if (ctrlZ) {
            lvl.ctrl_z();
            ctrlZ=false;
        }
        if (lvl.all_ontarget()) {
            next=JOptionPane.showConfirmDialog(this, "Félicitation vous avez terminer le niveau.\nVoulez-vous passez au niveau suivant ?");
            if(next==0)
                loadLvl();
            else if(next==1)
                lvl.ctrl_z();
            else
                resetAll();
        }
    }

    //on peint le niveau dans le panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Element e;
        Graphics2D g2 = (Graphics2D) g;

        //on calcule la pos x et y où dessiner le 1er element du tableau
        int pos_x=((getWidth() - sizeImg)/2)-sizeImg*(lvl.getSize()/2),
            pos_y=((getHeight() - sizeImg)/2)-sizeImg*(lvl.getSize()/2);

        paintBordure(g2, pos_x, pos_y);//on dessine le monde pere si il existe

        //on dessine du vide qui fera la taille de la matrice 
        g2.drawImage(vide, pos_x, pos_y, sizeImg*lvl.getSize(), sizeImg*lvl.getSize(), lvl.getColor(), this); 

        for (int i = 0; i < lvl.getSize(); i++) {
            for (int j = 0; j < lvl.getSize(); j++) {

                //on calcule la pos x et y de chaque image et on recupere l'element en pos (j, i)
                pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);
                pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2);
                e=lvl.getElem(i, j);

                //si c'est du vide on passe a la prochaine interation de la boucle 
                if(e.getSign()==' '){
                    continue;
                }
                //si c'est un mur on dessine le mur et on passe a la prochaine interation de la boucle
                if(e.getSign()=='#'){
                    g2.drawImage(mur, pos_x, pos_y, sizeImg, sizeImg, this);
                    continue;
                }
                //si c'est une cible on la dessine et on passe a la prochaine interation de la boucle
                if(e.getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                    continue;
                }
                //si c'est un joueur on le dessine et on passe a la prochaine interation de la boucle
                if(e.getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                    continue;
                }
                //si c'est un joueur dans une cible on le dessine et on passe a la prochaine interation de la boucle
                if(e.getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                    continue;
                }
                //si c'est un monde/box on la dessine et on passe a la prochaine interation de la boucle
                if(Character.isUpperCase(e.getSign())){
                    m_OU_b(g2, e, i, j);
                }else{
                //si c'est un monde/box dans une cible on la dessine et on passe a la prochaine interation de la boucle
                    m_OU_b(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
            }
        }
    }

    /* "m" est la matrice a afficher
     * "pos_x" et "pos_y" sont les coordonnées (pos_x, pos_y) de la localisation où il faut dessiner  
     */
    public void paintMonde(Graphics2D g2, Matrice m, int pos_x, int pos_y) {
        //on calcule la taille des images à dessiner et on sauvegarde la pos x et y du monde a dessiner
        int size = sizeImg/m.getSize(), i=pos_y, j=pos_x, a=0;
        Element e;

        //on dessine du vide qui fera la taille du monde
        g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, m.getColor(), this);
        
        for (int y = 0; y < m.getSize(); y++){
            for (int x = 0; x < m.getSize(); x++){
                //on calcule la pos x et y de la case a dessine et on recupere l'element a dessiner
                pos_x = j + size*x;
                pos_y = i + size*y;
                e=m.getElem(y,x);

                //meme commentaire que dans paintCompponante
                if(e.getSign()==' '){
                    continue;
                }
                if (e.getSign()=='#'){
                    g2.drawImage(mur, pos_x,  pos_y , size, size, this);
                    continue;
                }
                if(e.getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, size, size, this);
                    continue;
                }
                if(e.getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y , size, size, this);
                    g2.drawImage(cible, pos_x, pos_y , size, size, this);
                    continue;
                }
                if(e.getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, size, size, this);
                    continue;
                }

                if(Character.isUpperCase(e.getSign())){
                    for (int k = 0; k < 9; k++) {
                        if(e.getSign()==nomC[k]){
                            a=k;
                            break;
                        }
                    }
                    g2.drawImage(monde[a], pos_x, pos_y, size, size, this);
                }else{
                    for (int k = 0; k < 9; k++) {
                        if(e.getSign()==nomC[k]){
                            a=k;
                            break;
                        }
                    }
                    g2.drawImage(monde[a], pos_x, pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
            }
        }
    }

    public void m_OU_b(Graphics2D g2, Element e, int i, int j) {
        //meme commentaire que paintComponent
        int pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2),
            pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);

        g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, lvl.getColor(), this);

        //si c'est une box on la dessine sinon on dessine un monde
        if(e.getClass() == Box.class){
            int a=0;
            if(Character.isUpperCase(e.getSign())){
                for (int k = 0; k < 9; k++) {
                    if(e.getSign()==nomC[k]){
                        a=k;
                        break;
                    }
                }
                g2.drawImage(monde[a], pos_x, pos_y, sizeImg, sizeImg, this);
            }else{
                for (int k = 0; k < 9; k++) {
                    if(e.getSign()==nomC[k]){
                        a=k;
                        break;
                    }
                }
                g2.drawImage(monde[a], pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x , pos_y, sizeImg, sizeImg, this);
            }
        }else{
            paintMonde(g2, (Matrice) e, pos_x, pos_y);
        }
    }

    public void paintBordure(Graphics2D g2, int lvl_x, int lvl_y){
        if(m.isEmpty())
            return;

        //on recupere la matrice pere, la couleur du pere, on calcule la taille des images et la pos x et y des images a dessiner
        Matrice pere = m.peek();
        Element e;
        Color c = pere.getColor();
        int ecart=sizeImg*lvl.getSize();
        int pos_y=((getHeight() - sizeImg)/2)-sizeImg*(lvl.getSize()/2), 
            pos_x=((getWidth() - sizeImg)/2)-sizeImg*(lvl.getSize()/2), a=0;

        for (int i=0; i<pere.getSize(); i++) {
            for (int j=0; j<pere.getSize(); j++) {
                //meme commentaire que paintComponent
                pos_x = lvl_x + ecart*(j-pere.getWrldX());
                pos_y = lvl_y + ecart*(i-pere.getWrldY());
                e=pere.getElem(i,j);

                if(pos_x == pere.getWrldX() && pos_y == pere.getWrldY()){
                    continue;
                }
                if (e.getSign()=='#'){
                    g2.drawImage(mur, pos_x, pos_y , ecart, ecart, this);
                    continue;
                }
                if (e.getSign()==' ') {
                    g2.drawImage(vide, pos_x, pos_y , ecart, ecart, c, this);
                    continue;
                }
                if(e.getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                    continue;
                }
                if(Character.isUpperCase(e.getSign())){
                    for (int k = 0; k < 9; k++) {
                        if(e.getSign()==nomC[k]){
                            a=k;
                            break;
                        }
                    }
                    g2.drawImage(vide, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(monde[a], pos_x, pos_y, ecart, ecart, this);
                }else{
                    for (int k = 0; k < 9; k++) {
                        if(e.getSign()==nomC[k]){
                            a=k;
                            break;
                        }
                    }
                    g2.drawImage(vide, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(monde[a], pos_x, pos_y, ecart, ecart, this);
                    g2.drawImage(cible, pos_x , pos_y, ecart, ecart, this);
                }
            }
        }
    }

    /*
     * Les methodes peut_sortir_(dir) renvoie true si le joueur qui est la matrice principale peut aller dans
     * la matrice pere qui est dans la pile 'm' sinon les methodes renvoie false
     * Les methodes peut_sortir_(dir) seront utilise sur les methodes sort_(dir)
     */

    public boolean peut_sortir_haut(){
        if(m.isEmpty())
            return false;

        Matrice pere=m.peek();//on recupère la matrice pere
        int x=pere.getWrldX(), y=pere.getWrldY();//on recupère les coordonnées (x, y) du monde ou se trouve le joueur

       if (pere.getElem(y-1, x).getClass() == Wall.class || m.isEmpty())
        //si il y a un mur au dessus du monde ou est le joueur on renvoie false
            return false;
        else if(pere.getElem(y-1, x).getClass() == Box.class || pere.getElem(y-1, x).getClass() == Matrice.class)
        //si il y a une matrice ou une box on verifie si elle peuvent bouger pour laisser du vide pour que le joueur sorte si oui on renvoie true sinon false
            return pere.can_move_up(x, y-1);
        else
            return true; //si il y a du vide au dessus du monde ou est le joueur on renvoie true
    }

    //Meme raisonnemnt pour les 3 autres methodes mais avec des coordonnées differente
    public boolean peut_sortir_bas(){
        if(m.isEmpty())
            return false;

        Matrice pere=m.peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y+1, x).getClass() == Wall.class || m.isEmpty())
            return false;
        else if(pere.getElem(y+1, x).getClass() == Box.class || pere.getElem(y+1, x).getClass() == Matrice.class)
            return pere.can_move_down(x, y+1);
        else
            return true;
    }

    public boolean peut_sortir_gauche(){
        if(m.isEmpty())
            return false;

        Matrice pere=m.peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y, x-1).getClass() == Wall.class || m.isEmpty())
            return false;
        else if(pere.getElem(y, x-1).getClass() == Box.class || pere.getElem(y, x-1).getClass() == Matrice.class)
            return pere.can_move_left(x-1, y);
        else
            return true;
    }

    public boolean peut_sortir_droite(){
        if(m.isEmpty())
            return false;

        Matrice pere=m.peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y, x+1).getClass() == Wall.class)
            return false;
        else if(pere.getElem(y, x+1).getClass() == Box.class || pere.getElem(y, x+1).getClass() == Matrice.class)
            return pere.can_move_right(x+1, y);
        else
            return true;
    }

    /*
     * Les méthodes sort_(dir) change la pile 'm' (utiliser la methode pop) et mettent le joueur dans la matrice pere
     * grace a setElem (le joueur et le vide est une instance de DrawLevel)
     * Il faut verifier si il y a un obstacle si oui le deplacer puis faire les setElem
     * ATTENTION LES METHODES peut_sortir_(dir) renvoie true
     */

    public void sort_haut() {
    	if(!peut_sortir_haut()) {
            System.out.print("can't move there\n");
    		return;
    	}
    	
    	Matrice pere = m.pop();
    	int x = pere.getWrldX(), y = pere.getWrldY();
    	
        Matrice fils = (Matrice) pere.getElem(y, x);
    	int pos_x = fils.getPos_x(); int pos_y = fils.getPos_y();
    	    	
    	if(pere.getElem(y-1, x).getClass() == Box.class || pere.getElem(y-1, x).getClass() == Matrice.class) {
    		pere.move_up(y-1, x); // dans ce cas la y'a juste ca parceque a la fin d'un move_up il y'aura un vide a la coordonée donné en arguement
    	}					      // si le move_up marche et dans ce cas on sais qu'il marche prsq on a fait peut_sortir_haut
    	
    	// ici c'est juste un échange avec le vide qu'il y'a dans (y,x+1)
    	Vide temp = (Vide) pere.getElem(y-1, x);
		fils.setElem(pos_y,pos_x,temp);
        pere.setElem(y-1, x, p);
        
		fils.setPos_x(-1); fils.setPos_y(-1);
		pere.setPos_x(x);pere.setPos_y(y-1);
		pere.setWrldX(-1);pere.setWrldY(-1);
		
        fils.setIsHere(false);
		pere.setIsHere(true);
    }

    public void sort_bas() {
        if(!peut_sortir_bas()) {
            System.out.print("can't move there\n");
    		return;
    	}
    	
    	Matrice pere = m.pop();
    	int x = pere.getWrldX(), y = pere.getWrldY();
    	
        Matrice fils = (Matrice) pere.getElem(y, x);
    	int pos_x = fils.getPos_x(); int pos_y = fils.getPos_y();
    	    	
    	if(pere.getElem(y+1, x).getClass() == Box.class || pere.getElem(y+1, x).getClass() == Matrice.class) {
    		pere.move_up(y+1, x); // dans ce cas la y'a juste ca parceque a la fin d'un move_up il y'aura un vide a la coordonée donné en arguement
    	}					      // si le move_up marche et dans ce cas on sais qu'il marche prsq on a fait peut_sortir_haut
    	
    	// ici c'est juste un échange avec le vide qu'il y'a dans (y,x+1)
    	Vide temp = (Vide) pere.getElem(y+1, x);
		fils.setElem(pos_y,pos_x,temp);
        pere.setElem(y+1, x, p);
        
		fils.setPos_x(-1); fils.setPos_y(-1);
		pere.setPos_x(x);pere.setPos_y(y+1);
		pere.setWrldX(-1);pere.setWrldY(-1);
		
        fils.setIsHere(false);
		pere.setIsHere(true);
    }

    public void sort_gauche() {
        if(!peut_sortir_gauche()) {
            System.out.print("can't move there\n");
    		return;
    	}
    	
    	Matrice pere = m.pop();
    	int x = pere.getWrldX(), y = pere.getWrldY();
    	
        Matrice fils = (Matrice) pere.getElem(y, x);
    	int pos_x = fils.getPos_x(); int pos_y = fils.getPos_y();
    	    	
    	if(pere.getElem(y, x-1).getClass() == Box.class || pere.getElem(y, x-1).getClass() == Matrice.class) {
    		pere.move_right(y, x-1); // dans ce cas la y'a juste ca parceque a la fin d'un move_up il y'aura un vide a la coordonée donné en arguement
    	}					      // si le move_up marche et dans ce cas on sais qu'il marche prsq on a fait peut_sortir_haut
    	
    	// ici c'est juste un échange avec le vide qu'il y'a dans (y,x+1)
    	Vide temp = (Vide) pere.getElem(y, x-1);
		fils.setElem(pos_y,pos_x,temp);
        pere.setElem(y, x-1, p);
        
		fils.setPos_x(-1); fils.setPos_y(-1);
		pere.setPos_x(x-1);pere.setPos_y(y);
		pere.setWrldX(-1);pere.setWrldY(-1);
		
        fils.setIsHere(false);
		pere.setIsHere(true);
    }

    public void sort_droite() {
        if(!peut_sortir_droite()) {
            System.out.print("can't move there\n");
    		return;
    	}
    	
    	Matrice pere = m.pop();
    	int x = pere.getWrldX(), y = pere.getWrldY();
    	
        Matrice fils = (Matrice) pere.getElem(y, x);
    	int pos_x = fils.getPos_x(); int pos_y = fils.getPos_y();
    	    	
    	if(pere.getElem(y, x+1).getClass() == Box.class || pere.getElem(y, x+1).getClass() == Matrice.class) {
    		pere.move_right(y, x+1); // dans ce cas la y'a juste ca parceque a la fin d'un move_up il y'aura un vide a la coordonée donné en arguement
    	}					      // si le move_up marche et dans ce cas on sais qu'il marche prsq on a fait peut_sortir_haut
    	
    	// ici c'est juste un échange avec le vide qu'il y'a dans (y,x+1)
    	Vide temp = (Vide) pere.getElem(y, x+1);
		
		fils.setElem(pos_y,pos_x,temp);
        pere.setElem(y, x+1, p);

		fils.setPos_x(-1); fils.setPos_y(-1);
		pere.setPos_x(x+1);pere.setPos_y(y);
		pere.setWrldX(-1);pere.setWrldY(-1);
		
        fils.setIsHere(false);
		pere.setIsHere(true);
    }

    //on reinitialise toute les matrices et on dit que la matrice lvl est la matrice a afficher
    public void resetAll() {
        lvl.reset();       
        matriceP.reset();
        matriceB.reset();
        matriceC.reset();
        matriceD.reset();
        matriceE.reset();
        matriceF.reset();
        matriceG.reset();
        matriceH.reset();
        matriceI.reset();
        matriceJ.reset();
        m.clear();
        setPrincipale();
    }

    //permet les mouvements (dit si on a appuiez sur les fleches ou les bouttons)
    public void setHaut(boolean b) {
        haut=b;
    }
    public void setBas(boolean b) {
        bas=b;
    }
    public void setGauche(boolean b) {
        gauche=b;
    }
    public void setDroite(boolean b) {
        droite=b;
    }
    public void setCtrlZ(boolean b) {
        ctrlZ=b;
    }

    public int getSizeImg() {
        return sizeImg;
    }

    public Matrice getLvl(){
        return lvl;
    }

    /*
     * Methode qui permet de savoir quelle est la matrice ou ce trouve le joueur et met l'ancienne matrice
     * dans la pile 'm'. Cette methode permet aussi de mettre a jour la taille des images 
     */
    public void setPrincipale() {
        if(matriceB.isHere())
            lvl=matriceB;
        if(matriceC.isHere())
            lvl=matriceC;
        if(matriceD.isHere())
            lvl=matriceD;
        if(matriceE.isHere())
            lvl=matriceE;
        if(matriceF.isHere())
            lvl=matriceF;
        if(matriceG.isHere())
            lvl=matriceG;
        if(matriceH.isHere())
            lvl=matriceH;
        if(matriceI.isHere())
            lvl=matriceI;
        if(matriceJ.isHere())
            lvl=matriceJ;
        if(matriceP.isHere())
            lvl=matriceP;
    }
}