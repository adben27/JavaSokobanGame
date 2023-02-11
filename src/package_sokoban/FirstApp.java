package package_sokoban 

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

//classe FirstApp qui est une JFrame et qui implement l'interface KeyListener
public class FirstApp extends JFrame implements KeyListener{
    private static final int WIDTH=600, HEIGHT=400; //hauteur et largeur de la fenetre
    private JButton haut, bas, gauche, droite;//boutton qui permettront de ce deplacer si on a la flemme d'utiliser les fleches directionelles
    //private Img joueur;//les differentes images qu'on va utiliser
    private DrawLevel niveau;//le conteneur ou il y aura le niveau largeur=420 (21x20) longueur=320 (16x20)
    private Image icone;//icone de la fenetre
    private int r,g,b; //couleur du niveau

    public FirstApp(){
        super("Sokoban");//on creer une fenetre nommée "Sokoban"

        //pour qu'on puisse utiliser les bouttons de déplacement et les fleches directionelles
        setFocusable(true);

        //on dit a la fenetre d'écouter le clavier
        addKeyListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //on ferme le processus de la fenetre quand on clique sur la croix rouge
        
        setSize(WIDTH, HEIGHT); //la fenetre fera WIDHT pixels de hauteur et HEIGHT de largeur
        
        setLocationRelativeTo(null); //On centre la fenetre par rapport à l'écran ici le "bureau de travail"

        //creation de l'icone de la fenetre
        icone = getToolkit().getImage("Image/joueur.png");
        setIconImage(icone);

        //on cree le JPanel qui contiendra le niveau et on l'ajoute dans la JFrame
        niveau= new DrawLevel();
        niveau.setLayout(null);
        add(niveau, BorderLayout.CENTER);
        
        //on genere 3 nombres aleatoires qui feront la couleur du niveau
        Random random = new Random();
        r=random.nextInt(256);
        g=random.nextInt(256);
        b=random.nextInt(256);
        niveau.setBackground(new Color(r, g, b));
       
        //on creeé les bouton "haut" "bas" "gauche" "droite" et grace à "conteneurMove" on ajoute ces 4 button dans le conteneur qui va se trouver en bas de la fenetre
        droite = new JButton("droite");
        gauche = new JButton("gauche");
        bas = new JButton("bas");
        haut = new JButton("haut");

        add(createConteneurMove(haut, bas, gauche, droite), BorderLayout.SOUTH);

        add(information(), BorderLayout.WEST); //on rajoute des informations a gauche de la fenetre

        setVisible(true); //On affiche la fenetre 

        niveau.startGame(); //On demarre le thread qui va lancer la boucle de jeu 
    }

    //les Listener sont les methodes qui appeleront la methode move et appliqueront les modifications qui suivent
    //requestFocus() permette d'ecouter le clavier, sans les requestFocus() le clavier n'est plus ecouter 
    private void hautListener(ActionEvent e) {
        niveau.setHaut(true);
        requestFocus();
    }

    private void droiteListener(ActionEvent e) {
        niveau.setDroite(true);
        requestFocus();
    }

    private void gaucheListener(ActionEvent e) {
        niveau.setGauche(true);
        requestFocus();
    }

    private void basListener(ActionEvent e) {
        niveau.setBas(true);
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
            niveau.setHaut(true);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            niveau.setBas(true);
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            niveau.setGauche(true);
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            niveau.setDroite(true);
        }
        if (keyCode != KeyEvent.VK_RIGHT && keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_DOWN && keyCode != KeyEvent.VK_UP) {
            JOptionPane.showMessageDialog(this,"Seuls les flèches directionnelles peuvent être utilisé\n(Si vous ne voulez pas utiliser les flèches directionnelles\nutilisez les bouttons haut, bas, gauche, droite)","Information", JOptionPane.INFORMATION_MESSAGE);
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

    //on creer un JPanel qui contiendra des information utile pour le joueur
    public JPanel information() {
        //on separe le panel en 5 lignes qui on chacune 2 colonnes, l'ecart horizontal est de "-40" pour que l'image soit coller au texte
        JPanel info = new JPanel(new GridLayout(5,2, -60, 0)); 

        //on cree un texte qui dit quelle image correspond a quoi
        JTextArea text_monde =new JTextArea("Voici un monde, vous\ndevez déplacer les\ndifferents mondes\ndans les cibles");
        text_monde.setEditable(false); //le texte ne peut pas etre modifier
        info.add(text_monde, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/mondeB.png")); //on met l'image a gauche du panel

        JTextArea text_joueur =new JTextArea("Voici Atlas,\nvotre personnage");
        text_joueur.setEditable(false); //le texte ne peut pas etre modifier
        info.add(text_joueur, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/joueur.png")); //on met l'image a gauche du panel

        JTextArea text_vide =new JTextArea("Voici du vide");
        text_vide.setEditable(false); //le texte ne peut pas etre modifier
        info.add(text_vide, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/vide.png"));//on met l'image a gauche du panel

        JTextArea text_cible =new JTextArea("Voici une cible");
        text_cible.setEditable(false); //le texte ne peut pas etre modifier
        info.add(text_cible, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/cible.png"));//on met l'image a gauche du panel

        JTextArea text_mur =new JTextArea("Voici un mur, vous\nne pouvez pas deplacer\nles murs");
        text_mur.setEditable(false); //le texte ne peut pas etre modifier
        info.add(text_mur, BorderLayout.WEST); //on met le texte a gauche du panel
        info.add(new Img("Image/mur.png"));//on met l'image a gauche du panel

        return info;
    }    

    public static void main(String[] args) throws Exception{
        //la fenetre aura le look Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //creation de la fenetre
        new FirstApp(); 
    }
}
