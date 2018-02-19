package remote;

import dataTransfer.Equipment;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public final class StorageImpl extends UnicastRemoteObject implements Storage {

    /**
     * General success signal.
     */
    public static final int OK = 0;

    /**
     * Entity not found.
     */
    public static final int NOT_FOUND = -1;

    /**
     * Not enough amount of a particular entity stored.
     */
    public static final int NOT_ENOUGH_STORED = -2;

    private static final Map<Integer, Equipment> registry = new HashMap<>();
    private static int equipmentId = 0;

    /**
     * Construct a registry for equipment.
     */
    public StorageImpl() throws RemoteException {
        super();
    }

    /**
     * Register new equipment that is stored.
     *
     * @param name       A name for the equipment.
     * @param supplier   The name of the supplier of this equipment.
     * @param amount     The amount of this item stored.
     * @param lowerBound
     * @return true if the equipment was successfully registered, false otherwise.
     */
    @Override
    public synchronized boolean registerNewEquipment(String name, String supplier, int amount,
                                        int lowerBound) {
        if (findEquipment(name) == null) {
            Equipment equipment = new Equipment(equipmentId, name, supplier, amount, lowerBound);
            registry.putIfAbsent(equipmentId++, equipment);
            return true;
        }
        return false;
    }

    /**
     * Change the amount of a particular equipment stored.
     *
     * @param id         The id of the equipment.
     * @param difference The amount of equipment removed or added to storage.
     * @return 0 if ok, -1 if equipment with given id was not found and
     * -2 if the difference given is too high.
     */
    @Override
    public synchronized int alterSupply(int id, int difference) {
        Equipment equipment = findEquipment(id);
        if (equipment == null) {
            return NOT_FOUND;
        }
        if (equipment.changeAmount(difference)) {
            return OK;
        }
        return NOT_ENOUGH_STORED;
    }

    /**
     * Change the amount of a particular equipment stored.
     *
     * @param name          The name of the equipment.
     * @param difference    The amount of equipment removed or added to storage.
     * @return 0 if ok, -1 if equipment with given id was not found and
     * -2 if the difference given is too high.
     */
    public synchronized int alterSupply(String name, int difference) {
        Equipment equipment = findEquipment(name);
        if (equipment == null) {
            return NOT_FOUND;
        }
        if (equipment.changeAmount(difference)) {
            return OK;
        }
        return NOT_ENOUGH_STORED;
    }

    private Equipment findEquipment(int id) {
        return registry.get(id);
    }

    private Equipment findEquipment(String name) {
        for (Equipment equipment: registry.values()) {
            if (equipment.getName().equalsIgnoreCase(name)) {
                return equipment;
            }
        }
        return null;
    }

    /**
     * Get a list of all equipment with selective data.
     *
     * @return a string formatted as a list.
     */
    @Override
    public synchronized String getOrders() {
        StringBuilder sb = new StringBuilder("\n\nOrder list:\n");
        for (Equipment equipment : registry.values()) {
            sb.append(equipment.getId());
            sb.append(", ");
            sb.append(equipment.getName());
            sb.append(": ");
            sb.append(equipment.getOrderQuantity());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Get a list of all equipment with all data.
     *
     * @return a string formatted as a list.
     */
    @Override
    public synchronized String getData() {
        StringBuilder sb = new StringBuilder("All data:\n");
        for (Equipment equipment : registry.values()) {
            sb.append(equipment.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
