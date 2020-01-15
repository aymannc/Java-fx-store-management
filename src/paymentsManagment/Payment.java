package paymentsManagment;

import salesManagment.Sale;

import java.time.LocalDate;

public class Payment {
    public static String[] paymentsStates = {"Payé", "N.Payé"};
    private Long id;
    private long num;
    private Double amount;
    private LocalDate date;
    private String type;
    private String state;
    private String checknumber;
    private Sale sale;


    public Payment(Long id, long num, Double amount, LocalDate date, String type, String state, String checknumber, Sale sale) {
        this.id = id;
        this.num = num;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.state = state;
        this.checknumber = checknumber;
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
                ", state='" + state + '\'' +
                ", checknumber='" + checknumber + '\'' +
                ", sale=" + sale +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getChecknumber() {
        return checknumber;
    }

    public void setChecknumber(String checknumber) {
        this.checknumber = checknumber;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void update(Payment p) {
        this.amount = p.amount;
        this.date = p.date;
//        this.type = p.type;
//        this.sale = p.sale;
        this.checknumber = p.checknumber;
        this.state = p.state;
    }
}

