package com.example.geotrackerpro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import android.annotation.SuppressLint;
import android.provider.Settings;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * MainActivity
 *
 * Responsabilités :
 * - Gérer permissions GPS
 * - Récupérer localisation
 * - Lancer MapActivity
 */
public class MainActivity extends AppCompatActivity {

    private MaterialButton btnOpenMap;
    private LocationManager locationManager;

    private static final int LOCATION_PERMISSION_CODE = 100;

    // Coordonnées actuelles
    public static double currentLatitude = 0.0;
    public static double currentLongitude = 0.0;

    private RequestQueue requestQueue;

    /*
     API locale
     10.0.2.2 = localhost depuis émulateur Android
    */
    private static final String API_URL =
            "http://10.0.2.2/geotracker_api/save_position.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(this);

        initializeViews();
        initializeLocation();
        setupButton();
    }

    /**
     * Initialise les composants UI
     */
    private void initializeViews() {
        btnOpenMap = findViewById(R.id.btnOpenMap);
    }

    /**
     * Initialise le service GPS
     */
    private void initializeLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        checkAndRequestPermissions();
    }

    /**
     * Configure bouton navigation
     */
    private void setupButton() {
        btnOpenMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Vérifie permissions
     */
    private void checkAndRequestPermissions() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_CODE
            );

        } else {
            startLocationUpdates();
        }
    }

    /**
     * Lance suivi GPS
     */
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                10000,
                10,
                locationListener
        );
    }

    /**
     * Listener GPS
     */
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {

            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            sendLocationToServer(
                    currentLatitude,
                    currentLongitude
            );

            Toast.makeText(
                    MainActivity.this,
                    "Position : " +
                            currentLatitude + ", " +
                            currentLongitude,
                    Toast.LENGTH_SHORT
            ).show();
        }
    };

    /**
     * Résultat permission
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED) {

                startLocationUpdates();

            } else {
                Toast.makeText(
                        this,
                        "Permission GPS refusée",
                        Toast.LENGTH_LONG
                ).show();
            }
        }
    }
    /**
     * Envoie position au serveur
     */
    private void sendLocationToServer(
            double latitude,
            double longitude
    ) {
        try {

            String deviceId = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );

            JSONObject jsonBody = new JSONObject();

            jsonBody.put("latitude", latitude);
            jsonBody.put("longitude", longitude);
            jsonBody.put("device_id", deviceId);

            JsonObjectRequest request =
                    new JsonObjectRequest(
                            Request.Method.POST,
                            API_URL,
                            jsonBody,

                            response -> Toast.makeText(
                                    this,
                                    "Position envoyée",
                                    Toast.LENGTH_SHORT
                            ).show(),

                            error -> Toast.makeText(
                                    this,
                                    "Erreur serveur",
                                    Toast.LENGTH_SHORT
                            ).show()
                    );

            requestQueue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}