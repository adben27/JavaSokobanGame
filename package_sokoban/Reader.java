package package_sokoban;

import java.io.File;
import java.util.Scanner;
import java.io.*;
import java.util.regex.*;
import java.util.*;
import package_sokoban.*;

public class Reader {
	private Niveau lesniv;
	private int niv_actuel;
	private Element[][] tab_actuel;
	private Matrice matrice_actuel;
	private Map map_actuel;

	public static void main(String[] args) {
		Reader lecteur = new Reader();

		String[] map = sousMap(lecteur.envoyerLaMapEntiere());
		String[] entete = enteteSousMap(lecteur.envoyerLaMapEntiere(), map);
		int[] size = sizeSousMap(entete);
		String[] nom = nomSousMap(entete);

		System.out.println("\n 		¤ Bienvenue dans l'interface textuelle du Sokoban. ¤ 	\n");
		System.out.println("			§°°¨ EN AVANT SPARTAN JOUEZ ¨°°§  \n\n");
		Map niv0 = new Map(size, nom, map);
		lecteur.setMap_actuel(niv0);
		lecteur.setMatrice_actuel(niv0.getMap().get(nom[0]));
		lecteur.setTab_actuel(niv0.getMap().get(nom[0]).getLevel());

		lecteur.lancerGameInTerminal();
	}

	public Reader() {
		lesniv = new Niveau();
		niv_actuel = 0;
	}

	public String envoyerLaMapEntiere() {
		String niveau = new String();
		try {
			Scanner scanner = new Scanner(lesniv.getNiv_memoire().get(lesniv.getListeniv()[niv_actuel]));
			while (scanner.hasNextLine()) {
				niveau = niveau + scanner.nextLine();
			}
			scanner.close();
		} catch (Exception e) {
			System.out.println("Lecture du fichier a mal tourné");
		}
		return niveau;
	}

	public static String[] sousMap(String niveau) {
		Pattern pattern = Pattern.compile("[B-Z].[0-9]+");
		String[] result = pattern.split(niveau);
		return result;
	}

	public static String[] enteteSousMap(String niveau, String[] result) {
		Pattern pattern = Pattern.compile("[B-Z].[0-9]+");
		Matcher matcheri = pattern.matcher(niveau);
		String[] nivinf = new String[result.length - 1];
		int i = 0;
		while (matcheri.find()) {
			nivinf[i++] = matcheri.group();
		}
		return nivinf;
	}

	public static int[] sizeSousMap(String[] sousMap) {
		int[] size = new int[sousMap.length];
		for (int i = 0; i < size.length; i++) {
			size[i] = Integer.parseInt(sousMap[i].substring(2));
		}
		return size;
	}

	public static String[] nomSousMap(String[] sousMap) {
		String[] nom = new String[sousMap.length];
		for (int i = 0; i < nom.length; i++) {
			nom[i] = sousMap[i].substring(0, 1);
		}
		return nom;
	}

	public Niveau getLesNiv() {
		return lesniv;
	}

	public Element[][] getTab_actuel() {
		return tab_actuel;
	}

	public Matrice getMatrice_actuel(){
		return matrice_actuel;
	}

	public Map getMap_actuel() {
		return map_actuel;
	}

	public void setMap_actuel(Map x) {
		map_actuel = x;
	}

	public void setMatrice_actuel(Matrice y){
		matrice_actuel=y;
	}

	public void setTab_actuel(Element[][] z) {
		tab_actuel = z;
	}

	public void lancerGameInTerminal(){
		while(! getMap_actuel().mapFini()){
			System.out.println("Niveau "+ niv_actuel + "	: " + getLesNiv().getListeniv()[niv_actuel].replaceFirst("[.][^.]+$", "") + " >> Monde "+ getMatrice_actuel().getName() + " [" + getMatrice_actuel().getSize() + " x " + getMatrice_actuel().getSize() + "]");
			Element.afficherElements(tab_actuel);
			getMatrice_actuel().move();
			actualiser();
		}
	}
	public void actualiser(){
		System.out.println();
		if(getMap_actuel().mapFini()){
			System.out.println("Niveau "+ niv_actuel + "	: " + getLesNiv().getListeniv()[niv_actuel].replaceFirst("[.][^.]+$", "") + " >> Monde "+ getMatrice_actuel().getName() + " [" + getMatrice_actuel().getSize() + " x " + getMatrice_actuel().getSize() + "]");
			Element.afficherElements(tab_actuel);
			if(niv_actuel + 1 < lesniv.getNiv_memoire().size()){
				System.out.println("\n\t*************************************************************************	");  
				System.out.println("****	    Vous avez validé ce niveau. Passage au prochain niveau Sensei !!!        ****");
				System.out.println("\t*************************************************************************	\n");
				niv_actuel++;
				String[] map = sousMap(envoyerLaMapEntiere());
				String[] entete = enteteSousMap(envoyerLaMapEntiere(), map);
				int[] size = sizeSousMap(entete);
				String[] nom = nomSousMap(entete);
				Map niv_prochain=new Map(size,nom,map);
				setMap_actuel(niv_prochain);
				setMatrice_actuel(niv_prochain.getMap().get(nom[0]));
				setTab_actuel(niv_prochain.getMap().get(nom[0]).getLevel());
			}else{
				System.out.println("\n\t*************************************************************************************************   ");  
				System.out.println("****	    Vous avez validé le dernier niveau. A la prochaine pour de nouvelles aventures Senpai !!!        ****");
				System.out.println("\t************************************************************************************************* \n");
			}
		}
	}
}
