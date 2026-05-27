package com.example.geotrackerpro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * MapActivity
 *
 * Affiche une vraie carte OpenStreetMap
 * centrée sur la position utilisateur
 */
public class MapActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Configuration OSMDroid
        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_map);

        map = findViewById(R.id.map);

        // Active zoom tactile
        map.setMultiTouchControls(true);

        // Coordonnées utilisateur
        GeoPoint userLocation = new GeoPoint(
                MainActivity.currentLatitude,
                MainActivity.currentLongitude
        );

        // Zoom initial
        map.getController().setZoom(18.0);

        // Centre carte
        map.getController().setCenter(userLocation);

        // Marqueur
        Marker marker = new Marker(map);
        marker.setPosition(userLocation);
        marker.setTitle("Votre position");

        map.getOverlays().add(marker);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}