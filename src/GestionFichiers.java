import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GestionFichiers {
    private static final String FICHIER_LIVRES = "livres.txt";
    private static final String FICHIER_ADHERENTS = "adherents.txt";

    // Nettoie les caractères spéciaux (';' et ',') pour éviter de corrompre le format CSV
    private static String nettoyer(String texte) {
        return texte.replace(";", " ").replace(",", " ").trim();
    }

    // --- SAUVEGARDE ---
    public static void sauvegarder(Bibliotheque biblio) {
        sauvegarderLivres(biblio.getCatalogue());
        sauvegarderAdherents(biblio.getAdherents());
    }

    private static void sauvegarderLivres(List<Livre> livres) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FICHIER_LIVRES), StandardCharsets.UTF_8))) {
            for (Livre l : livres) {
                writer.write(nettoyer(l.getIsbn()) + ";" 
                           + nettoyer(l.getTitre()) + ";" 
                           + nettoyer(l.getAuteur()) + ";" 
                           + l.isDisponible());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des livres : " + e.getMessage());
        }
    }

    private static void sauvegarderAdherents(List<Adherent> adherents) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(FICHIER_ADHERENTS), StandardCharsets.UTF_8))) {
            for (Adherent a : adherents) {
                StringBuilder isbnsEmpruntes = new StringBuilder();
                List<Livre> emprunts = a.getLivresEmpruntes();
                for (int i = 0; i < emprunts.size(); i++) {
                    isbnsEmpruntes.append(nettoyer(emprunts.get(i).getIsbn()));
                    if (i < emprunts.size() - 1) {
                        isbnsEmpruntes.append(",");
                    }
                }
                writer.write(nettoyer(a.getNumeroId()) + ";" 
                           + nettoyer(a.getNom()) + ";" 
                           + nettoyer(a.getPrenom()) + ";" 
                           + isbnsEmpruntes);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des adhérents : " + e.getMessage());
        }
    }

    // --- CHARGEMENT ---
    public static void charger(Bibliotheque biblio) {
        chargerLivres(biblio);
        chargerAdherents(biblio);
    }

    private static void chargerLivres(Bibliotheque biblio) {
        File fichier = new File(FICHIER_LIVRES);
        if (!fichier.exists()) return;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                String[] parts = ligne.split(";");
                if (parts.length == 4) {
                    Livre livre = new Livre(parts[1], parts[2], parts[0]);
                    livre.setDisponible(Boolean.parseBoolean(parts[3]));
                    biblio.ajouterLivreSilencieux(livre);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des livres : " + e.getMessage());
        }
    }

    private static void chargerAdherents(Bibliotheque biblio) {
        File fichier = new File(FICHIER_ADHERENTS);
        if (!fichier.exists()) return;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(fichier), StandardCharsets.UTF_8))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                if (ligne.trim().isEmpty()) continue;
                String[] parts = ligne.split(";", -1);
                if (parts.length >= 3) {
                    Adherent adherent = new Adherent(parts[1], parts[2], parts[0]);
                    biblio.inscrireAdherentSilencieux(adherent);

                    if (parts.length == 4 && !parts[3].isEmpty()) {
                        String[] isbns = parts[3].split(",");
                        for (String isbn : isbns) {
                            Livre livre = biblio.trouverLivre(isbn);
                            if (livre != null) {
                                adherent.ajouterEmprunt(livre);
                                livre.setDisponible(false); // Garantit la cohérence de l'état
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des adhérents : " + e.getMessage());
        }
    }
}