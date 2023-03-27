# Rapport individuel - DOUZI Jirbil
## Contribution
Je me suis occupé de l'interface graphique (les classes 'Img', 'DrawLevel' et 'FirstApp') et j'ai aidé Mounir a faire les differentes méthodes de déplacement comme les méthodes pour sortir/entrer dans un sous-monde, le chainage et l'absorbtion de boite (objet de la classe 'Box' mais pas de sous-monde)

## Réflexion libre
J'aurai du créer une classe 'Clavier' qui permet de lire le clavier au lieu de mettre des boolean dans la classe 'DrawLevel', une classe 'Univers' qui contiendrait toutes les méthodes de déplacement possibles, et créer une classe 'Copie' qui permettra de sauvegarder les instances de chaque objets de la classe 'Matrice'.
La classe 'Clavier' renverrait la valeur d'un boolean qui correspond à une touche qui est appuiée (get_up() pour savoir si la flèche du haut a était appuier).
La classe 'Univers' contiendrai une pile de matrice qui correspondra aux matrices pères (et aussi grand-père etc...) de la matrice où ce trouve le joueur, cette classe aura aussi un tableau de matrice, et aura pour terminer la méthode 'loadlvl(Matrice[] mondes)'
La classe 'Deplacement' comme son nom l'indique aura toutes les méthodes de déplacement que le joueur peut faire (entrer/sortir d'une matrice, qu'une sous-matrice absorbe une boite/matrice), cette classe aura une instance de 'Univers'

## Retour d'experience
J'ai bien aimé le projet, ce projet m'a permis de me familiariser avec swing. J'ai toujours du mal avec certain layout, je voulais au début du projet que les boutons qui permettent les déplacements est la même forme que les flèches directionnelles sur le clavier. Malheuresement je n'ai pas réussi à avoir un affichage avec une profondeur de 4 et 1/4 de case de chaque coté, j'avais toujours des carrés blancs au lieu d'avoir l'affichage d'un monde


