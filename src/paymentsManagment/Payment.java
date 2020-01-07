package paymentsManagment;

import salesManagment.Sale;

import java.sql.Timestamp;

public class Payment {

    private long id;
    private long num;
    private double amount;
    private java.sql.Timestamp date;
    private String type;
    private Sale sale;

    public Payment(long id, long num, double amount, Timestamp date, String type, Sale sale) {
        this.id = id;
        this.num = num;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.sale = sale;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", num=" + num +
                ", amount=" + amount +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", sale=" + sale +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getSaleID() {
        return sale.getId();
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


    public java.sql.Timestamp getDate() {
        return date;
    }

    public void setDate(java.sql.Timestamp date) {
        this.date = date;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void update(Payment p) {
        this.id = p.id;
        this.num = p.num;
        this.amount = p.amount;
        this.date = p.date;
        this.type = p.type;
        this.sale = p.sale;
    }
}

