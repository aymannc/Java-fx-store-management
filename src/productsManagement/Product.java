package productsManagement;

import categoriesManagement.Category;

import java.util.Objects;

public class Product {
    private long code;
    private String designation;
    private Double price;
    private Category category;

    public Product(long code, String designation, Double price, Category category) {
        this.code = code;
        this.designation = designation;
        this.price = price;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Product{" +
                "code=" + code +
                ", designation='" + designation + '\'' +
                ", price=" + price +
                '}';
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void update(Product p) {
        this.code = p.code;
        this.designation = p.designation;
        this.price = p.price;
        this.category = p.category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return code == product.code &&
                designation.equals(product.designation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, designation, price, category);
    }
}


