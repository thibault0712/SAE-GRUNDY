#!/bin/bash

# Répertoire source des fichiers Java
SOURCE_DIR="../src"
# Répertoire cible pour les fichiers compilés
CLASS_DIR="../class"

echo -e "\033[1;34m=== Début de la compilation des fichiers Java dans '$SOURCE_DIR' ===\033[0m"

# Parcourir tous les fichiers dans le répertoire source
for fichier in "$SOURCE_DIR"/*; do
  # Vérifier que l'élément est un fichier régulier
  if [ -f "$fichier" ]; then
    echo -e "\033[1;33mCompilation de : $fichier\033[0m"
    javac -d "$CLASS_DIR" "$fichier"

    # Vérifier si la compilation a réussi
    if [ $? -eq 0 ]; then
      echo -e "\033[1;32m✅️ Compilation réussie pour : $fichier\033[0m"
    else
      echo -e "\033[1;31m❌ Erreur lors de la compilation de : $fichier\033[0m"
    fi
  fi
done

echo -e "\033[1;34m=== Compilation terminée. Les fichiers compilés sont dans '$CLASS_DIR' ===\033[0m"
