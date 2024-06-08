# Rapport

Exercice 1

Partie 1

Nous avons choisi l'outil Postman.


Partie 2

La batterie de test 100% nous a montré les problèmes logiques suivants :

Quand aucun parent n'a de salaire, la méthode, actuellement, nous retourne parent2. Dans ce cas, le méthode devrait retourner les deux parents.

La méthode retourne parent1 si le parent2 gagne plus d'argent?

Les résidences des parents et enfant (et le fait que les parents vivent ensemble) ne sont pas pris en compte pour la décison.


Partie 3

La classe a été crée et les tests ont été adaptés (La map a été remplacée par la classe). Les tests étant les mêmes, les résultats nous montrent les mêmes problèmes logiques de la méthode.

Partie 4

Après vérification, il est obligatoire pour des personnes exerçant une activité indépendante de cotiser à l'AVS. Dès lors, nous avons délibérement mis de côté "les 2 parents sont indépendants" dans le schéma fourni, les salaires étant suffisants pour déterminer le montant soumis au droit à l'AVS.

Nous avons effectué un test "getParentDroitAllocation_Given_IndependentParentsLivingTogether_shouldBe_BothParents", à savoir que les 2 parents ont l'autorité parentale, habitent ensemble et ont le même salaire. Le test ne passe pas car le schéma fournit pas le cas où les parents ont exactement le même salaire. Il est sous-entendu qu'à salaire pareil, le montant soumis à l'AVS par parent est différent selon d'autres paramètres et règles qu'il serait trop long à prendre en compte pour ce travail.


Exercice 2

Partie 1

Etant donnée que la consigne ne précise par quel bied supprimé un locataire, nous utilisons l'id pour la suppression.
La suppression fonctionne. Pour tester, veuillez utiliser l'id 21.

Partie 2

Nous avons utilisé la spécification standardisée par le protocole HTTP "PATCH" car elle permet de remplacer certaines informations sans devoir remplacer tous les champs d'un table. Comme ceci, le numéro AVS n'a pas besoin d'être modifié.

Exercice 3

A observer dans l'appliation, selon commit


Exercice 4


Partie 1

La ré-organisation a été faite avec succès.

Partie 2

La classe "MyTestsIT" a été crée et le test est concluant.

Partie 3

Les 2 fichiers JAR ont été ajoutés

Partie 4

A observer dans l'appliation, selon commit









