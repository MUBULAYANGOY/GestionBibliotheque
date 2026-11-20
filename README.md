# GestionBibliotheque

Application Java en mode console permettant de gérer le catalogue d'une bibliothèque, les adhérents et les emprunts de livres.

Le projet utilise uniquement les bibliothèques standard de Java : aucune dépendance externe ni base de données n'est nécessaire.

## Table des matières

- [Objectifs du projet](#objectifs-du-projet)
- [Fonctionnalités](#fonctionnalités)
- [Architecture du projet](#architecture-du-projet)
- [Composants du code](#composants-du-code)
- [Fonctionnement général](#fonctionnement-général)
- [Prérequis](#prérequis)
- [Installation](#installation)
- [Compilation et exécution](#compilation-et-exécution)
- [Utilisation pas à pas](#utilisation-pas-à-pas)
- [Format des fichiers de données](#format-des-fichiers-de-données)
- [Règles de gestion](#règles-de-gestion)
- [Arborescence](#arborescence)
- [Limitations et pistes d'amélioration](#limitations-et-pistes-damélioration)

## Objectifs du projet

Cette application permet de :

- ajouter des livres au catalogue ;
- inscrire des adhérents ;
- rechercher un livre par une partie de son titre ;
- consulter les livres disponibles ;
- emprunter et retourner un livre ;
- consulter les emprunts d'un adhérent ;
- conserver automatiquement les données entre deux exécutions.

## Fonctionnalités

### Gestion des livres

- ajout d'un livre avec son titre, son auteur et son ISBN ;
- contrôle de l'unicité de l'ISBN ;
- affichage de tous les livres disponibles ;
- recherche insensible à la casse par mot-clé dans le titre ;
- statut disponible ou emprunté.

### Gestion des adhérents

- inscription avec nom, prénom et identifiant unique ;
- contrôle de l'unicité de l'identifiant ;
- consultation de la liste des livres empruntés ;
- limite de trois emprunts par adhérent.

### Gestion des emprunts

- vérification de l'existence du livre ;
- vérification de l'existence de l'adhérent ;
- vérification de la disponibilité du livre ;
- vérification de la limite de trois emprunts ;
- mise à jour simultanée du livre et de l'adhérent ;
- sauvegarde immédiate après chaque opération réussie.

## Architecture du projet

Le projet suit une organisation simple en classes, chaque classe ayant une responsabilité distincte :

```text
Main
  ├── Bibliotheque
  │     ├── Liste de Livre
  │     └── Liste d'Adherent
  ├── GestionFichiers
  │     ├── livres.txt
  │     └── adherents.txt
  └── Scanner
```

- `Main` représente l'interface console et le lancement du programme.
- `Bibliotheque` centralise les données et les règles métier.
- `Livre` représente un livre.
- `Adherent` représente un adhérent et ses emprunts.
- `GestionFichiers` assure le chargement et la sauvegarde des données.

## Composants du code

### `Livre`

La classe `Livre` contient :

- `titre` : titre du livre ;
- `auteur` : nom de l'auteur ;
- `isbn` : identifiant du livre ;
- `disponible` : indique si le livre peut être emprunté.

À la création, un livre est disponible par défaut. La méthode `toString()` affiche son ISBN, son titre, son auteur et son état actuel.

Exemple d'affichage :

```text
[ISBN001] "L'Étranger" de Albert Camus - Statut : Disponible
```

### `Adherent`

La classe `Adherent` contient :

- `nom` ;
- `prenom` ;
- `numeroId` ;
- `livresEmpruntes` : liste des livres actuellement empruntés.

Chaque adhérent peut emprunter au maximum trois livres grâce à la constante `LIMITE_EMPRUNTS`. Les méthodes `ajouterEmprunt()` et `retirerEmprunt()` modifient la liste des emprunts.

### `Bibliotheque`

La classe `Bibliotheque` contient deux collections :

- `catalogue` : liste de tous les livres ;
- `adherents` : liste de tous les adhérents.

Elle fournit les principales opérations :

- `ajouterLivre()` ;
- `inscrireAdherent()` ;
- `trouverLivre()` ;
- `trouverAdherent()` ;
- `afficherLivresDisponibles()` ;
- `rechercherParTitre()` ;
- `emprunterLivre()` ;
- `retournerLivre()` ;
- deux méthodes silencieuses utilisées au chargement des fichiers : `ajouterLivreSilencieux()` et `inscrireAdherentSilencieux()`.

Les recherches d'ISBN et d'identifiant sont insensibles à la casse.

### `GestionFichiers`

La classe `GestionFichiers` utilise les classes Java d'entrée/sortie avec l'encodage UTF-8.

Elle sauvegarde les données dans :

- `livres.txt` ;
- `adherents.txt`.

Elle charge les données au démarrage puis relie les ISBN enregistrés pour un adhérent aux objets `Livre` correspondant du catalogue.

Les caractères `;` et `,` sont remplacés par des espaces avant l'écriture afin de ne pas corrompre le format CSV utilisé par les fichiers.

### `Main`

La classe `Main` est le point d'entrée du programme. Elle :

1. crée la bibliothèque ;
2. crée le lecteur clavier `Scanner` ;
3. charge les données sauvegardées ;
4. initialise les données par défaut uniquement si aucun fichier ne contient de données ;
5. affiche un menu dans une boucle ;
6. exécute l'action choisie ;
7. sauvegarde les modifications réussies ;
8. sauvegarde une dernière fois avant de quitter.

## Fonctionnement général

### Séquence au lancement

1. Java lance la méthode `Main.main()`.
2. Une nouvelle instance de `Bibliotheque` est créée.
3. `GestionFichiers.charger()` lit les fichiers texte.
4. Les livres sont chargés dans le catalogue.
5. Les adhérents sont chargés.
6. Les ISBN figurant dans les emprunts d'un adhérent sont associés aux livres correspondants.
7. Si les fichiers sont absents ou ne contiennent aucune donnée valide, des données initiales sont créées.
8. Le menu console est affiché.

### Séquence d'un emprunt

1. L'utilisateur saisit l'ISBN du livre.
2. L'utilisateur saisit l'identifiant de l'adhérent.
3. `Bibliotheque.emprunterLivre()` recherche les deux objets.
4. Le programme vérifie que le livre existe.
5. Le programme vérifie que l'adhérent existe.
6. Le programme vérifie que le livre est disponible.
7. Le programme vérifie que l'adhérent n'a pas atteint trois emprunts.
8. Le statut du livre devient `false`.
9. Le livre est ajouté à la liste de l'adhérent.
10. Les données sont sauvegardées dans les deux fichiers.

### Séquence d'un retour

1. L'utilisateur saisit l'ISBN et l'identifiant.
2. `Bibliotheque.retournerLivre()` vérifie leur existence.
3. Le programme vérifie que l'adhérent possède bien cet emprunt.
4. Le statut du livre redevient disponible.
5. Le livre est retiré de la liste de l'adhérent.
6. Les données sont sauvegardées.

## Prérequis

- Java JDK 21 recommandé ;
- Windows PowerShell ou invite de commandes ;
- Visual Studio Code avec l'extension Java recommandé pour les tâches et le lancement intégrés.

Le `.classpath` du projet cible Java SE 21.

Pour vérifier l'installation de Java :

```powershell
java -version
javac -version
```

## Installation

1. Ouvrez un terminal.
2. Placez-vous à la racine du projet :

```powershell
cd C:\Users\HP\GestionBibliotheque
```

3. Vérifiez que les fichiers sources sont présents :

```powershell
Get-ChildItem src
```

## Compilation et exécution

Il est important d'exécuter le programme depuis la racine du projet. Cette localisation permet à `GestionFichiers` de trouver `livres.txt` et `adherents.txt`.

### Avec PowerShell

Compiler tous les fichiers Java :

```powershell
javac -encoding UTF-8 -d out src\*.java
```

Exécuter le programme :

```powershell
java -cp out Main
```

Compiler et exécuter en une seule séquence :

```powershell
javac -encoding UTF-8 -d out src\*.java
if ($LASTEXITCODE -eq 0) { java -cp out Main }
```

### Avec Visual Studio Code

Deux méthodes sont disponibles :

- appuyez sur `Ctrl+F5` pour lancer `Java: lancer Main` ;
- utilisez les tâches configurées dans `.vscode/tasks.json`, notamment :
  - `Java: compiler le projet` ;
  - `Java: compiler et executer` ;
  - `Java: executer Main`.

## Utilisation pas à pas

Au lancement, un menu contenant huit options s'affiche :

```text
1. Ajouter un nouveau livre
2. Inscrire un nouvel adhérent
3. Afficher les livres disponibles
4. Rechercher un livre par titre
5. Emprunter un livre
6. Retourner un livre
7. Afficher les emprunts d'un adhérent
8. Quitter
```

### 1. Ajouter un livre

1. Choisissez `1`.
2. Saisissez le titre.
3. Saisissez l'auteur.
4. Saisissez l'ISBN.
5. La sauvegarde est automatique si l'ISBN n'existe pas déjà.

### 2. Inscrire un adhérent

1. Choisissez `2`.
2. Saisissez le nom.
3. Saisissez le prénom.
4. Saisissez un identifiant unique.
5. La sauvegarde est automatique si l'identifiant n'existe pas déjà.

### 3. Afficher les livres disponibles

Choisissez `3` pour afficher uniquement les livres dont le statut est disponible.

### 4. Rechercher un livre

1. Choisissez `4`.
2. Saisissez un mot ou une partie du titre.
3. La recherche ignore les majuscules et les minuscules.

Exemple : rechercher `code` trouvera `Clean Code`.

### 5. Emprunter un livre

1. Choisissez `5`.
2. Saisissez l'ISBN exact ou sans différence de casse.
3. Saisissez l'identifiant exact ou sans différence de casse.
4. L'emprunt est refusé si :
   - le livre n'existe pas ;
   - l'adhérent n'existe pas ;
   - le livre est déjà emprunté ;
   - l'adhérent possède déjà trois emprunts.

### 6. Retourner un livre

1. Choisissez `6`.
2. Saisissez l'ISBN.
3. Saisissez l'identifiant de l'adhérent ayant emprunté le livre.
4. Le retour est effectué uniquement si cet adhérent possède réellement l'emprunt.

### 7. Afficher les emprunts d'un adhérent

1. Choisissez `7`.
2. Saisissez l'identifiant de l'adhérent.
3. La liste de ses emprunts actuels est affichée.

### 8. Quitter

Choisissez `8` pour sauvegarder les données une dernière fois et fermer l'application.

## Format des fichiers de données

Les deux fichiers sont au format texte séparé par des points-virgules.

### `livres.txt`

Format :

```text
ISBN;titre;auteur;disponible
```

Exemple :

```text
ISBN001;L'Étranger;Albert Camus;true
ISBN002;1984;George Orwell;true
```

- `true` indique que le livre est disponible ;
- `false` indique que le livre est emprunté.

### `adherents.txt`

Format :

```text
identifiant;nom;prenom;liste-ISBN
```

Plusieurs ISBN empruntés sont séparés par des virgules :

```text
ADH01;Dupont;Jean;ISBN001,ISBN004
```

La partie suivante l'identifiant, le nom et le prénom peut être vide si l'adhérent n'a aucun emprunt.

## Règles de gestion

- un ISBN ne peut être utilisé que par un seul livre ;
- un numéro d'identifiant ne peut être utilisé que par un seul adhérent ;
- un adhérent ne peut pas emprunter plus de trois livres ;
- un livre emprunté ne peut pas être emprunté une seconde fois avant d'être retourné ;
- un adhérent ne peut retourner que les livres qu'il a empruntés ;
- les données sont enregistrées après chaque opération réussie ;
- les fichiers sont lus avec l'encodage UTF-8 ;
- les données initiales ne sont créées que lorsque aucun fichier n'est chargé avec des données.

## Arborescence

```text
GestionBibliotheque/
├── .vscode/
│   ├── launch.json
│   ├── settings.json
│   └── tasks.json
├── out/
├── src/
│   ├── Adherent.java
│   ├── Bibliotheque.java
│   ├── GestionFichiers.java
│   ├── Livre.java
│   └── Main.java
├── adherents.txt
├── livres.txt
├── .classpath
├── .project
├── .gitignore
└── README.md
```

Le dossier `out/` contient les fichiers `.class` générés par la compilation. Il n'a pas besoin d'être ajouté au dépôt et est ignoré par Git selon `.gitignore`.

## Limitations et pistes d'amélioration

Cette version reste volontairement simple :

- les données sont stockées dans des fichiers texte au lieu d'une base de données ;
- il n'y a pas d'interface graphique ;
- il n'y a pas de dates d'emprunt ni de gestion des retards ;
- il n'y a pas de recherche par auteur ;
- il n'y a pas d'authentification ou de rôles ;
- les fichiers ne prennent pas en charge les caractères `;` et `,` dans les données : ils sont nettoyés lors de la sauvegarde ;
- une modification manuelle incorrecte des fichiers peut empêcher leur lecture normale ;
- le format actuel ne garantit pas les transactions lors d'écritures simultanées.

Les améliorations possibles sont :

- remplacer les fichiers texte par SQLite ou une autre base de données ;
- ajouter des dates de prêt et de retour ;
- ajouter des pénalités ou des réservations ;
- ajouter une recherche combinée par titre, auteur et ISBN ;
- créer une interface graphique JavaFX ;
- ajouter des tests unitaires avec JUnit ;
- organiser le projet avec Maven ou Gradle ;
- ajouter la gestion des erreurs de saisie plus complète ;
- ajouter un identifiant interne indépendant de l'ISBN pour éviter les dépendances métier.
