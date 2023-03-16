package package_sokoban;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.*;
import java.awt.event.*;

//classe FirstApp qui est une JFrame et qui implement l'interface KeyListener
public class FirstApp extends JFrame implements KeyListener{

    //boutton qui permettront de ce deplacer si on a la flemme d'utiliser les fleches directionelles
    //ou d'afficher une fentre qui donne certaines informations
    private JButton haut, bas, gauche, droite, information, commande, reset;
    private DrawLevel niveau;//le conteneur ou il y aura le niveau 
    private Image icone;//icone de la fenetre
    private JFrame info_image;//fenetre qui donne certaines info_imagermations
    private JFrame info_commande;//fenetre qui donne les touche à utiliser

    public FirstApp(){
        super("Sokoban");//on creer une fenetre nommée "Sokoban"

        //creation de la fenetre "info_image" et "info_commande"
        info_commande = new JFrame("Information sur les commandes");
        info_image = new JFrame("Information sur les images");

        info_image = createFrame(info_image, 400, 450);
        info_image.add(information_image());

        info_commande = createFrame(info_commande, 425, 155);
        info_commande.add(information_commande());

        //pour qu'on puisse utiliser les bouttons de déplacement et les fleches directionelles
        setFocusable(true);

        //on dit a la fenetre d'écouter le clavier
        addKeyListener(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //on ferme le processus de la fenetre quand on clique sur la croix rouge
        
        Dimension size=getToolkit().getScreenSize();
        setSize((int) size.getWidth(),(int) size.getHeight()-40); //la fenetre fera la taille de l'écran
        
        setLocationRelativeTo(null); //On centre la fenetre par rapport à l'écran ici le "bureau de travail"

        //creation de l'icone des fenetre
        icone = getToolkit().getImage("Image/joueur.png");
        setIconImage(icone);
        info_commande.setIconImage(icone);
        info_image.setIconImage(icone);

        //on cree le JPanel qui contiendra le niveau et on l'ajoute dans la JFrame
        niveau= new DrawLevel();
        niveau.setLayout(null);
        add(niveau);
       
        //on creeé les bouton "haut" "bas" "gauche" "droite" et grace à "conteneurMove" on ajoute ces 4 button dans le conteneur qui va se trouver en bas de la fenetre
        droite = new JButton("droite");
        gauche = new JButton("gauche");
        bas = new JButton("bas");
        haut = new JButton("haut");
        information = new JButton("information");
        commande =  new JButton("commande");
        reset =  new JButton("reset");

        add(createConteneurMove(), BorderLayout.SOUTH);

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

    private void infoListener(ActionEvent e) {
        info_image.setVisible(true);
        requestFocus();
    }

    private void commandeListener(ActionEvent e) {
        info_commande.setVisible(true);
        requestFocus();
    }

    private void resetListener(ActionEvent e) {
        niveau.resetAll();
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
        if(keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_ENTER){
            info_image.dispose();
            info_commande.dispose();
        }
        if (keyCode == KeyEvent.VK_I) {
            information.doClick();
            info_image.toFront();
        }
        if (keyCode == KeyEvent.VK_C) {
            commande.doClick();
            info_commande.toFront();
        }
        if (keyCode == KeyEvent.VK_R) {
            reset.doClick();
        }
        if (e.isControlDown() && keyCode == KeyEvent.VK_Z) {
            niveau.setCtrlZ(true);
        }
        if (keyCode != KeyEvent.VK_R && keyCode != KeyEvent.VK_CONTROL && keyCode != KeyEvent.VK_Z && keyCode != KeyEvent.VK_C && keyCode != KeyEvent.VK_RIGHT && keyCode != KeyEvent.VK_I && keyCode != KeyEvent.VK_LEFT && keyCode != KeyEvent.VK_DOWN && keyCode != KeyEvent.VK_UP && keyCode != KeyEvent.VK_ESCAPE && keyCode != KeyEvent.VK_ENTER) {
            JOptionPane.showMessageDialog(this,"Seuls les flèches directionnelles peuvent être utilisé\n(Si vous ne voulez pas utiliser les flèches directionnelles\nutilisez les bouttons haut, bas, gauche, droite)","Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private JPanel createConteneurMove(){
        //creation des panel ou il y aura les mouvements
        JPanel move= new JPanel(new FlowLayout());
        
        move.setBackground(new Color(225));

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

        //initialise la hauteur et la largeur du bonton "information"
        information.setPreferredSize(new Dimension(90, 30));
        //si le bouton "information" est cliqué cela execute ce que "infoListener" doit faire
        information.addActionListener((e) -> infoListener(e));
        //ajout du bouton "inforamtion" au panel "move"
        move.add(information);

        //initialise la hauteur et la largeur du bonton "commande"
        commande.setPreferredSize(new Dimension(90, 30));
        //si le bouton "information" est cliqué cela execute ce que "commandeListener" doit faire
        commande.addActionListener((e) -> commandeListener(e));
        //ajout du bouton "commande" au panel "move"
        move.add(commande);

        //initialise la hauteur et la largeur du bonton "commande"
        reset.setPreferredSize(new Dimension(70, 30));
        //si le bouton "information" est cliqué cela execute ce que "commandeListener" doit faire
        reset.addActionListener((e) -> resetListener(e));
        //ajout du bouton "commande" au panel "move"
        move.add(reset);

        return move;
    }
    
    //on creer un JPanel qui dira au joueur quelle touche utiliser pour jouer au jeu
    public JPanel information_commande() {
        JPanel info = new JPanel(new BorderLayout());

        JTextField quittez = new JTextField("Pour quittez la fenetre vous pouvez appuiez sur 'echap' ou sur 'entrer'");
        quittez.setEditable(false);
        info.add(quittez, BorderLayout.NORTH);

        JTextArea commande = new JTextArea("Pour vous déplacez utiliser les flèches directionelles ou les boutons\nSi vous appuiez sur 'i' le boutton 'information' sera appuié\nSi vous appuiez sur 'c' le boutton 'commande' sera appuié\nSi vous vous appuiez sur 'ctrl+z' vous allez retourner d'une action en arrière\nSi vous appuiez sur 'r' vous recommencerez le niveau");
        commande.setEditable(false);
        info.add(commande);

        return info;
    }

    //on creer un JPanel qui contiendra des information sur les images
    public JPanel information_image() {
        JPanel info_image = new JPanel(new GridLayout(5, 2, 0, 0));
        JPanel information = new JPanel(new BorderLayout());

        //on cree un texte qui dit quelle image correspond a quoi
        JTextArea text_monde =new JTextArea("Les mondes sont des planètes\nmais aussi des niveaux, vous\ndevez déplacer les differents\nmondes dans les cibles");
        text_monde.setEditable(false); //le texte ne peut pas etre modifier
        text_monde.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        info_image.add(text_monde); //on met le texte a gauche du panel
        info_image.add(new Img("package_sokoban/Image/mondeB.png")); //on ajoute l'image au panel

        JTextArea text_joueur =new JTextArea("Voici Atlas, votre personnage");
        text_joueur.setEditable(false); //le texte ne peut pas etre modifier
        info_image.add(text_joueur); //on met le texte a gauche du panel
        info_image.add(new Img("package_sokoban/Image/joueur.png")); //on ajoute l'image au panel

        JTextArea text_vide =new JTextArea("Le vide est de differente couleur\npour chaque niveau");
        text_vide.setEditable(false); //le texte ne peut pas etre modifier
        info_image.add(text_vide); //on met le texte a gauche du panel
        info_image.add(new Img("package_sokoban/Image/vide.png"));//on ajoute l'image au panel

        JTextArea text_cible =new JTextArea("Voici une cible");
        text_cible.setEditable(false); //le texte ne peut pas etre modifier
        info_image.add(text_cible); //on met le texte a gauche du panel
        info_image.add(new Img("package_sokoban/Image/cible.png"));//on ajoute l'image au panel

        JTextArea text_mur =new JTextArea("Voici un mur, vous ne pouvez pas\ndéplacer les murs");
        text_mur.setEditable(false); //le texte ne peut pas etre modifier
        info_image.add(text_mur); //on met le texte a gauche du panel
        info_image.add(new Img("package_sokoban/Image/mur.png"));//on ajoute l'image au panel

        JTextField quittez = new JTextField("Pour quittez la fenetre vous pouvez appuiez sur 'echap' ou sur 'entrer'");
        quittez.setEditable(false);

        information.add(quittez, BorderLayout.NORTH);
        information.add(info_image);

        return information;
    }

    public JFrame createFrame(JFrame frame, int width, int height) {
        frame.dispose();
        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);
        frame.addKeyListener(this);

        return frame;
    }

    public static void main(String[] args) throws Exception{
        //la fenetre aura le look Nimbus
        UIManager.setLookAndFeel(new NimbusLookAndFeel());

        //creation de la fenetre
        new FirstApp(); 
    }
}