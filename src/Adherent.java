import java.util.ArrayList;
import java.util.List;

public class Adherent {
    private String nom;
    private String prenom;
    private String numeroId;
    private List<Livre> livresEmpruntes;
    private static final int LIMITE_EMPRUNTS = 3;

    public Adherent(String nom, String prenom, String numeroId) {
        this.nom = nom;
        this.prenom = prenom;
        this.numeroId = numeroId;
        this.livresEmpruntes = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getNumeroId() {
        return numeroId;
    }

    public List<Livre> getLivresEmpruntes() {
        return livresEmpruntes;
    }

    public boolean peutEmprunter() {
        return livresEmpruntes.size() < LIMITE_EMPRUNTS;
    }

    public void ajouterEmprunt(Livre livre) {
        if (!livresEmpruntes.contains(livre)) {
            livresEmpruntes.add(livre);
        }
    }

    public void retirerEmprunt(Livre livre) {
        livresEmpruntes.remove(livre);
    }

    public void afficherLivresEmpruntes() {
        System.out.println("\n--- Livres empruntés par " + prenom + " " + nom + " (" + numeroId + ") ---");
        if (livresEmpruntes.isEmpty()) {
            System.out.println("Aucun livre emprunté actuellement.");
        } else {
            for (Livre livre : livresEmpruntes) {
                System.out.println("- " + livre.getTitre() + " (ISBN: " + livre.getIsbn() + ")");
            }
        }
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (ID: " + numeroId + ") - Emprunts : " + livresEmpruntes.size() + "/" + LIMITE_EMPRUNTS;
    }
}