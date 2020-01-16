package salesManagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import productsManagement.ProductDAOIMPL;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleItemDAOIMPL implements MagazineDAO<SaleItem> {
    ProductDAOIMPL productDAOIMPL = new ProductDAOIMPL();
    SaleDAOIMPL saleDAOIMPL = new SaleDAOIMPL();
    private Sale sale;
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    public SaleItemDAOIMPL(Sale sale) {
        this.sale = sale;
    }

    @Override
    public SaleItem find(long id) {
        String sql = String.format("select * from `saleitems` where id='%d'", id);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return new SaleItem(resultSet.getLong("id"), resultSet.getInt("num"), resultSet.getInt("quantity"),
                        productDAOIMPL.find(resultSet.getInt("product_id")), sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(SaleItem sale) {
        String sql = String.format("INSERT INTO `saleitems` (`num`,`quantity`, `sub_total`,`sale_id`,`product_id`) VALUES ('%d','%d','%f', '%d', '%d')",
                sale.getNum(), sale.getQuantity(), sale.getSubTotal(), sale.getSale().getId(), sale.getProduct().getCode());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sale.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating SALEITEM failed, no ID obtained.");
                }
            }
            if (saleDAOIMPL.setTotal(this.sale, this.sale.getTotal() + sale.getSubTotal()))
                System.out.println("create Sale total updated");
        } catch (SQLException e) {
            System.out.println("Errors in create" + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(SaleItem sale) {
        try {
            String sql = String.format("delete from saleitems where id=%d", sale.getId());
            System.out.println(sql);
            statement = connection.createStatement();
            statement.execute(sql);

        } catch (SQLException e) {
            System.out.println("Errors in delete" + e.toString());
            return false;
        } catch (NullPointerException e) {
            System.out.println("Null" + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(SaleItem saleitem1, SaleItem saleitem2) {
        String sql = String.format("UPDATE `saleitems` SET `num` = '%d',`quantity` = '%d',`sub_total` = '%f',`sale_id`" +
                        " = '%s' ,`product_id`= '%s' WHERE `id` =%d",
                saleitem2.getNum(), saleitem2.getQuantity(), saleitem2.getSubTotal(), saleitem2.getSale().getId(),
                saleitem2.getProduct().getCode(), saleitem1.getId());

        try {
            statement = connection.createStatement();
            statement.execute(sql);
            saleitem1.update(saleitem2);
            if (updateQuantity(saleitem1))
                return true;
        } catch (SQLException e) {
            System.out.println("Errors in update" + e.toString());
            return false;
        }

        return true;

    }

    private ArrayList<SaleItem> getResult(String sql) {
        ArrayList<SaleItem> list = new ArrayList<SaleItem>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new SaleItem(resultSet.getLong("id"), resultSet.getInt("num"), resultSet.getInt("quantity"),
                        productDAOIMPL.find(resultSet.getInt("product_id")), sale));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;

    }

    public ObservableList<SaleItem> getAllObsList() {
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (Exception e) {
            return null;
        }
    }

    public ObservableList<SaleItem> getAllObsList(String s) {
        return FXCollections.observableArrayList(findAll(s));
    }

    @Override
    public List<SaleItem> findAll() {
        String sql = "select * from saleitems where sale_id =" + sale.getId();
        return getResult(sql);
    }

    @Override
    public List<SaleItem> findAll(String key) {
        // TODO search by product name
        String sql = "select * from saleitems WHERE id LIKE '%" + key + "%'";
        return getResult(sql);

    }

    public ArrayList<SaleItem> getAllArrList() {
        String sql = "select * from saleitems WHERE sale_id = " + sale.getId() + "";
        return getResult(sql);
    }


    public double getSaleTotal() {
        double total = 0.0;
        ArrayList<SaleItem> list = getAllArrList();
        if (list != null)
            for (SaleItem saleItem : list) {
                total += saleItem.getSubTotal();
            }
        return total;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public boolean updateQuantity(SaleItem saleItem) {
        System.out.println("new " + saleItem.getSubTotal());
        String sql = String.format("UPDATE `saleitems` SET `quantity` = '%d',`sub_total` = '%f' WHERE `id` =%d",
                saleItem.getQuantity(),
                saleItem.getSubTotal(),
                saleItem.getId());
        System.out.println(sql);
        try {
            statement = connection.createStatement();
            statement.execute(sql);
            double total = getSaleTotal();
            if (saleDAOIMPL.setTotal(saleItem.getSale(), total)) {
                System.out.println("updateQuantity Sale total updated");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Errors in create" + e.toString());
        }
        return false;
    }
}
