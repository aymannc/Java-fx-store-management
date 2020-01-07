package salesManagment;


import clientsManagement.Client;

import java.sql.Timestamp;

public class Sale {

    private long id;
    private Client client;
    private double total;
    private Timestamp dateAdded;
    private Timestamp dateModified;
    private Timestamp dateDeleted;

    public Sale(long id, Client client, double total, Timestamp dateAdded, Timestamp dateModified, Timestamp dateDeleted) {
        this.id = id;
        this.client = client;
        this.total = total;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.dateDeleted = dateDeleted;
    }

    public Sale(long id, Client client, Timestamp dateAdded, Timestamp dateModified, Timestamp dateDeleted) {
        this.id = id;
        this.client = client;
        this.dateAdded = dateAdded;
        this.dateModified = dateModified;
        this.dateDeleted = dateDeleted;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", client=" + client +
                ", total=" + total +
                ", dateAdded=" + dateAdded +
                ", dateModified=" + dateModified +
                ", dateDeleted=" + dateDeleted +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public java.sql.Timestamp getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(java.sql.Timestamp dateAdded) {
        this.dateAdded = dateAdded;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public java.sql.Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(java.sql.Timestamp dateModified) {
        this.dateModified = dateModified;
    }


    public java.sql.Timestamp getDateDeleted() {
        return dateDeleted;
    }

    public void setDateDeleted(java.sql.Timestamp dateDeleted) {
        this.dateDeleted = dateDeleted;
    }

    public String getClientName() {
        return client.getName();
    }

    public void update(Sale p) {
        this.id = p.id;
        this.client = p.client;
        this.total = p.total;
        this.dateAdded = p.dateAdded;
        this.dateModified = p.dateModified;
        this.dateDeleted = p.dateDeleted;
    }
}