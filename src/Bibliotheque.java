import java.util.ArrayList;
import java.util.List;

public class Bibliotheque {
    private List<Livre> catalogue;
    private List<Adherent> adherents;

    public Bibliotheque() {
        this.catalogue = new ArrayList<>();
        this.adherents = new ArrayList<>();
    }

    // --- Accesseurs pour la persistance (GestionFichiers) ---
    public List<Livre> getCatalogue() {
        return catalogue;
    }

    public List<Adherent> getAdherents() {
        return adherents;
    }

    public void ajouterLivreSilencieux(Livre livre) {
        catalogue.add(livre);
    }

    public void inscrireAdherentSilencieux(Adherent adherent) {
        adherents.add(adherent);
    }

    public Livre trouverLivre(String isbn) {
        for (Livre l : catalogue) {
            if (l.getIsbn().equalsIgnoreCase(isbn)) {
                return l;
            }
        }
        return null;
    }

    public Adherent trouverAdherent(String numeroId) {
        for (Adherent a : adherents) {
            if (a.getNumeroId().equalsIgnoreCase(numeroId)) {
                return a;
            }
        }
        return null;
    }

    // --- Gestion des livres ---
    public boolean ajouterLivre(Livre livre) {
        if (trouverLivre(livre.getIsbn()) != null) {
            System.out.println("Erreur : Un livre avec l'ISBN " + livre.getIsbn() + " existe déjà.");
            return false;
        }
        catalogue.add(livre);
        System.out.println("Succès : Le livre \"" + livre.getTitre() + "\" a été ajouté.");
        return true;
    }

    public void afficherLivresDisponibles() {
        System.out.println("\n=== LIVRES DISPONIBLES ===");
        boolean trouve = false;
        for (Livre livre : catalogue) {
            if (livre.isDisponible()) {
                System.out.println(livre);
                trouve = true;
            }
        }
        if (!trouve) {
            System.out.println("Aucun livre disponible pour le moment.");
        }
    }

    public void rechercherParTitre(String motCle) {
        System.out.println("\n=== RÉSULTATS DE RECHERCHE POUR \"" + motCle + "\" ===");
        boolean trouve = false;
        for (Livre livre : catalogue) {
            if (livre.getTitre().toLowerCase().contains(motCle.toLowerCase())) {
                System.out.println(livre);
                trouve = true;
            }
        }
        if (!trouve) {
            System.out.println("Aucun livre ne correspond à cette recherche.");
        }
    }

    // --- Gestion des adhérents ---
    public boolean inscrireAdherent(Adherent adherent) {
        if (trouverAdherent(adherent.getNumeroId()) != null) {
            System.out.println("Erreur : Un adhérent avec l'ID " + adherent.getNumeroId() + " existe déjà.");
            return false;
        }
        adherents.add(adherent);
        System.out.println("Succès : Adhérent " + adherent.getPrenom() + " " + adherent.getNom() + " inscrit.");
        return true;
    }

    // --- Opérations d'emprunt et de retour ---
    public boolean emprunterLivre(String isbn, String numeroIdAdherent) {
        Livre livre = trouverLivre(isbn);
        Adherent adherent = trouverAdherent(numeroIdAdherent);

        if (livre == null) {
            System.out.println("Erreur : Aucun livre trouvé avec l'ISBN " + isbn);
            return false;
        }
        if (adherent == null) {
            System.out.println("Erreur : Aucun adhérent trouvé avec l'ID " + numeroIdAdherent);
            return false;
        }
        if (!livre.isDisponible()) {
            System.out.println("Refus : Le livre \"" + livre.getTitre() + "\" est déjà emprunté.");
            return false;
        }
        if (!adherent.peutEmprunter()) {
            System.out.println("Refus : " + adherent.getPrenom() + " a atteint la limite maximale de 3 emprunts.");
            return false;
        }

        livre.setDisponible(false);
        adherent.ajouterEmprunt(livre);
        System.out.println("Succès : \"" + livre.getTitre() + "\" a été emprunté par " + adherent.getPrenom() + " " + adherent.getNom() + ".");
        return true;
    }

    public boolean retournerLivre(String isbn, String numeroIdAdherent) {
        Livre livre = trouverLivre(isbn);
        Adherent adherent = trouverAdherent(numeroIdAdherent);

        if (livre == null || adherent == null) {
            System.out.println("Erreur : ISBN ou ID adhérent invalide.");
            return false;
        }
        if (!adherent.getLivresEmpruntes().contains(livre)) {
            System.out.println("Erreur : Cet adhérent n'a pas emprunté ce livre.");
            return false;
        }

        livre.setDisponible(true);
        adherent.retirerEmprunt(livre);
        System.out.println("Succès : Le livre \"" + livre.getTitre() + "\" a bien été retourné.");
        return true;
    }
}