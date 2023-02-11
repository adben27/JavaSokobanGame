package package_sokoban

import java.awt.Dimension;
import java.awt.*;
import javax.swing.*;

//classe Img (image) qui herite de JLabel sert pour le Jpanel information
public class Img extends JLabel{

    private Image img;
    private String chemin;

    public Img(String chemin, int x, int y) {
        super(new ImageIcon(chemin)); //on creer un JLabel qui contiendra l'image que l'on veut afficher
        img = getToolkit().getImage(chemin);

        setPreferredSize(new Dimension(20,20));
        setSize(20, 20); //Le label fera 20 pixels de longueur et de larguer

        setLocation(x, y); //on positionne le label en (x,y)
    }

    public Img(String chemin) {
        super(new ImageIcon(chemin)); //on creer une JLabel qui contiendra l'image que l'on veut afficher
        img = getToolkit().getImage(chemin);

        setSize(20, 20); //Le label fera 20 pixels de longueur et de larguer
        setPreferredSize(new Dimension(20,20));
    }

    public String getChemin() {
        return chemin;
    }

    public Image getImage(){
        return img;
    }
}
