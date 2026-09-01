import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Stock {
    private String symbol;
    private String companyName;
    private double price;

    public Stock(String symbol, String companyName, double price) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void displayStock() {
        System.out.printf(
                "%-10s %-25s Rs. %.2f%n",
                symbol,
                companyName,
                price
        );
    }
}

class Transaction {
    private String type;
    private String stockSymbol;
    private int quantity;
    private double price;

    public Transaction(String type, String stockSymbol,
                       int quantity, double price) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotal() {
        return quantity * price;
    }

    @Override
    public String toString() {
        return type +
                " | Stock: " + stockSymbol +
                " | Quantity: " + quantity +
                " | Price: Rs. " +
                String.format("%.2f", price) +
                " | Total: Rs. " +
                String.format("%.2f", getTotal());
    }
}

class Portfolio {

    private HashMap<String, Integer> holdings;
    private ArrayList<Transaction> transactions;
    private double cashBalance;

    public Portfolio(double initialBalance) {
        cashBalance = initialBalance;
        holdings = new HashMap<>();
        transactions = new ArrayList<>();
    }

    public void buyStock(Stock stock, int quantity) {

        if (quantity <= 0) {
            System.out.println(
                    "Quantity must be greater than 0.");
            return;
        }

        double totalCost =
                stock.getPrice() * quantity;

        if (totalCost > cashBalance) {

            System.out.println(
                    "Insufficient balance.");

            System.out.printf(
                    "Required: Rs. %.2f%n",
                    totalCost);

            System.out.printf(
                    "Available: Rs. %.2f%n",
                    cashBalance);

            return;
        }

        cashBalance -= totalCost;

        int currentQuantity =
                holdings.getOrDefault(
                        stock.getSymbol(), 0);

        holdings.put(
                stock.getSymbol(),
                currentQuantity + quantity
        );

        Transaction transaction =
                new Transaction(
                        "BUY",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                );

        transactions.add(transaction);

        System.out.println(
                "\nStock purchased successfully!");

        System.out.println(
                "Stock    : " + stock.getSymbol());

        System.out.println(
                "Quantity : " + quantity);

        System.out.printf(
                "Price    : Rs. %.2f%n",
                stock.getPrice());

        System.out.printf(
                "Total    : Rs. %.2f%n",
                totalCost);
    }

    public void sellStock(Stock stock, int quantity) {

        if (quantity <= 0) {

            System.out.println(
                    "Quantity must be greater than 0.");

            return;
        }

        int ownedQuantity =
                holdings.getOrDefault(
                        stock.getSymbol(), 0);

        if (ownedQuantity < quantity) {

            System.out.println(
                    "You do not own enough shares.");

            System.out.println(
                    "Owned shares: " + ownedQuantity);

            return;
        }

        double totalAmount =
                stock.getPrice() * quantity;

        cashBalance += totalAmount;

        int remainingQuantity =
                ownedQuantity - quantity;

        if (remainingQuantity == 0) {

            holdings.remove(
                    stock.getSymbol());

        } else {

            holdings.put(
                    stock.getSymbol(),
                    remainingQuantity);
        }

        Transaction transaction =
                new Transaction(
                        "SELL",
                        stock.getSymbol(),
                        quantity,
                        stock.getPrice()
                );

        transactions.add(transaction);

        System.out.println(
                "\nStock sold successfully!");

        System.out.println(
                "Stock    : " + stock.getSymbol());

        System.out.println(
                "Quantity : " + quantity);

        System.out.printf(
                "Price    : Rs. %.2f%n",
                stock.getPrice());

        System.out.printf(
                "Total    : Rs. %.2f%n",
                totalAmount);
    }

    public void displayPortfolio(
            HashMap<String, Stock> market) {

        System.out.println(
                "\n========== MY PORTFOLIO ==========");

        if (holdings.isEmpty()) {

            System.out.println(
                    "No stocks in your portfolio.");

            System.out.printf(
                    "Cash Balance: Rs. %.2f%n",
                    cashBalance);

            return;
        }

        System.out.printf(
                "%-10s %-12s %-15s %-15s%n",
                "Symbol",
                "Quantity",
                "Price",
                "Value"
        );

        System.out.println(
                "--------------------------------------------------");

        double totalStockValue = 0;

        for (Map.Entry<String, Integer> entry :
                holdings.entrySet()) {

            String symbol = entry.getKey();

            int quantity = entry.getValue();

            Stock stock = market.get(symbol);

            if (stock != null) {

                double value =
                        stock.getPrice() * quantity;

                totalStockValue += value;

                System.out.printf(
                        "%-10s %-12d Rs. %-11.2f Rs. %-11.2f%n",
                        symbol,
                        quantity,
                        stock.getPrice(),
                        value
                );
            }
        }

        double totalPortfolioValue =
                cashBalance + totalStockValue;

        System.out.println(
                "--------------------------------------------------");

        System.out.printf(
                "Total Stock Value : Rs. %.2f%n",
                totalStockValue);

        System.out.printf(
                "Cash Balance      : Rs. %.2f%n",
                cashBalance);

        System.out.printf(
                "Total Portfolio   : Rs. %.2f%n",
                totalPortfolioValue);
    }

