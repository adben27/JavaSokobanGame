

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class FirstApp implements KeyListener{
    private static final int WIDTH=600, HEIGHT=400; //hauteur et largeur de la fenetre

    private JButton haut, bas, gauche, droite; //boutton qui permettront de ce deplacer si on a la flemme d'untiliser les fleches directionelles
    private JFrame fenetre;

    public FirstApp(){
        fenetre = new JFrame("FirstApp");//on creer une fenetre nommée "FirstApp"

        //pour qu'on puisse utiliser les bouttons de déplacement et les fleches directionelles
        fenetre.setFocusable(true);

        //on dit a la fenetre d'écouter le clavier
        fenetre.addKeyListener(this);

        fenetre.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //on ferme le processus de la fenetre quand on clique sur la croix rouge
        
        fenetre.setSize(WIDTH, HEIGHT); //la fenetre fera WIDHT pixels de hauteur et HEIGHT de largeur
        
        fenetre.setLocationRelativeTo(null); //On centre la fenetre par rapport à l'écran ici le "bureau de travail"

        //on creeé les bouton "haut" "bas" "gauche" "droite" et grace à "conteneurMove" on ajoute ces 4 button et on les placent dans le conteneur en bas de la fenetre
        droite = new JButton("droite");
        gauche = new JButton("gauche");
        bas = new JButton("bas");
        haut = new JButton("haut");

        fenetre.add(createConteneurMove(haut, bas, gauche, droite), BorderLayout.SOUTH);

        fenetre.setVisible(true); //On affiche la fenetre 
    }

    //les Listener sont les methodes qui appeleront la methode move et appliqueront les modifications qui suivent (KetListener pour le clavier et Listener pour les bouttons)
    //les println seront remplacer par les mouvements que le joueur fera
    private void hautKeyListener() {
        System.out.println("fleche haut");
    }

    private void basKeyListener() {
        System.out.println("fleche bas");
    }

    private void gaucheKeyListener() {
        System.out.println("fleche gauche");
    }

    private void droiteKeyListener() {
        System.out.println("fleche droite");
    }

    //requestFocus() permette d'ecouter le clavier, sans les requestFocus() le clavier n'est plus ecouter 
    private void hautListener(ActionEvent e) {
        System.out.println("mouvement haut");
        fenetre.requestFocus(); 
    }

    private void droiteListener(ActionEvent e) {
        System.out.println("mouvement droite");
        fenetre.requestFocus();
    }

    private void gaucheListener(ActionEvent e) {
        System.out.println("mouvement gauche");
        fenetre.requestFocus();
    }

    private void basListener(ActionEvent e) {
        System.out.println("mouvement bas");
        fenetre.requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP) {
            this.hautKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            this.basKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            this.gaucheKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            this.droiteKeyListener();
            return;
        }
        System.out.println("Les seuls touchent a utiliser sont les fleches directionelles");
    }

    @Override
    public void keyReleased(KeyEvent e) { }

    private JPanel createConteneurMove(JButton haut, JButton bas, JButton gauche, JButton droite){
        //creation du panel ou il y aura les mouvements
        JPanel move= new JPanel(new FlowLayout());

        //initialise la hauteur et la largeur du bonton "haut"
        haut.setPreferredSize(new Dimension(70, 30));
        //si le bouton "haut" est cliqué cela execute ce que "hautListener" doit faire
        haut.addActionListener((e) -> hautListener(e));
        //ajout du bouton "haut" au panel "move"
        move.add(haut);
        
        //initialise la hauteur et la largeur du bonton "bas"
        bas.setPreferredSize(new Dimension(70, 30));
        //si le bouton "bas" est cliqué cela execute ce que "basListener" doit faire
        bas.addActionListener((e) -> basListener(e));
        //ajout du bouton "bas" au panel "move"
        move.add(bas);
       
        //initialise la hauteur et la largeur du bonton "gauche"
        gauche.setPreferredSize(new Dimension(70, 30));
        //si le bouton "gauche" est cliqué cela execute ce que "gaucheListener" doit faire
        gauche.addActionListener((e) -> gaucheListener(e));
        //ajout du bouton "gauche" au panel "move"
        move.add(gauche); 

        //initialise la hauteur et la largeur du bonton "droite"
        droite.setPreferredSize(new Dimension(70, 30));
        //si le bouton "droite" est cliqué cela execute ce que "droiteListener" doit faire
        droite.addActionListener((e) -> droiteListener(e));
        //ajout du bouton "droite" au panel "move"
        move.add(droite);

        return move;
    }

    public static void main(String[] args) throws Exception{
        //la fenetre aura le look Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //debut de la fenetre
        new FirstApp(); 
    }

    
}