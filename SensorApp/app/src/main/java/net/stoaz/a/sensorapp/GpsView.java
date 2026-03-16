package net.stoaz.a.sensorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GpsView extends AppCompatActivity {

    private TextView latitudeTextView;
    private TextView longitudeTextView;
    private LocationManager locationManager;

    private final ActivityResultLauncher<String[]> locationPermissionRequest =
            registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
                Boolean fineGranted = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                Boolean coarseGranted = result.get(Manifest.permission.ACCESS_COARSE_LOCATION);

                if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
                    readCurrentLocation();
                } else {
                    latitudeTextView.setText(R.string.latitude_permission_denied);
                    longitudeTextView.setText(R.string.longitude_permission_denied);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gps_view);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        latitudeTextView = findViewById(R.id.latitudeTextView);
        longitudeTextView = findViewById(R.id.longitudeTextView);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        requestPermissions();
    }

    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            readCurrentLocation();
            return;
        }

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    private void readCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            latitudeTextView.setText(R.string.latitude_permission_missing);
            longitudeTextView.setText(R.string.longitude_permission_missing);
            return;
        }

        String provider = LocationManager.GPS_PROVIDER;
        if (!locationManager.isProviderEnabled(provider)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if (!locationManager.isProviderEnabled(provider)) {
            latitudeTextView.setText(R.string.latitude_location_disabled);
            longitudeTextView.setText(R.string.longitude_location_disabled);
            return;
        }

        locationManager.getCurrentLocation(provider, null, getMainExecutor(), location -> {
            if (location == null) {
                latitudeTextView.setText(R.string.latitude_unavailable);
                longitudeTextView.setText(R.string.longitude_unavailable);
                return;
            }

            latitudeTextView.setText(String.format(java.util.Locale.US, "Latitude: %.6f", location.getLatitude()));
            longitudeTextView.setText(String.format(java.util.Locale.US, "Longitude: %.6f", location.getLongitude()));
        });
    }
}