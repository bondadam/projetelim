#### projetelim

##### Projet ELIM Bond / Munier


# Rechord
Aide à l'apprentisage des accords de guitare.

BOND Adam : IAM, adam.bond@etu.unice.fr
MUNIER Rémy : IAM, remy.munier@etu.unice.fr



### Scénario

L'ulisateur veut apprendre la guitare, et pour cela il s'entraîne à jouer ses premiers accords. Mais comme il débute et qu'il n'a pas de professeur, il ne sait pas si il joue correctement ces accords. Avec notre application, il peut s'entraîner jusqu'à le jouer parfaitement; l'application lui indique l'écart avec l'accord recherché avec un affichage sous forme de signal audio ludique.

[Optionnel: intégration des retours; L'utilisateur est par exemple avec son professeur et lui demande de jouer un accord parfait, l'application l'enregistre pour étendre les données du modèle.]


Des exemples sons de bon et mauvais accords sont disponible sur le git.

### Architecture

On enregistre des accords joués à la guitare, dans des conditions différentes (Bruits, qualité de l'accord, guitare, micro du téléphone ...), on transforme les sons en vecteurs et on entraine notre application à reconnaitre un bon accord avec du Deep Learning en approche SVM. En utilisant la classification, l'application sera capable de classer l'accord jouer dans l'une des catégories :
  - Réussi
  - Raté : Guitare désaccordée
  - Raté : Intervalles harmonique différents
  - Raté : Sons parasites
  - Non reconnu.
  
Librairie Java SVM : https://www.csie.ntu.edu.tw/~cjlin/libsvm/
