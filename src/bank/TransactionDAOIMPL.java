package bank;

import magazineDAO.MagazineDAO;

import java.util.List;

public class TransactionDAOIMPL implements MagazineDAO<Transaction> {
    @Override
    public Transaction find(long id) {
        return null;
    }

    @Override
    public boolean create(Transaction t) {
        AccountDAOIMPL accountDAOIMPL = new AccountDAOIMPL();
        System.out.println(t.getAccount().getBalance() + " - " + t.getAmount());
        Account a = t.getAccount();
        float new_balance = (float) (a.getBalance() - t.getAmount());
        if (new_balance >= 0)
            return accountDAOIMPL.update(a, new Account(0, "", new_balance));
        return false;
    }

    @Override
    public boolean delete(Transaction p) {
        return false;
    }

    @Override
    public boolean update(Transaction p1, Transaction p2) {
        return false;
    }

    @Override
    public List<Transaction> findAll() {
        return null;
    }

    @Override
    public List<Transaction> findAll(String key) {
        return null;
    }
}
