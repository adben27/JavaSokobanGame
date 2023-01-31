package package_sokoban 

import javax.swing.ImageIcon;
import javax.swing.JLabel;

//classe Img (image) qui herite de JLabel
public class Img extends JLabel{

    public Img(String chemin, int x, int y) {
        super(new ImageIcon(chemin)); //on creer une JLabel qui contiendra le niveau que l'on veut afficher

        setSize(20, 20); //Le label fera 20 pixels de longueur et de larguer
        setLocation(x, y); //on positionne le label en (x,y)
    }

    public Img(String chemin) {
        super(new ImageIcon(chemin)); //on creer une JLabel qui contiendra le niveau que l'on veut afficher

        setSize(20, 20); //Le label fera 20 pixels de longueur et de larguer
    }

    //methode de deplacement du joueur
    public void deplacementBas(){
        setLocation(getX(), getY()+20);
    }

    public void deplacementHaut(){
        setLocation(getX(), getY()-20);
    }

    public void deplacementGauche(){
        setLocation(getX()-20, getY());
    }

    public void deplacementDroite(){
        setLocation(getX()+20, getY());
    }

    //pour placer les mur, cible, etc..
    public void setPosition(int x, int y) {
        setLocation(x, y);
    }
}
