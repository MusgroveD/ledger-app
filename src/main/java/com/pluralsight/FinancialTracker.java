package com.pluralsight;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * Capstone skeleton – personal finance tracker.
 * ------------------------------------------------
 * File format  (pipe-delimited)
 *     yyyy-MM-dd|HH:mm:ss|description|vendor|amount
 * A deposit has a positive amount; a payment is stored
 * as a negative amount.
 */
public class FinancialTracker {

    /* ------------------------------------------------------------------
       Shared data and formatters
       ------------------------------------------------------------------ */
    private static final ArrayList<Transaction> transactions = new ArrayList<>();
    private static final String FILE_NAME = "transactions.csv";

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATETIME_PATTERN = DATE_PATTERN + " " + TIME_PATTERN;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern(TIME_PATTERN);
    private static final DateTimeFormatter DATETIME_FMT = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    /* ------------------------------------------------------------------
       Main menu
       ------------------------------------------------------------------ */
    public static void main(String[] args) {
        loadTransactions(FILE_NAME);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D" -> addDeposit(scanner);
                case "P" -> addPayment(scanner);
                case "L" -> ledgerMenu(scanner);
                case "X" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
        scanner.close();
    }

    /* ------------------------------------------------------------------
       File I/O
       ------------------------------------------------------------------ */

    /**
     * Load transactions from FILE_NAME.
     * • If the file doesn’t exist, create an empty one so that future writes succeed.
     * • Each line looks like: date|time|description|vendor|amount
     */
    public static void loadTransactions(String fileName) {
        // TODO: create file if it does not exist, then read each line,
        //       parse the five fields, build a Transaction object,
        //       and add it to the transactions list.

        try {
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner fileScanner = new Scanner(file);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();

                String[] parts = line.split("\\|");
                if (parts.length != 5) {
                    continue;
                }

                String dateStr = parts[0];
                String timeStr = parts[1];
                String description = parts[2];
                String vendor = parts[3];
                String amountStr = parts[4];

                LocalDate date = LocalDate.parse(dateStr);
                LocalTime time = LocalTime.parse(timeStr);
                double amount = Double.parseDouble(amountStr);

                Transaction t = new Transaction(date, time, description, vendor, amount);
                transactions.add(t);
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading transactions: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------
       Add new transactions
       ------------------------------------------------------------------ */

    /**
     * Prompt for ONE date+time string in the format
     * "yyyy-MM-dd HH:mm:ss", plus description, vendor, amount.
     * Validate that the amount entered is positive.
     * Store the amount as-is (positive) and append to the file.
     */
    private static void addDeposit(Scanner scanner) {
        // TODO

        try {
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
            String dateTimeInput = scanner.nextLine();
            String[] parts = dateTimeInput.split(" ");
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime time = LocalTime.parse(parts[1]);

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);

            java.io.FileWriter writer = new java.io.FileWriter(FILE_NAME, true);
            writer.write(t.toString() + "\n");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error adding deposit: " + e.getMessage());
        }
    }

    /**
     * Same prompts as addDeposit.
     * Amount must be entered as a positive number,
     * then converted to a negative amount before storing.
     */
    private static void addPayment(Scanner scanner) {
        // TODO

        try {
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm:ss): ");
            String dateTimeInput = scanner.nextLine();
            String[] parts = dateTimeInput.split(" ");
            LocalDate date = LocalDate.parse(parts[0]);
            LocalTime time = LocalTime.parse(parts[1]);

            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            System.out.print("Enter vendor: ");
            String vendor = scanner.nextLine();

            System.out.print("Enter amount: ");
            double amount = Double.parseDouble(scanner.nextLine());
            if (amount <= 0) {
                System.out.println("Amount must be positive.");
                return;
            }

            amount = -amount;

            Transaction t = new Transaction(date, time, description, vendor, amount);
            transactions.add(t);

            java.io.FileWriter writer = new java.io.FileWriter(FILE_NAME, true);
            writer.write(t.toString() + "\n");
            writer.close();

        } catch (Exception e) {
            System.out.println("Error adding payment: " + e.getMessage());
        }
    }

