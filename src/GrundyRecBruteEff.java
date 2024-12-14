import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Grundy Game with AI for the machine
 * This program only contains methods to test jouerGagnant()
 * This version is raw without any improvement
 *
 * @author J-F. Kamp, C. Tibermacine, T. FALEZAN, G. MAILLARD
 */

class GrundyRecBrute {

    /**
     * Main Principal
     */
    void principal() {
        testJouerGagnant();
		testPremier();
		testSuivant();

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

            displayMatchsticks(jeu);

            robotEditMatchsticks(jeu);
            if(!estPossible(jeu)){
                winner = "Robot";
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
    
    void testDisplayMatchsticks(){
		
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
     * Joue le coup gagnant s'il existe
     * 
     * @param jeu plateau de jeu
     * @return vrai s'il y a un coup gagnant, faux sinon
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
     * Méthode RECURSIVE qui indique si la configuration (du jeu actuel ou jeu d'essai) est perdante.
	 * Cette méthode est utilisée par la machine pour savoir si l'adversaire peut perdre (à 100%).
     * 
     * @param jeu plateau de jeu actuel (l'état du jeu à un certain moment au cours de la partie)
     * @return vrai si la configuration (du jeu) est perdante, faux sinon
     */
    boolean estPerdante(ArrayList<Integer> jeu) {
	
        boolean ret = true; // par défaut la configuration est perdante
		
		
        if (jeu == null) {
            System.err.println("estPerdante(): le paramètre jeu est null");
        }
		
		else {
			// si il n'y a plus que des tas de 1 ou 2 allumettes dans le plateau de jeu
			// alors la situation est forcément perdante (ret=true) = FIN de la récursivité
            if ( !estPossible(jeu) ) {
                ret = true;
            }
			
			else {
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
						// génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
						// à partir du jeu, si ligne = -1 il n'y a plus de décomposition possible
                        ligne = suivant(jeu, essai, ligne);
                    }
                }
            }
        }
		
        return ret;
    }
	
	/**
     * Indique si la configuration est gagnante.
	 * Méthode qui appelle simplement "estPerdante".
     * 
     * @param jeu plateau de jeu
     * @return vrai si la configuration est gagnante, faux sinon
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
     * Tests succincts de la méthode joueurGagnant()
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
     * Test d'un cas de la méthode jouerGagnant()
	 *
	 * @param jeu le plateau de jeu
	 * @param resJeu le plateau de jeu après avoir joué gagnant
	 * @param res le résultat attendu par jouerGagnant
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
     * Divise en deux tas les allumettes d'une ligne de jeu (1 ligne = 1 tas).
	 * Le nouveau tas se place nécessairement en fin de tableau.
	 * Le tas qui est divisé diminue du nombre d'allumettes enlevées.
     * 
     * @param jeu   tableau des allumettes par ligne
     * @param ligne tas pour lequel les allumettes doivent être séparées
     * @param nb    nombre d'allumettes RETIREE du tas (ligne) lors de la séparation
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
     * Teste s'il est possible de séparer un des tas
     * 
     * @param jeu      plateau de jeu
     * @return vrai s'il existe au moins un tas de 3 allumettes ou plus, faux sinon
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
     * Crée une toute première configuration d'essai à partir du jeu
     * 
     * @param jeu      plateau de jeu
     * @param jeuEssai nouvelle configuration du jeu
     * @return le numéro du tas divisé en deux ou (-1) si il n'y a pas de tas d'au moins 3 allumettes
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
			
			// recopie case par case de jeu dans jeuEssai
			// jeuEssai est le même que le jeu avant la première configuration d'essai
            while (i < jeu.size()) {
                jeuEssai.add(jeu.get(i));
                i = i + 1;
            }
			
            i = 0;
			// rechercher un tas d'allumettes d'au moins 3 allumettes dans le jeu
			// sinon numTas = -1
			boolean trouve = false;
            while ( (i < jeu.size()) && !trouve) {
				
				// si on trouve un tas d'au moins 3 allumettes
				if ( jeuEssai.get(i) >= 3 ) {
					trouve = true;
					numTas = i;
				}
				
				i = i + 1;
            }
			
			// sépare le tas (case numTas) en un nouveau tas d'UNE SEULE allumette qui vient se placer en fin du tableau 
			// le tas en case numTas a diminué d'une allumette (retrait d'une allumette)
			// jeuEssai est le plateau de jeu qui fait apparaître cette séparation
            if ( numTas != -1 ) enlever ( jeuEssai, numTas, 1 );
        }
		
        return numTas;
    }

    /**
     * Tests succincts de la méthode premier()
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
     * Test un cas de la méthode testPremier
	 * @param jeu le plateau de jeu
	 * @param ligne le numéro du tas séparé en premier
	 * @param res le plateau de jeu après une première séparation
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
     * Génère la configuration d'essai suivante (c'est-à-dire UNE décomposition possible)
     * 
     * @param jeu      plateau de jeu
     * @param jeuEssai configuration d'essai du jeu après séparation
     * @param ligne    le numéro du tas qui est le dernier à avoir été séparé
     * @return le numéro du tas divisé en deux pour la nouvelle configuration, -1 si plus aucune décomposition n'est possible
     */
    int suivant(ArrayList<Integer> jeu, ArrayList<Integer> jeuEssai, int ligne) {
	
        // System.out.println("suivant(" + jeu.toString() + ", " +jeuEssai.toString() +
        // ", " + ligne + ") = ");
		
		int numTas = -1; // par défaut il n'y a plus de décomposition possible
		
        int i = 0;
		// traitement des erreurs
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
			
			// si sur la même ligne (passée en paramètre) on peut encore retirer des allumettes,
			// c-à-d si l'écart entre le nombre d'allumettes sur cette ligne et
			// le nombre d'allumettes en fin de tableau est > 2, alors on retire encore
			// 1 allumette sur cette ligne et on ajoute 1 allumette en dernière case		
            if ( (nbAllumEnLigne - nbAllDernCase) > 2 ) {
                jeuEssai.set ( ligne, (nbAllumEnLigne - 1) );
                jeuEssai.set ( jeuEssai.size() - 1, (nbAllDernCase + 1) );
                numTas = ligne;
            } 
			
			// sinon il faut examiner le tas (ligne) suivant du jeu pour éventuellement le décomposer
			// on recrée une nouvelle configuration d'essai identique au plateau de jeu
			else {
                // copie du jeu dans JeuEssai
                jeuEssai.clear();
                for (i = 0; i < jeu.size(); i++) {
                    jeuEssai.add(jeu.get(i));
                }
				
                boolean separation = false;
                i = ligne + 1; // tas suivant
				// si il y a encore un tas et qu'il contient au moins 3 allumettes
				// alors on effectue une première séparation en enlevant 1 allumette
                while ( i < jeuEssai.size() && !separation ) {
					// le tas doit faire minimum 3 allumettes
                    if ( jeu.get(i) > 2 ) {
                        separation = true;
						// on commence par enlever 1 allumette à ce tas
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
     * Tests succincts de la méthode suivant()
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
     * Test un cas de la méthode suivant
	 * 
	 * @param jeu le plateau de jeu
	 * @param jeuEssai le plateau de jeu obtenu après avoir séparé un tas
	 * @param ligne le numéro du tas qui est le dernier à avoir été séparé
	 * @param resJeu est le jeuEssai attendu après séparation
	 * @param resLigne est le numéro attendu du tas qui est séparé
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

}
