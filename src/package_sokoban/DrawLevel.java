package package_sokoban;

import java.awt.*;
import javax.swing.*;
 
public class DrawLevel extends JPanel implements Runnable{

    private Thread game; //creer un thread qui sera ma boucle de jeu
    private int FPS=60;//nombre de FPS du jeu et indice (x,y) du joueur
    private Image mur, vide, cible, mondeB, mondeC, joueur;//image que l'on va afficher
    private boolean haut, bas, gauche, droite;//pour faire bouger le joueur
    private Matrice lvl;

    public DrawLevel() {
        super();

        setLayout(null);
        setPreferredSize(new Dimension(600, 400));
        Wall a= new Wall();
        Wall b= new Wall();
        Wall c= new Wall();
        Wall d= new Wall();
        Wall e= new Wall();
        Wall h= new Wall();
        Wall i= new Wall();
        Wall l= new Wall();
        Wall m= new Wall();
        Wall n= new Wall();
        Wall o= new Wall();
        Wall p= new Wall();

        Player f = new Player(false);

        Vide j = new Vide(false);
        Vide g = new Vide(false);
        Vide k = new Vide(true);
        Element[][] tab={{a,b,c,d},{e,f,g,h},{i,j,k,l},{m,n,o,p}};
        Matrice mat = new Matrice("test", 4, tab, 1, 1);

        lvl=new Matrice("test",4,tab,1,1);

        //on recupère les images qu'on va utiliser
        mur = getToolkit().getImage("Image/mur.png");
        vide = getToolkit().getImage("Image/vide.png");
        cible = getToolkit().getImage("Image/cible.png");
        mondeB = getToolkit().getImage("Image/mondeB.png");
        mondeC = getToolkit().getImage("Image/mondeC.png");
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
            
            //pour verifie si la pos du joueur en memoire correspond a la pos du joueur dans l'interface graphique
            //System.out.println("joueur_x:" + joueur_x + "\njoueur_y" + joueur_y + "\n");

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
    }

    //on peint le niveau dans le panel
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        
        for (int i = 0; i < lvl.getSize(); i++) {
        		for (int j = 0; j < lvl.getSize(); j++) {
                
                if(lvl.getElem(i,j).getSign()==' '){
                    g2.drawImage(vide, ((getWidth() - vide.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - vide.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                }
                if(lvl.getElem(i,j).getSign()=='A'||lvl.getElem(i,j).getSign()=='a'){
                    g2.drawImage(vide, ((getWidth() - vide.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - vide.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                    g2.drawImage(joueur, ((getWidth() - joueur.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - joueur.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                }
                if(lvl.getElem(i,j).getSign()=='B'){
                    g2.drawImage(vide, ((getWidth() - vide.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - vide.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                    g2.drawImage(mondeB, ((getWidth() - mondeB.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - mondeB.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                }
                if(lvl.getElem(i,j).getSign()=='C'){
                    g2.drawImage(vide, ((getWidth() - vide.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - vide.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                    g2.drawImage(mondeC, ((getWidth() - mondeC.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - mondeC.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                }
                if(lvl.getElem(i,j).getSign()=='X'){
                    g2.drawImage(vide, ((getWidth() - mondeC.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - mondeC.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                    g2.drawImage(cible, ((getWidth() - cible.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - cible.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
                }
                if(lvl.getElem(i,j).getSign()=='#'){
                    g2.drawImage(mur, ((getWidth() - mur.getWidth(null))/2)+20*(j-lvl.getSize()/2), ((getHeight() - mur.getHeight(null))/2)+20*(i-lvl.getSize()/2), this);
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
