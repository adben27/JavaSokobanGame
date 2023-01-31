package package_sokoban 

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.*;

//classe FirstApp qui est une JFrame et qui implement l'interface KeyListener
public class FirstApp extends JFrame implements KeyListener{
    private static final int WIDTH=600, HEIGHT=400; //hauteur et largeur de la fenetre

    private JButton haut, bas, gauche, droite;//boutton qui permettront de ce deplacer si on a la flemme d'utiliser les fleches directionelles
    private Img joueur, monde, vide, cible, mur;
    private JPanel niveau;

    public FirstApp(){
        super("FirstApp");//on creer une fenetre nommée "FirstApp"

        //on cree le JPanel qui contiendra le niveau et on l'ajoute dans la JFrame
        niveau= new JPanel(new FlowLayout());
        niveau.setBorder(new EmptyBorder(5, 5, 5, 5)); 
        niveau.setLayout(null);
        add(niveau);

        //on met le JPanel en vert
        niveau.setBackground(new Color(0, 155, 100));

        //on cree les differente images qui vont etre utiliser
        joueur = new Img("Image/joueur.png", 0, 0);
        cible = new Img("Image/cible.png", 100, 100);
        vide = new Img("Image/vide.png", 40, 40);
        monde = new Img("Image/monde.png", 140, 140);
        mur = new Img("Image/mur.png", 200, 200);

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

        add(information(), BorderLayout.WEST); //on rajoute des informations a gauche de la fenetre

        //on affiche les images qui corespondront au elements du niveau (sauf le joueur) qui occupera le reste de la fenetre
        creerNiveau();

        setVisible(true); //On affiche la fenetre 
    }

    //les Listener sont les methodes qui appeleront la methode move et appliqueront les modifications qui suivent (KetListener pour le clavier et Listener pour les bouttons)
    //les println seront remplacer par les mouvements que le joueur fera
    private void hautKeyListener() {
        joueur.deplacementHaut();
    }

    private void basKeyListener() {
        joueur.deplacementBas();
    }

    private void gaucheKeyListener() {
        joueur.deplacementGauche();
    }

    private void droiteKeyListener() {
        joueur.deplacementDroite();
    }

    //requestFocus() permette d'ecouter le clavier, sans les requestFocus() le clavier n'est plus ecouter 
    private void hautListener(ActionEvent e) {
        joueur.deplacementHaut();
        requestFocus(); 
    }

    private void droiteListener(ActionEvent e) {
        joueur.deplacementDroite();
        requestFocus();
    }

    private void gaucheListener(ActionEvent e) {
        joueur.deplacementGauche();
        requestFocus();
    }

    private void basListener(ActionEvent e) {
        joueur.deplacementBas();
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
            hautKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            basKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            gaucheKeyListener();
            return;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            droiteKeyListener();
            return;
        }
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

    //on ajoute les image dans le JPanel "niveau"
    public void creerNiveau() {
        niveau.add(joueur);
        niveau.add(mur);
        niveau.add(vide);
        niveau.add(cible);
        niveau.add(monde);
    }

    //on creer un JPanel qui contiendra des information utile pour le joueur
    public JPanel information() {
        JPanel info = new JPanel(new GridLayout(5,2, -35, 5)); //on separe le panel en 5 lignes qui on chacune 2 colonnes
        
        //on cree un texte qui dit quelle image correspond a quoi
        JTextField text_monde =new JTextField("Voici un monde:");
        text_monde.setEditable(false); //le texte ne peut pas etre lodifier
        info.add(text_monde, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/monde.png"), BorderLayout.WEST); //on met l'image a gauche du panel

        JTextField text_joueur =new JTextField("Voici votre joueur:");
        text_joueur.setEditable(false); //le texte ne peut pas etre lodifier
        info.add(text_joueur, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/joueur.png"), BorderLayout.WEST); //on met l'image a gauche du panel

        JTextField text_vide =new JTextField("Voici du vide:");
        text_vide.setEditable(false); //le texte ne peut pas etre lodifier
        info.add(text_vide, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/vide.png"), BorderLayout.WEST);//on met l'image a gauche du panel

        JTextField text_cible =new JTextField("Voici une cible:");
        text_cible.setEditable(false); //le texte ne peut pas etre lodifier
        info.add(text_cible, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/cible.png"), BorderLayout.WEST);//on met l'image a gauche du panel

        JTextField text_mur =new JTextField("Voici un mur:");
        text_monde.setEditable(false); //le texte ne peut pas etre lodifier
        info.add(text_mur, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/mur.png"), BorderLayout.WEST);//on met l'image a gauche du panel

        return info;
    }

    public static void main(String[] args) throws Exception{
        //la fenetre aura le look Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //creation de la fenetre
        new FirstApp(); 
    }    
}
