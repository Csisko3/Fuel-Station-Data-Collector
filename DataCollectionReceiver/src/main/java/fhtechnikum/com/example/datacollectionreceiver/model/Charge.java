package fhtechnikum.com.example.datacollectionreceiver.model;

public class Charge {
    private int id;
    private float kwh;
    private int customerId;

    public Charge(int id, float kwh, int customerId) {
        this.id = id;
        this.kwh = kwh;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public float getKwh() {
        return kwh;
    }

    public int getCustomerId() {
        return customerId;
    }
}
