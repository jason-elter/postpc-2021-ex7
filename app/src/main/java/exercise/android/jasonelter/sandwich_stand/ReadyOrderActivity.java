package exercise.android.jasonelter.sandwich_stand;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ReadyOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_order);

        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        // Get order from intent
        Intent receivedIntent = getIntent();
        Order receivedOrder = (Order) receivedIntent.getSerializableExtra("current_order");

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(v -> storage.collection("orders")
                .document(receivedOrder.getId()).update("status", Order.DONE)
                .addOnSuccessListener(unused -> {
                    SandwichStandApplication.newOrder();
                    Intent intent = new Intent(this, NewOrderActivity.class);
                    startActivity(intent);
                    finish();
                }));
    }
}