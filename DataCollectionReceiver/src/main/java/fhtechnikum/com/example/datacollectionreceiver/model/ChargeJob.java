package fhtechnikum.com.example.datacollectionreceiver.model;

import java.util.ArrayList;
import java.util.List;

public class ChargeJob {
    private String customerId;
    private List<Charge> charges = new ArrayList<>();

    public ChargeJob(String customerId) {
        this.customerId = customerId;
    }

    public void addCharge(Charge charge) {
        charges.add(charge);
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<Charge> getCharges() {
        return charges;
    }

    public boolean isComplete(int expectedCount) {
        return charges.size() >= expectedCount;
    }
}
