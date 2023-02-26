//package package_sokoban;

import java.awt.*;
import javax.swing.*;

public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera la boucle de jeu
    private static final int FPS=60;//nombre de FPS du jeu
    private static final int sizeImg=32;//taille des images

    //image que l'on va afficher les "monde(x)" seront utiliser pour la version classique
    private Image mur, vide, cible, mondeB, mondeC, mondeD, mondeE,
                  mondeF, mondeG, mondeH, mondeI, mondeJ, joueur;
    private boolean haut, bas, gauche, droite;//pour faire bouger le joueur

     //matrice que l'on va utiliser pour la version recursive (il ne doit y avoir que du vide, des murs et des cibles dans ces matrice JE PENSE)
    private Matrice lvl, matriceB, matriceC, matriceD, matriceE,
                    matriceF, matriceG, matriceH, matriceI, matriceJ;

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

        Player p = new Player(false);

        Vide v = new Vide(false);

        Element[][] tab={{m,m,m,m,m,m,m},{m,d,v,j,v,v,m},{m,v,p,v,c,v,m},{m,b,v,v,v,i,m},{m,v,e,v,v,h,m},{m,v,g,v,f,v,m},{m,m,m,m,m,m,m}};
        Element[][] tab_b={{b}};
        Element[][] tab_c={{m,v,m,m,v,v,m,m},{v,m,m,m,c,m,m,v},{m,v,v,m,m,v,m,m},{m,m,v,m,v,m,m,m},{m,v,v,m,m,v,m,m},{c,v,m,m,c,v,m,m},{m,v,m,v,v,m,m,m},{m,m,m,m,m,m,m,m}};
        Element[][] tab_d={{m,v,m,m},{m,d,m,m},{m,m,m,m},{m,m,m,m}};
        Element[][] tab_e={{e,v},{v,e}};
        Element[][] tab_f={{m,v,m,m},{m,f,m,m},{m,m,m,m},{m,m,m,m}};
        Element[][] tab_g={{m,v,m,m},{m,g,m,m},{m,m,m,m},{m,m,m,m}};
        Element[][] tab_h={{m,v,m,m},{m,h,m,m},{m,m,m,m},{m,m,m,m}};
        Element[][] tab_i={{i}};
        Element[][] tab_j={{m,v,m,m},{v,j,v,m},{m,v,v,m},{m,v,m,m}};

        lvl=new Matrice("lvl",tab.length, tab,2,2);
        matriceB=new Matrice("B", tab_b.length, tab_b, 0, 0);
        matriceC=new Matrice("C", tab_c.length, tab_c, 0, 0);
        matriceD=new Matrice("D", tab_d.length, tab_d, 0, 0);
        matriceE=new Matrice("E", tab_e.length, tab_e, 0, 0);
        matriceF=new Matrice("F", tab_f.length, tab_f, 0, 0);
        matriceG=new Matrice("G", tab_g.length, tab_g, 0, 0);
        matriceH=new Matrice("H", tab_h.length, tab_h, 0, 0);
        matriceI=new Matrice("I", tab_i.length, tab_i, 0, 0);
        matriceJ=new Matrice("J", tab_j.length, tab_j, 0, 0);

        //on recupère les images qu'on va utiliser
        mur = getToolkit().getImage("Image/mur.png");
        vide = getToolkit().getImage("Image/vide.png");
        cible = getToolkit().getImage("Image/cible.png");
        mondeB = getToolkit().getImage("Image/mondeB.png");
        mondeC = getToolkit().getImage("Image/mondeC.png");
        mondeD = getToolkit().getImage("Image/mondeD.png");
        mondeE = getToolkit().getImage("Image/mondeE.png");
        mondeF = getToolkit().getImage("Image/mondeF.png");
        mondeG = getToolkit().getImage("Image/mondeG.png");
        mondeH = getToolkit().getImage("Image/mondeH.png");
        mondeI = getToolkit().getImage("Image/mondeI.png");
        mondeJ = getToolkit().getImage("Image/mondeJ.png");
        joueur = getToolkit().getImage("Image/joueur.png");

        //on met tout a false pour pas bouger le joueur
        haut=bas=gauche=droite=false;
    }

    //methode qui charge un niveau (a modifier pour pouvoir lire les niveaux dans un fichier)
    public char[][] loadLvl() {
        return null;
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
            update();//met à jour le niveau en memoire
            repaint();//repeint le niveau dans le panel
            
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
            lvl.move_down();;
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
    }

    //on peint le niveau dans le panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        for (int i = 0; i < lvl.getSize(); i++) {
        		for (int j = 0; j < lvl.getSize(); j++) {
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);

                    if(lvl.getElem(i,j).getSign()=='A'||lvl.getElem(i,j).getSign()=='a'){
                        g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);
                        g2.drawImage(joueur, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);
                    }
                    if(lvl.getElem(i,j).getSign()=='B'||lvl.getElem(i,j).getSign()=='b'){
                        paintMonde(g2, matriceB, mondeB, 'B', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='C'||lvl.getElem(i,j).getSign()=='c'){
                        paintMonde(g2, matriceC, mondeC, 'C', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='D'||lvl.getElem(i,j).getSign()=='d'){
                        paintMonde(g2, matriceD, mondeD, 'D', i, j);;
                    }
                    if(lvl.getElem(i,j).getSign()=='E'||lvl.getElem(i,j).getSign()=='e'){
                        paintMonde(g2, matriceE, mondeE, 'E', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='F'||lvl.getElem(i,j).getSign()=='f'){
                        paintMonde(g2, matriceF, mondeF, 'F', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='G'||lvl.getElem(i,j).getSign()=='g'){
                        paintMonde(g2, matriceG, mondeG, 'G', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='H'||lvl.getElem(i,j).getSign()=='h'){
                        paintMonde(g2, matriceH, mondeH, 'H', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='I'||lvl.getElem(i,j).getSign()=='i'){
                        paintMonde(g2, matriceI, mondeI, 'I', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='J'||lvl.getElem(i,j).getSign()=='j'){
                        paintMonde(g2, matriceJ, mondeJ, 'J', i, j);
                    }
                    if(lvl.getElem(i,j).getSign()=='@'){
                        g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);
                        g2.drawImage(cible, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);
                    }
                    if(lvl.getElem(i,j).getSign()=='#'){
                        g2.drawImage(mur, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2), ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2), sizeImg, sizeImg, this);
                    }
                }
        }
    }

    /*A MODIFIER POUR AFFICHER TOUT LES MONDES (le parametre "sign" et "img" vont disparaitre)
     * "m" est la matrice a afficher
     * "img" est le monde a afficher
     * "sign est le signe du monde afficher"
     * "i" et "j" sont les coordonnées (j,i) de la location où il faut dessiner  
     */
    public void paintMonde(Graphics2D g2, Matrice m, Image img, char sign, int i, int j) {
        for (int y = 0; y < m.getSize(); y++){
            for (int x = 0; x < m.getSize(); x++){
                if (m.getElem(y, x).getSign()=='#'){
                    g2.drawImage(mur, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }   
                if (m.getElem(y, x).getSign()==' '){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if (m.getElem(y, x).getSign()==sign){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(img, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);  
                }
            }
        }
    }

    //permet les mouvements (dit si on a appuiez sur les fleches ou les bouttons)
    public void setHaut(boolean dir) {
        haut=dir;
    }
    public void setBas(boolean dir) {
        bas=dir;
    }
    public void setGauche(boolean dir) {
        gauche=dir;
    }
    public void setDroite(boolean dir) {
        droite=dir;
    }
}