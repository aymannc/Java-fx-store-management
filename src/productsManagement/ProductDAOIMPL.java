package productsManagement;

import categoriesManagement.CategoryDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOIMPL implements MagazineDAO<Product> {
    CategoryDAOIMPL categoryDAOIMPL = new CategoryDAOIMPL();
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Product find(long id) {
        String sql = "select * from products where id='" + id + "'";
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("price"), categoryDAOIMPL.find(resultSet.getLong("category_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Product p) {
        String sql = "INSERT INTO `products` (`id`,`name`, `price`,`category_id`) VALUES ('" + p.getCode() + "','" + p.getDesignation() + "','" +
                p.getPrice() + "', '" + p.getCategory().getId() + "')";
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Errors in create");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Product p) {
        String sql = "delete from products where id=" + p.getCode();

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Errors in delete");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Product p1, Product p2) {
        String sql = "UPDATE `products` SET `id` = '" + p2.getCode() + "',`name` = '" + p2.getDesignation() + "',`price` = '" +
                p2.getPrice() + "' ,`category_id`= '" + p2.getCategory().getId() + "' WHERE `products`.`id` =" + p1.getCode();

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errors in update");
            return false;
        }
        p1.update(p2);
        return true;

    }

    private List<Product> getResult(String sql) {
        List<Product> list = new ArrayList<Product>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Product(resultSet.getInt("id"), resultSet.getString("name"),
                        resultSet.getDouble("price"), categoryDAOIMPL.find(resultSet.getLong("category_id"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;

    }

    public ObservableList<Product> getAll() {
        return FXCollections.observableArrayList(findAll());
    }

    public ObservableList<Product> getAll(String s) {
        return FXCollections.observableArrayList(findAll(s));
    }

    @Override
    public List<Product> findAll() {
        String sql = "select * from products";
        return getResult(sql);
    }

    @Override
    public List<Product> findAll(String key) {
        String sql = "select * from products WHERE name LIKE '%" + key + "%'";
        return getResult(sql);

    }
}