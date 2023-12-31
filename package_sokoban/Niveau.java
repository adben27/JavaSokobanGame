package package_sokoban;

import java.io.*;
import java.util.*;

public class Niveau{
    private String extension; // extension des fichiers de niveaux pour nous cette dernière sera en .txt
    private String[] listeniv; // tableau des noms de niveaux
    private HashMap<String,File> niv_memoire; // HashMap des fichiers de niveaux en mémoire
   
    public Niveau(){
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
        storeLevelsFiles(new File("package_sokoban"+File.separator+"outlevels"+File.separator).getAbsoluteFile(), niv_memoire, this.extension);        
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
}