package bank;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BankServer {
    public static final int port = 6666;
    public static final String domain = "127.0.0.1";
    AccountDAOIMPL accountDAOIMPL;
    ServerSocket serverSocket;
    Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    Object o;

    public BankServer() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Waiting for connections");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        BankServer bankServer = new BankServer();
        while (true) {
            bankServer.acceptConnections();
            boolean valid = bankServer.getRequest();
            bankServer.sendResponse(valid);
        }

    }

    void acceptConnections() {
        try {
            socket = serverSocket.accept();
            System.out.println("Got one");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getRequest() {
        boolean valid = true;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            o = inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }

    private void sendResponse(boolean valid) {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            accountDAOIMPL = new AccountDAOIMPL();
            if (!valid)
                outputStream.writeObject("Error");
            else {
                accountDAOIMPL = new AccountDAOIMPL();
                if (o instanceof Transaction) {
                    boolean response = accountDAOIMPL.draw((Transaction) o);
                    outputStream.writeObject(response);
                } else if (o instanceof Account) {
                    o = accountDAOIMPL.find(((Account) o).getNumber());
                    outputStream.writeObject(o);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