    /* ------------------------------------------------------------------
       Ledger menu
       ------------------------------------------------------------------ */
    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A" -> displayLedger();
                case "D" -> displayDeposits();
                case "P" -> displayPayments();
                case "R" -> reportsMenu(scanner);
                case "H" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Display helpers: show data in neat columns
       ------------------------------------------------------------------ */
    private static void displayLedger() {
        /* TODO – print all transactions in column format */

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }

    private static void displayDeposits() {
        /* TODO – only amount > 0               */

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            if (t.getAmount() > 0) {
                System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                        t.getDate(),
                        t.getTime(),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
            }
        }
    }

    private static void displayPayments() {
        /* TODO – only amount < 0               */

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                        t.getDate(),
                        t.getTime(),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
            }
        }
    }

    /* ------------------------------------------------------------------
       Reports menu
       ------------------------------------------------------------------ */
    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("6) Custom Search");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    LocalDate now = LocalDate.now();
                    LocalDate start = now.withDayOfMonth(1);
                    LocalDate end = now;
                    filterTransactionsByDate(start, end);
                }
                case "2" -> {
                    LocalDate now = LocalDate.now();
                    LocalDate firstDayThisMonth = now.withDayOfMonth(1);
                    LocalDate lastDayPrevMonth = firstDayThisMonth.minusDays(1);
                    LocalDate firstDayPrevMonth = lastDayPrevMonth.withDayOfMonth(1);
                    filterTransactionsByDate(firstDayPrevMonth, lastDayPrevMonth);
                }
                case "3" -> {/* TODO – year-to-date report   */ }
                case "4" -> {/* TODO – previous year report  */ }
                case "5" -> {/* TODO – prompt for vendor then report */ }
                case "6" -> customSearch(scanner);
                case "0" -> running = false;
                default -> System.out.println("Invalid option");
            }
        }
    }

    /* ------------------------------------------------------------------
       Reporting helpers
       ------------------------------------------------------------------ */
    private static void filterTransactionsByDate(LocalDate start, LocalDate end) {
        // TODO – iterate transactions, print those within the range

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            if (!t.getDate().isBefore(start) && !t.getDate().isAfter(end)) {
                System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                        t.getDate(),
                        t.getTime(),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
            }
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
        // TODO – iterate transactions, print those with matching vendor

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            if (t.getVendor().equalsIgnoreCase(vendor)) {
                System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                        t.getDate(),
                        t.getTime(),
                        t.getDescription(),
                        t.getVendor(),
                        t.getAmount());
            }
        }
    }

    private static void customSearch(Scanner scanner) {
        // TODO – prompt for any combination of date range, description,
        //        vendor, and exact amount, then display matches

        System.out.print("Start date (yyyy-MM-dd) or blank: ");
        String startInput = scanner.nextLine();
        LocalDate start = startInput.isEmpty() ? null : LocalDate.parse(startInput);

        System.out.print("End date (yyyy-MM-dd) or blank: ");
        String endInput = scanner.nextLine();
        LocalDate end = endInput.isEmpty() ? null : LocalDate.parse(endInput);

        System.out.print("Description or blank: ");
        String description = scanner.nextLine();

        System.out.print("Vendor or blank: ");
        String vendor = scanner.nextLine();

        System.out.print("Exact amount or blank: ");
        String amountInput = scanner.nextLine();
        Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

        System.out.printf("%-12s %-10s %-20s %-20s %-10s%n",
                "Date", "Time", "Description", "Vendor", "Amount");

        for (Transaction t : transactions) {
            if (start != null && t.getDate().isBefore(start)) continue;
            if (end != null && t.getDate().isAfter(end)) continue;
            if (!description.isEmpty() && !t.getDescription().equalsIgnoreCase(description)) continue;
            if (!vendor.isEmpty() && !t.getVendor().equalsIgnoreCase(vendor)) continue;
            if (amount != null && t.getAmount() != amount) continue;

            System.out.printf("%-12s %-10s %-20s %-20s %-10.2f%n",
                    t.getDate(),
                    t.getTime(),
                    t.getDescription(),
                    t.getVendor(),
                    t.getAmount());
        }
    }

    /* ------------------------------------------------------------------
       Utility parsers (you can reuse in many places)
       ------------------------------------------------------------------ */
    private static LocalDate parseDate(String s) {
        /* TODO – return LocalDate or null */
        return null;
    }

    private static Double parseDouble(String s) {
        /* TODO – return Double   or null */
        return null;
    }
}
