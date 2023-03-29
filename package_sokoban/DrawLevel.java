package package_sokoban;

import java.awt.*;
import java.io.*;
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
     * lvl sera la matrice que l'on va afficher (changera a chaque appel des methodes d'entrer et de sortie de monde)
     * matriceP sera la matrice qui contient le 1er monde lors du démarage du jeu
     */
    private Matrice lvl, matriceP;

    /*matrice[0]=matriceB
     *matrice[1]=matriceC
     *etc...
     *jusqu'a matriceJ
    */
    private Matrice[] matrice;         

    private Player p;//Le joueur
    private Vide v;
    private Stack<Matrice> m;//pile qui contient les matrices peres

    public DrawLevel() throws IOException, FileNotFoundException {
        super();
        setLayout(null);

        m=new Stack<Matrice>();
        matrice=new Matrice[9];

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

        Element[][] tab_b={{m,m,m,m,v,m},//matrice[0]
                           {m,v,v,v,v,m},
                           {m,v,v,v,v,m},
                           {v,v,v,v,v,v},
                           {m,v,v,v,v,m},
                           {m,m,m,v,m,m}};

        Element[][] tab_c={{m,v,m,m,m,m,m},//matrice[1]
                           {m,v,v,c,v,v,v},
                           {v,v,v,v,v,m,m},
                           {m,m,v,v,v,m,m},
                           {m,v,v,v,v,v,m},
                           {m,v,v,v,v,v,m},
                           {m,m,m,v,m,m,m}};

        Element[][] tab_d={{m,v,m,m,m},//matrice[2]
                           {m,v,v,m,m},
                           {v,d,v,v,v},
                           {m,v,v,v,m},
                           {m,m,v,m,m}};

        Element[][] tab_e={{e,v},//matrice[3]
                           {v,m}};

        Element[][] tab_f={{m,m,m,v,m},//matrice[4]
                           {m,f,v,v,m},
                           {v,v,v,v,v},
                           {m,v,v,v,m},
                           {m,m,v,m,m}};

        Element[][] tab_g={{m,m,m,m,m,m,v,m,m},//matrice[5]
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,h,v,m},
                           {m,v,v,b,v,v,v,v,m},
                           {v,v,v,v,b,v,v,v,v},
                           {m,v,v,v,v,v,j,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,c,v,v,v,v,m},
                           {m,m,m,m,m,m,m,m,m}};

        Element[][] tab_h={{m,v,m},//matrice[6]
                           {v,h,v},
                           {m,v,m}};

        Element[][] tab_i={{i}};//matrice[7]

        Element[][] tab_j={{m,v,m,m,m},//matrice[8]
                           {v,v,v,m,m},
                           {m,v,j,v,v},
                           {m,v,v,m,m},
                           {m,m,v,m,m}};	
	
        matrice[0/*B*/]=new Matrice("B", 'b', false, tab_b.length, tab_b, 0, 0, false, false,-1,-1);
        matrice[1/*C*/]=new Matrice("C", 'c', false, tab_c.length, tab_c, 0, 0, false, false,-1,-1);
        matrice[2/*D*/]=new Matrice("D", 'd', false, tab_d.length, tab_d, 0, 0, false, false,-1,-1);
        matrice[3/*E*/]=new Matrice("E", 'e', false, tab_e.length, tab_e, 0, 0, false, false,-1,-1);
        matrice[4/*F*/]=new Matrice("F", 'f', false, tab_f.length, tab_f, 0, 0, false, false,-1,-1);
        matrice[5/*G*/]=new Matrice("G", 'g', false, tab_g.length, tab_g, 0, 0, false, false,-1,-1);
        matrice[6/*H*/]=new Matrice("H", 'h', false, tab_h.length, tab_h, 0, 0, false, false,-1,-1);
        matrice[7/*I*/]=new Matrice("I", 'i', false, tab_i.length, tab_i, 0, 0, false, false,-1,-1);
        matrice[8/*J*/]=new Matrice("J", 'j', false, tab_j.length, tab_j, 0, 0, false, false,-1,-1);

        tab_b[1][2]=matrice[2];
        tab_b[3][2]=matrice[5];
        tab_d[1][1]=matrice[8];
        tab_j[3][3]=matrice[1];
        tab_c[4][4]=matrice[3];

        matrice[0].setlevel(tab_b);
        matrice[1].setlevel(tab_c);
        matrice[2].setlevel(tab_d);
        matrice[8].setlevel(tab_j);

        Element[][] tab_lvl={{m,m,m,m,m,m},
                             {m,v,p,v,v,v},
                             {m,v,matrice[7],matrice[3],matrice[0],m},
                             {m,v,v,v,v,m},
                             {m,v,v,v,v,m},
                             {m,m,m,m,m,m}};
        
        //lvl=new Matrice("lvl", 'l', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);
        //matriceP=new Matrice("P", 'p', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);

        lvl=loadLvl(null, "package_sokoban/outlevels/4.txt");
        matriceP=lvl;

        //taille des images
        sizeImg=(int)getToolkit().getScreenSize().getHeight()/(2*lvl.getSize());
        if(lvl.getSize()>=10){
            sizeImg-=1;
        }
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

    public Matrice loadLvl(Matrice[] monde, String fileName) throws IOException, FileNotFoundException {
        FileReader filereader=new FileReader(fileName);
        BufferedReader br = new BufferedReader(filereader);
        String line = br.readLine();
        String[] parts = line.split(" ");
	    String name = parts[0];
        int size = Integer.parseInt(parts[1]);
        char[][] level = new char[size][size];
	    Element[][] el=new Element[size][size];
        int row = 0, x=0, y=0;
        while ((line = br.readLine()) != null) {
            for (int col = 0; col < size; col++) {
            	level[row][col] = line.charAt(col);
                switch(level[row][col]){
                    case '#':   el[row][col]=new Wall();
                            break;
                    case '@':   el[row][col]=new Vide(true);    
                            break;                       
                    case 'A':   el[row][col]=new Player(false);
                                x=col;
                                y=row;
                            break;                          
                    case 'a':   el[row][col]=new Player(true);
                                x=col;
                                y=row;
                            break;                           
                    case ' ':   el[row][col]=new Vide(false);
                            break;
                    case 'B':   el[row][col]=new Box(false);
                            break;
                    case 'b':   el[row][col]=new Box(true);
                            break;
                    default :   System.out.println("WTF BRO");
                    }
            }
            row++;
        }
        matriceP=new Matrice(name,'c', false, size, el, x, y, true, true, -1, -1);
        lvl=matriceP;
        br.close();
        return lvl;
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
        setPrincipale();//lvl=la matrice ou il y a le joueur
        int x=lvl.getPos_x(), y=lvl.getPos_y();

        if (bas) {
            //si le joueur ne peut pas bouger mais peut sortir on le fait sortir
            if(!lvl.can_move_down(x, y) && peut_sortir_bas(x, y)){
                sort_bas(x, y);
                bas=false;
                return;
            }
            /*y+1<lvl.getSize() pour ne pas avoir d'execption du type on depasse le tableau 
             *si on ne paut pas bouger mais que l'on peut rentrer dans la matrice en bas du joueur alors on fait rentrer le joueur
            */ 
            if (y+1<lvl.getSize() && lvl.getElem(y+1, x).getClass() == Matrice.class)
                if(!lvl.can_move_down(x, y) && lvl.can_enter_up((Matrice) lvl.getElem(y+1, x))){
                    m.push(lvl);//on met la matrice pere dans une pile
                    lvl.enter_up((Matrice) lvl.getElem(y+1, x), x, y);
                    bas=false;
                    return;
                }
            //on bouge le joueur en bas si aucune condition des 'if' n'est vérifié
            lvl.move_down(x, y);
            bas=false;
        }
        //meme commentaire que pour 'if(bas)' mais avec des directions différentes
        if (haut) {
            if(!lvl.can_move_up(x, y) && peut_sortir_haut(x, y)){
                sort_haut(x, y);
                haut=false;
                return;
            }
            if (y-1>=0 && lvl.getElem(y-1, x).getClass() == Matrice.class)
                if(!lvl.can_move_up(x, y) && lvl.can_enter_down((Matrice) lvl.getElem(y-1, x))){
                    m.push(lvl);
                    lvl.enter_down((Matrice) lvl.getElem(y-1, x), x, y);
                    haut=false;
                    return;
                }
            lvl.move_up(x, y);
            haut=false;
        }
        if (gauche) {
            if(!lvl.can_move_left(x, y) && peut_sortir_gauche(x, y)){
                sort_gauche(x, y);
                gauche=false;
                return;
            }
            if (x-1>=0 && lvl.getElem(y, x-1).getClass() == Matrice.class){
                if(!lvl.can_move_left(x, y) && lvl.can_enter_right((Matrice) lvl.getElem(y, x-1))){
                    m.push(lvl);
                    lvl.enter_right((Matrice) lvl.getElem(y, x-1), x, y);
                    gauche=false;
                    return;
                }
            }

            lvl.move_left(x, y);
            gauche=false;
        }
        if (droite) {
            if(!lvl.can_move_right(x, y) && peut_sortir_droite(x, y)){
                sort_droite(x, y);
                droite=false;
                return;
            }
            if(x+1<lvl.getSize() && lvl.getElem(y, x+1).getClass() == Matrice.class)
                if(!lvl.can_move_right(x, y) &&lvl.can_enter_left((Matrice) lvl.getElem(y, x+1))){
                    m.push(lvl);
                    lvl.enter_left((Matrice) lvl.getElem(y, x+1), x, y);
                    droite=false;
                    return;
                }
            lvl.move_right(x, y);
            droite=false;
        }
        //si 'ctrl+z' est appuié alors on revient d'une action en arrière
        if (ctrlZ) {
            lvl.ctrl_z();
            ctrlZ=false;
        }
        /*si toute les matrices on leurs box/matrice ou le joueur sur une cible alors on affiche un message qui dit
         *que le niveau est terminé et on demande si l'utilisateur veut passez au niveau suivant
        */ 
        if (matriceP.estFini()) {
            next=JOptionPane.showConfirmDialog(this, "Félicitation vous avez terminer le niveau.\nVoulez-vous passez au niveau suivant ?");
            if(next==0){
                try {
                    lvl=loadLvl(matrice, "package_sokoban/outlevels/4.txt");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(next==1)
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
        if(m.getSize()==1)
            g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, this);
        else
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
                            g2.drawImage(vide, pos_x, pos_y, ecart, ecart, c, this);
                            g2.drawImage(monde[k], pos_x, pos_y, ecart, ecart, this);
                            break;
                        }
                    }
                }else{
                    for (int k = 0; k < 9; k++) {
                        if(e.getSign()==nomC[k]){
                            g2.drawImage(vide, pos_x, pos_y, ecart, ecart, c, this);
                            g2.drawImage(monde[k], pos_x, pos_y, ecart, ecart, this);
                            g2.drawImage(cible, pos_x , pos_y, ecart, ecart, this);
                            break;
                        }
                    }
                }
            }
        }
    }

    /*
     * Les methodes peut_sortir_(dir) renvoie true si le joueur qui est la matrice principale peut aller dans
     * la matrice pere qui est dans la pile 'm' sinon les methodes renvoie false
     * Les methodes peut_sortir_(dir) seront utilise sur les methodes sort_(dir)
     */

    public boolean peut_sortir_haut(int x, int y){
        if(m.isEmpty() || lvl.getElem(0, x).getClass() == Wall.class)
            return false;
        
        if(!lvl.can_move_up(x, y) && y-1>0)
            return peut_sortir_haut(x, y-1);

        Matrice pere=m.peek();
        int xp=pere.getWrldX(), yp=pere.getWrldY();

        if (yp-1<0 || pere.getElem(yp-1, xp).getClass() == Wall.class)
            return false;
        else if(pere.getElem(yp-1, xp).getClass() == Box.class || pere.getElem(yp-1, xp).getClass() == Matrice.class){
            if(pere.can_move_up(xp, yp-1)){
                pere.move_up(xp, yp-1);
                return true;
            }
            return false;
        }
        else
            return true;
    }

    //Meme raisonnemnt pour les 3 autres methodes mais avec des coordonnées differente
    public boolean peut_sortir_bas(int x, int y){
        if(m.isEmpty() || lvl.getElem(lvl.getSize()-1, x).getClass() == Wall.class)
            return false;
        
        if(!lvl.can_move_down(x, y) && y+1<lvl.getSize())
            return peut_sortir_bas(x, y+1);

        Matrice pere=m.peek();
        int xp=pere.getWrldX(), yp=pere.getWrldY();

        if (yp+1>=pere.getSize() || pere.getElem(yp+1, xp).getClass() == Wall.class)
            return false;
        else if(pere.getElem(yp+1, xp).getClass() == Box.class || pere.getElem(yp+1, xp).getClass() == Matrice.class){
            if(pere.can_move_down(xp, yp+1)){
                pere.move_down(xp, yp+1);
                return true;
            }
            return false;
        }
        else
            return true;
    }

    public boolean peut_sortir_gauche(int x, int y){
        if(m.isEmpty() || lvl.getElem(y, 0).getClass() == Wall.class)
            return false;
        
        if(x-1>=0 && !lvl.can_move_left(x, y))
            return peut_sortir_gauche(x-1, y);

        Matrice pere=m.peek();
        int xp=pere.getWrldX(), yp=pere.getWrldY();

        if (xp-1<0 || pere.getElem(yp, xp-1).getClass() == Wall.class)
            return false;
        else if(pere.getElem(yp, xp-1).getClass() == Box.class || pere.getElem(yp, xp-1).getClass() == Matrice.class){
            if(pere.can_move_left(xp-1, yp)){
                pere.move_left(xp-1, yp);
                return true;
            }
            return false;
        }
        else
            return true;
    }

    public boolean peut_sortir_droite(int x ,int y){
        if(m.isEmpty() || lvl.getElem(y, lvl.getSize()-1).getClass() == Wall.class)
            return false;
        
        if(x+1<lvl.getSize() && !lvl.can_move_right(x, y))
            return peut_sortir_droite(x+1, y);

        Matrice pere=m.peek();
        int xp=pere.getWrldX(), yp=pere.getWrldY();

        if (xp+1>=pere.getSize() || pere.getElem(yp, xp+1).getClass() == Wall.class)
            return false;
        else if(pere.getElem(yp, xp+1).getClass() == Box.class || pere.getElem(yp, xp+1).getClass() == Matrice.class){
            if(pere.can_move_right(xp+1, yp)){
                pere.move_right(xp+1, yp);
                return true;
            }
            return false;
        }
        else
            return true;
    }

    /*
     * Les méthodes sort_(dir) change la pile 'm' (utiliser la methode pop) et mettent le joueur dans la matrice pere
     * grace a setElem (le joueur et le vide est une instance de DrawLevel)
     * Il faut verifier si il y a un obstacle si oui le deplacer puis faire les setElem
     * ATTENTION LES METHODES peut_sortir_(dir) renvoie true
     */

    public void sort_haut(int x, int y) {
    	if(!peut_sortir_haut(x, y)) {
            System.out.print("can't move there\n");
    		return;
    	}
        if(y>0){
            sort_haut(x, y-1);
            lvl.move_up(x, y);
            return;
        }

    	Matrice pere = m.peek();
    	int xp = pere.getWrldX(), yp = pere.getWrldY();
    	
        Vide temp = (Vide) pere.getElem(yp-1, xp);		
        pere.setElem(yp-1, xp, lvl.getElem(y, x));
		lvl.setElem(y,x,temp);

        if(x==lvl.getPos_x() && y==lvl.getPos_y()){
            lvl.setPos_x(-1); lvl.setPos_y(-1);
    		pere.setPos_x(xp); pere.setPos_y(yp-1);
	    	pere.setWrldX(-1); pere.setWrldY(-1);

            pere.setIsHere(true);
            lvl.setIsHere(false);
            m.pop();
        }
    }

    public void sort_bas(int x, int y) {
        if(!peut_sortir_bas(x, y)) {
            System.out.print("can't move there bas\n");
    		return;
    	}
        if(y<lvl.getSize()-1){
            sort_bas(x, y+1);
            lvl.move_down(x, y);
            return;
        }

    	Matrice pere = m.peek();
    	int xp = pere.getWrldX(), yp = pere.getWrldY();
    	
        Vide temp = (Vide) pere.getElem(yp+1, xp);		
        pere.setElem(yp+1, xp, lvl.getElem(y, x));
		lvl.setElem(y,x,temp);

        if(x==lvl.getPos_x() && y==lvl.getPos_y()){
            lvl.setPos_x(-1); lvl.setPos_y(-1);
    		pere.setPos_x(xp); pere.setPos_y(yp+1);
	    	pere.setWrldX(-1); pere.setWrldY(-1);

            pere.setIsHere(true);
            lvl.setIsHere(false);
            m.pop();
        }
    }

    public void sort_gauche(int x, int y) {
        if(!peut_sortir_gauche(x, y)) {
            System.out.print("can't move there\n");
    		return;
    	}
        if(x>0){
            sort_gauche(x-1, y);
            lvl.move_left(x, y);
            return;
        }

    	Matrice pere = m.peek();
    	int xp = pere.getWrldX(), yp = pere.getWrldY();
    	
        Vide temp = (Vide) pere.getElem(yp, xp-1);		
        pere.setElem(yp, xp-1, lvl.getElem(y, x));
		lvl.setElem(y,x,temp);

        if(x==lvl.getPos_x() && y==lvl.getPos_y()){
            lvl.setPos_x(-1); lvl.setPos_y(-1);
    		pere.setPos_x(xp-1); pere.setPos_y(yp);
	    	pere.setWrldX(-1); pere.setWrldY(-1);

            pere.setIsHere(true);
            lvl.setIsHere(false);
            m.pop();
        }
    }

    public void sort_droite(int x ,int y) {
        if(!peut_sortir_droite(x, y)) {
            System.out.print("can't move there\n");
    		return;
    	}
        if(x<lvl.getSize()-1){
            sort_droite(x+1, y);
            lvl.move_right(x, y);
            return;
        }

    	Matrice pere = m.peek();
    	int xp = pere.getWrldX(), yp = pere.getWrldY();
    	
        Vide temp = (Vide) pere.getElem(yp, xp+1);		
        pere.setElem(yp, xp+1, lvl.getElem(y, x));
		lvl.setElem(y,x,temp);

        if(x==lvl.getPos_x() && y==lvl.getPos_y()){
            lvl.setPos_x(-1); lvl.setPos_y(-1);
    		pere.setPos_x(xp+1); pere.setPos_y(yp);
	    	pere.setWrldX(-1); pere.setWrldY(-1);

            pere.setIsHere(true);
            lvl.setIsHere(false);
            m.pop();
        }
    }

    //on reinitialise toute les matrices et on dit que la matrice lvl est la matrice a afficher
    public void resetAll() {
        lvl.reset();    
        matriceP.reset();
 
        for (int i = 0; i < matrice.length; i++)
            matrice[i].reset();

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

    //Methode qui permet de savoir quelle est la matrice ou ce trouve le joueur.
    public void setPrincipale() {
        if(matriceP.isHere())
            lvl=matriceP;
        for (int i = 0; i < matrice.length; i++) {
            if(matrice[i].isHere())
                lvl=matrice[i];
        }
    }
}
