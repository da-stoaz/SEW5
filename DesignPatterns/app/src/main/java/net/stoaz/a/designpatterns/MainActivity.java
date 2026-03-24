package net.stoaz.a.designpatterns;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "VisitorDemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        runVisitorDemo();
    }

    private void runVisitorDemo() {
        List<Shape> shapes = new ArrayList<>();
        shapes.add(new Circle(3));
        shapes.add(new Rectangle(4, 6));

        ShapeVisitor<String> descriptionVisitor = new DescriptionVisitor();
        ShapeVisitor<Double> areaVisitor = new AreaVisitor();

        StringBuilder result = new StringBuilder("Visitor pattern demo:\n\n");

        for (Shape shape : shapes) {
            String description = shape.accept(descriptionVisitor);
            double area = shape.accept(areaVisitor);

            result.append(description)
                    .append(" -> area = ")
                    .append(String.format(Locale.US, "%.2f", area))
                    .append("\n");
        }

        Log.d(TAG, result.toString());
        Toast.makeText(this, "Check Logcat: " + TAG, Toast.LENGTH_LONG).show();
    }
}
