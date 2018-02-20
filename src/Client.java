import remote.Storage;
import remote.StorageImpl;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Client {
    private static String host = "localhost";
    private static int port = -1;

    public static void main(String[] args) {
        processArgs(args);

        System.out.println("Using hostname: '" + host + "'");
        if (port != -1) System.out.println("Using port: '" + port + "'");

        try {
            Registry registry;
            if (port != -1) {
                registry = LocateRegistry.getRegistry(host, port);
            } else {
                registry = LocateRegistry.getRegistry(host);
            }
            Storage storage = (Storage) registry.lookup("Storage");

            String input = "";
            while (!Pattern.matches("^[eE]xit|e|E|5$", input)) {
                input = displayMenu(storage);
            }
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        switch (args.length) {
            case 1:
                port = getUnsignedInt(args[0]);
                if (port == -1) {
                    host = args[0];
                }
                break;
            case 2:
                port = getUnsignedInt(args[0]);
                if (port == -1) {
                    host = args[0];
                    port = getUnsignedInt(args[1]);
                } else {
                    host = args[1];
                }
                break;
            default:
                break;
        }
    }

    private static int getUnsignedInt(String input) {
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group());
        }
        return -1;
    }

    private static String displayMenu(Storage storage) {
        System.out.println("\nSelect one of the following options:\n" +
                "1: Register new equipment.\n" +
                "2: Withdraw or deposit equipment.\n" +
                "3: Display data.\n" +
                "4: Display orders.\n" +
                "5: Exit");
        System.out.print("Option: ");
        String input = System.console().readLine();
        int option = getUnsignedInt(input);
        switch (option) {
            case 1:
                registerEquipment(storage);
                break;
            case 2:
                alterSupply(storage);
                break;
            case 3:
                displayData(storage);
                break;
            case 4:
                displayOrders(storage);
                break;
            default:
                break;
        }
        return input;
    }

    private static void registerEquipment(Storage storage) {
        System.out.println("\nRegister equipment by name, supplier, amount stored and lower bound:");
        do {
            System.out.print("Equipment: ");
            String input = System.console().readLine();
            String[] values = input.split(",");

            String name = "";
            String supplier = "";
            int amount = 0;
            int lowerBound = 0;

            switch (values.length) {
                case 4:
                    lowerBound = getUnsignedInt(values[3].trim());
                case 3:
                    amount = getUnsignedInt(values[2].trim());
                case 2:
                    supplier = values[1].trim();
                case 1:
                    name = values[0].trim();
                default:
            }

            System.out.println("\nRegister equipment with the following values?\n" +
                    "Name:\t\t" + name + "\n" +
                    "Supplier:\t" + supplier + "\n" +
                    "Amount:\t\t" + ((amount != -1) ? amount : 0) + "\n" +
                    "Lower bound:\t" + ((lowerBound != -1) ? lowerBound : 0));
            System.out.print("Register (y/n): ");
            input = System.console().readLine().trim();
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                try {
                    if (storage.registerNewEquipment(name, supplier, amount, lowerBound)) {
                        return;
                    }
                    System.out.println("Failed to register equipment.");
                } catch (RemoteException re) {
                    re.printStackTrace();
                }
            }
        } while (true);
    }

    private static void alterSupply(Storage storage) {
        System.out.println("\nSelect equipment:");
        do {
            try {
                System.out.println("\n" + storage.getData());
                System.out.print("Equipment id: ");
                String input = System.console().readLine();
                int id = getUnsignedInt(input.trim());

                System.out.println("\nDeposit or Withdraw from storage?");
                System.out.print("Action (d/w): ");
                input = System.console().readLine();
                boolean isWithdrawing = Pattern.matches("[wW]", input);

                System.out.println("\nEnter the amount to " + (isWithdrawing ? "withdraw." : "deposit."));
                System.out.print("Amount: ");
                input = System.console().readLine();
                int amount = getUnsignedInt(input.trim());

                System.out.println("\n" + (isWithdrawing ? "Withdraw" : "Deposit") + " " + amount + " of equipment " + id);
                System.out.print("Preform action (y/n): ");
                input = System.console().readLine().trim();
                if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes")) {
                    int statusCode = storage.alterSupply(id, (isWithdrawing ? amount * -1 : amount));
                    switch (statusCode) {
                        case StorageImpl.OK:
                            return;
                        case StorageImpl.NOT_FOUND:
                            System.out.println("No equipment found with id '" + id + "'.");
                            break;
                        case StorageImpl.NOT_ENOUGH_STORED:
                            System.out.println("Cannot withdraw more than stored equipment.");
                            break;
                        default:
                            break;
                    }
                }
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        } while (true);
    }

    private static void displayData(Storage storage) {
        try {
            System.out.println("\n" + storage.getData());
        } catch (RemoteException re) {
            System.out.println("Failed to fetch data.");
        }
    }

    private static void displayOrders(Storage storage) {
        try {
            System.out.println("\n" + storage.getOrders());
        } catch (RemoteException re) {
            System.out.println("Failed to fetch orders.");
        }
    }
}
