import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Bibliotheque maBibliotheque = new Bibliotheque();
        Scanner scanner = new Scanner(System.in);

        // 1. Chargement automatique au démarrage
        GestionFichiers.charger(maBibliotheque);

        // 2. Initialisation par défaut uniquement si aucun fichier n'existe encore
        if (maBibliotheque.getCatalogue().isEmpty() && maBibliotheque.getAdherents().isEmpty()) {
            System.out.println("Premier lancement : création des données initiales...");
            maBibliotheque.ajouterLivreSilencieux(new Livre("L'Étranger", "Albert Camus", "ISBN001"));
            maBibliotheque.ajouterLivreSilencieux(new Livre("1984", "George Orwell", "ISBN002"));
            maBibliotheque.ajouterLivreSilencieux(new Livre("Le Petit Prince", "Antoine de Saint-Exupéry", "ISBN003"));
            maBibliotheque.ajouterLivreSilencieux(new Livre("Clean Code", "Robert C. Martin", "ISBN004"));

            maBibliotheque.inscrireAdherentSilencieux(new Adherent("Dupont", "Jean", "ADH01"));
            maBibliotheque.inscrireAdherentSilencieux(new Adherent("Kabila", "Sarah", "ADH02"));

            GestionFichiers.sauvegarder(maBibliotheque);
        } else {
            System.out.println("Données restaurées : " + maBibliotheque.getCatalogue().size() 
                             + " livre(s) et " + maBibliotheque.getAdherents().size() + " adhérent(s).");
        }

        int choix = 0;

        while (choix != 8) {
            System.out.println("\n==========================================");
            System.out.println("     SYSTÈME DE GESTION DE BIBLIOTHÈQUE");
            System.out.println("==========================================");
            System.out.println("1. Ajouter un nouveau livre");
            System.out.println("2. Inscrire un nouvel adhérent");
            System.out.println("3. Afficher les livres disponibles");
            System.out.println("4. Rechercher un livre par titre");
            System.out.println("5. Emprunter un livre");
            System.out.println("6. Retourner un livre");
            System.out.println("7. Afficher les emprunts d'un adhérent");
            System.out.println("8. Quitter");
            System.out.print("Votre choix : ");

            if (scanner.hasNextInt()) {
                choix = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Veuillez entrer un chiffre valide.");
                scanner.nextLine();
                continue;
            }

            switch (choix) {
                case 1:
                    System.out.print("Titre du livre : ");
                    String titre = scanner.nextLine();
                    System.out.print("Auteur : ");
                    String auteur = scanner.nextLine();
                    System.out.print("ISBN : ");
                    String isbn = scanner.nextLine();
                    if (maBibliotheque.ajouterLivre(new Livre(titre, auteur, isbn))) {
                        GestionFichiers.sauvegarder(maBibliotheque);
                    }
                    break;

                case 2:
                    System.out.print("Nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Numéro d'identification (ex: ADH03) : ");
                    String id = scanner.nextLine();
                    if (maBibliotheque.inscrireAdherent(new Adherent(nom, prenom, id))) {
                        GestionFichiers.sauvegarder(maBibliotheque);
                    }
                    break;

                case 3:
                    maBibliotheque.afficherLivresDisponibles();
                    break;

                case 4:
                    System.out.print("Entrez un mot-clé du titre : ");
                    String motCle = scanner.nextLine();
                    maBibliotheque.rechercherParTitre(motCle);
                    break;

                case 5:
                    System.out.print("ISBN du livre à emprunter : ");
                    String isbnEmp = scanner.nextLine();
                    System.out.print("ID de l'adhérent : ");
                    String idEmp = scanner.nextLine();
                    if (maBibliotheque.emprunterLivre(isbnEmp, idEmp)) {
                        GestionFichiers.sauvegarder(maBibliotheque);
                    }
                    break;

                case 6:
                    System.out.print("ISBN du livre à retourner : ");
                    String isbnRet = scanner.nextLine();
                    System.out.print("ID de l'adhérent : ");
                    String idRet = scanner.nextLine();
                    if (maBibliotheque.retournerLivre(isbnRet, idRet)) {
                        GestionFichiers.sauvegarder(maBibliotheque);
                    }
                    break;

                case 7:
                    System.out.print("ID de l'adhérent : ");
                    String idAdh = scanner.nextLine();
                    Adherent adh = maBibliotheque.trouverAdherent(idAdh);
                    if (adh != null) {
                        adh.afficherLivresEmpruntes();
                    } else {
                        System.out.println("Adhérent introuvable.");
                    }
                    break;

                case 8:
                    GestionFichiers.sauvegarder(maBibliotheque);
                    System.out.println("Données sauvegardées. Fermeture de l'application !");
                    break;

                default:
                    System.out.println("Option invalide. Réessayez.");
            }
        }
        scanner.close();
    }
}