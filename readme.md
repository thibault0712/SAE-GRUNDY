# SAE-GRUNDY
# Guide Git pour Débutants 🚀

## Introduction
Ce guide est conçu pour aider un utilisateur à modifier le code et à envoyer ses modifications vers un dépôt Git (push), même sans connaissances préalables en Git.

---

## 1. Cloner le dépôt
Avant de pouvoir modifier le code, vous devez obtenir une copie locale du projet.

### Étape 1 : Cloner le dépôt
Ouvrez votre terminal et tapez cette commande :
```bash
git clone https://github.com/thibault0712/SAE-GRUNDY
```

Cela va créer une copie du projet sur votre machine, dans un dossier appelé `SAE - GRUNDY`.

---

## 2. Modifier le code
Une fois que le projet est cloné, ouvrez-le avec votre éditeur de texte préféré (comme VSCode, Sublime Text, ou même Notepad++). Vous pouvez maintenant apporter vos modifications au code.

---

## 3. Sauvegarder vos modifications (commit)
Une fois les modifications effectuées, il faut les "enregistrer" dans Git.

### Étape 1 : Ajouter vos fichiers modifiés
Dans le terminal, placez-vous dans le dossier du projet cloné :
```bash
cd 'SAE - GRUNDY'
```

Ensuite, pour ajouter vos modifications (les fichiers que vous avez changés) :
```bash
git add .
```
Cela ajoutera toutes les modifications dans le projet.

### Étape 2 : Sauvegarder avec un commit
Maintenant, pour sauvegarder vos changements, tapez cette commande avec un message qui décrit ce que vous avez modifié :
```bash
git commit -m "Description de vos changements"
```

---

## 4. Envoyer vos modifications (push)
Maintenant que vos changements sont sauvegardés localement, il faut les envoyer vers le dépôt distant (sur GitHub).

### Étape 1 : Envoyer les changements
Pour envoyer vos modifications sur le dépôt distant, tapez cette commande :
```bash
git push origin main
```

Cela mettra à jour le dépôt avec les nouvelles modifications que vous avez faites.

⚠️ **Important :** Ne jamais utiliser `git push --force`, car cela peut écraser les modifications des autres. Si vous n'êtes pas sûr, demandez confirmation avant d'utiliser cette commande.

---

## 5. Vérifier les dernières modifications (pull)
Si d'autres personnes ont modifié le code, vous devez récupérer leurs changements avant de commencer à travailler sur vos propres modifications.

### Étape 1 : Récupérer les dernières modifications
Avant de commencer à travailler, tapez cette commande pour récupérer les dernières mises à jour du dépôt :
```bash
git pull origin main
```

Cela garantit que vous travaillez sur la version la plus récente du projet.

---

## Astuces utiles
- Utilisez toujours `git status` pour vérifier l'état de votre dépôt. Cela vous indiquera si vous avez des modifications non enregistrées.
- Si vous ne savez pas ce qui a changé, tapez `git diff` pour voir les différences avant de committer.

---
