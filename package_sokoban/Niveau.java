package package_sokoban;

import java.io.*;
import java.util.*;

public class Niveau{
    private String extension; // extension des fichiers de niveaux pour nous cette dernière sera en .txt
    private int niveau_actuel;
    private String[] listeniv; // tableau des noms de niveaux
    private HashMap<String,File> niv_memoire; // HashMap des fichiers de niveaux en mémoire
    private boolean niveauValide;
   
    public static void main(String[] args) {  
            
    }

    
    public Niveau(){
        niveau_actuel=0; 
        extension=".txt";
        chargerNiveauxMemoire();
        listeniv=new String[niv_memoire.size()];
        niv_memoire.keySet().toArray(listeniv);
        Arrays.sort(listeniv);
    }

    public static void storeLevelsFiles(File file, HashMap<String,File> all, final String extension){
        FileFilter filtre= new FileFilter() {
            public boolean accept(File f){
                return f.getName().endsWith(extension);
            }
        };
        final File[] fichiers= file.listFiles(filtre);        
        if(fichiers != null){
            for(File unite : fichiers){
                all.put(unite.getName(), unite);
                storeLevelsFiles(unite, all, extension);
            } 
        } 
    }

    public void chargerNiveauxMemoire(){
        this.niv_memoire= new HashMap<String,File>();
        storeLevelsFiles(new File("outlevels"+File.separator).getAbsoluteFile(), niv_memoire, this.extension);        
    }

    public String[] getListeniv(){
        return listeniv;
    }

    public HashMap<String,File> getNiv_memoire(){
        return niv_memoire;
    }


    public void afficherListeniv(){
        for( String nom: listeniv){
            System.out.println(nom);
        }
    }

    public void getNiveau_actuel(){
        System.out.println("Vous etes au Niveau :" + niveau_actuel + listeniv[niveau_actuel]); //Pour le joueur plutot 
    }

}