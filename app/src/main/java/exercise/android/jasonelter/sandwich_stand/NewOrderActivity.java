package exercise.android.jasonelter.sandwich_stand;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.android.material.slider.Slider;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        // Find all UI components.
        EditText editTextName = findViewById(R.id.editTextName);
        Slider picklesSlider = findViewById(R.id.picklesSlider);
        CheckBox hummusCheckbox = findViewById(R.id.hummusCheckbox);
        CheckBox tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        EditText editTextComments = findViewById(R.id.editTextComments);
        Button buttonSubmitOrder = findViewById(R.id.buttonSubmitOrder);

        editTextName.setText("");

        // Set listeners.
        editTextName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                // text did change
                String name = editTextName.getText().toString();
                buttonSubmitOrder.setEnabled(!name.isEmpty());
            }
        });

        buttonSubmitOrder.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String comment = editTextComments.getText().toString();
            Order newOrder = new Order(name, comment, (int) picklesSlider.getValue(),
                    hummusCheckbox.isChecked(), tahiniCheckbox.isChecked());

            SandwichStandApplication.newOrder(newOrder);

            Task<Void> orderTask = storage.collection("orders")
                    .document(newOrder.getId()).set(newOrder);

            orderTask.addOnSuccessListener(unused -> {
                Intent intent = new Intent(this, EditOrderActivity.class);
                intent.putExtra("current_order", newOrder);
                startActivity(intent);
                finish();
            });
        });
    }
}