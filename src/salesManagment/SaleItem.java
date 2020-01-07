package salesManagment;


import productsManagement.Product;

import java.util.Objects;

public class SaleItem {

    private long id;
    private long num;
    private long quantity;
    private double subTotal;
    private Product product;
    private Sale sale;

    public SaleItem(Long id, long num, long quantity, Product product, Sale sale) {
        this.id = id == null ? 0 : id;
        this.num = num;
        this.quantity = quantity;
        this.product = product;
        this.sale = sale;
        this.subTotal = quantity * product.getPrice();
    }

    @Override
    public String toString() {
        return "SaleItem{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", subTotal=" + subTotal +
                ", product=" + product +
                ", sale=" + sale +
                '}';
    }

    public long getNum() {
        return num;
    }

    public String getProductName() {
        return product.getDesignation();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }


    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public void update(SaleItem sale2) {
        this.num = sale2.num;
        this.quantity = sale2.quantity;
        this.product = sale2.product;
        this.sale = sale2.sale;
        this.subTotal = sale2.subTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaleItem saleItem = (SaleItem) o;
        return product.equals(saleItem.product) &&
                sale.equals(saleItem.sale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, sale);
    }
}
