package exercise.android.jasonelter.sandwich_stand;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SomeTests {

    @Test
    public void testSP() {
        SandwichStandApplication.newOrder();
        assertNull(SandwichStandApplication.getCurrentOrderId());

        final String name = "Jerry", comment = "Hi!";
        final int pickles = 7;
        final boolean hummus = true, tahini = false;
        Order toAdd = new Order(name, comment, pickles, hummus, tahini);

        SandwichStandApplication.newOrder(toAdd);
        assertEquals(SandwichStandApplication.getCurrentOrderId(), toAdd.getId());

        SandwichStandApplication.newOrder();
        assertNull(SandwichStandApplication.getCurrentOrderId());
    }

    @Test
    public void checkFirestoreUpload() {
        final String name = "Jerry", comment = "Hi!";
        final int pickles = 7;
        final boolean hummus = true, tahini = false;
        Order toAdd = new Order(name, comment, pickles, hummus, tahini);
        String id = toAdd.getId();

        FirebaseApp.initializeApp(SandwichStandApplication.getInstance());
        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        storage.collection("orders").document(id).delete();
        storage.collection("orders").document(id).get().addOnSuccessListener(result -> fail());

        storage.collection("orders").document(id).set(toAdd);
        storage.collection("orders").document(id).get().addOnSuccessListener(result -> {
            assertNotNull(result);
            Order resultOrder = result.toObject(Order.class);
            assertNotNull(resultOrder);

            assertEquals(name, resultOrder.getCustomerName());
            assertEquals(comment, resultOrder.getComment());
            assertEquals(Order.WAITING, resultOrder.getStatus());
            assertEquals(pickles, resultOrder.getPickles());
            assertEquals(hummus, resultOrder.isHummus());
            assertEquals(tahini, resultOrder.isTahini());
        });
    }

    @Test
    public void checkFirestoreUpdate() {
        final String name = "Jerry", comment = "Hi!";
        final int pickles = 7;
        final boolean hummus = true, tahini = false;
        Order toAdd = new Order(name, comment, pickles, hummus, tahini);
        String id = toAdd.getId();

        FirebaseApp.initializeApp(SandwichStandApplication.getInstance());
        FirebaseFirestore storage = FirebaseFirestore.getInstance();

        storage.collection("orders").document(id).set(toAdd);
        storage.collection("orders").document(id).get().addOnSuccessListener(result -> {
            assertNotNull(result);
            Order resultOrder = result.toObject(Order.class);
            assertNotNull(resultOrder);

            assertEquals(name, resultOrder.getCustomerName());
            assertEquals(comment, resultOrder.getComment());
            assertEquals(Order.WAITING, resultOrder.getStatus());
            assertEquals(pickles, resultOrder.getPickles());
            assertEquals(hummus, resultOrder.isHummus());
            assertEquals(tahini, resultOrder.isTahini());
        });

        final String name2 = "Jerry2", comment2 = "Hi!2";
        final int pickles2 = 8;
        final boolean hummus2 = false, tahini2 = true;

        DocumentReference toUpdate = storage.collection("orders").document(id);
        toUpdate.update("customerName", name2);
        toUpdate.update("comment", comment2);
        toUpdate.update("status", Order.READY);
        toUpdate.update("pickles", pickles2);
        toUpdate.update("hummus", hummus2);
        toUpdate.update("tahini", tahini2);

        storage.collection("orders").document(id).get().addOnSuccessListener(result -> {
            assertNotNull(result);
            Order resultOrder = result.toObject(Order.class);
            assertNotNull(resultOrder);

            assertEquals(name2, resultOrder.getCustomerName());
            assertEquals(comment2, resultOrder.getComment());
            assertEquals(Order.READY, resultOrder.getStatus());
            assertEquals(pickles2, resultOrder.getPickles());
            assertEquals(hummus2, resultOrder.isHummus());
            assertEquals(tahini2, resultOrder.isTahini());
        });
    }
}