import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.Consumer;

public class Client extends Thread {

    private Socket socketClient;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Consumer<Serializable> callback;
    String customerName, type;
    Client(Consumer<Serializable> call) {
        callback = call;
    }

    void setCustomerName(String customerName){
        this.customerName = customerName;
    }
    void setClientType(String type){
        this.type = type;
    }

    String getCustomerName(){
        return customerName;
    }
    String getClientType(){
        return type;
    }


    public void run() {
        try {
            socketClient = new Socket("127.0.0.1", 5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);


            // Send client type and ID to the server
            //out.writeObject("clientName:John");
//            send("clientName:John");
//            out.writeObject("yolo");
//            send("clientName:" + getCustomerName());
            send(getClientType() + getCustomerName());
            out.flush();

            while (true) {
                Serializable info = (Serializable) in.readObject();
                callback.accept(info);
                //System.out.println("Message from server: " + info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(Serializable data) {
        try {
            out.writeObject(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
