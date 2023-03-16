package package_sokoban;

import java.util.HashMap;

public class Map {

    HashMap<String, Matrice> mondes;

    public Map(int[] size, String[] nom, String[] sousmap) {
        mondes = new HashMap<String, Matrice>();
        for (int i = 0; i < size.length; i++) {
            mondes.put(nom[i], new Matrice(nom[i], nom[i].charAt(0), false, size[i]));
        }
        mondes.get(nom[0]).setIs_main();
        for (int i = 0; i < size.length; i++) {
            mondes.get(nom[i]).setlevel(Element.remplirTableauGrille(sousmap[i + 1], size[i], mondes, nom[i]));
        }
    }

    public HashMap<String, Matrice> getMap() {
        return this.mondes;
    }

    public boolean mapFini(){
        boolean val=true;
        for(Matrice m :mondes.values()){
            val=val&&m.estFini();
        }
        return val;
    }
}
