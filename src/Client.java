import remote.Storage;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public final class Client {
    public static void main(String[] args) {
//        if (System.getSecurityManager() == null) {
//            System.setSecurityManager(new SecurityManager());
//        }

        try {
            Registry registry = LocateRegistry.getRegistry(args[0]); // args[0] could be "rmi://localhost/"
            Storage storage = (Storage) registry.lookup("Storage");

            System.out.println("Register: " + storage.registerNewEquipment("Hammer", "Hencock & Huffler", 10, 30));
            System.out.println(storage.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
