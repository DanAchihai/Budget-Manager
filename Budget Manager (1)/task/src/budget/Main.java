package budget;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static String fileName
            = "C:\\Users\\Dan Achihai\\IdeaProjects\\Budget Manager\\Budget Manager\\task\\purchases1.txt";
    private static java.util.Scanner sc
            = new java.util.Scanner(System.in);
    private static java.util.List<Purchase> purchaseList
            = new java.util.ArrayList<Purchase>();
    private static double totalIncome = 0d;
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final String[] types
            = {"Food", "Clothes", "Entertainment", "Other", "All"};
    public static final String typesList
            = "\n1) " + types[0]
            + "\n2) " + types[1]
            + "\n3) " + types[2]
            + "\n4) " + types[3];
    private static final String[][] optionSelectionOrError =
            {   //0 -> Options of the menu and the error selection message
                {"Choose your action:"
                        + "\n1) Add income"
                        + "\n2) Add purchase"
                        + "\n3) Show list of purchases"
                        + "\n4) Balance"
                        + "\n5) Save"
                        + "\n6) Load"
                        + "\n7) Analyze (Sort)"
                        + "\n0) Exit"
                        , "Your action can be 0 to 7"}
                ,
                //1 -> Type of purchase to add options and the error selection message
                {"Choose the type of purchase" + typesList + "\n5) Back"
                        , "Your type of purchase to add option can be 1 to 4!\n" +
                        "If you don't want to add a puchase select the option  5"}
                    ,
                //2 -> Type of purchase to list options and the error selection message
                {"Choose the type of purchases" + typesList + "\n5) All" + "\n6) Back"
                        , "Your type of purchase to list option can be 1 to 5!\n" +
                        "If you don't want to list puchases select the option  6"}
                    ,
                //3 -> Type of analyze options and the error selection message
                {"How do you want to sort?\n" +
                        "1) Sort all purchases\n" +
                        "2) Sort by type\n" +
                        "3) Sort certain type\n" +
                        "4) Back"}
                    ,
                //4 -> Type of purchase to analyze and the error selection message
                {"Choose the type of purchase" + typesList
                        , "Your type of purchase to add option can be 1 to 4!\n"}
            };

    public static void main(String[] args) throws Exception {
        // write your code here
        int action = -1;
        while (action != 0) {
            action = optionSelection(0, 0, 7);
            switch (action) {
                case 1:
                    addIncome();
                    break;
                case 2:
                    addPurchase();
                    break;
                case 3:
                    showPuchaseList();
                    break;
                case 4:
                    showBalance();
                    break;
                case 5:
                    savePurchaseList();
                    break;
                case 6:
                    loadPurchaseList();
                    break;
                case 7:
                    analyze();
                    break;
                default:
            }

        }
        System.out.println("Bye!");
        sc.close();
    }

    private static void analyze() {
        int action = -1;
        while (action != 4) {
            action = optionSelection(3, 1, 4);
            List<Purchase> sorted = new ArrayList();
            switch (action) {
                case 1: // Sort all purchases
                    purchaseList
                            .stream()
                            .sorted(Comparator.reverseOrder())
                            .forEach(sorted::add);
                    printList("All", sorted);
                    break;
                case 2: // Sort by type
                    double[] typeExpense = {0d, 0d, 0d, 0d};
                    for (Purchase item: purchaseList) {
                        typeExpense[(int) (item.getType())] += item.getPrice();
                    }
                    for (int i = 0; i < 4; i++) {
                        sorted.add(new Purchase(4, types[i] + " -", typeExpense[i]));
                    }
                    Collections.sort(sorted, Comparator.reverseOrder());
                    printList("Types", sorted);

                    break;
                case 3: // Sort certain type
                    int type = optionSelection(4,1, 4);
                    purchaseList
                            .stream()
                            .filter(item -> item.getType() == type - 1)
                            .sorted(Comparator.reverseOrder())
                            .forEach(sorted::add);
                    printList(types[type - 1], sorted);
                    break;
                default:
            }
        }
    }

    private static void showPuchaseList() {
        int type = 0;
        while (type < 6) {
            if (type > 0) {
                if (type == 5) {
                    printList("All", purchaseList);
                } else {
                    final int TYPE = type - 1;
                    printList(types[TYPE],
                            purchaseList
                                    .stream()
                                    .filter(item -> item.getType() == TYPE)
                                    .collect(Collectors.toList()));
                }
            }
            type = optionSelection(2, 1, 6);
        }
    }

    private static void addPurchase() {
        int type = 0;
        while (type < 5) {
            if (type > 0) {
                System.out.println("Enter purchase name:");
                String name = sc.nextLine().trim();
                System.out.println("Enter its price:");
                purchaseList.add(new Purchase(type - 1
                        , name
                        , Double.parseDouble(df.format(sc.nextDouble()))));
                System.out.println("Purchase was added!");
                System.out.println();
            }
            type = optionSelection(1, 1, 5);
        }
    }

    private static int optionSelection(int optionsAndErrorIndex, int min, int max) {
        while (true) {
            System.out.println(optionSelectionOrError[optionsAndErrorIndex][0]);
            int action = sc.nextInt();
            sc.nextLine();
            if (action < min || action > max) {
                System.out.println("Error!\n"
                        + optionSelectionOrError[optionsAndErrorIndex][1]
                        + "!\nTry Again!");
            } else {
                System.out.println();
                return action;
            }
        }
    }

    private static void addIncome() {
        System.out.println("Enter income:");
        totalIncome += roundToTwoDecimals(sc.nextDouble());
        sc.nextLine();
        System.out.println("Income was added!");
        System.out.println();
    }

    private static void showBalance() {
        double totalExpense = 0d;
        for (Purchase item: purchaseList) {
            totalExpense += item.getPrice();
        }
        double balance = totalExpense > totalIncome
                ? 0
                : totalIncome - totalExpense;
        DecimalFormat df = new DecimalFormat("Balance: $0.00");
        System.out.println(df.format(balance));
        System.out.println();
    }

    private static void savePurchaseList() throws IOException {
        try (FileOutputStream fo = new FileOutputStream(fileName);
             BufferedOutputStream bo = new BufferedOutputStream(fo);
             ObjectOutputStream oo = new ObjectOutputStream(bo))
        {
            oo.writeDouble(totalIncome);
            oo.writeInt(purchaseList.size());
            for (Purchase item: purchaseList) {
                oo.writeObject(item);
            }
            System.out.println("Purchases were saved!");
            System.out.println();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void loadPurchaseList()
            throws IOException, ClassNotFoundException
    {
        try (FileInputStream fi = new FileInputStream(fileName);
             BufferedInputStream bi = new BufferedInputStream(fi);
             ObjectInput oi = new ObjectInputStream(bi))
        {
            totalIncome = oi.readDouble();
            int purchaseCounter = oi.readInt();
            for (int i = 0; i < purchaseCounter; i++) {
                purchaseList.add((Purchase) oi.readObject());
            }
            System.out.println("Purchases were loaded!");
            System.out.println();
        } catch (Exception e) {
            File fI = new java.io.File(fileName);
            System.out.println(e.getMessage());
        }
    }

    private static void printList(String listTitle, List<Purchase> list) {
        StringBuffer sb = new StringBuffer();
        if (list.size() == 0) {
            sb.append("The purchase list is empty!").append(System.lineSeparator());
        } else {
            sb.append(listTitle).append(":").append(System.lineSeparator());
            double listCost = 0.00d;
            for (Purchase item: list) {
                double price = item.getPrice();
                sb.append(item.getName()).append(" $").append(df.format(price)).append(System.lineSeparator());
                listCost += price;
            }
            sb.append("Total sum: $" + df.format(listCost)).append(System.lineSeparator());
        }
        System.out.println(sb);
    }

    private static double roundToTwoDecimals(double toBeRounded) {
        return Double.parseDouble(df.format(toBeRounded));
    }
}

class Purchase implements Serializable, Comparable<Purchase> {
    private int type;
    private String name;
    private double price;
    private static final long serialVersionUID = -6620383667604023943L;

    Purchase(int type, String name, double price) {
        this.type = type;
        this.name = name;
        this.price = price;
    }

    double getType() {
        return type;
    }

    String getName() {
        return name;
    }

    double getPrice() {
        return price;
    }

    @Override
    public int compareTo(Purchase anotherPurchase) {
        int compareResult = (int) Math.signum(this.price - anotherPurchase.getPrice());
        if (compareResult != 0) {
            return compareResult;
        }
        compareResult = name.compareTo(anotherPurchase.getName());
        if (compareResult != 0) {
            return compareResult;
        }
        return (int) (type - anotherPurchase.getType());
    }
}