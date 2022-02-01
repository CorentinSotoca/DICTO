import extensions.Sound;
import extensions.File;
import extensions.CSVFile;

class DICTO extends Program {

    final char etx = 3;

    final String ETX = "" + etx; // Le char (qui est mis en string pour plus de facilité) qui marque la fin d'une
                                 // chaine de charactére



    void stringToTab(String string, String[] tab) { // Prend un string et envoi chaque mot dans un tableau de String
        String tmp = "";
        int idx = 0;
        char prev = ' ';
        for (int i = 0; i < length(string); i++) {
            char letter = charAt(string, i); // Pour chaque char
            if (letter == ' ' && prev != ' ') { // Si le char actuelle est un ' ' et que le precedent n'en est pas un
                tab[idx] = tmp; // On rempli tab[idx] part la string qui était dans le buffer
                tmp = ""; // On vide le buffer
                idx = idx + 1; // On ajoute 1 a idx pour preparer la prochaine itération
            } else if (letter != ' ') { // Si c'est pas ' '
                tmp = tmp + letter; // On ajoute au buffer
            }
            prev = letter;
        }
        if (!equals(tmp, "")) { // On vide le buffer si besoin
            tab[idx] = tmp;
            idx = idx + 1;
        }
        tab[idx] = ETX; // char de fin de chaine
    }

    void testSizeString(){
        assertEquals(4, sizeString("toto tata titi"));
        assertEquals(4, sizeString("toto tata "));
        assertEquals(6, sizeString(" toto tata tutu titi"));
    }

    int sizeString(String string) { // Renvoi le nombre d'espace dans la chaine + 2
        int res = 2;
        for (int i = 0; i < length(string); i++) {
            char letter = charAt(string, i);
            if (letter == ' ') { // On compte le nombre d'espace
                res = res + 1;
            }
        }

        return res;
    }

    void testTabToString(){
        String[] tab=new String[]{"toto","tata","tutu"};
        assertEquals("toto tata tutu", tabToString(tab));
        tab=new String[]{"toto","tutu"};
        assertEquals("toto tutu", tabToString(tab));
    }

    String tabToString(String[] tab) { // Prend un tab de string et renvoi un string séparant chaque cellule d'espace
        String res = "";

        for (int i = 0; i < length(tab); i++) {
            res = res + tab[i];
            if (i < length(tab) - 1) {
                res = res + " "; // On ajoute au string en espacant d'un ' ' par case
            }
        }
        return res;
    }

    void testStarify(){
        String[] expects = new String[]{"****","****",".",ETX};
        String[] words = new String[]{"toto","tata",".",ETX};
        String[] toStarify= new String[length(words)];
        starify(words, toStarify);
        assertArrayEquals(expects, toStarify);
    }

    void starify(String[] string, String[] toStarify) { // Remplace chaque char d'un tableau de string par une '*'
        for (int i = 0; !equals(string[i], ETX); i++) {
            String res = "";
            for (int y = 0; y < length(string[i]); y++) {
                char tmp = charAt(string[i],y);
                if(tmp==',' || tmp == '.' || tmp == '?' || tmp == '!' || tmp == ':'){
                    res=res+tmp;
                }else{
                    res = res + '*';
                }
            }
            toStarify[i] = res;
        }
        toStarify[length(string) - 1] = ETX;
    }

    void init(String[] words,String string, String[] tabToDiscover) { // Initialise les tableaux et renvoi
                                                                             // toDiscover starifié 
        stringToTab(string, words);

        starify(words, tabToDiscover);
    }

    void testDiscover(){
        int points = 500;
        String[] string=new String[]{"toto", "tata", ".", ETX};
        String[] toDiscover=new String[]{"***", "****", ".", ETX};
        String input= "titi toto tonton tata";
        assertEquals(490, discover(string, toDiscover, input, points));
    }

    int discover(String[] string, String[] toDiscover, String input, int points) { // prend l'input et découvre
                                                                                // toDiscover, renvoi les points du joueur
        String[] tabInput = new String[sizeString(input)];
        stringToTab(input, tabInput);
        boolean found=false;
        for (int i = 0; i < length(tabInput); i++){
            for (int y = 0; y < length(string); y++) {
                if (equals(tabInput[i], string[y])) {
                    toDiscover[y] = string[y];
                    found=true;
                }
            }
            if(!found){
                points=points-5;
            }else{
                found=false;
            }
        }

        return points;                                                           
    }

