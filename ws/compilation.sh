#!/bin/bash

# Répertoire source des fichiers Java
SOURCE_DIR="../src"
# Répertoire cible pour les fichiers compilés
CLASS_DIR="../class"

param1=$1 # all -> Compilation de tous les fichiers | workflow -> Compilation pour le workflow GitHub | none -> on compile les fichiers sauf SimpleInput et Start
param2=$2 # debug -> Afficher les messages de compilation | none -> Ne pas afficher les messages de compilation
output="null"


echo -e "\033[1;34m=== Début de la compilation des fichiers Java dans '$SOURCE_DIR' ===\033[0m"



if [ "$param1" == "workflow" ]; then # Compilation pour le workflow GitHub
  javac ../src/*.java
  outputJavaDoc=$(javadoc -encoding UTF8 -private -d ../javaDoc ../src/*.java 2>&1)
  echo $outputJavaDoc
  
  # Vérifier si la compilation a réussi
  if [ $? -eq 0 ]; then
    echo -e "\033[1;32m✅️ Compilation réussie"
  else
    echo -e "\033[1;31m❌ Erreur lors de la compilation"
    exit 1
  fi

  if echo "$outputJavaDoc" | grep -i "warning"; then
    echo -e "\033[1;33m❌ Erreur des warnings on été détecté sur la javadoc !\033[0m"
    exit 1
  fi
elif [ "$param1" == "all" ]; then # Compilation de tous les fichiers
  for fichier in "$SOURCE_DIR"/*; do
    # Vérifier que l'élément est un fichier régulier
    if [ -f "$fichier" ] && { [ "$fichier" == "../src/SimpleInput.java" ] || [ "$fichier" == "../src/Start.java" ]; }; then
      echo -e "\033[1;33mCompilation de : $fichier\033[0m"
      if [ "$param2" == "debug" ]; then
          javac -d "$CLASS_DIR" "$fichier"
      else
          javac -d "$CLASS_DIR" "$fichier" > /dev/null 2>&1
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

if [ "$param1" != "workflow" ]; then
  # Parcourir tous les fichiers dans le répertoire source
  for fichier in "$SOURCE_DIR"/*; do
    # Vérifier que l'élément est un fichier régulier
    if [ -f "$fichier" ] && [ "$fichier" != "../src/SimpleInput.java" ] && [ "$fichier" != "../src/Start.java" ]; then
      echo -e "\033[1;33mCompilation de : $fichier\033[0m"

      if [ "$param2" == "debug" ]; then
          javac -d "$CLASS_DIR" "$fichier"
      else
          javac -d "$CLASS_DIR" "$fichier" > /dev/null 2>&1
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

echo -e "\033[1;34m=== Compilation terminée. Les fichiers compilés sont dans '$CLASS_DIR' ===\033[0m"
