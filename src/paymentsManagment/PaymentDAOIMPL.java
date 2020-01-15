package paymentsManagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import salesManagment.Sale;
import salesManagment.SaleDAOIMPL;
import shared.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAOIMPL implements MagazineDAO<Payment> {
    SaleDAOIMPL saleDAOIMPL = new SaleDAOIMPL();
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Payment find(long id) {
        try {
            String sql = "select * from payments where id='" + id + "'";
            statement = connection.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    return new Payment(resultSet.getLong("id"), resultSet.getLong("num"),
                            resultSet.getDouble("amount"), resultSet.getDate("date").toLocalDate(),
                            resultSet.getString("type"), resultSet.getString("state"),
                            resultSet.getString("check_number"), saleDAOIMPL.find(resultSet.getLong("sale_id")));
                }
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }

    @Override
    public boolean create(Payment p) {
        String sql = String.format("INSERT INTO `payments` (`sale_id`, `num`, `amount`, `date`, `type`,`state`,`check_number`) " +
                        "VALUES ('%d', '%d', '%s', '%s', '%s', '%s', '%s')",
                p.getSale().getId(), p.getNum(), p.getAmount(), p.getDate(), p.getType(), p.getState(), p.getChecknumber());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating payment failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    p.setId(generatedKeys.getLong(1));
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
    public boolean delete(Payment p) {

        try {
            String sql = "delete from payments where id=" + p.getId();
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Errors in delete");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Payment p1, Payment p2) {
        String sql = String.format("UPDATE `payments` SET `amount` = '%f' ,`date`= '%s' ,`type`= '%s' ,`state`= '%s',`check_number`= '%s' " +
                        "WHERE `payments`.`id` =%d",
                p2.getAmount(), p2.getDate(), p2.getType(), p2.getState(), p2.getChecknumber(), p1.getId());

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("Errors in update");
            return false;
        }
        p1.update(p2);
        return true;

    }

    @Override
    public List<Payment> findAll() {
        String sql = "select * from payments";
        return getResult(sql);
    }

    @Override
    public List<Payment> findAll(String key) {
        return null;
    }

    private ArrayList<Payment> getResult(String sql) {
        ArrayList<Payment> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Payment(resultSet.getLong("id"), resultSet.getLong("num"),
                        resultSet.getDouble("amount"), resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("type"), resultSet.getString("state"),
                        resultSet.getString("check_number"), saleDAOIMPL.find(resultSet.getLong("sale_id"))));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;
    }

    public ObservableList<Payment> getAll() {
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public ObservableList<Payment> getAll(Sale sale) {
        try {
            return FXCollections.observableArrayList(findAll(sale));
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public ObservableList<Payment> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public ArrayList<Payment> findAll(Sale sale) {
        try {
            String sql = "select * from payments WHERE sale_id = " + sale.getId() + "";
            System.out.println(sql);
            return getResult(sql);
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public double getTotalPayed(Sale sale) {
        try {
            return findAll(sale).stream().mapToDouble(Payment::getAmount).sum();
        } catch (NullPointerException ignored) {
            return 0;
        }
    }
}