    String userInput() { // Récupére l'entrée utilisateur
        String res = "";
        res = readString();
        while (charAt(res, length(res) - 1) == ' ') { // on supprime les espaces a la fin sinon le programme plante
            res = substring(res, 0, length(res) - 1);
        }
        return res;
    }

    void testVerify(){
        String[] tabToDiscover=new String[]{"****", "*", "toto"};
        assertFalse(verify(tabToDiscover));
        tabToDiscover=new String[]{"tata", "a", "toto"};
        assertTrue(verify(tabToDiscover));
    }

    boolean verify(String[] tabToDiscover) { // Verifie si la partie est gagné
        boolean res = true;
        for (int i = 0; i < length(tabToDiscover) && res; i++) {
            for (int y = 0; y < length(tabToDiscover[i]) && res; y++) {
                if (charAt(tabToDiscover[i], y) == '*') { // On fait tout le tour, si on a une étoile quelque part,
                                                          // c'est perdu. On ne peut donc pas avoir d'étoile dans le
                                                          // texte de base
                    res = false;
                }
            }
        }
        return res;
    }

    void addToList(Level[] list, String text, String audio) { // Ajoute un niveau au tableau list. Ecrase le dernier
                                                              // niveau si tableau plein
        int i = 0;
        while (i < numberLevels() && list[i].text != "") {
            i = i + 1;
        }

        list[i].text = text;
        list[i].audio = newSound(audio);
    }

    void testCat(){ // Le test planteras si la premiére ligne de ".scoreboard.csv" est changé
        assertEquals("Nom,Score,Level", cat("../ressources/.scoreboard.csv"));
    }

    String cat(String input) { // Renvoi la premiére ligne d'un fichier
        final String FILENAME = input;
        File f = newFile(FILENAME);
        String currentLine = readLine(f);
        return currentLine;
    }

