package bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import magazineDAO.MagazineDAO;
import shared.DataConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountDAOIMPL implements MagazineDAO<Account> {
    TransactionDAOIMPL transactionDAOIMPL = null;
    private Connection connection = DataConnection.getDataConnection().getConnection();
    private Statement statement;

    @Override
    public Account find(long id) {
        String sql = String.format("select * from accounts where id='%d'", id);
        return find_one(sql);
    }

    public Account find(String account_number) {
        String sql = String.format("select * from accounts where number='%s'", account_number);
        return find_one(sql);
    }

    private Account find_one(String sql) {
        try {
            statement = connection.createStatement();
            try {
                ResultSet resultSet = statement.executeQuery(sql);
                if (resultSet.next()) {
                    return new Account(resultSet.getLong("id"), resultSet.getString("number"),
                            resultSet.getFloat("balance"));
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
    public boolean create(Account a) {
        String sql = String.format("INSERT INTO `accounts` (`number`, `balance`) VALUES ('%s', '%f')",
                a.getNumber(), a.getBalance());
        try {
            PreparedStatement statement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating Account failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    a.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Creating Account failed, no ID obtained.");
                }
            }
        } catch (Exception e) {
            System.out.println("Errors in Account create");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Account a) {
        try {
            String sql = String.format("delete from accounts where id=%d", a.getId());
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (Exception e) {
            System.out.println("Errors in Account delete");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Account a1, Account a2) {
        System.out.println("updateezzzed" + a1 + a2);
        String sql = String.format("UPDATE `accounts` SET `balance`= '%s' WHERE `accounts`.`id` =%d", a2.getBalance(), a1.getId());

        try {
            statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.toString());
            System.out.println("Errors in Account update");
            return false;
        }
//        a1.setBalance();
        a1.update(a2);
        System.out.println("updateezzzed" + a1 + a2);
        return true;
    }

    @Override
    public List<Account> findAll() {
        String sql = "select * from Accounts";
        return getResult(sql);
    }

    @Override
    public List<Account> findAll(String key) {
        return null;
    }

    private ArrayList<Account> getResult(String sql) {
        ArrayList<Account> list = new ArrayList<>();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Account(resultSet.getLong("id"), resultSet.getString("number"),
                        resultSet.getFloat("balance")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (list.size() == 0)
            return null;
        return list;
    }

    public ObservableList<Account> getAll() {
        try {
            return FXCollections.observableArrayList(findAll());
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public ObservableList<Account> getAll(String s) {
        try {
            return FXCollections.observableArrayList(findAll(s));
        } catch (NullPointerException ignored) {
        }
        return null;
    }

    public boolean draw(Transaction t) {
        System.out.println(t.getAccount().getBalance() + " - " + t.getAccount().getBalance());
        Account a = t.getAccount();
        float v = t.getAccount().getBalance();
        float new_balance = (float) (a.getBalance() - t.getAmount());
        if (new_balance >= 0) {
            if (update(a, new Account(0, "", new_balance))) {
                transactionDAOIMPL = new TransactionDAOIMPL();
                if (transactionDAOIMPL.create(new Transaction(0, t.getAccount().getBalance(), LocalDate.now(), t.getAccount())))
                    return true;
                else {
                    update(t.getAccount(), new Account(t.getAccount().getId(), t.getAccount().getNumber(), (float) (new_balance + t.getAmount())));
                    return false;
                }
            }
        }
        return false;
    }
}
