package exercise.android.jasonelter.sandwich_stand;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        FirebaseFirestore storage = FirebaseFirestore.getInstance();
        String currentOrderId = SandwichStandApplication.getCurrentOrderId();

        if (currentOrderId == null) {
            startNewOrder();
            return;
        }

        storage.collection("orders")
                .document(currentOrderId).get().addOnSuccessListener(result -> {
            Order existingOrder;
            if (result == null ||
                    (existingOrder = result.toObject(Order.class)) == null
                    || existingOrder.getStatus().equals(Order.DONE)) {
                startNewOrder();
                return;
            }

            Intent intent;
            switch (existingOrder.getStatus()) {
                case Order.WAITING:
                    intent = new Intent(this, EditOrderActivity.class);
                    break;
                case Order.IN_PROGRESS:
                    intent = new Intent(this, MakingOrderActivity.class);
                    break;
                case Order.READY:
                    intent = new Intent(this, ReadyOrderActivity.class);
                    break;
                default:
                    return;
            }

            intent.putExtra("current_order", existingOrder);
            startActivity(intent);
            finish();
        });
    }

    private void startNewOrder() {
        Intent intent = new Intent(this, NewOrderActivity.class);
        startActivity(intent);
        finish();
    }
}