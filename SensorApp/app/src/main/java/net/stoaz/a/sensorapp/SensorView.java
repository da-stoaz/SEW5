package net.stoaz.a.sensorapp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SensorView extends AppCompatActivity implements SensorEventListener {

    private Sensor sensor;
    private SensorManager sensorManager;

    private TextView xAxisText;
    private TextView yAxisText;
    private TextView zAxisText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sensor_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        xAxisText = this.findViewById(R.id.xAxisText);
        yAxisText = this.findViewById(R.id.yAxisText);
        zAxisText = this.findViewById(R.id.zAxisText);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            Toast.makeText(this, "Accelerometer detected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Accelerometer not detected", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensor != null) {
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Log.e("SENSOR_UI", "sensor is null; listener not registered");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER) return;
        final float x = sensorEvent.values[0];
        final float y = sensorEvent.values[1];
        final float z = sensorEvent.values[2];

        runOnUiThread(() -> {
            if (xAxisText == null || yAxisText == null || zAxisText == null) {
                Log.e("SENSOR_UI", "TextView is null (check activity_sensor_view.xml ids)");
                return;
            }
            xAxisText.setText(String.format("X-Pos: %.3f", x));
            yAxisText.setText(String.format("Y-Pos: %.3f", y));
            zAxisText.setText(String.format("Z-Pos: %.3f", z));
        });

        Log.v("SENSOR_UI", "x:" + sensorEvent.values[0] + " y:" + sensorEvent.values[1] + " z:" + sensorEvent.values[2]);
    }


}