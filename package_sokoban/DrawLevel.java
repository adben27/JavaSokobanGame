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
    private Stack<Matrice> m;

    //private Stack<Matrice> m;

    public DrawLevel() {
        super();
        setLayout(null);

        m=new Stack<>();

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
        tab_c[4][4]=matriceH;

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

        int pos_x=((getWidth() - sizeImg)/2)-sizeImg*(lvl.getSize()/2),
            pos_y=((getHeight() - sizeImg)/2)-sizeImg*(lvl.getSize()/2);

        paintBordure(g2, pos_x, pos_y);

        g2.drawImage(vide, pos_x, pos_y, sizeImg*lvl.getSize(), sizeImg*lvl.getSize(), lvl.getColor(), this);

        for (int i = 0; i < lvl.getSize(); i++) {
            for (int j = 0; j < lvl.getSize(); j++) {
                pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);
                pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2);
                e=lvl.getElem(i, j);
                
                if(e.getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y, sizeImg, sizeImg, this);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='B'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='b') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='C'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='c') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='D'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='d') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='E'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='e') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='F'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='f') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='G'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='g') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='H'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='h') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='I'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='i') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='J'){
                    M_ou_B(g2, e, i, j);
                }
                if (e.getSign()=='j') {
                    M_ou_B(g2, e, i, j);
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
                }
                if(e.getSign()=='#'){
                    g2.drawImage(mur, pos_x, pos_y, sizeImg, sizeImg, this);
                }
            }
        }
    }

    /* "m" est la matrice a afficher
     * "i" et "j" sont les coordonnées (j,i) de la localisation où il faut dessiner  
     */
    public void paintMonde(Graphics2D g2, Matrice m, int i, int j, int pos_x, int pos_y) {
        int size = sizeImg/m.getSize();
        Element e;

        g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, m.getColor(), this);
        
        for (int y = 0; y < m.getSize(); y++){
            for (int x = 0; x < m.getSize(); x++){
                pos_x = ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + size*x;
                pos_y = ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + size*y;
                e=m.getElem(y,x);

                if(e.getSign()=='A'){
                    g2.drawImage(joueur, pos_x, pos_y, size, size, this);
                }
                if(e.getSign()=='a'){
                    g2.drawImage(joueur, pos_x, pos_y , size, size, this);
                    g2.drawImage(cible, pos_x, pos_y , size, size, this);
                }
                if (m.getElem(y, x).getSign()=='#'){
                    g2.drawImage(mur, pos_x,  pos_y , size, size, this);
                }
                if(e.getSign()=='@'){
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(cible, pos_x, pos_y, size, size, this);
                }
                if(e.getSign()=='B'){
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(mondeB, pos_x, pos_y, size, size, this);
                }
                if (e.getSign()=='b') {
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(mondeB, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='C'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeC, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='c') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeC, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='D'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeD, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='d') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeD, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='E'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeE, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='e') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeE, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='F'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeF, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='f') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeF, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='G'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeG, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='g') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeG, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='H'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeH, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='h') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeH, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='I'){
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeI, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='i') {
                    g2.drawImage(vide,pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeI,pos_x , pos_y, size, size, this);
                    g2.drawImage(cible,pos_x , pos_y, size, size, this);
                }
                if(e.getSign()=='J'){
                    g2.drawImage(vide, pos_x, pos_y, size, size, this);
                    g2.drawImage(mondeJ, pos_x , pos_y, size, size, this);
                }
                if (e.getSign()=='j') {
                    g2.drawImage(vide, pos_x , pos_y, size, size, this);
                    g2.drawImage(mondeJ, pos_x , pos_y, size, size, this);
                    g2.drawImage(cible, pos_x , pos_y, size, size, this);
                }
            }
        }
    }

    public void M_ou_B(Graphics2D g2, Element e, int i, int j) {
        int pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2),
            pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);

        g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, lvl.getColor(), this);

        if(e.getClass() == Box.class){
            if(e.getSign()=='B'){
                g2.drawImage(mondeB, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='b') {
                g2.drawImage(mondeB, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='C'){
                g2.drawImage(mondeC, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='c') {
                g2.drawImage(mondeC, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='D'){
                g2.drawImage(mondeD, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='d') {
                g2.drawImage(mondeD, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='E'){
                g2.drawImage(mondeE, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='e') {
                g2.drawImage(mondeE, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='F'){
                g2.drawImage(mondeF, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='f') {
                g2.drawImage(mondeF, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='G'){
                g2.drawImage(mondeG, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='g') {
                g2.drawImage(mondeG, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='H'){
                g2.drawImage(mondeH, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='h') {
                g2.drawImage(mondeH, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='I'){
                g2.drawImage(mondeI, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='i') {
                g2.drawImage(mondeI,pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible,pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if(e.getSign()=='J'){
                g2.drawImage(mondeJ, pos_x, pos_y, sizeImg, sizeImg, this);
            }
            if (e.getSign()=='j') {
                g2.drawImage(mondeJ, pos_x, pos_y, sizeImg, sizeImg, this);
                g2.drawImage(cible, pos_x, pos_y, sizeImg, sizeImg, this);
            }
        }else{
            paintMonde(g2, (Matrice) e, i, j, pos_x, pos_y);
        }
    }

    public void paintBordure(Graphics2D g2, int lvl_x, int lvl_y){
        if(m.isEmpty())
            return;

        Matrice pere = m.peek();
        Element e;
        Color c = pere.getColor();
        int ecart=sizeImg*lvl.getSize();
        int pos_y=lvl_y-ecart, pos_x=lvl_x-ecart, a=-2, b=-2;

        for (int i = pere.getWrldY()-2; i <= pere.getWrldY()+2; i++) {
            for (int j = pere.getWrldX()-2; j <= pere.getWrldX()+2; j++) {

                if(i<0 || i>=pere.getSize() || j<0 || j>=pere.getSize())
                    continue;
                
                if(i==pere.getWrldY()-2) pos_y=lvl_y-ecart-ecart;
                if(i==pere.getWrldY()-1) pos_y=lvl_y-ecart;
                if(i==pere.getWrldY()) pos_y=lvl_y;
                if(i==pere.getWrldY()+1) pos_y= lvl_y+ecart;
                if(i==pere.getWrldY()+2) pos_y=lvl_y+ecart+ecart;

                if(j==pere.getWrldX()-2) pos_x=lvl_x-ecart-ecart;
                if(j==pere.getWrldX()-1) pos_x=lvl_x-ecart;
                if(j==pere.getWrldX()) pos_x=lvl_x;
                if(j==pere.getWrldX()+1) pos_x= lvl_x+ecart;
                if(j==pere.getWrldX()+2) pos_x=lvl_x+ecart+ecart;

                e=pere.getElem(i, j);

                if(e.getSign()==' '){
                    g2.drawImage(vide, pos_x, pos_y, ecart, ecart, c, this);
                }
                if(e.getSign()=='#'){
                    g2.drawImage(mur, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='@'){
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, c, this);
                }
                if(e.getSign()=='B'){
                    g2.drawImage(mondeB, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='b') {
                    g2.drawImage(mondeB, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='C'){
                    g2.drawImage(mondeC, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='c') {
                    g2.drawImage(mondeC, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='D'){
                    g2.drawImage(mondeD, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='d') {
                    g2.drawImage(mondeD, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='E'){
                    g2.drawImage(mondeE, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='e') {
                    g2.drawImage(mondeE, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='F'){
                    g2.drawImage(mondeF, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='f') {
                    g2.drawImage(mondeF, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='G'){
                    g2.drawImage(mondeG, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='g') {
                    g2.drawImage(mondeG, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='H'){
                    g2.drawImage(mondeH, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='h') {
                    g2.drawImage(mondeH, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='I'){
                    g2.drawImage(mondeI, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='i') {
                    g2.drawImage(mondeI,pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible,pos_x, pos_y, ecart, ecart, this);
                }
                if(e.getSign()=='J'){
                    g2.drawImage(mondeJ, pos_x, pos_y, ecart, ecart, c, this);
                }
                if (e.getSign()=='j') {
                    g2.drawImage(mondeJ, pos_x, pos_y, ecart, ecart, c, this);
                    g2.drawImage(cible, pos_x, pos_y, ecart, ecart, this);
                }
                b++;
            }
            a++;
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
     * Methodes qui permet de savoir quelle est la matrice ou ce trouve le joueur et met l'ancienne matrice dans la pile 'm'
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