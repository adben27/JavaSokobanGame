package package_sokoban 

import java.awt.*;
import javax.swing.*;
 
public class DrawLevel extends JPanel{

    public DrawLevel() {
        super();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image mur = getToolkit().getImage("Image/mur.png");
        Image vide = getToolkit().getImage("Image/vide.png");
        Image cible = getToolkit().getImage("Image/cible.png");
        Image monde = getToolkit().getImage("Image/monde.png");

        for(int i=0; i<9; i++)
            g.drawImage(mur, 20*i, 0, this);
       
        for (int i = 1; i<9; i++)
            g.drawImage(mur, 0, 20*i, this);
            
        for (int i = 1; i<9; i++)
            g.drawImage(mur, 20*8, 20*i, this);
       
        for (int i = 1; i<9; i++)
            g.drawImage(mur, 20*i, 20*8, this);

        g.drawImage(cible, 20*2, 20*2, this);
        g.drawImage(monde, 20*3, 20*2, this);
        g.drawImage(monde, 20*4, 20*2, this);
        g.drawImage(monde, 20*6, 20*2, this);

        g.drawImage(mur, 20*1, 20*4, this);
        g.drawImage(mur, 20*2, 20*4, this);
        g.drawImage(mur, 20*3, 20*4, this);
        g.drawImage(mur, 20*4, 20*4, this);
        g.drawImage(monde, 20*6, 20*4, this);

        g.drawImage(mur, 20*1, 20*5, this);
        g.drawImage(mur, 20*3, 20*5, this);
        g.drawImage(cible, 20*2, 20*5, this);
        g.drawImage(mur, 20*4, 20*5, this);
        g.drawImage(monde, 20*6, 20*5, this);

        g.drawImage(cible, 20*1, 20*6, this);
        g.drawImage(monde, 20*2, 20*6, this);
        g.drawImage(cible, 20*3, 20*6, this);
        g.drawImage(mur, 20*4, 20*6, this);
        g.drawImage(monde, 20*6, 20*6, this);

        g.drawImage(mur, 20*1, 20*7, this);
        g.drawImage(mur, 20*3, 20*7, this);
        g.drawImage(cible, 20*2, 20*7, this);
        g.drawImage(mur, 20*4, 20*7, this);
    }
}
