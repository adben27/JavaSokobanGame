package package_sokoban 

import javax.swing.*;

//classe Img (image) qui herite de JLabel sert pour le Jpanel information et pour bouger le joueur
public class Img extends JLabel{

    private String chemin;

    //constructeur pour le conteneur d'information
    public Img(String chemin, int x, int y) {
        super(new ImageIcon(chemin)); //on creer une JLabel qui contiendra le niveau que l'on veut afficher

        setSize(20, 20); //Le label fera 20 pixels de longueur et de larguer

        setLocation(x, y); //on positionne le label en (x,y)
    }

    //constructeur pour le conteneur d'information 
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

    public String getChemin() {
        return chemin;
    }
}
