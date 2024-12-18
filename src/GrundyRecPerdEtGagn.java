import java.util.ArrayList;

/**
 * Grundy Game with AI for the machine
 * This program only contains methods to test jouerGagnant()
 * This version is raw without any improvement
 *
 * @author J-F. Kamp, C. Tibermacine, T. FALEZAN, J. MAILLARD
 */

class GrundyRecBrutePerdEtGagn {

    /**
     * Compter per minute to obtain the complexity
     */
    long cpt = 0;

    /**
     * List of known losing positions
     */
    ArrayList<ArrayList<Integer>> posPerdantes = new ArrayList<ArrayList<Integer>>();

    /**
     * Principal method
     */
    void principal() {
		System.out.println("------------------------------------------");
        testJouerGagnant();
        System.out.println("------------------------------------------");
		testPremier();
		System.out.println("------------------------------------------");
		testSuivant();
		System.out.println("------------------------------------------");
		testDisplayMatchsticks();
		System.out.println("------------------------------------------");
		testPlayerEditMatchsticks();
		System.out.println("------------------------------------------");
		testRobotEditMatchsticks() ;
		System.out.println("------------------------------------------");
		testRobotPlayedRandom();
		System.out.println("------------------------------------------");
        testSortGame();
		System.out.println("------------------------------------------");
        testEstConnuePerdante();
		System.out.println("------------------------------------------");
		System.out.println();
        System.out.println("Test d'efficacités");
        testEstGagnanteEfficacite();
		
		System.out.println("Lancement du jeu de Grundy");
        leJeu();
    }

    /**
     * Launch the game
     */
    void leJeu(){
        String playerName;
        String winner = null;
        int nbMatchSticks;
        ArrayList<Integer> jeu;

        do {
            playerName = SimpleInput.getString("Entrer votre nom : ");
            System.out.println();
        } while (playerName == null);

        do{
            nbMatchSticks = SimpleInput.getInt("Veuillez entrer le nombre d'alumette : ");
            System.out.println();
        } while (nbMatchSticks <= 2);

        jeu = new ArrayList<Integer>();
        jeu.add(nbMatchSticks);

        while (winner == null){
            displayMatchsticks(jeu);

            playerEditMatchsticks(jeu, playerName);
            if(!estPossible(jeu)){
                winner = playerName;
            }

            if (winner == null){
                displayMatchsticks(jeu);
                robotEditMatchsticks(jeu);
                if(!estPossible(jeu)){
                    winner = "Robot";
                }
            }
        }

        displayMatchsticks(jeu);
		
		System.out.println();
        System.out.println("Le jeu est terminé, bien joué à " + winner + " !");
        
    }

    /**
     * Show the current game
     * 
     * @param jeu the table of matchticks
     */
    void displayMatchsticks(ArrayList<Integer> jeu){
        System.out.println();
        System.out.print("Jeu actuel : ");

        for (int i = 0; i < jeu.size(); i++) {
            
            if(i != 0){
                System.out.print(" et ");
            }

            for (int j = 0; j < jeu.get(i); j++) {
                System.out.print("|");
            }

        }

        System.out.println();
    }

    /**
	 * Allows the player to modify the number of matchsticks in a pile.
	 * The player cannot remove all the matchsticks from a pile.
	 *
	 * @param jeu List representing the piles of matchsticks.
	 * @param playerName The player's name.
	 */
	void playerEditMatchsticks(ArrayList<Integer> jeu, String playerName) {
		int line;
		int nb;

		
		do {
			line = SimpleInput.getInt("\n" + playerName + " -> Choisissez le tas à modifier (entre 0 et " + (jeu.size() - 1) + ") : ");
			
			if (line < 0 || line >= jeu.size()) {
				System.out.println("Erreur : Le numéro de tas choisi est invalide. Veuillez réessayer. ");
			} 
			else if (jeu.get(line) <= 2) {
				System.out.println("Erreur : Vous ne pouvez pas choisir un tas contenant 1 ou 2 allumettes. Veuillez réessayer. ");
			}
		} while (line < 0 || line >= jeu.size() || jeu.get(line) <= 2);

		
		do {
			nb = SimpleInput.getInt("\n" + playerName + " -> Choisissez le nombre d'allumettes à retirer (entre 1 et " + (jeu.get(line) - 1) + ") : ");
			
			if (nb < 1 || nb >= jeu.get(line)) {
				System.out.println("Erreur : Le nombre d'allumettes à retirer est invalide. Veuillez réessayer. ");
			} 
			else if (jeu.get(line) - nb == nb) {
				System.out.println("Erreur : La séparation en deux tas égaux est interdite. Veuillez réessayer. ");
			}
		} while (nb < 1 || nb >= jeu.get(line) || jeu.get(line) - nb == nb);

		
		enlever(jeu, line, nb);
	}

