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
import com.google.firebase.firestore.ListenerRegistration;

public class EditOrderActivity extends AppCompatActivity {

    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_order);

        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        // Get order from intent
        Intent receivedIntent = getIntent();
        Order receivedOrder = (Order) receivedIntent.getSerializableExtra("current_order");

        // Find all UI components.
        EditText editTextName = findViewById(R.id.editTextName);
        Slider picklesSlider = findViewById(R.id.picklesSlider);
        CheckBox hummusCheckbox = findViewById(R.id.hummusCheckbox);
        CheckBox tahiniCheckbox = findViewById(R.id.tahiniCheckbox);
        EditText editTextComments = findViewById(R.id.editTextComments);
        Button buttonDeleteOrder = findViewById(R.id.buttonDeleteOrder);
        Button buttonSaveOrder = findViewById(R.id.buttonSaveOrder);

        // Set up fields
        editTextName.setText(receivedOrder.getCustomerName());
        picklesSlider.setValue(receivedOrder.getPickles());
        hummusCheckbox.setChecked(receivedOrder.isHummus());
        tahiniCheckbox.setChecked(receivedOrder.isTahini());
        editTextComments.setText(receivedOrder.getComment());

        // Set listeners.
        editTextName.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void afterTextChanged(Editable s) {
                // text did change
                String name = editTextName.getText().toString();
                buttonSaveOrder.setEnabled(!name.isEmpty());
            }
        });

        buttonSaveOrder.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            String comment = editTextComments.getText().toString();
            Order newOrder = new Order(receivedOrder.getId(), receivedOrder.getStatus(), name,
                    comment, (int) picklesSlider.getValue(), hummusCheckbox.isChecked(),
                    tahiniCheckbox.isChecked());

            storage.collection("orders").document(newOrder.getId()).set(newOrder);
        });

        buttonDeleteOrder.setOnClickListener(v -> {
            SandwichStandApplication.newOrder();
            Task<Void> orderTask = storage.collection("orders")
                    .document(receivedOrder.getId()).delete();

            orderTask.addOnSuccessListener(unused -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                erase();
                finish();
            });
        });

        listenerRegistration = storage.collection("orders")
                .document(receivedOrder.getId()).addSnapshotListener((value, error) -> {
            if (error != null || value == null || !value.exists()) {
                return;
            }

            Order updatedOrder = value.toObject(Order.class);

            Intent intent;
            switch (updatedOrder.getStatus()) {
                case Order.IN_PROGRESS:
                    intent = new Intent(this, MakingOrderActivity.class);
                    intent.putExtra("current_order", updatedOrder);
                    break;
                case Order.READY:
                    intent = new Intent(this, ReadyOrderActivity.class);
                    intent.putExtra("current_order", updatedOrder);
                    break;
                case Order.DONE:
                    intent = new Intent(this, NewOrderActivity.class);
                    break;
                default:
                    return;
            }

            startActivity(intent);
            finish();
        });
    }

    private void erase() {
        SandwichStandApplication.newOrder();
        listenerRegistration.remove();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        erase();
    }
}