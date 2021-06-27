package exercise.android.jasonelter.sandwich_stand;

import java.io.Serializable;
import java.util.UUID;

import javax.annotation.Nonnull;

public class Order implements Serializable {
    public static final String WAITING = "waiting", IN_PROGRESS = "in-progress",
            READY = "ready", DONE = "done";

    private final String id;
    private String customerName, comment, status;
    private int pickles;
    private boolean hummus, tahini;

    public Order() {
        this.id = UUID.randomUUID().toString();
        this.status = WAITING;

        this.customerName = "";
        this.comment = "";
        this.pickles = 0;
        this.hummus = false;
        this.tahini = false;
    }

    public Order(String customerName, String comment, int pickles, boolean hummus, boolean tahini) {
        this.id = UUID.randomUUID().toString();
        this.status = WAITING;

        this.customerName = customerName;
        this.comment = comment;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
    }

    public Order(String id, String status, String customerName, String comment, int pickles, boolean hummus, boolean tahini) {
        this.id = id;
        this.status = status;
        this.customerName = customerName;
        this.comment = comment;
        this.pickles = pickles;
        this.hummus = hummus;
        this.tahini = tahini;
    }

    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public @Nonnull String getStatus() {
        return status;
    }

    private String getNextStatus() {
        switch (status) {
            case WAITING:
                return IN_PROGRESS;
            case IN_PROGRESS:
                return READY;
            default:
                return DONE;
        }
    }

    public void updateStatus() {
        this.status = getNextStatus();
    }

    public int getPickles() {
        return pickles;
    }

    public void setPickles(int pickles) {
        this.pickles = pickles;
    }

    public boolean isHummus() {
        return hummus;
    }

    public void setHummus(boolean hummus) {
        this.hummus = hummus;
    }

    public boolean isTahini() {
        return tahini;
    }

    public void setTahini(boolean tahini) {
        this.tahini = tahini;
    }
}