    /**
     * Edit the matchsticks for the robot
     * 
     * @param jeu the table of matchsticks
     */
    void robotEditMatchsticks(ArrayList<Integer> jeu){
        boolean played = jouerGagnant(jeu);
        int i = 0;
		
		System.out.println();
        System.out.println("Robot -> en train de jouer");

        //If it's impossible to have a winning move, the robot will play by removing 1 matchstick from the first line with more than 2 matchsticks
        while(i < jeu.size() && !played){ 
            if(jeu.get(i) > 2){
                robotPlayedRandom(jeu);
                played = true;
            }
        }
    }

    /**
	 * Allows the robot to play by randomly removing matchsticks,
	 * while respecting the rule of not creating two equal piles.
	 *
	 * @param jeu List representing the piles of matchsticks.
	 */
	void robotPlayedRandom(ArrayList<Integer> jeu) {
		int line; // Index du tas sélectionné
		int nb;   // Nombre d'allumettes à retirer

		System.out.println();
		System.out.println("Robot -> en train de jouer (choix aléatoire)");

		do {
			line = (int) (Math.random() * jeu.size()); // Sélection d'un tas au hasard
		} while (jeu.get(line) <= 2); // Ignorer les tas contenant 2 allumettes ou moins

		do {
			nb = (int) (Math.random() * (jeu.get(line) - 1)) + 1; // Retirer entre 1 et (taille du tas - 1)
		} while (nb == jeu.get(line) / 2); // Éviter de diviser le tas en deux parties égales

		enlever(jeu, line, nb); // Mise à jour du jeu
		System.out.println("Robot a retiré " + nb + " allumette(s) du tas " + line + ".");
	}
	
    /**
     * Plays the winning move if it exists
     * 
     * @param jeu game board
     * @return true if there is a winning move, false otherwise
     */
    boolean jouerGagnant(ArrayList<Integer> jeu) {
	
        boolean gagnant = false;
		
        if (jeu == null) {
            System.err.println("jouerGagnant(): le paramètre jeu est null");
        } else {
            ArrayList<Integer> essai = new ArrayList<Integer>();
			
			// Une toute première décomposition est effectuée à partir de jeu.
			// Cette première décomposition du jeu est enregistrée dans essai.
			// ligne est le numéro de la case du tableau ArrayList (qui commence à zéro) qui
			// mémorise le tas (nbre d'allumettes) qui a été décomposé
            int ligne = premier(jeu, essai);
			
			// mise en oeuvre de la règle numéro2
			// Une situation (ou position) est dite gagnante pour la machine, s’il existe AU MOINS UNE décomposition
			// (c-à-d UNE action qui consiste à décomposer un tas en 2 tas inégaux) perdante pour l’adversaire. C'est
			// évidemment cette décomposition perdante qui sera choisie par la machine.
            while (ligne != -1 && !gagnant) {
				// estPerdante est récursif
                if (estPerdante(essai)) {
					// estPerdante (pour l'adversaire) à true ===> Bingo essai est la décomposition choisie par la machine qui est alors
					// certaine de gagner !!
                    jeu.clear();
                    gagnant = true;
					// essai est recopié dans jeu car essai est la nouvelle situation de jeu après que la machine ait joué (gagnant)
                    for (int i = 0; i < essai.size(); i++) {
                        jeu.add(essai.get(i));
                    }
                } else {
					// estPerdante à false ===> la machine essaye une autre décomposition en faisant appel à "suivant".
					// Si, après exécution de suivant, ligne est à (-1) alors il n'y a plus de décomposition possible à partir de jeu (et on sort du while).
					// En d'autres mots : la machine n'a PAS trouvé à partir de jeu UNE décomposition gagnante.
                    ligne = suivant(jeu, essai, ligne);
                }
            }
        }
		
        return gagnant;
    }
	
