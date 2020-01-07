package categoriesManagement;

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
        String sql = "INSERT INTO `categories` (`id`,`name`) VALUES ('" + category.getId() + "','" + category.getName() + "');";
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
        String sql = "UPDATE `categories` SET `id` = '" + c2.getId() + "',`name` = '" + c2.getName() + "' WHERE `id` =" + c1.getId();

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Errors in update");
            return false;
        }
        c1.setId(c2.getId());
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
        return FXCollections.observableArrayList(findAll());
    }

    public ObservableList<Category> getAll(String s) {
        return FXCollections.observableArrayList(findAll(s));
    }
}
