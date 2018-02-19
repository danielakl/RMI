package remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 */
public interface Storage extends Remote {
    /**
     * Register new equipment that is stored.
     * @param name          A name for the equipment.
     * @param supplier      The name of the supplier of this equipment.
     * @param amount        The amount of this item stored.
     * @param lowerBound
     * @return true if the equipment was successfully registered, false otherwise.
     */
    boolean registerNewEquipment(String name, String supplier, int amount, int lowerBound) throws RemoteException;

    /**
     * Change the amount of a particular equipment stored.
     * @param id            The id of the equipment.
     * @param difference    The amount of equipment removed or added to storage.
     * @return 0 if ok, -1 if equipment with given id was not found and
     * -2 if the difference given is too high.
     */
    int alterSupply(int id, int difference) throws RemoteException;

    /**
     * Get a list of all equipment with selective data.
     * @return a string formatted as a list.
     */
    String getOrders() throws RemoteException;

    /**
     * Get a list of all equipment with all data.
     * @return a string formatted as a list.
     */
    String getData() throws RemoteException;
}
