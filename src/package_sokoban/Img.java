package package_sokoban; 

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

//classe Img (image) qui herite de JPanel
public class Img extends JPanel{

    public Img() {
        super(); //on creer une JPanel qui contiendra le niveau que l'on veut afficher
        setBorder(new EmptyBorder(5, 5, 5, 5)); 
        setLayout(null); 
        setBackground(new Color(0)); //on met la couleur du JPanel en noir
    }

    public JLabel ajout(String nom, int x, int y){
        ImageIcon icone = new ImageIcon(nom); //on cree une ImageIcon qui representra l'image "nom"

        JLabel image = new JLabel(icone); //on cree le JLabel qui contiendra l'image "icone"
        image.setBounds(x, y, 20, 20);
        add(image); //on ajoute l'image dans le JPanel

        return image;
    }
}
