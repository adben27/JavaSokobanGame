package package_sokoban

import java.awt.*;

import javax.swing.*;
 
public class DrawLevel extends JPanel{

    private int joueur_x, joueur_y;
    private Image mur, vide, cible, monde;
    private Img joueur;
    private int n=7; //taille niveau
    //niveau a afficher
    private char[] lvl={'#', '#', '#', '#', '#', '#', '#', 
                        '#', ' ', '@', '@', 'B', ' ', '#',
                        '#', ' ', '#', 'B', ' ', ' ', '#',
                        '#', '@', '#', '#', ' ', 'B', '#',
                        '#', 'A', 'B', ' ', ' ', ' ', '#',
                        '#', '#', ' ', ' ', '#', '@', '#',
                        '#', '#', '#', '#', '#', '#', '#',
    };

    public DrawLevel() {
        super();

        setLayout(null);
        setPreferredSize(new Dimension(600, 400));
        setSize(600, 400);

        joueur = new Img("Image/joueur.png");

        mur = getToolkit().getImage("Image/mur.png");
        vide = getToolkit().getImage("Image/vide.png");
        cible = getToolkit().getImage("Image/cible.png");
        monde = getToolkit().getImage("Image/monde.png");

        int i,j;
        i=j=0;

        while (i<n && !(lvl[j*n+i]=='A')) {
            while (j<n && !(lvl[j*n+i]=='A')) {
                if(lvl[i*n+j]=='A'){
                    joueur_x=((getWidth() - joueur.getWidth())/2)+20*((5*j)-(n/2))-185;
                    joueur_y=((getHeight() - joueur.getHeight())/2)+20*((2*j)-(n/2))-9;
                }
                j++;
            }
            i++;
            j=0;
        }

        joueur.setLocation(joueur_x, joueur_y);
        add(joueur);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                
                if((lvl[n*j+i]==' ') || (lvl[n*j+i]=='A')){
                    g.drawImage(vide, ((getWidth() - vide.getWidth(null))/2)+20*i-20*n/2, ((getHeight() - vide.getHeight(null))/2)+j*20-20*n/2, this);
                }
                if(lvl[n*j+i]=='B'){
                    g.drawImage(monde, ((getWidth() - monde.getWidth(null))/2)+20*i-20*n/2, ((getHeight() - monde.getHeight(null))/2)+j*20-20*n/2, this);
                }
                if(lvl[n*j+i]=='@'){
                    g.drawImage(cible, ((getWidth() - cible.getWidth(null))/2)+20*i-20*n/2, ((getHeight() - cible.getHeight(null))/2)+j*20-20*n/2, this);
                }
                if(lvl[n*j+i]=='#'){
                    g.drawImage(mur, ((getWidth() - mur.getWidth(null))/2)+20*i-20*n/2, ((getHeight() - mur.getHeight(null))/2)+j*20-20*n/2, this);
                }
            }
        }
    }

    public int getJoueur_x(){
        return joueur_x;
    }

    public int getJoueur_y(){
        return joueur_y;
    }

    public Img getJoueur(){
        return joueur;
    }
}
