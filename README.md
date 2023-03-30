# Sokoban 
Ce dépôt contient le code du jeu Sokoban et nos rapports. Nous avons réalisé le projet à 4.

## Description
Le jeu contient une interface graphique, ainsi qu'une interface textuelle.

Bien que le chargement de niveau récursif ne soit pas présent, les niveau récursifs fonctionnement sur l'interface graphique.

## Installation
Vous aurez besoin de `java` pour faire tourner le jeu.

Tout d'abord, allez au répetroire parent de 'package_sokoban' et compilez le code:
`javac package_sokoban/*.java`

Puis pour utiliser l'interface textuelle allez au répetroire parent de 'package_sokoban' et faite :
`java package_sokoban.Reader`

Pour utiliser l'interface graphique allez au répetroire parent de 'package_sokoban' et faite:
`java package_sokoban.FirstApp`

Pour tester la version récursive du jeu `mettez en commentaire les lignes 152,153 et 366 jusqu'a 368 de la classe DrawLevel` et `enlevez les commentaire de la ligne 155 et 156`, pour finir `mettez à la ligne 148 de la classe Matrice 'return false' au lieu de 'return true'`, puis `faites les étapes d'installation qui ce trouve au dessus`. Ce niveau est un niveau de test donc il n'y aura pas de cible.
