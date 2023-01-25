package package_sokoban; 

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class FirstApp extends JFrame implements KeyListener{
    private static final int WIDTH=600, HEIGHT=400; //hauteur et largeur de la fenetre

    private JButton haut, bas, gauche, droite; //boutton qui permettront de ce deplacer si on a la flemme d'utiliser les fleches directionelles

    public FirstApp() throws IOException{
        super("FirstApp");//on creer une fenetre nommée "FirstApp"

        //pour qu'on puisse utiliser les bouttons de déplacement et les fleches directionelles
        setFocusable(true);

        //on dit a la fenetre d'écouter le clavier
        addKeyListener(this);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); //on ferme le processus de la fenetre quand on clique sur la croix rouge
        
        setSize(WIDTH, HEIGHT); //la fenetre fera WIDHT pixels de hauteur et HEIGHT de largeur
        
        setLocationRelativeTo(null); //On centre la fenetre par rapport à l'écran ici le "bureau de travail"

        //on creeé les bouton "haut" "bas" "gauche" "droite" et grace à "conteneurMove" on ajoute ces 4 button dans le conteneur qui va se trouver en bas de la fenetre
        droite = new JButton("droite");
        gauche = new JButton("gauche");
        bas = new JButton("bas");
        haut = new JButton("haut");

        add(createConteneurMove(haut, bas, gauche, droite), BorderLayout.SOUTH);

        //on affiche les images qui corespondront au elements du niveau qui occupera le reste de la fenetre
        add(creerNiveau());

        setVisible(true); //On affiche la fenetre 
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
        System.out.println("boutton haut");
        requestFocus(); 
    }

    private void droiteListener(ActionEvent e) {
        System.out.println("boutton droite");
        requestFocus();
    }

    private void gaucheListener(ActionEvent e) {
        System.out.println("boutton gauche");
        requestFocus();
    }

    private void basListener(ActionEvent e) {
        System.out.println("boutton bas");
        requestFocus();
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }

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
        System.out.println("Les seuls touchent a utiliser sont les moves directionelles");
    } 

    private JPanel createConteneurMove(JButton haut, JButton bas, JButton gauche, JButton droite){
        //creation des panel ou il y aura les mouvements
        JPanel move= new JPanel(new FlowLayout());
        
        move.setBackground(new Color(225));

        //initialise la hauteur et la largeur du bonton "haut"
        haut.setPreferredSize(new Dimension(70, 30));
        //si le bouton "haut" est cliqué cela execute ce que "hautListener" doit faire
        haut.addActionListener((e) -> hautListener(e));
        //ajout du bouton "haut" au panel "move"
        move.add(haut, BorderLayout.NORTH);
        
        //initialise la hauteur et la largeur du bonton "bas"
        bas.setPreferredSize(new Dimension(70, 30));
        //si le bouton "bas" est cliqué cela execute ce que "basListener" doit faire
        bas.addActionListener((e) -> basListener(e));
        //ajout du bouton "bas" au panel "move"
        move.add(bas, BorderLayout.SOUTH);
       
        //initialise la hauteur et la largeur du bonton "gauche"
        gauche.setPreferredSize(new Dimension(70, 30));
        //si le bouton "gauche" est cliqué cela execute ce que "gaucheListener" doit faire
        gauche.addActionListener((e) -> gaucheListener(e));
        //ajout du bouton "gauche" au panel "move"
        move.add(gauche, BorderLayout.SOUTH); 

        //initialise la hauteur et la largeur du bonton "droite"
        droite.setPreferredSize(new Dimension(70, 30));
        //si le bouton "droite" est cliqué cela execute ce que "droiteListener" doit faire
        droite.addActionListener((e) -> droiteListener(e));
        //ajout du bouton "droite" au panel "move"
        move.add(droite, BorderLayout.SOUTH);

        return move;
    }

    public Img creerNiveau(){
        Img niveau=new Img(); //on cree une image

        niveau.ajout("cible.png", 0, 0); // met l'image cible.png en pos_x=0 pos_y=0
        niveau.ajout("monde.png", 20,0); // met l'image cible.png en pos_x=20 pos_y=0
        niveau.ajout("vide.png", 40, 0); // met l'image cible.png en pos_x=40 pos_y=0
        niveau.ajout("joueur.png", 60, 0); // met l'image cible.png en pos_x=60 pos_y=0

        return niveau;
    } 

    public static void main(String[] args) throws Exception{
        //la fenetre aura le look Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //creation de la fenetre
        new FirstApp(); 
    
    }

    
}
