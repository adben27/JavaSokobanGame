package package_sokoban;

import java.awt.*;

import javax.swing.*;

public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera la boucle de jeu
    private static final int FPS=60;//nombre de FPS du jeu
    private int sizeImg, next;//taille des images et pour savoir si on lance le prochain niveau 

    //image que l'on va afficher les "monde(x)" seront utiliser pour la version classique
    private Image mur, vide, cible, mondeB, mondeC, mondeD, mondeE,
                  mondeF, mondeG, mondeH, mondeI, mondeJ, joueur;

    private boolean haut, bas, gauche, droite, ctrlZ;//pour faire bouger le joueur

    //matrice que l'on va utiliser pour la version recursive
    public Matrice lvl, matriceB, matriceC, matriceD, matriceE,
                    matriceF, matriceG, matriceH, matriceI, matriceJ;

    private Player p;//Le joueur

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
        
        Vide v = new Vide(false);
        Vide y = new Vide(true);

        Element[][] tab_b={{m,m,m,m,v,m},
                           {m,v,v,v,v,m},
                           {m,v,v,v,v,m},
                           {m,v,v,b,v,m},
                           {v,v,v,v,v,m},
                           {m,m,m,m,m,m}};

        Element[][] tab_c={{m,v,m,v,v,m,m},
                           {m,m,v,c,v,v,v},
                           {m,v,v,v,v,m,m},
                           {m,m,v,v,v,m,m},
                           {m,m,m,m,m,m,m}};

        Element[][] tab_d={{m,v,m,m,m},
                           {m,v,v,m,m},
                           {m,d,v,v,v},
                           {m,m,v,v,m},
                           {m,m,m,m,m}};

        Element[][] tab_e={{e,v},
                           {v,m}};

        Element[][] tab_f={{m,m,m,v,m},
                           {m,f,v,v,m},
                           {m,v,v,v,m},
                           {m,v,v,v,m},
                           {m,m,m,m,m}};

        Element[][] tab_g={{m,v,m,m,m,m,v,m,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,g,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,m,m,m,m,m,m,m,m}};

        Element[][] tab_h={{m,v,m},
                           {m,h,m},
                           {m,m,m}};

        Element[][] tab_i={{i}};

        Element[][] tab_j={{m,v,m,m,m},
                           {v,j,v,m,m},
                           {m,v,v,m,m},
                           {m,v,m,m,m},
                           {m,m,m,m,m}};

        matriceB=new Matrice("B", 'b', false, tab_b.length, tab_b, 0, 0, false, false,-1,-1);
        matriceC=new Matrice("C", 'c', false, tab_c.length, tab_c, 0, 0, false, false,-1,-1);
        matriceD=new Matrice("D", 'd', false, tab_d.length, tab_d, 0, 0, false, false,-1,-1);
        matriceE=new Matrice("E", 'e', false, tab_e.length, tab_e, 0, 0, false, false,-1,-1);
        matriceF=new Matrice("F", 'f', false, tab_f.length, tab_f, 0, 0, false, false,-1,-1);
        matriceG=new Matrice("G", 'g', false, tab_g.length, tab_g, 0, 0, false, false,-1,-1);
        matriceH=new Matrice("H", 'h', false, tab_h.length, tab_h, 0, 0, false, false,-1,-1);
        matriceI=new Matrice("I", 'i', false, tab_i.length, tab_i, 0, 0, false, false,-1,-1);
        matriceJ=new Matrice("J", 'j', false, tab_j.length, tab_j, 0, 0, false, false,-1,-1);
        
        Element[][] tab={{m,m,m,m,m,m,m},
                         {m,p,v,matriceB,matriceC,v,m},
                         {m,v,v,matriceJ,v,matriceF,m},
                         {m,v,v,v,v,matriceI,m},
                         {m,matriceE,v,y,v,matriceH,m},
                         {m,v,matriceG,v,v,matriceD,m},
                         {m,m,m,m,m,m,m}};
        
        lvl=new Matrice("lvl", 'l', false, tab.length, tab,1,1, true, true,-1,-1);
        
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
        if (bas) {
            lvl.move_down();
            bas=false;
        }
        if (haut) {
            lvl.move_up();
            haut=false;
        }
        if (gauche) {
            lvl.move_left();
            gauche=false;
        }
        if (droite) {
            lvl.move_right();
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

        for (int i = 0; i < lvl.getSize(); i++) {
            for (int j = 0; j < lvl.getSize(); j++) {
                pos_x=((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2);
                pos_y=((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2);
                
                g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, this);
                
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
                    g2.drawImage(vide, pos_x, pos_y, sizeImg, sizeImg, this);
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
        int pos_y, pos_x, size;
        
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
    public void resetAll() {
        lvl.reset();
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
}