    /**
     * RECURSIVE method that indicates if the configuration (of the current game or trial game) is losing.
     * This method is used by the machine to know if the opponent can lose (100%).
     * 
     * @param jeu current game board (the state of the game at a certain moment during the game)
     * @return true if the configuration (of the game) is losing, false otherwise
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
        ArrayList<Integer> gameSorted = sortGame(jeu);
        boolean ret = true; // par défaut la configuration est perdante
		
        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        }else if(estConnuePerdante(jeu)){
            ret = true;
        }else{
			// si il n'y a plus que des tas de 1 ou 2 allumettes dans le plateau de jeu
			// alors la situation est forcément perdante (ret=true) = FIN de la récursivité
            if ( !estPossible(jeu) ) {
                ret = true;
            }else {
				// création d'un jeu d'essais qui va examiner toutes les décompositions
				// possibles à partir de jeu
                ArrayList<Integer> essai = new ArrayList<Integer>(); // size = 0 !
				
				// toute première décomposition : enlever 1 allumette au premier tas qui possède
				// au moins 3 allumettes, ligne = -1 signifie qu'il n'y a plus de tas d'au moins 3 allumettes
                int ligne = premier(jeu, essai);
				
                while ( (ligne != -1) && ret) {
				
					// mise en oeuvre de la règle numéro1
					// Une situation (ou position) est dite perdante si et seulement si TOUTES ses décompositions possibles
					// (c-à-d TOUTES les actions qui consistent à décomposer un tas en 2 tas inégaux) sont TOUTES gagnantes 
					// (pour l’adversaire).
					// L'appel à "estPerdante" est RECURSIF.
					
					// Si "estPerdante(essai)" est true c'est équivalent à "estGagnante" est false, la décomposition
					// essai n'est donc pas gagnante, on sort du while et on renvoie false.
                    if (estPerdante(essai) == true) {
					
						// Si UNE SEULE décomposition (à partir du jeu) est perdante (pour l'adversaire) alors le jeu n'EST PAS perdant.
						// On renverra donc false : la situation (jeu) n'est PAS perdante.
                        ret = false;						
                    } else {
                        // generates the next trial configuration (i.e., a possible decomposition)
                        // from the game, if ligne = -1 there are no more possible decompositions
                        ligne = suivant(jeu, essai, ligne);
                    }

                    cpt += 1;
                }
            }
        }

        if(ret && !estConnuePerdante(gameSorted) && estPossible(gameSorted)){ 
            posPerdantes.add(gameSorted);
        }

        return ret;
    }

    


    /**
     * Indicates if the configuration is know as loosing.
     * @param jeu game board
     * @return true if the configuration is losing, false otherwise
     */
    boolean estConnuePerdante(ArrayList<Integer> jeu) {
        boolean ret = false;
        int i = 0;
        ArrayList<Integer> gameSorted = sortGame(jeu);
        
        while (i < posPerdantes.size() && !ret) {
            if (posPerdantes.get(i).equals(gameSorted)) {
                ret = true;
            }
            i = i + 1;
        }

        return ret;
    }

    /**
     * Sort the game board by ascending order and remove the piles with 1 or 2 matchsticks.
     * @param game game board
     * @return the game board sorted
     */
    ArrayList<Integer> sortGame(ArrayList<Integer> game){
        ArrayList<Integer> gameSorted = new ArrayList<>();

        for (int j = 0; j < game.size(); j++) {
            if (game.get(j) > 2) {
                gameSorted.add(game.get(j));
            }
        }

        for (int i = 0; i < gameSorted.size() - 1; i++) { 
            for (int j = 0; j < gameSorted.size() - i - 1; j++) {
                if (gameSorted.get(j) > gameSorted.get(j + 1)) {
                    int temp = gameSorted.get(j);
                    gameSorted.set(j, gameSorted.get(j + 1));
                    gameSorted.set(j + 1, temp);
                }
            }
        }

        return gameSorted;
    }

