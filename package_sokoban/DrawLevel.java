package package_sokoban;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera la boucle de jeu
    private static final int FPS=60;//nombre de FPS du jeu
    private int sizeImg;//taille des images
    private JFrame felicitation;
    private JButton next;

    //image que l'on va afficher les "monde(x)" seront utiliser pour la version classique
    private Image mur, vide, cible, mondeB, mondeC, mondeD, mondeE,
                  mondeF, mondeG, mondeH, mondeI, mondeJ, joueur;
    private boolean haut, bas, gauche, droite, ctrlZ;//pour faire bouger le joueur

    //matrice que l'on va utiliser pour la version recursive (il ne doit y avoir que du vide, des murs et des cibles dans ces matrice JE PENSE)
    private Matrice lvl, matriceB, matriceC, matriceD, matriceE,
                    matriceF, matriceG, matriceH, matriceI, matriceJ;

    private Player p = new Player(false);

    public DrawLevel() {
        super();

        felicitation = new JFrame("Sokoban");
        next = new JButton("next");

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
        
        Vide v = new Vide();

        Element[][] tab={{m,m,m,m,m,m,m},
                         {m,v,v,v,c,v,m},
                         {m,v,p,j,v,f,m},
                         {m,v,v,v,v,i,m},
                         {m,v,e,v,v,h,m},
                         {m,v,g,d,v,b,m},
                         {m,m,m,m,m,m,m}};

        Element[][] tab_b={{m,m,m,m,v,m},
                           {m,v,v,v,v,m},
                           {m,v,v,v,v,m},
                           {m,v,b,v,v,m},
                           {v,v,v,v,v,m},
                           {m,m,m,m,m,m}};


        Element[][] tab_c={{m,v,m,v,v,m,m},
                           {m,m,v,c,m,m,v},
                           {m,v,v,v,v,m,m},
                           {m,m,v,v,v,m,m},
                           {m,m,m,m,m,m,m}};

        Element[][] tab_d={{m,v,m,m,m},
                           {m,d,v,m,m},
                           {m,m,v,m,v},
                           {m,m,m,v,m},
                           {m,m,m,m,m}};

        Element[][] tab_e={{e,v},
                           {v,e}};

        Element[][] tab_f={{m,m,m,v,m},
                           {m,f,v,v,m},
                           {m,v,v,v,m},
                           {m,v,v,v,m},
                           {m,m,m,m,m}};

        Element[][] tab_g={{m,v,m,m,m,m,v,m,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,h,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,g,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,v,v,v,v,v,v,v,m},
                           {m,m,m,m,m,m,m,m,m}};

        Element[][] tab_h={{m,v,m},
                           {m,h,m},
                           {m,m,m}};

        Element[][] tab_i={{i}};

        Element[][] tab_j={{m,v,m,m,m},
                           {v,j,v,m,m},
                           {m,v,h,m,m},
                           {m,v,m,m,m},
                           {m,m,m,m,m}};

        lvl=new Matrice("lvl", 'l', false,tab.length, tab,2,2);
        matriceB=new Matrice("B", 'b', false, tab_b.length, tab_b, 0, 0);
        matriceC=new Matrice("C", 'c', false, tab_c.length, tab_c, 0, 0);
        matriceD=new Matrice("D", 'd', false,tab_d.length, tab_d, 0, 0);
        matriceE=new Matrice("E", 'e', false,tab_e.length, tab_e, 0, 0);
        matriceF=new Matrice("F", 'f', false,tab_f.length, tab_f, 0, 0);
        matriceG=new Matrice("G", 'g', false,tab_g.length, tab_g, 0, 0);
        matriceH=new Matrice("H", 'h', false,tab_h.length, tab_h, 0, 0);
        matriceI=new Matrice("I", 'i', false,tab_i.length, tab_i, 0, 0);
        matriceJ=new Matrice("J", 'j', true,tab_j.length, tab_j, 0, 0);

        sizeImg=(int)getToolkit().getScreenSize().getHeight()/lvl.getSize()-20;

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

        felicitation.setIconImage(joueur);
        felicitation.setSize(350, 100);
        felicitation.add(panel_bravo());

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
        if (estFini()/*CONDITION A REMPLACER PAR LA METHODE QUI PERMET DE SAVOIR SI LE NIVEAU EST TERMINER */) {
            felicitation.setVisible(true);
        }
    }

    /*METHODE A METTRE DANS LA CLASSE MATRICE POUR VERIFIER SI LE NIVEAU EST TERMINER */
    public boolean estFini() {
        if (lvl.getPos_x()==4 && lvl.getPos_y()==3) {
            return true;
        }
        return false;
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
                    paintMonde(g2, matriceB, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='C'||lvl.getElem(i,j).getSign()=='c'){
                    paintMonde(g2, matriceC, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='D'||lvl.getElem(i,j).getSign()=='d'){
                    paintMonde(g2, matriceD, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='E'||lvl.getElem(i,j).getSign()=='e'){
                    paintMonde(g2, matriceE, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='F'||lvl.getElem(i,j).getSign()=='f'){
                    paintMonde(g2, matriceF, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='G'||lvl.getElem(i,j).getSign()=='g'){
                    paintMonde(g2, matriceG, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='H'||lvl.getElem(i,j).getSign()=='h'){
                    paintMonde(g2, matriceH, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='I'||lvl.getElem(i,j).getSign()=='i'){
                    paintMonde(g2, matriceI, i, j);
                }
                if(lvl.getElem(i,j).getSign()=='J'||lvl.getElem(i,j).getSign()=='j'){
                    paintMonde(g2, matriceJ, i, j);
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

    /* "m" est la matrice a afficher
     * "i" et "j" sont les coordonnées (j,i) de la localisation où il faut dessiner  
     */
    public void paintMonde(Graphics2D g2, Matrice m, int i, int j) {
        for (int y = 0; y < m.getSize(); y++){
            for (int x = 0; x < m.getSize(); x++){

                if(m.getElem(y, x).getSign()=='A'||m.getElem(y, x).getSign()=='a'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(joueur, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if (m.getElem(y, x).getSign()=='#'){
                    g2.drawImage(mur, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }   
                if (m.getElem(y, x).getSign()==' '){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='@'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(cible, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='B'||m.getElem(y, x).getSign()=='b'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeB, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='C'||m.getElem(y, x).getSign()=='c'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeC, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='D'||m.getElem(y, x).getSign()=='d'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeD, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='E'||m.getElem(y, x).getSign()=='e'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeE, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='F'||m.getElem(y, x).getSign()=='f'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeF, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='G'||m.getElem(y, x).getSign()=='g'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeG, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='H'||m.getElem(y, x).getSign()=='h'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeH, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='I'||m.getElem(y, x).getSign()=='i'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeI, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
                if(m.getElem(y, x).getSign()=='J'||m.getElem(y, x).getSign()=='j'){
                    g2.drawImage(vide, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                    g2.drawImage(mondeJ, ((getWidth() - sizeImg)/2)+sizeImg*(j-lvl.getSize()/2) + (sizeImg/m.getSize())*x, ((getHeight() - sizeImg)/2)+sizeImg*(i-lvl.getSize()/2) + (sizeImg/m.getSize())*y, sizeImg/m.getSize(), sizeImg/m.getSize(), this);
                }
            }
        }
    }

    public JPanel panel_bravo() {
        JPanel bravo = new JPanel(new BorderLayout());

        JTextArea gg = new JTextArea("Vous avez terminez le niveau.\nAppuiez sur le bouton 'next' pour passez au niveau suivant");
        gg.setEditable(false);

        felicitation.setPreferredSize(new Dimension(70, 30));
        next.addActionListener((e) -> nextListener(e));
        felicitation.add(next, BorderLayout.SOUTH);
        felicitation.add(gg, BorderLayout.NORTH);
        felicitation.setLocationRelativeTo(null);

        return bravo;
    }

    private void nextListener(ActionEvent e) {
        loadLvl();
        felicitation.dispose();
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