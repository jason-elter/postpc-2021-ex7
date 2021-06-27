package exercise.android.jasonelter.sandwich_stand;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class MakingOrderActivity extends AppCompatActivity {

    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_order);

        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        // Get order from intent
        Intent receivedIntent = getIntent();
        Order receivedOrder = (Order) receivedIntent.getSerializableExtra("current_order");

        listenerRegistration = storage.collection("orders")
                .document(receivedOrder.getId()).addSnapshotListener((value, error) -> {
            if (error != null || value == null || !value.exists()) {
                return;
            }

            Order updatedOrder = value.toObject(Order.class);

            Intent intent;
            switch (updatedOrder.getStatus()) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        listenerRegistration.remove();
    }
}