    public void displayTransactions() {

        System.out.println(
                "\n========== TRANSACTION HISTORY ==========");

        if (transactions.isEmpty()) {

            System.out.println(
                    "No transactions found.");

            return;
        }

        for (Transaction transaction :
                transactions) {

            System.out.println(transaction);
        }
    }

    public void saveTransactions() {

        try {

            FileWriter writer =
                    new FileWriter(
                            "stock_transactions.txt");

            for (Transaction transaction :
                    transactions) {

                writer.write(
                        transaction.toString());

                writer.write(
                        System.lineSeparator());
            }

            writer.close();

            System.out.println(
                    "Transactions saved successfully.");

            System.out.println(
                    "File: stock_transactions.txt");

        } catch (IOException e) {

            System.out.println(
                    "Error while saving transactions.");
        }
    }
}

public class StockTradingPlatform {

    static Scanner scanner =
            new Scanner(System.in);

    static HashMap<String, Stock> market =
            new HashMap<>();

    public static void loadStocks() {

        market.put(
                "AAPL",
                new Stock(
                        "AAPL",
                        "Apple Inc.",
                        220.50
                )
        );

        market.put(
                "GOOG",
                new Stock(
                        "GOOG",
                        "Alphabet Inc.",
                        185.75
                )
        );

        market.put(
                "MSFT",
                new Stock(
                        "MSFT",
                        "Microsoft",
                        430.20
                )
        );

        market.put(
                "AMZN",
                new Stock(
                        "AMZN",
                        "Amazon",
                        195.40
                )
        );

        market.put(
                "TSLA",
                new Stock(
                        "TSLA",
                        "Tesla",
                        250.80
                )
        );

        market.put(
                "NVDA",
                new Stock(
                        "NVDA",
                        "NVIDIA",
                        140.60
                )
        );
    }

    public static void displayMarket() {

        System.out.println(
                "\n========== STOCK MARKET ==========");

        System.out.printf(
                "%-10s %-25s %s%n",
                "Symbol",
                "Company",
                "Price"
        );

        System.out.println(
                "--------------------------------------------------");

        for (Stock stock :
                market.values()) {

            stock.displayStock();
        }
    }

    public static void main(String[] args) {

        loadStocks();

        Portfolio portfolio =
                new Portfolio(100000);

        int choice;

        System.out.println(
                "==========================================");

        System.out.println(
                "       STOCK TRADING PLATFORM");

        System.out.println(
                "==========================================");

        do {

            System.out.println(
                    "\n========== MAIN MENU ==========");

            System.out.println(
                    "1. Display Market");

            System.out.println(
                    "2. Buy Stock");

            System.out.println(
                    "3. Sell Stock");

            System.out.println(
                    "4. View Portfolio");

            System.out.println(
                    "5. View Transactions");

            System.out.println(
                    "6. Save Transactions");

            System.out.println(
                    "7. Exit");

            System.out.print(
                    "Enter your choice: ");

            while (!scanner.hasNextInt()) {

                System.out.println(
                        "Please enter a valid number.");

                scanner.next();

                System.out.print(
                        "Enter your choice: ");
            }

            choice =
                    scanner.nextInt();

            switch (choice) {

                case 1:

                    displayMarket();

                    break;

                case 2:

                    displayMarket();

                    System.out.print(
                            "\nEnter stock symbol: ");

                    String buySymbol =
                            scanner.next().toUpperCase();

                    Stock buyStock =
                            market.get(buySymbol);

                    if (buyStock == null) {

                        System.out.println(
                                "Stock not found.");

                        break;
                    }

                    System.out.print(
                            "Enter quantity: ");

                    int buyQuantity =
                            scanner.nextInt();

                    portfolio.buyStock(
                            buyStock,
                            buyQuantity
                    );

                    break;

                case 3:

                    displayMarket();

                    System.out.print(
                            "\nEnter stock symbol: ");

                    String sellSymbol =
                            scanner.next().toUpperCase();

                    Stock sellStock =
                            market.get(sellSymbol);

                    if (sellStock == null) {

                        System.out.println(
                                "Stock not found.");

                        break;
                    }

                    System.out.print(
                            "Enter quantity: ");

                    int sellQuantity =
                            scanner.nextInt();

                    portfolio.sellStock(
                            sellStock,
                            sellQuantity
                    );

                    break;

                case 4:

                    portfolio.displayPortfolio(
                            market);

                    break;

                case 5:

                    portfolio.displayTransactions();

                    break;

                case 6:

                    portfolio.saveTransactions();

                    break;

                case 7:

                    System.out.println(
                            "\nThank you for using " +
                            "Stock Trading Platform!");

                    break;

                default:

                    System.out.println(
                            "Invalid choice. Try again.");
            }

        } while (choice != 7);

        scanner.close();
    }
}