import remote.Storage;
import remote.StorageImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class Server {
    public static void main(String[] args) {
        try {
            Storage storage = new StorageImpl();

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Storage", storage);

            javax.swing.JOptionPane.showMessageDialog(null, "Trykk OK for å stoppe tjeneren.");
            registry.unbind("Storage");
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
