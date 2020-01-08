package clientsManagement;

import java.util.Objects;

public class Client {
    private long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(name, client.name) &&
                Objects.equals(gender, client.gender) &&
                Objects.equals(address, client.address) &&
                Objects.equals(phone, client.phone) &&
                Objects.equals(email, client.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, gender, address, phone, email);
    }

    private String gender;
    private String address;
    private String phone;
    private String email;

    public Client(long id, String name, String gender, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public void update(Client client) {
        id = client.id;
        this.name = client.name;
        this.gender = client.gender;
        this.address = client.address;
        this.phone = client.phone;
        this.email = client.email;
    }

    @Override
    public String toString() {
        return "Client [id=" + id + ", name=" + name + ", gender=" + gender + ", address=" + address
                + ", phone=" + phone + ", email=" + email + "]";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