    void order(String[] array) { // Organise un tableau a une dimensions
        for (int i = 0; i < length(array); i++) {
            for (int j = 0; j < length(array); j++) {
                if (compare(array[i], array[j]) < 0) {
                    String temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    void order(String[][] array, int idx) { // Organise un tableau a deux dimensions en fonction de la idx dimensions
        for (int i = 1; i < length(array); i++) {
            for (int j = 1; j < length(array); j++) {
                if (stringToInt(array[i][idx])  < stringToInt(array[j][idx])) {
                    String[] temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }
            }
        }
    }

    

    void updateList(Level[] list) { // Met a jour le contenu de list
        String[] ressources = getAllFilesFromDirectory("../ressources");
        order(ressources);
        for (int i = 1; i < length(ressources); i++) { // On remplie la liste
            list[i - 1] = new Level();
            list[i - 1].name = ressources[i];
            list[i - 1].text = cat("../ressources/" + ressources[i] + "/texte.txt");
            list[i - 1].audio = newSound("../ressources/" + ressources[i] + "/audio.mp3");
        }
    }

    void testNumberLevels(){ // Si des niveaux sont ajouté ou retiré, ce test planteras.
        assertEquals(10, numberLevels());
    }

    int numberLevels() { // On compte le nombre de dossier dan ressources pour compter les niveau
        return length(getAllFilesFromDirectory("../ressources")) - 1;
    }

    int readInt_s(){ // Manière sécurisé de recuperer un INT a l'utilisateur
        String convert;
        boolean lettre;
        do{
            lettre=false;
            convert = readString();
            if(convert!=""){
                for(int i = 0; i<length(convert);i++){
                    if(charAt(convert, i)<'0' || charAt(convert, i)>'9'){
                        lettre=true;
                    }
                }
            }
        }while(lettre);
        return stringToInt(convert);
    }

    Level menu(Level[] list) { // Menu de debut
        Level res = new Level();
        int input;
        boolean exit = false;
        while (!exit) {
            updateList(list); // On met a jour pour être sur d'avoir les bonnes valeurs
            println("Quel niveau voulez vous lancer ? (0 pour afficher le tableau des scores)\n");
            for (int i = 0; i < length(list); i++) {
                println((i + 1) + ". " + list[i].name); // Affiche chaque niveau avec son numéro
            }
            do {
                print("Entrez une réponse entre 0 et " + (length(list)) + " :");
                input = readInt_s();
            } while (input >= length(list) + 1 || input < 0); // on boucle tant que la réponse n'est pas compatible
            if (input != 0) {
                res = list[input - 1];
                exit = true;
            } else{
                String level = "";
                
                println("Tapez le nom du niveau pour le quel vous voulez avoir le score:");
                level=readString();
                showScoreboard(level);
            }
        }
        return res;
    }

    void showScoreboard(String level) { // Affiche le scoreboard selon le niveau
        CSVFile scoreboardCSV = loadCSV("../ressources/.scoreboard.csv");
        String[][] scoreboard = new String[rowCount(scoreboardCSV) + 1][columnCount(scoreboardCSV)];
        CSVToTab(scoreboardCSV, scoreboard);
        String tmpName="";
        clearScreen();

        for (int i = 0; i < 64; i++) {
            print("_");
        }
        print("\n|Nom                 |Score               |Level               |\n");
        for (int i = 0; i < length(scoreboard, 1) - 1; i++) {
            if(equals(scoreboard[i][2],level)){
                for (int y = 0; y < length(scoreboard, 2); y++) {
                
                    if(length(scoreboard[i][y])>20){
                        tmpName=substring(scoreboard[i][y], 0, 17)+"...";
                    }else{
                        tmpName=scoreboard[i][y];
                    }
                    print("|" + tmpName);
                    for (int idx = 0; idx < 20 - length(scoreboard[i][y]); idx++) {
                        print(" ");
                    }
                }
                print("|\n");    
            }  
        }
        for (int i = 0; i < 64; i++) {
            print("‾");
        }
        print("\n");
    }

    void save(int points, Level level, String name) { // Sauvegarde une partie dans le scoreboard selon les points, le nom du niveau et le nom du joueur
        CSVFile scoreboardCSV = loadCSV("../ressources/.scoreboard.csv");
        String[][] scoreboard = new String[rowCount(scoreboardCSV) + 1][columnCount(scoreboardCSV)];
        CSVToTab(scoreboardCSV, scoreboard);

        scoreboard[rowCount(scoreboardCSV)][0] = name;
        scoreboard[rowCount(scoreboardCSV)][1] = intToString(points);
        scoreboard[rowCount(scoreboardCSV)][2] = level.name;
        order(scoreboard,1);
        saveCSV(scoreboard, "../ressources/.scoreboard.csv");
    }

    void CSVToTab(CSVFile fichier, String[][] tab) { // transforme le contenu d'un csv en tableau a double entrée
        for (int i = 0; i < rowCount(fichier); i++) {
            for (int y = 0; y < columnCount(fichier); y++) {
                tab[i][y] = getCell(fichier, i, y);
            }
        }
    }

    void testIntToString(){
        assertEquals("13420", intToString(13420));
        assertEquals("50", intToString(50));
        assertEquals("0", intToString(0));
        assertEquals("-30", intToString(-30));
    }

    String intToString(int n) { // Transforme un INT en string
        String res = "";
        boolean neg=false;

        if(n==0){
            res="0";
        }else{
            if(n<0){
                n=n*-1;
                neg=true;
            }
            while (n > 0) {
                res = (n % 10) + res;
                n = n / 10;
            }
        }

        if(neg){
            res= "-"+res;
        }
        
        return res;
    }

    void algorithm() { 

        clearScreen(); // Efface le contenu du terminal pour avoir une page vide

        Level[] list = new Level[numberLevels()]; // On crée la liste de niveau

        Level level = menu(list); // On choisi le niveau voulu

        String toDiscover = "";
        String[] tabToDiscover = new String[sizeString(level.text)];
        String[] words= new String[sizeString(level.text)];
        int points = 1010; // Points de base, on perd 10 par tentatives donc score max = 500

        init(words, level.text, tabToDiscover); // On initialiste toutes nos var

        while (!verify(tabToDiscover)) { // On boucle tant qu'on a pas gagné
            clearScreen();
            points = points - 10; // On énléve des points
            play(level.audio, true); // On joue l'audio
            println("⌜‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾⌝\n| Veuillez taper ce que vous entendez        |\n⌞____________________________________________⌟\n\n"
                    + tabToString(tabToDiscover) + "\n"); // On imprime le tableau a découvrir
            String input = userInput();
            points = discover(words,tabToDiscover, input, points); // On découvre le tableau
            stop(level.audio); // On arréte de jouer l'audio
        }

        println("Vous avez fait "
                + points +
                " points ! Bien joué !" +
                "\nSous quel nom voulez vous enregistrer ce score ?");// Quand le joueur a terminé,
                                                                      // on lui affiche son score
        String name = readString();

        save(points, level, name); // On sauvegarde le score dans le tableau des score.

    }

}