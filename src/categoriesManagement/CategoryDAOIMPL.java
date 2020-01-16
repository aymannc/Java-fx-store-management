package categoriesManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOIMPL implements MagazineDAO<Category> {
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Category find(long id) {
        String sql = "select * from categories where id='" + id + "'";
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return new Category(resultSet.getLong("id"), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean create(Category category) {
        String sql = String.format("INSERT INTO `categories` (`name`) VALUES ('%s');", category.getName());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getLong(1));
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
    public boolean delete(Category c) {
        String sql = "delete from categories where id=" + c.getId();
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }

        return true;

    }

    @Override
    public boolean update(Category c1, Category c2) {
        String sql = String.format("UPDATE `categories` SET `name` = '%s' WHERE `id` =%d", c2.getName(), c1.getId());

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Errors in update");
            return false;
        }
        c1.setName(c2.getName());
        return true;
    }

    public List<Category> findAll() {
        String sql = "select * from categories";
        return getResult(sql);
    }

    @Override
    public List<Category> findAll(String key) {
        String sql = "select * from categories WHERE name LIKE '%" + key + "%'";
        return getResult(sql);
    }

    private List<Category> getResult(String sql) {
        List<Category> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e);
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Category(resultSet.getLong("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        if (list.size() == 0)
            return null;
        return list;

    }

    public ObservableList<Category> getAll() {
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }
    }

    public ObservableList<Category> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }
    }
}