    /**
     * Indicates if the configuration is winning.
     * Method that simply call "estPerdante".
     * 
     * @param jeu game board
     * @return true if the configuration is winning, false otherwise
     */
    boolean estGagnante(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estGagnante(): le paramètre jeu est null");
        } else {
            ret = !estPerdante(jeu);
        }
        return ret;
    }

    /**
     * Test the method jouerGagnant()
     */
    void testJouerGagnant() {
        System.out.println();
        System.out.println("*** testJouerGagnant() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(6);
        ArrayList<Integer> resJeu1 = new ArrayList<Integer>();
        resJeu1.add(4);
        resJeu1.add(2);
		
        testCasJouerGagnant(jeu1, resJeu1, true);
        
    }

    /**
     * Test a case of the method jouerGagnant()
     *
     * @param jeu the game board
     * @param resJeu the game board after playing the winning move
     * @param res the expected result of jouerGagnant
     */
    void testCasJouerGagnant(ArrayList<Integer> jeu, ArrayList<Integer> resJeu, boolean res) {
        // Arrange
        System.out.print("jouerGagnant (" + jeu.toString() + ") : ");

        // Act
        boolean resExec = jouerGagnant(jeu);

        // Assert
        System.out.print(jeu.toString() + " " + resExec + " : ");
		boolean egaliteJeux = jeu.equals(resJeu);
        if (  egaliteJeux && (res == resExec) ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }	

    /**
     * Splits a pile of matchsticks from a line in the game (1 line = 1 pile).
     * The new pile is necessarily placed at the end of the list.
     * The pile that is split decreases by the number of matchsticks removed.
     * 
     * @param jeu   list of matchsticks per line
     * @param ligne pile for which the matchsticks must be split
     * @param nb    number of matchsticks REMOVED from the pile (line) during the split
     */
    void enlever ( ArrayList<Integer> jeu, int ligne, int nb ) {
		// traitement des erreurs
        if (jeu == null) {
            System.err.println("enlever() : le paramètre jeu est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("enlever() : le numéro de ligne est trop grand");
        } else if (nb >= jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop grand");
        } else if (nb <= 0) {
            System.err.println("enlever() : le nb d'allumettes à retirer est trop petit");
        } else if (2 * nb == jeu.get(ligne)) {
            System.err.println("enlever() : le nb d'allumettes à retirer est la moitié");
        } else {
			// nouveau tas ajouté au jeu (nécessairement en fin de tableau)
			// ce nouveau tas contient le nbre d'allumettes retirées (nb) du tas à séparer			
            jeu.add(nb);
			// le tas restant possède "nb" allumettes en moins
            jeu.set ( ligne, (jeu.get(ligne) - nb) );
        }
    }

    /**
     * Tests if it is possible to split one of the piles
     * 
     * @param jeu      game board
     * @return true if there is at least one pile with 3 or more matchsticks, false otherwise
     */
    boolean estPossible(ArrayList<Integer> jeu) {
        boolean ret = false;
        if (jeu == null) {
            System.err.println("estPossible(): le paramètre jeu est null");
        } else {
            int i = 0;
            while (i < jeu.size() && !ret) {
                if (jeu.get(i) > 2) {
                    ret = true;
                }
                i = i + 1;
            }
        }
        return ret;
    }

    /**
     * Creates an initial trial configuration from the game
     * 
     * @param jeu      game board
     * @param jeuEssai new configuration of the game
     * @return the number of the pile divided into two or (-1) if there is no pile with at least 3 matchsticks
     */
    int premier(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai) {
	
        int numTas = -1; // pas de tas à séparer par défaut
		int i;
		
        if (jeu == null) {
            System.err.println("premier(): le paramètre jeu est null");
        } else if (!estPossible((jeu)) ){
            System.err.println("premier(): aucun tas n'est divisible");
        } else if (jeuEssai == null) {
            System.err.println("premier(): le paramètre jeuEssai est null");
        } else {
            // avant la copie du jeu dans jeuEssai il y a un reset de jeuEssai 
            jeuEssai.clear(); // size = 0
            i = 0;
			
            // copy each element from jeu to jeuEssai
            // jeuEssai is the same as jeu before the first trial configuration
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
			
            i = 0;
            // search for a pile of at least 3 matchsticks in the game
            // otherwise numTas = -1
            boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {
                
                // if we find a pile with at least 3 matchsticks
				if ( jeuEssai.get(i) >= 3 ) {
					trouve = true;
					numTas = i;
				}
				
				i = i + 1;
            }
			
            // splits the pile (numTas index) into a new pile with ONLY ONE matchstick that is placed at the end of the list
            // the pile at numTas index is decreased by one matchstick (removal of one matchstick)
            // jeuEssai is the game board that shows this split
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }
		
        return numTas;
    }

    /**
     * Test the method premier()
     */
    void testPremier() {
        System.out.println();
        System.out.println("*** testPremier()");

        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        jeu1.add(11);
        int ligne1 = 0;
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(9);
        res1.add(11);
        res1.add(1);
        testCasPremier(jeu1, ligne1, res1);
    }

    /**
     * Test a case of the method testPremier
     * @param jeu the game board
     * @param ligne the number of the pile that was first split
     * @param res the game board after the first split
     */
    void testCasPremier(ArrayList<Integer> jeu, int ligne, ArrayList<Integer> res) {
        // Arrange
        System.out.print("premier (" + jeu.toString() + ") : ");
        ArrayList<Integer> jeuEssai = new ArrayList<Integer>();
        // Act
        int noLigne = premier(jeu, jeuEssai);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
		boolean egaliteJeux = jeuEssai.equals(res);
        if ( egaliteJeux && noLigne == ligne ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }

    /**
     * Generates the next trial configuration (i.e., a possible decomposition)
     * 
     * @param jeu      game board
     * @param jeuEssai trial configuration of the game after splitting
     * @param ligne    the number of the pile that was last split
     * @return the number of the pile divided into two for the new configuration, -1 if no further decomposition is possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
	
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() + ", " + ligne + ") = ");
		
        int numTas = -1; // by default there is no further decomposition possible
		
        int i = 0;
        // error handling
        if (jeu == null) {
            System.err.println("suivant(): le paramètre jeu est null");
        } else if (jeuEssai == null) {
            System.err.println("suivant() : le paramètre jeuEssai est null");
        } else if (ligne >= jeu.size()) {
            System.err.println("suivant(): le paramètre ligne est trop grand");
        }
		
		else {
		
			int nbAllumEnLigne = jeuEssai.get(ligne);
			int nbAllDernCase = jeuEssai.get(jeuEssai.size() - 1);
			
            // if on the same line (passed as a parameter) we can still remove matchsticks,
            // i.e., if the difference between the number of matchsticks on this line and
            // the number of matchsticks at the end of the list is > 2, then we remove another
            // matchstick from this line and add 1 matchstick to the last position
            if ( (nbAllumEnLigne - nbAllDernCase) > 2 ) {
                jeuEssai.set ( ligne, (nbAllumEnLigne - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = ligne;
            } 
			
            // otherwise, we need to examine the next pile (line) in the game to possibly decompose it
            // we create a new trial configuration identical to the game board
			else {
                // copy the game into jeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
				
                boolean separation = false;
                i = ligne + 1; // tas suivant
                // if there is still a pile and it contains at least 3 matchsticks
                // then we perform an initial split by removing 1 matchstick
                while ( i < jeuEssai.size() && !separation ) {
                    // the pile must have at least 3 matchsticks
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
                        // we start by removing 1 matchstick from this pile
                        enlever(jeuEssai, i, 1);
						numTas = i;
                    } else {
                        i = i + 1;
                    }
                }				
            }
        }
		
        return numTas;
    }

    /**
     * Test the method suivant()
     */
    void testSuivant() {
        System.out.println();
        System.out.println("*** testSuivant() ****");

        int ligne1 = 0;
        int resLigne1 = 0;
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(10);
        ArrayList<Integer> jeuEssai1 = new ArrayList<Integer>();
        jeuEssai1.add(9);
        jeuEssai1.add(1);
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(8);
        res1.add(2);
        testCasSuivant(jeu1, jeuEssai1, ligne1, res1, resLigne1);

        int ligne2 = 0;
        int resLigne2 = -1;
        ArrayList<Integer> jeu2 = new ArrayList<Integer>();
        jeu2.add(10);
        ArrayList<Integer> jeuEssai2 = new ArrayList<Integer>();
        jeuEssai2.add(6);
        jeuEssai2.add(4);
        ArrayList<Integer> res2 = new ArrayList<Integer>();
        res2.add(10);
        testCasSuivant(jeu2, jeuEssai2, ligne2, res2, resLigne2);

        int ligne3 = 1;
        int resLigne3 = 1;
        ArrayList<Integer> jeu3 = new ArrayList<Integer>();
        jeu3.add(4);
        jeu3.add(6);
        jeu3.add(3);
        ArrayList<Integer> jeuEssai3 = new ArrayList<Integer>();
        jeuEssai3.add(4);
        jeuEssai3.add(5);
        jeuEssai3.add(3);
        jeuEssai3.add(1);
        ArrayList<Integer> res3 = new ArrayList<Integer>();
        res3.add(4);
        res3.add(4);
        res3.add(3);
        res3.add(2);
        testCasSuivant(jeu3, jeuEssai3, ligne3, res3, resLigne3);

    }

    /**
     * Test a case of the method suivant
     * 
     * @param jeu the game board
     * @param jeuEssai the game board obtained after splitting a pile
     * @param ligne the number of the pile that was last split
     * @param resJeu the expected jeuEssai after splitting
     * @param resLigne the expected number of the pile that was split
     */
    void testCasSuivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne, ArrayList<Integer> resJeu, int resLigne) {
        // Arrange
        System.out.print("suivant (" + jeu.toString() + ", " + jeuEssai.toString() + ", " + ligne + ") : ");
        // Act
        int noLigne = suivant(jeu, jeuEssai, ligne);
        // Assert
        System.out.println("\nnoLigne = " + noLigne + " jeuEssai = " + jeuEssai.toString());
		boolean egaliteJeux = jeuEssai.equals(resJeu);
        if ( egaliteJeux && noLigne == resLigne ) {
            System.out.println("OK\n");
        } else {
            System.err.println("ERREUR\n");
        }
    }


    /**
     * Test the efficacity of the method estGagnante
     */
    void testEstGagnanteEfficacite(){
            ArrayList<Integer> jeu;
            int n;
            long t1, t2, diffT;
    
            n = 10;
    
            System.out.println("\n\t\t Test de l'efficacité estGagnante\n");
    
            for ( int i = 1; i <= 5; i++ ) {
                posPerdantes.clear();
                jeu = new ArrayList<Integer>();
                jeu.add(n);
                cpt = 0;
        
                t1 = System.currentTimeMillis();
                estGagnante(jeu);
                t2 = System.currentTimeMillis();
                diffT = (t2 - t1);
    
                System.out.println ( "***********test " + i );
                System.out.println ( "Nombre tas = " + jeu.size());
                System.out.println ( "nbAlumette = " + n);
                System.out.println ( "Cpt = " + cpt );
                System.out.println ( "Tps = " + diffT + " ms" );
    
                n = n + 3;
            }    
    }

    	/**
	 * Test case for displayMatchsticks method
	 * 
	 * @param jeu      the input array representing matchsticks
	 * @param expected the expected string representation of the game
	 */
	void testCasDisplayMatchsticks(ArrayList<Integer> jeu, String expected) {
		System.out.println();
		System.out.println("Test avec le jeu : " + jeu);
		System.out.println("Attendu : " + expected);
		System.out.print("Résultat : ");
		displayMatchsticks(jeu);
		System.out.println();
	}
		
	/**
	 * Test the displayMatchsticks method
	 */
	void testDisplayMatchsticks() {
		System.out.println();
		System.out.println("*** testDisplayMatchsticks() ***");
		// Cas 1 : jeu avec plusieurs tas d'allumettes
		ArrayList<Integer> jeu1 = new ArrayList<>();
		jeu1.add(3);
		jeu1.add(5);
		jeu1.add(2);
		testCasDisplayMatchsticks(jeu1, "||| et ||||| et ||");
		
		// Cas 2 : jeu avec un seul tas
		ArrayList<Integer> jeu2 = new ArrayList<>();
		jeu2.add(7);
		testCasDisplayMatchsticks(jeu2, "|||||||");
		
		// Cas 3 : jeu vide
		ArrayList<Integer> jeu3 = new ArrayList<>();
		testCasDisplayMatchsticks(jeu3, " ");
		
		// Cas 4 : jeu avec un tas vide (0 allumettes)
		ArrayList<Integer> jeu4 = new ArrayList<>();
		jeu4.add(0);
		testCasDisplayMatchsticks(jeu4, " ");
	}
    /**
	 * Test case for playerEditMatchsticks
	 * 
	 * @param jeu          The initial list of matchstick piles.
	 * @param playerName   The player's name.
	 * @param expected     The expected state of the matchstick piles after modification.
	 */
	void testCasPlayerEditMatchsticks(ArrayList<Integer> jeu, String playerName, ArrayList<Integer> expected) {
		System.out.println("\nTest avec le jeu : " + jeu );
		System.out.println("Attendu : " + expected);
		
		playerEditMatchsticks(jeu, playerName);
		System.out.println("Résultat : " + jeu);
		System.out.println(expected.equals(jeu) ? "OK " : "ECHEC ");
		System.out.println();
	}
	/**
	 * Test the playerEditMatchsticks method
	 */
	void testPlayerEditMatchsticks() {
		System.out.println();
		System.out.println("*** testPlayerEditMatchsticks() ***");
		// Cas 1 : Modification réussie
		ArrayList<Integer> jeu1 = new ArrayList<>();
		jeu1.add(5);
		jeu1.add(8);
		jeu1.add(3);
		ArrayList<Integer> res1 = new ArrayList<>();
		res1.add(5);
		res1.add(5);
		res1.add(3);
		res1.add(3);
		
		System.out.println();
		System.out.println("Cas 1 : Veuillez entrer les valeurs correspondantes (tas = 1, allumettes = 3)");
		testCasPlayerEditMatchsticks(jeu1, "Joueur 1", res1);
		// Cas 2 : Tentative sur un tas invalide
		ArrayList<Integer> jeu2 = new ArrayList<>();
		jeu2.add(5);
		jeu2.add(8);
		jeu2.add(2);
		ArrayList<Integer> res2 = new ArrayList<>();
		res2.add(5);
		res2.add(6);
		res2.add(2);
		res2.add(2);
		
		System.out.println("Cas 2 : Veuillez entrer les valeur correspondantes ( tas = 2) puis ( tas = 1, allumettes = 2) ");
		testCasPlayerEditMatchsticks(jeu2, "Joueur 2", res2);
		// Cas 3 : Tentative de retirer toutes les allumettes (interdit)
		ArrayList<Integer> jeu3 = new ArrayList<>();
		jeu3.add(7);
		jeu3.add(10);
		ArrayList<Integer> res3 = new ArrayList<>();
		res3.add(6);
		res3.add(10);
		res3.add(1);
		System.out.println("Cas 3 : Veuillez retirer toutes les allumettes du ( tas = 0) , puis entrer les valeurs correspondantes ( allumettes = 1 )");
		testCasPlayerEditMatchsticks(jeu3, "Joueur 1", res3);
		// Cas 4 : Séparation interdite en deux tas égaux
		ArrayList<Integer> jeu4 = new ArrayList<>();
		jeu4.add(6);
		jeu4.add(9);
		ArrayList<Integer> res4 = new ArrayList<>();
		res4.add(4);
		res4.add(9);
		res4.add(2);
		
		System.out.println("Cas 4 : Veuillez prendre le ( tas = 0 ) et le diviser en deux tas égaux, puis entrer les valeurs correspondantes (  allumettes = 2) ");
		testCasPlayerEditMatchsticks(jeu4, "Joueur 2", res4);
	}

      
    /**
     * Test case for robotEditMatchsticks
     *
     * @param jeu          The initial list of matchstick piles.
     * @param expected     The expected state of the matchstick piles after modification.
     * @param casErreur	   A variable who check if a situation is an Error case
     */
    void testCasRobotEditMatchsticks(ArrayList<Integer> jeu, ArrayList<Integer> expected, boolean casErreur) {
        System.out.println("\nTest avec le jeu : " + jeu);
        if (casErreur == false){
			System.out.println("Attendu : " + expected);
			robotEditMatchsticks(jeu);
			System.out.println("Résultat : " + jeu);
			System.out.println(expected.equals(jeu) ? "OK" : "ECHEC");
			System.out.println();
		}
        else{
			System.out.println("erreur : robotEditMatchsticks -> situation impossible");
			System.out.println();
		}
    }

    /**
     * Test the robotEditMatchsticks method
     */
    void testRobotEditMatchsticks() {
        System.out.println();
        System.out.println("*** testRobotEditMatchsticks() ***");
        // Cas 1 : Robot performs a winning move
        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(5);
        jeu1.add(7);
        ArrayList<Integer> res1 = new ArrayList<>();
        res1.add(4);
        res1.add(7); // Robot removes 1 matchstick to avoid equal piles
        res1.add(1);
        System.out.println("Cas 1 : Le robot effectue un mouvement gagnant sur le tas 1 (allumettes = 1)");
        testCasRobotEditMatchsticks(jeu1, res1, false);
        // Cas 2 : Robot plays randomly when no winning move is available
        ArrayList<Integer> jeu2 = new ArrayList<>();
        jeu2.add(4);
        jeu2.add(2);
        jeu2.add(1);
        ArrayList<Integer> res2 = new ArrayList<>();
        res2.add(1);
        res2.add(2);
        res2.add(1);
        res2.add(3);
       
        
        System.out.println("Cas 2 : Le robot joue un mouvement aléatoire");
        testCasRobotEditMatchsticks(jeu2, res2, false);
        // Cas 3 : Robot plays on the only valid pile
        ArrayList<Integer> jeu3 = new ArrayList<>();
        jeu3.add(1);
        jeu3.add(8);
        ArrayList<Integer> res3 = new ArrayList<>();
        res3.add(1);
        res3.add(7);
        res3.add(1); // Robot removes 1 matchstick from the second pile
        System.out.println("Cas 3 : Le robot joue sur le tas 1 avec 1 allumette enlevée");
        testCasRobotEditMatchsticks(jeu3, res3, false);
        // Cas 4 : No moves possible for the robot (all piles have <= 2 matchsticks)
        ArrayList<Integer> jeu4 = new ArrayList<>();
        jeu4.add(2);
        jeu4.add(2);
        ArrayList<Integer> res4 = new ArrayList<>();
        res4.add(2);
        res4.add(2); // No change, robot cannot play
        System.out.println("Cas 4 : Aucun mouvement possible pour le robot");
        testCasRobotEditMatchsticks(jeu4, res4, true);
    }

    	 /**
     * Test case for robotPlayedRandom
     *
     * @param jeu          The initial list of matchstick piles.
     * @param expectedSize The expected number of piles after the robot's move (if a new pile is created).
     * @param casErreur	   A variable who check if a situation is an Error case
     */
    void testCasRobotPlayedRandom(ArrayList<Integer> jeu, int expectedSize, boolean casErreur) {
        if (casErreur == false){
			System.out.println("\nTest avec le jeu : " + jeu);
			System.out.println("Taille attendue après coup du robot : " + expectedSize);
			robotPlayedRandom(jeu);
			System.out.println("Résultat après coup du robot : " + jeu);
			System.out.println(jeu.size() == expectedSize ? "OK" : "ECHEC");
			System.out.println();
		}
        else {
			System.out.println("erreur : robotPlayedRandom -> situation impossible");
			System.out.println();
		}
        
    }

    /**
     * Test the robotPlayedRandom method
     */
    void testRobotPlayedRandom() {
        System.out.println();
        System.out.println("*** testRobotPlayedRandom() ***");
        // Cas 1 : Robot removes matchsticks from a valid pile
        ArrayList<Integer> jeu1 = new ArrayList<>();
        jeu1.add(5);
        jeu1.add(8);
        jeu1.add(3);
        int expectedSize1 = 4;
        System.out.println("Cas 1 : Le robot joue sur un tas valide (pas de tas supprimé)");
        testCasRobotPlayedRandom(jeu1, expectedSize1, false);
        // Cas 2 : Robot avoids piles with 2 or fewer matchsticks
        ArrayList<Integer> jeu2 = new ArrayList<>();
        jeu2.add(2);
        jeu2.add(9);
        jeu2.add(1);
        int expectedSize2 = 4; 
        
        System.out.println("Cas 2 : Le robot évite les tas avec 2 allumettes ou moins");
        testCasRobotPlayedRandom(jeu2, expectedSize2, false);
        // Cas 3 : Robot avoids splitting a pile into two equal parts
        ArrayList<Integer> jeu3 = new ArrayList<>();
        jeu3.add(10);
        jeu3.add(5);
        int expectedSize3 = 3; 
        System.out.println("Cas 3 : Le robot évite de diviser un tas en deux parties égales");
        testCasRobotPlayedRandom(jeu3, expectedSize3, false);
        // Cas 4 : Robot plays on the only valid pile
        ArrayList<Integer> jeu4 = new ArrayList<>();
        jeu4.add(1);
        jeu4.add(2);
        jeu4.add(6);
        int expectedSize4 = 4; 
        System.out.println("Cas 4 : Le robot joue sur le seul tas valide");
        testCasRobotPlayedRandom(jeu4, expectedSize4, false);
        // Cas 5 : No valid moves (all piles have <= 2 matchsticks)
        ArrayList<Integer> jeu5 = new ArrayList<>();
        jeu5.add(2);
        jeu5.add(1);
        jeu5.add(2);
        int expectedSize5 = 3; // No changes occur
        System.out.println("Cas 5 : Aucun mouvement valide possible");
        testCasRobotPlayedRandom(jeu5, expectedSize5, true);
    }


    /**
     * Test a case of the method estConnuePerdante
     * @param jeu the game board
     * @param result the expected result
     */
    void testCasEstConnuePerdante(ArrayList<Integer> jeu, boolean result){
        // Arrange
        System.out.print("estConnuePerdante (" + jeu.toString() + ") : ");
        // Act
        boolean resExec = estConnuePerdante(jeu);
        // Assert
        System.out.print(resExec);
        if (result == resExec) {
            System.out.println(" -> OK");
        } else {
            System.err.println(" -> ERREUR");
        }
    }


    /**
     * Test the method estConnuePerdante
     */
    void testEstConnuePerdante(){
        System.out.println();
        System.out.println("*** testEstConnuePerdante() ***");

        System.out.println("Test des cas normaux");
        //Test 1
        ArrayList<Integer> jeu1 = new ArrayList<Integer>();
        jeu1.add(4);
        jeu1.add(6);
        posPerdantes.clear();
        posPerdantes.add(jeu1);
        testCasEstConnuePerdante(jeu1, true);
        //Test 2
        ArrayList<Integer> jeu2 = new ArrayList<Integer>();
        jeu1.add(5);
        jeu1.add(7);
        posPerdantes.clear();
        testCasEstConnuePerdante(jeu2, false);
    }

    /**
     * Test a case of the method sortGame
     * @param game the game board
     * @param result the expected result
     */
    void testCasSortGame(ArrayList<Integer> game, ArrayList<Integer> result){
        System.out.print("sortGame (" + game.toString() + ") : ");

        ArrayList<Integer> resExec = sortGame(game);
        System.out.print(resExec);
        if (result.equals(resExec)) {
            System.out.println(" -> OK");
        } else {
            System.err.println(" -> ERREUR");
        }
    }

    /**
     * Test the method sortGame
     */
    void testSortGame(){
        System.out.println();
        System.out.println("*** testSortGame() ***");

        System.out.println("Test des cas normaux");
        ArrayList<Integer> game1 = new ArrayList<Integer>();
        game1.add(11);
        game1.add(1);
        game1.add(10);
        game1.add(2);
        game1.add(1);
        ArrayList<Integer> res1 = new ArrayList<Integer>();
        res1.add(10);
        res1.add(11);
        testCasSortGame(game1, res1);

        System.out.println("Test des cas limite");
        ArrayList<Integer> game2 = new ArrayList<Integer>();
        game2.add(1);
        game2.add(2);
        game2.add(1);
        game2.add(2);
        ArrayList<Integer> res2 = new ArrayList<Integer>();
        testCasSortGame(game2, res2);
    }

}