import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.HashMap;

public class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private Account currentAccount;
    private Scanner scanner = new Scanner(System.in);
    private int accountCounter = 1000;
    
    
    private LocalDate readValidDate() {
        while (true) {
            System.out.print("| Enter your dob (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            
            try {
                // Try to parse the date
                LocalDate date = LocalDate.parse(input);
                
                // Additional validation: check if date is reasonable
                LocalDate today = LocalDate.now();
                LocalDate minDate = today.minusYears(120); // No one is older than 120
                LocalDate maxDate = today.minusYears(18);  // Must be at least 18
                
                if (date.isBefore(minDate)) {
                    System.out.println("Invalid date: Too far in the past.");
                    continue;
                }
                if (date.isAfter(maxDate)) {
                    System.out.println("You must be at least 18 years old.");
                    continue;
                }
                if (date.isAfter(today)) {
                    System.out.println("Date cannot be in the future.");
                    continue;
                }
                
                return date;  // Valid date!
                
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD (e.g., 2000-12-31)");
            }
        }
    }
    

    public static void main(String[] args) {
        new Bank().run();
    }
    
    public void run() {
    	System.out.println("=== Git test: Bank App starting ===");

        while (true) {
            System.out.println("\n#--------------------------------#");
            System.out.println("|       Welcome to Bank          |");
            System.out.println("| Make a selection:              |");
            System.out.println("|  1. Login                      |");
            System.out.println("|  2. Open new account           |");
            System.out.println("|  Your selection:               |");
            System.out.println("#--------------------------------#\"");
            int input = readInt();

            if (input == 1) {
                if (login()) sessionMenu();
            } else if (input == 2) {
                openAccount();
                sessionMenu();
            } else {
                System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private boolean login() {
        for (int attempts = 0; attempts < 3; attempts++) {
            System.out.println("\n#--------------------------------#");
            System.out.println("|            Login               |");
            System.out.print("| Enter account number: ");
            String acctNum = scanner.nextLine().trim();
            System.out.print("| Enter your pin: ");
            String pin = scanner.nextLine().trim();

            Account acct = findAccount(acctNum);
            if (acct != null && acct.getPin().equals(pin)) {
                currentAccount = acct;
                System.out.println("Login successful.");
                return true;
            } else {
                System.out.printf("Invalid credentials. %d attempt(s) left.%n", 2 - attempts);
            }
        }
        System.out.println("Too many failed attempts. Exiting.");
        System.exit(0);
        return false;
    }

    private void openAccount() {
        System.out.println("\n#--------------------------------#");
        System.out.println("|        New Account             |");
        System.out.print("| Enter your firstname: ");
        String firstName = scanner.nextLine().trim();
        System.out.print("| Enter your lastname: ");
        String lastName = scanner.nextLine().trim();
        System.out.print("| Enter your dob (YYYY-MM-DD): ");
        String dobStr = scanner.nextLine().trim();
        System.out.print("| Enter your ssn: ");
        String ssn = scanner.nextLine().trim();
        System.out.print("| Enter your email: ");
        String email = scanner.nextLine().trim();
        System.out.print("| Enter your tel: ");
        String tel = scanner.nextLine().trim();
        System.out.print("| Enter your address: ");
        String address = scanner.nextLine().trim();
        System.out.print("| Enter your pin: ");
        String pin = scanner.nextLine().trim();
        System.out.print("| Initial deposit: $");
        double initialDeposit = readDouble();

        Date dob = Date.valueOf(dobStr); // Converts YYYY-MM-DD
        String acctNum = generateAccountNumber(firstName, lastName, dobStr);
        User user = new User(firstName, lastName, dob, ssn, address, email, tel);
        Account acct = new Account(user, acctNum, pin, 0.0);
        acct.deposit(initialDeposit);

        accounts.put(acctNum, acct);
        currentAccount = acct;
        System.out.println("Account created! Your account number is " + acctNum);
    }

    private void sessionMenu() {
        while (currentAccount != null) {
            System.out.println("\n#--------------------------------#");
            System.out.println("|             Menu               |");
            System.out.println("| 1. View balance                |");
            System.out.println("| 2. Deposit                     |");
            System.out.println("| 3. Withdraw                    |");
            System.out.println("| 4. Withdraw all & close acct   |");
            System.out.println("| 5. Logout                      |");
            System.out.print("| Your selection: ");
            int choice = readInt();

            switch (choice) {
                case 1: viewBalance(); break;
                case 2: deposit(); break;
                case 3: withdraw(); break;
                case 4: closeAccount(); break;
                case 5: logout(); break;
                default: System.out.println("Invalid selection.");
            }
        }
    }

    private void viewBalance() {
        System.out.printf("Current balance: $%.2f%n", currentAccount.getBalance());
    }

    private void deposit() {
        System.out.print("Amount to deposit: $");
        double amt = readDouble();
        if (currentAccount.deposit(amt)) {
            System.out.printf("Deposited $%.2f with 0.2%% interest.%n", amt);
        } else {
            System.out.println("Deposit failed. Enter a valid amount.");
        }
    }

    private void withdraw() {
        System.out.print("Amount to withdraw: $");
        double amt = readDouble();
        if (currentAccount.withdraw(amt)) {
            System.out.printf("Withdrew $%.2f with 2%% fee.%n", amt);
        } else {
            System.out.println("Withdrawal failed. Check your balance or amount.");
        }
    }

    private void closeAccount() {
        double bal = currentAccount.getBalance();
        if (bal > 0) {
            double fee = bal * 0.02;
            double payout = bal - fee;
            System.out.printf("Final payout: $%.2f (2%% fee = $%.2f).%n", payout, fee);
        }
        System.out.println("Closing account " + currentAccount.getAccountNumber());
        accounts.remove(currentAccount.getAccountNumber());
        currentAccount = null;
    }

    private void logout() {
        System.out.println("Logged out.");
        currentAccount = null;
    }

    private Account findAccount(String acctNum) {
    	return accounts.get(acctNum);
    }

    private String generateAccountNumber(String fn, String ln, String dob) {
        String L = ln.toUpperCase().substring(0, Math.min(2, ln.length()));
        String F = fn.toUpperCase().substring(0, Math.min(2, fn.length()));
        String year = dob.length() >= 4 ? dob.substring(0, 4) : "0000";
        String accountNum = L + F + year + "-" + String.format("%-4d", accountCounter);
        accountCounter++;
        
        return accountNum;
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }
}

//import java.util.Date;
/*@SuppressWarnings("deprecation")
public static void main(String[] args) {
	User me = new User("josh", "davidowitz", new Date(2000,11,30), "1234152", "addy", "JoshD@gmail.com", "917-123-4567");
	System.out.println(me);*/