package clientsManagement;

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

public class ClientDAOIMPL implements MagazineDAO<Client> {
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Client find(long id) {
        String sql = "select * from clients where id='" + id + "'";
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
        String sql = "INSERT INTO `clients` (`id`,`full_name`, `gender`, `address`, `phone_number`, `email`) VALUES" + " ('" + client.getId() + "','"
                + client.getName() + "', '" + client.getGender() + "', '" + client.getAddress() + "', '"
                + client.getPhone() + "', '" + client.getEmail() + "')";
        System.out.println(sql);
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
    public boolean delete(Client client) {
        String sql = "delete from clients where id=" + client.getId();
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
        String sql = "UPDATE `clients` SET `id` = '" + c2.getId() + "', `full_name` = '" + c2.getName() + "', " +
                "`gender` = '" + c2.getGender() + "', `address` = '" + c2.getAddress() + "', `phone_number` = '" +
                c2.getPhone() + "', `email` = '" + c2.getEmail() + "' WHERE `clients`.`id` =" + c1.getId();
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
        ObservableList<Client> clients = FXCollections.observableArrayList(findAll());
        return clients;
    }

    public ObservableList<Client> getAll(String s) {
        ObservableList<Client> clients = FXCollections.observableArrayList(findAll(s));
        return clients;
    }
}
