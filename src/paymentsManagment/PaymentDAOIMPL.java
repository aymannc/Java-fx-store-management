package paymentsManagment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import salesManagment.Sale;
import salesManagment.SaleDAOIMPL;
import shared.DataConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                            resultSet.getDouble("amount"), resultSet.getTimestamp("date"),
                            resultSet.getString("type"), saleDAOIMPL.find(resultSet.getLong("sale_id")));
                }
            } catch (SQLException e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public boolean create(Payment p) {
        try {
            String sql = String.format("INSERT INTO `payments` (`id`,`sale_id`, `num`, `amount`, `date`, `type`) VALUES ('%d','%d', '%d', '%s', '%s', '%s')", p.getId(), p.getSale().getId(), p.getNum(), p.getAmount(), p.getDate(), p.getType());
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Errors in create");
            System.out.println(e);
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
            System.out.println(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Payment p1, Payment p2) {
        String sql = "UPDATE `payments` SET `id` = '" + p2.getId() + "',`sale_id` = '" + p2.getSale().getId() + "',`amount` = '" +
                p2.getAmount() + "' ,`date`= '" + p2.getDate() + "' ,`type`= '" + p2.getType() + "' WHERE `payments`.`id` =" + p1.getId();

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e);
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
        ArrayList<Payment> list = new ArrayList();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Payment(resultSet.getLong("id"), resultSet.getLong("num"),
                        resultSet.getDouble("amount"), resultSet.getTimestamp("date"),
                        resultSet.getString("type"), saleDAOIMPL.find(resultSet.getLong("sale_id"))));
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
        } catch (NullPointerException e) {
        }
        return null;
    }

    public ObservableList<Payment> getAll(Sale sale) {
        try {
            return FXCollections.observableArrayList(findAll(sale));
        } catch (NullPointerException e) {
        }
        return null;
    }

    public ObservableList<Payment> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException e) {
        }
        return null;
    }

    public ArrayList<Payment> findAll(Sale sale) {
        try {
            String sql = "select * from payments WHERE sale_id = " + sale.getId() + "";
            System.out.println(sql);
            return getResult(sql);
        } catch (NullPointerException e) {
        }
        return null;
    }
}
