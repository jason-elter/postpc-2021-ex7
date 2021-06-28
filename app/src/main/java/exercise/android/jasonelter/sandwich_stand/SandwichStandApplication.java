package exercise.android.jasonelter.sandwich_stand;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SandwichStandApplication extends Application {
    private static String currentOrderId;
    private static SharedPreferences sp;
    private static SandwichStandApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        currentOrderId = sp.getString("order_id", null);

        instance = this;
    }

    public static String getCurrentOrderId() {
        return currentOrderId;
    }

    public static void newOrder() {
        currentOrderId = null;
        sp.edit().remove("order_id").apply();
    }

    public static void newOrder(Order order) {
        currentOrderId = order.getId();
        sp.edit().putString("order_id", currentOrderId).apply();
    }

    public static SandwichStandApplication getInstance() {
        return instance;
    }
}