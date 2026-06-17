MILOIKOVITCH Maxime, MARCOLET Matteo, LOGEART Pierre, ADAM Tristan
# Détection de Biomes et Écosystèmes Planétaires

Le projet analyse des images de planètes pour détecter des biomes et modéliser des écosystèmes. On utilise des techniques de traitement d'image et des algorithmes de clustering (K-Means & DBSCAN) pour regrouper et identifier les différentes zones géographiques.

## Architecture du Projet

Le code est organisé en packages distincts selon le principe de séparation des responsabilités :

* **app/** : Contient Main.java.
* **biome/** : Regroupe la logique liée à l'identification des biomes. Gère la définition Biome & ReferenceBiomes, l'analyse DetecteurBiomes et le stockage des résultats ResultatBiomes.
* **clustering/** : Implémente les algorithmes avec une interface commune ClusteringInterface implémentée par DBSCAN et KMeans.
* **distance/** : Contient les méthodes de calcul de distance nécessaires aux algorithmes de clustering DistanceEuclidienne implémente l'interface Distance.
* **ecosysteme/** : Gère les écosystèmes à partir des biomes détectés DetecteurEcosystemes & ResultatEcosystemes.
* **image/** : Gère le traitement et la manipulation des images d'entrée avec les algorithmes de flou FlouGaussien & FlouParMoyenne pour réduire le bruit avant l'analyse et lé lecture écriture ImageUtils.
* **utilitaire/** : Contient les fonctions et méthodes Methode.java.

## Fichiers de Test

le projet à des images de test PlaneteX.jpg & PlaneteTest.jpg qui servent de données d'entrée pour tester les algorithmes de détection.

## Exécution

Utilisation : java app.Main <image> [nombreBiomes] [flou]
Exemple : java app.Main Planete1.jpg 10 gaussien
Flous disponibles : gaussien ou moyenne