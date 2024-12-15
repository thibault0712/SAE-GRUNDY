#!/bin/bash

# Répertoire source des fichiers Java
SOURCE_DIR="../src"
# Répertoire cible pour les fichiers compilés
CLASS_DIR="../class"

param1=$1 # all -> on compile SimpleInput et Start en plus des autres fichiers | none -> on ne compile que les autres fichiers
param2=$2 # debug -> on affiche les erreurs de compilation | none -> on ne les affiche pas
param3=$3 # workflow -> on compile les fichiers pour le workflow | none -> on ne compile pas pour le workflow

echo -e "\033[1;34m=== Début de la compilation des fichiers Java dans '$SOURCE_DIR' ===\033[0m"

# Ajouter un espace entre les crochets et les conditions
if [ "$param1" == "all" ]; then
  for fichier in "$SOURCE_DIR"/*; do
    # Vérifier que l'élément est un fichier régulier
if [ -f "$fichier" ] && { [ "$fichier" == "../src/SimpleInput.java" ] || [ "$fichier" == "../src/Start.java" ]; }; then
      echo -e "\033[1;33mCompilation de : $fichier\033[0m"
      if [ "$param2" == "debug" ]; then
          if [ "$param3" == "workflow" ]; then
              javac "$fichier"
          else
              javac -d "$CLASS_DIR" "$fichier"
          fi
      else
          if [ "$param3" == "workflow" ]; then
              javac "$fichier" > /dev/null 2>&1
          else
              javac -d "$CLASS_DIR" "$fichier" > /dev/null 2>&1
          fi
      fi

      # Vérifier si la compilation a réussi
      if [ $? -eq 0 ]; then
        echo -e "\033[1;32m✅️ Compilation réussie pour : $fichier\033[0m"
      else
        echo -e "\033[1;31m❌ Erreur lors de la compilation de : $fichier\033[0m"
        exit 1
      fi
    fi
  done
fi


# Parcourir tous les fichiers dans le répertoire source
for fichier in "$SOURCE_DIR"/*; do
  # Vérifier que l'élément est un fichier régulier
if [ -f "$fichier" ] && [ "$fichier" != "../src/SimpleInput.java" ] && [ "$fichier" != "../src/Start.java" ]; then
    echo -e "\033[1;33mCompilation de : $fichier\033[0m"

    if [ "$param2" == "debug" ]; then
          if [ "$param3" == "workflow" ]; then
              javac "$fichier"
          else
              javac -d "$CLASS_DIR" "$fichier"
          fi
      else
          if [ "$param3" == "workflow" ]; then
              javac "$fichier" > /dev/null 2>&1
          else
              javac -d "$CLASS_DIR" "$fichier" > /dev/null 2>&1
          fi
    fi

    # Vérifier si la compilation a réussi
    if [ $? -eq 0 ]; then
      echo -e "\033[1;32m✅️ Compilation réussie pour : $fichier\033[0m"
    else
      echo -e "\033[1;31m❌ Erreur lors de la compilation de : $fichier\033[0m"
      exit 1
    fi
  fi
done

echo -e "\033[1;34m=== Compilation terminée. Les fichiers compilés sont dans '$CLASS_DIR' ===\033[0m"
