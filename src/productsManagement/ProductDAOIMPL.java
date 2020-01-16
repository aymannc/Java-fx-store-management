package productsManagement;

import categoriesManagement.CategoryDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOIMPL implements MagazineDAO<Product> {
    CategoryDAOIMPL categoryDAOIMPL = new CategoryDAOIMPL();
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Product find(long id) {
        String sql = String.format("select * from products where id='%d'", id);
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
        String sql = String.format("INSERT INTO `products` (`name`, `price`,`category_id`) VALUES ('%s','%s', '%d')",
                p.getDesignation(), p.getPrice(), p.getCategory().getId());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setCode(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating payment failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            System.out.println("Errors in create");
//            System.out.println(e.toString());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Product p) {
        String sql = String.format("delete from products where id=%d", p.getCode());

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
        String sql = String.format("UPDATE `products` SET `name` = '%s',`price` = '%s' ,`category_id`= '%d' WHERE `products`.`id` =%d",
                p2.getDesignation(), p2.getPrice(), p2.getCategory().getId(), p1.getCode());

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
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }

    }

    public ObservableList<Product> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }
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