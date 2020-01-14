package bank;

import java.io.Serializable;
import java.util.Objects;

public class Account implements Serializable {
    private long id;
    private String number;
    private float balance;

    public Account(long id, String number, float balance) {
        this.id = id;
        this.number = number;
        this.balance = balance;
    }

    public Account(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", balance=" + balance +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id &&
                balance == account.balance &&
                Objects.equals(number, account.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, balance);
    }

    public void update(Account a2) {
//        this.number = a2.number;
        this.balance = a2.balance;
    }
}
