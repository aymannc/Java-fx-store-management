package salesManagment;

import clientsManagement.ClientDAOIMPL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleDAOIMPL implements MagazineDAO<Sale> {
    public static final String[] paymentTypes = new String[]{"Carte bancaire", "Espèces", "Chèque", "Traite"};
    ClientDAOIMPL clientDAOIMPL = new ClientDAOIMPL();
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Sale find(long id) {
        String sql = String.format("select * from sales where id='%d'", id);
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                return new Sale(resultSet.getLong("id"), clientDAOIMPL.find(resultSet.getLong("client_id")),
                        resultSet.getDouble("total"), resultSet.getTimestamp("date_added"),
                        resultSet.getTimestamp("date_modified"), resultSet.getTimestamp("date_deleted"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean create(Sale sale) {
        String sql = String.format("INSERT INTO `sales` (`total`,`client_id`) VALUES ('%.2f','%d')",
                sale.getTotal(),
                sale.getClient().getId());

        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating SALE failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    sale.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating SALE failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Errors in create" + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Sale sale) {
        String sql = "delete from sales where id=" + sale.getId();
        // delete payments and delete saleitems

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Errors in delete" + e.toString());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Sale sale1, Sale sale2) {
        String sql;

        sql = String.format("UPDATE `sales` SET `client_id` = '%d',`total` = '%.2f' WHERE `sales`.`id` =%d",
                sale2.getClient().getId(),
                sale2.getTotal(),
                sale1.getId());

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Errors in update" + e.toString());
            return false;
        }
        sale1.update(sale2);
        return true;

    }

    private List<Sale> getResult(String sql) {
        List<Sale> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Sale s = new Sale(resultSet.getLong("id"), clientDAOIMPL.find(resultSet.getLong("client_id")),
                        resultSet.getDouble("total"), resultSet.getTimestamp("date_added"),
                        resultSet.getTimestamp("date_modified"), resultSet.getTimestamp("date_deleted"));
                list.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;

    }

    public ObservableList<Sale> getAll() {
        return FXCollections.observableArrayList(findAll());
    }

    public ObservableList<Sale> getAll(String s) {
        return FXCollections.observableArrayList(findAll(s));
    }

    @Override
    public List<Sale> findAll() {
        String sql = "select * from sales";
        return getResult(sql);
    }

    @Override
    public List<Sale> findAll(String key) {
        // TODO search by client name
        String sql = "select * from sales WHERE id LIKE '%" + key + "%'";
        return getResult(sql);

    }

    public boolean updateTotal(double subTotal, Sale sale, boolean delete) {
        double total = delete ? sale.getTotal() - subTotal : subTotal + sale.getTotal();
        System.out.println("deleted " + total);
        String sql = String.format("UPDATE `sales` SET `total` = '%.2f' WHERE `sales`.`id` =%d",
                total,
                sale.getId());
        System.out.println(sql);
        try {
            statement = connection.createStatement();
            statement.execute(sql);
            sale.setTotal(total);
        } catch (SQLException e) {
            System.out.println("Errors in update" + e.toString());
            return false;
        }
        return true;
    }
}
