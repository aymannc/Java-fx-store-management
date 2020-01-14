package bank;

import magazineDAO.MagazineDAO;

import java.util.List;

public class TransactionDAOIMPL implements MagazineDAO<Transaction> {
    @Override
    public Transaction find(long id) {
        return null;
    }

    @Override
    public boolean create(Transaction p) {
        System.out.println(p);
        return true;
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
