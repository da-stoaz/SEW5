package net.stoaz.firstandroidproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import net.stoaz.firstandroidproject.data.DataClass;
import net.stoaz.firstandroidproject.fragments.MyFragment;

public class Dashboard extends AppCompatActivity implements View.OnClickListener {

    private Button btnSubmit;

    private MyFragment fragMyFragment;
    private EditText inpName;

    private TextView txtCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtCounter = this.findViewById(R.id.txtCounter);

        fragMyFragment = (MyFragment) getSupportFragmentManager().findFragmentById(R.id.fragMyFragment);


        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            DataClass data = bundle.getParcelable("data", DataClass.class);

            if (data != null) {
                txtCounter.setText(Integer.toString(data.getCount()));
                fragMyFragment.setData(data);
                Log.d(Dashboard.class.getSimpleName(), bundle.getString("name"));
                Log.d(Dashboard.class.getSimpleName(), Integer.toString(data.getCount()));
            }

        } else {
            txtCounter.setText("NULL");
        }


        btnSubmit = this.findViewById(R.id.btnSubmit);
        inpName = this.findViewById(R.id.inpName);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        var str = inpName.getText();

        Log.i(R.class.getSimpleName(), "Name: " + str);
    }
}
