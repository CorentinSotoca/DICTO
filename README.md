# Dicto 

## Présentation de Dicto

Des captures d'écran illustrant le fonctionnement du logiciel sont proposées dans le répertoire shots.


## Utilisation de Dicto

Pour compiler les fichiers présents dans 'src' et création des fichiers '.class' dans 'classes':
```bash
./compile.sh
```
Pour lancer le jeu
```bash
./run.sh DICTO
```

⚠️ Certains emulateurs de terminal (Par exemple celui de VSCode) ne supportent pas certains caractéres, ce qui rend certains niveaux injouable. Nous avons utilisé le terminal Mate et Konsole et ça fonctionnais ⚠️

## Régles du jeu

Un audio vous est joué, vous devez le rettaper le plus efficament possible, sans faute. Vous pouvez taper les mots dans le désordre.  Attention toute fois, vous commencez avec 1000 points, et vous perdez 10 points a chaque fois que vous appuyez sur entrée pour relancer l'audio. Vous perdez aussi 5 points pour chaque mot tapé n'étant pas dans la phrase.

Et, en effet, les scores livré dans le scoreboard de base ne sont pas réaliste, ils ont été fait sur [ce site](https://generatedata.com/generator) qui permet de generer beaucoup de donnée rapidement.