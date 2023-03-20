package package_sokoban;

import java.awt.*;
import java.util.Stack;

import javax.swing.*;

public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera la boucle de jeu
    private static final int FPS=60;//nombre de FPS du jeu
    private int sizeImg, next;//taille des images et pour savoir si on lance le prochain niveau 

    //image que l'on va afficher les "monde(x)" seront utiliser pour la version classique
    private Image mur, vide, cible, mondeB, mondeC, mondeD, mondeE,
                  mondeF, mondeG, mondeH, mondeI, mondeJ, joueur;

    private boolean haut, bas, gauche, droite, ctrlZ;//pour faire bouger le joueur

    /* Matrice que l'on va utiliser pour la version recursive
     * lvl sera la matrice que l'on va afficher (changera a chaque appel des methodes de deplacement)
     * matriceP sera la matrice qui contient le 1er monde lors du démarage du jeu
     */
    public Matrice lvl, matriceP, matriceB, matriceC, matriceD, matriceE,
                    matriceF, matriceG, matriceH, matriceI, matriceJ;

    private Player p;//Le joueur
    private Vide v;

    //private Stack<Matrice> m;

    public DrawLevel() {
        super();
        setLayout(null);

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
        
        Element[][] tab_lvl={{m,m,m,m,m,m},
                             {m,v,p,v,v,v},
                             {m,v,v,v,v,m},
                             {m,v,v,v,v,m},
                             {m,v,matriceB,matriceC,matriceE,m},
                             {m,m,m,m,m,m}};
        
        lvl=new Matrice("lvl", 'l', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);
        matriceP=new Matrice("P", 'p', false, tab_lvl.length, tab_lvl,2,1, true, true,-1,-1);

        //taille des images
        sizeImg=(int)getToolkit().getScreenSize().getHeight()/lvl.getSize()-20;

        //on recupère les images qu'on va utiliser
        mur = getToolkit().getImage("package_sokoban/Image/mur.png");
        vide = getToolkit().getImage("package_sokoban/Image/vide.png");
        cible = getToolkit().getImage("package_sokoban/Image/cible.png");
        mondeB = getToolkit().getImage("package_sokoban/Image/mondeB.png");
        mondeC = getToolkit().getImage("package_sokoban/Image/mondeC.png");
        mondeD = getToolkit().getImage("package_sokoban/Image/mondeD.png");
        mondeE = getToolkit().getImage("package_sokoban/Image/mondeE.png");
        mondeF = getToolkit().getImage("package_sokoban/Image/mondeF.png");
        mondeG = getToolkit().getImage("package_sokoban/Image/mondeG.png");
        mondeH = getToolkit().getImage("package_sokoban/Image/mondeH.png");
        mondeI = getToolkit().getImage("package_sokoban/Image/mondeI.png");
        mondeJ = getToolkit().getImage("package_sokoban/Image/mondeJ.png");
        joueur = getToolkit().getImage("package_sokoban/Image/joueur.png");

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
        getPrincipale();

        if (bas) {
            if(lvl.getPos_y()==lvl.getSize()-1){
                sort_bas();
                bas=false;
                return;
            }
            if (lvl.getElem(y+1, x).getClass() == Matrice.class)
                if(lvl.can_enter_up((Matrice) lvl.getElem(y+1, x)))
                    matriceP.getStackM().push(lvl);
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
                if(lvl.can_enter_down((Matrice) lvl.getElem(y-1, x)))
                    matriceP.getStackM().push(lvl);

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
                if(lvl.can_enter_right((Matrice) lvl.getElem(y, x-1)))
                    matriceP.getStackM().push(lvl);
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
                if(lvl.can_enter_left((Matrice) lvl.getElem(y, x+1)))
                    matriceP.getStackM().push(lvl);
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

        Graphics2D g2 = (Graphics2D) g;
        int pos_x,pos_y;

        g2.drawImage(vide, ((getWidth() - sizeImg)/2)-sizeImg*(lvl.getSize()/2), ((getHeight() - sizeImg)/2)-sizeImg*(lvl.getSize()/2), sizeImg*lvl.getSize(), sizeImg*lvl.getSize(), lvl.getColor(), this);

        for (int i = 0; i < lvl.getSize(); i++) {
            for (int j = 0; j < lvl.getSize(); j++) {
                pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);
                pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2);
                
                if(lvl.getElem(i,j).getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='B'){
                    paintMonde(g2, matriceB, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='b') {
                    paintMonde(g2, matriceB, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='C'){
                    paintMonde(g2, matriceC, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='c') {
                    paintMonde(g2, matriceC, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='D'){
                    paintMonde(g2, matriceD, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='d') {
                    paintMonde(g2, matriceD, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='E'){
                    paintMonde(g2, matriceE, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='e') {
                    paintMonde(g2, matriceE, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='F'){
                    paintMonde(g2, matriceF, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='f') {
                    paintMonde(g2, matriceF, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='G'){
                    paintMonde(g2, matriceG, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='g') {
                    paintMonde(g2, matriceG, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='H'){
                    paintMonde(g2, matriceH, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='h') {
                    paintMonde(g2, matriceH, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='I'){
                    paintMonde(g2, matriceI, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='i') {
                    paintMonde(g2, matriceI, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='J'){
                    paintMonde(g2, matriceJ, i, j);
                }
                if (lvl.getElem(i,j).getSign()=='j') {
                    paintMonde(g2, matriceJ, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(lvl.getElem(i,j).getSign()=='#'){
                    g2.drawImage(mur, pos_x, pos_y, sizeImg, sizeImg, this);
                }
            }
        }
    }

    /* "m" est la matrice a afficher
     * "i" et "j" sont les coordonnées (j,i) de la localisation où il faut dessiner  
     */
    public void paintMonde(Graphics2D g2, Matrice m, int i, int j) {
        int pos_y=  ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2),
            pos_x= ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2),
            size;

        if (m.getSize()==1)
            g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, lvl.getColor(), this);
        else
            g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, m.getColor(), this);
        
        for (int y = 0; y < m.getSize(); y++){
            for (int x = 0; x < m.getSize(); x++){
                size = sizeImg/m.getSize();
                pos_x = ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + size*x;
                pos_y = ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + size*y;

                if(m.getElem(y,x).getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y , size, size, this);
                    g2.drawImage(cible, pos_x, pos_y , size, size, this);
                }
                if (m.getElem(y, x).getSign()=='#'){
                    g2.drawImage(mur, pos_x,  pos_y , size, size, this);
                }
                if(m.getElem(y, x).getSign()=='@'){
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(cible, pos_x, pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='B'){
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(mondeB, pos_x, pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='b') {
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(mondeB, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='C'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeC, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='c') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeC, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='D'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeD, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='d') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeD, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='E'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeE, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='e') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeE, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='F'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeF, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='f') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeF, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='G'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeG, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='g') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeG, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='H'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeH, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='h') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeH, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='I'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeI, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='i') {
                    g2.drawImage(vide,pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeI,pos_x , pos_y, size, size, this);
                    g2.drawImage(cible,pos_x , pos_y, size, size, this);
                }
                if(m.getElem(y, x).getSign()=='J'){
                    g2.drawImage(vide,pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeJ, pos_x , pos_y, size, size, this);
                }
                if (m.getElem(y, x).getSign()=='j') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeJ, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
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
        Matrice pere=matriceP.getStackM().peek();//on recupère la matrice pere
        int x=pere.getWrldX(), y=pere.getWrldY();//on recupère les coordonnées (x, y) du monde ou se trouve le joueur

        if (pere.getElem(y-1, x).getClass() == Wall.class || matriceP.getStackM().isEmpty())
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
        Matrice pere=matriceP.getStackM().peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y+1, x).getClass() == Wall.class || matriceP.getStackM().isEmpty())
            return false;
        else if(pere.getElem(y+1, x).getClass() == Box.class || pere.getElem(y+1, x).getClass() == Matrice.class)
            return pere.can_move_down(x, y+1);
        else
            return true;
    }

    public boolean peut_sortir_gauche(){
        Matrice pere=matriceP.getStackM().peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y, x-1).getClass() == Wall.class || matriceP.getStackM().isEmpty())
            return false;
        else if(pere.getElem(y, x-1).getClass() == Box.class || pere.getElem(y, x-1).getClass() == Matrice.class)
            return pere.can_move_left(x-1, y);
        else
            return true;
    }

    public boolean peut_sortir_droite(){
        Matrice pere=matriceP.getStackM().peek();
        int x=pere.getWrldX(), y=pere.getWrldY();

        if (pere.getElem(y, x+1).getClass() == Wall.class || matriceP.getStackM().isEmpty())
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
        System.out.println(peut_sortir_haut());
    }

    public void sort_bas() {
        System.out.println(peut_sortir_bas());
    }

    public void sort_gauche() {
        System.out.println(peut_sortir_gauche());
    }

    public void sort_droite() {
        System.out.println(peut_sortir_droite());
    }

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
     * Methodes qui permet de savoir quelle est la matrice ou ce trouve le joueur et met l'ancienne matrice dans la pile 'm'
     */
    public void getPrincipale() {
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
    }
}