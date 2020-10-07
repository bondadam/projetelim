#### projetelim

##### Projet ELIM Bond / Munier


# Rechord


### Scénario

Michou veut apprendre la guitare, et pour cela il s'entraîne à jouer ses premiers accords. Mais comme il débute et qu'il n'a pas de professeur, il ne sait pas si il joue correctement ces accords. Avec notre application, il peut s'entraîner jusqu'à le jouer parfaitement; l'application lui indique l'écart avec l'accord recherché avec un affichage sous forme de signal audio ludique.

[Optionnel: intégration des retours; L'utilisateur est par exemple avec son professeur et lui demande de jouer un accord parfait, l'application l'enregistre pour étendre les données du modèle.]

### Architecture

On enregistre des accords joués à la guitare, dans des conditions différentes (Bruits, qualité de l'accord, guitare, micro du téléphone ...), on transforme les sons en vecteurs et on entraine notre application à reconnaitre un bon accord avec du Deep Learning en approche SVM.