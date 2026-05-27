LAB 13 : Application de Localisation avec OpenStreetMap
=======================================================

1\. Prérequis
-------------

*   Android Studio (version récente)
*   Connaissances de base en Android (Java)
*   Émulateur ou appareil Android
*   XAMPP / WAMP pour le backend PHP

2\. Création du projet Android
------------------------------

### Configuration

*   Nom : MapApplication
*   Package : com.example.mapapplication
*   Langage : Java
*   Min SDK : 24

3\. Dépendances (build.gradle.kts)
----------------------------------

    
    implementation(libs.volley)
    implementation(libs.maps.core)
    implementation(libs.osmdroid)
    implementation(libs.appcompat)
    implementation(libs.material)
      

4\. Permissions Android
-----------------------

    
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.INTERNET"/>
      

5\. Interface utilisateur
-------------------------

### Main Activity Layout

    
    <Button
        android:id="@+id/btnMap"
        android:text="Afficher La Map"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"/>
      

### Map Layout

    
    <org.osmdroid.views.MapView
        android:id="@+id/map"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
      

6\. Backend PHP
---------------

### createPosition.php

    
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'];
    $date = $_POST['date'];
    $imei = $_POST['imei'];
    
    INSERT INTO positions (latitude, longitude, date, imei)
    VALUES (:latitude, :longitude, :date, :imei);
      

### getPosition.php

    
    SELECT * FROM positions ORDER BY date DESC;
      

7\. Base de données MySQL
-------------------------

    
    CREATE TABLE positions (
      id INT AUTO_INCREMENT PRIMARY KEY,
      latitude DOUBLE,
      longitude DOUBLE,
      date DATETIME,
      imei VARCHAR(50)
    );
      

8\. Fonctionnement de l’application
-----------------------------------

*   L’application récupère la position GPS
*   Envoie les données vers un serveur PHP
*   Stocke les positions dans MySQL
*   Affiche les positions sur une carte OpenStreetMap

9\. Fonctionnalités principales
-------------------------------

*   Tracking GPS en temps réel
*   Stockage backend PHP/MySQL
*   Affichage OpenStreetMap (OSMDroid)
*   Marqueurs dynamiques

1\. Démonstration du Projet
----------------------------

Glissez-déposez ici votre fichier de démonstration (vidéo, image ou document) pour illustrer le fonctionnement de l’application.


https://github.com/user-attachments/assets/7a3630ec-0479-4809-9ab4-c5662a786aae



11\. Conclusion
---------------

Ce projet permet de comprendre l’intégration complète entre Android, GPS, API REST PHP et OpenStreetMap. Il constitue une base solide pour les applications de géolocalisation modernes.
