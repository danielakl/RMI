package dataTransfer;

import java.io.Serializable;

/**
 * Equipment
 */
public final class Equipment implements Serializable, Comparable {
    /**
     * Generic price multiplier.
     */
    public static final int ORDER_FACTOR = 5;

    private final int id;
    private String name;
    private String supplier;
    private int amount;
    private int lowerBound;

    /**
     * Constructor.
     * @param id            The id of the equipment.
     * @param name          The name of the equipment.
     * @param supplier      The name of the supplier for the equipment.
     * @param amount        The amount of this equipment stored.
     * @param lowerBound
     */
    public Equipment(int id, String name, String supplier, int amount, int lowerBound) {
        if (id < 0) {
            throw new IllegalArgumentException("The id of the equipment must be a positive integer.");
        }
        if (name == null) {
            throw new IllegalArgumentException("The equipment must be named.");
        }
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.amount = (amount < 0) ? 0 : amount;
        this.lowerBound = (lowerBound < 0) ? 0 : lowerBound;
    }

    /**
     * Get the id of the equipment.
     * @return an id as an integer.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the name of the equipment.
     * @return the name as a string.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the name of the supplier for the equipment.
     * @return name of the supplier as a string.
     */
    public String getSupplier() {
        return supplier;
    }

    /**
     * Get the amount stored.
     * @return the amount stored as an integer.
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Get the lower bound.
     * @return lower bound as an integer.
     */
    public int getLowerBound() {
        return lowerBound;
    }

    /**
     * Change the name of the equipment.
     * @param name the new name of the equipment.
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The equipment must be named.");
        }
        this.name = name;
    }

    /**
     * Change the name of the supplier of the equipment.
     * @param supplier the new name of the supplier.
     */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    /**
     * Change the lower bound of the equipment.
     * @param lowerBound the new lower bound.
     */
    public void setLowerBound(int lowerBound) {
        this.lowerBound = (lowerBound < 0) ? 0 : lowerBound;
    }

    /**
     *
     * @return
     */
    public int getOrderQuantity() {
        if (amount < lowerBound) {
            return ORDER_FACTOR * lowerBound;
        }
        return 0;
    }

    /**
     * Change the amount stored.
     * @param change the amount of equipment added (positive integer) or
     *               removed (negative integer).
     * @return true if the change is applied, false otherwise.
     */
    public boolean changeAmount(int change) {
        System.out.println("Changing stored amount, equipment id " + id + ", change: " + change);
        if (amount + change < 0) {
            return false;
        }
        amount += change;
        return true;
    }

    /**
     * Check the equality of another object to this one.
     * @param obj a object to compare to.
     * @return true if the object given is this object, have the same id or name. Otherwise false.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        if (obj instanceof Equipment) {
            Equipment other = (Equipment) obj;
            return (other.getId() == id || other.getName().equalsIgnoreCase(name));
        }
        return false;
    }

    /**
     * Creates a string of all the properties of this equipment object.
     * @return a string describing the equipment.
     */
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Supplier: " + supplier +
                ", Amount: " + amount + ", Lower bound: " + lowerBound;
    }

    /**
     * Compares the IDs of this object an another equipment.
     * @param obj the object to compare to.
     * @return an integer, zero, or a positive integer as this object is less than,
     * equal to, or greater that specified object.
     */
    @Override
    public int compareTo(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }

        if (obj instanceof Equipment) {
            return ((Equipment) obj).getId() - this.id;
        }

        throw new ClassCastException();
    }
}
