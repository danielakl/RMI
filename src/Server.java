import remote.Storage;
import remote.StorageImpl;

import javax.swing.JOptionPane;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Server exposing all RMI remotes.
 */
public final class Server {
    public static void main(String[] args) {
        try {
            Storage storage = new StorageImpl();

            // Bind remote objects.
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("Storage", storage);

            // Print remote objects provided.
            String[] binds = registry.list();
            StringBuilder sb = new StringBuilder("Server starting with following bind" +
                    ((binds.length > 1) ? "s" : "") + ": ");
            for (int i = 0; i < binds.length; i++) {
                sb.append(binds[i]);
                if (i != binds.length - 1) sb.append(", ");
            }
            System.out.println(sb.toString());

            // Unbind remote objects.
            JOptionPane.showMessageDialog(null, "Press OK to stop the server.");
            for (String bind : binds) {
                registry.unbind(bind);
            }
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
