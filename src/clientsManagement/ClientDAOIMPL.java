package clientsManagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAOIMPL implements MagazineDAO<Client> {
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Client find(long id) {
        String sql = String.format("select * from clients where id='%d'", id);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return new Client(resultSet.getLong("id"), resultSet.getString("full_name"),
                        resultSet.getString("gender"), resultSet.getString("address"),
                        resultSet.getString("phone_number"), resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Client client) {
        String sql = String.format("INSERT INTO `clients` (`full_name`, `gender`, `address`, `phone_number`, `email`) VALUES ('%s', '%s', '%s', '%s', '%s')",
                client.getName(), client.getGender(), client.getAddress(), client.getPhone(), client.getEmail());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    client.setId(generatedKeys.getLong(1));
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
    public boolean delete(Client client) {
        String sql = String.format("delete from clients where id=%d", client.getId());
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean update(Client c1, Client c2) {
        String sql = String.format("UPDATE `clients` SET `full_name` = '%s', `gender` = '%s', `address` = '%s'," +
                        " `phone_number` = '%s', `email` = '%s' WHERE `clients`.`id` =%d",
                c2.getName(), c2.getGender(), c2.getAddress(), c2.getPhone(), c2.getEmail(), c1.getId());
        System.out.println(sql);
        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errors in update");
            return false;
        }
        c1.update(c2);

        return true;
    }

    @Override
    public List<Client> findAll() {
        String sql = "select * from clients";
        return getResult(sql);
    }

    @Override
    public List<Client> findAll(String key) {
        String sql = "select * from clients WHERE full_name LIKE '%" + key + "%'";
        return getResult(sql);
    }

    private List<Client> getResult(String sql) {
        List<Client> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Client(resultSet.getLong("id"), resultSet.getString("full_name"),
                        resultSet.getString("gender"), resultSet.getString("address"),
                        resultSet.getString("phone_number"), resultSet.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;

    }

    public ObservableList<Client> getAll() {
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }

    }

    public ObservableList<Client> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException e) {
            return FXCollections.observableArrayList(new ArrayList<>());
        }
    }
}
