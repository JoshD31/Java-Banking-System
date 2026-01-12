import java.time.LocalDate;
import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.math.BigDecimal;      
import java.math.RoundingMode;     


public class Bank {
    private Map<String, Account> accounts = new HashMap<>();
    private Account currentAccount;
    private Scanner scanner = new Scanner(System.in);
    private int accountCounter = 1000;
    
    
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
            if (acct != null && acct.verifyPin(pin)) {
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
        String firstName = readValidFirstName();
        String lastName = readValidLastName();
        LocalDate dob = readValidDate();
        String ssn = readValidSSN();
        String email = readValidEmail();
        String tel = readValidPhone();
        String address = readValidAddress();
        String pin = readValidPin();
        double initialDepositInput = readValidDepositAmount();
        
        BigDecimal initialDeposit = new BigDecimal(String.valueOf(initialDepositInput));

        String acctNum = generateAccountNumber(firstName, lastName, dob.toString());
        User user = new User(firstName, lastName, dob, ssn, address, email, tel);
        Account acct = new Account(user, acctNum, pin, BigDecimal.ZERO);
        acct.deposit(initialDeposit);

        accounts.put(acctNum, acct);
        currentAccount = acct;
        System.out.println("Account created! Your account number is " + acctNum);
    }

    private void sessionMenu() {
        while (currentAccount != null) {
            System.out.println("\n#--------------------------------#");
            System.out.println("|             Menu               |");
            System.out.println("| Account: " + maskAccountNumber(currentAccount.getAccountNumber()) + "     |");
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
    private LocalDate readValidDate() {
        while (true) {
            System.out.print("| Enter your dob (YYYY-MM-DD): ");
            String input = scanner.nextLine().trim();
            
            String formattedDate = formatDateInput(input);
            
            if (formattedDate == null) {
                System.out.println("Invalid date format. Use YYYYMMDD or YYYY-MM-DD (e.g., 20001231 or 2000-12-31)");
                continue;
            }
            
            try {
                LocalDate date = LocalDate.parse(formattedDate);
                LocalDate today = LocalDate.now();
                
                // Check 1: Future date (check this FIRST)
                if (date.isAfter(today)) {
                    System.out.println("Date cannot be in the future.");
                    continue;
                }
                
                // Check 2: Too old (120+ years ago)
                LocalDate minDate = today.minusYears(120);
                if (date.isBefore(minDate)) {
                    System.out.println("Invalid date: Too far in the past.");
                    continue;
                }
                
                // Check 3: Too young (less than 18 years old)
                LocalDate minAgeDate = today.minusYears(18);
                if (date.isAfter(minAgeDate)) {
                    System.out.println("You must be at least 18 years old.");
                    continue;
                }
                
                return date;
                
            } catch (Exception e) {
                System.out.println("Invalid date. Please check the day/month values.");
            }
        }
    }

    // Helper method to format date input
    private String formatDateInput(String input) {
        // Remove all dashes, slashes, spaces, and dots
        String cleaned = input.replaceAll("[-/\\s.]", "");
        
        // Check if it's 8 digits (YYYYMMDD)
        if (cleaned.matches("^\\d{8}$")) {
            // Extract parts: YYYYMMDD
            String year = cleaned.substring(0, 4);
            String month = cleaned.substring(4, 6);
            String day = cleaned.substring(6, 8);
            return year + "-" + month + "-" + day;
        }
        
        // Check if input already has dashes (YYYY-MM-DD)
        if (input.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            return input;
        }
        
        // Invalid format
        return null;
    }
    

    private void viewBalance() {
        System.out.printf("Current balance: $%.2f%n", currentAccount.getBalance().setScale(2, RoundingMode.HALF_UP));
    }

    private void deposit() {
    	while (true) {
        System.out.print("Amount to deposit: $");
        double amtInput = readDouble();
        
        if (amtInput == -1) {
            System.out.println("Invalid input. Please enter a number.");
            continue;
        }
        
        if (amtInput <= 0) {
            System.out.println("Deposit failed. Enter a valid amount.");
            continue;
        }
        if (amtInput < 0.01) {
            System.out.println("Minimum deposit is $0.01");
            continue;
        }
        if (!InputValidator.isValidAmount(amtInput)) {
            System.out.println("Amount too large. Maximum is $1,000,000.");
            continue;
        }
        BigDecimal amt = new BigDecimal(String.valueOf(amtInput));
        
        if (currentAccount.deposit(amt)) {
            System.out.printf("Deposited $%s with 0.2%% interest.%n", amt.setScale(2, RoundingMode.HALF_UP));
            	return;
        } else {
            System.out.println("Deposit failed. Enter a valid amount.");
            continue;
        }
    }
}

    private void withdraw() {
    	while(true) {
    	System.out.print("Amount to withdraw: $");
        double amtInput = readDouble();
        
        if (amtInput == -1) {
            System.out.println("Invalid input. Please enter a number.");
            continue;
        }
        
        if (amtInput <= 0) {
            System.out.println("Withdrawal failed. Check your balance or amount.");
            continue;
        }
        
        if (amtInput < 0.01) {
            System.out.println("Minimum withdrawal is $0.01");
            continue;
        }
        
        BigDecimal amount = new BigDecimal(String.valueOf(amtInput));
        
        BigDecimal feeRate = new BigDecimal("0.02");
        BigDecimal fee = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalCharge = amount.add(fee);
        
        // Check if user has enough
        if (currentAccount.getBalance().compareTo(totalCharge) < 0) {
            System.out.printf("Insufficient funds. You need $%s (including $%s fee) but only have $%s.%n",
                             totalCharge.setScale(2, RoundingMode.HALF_UP),
                             fee.setScale(2, RoundingMode.HALF_UP),
                             currentAccount.getBalance().setScale(2, RoundingMode.HALF_UP));
            
            // Ask if they want to try again
            System.out.print("Try a different amount? (y/n): ");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (!choice.equals("y")) {
                return;  // Exit method
            }
            continue;  // Try again
        }
        
        // Sufficient funds - proceed
        if (currentAccount.withdraw(amount)) {
            System.out.printf("✓ Withdrew $%s with 2%% fee ($%s).%n", 
                             amount.setScale(2, RoundingMode.HALF_UP),
                             fee.setScale(2, RoundingMode.HALF_UP));
            return;  // Exit method
        } else {
            System.out.println("Withdrawal failed. Please try again.");
            continue;
        }
    }
}
    private void closeAccount() {
        BigDecimal bal = currentAccount.getBalance();
        if (bal.compareTo(BigDecimal.ZERO) > 0) {
        	
            BigDecimal feeRate = new BigDecimal("0.02");
            BigDecimal fee = bal.multiply(feeRate)
                               .setScale(2, RoundingMode.HALF_UP);
            BigDecimal payout = bal.subtract(fee);
            
            System.out.printf("Final payout: $%s (2%% fee = $%s).%n", 
                             payout.setScale(2, RoundingMode.HALF_UP),
                             fee.setScale(2, RoundingMode.HALF_UP));
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
 // Helper method to mask account number (show only last 4 characters)
    private String maskAccountNumber(String accountNumber) {
        if (accountNumber.length() <= 4) {
            return "****";
        }
        int visibleChars = 4;
        int maskedLength = accountNumber.length() - visibleChars;
        return "*".repeat(maskedLength) + accountNumber.substring(maskedLength);
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

    private String readValidFirstName() {
        while (true) {
            System.out.print("| Enter your firstname: ");
            String name = scanner.nextLine().trim();
            
            if (InputValidator.isValidName(name)) {
                return name;
            } else {
                System.out.println("Invalid name. Use only letters, spaces, hyphens (2-50 characters).");
            }
        }
    }

    private String readValidLastName() {
        while (true) {
            System.out.print("| Enter your lastname: ");
            String name = scanner.nextLine().trim();
            
            if (InputValidator.isValidName(name)) {
                return name;
            } else {
                System.out.println("Invalid name. Use only letters, spaces, hyphens (2-50 characters).");
            }
        }
    }

    private String readValidSSN() {
        while (true) {
            System.out.print("| Enter your ssn (9 digits): ");
            String ssn = scanner.nextLine().trim();
            
            String formatted = InputValidator.formatSSN(ssn);
            
            if (InputValidator.isValidSSN(formatted)) {
                return formatted;
            } else {
                System.out.println("Invalid SSN. Must be exactly 9 digits.");
            }
        }
    }

    private String readValidEmail() {
        while (true) {
            System.out.print("| Enter your email: ");
            String email = scanner.nextLine().trim();
            
            if (InputValidator.isValidEmail(email)) {
                return email;
            } else {
                System.out.println("Invalid email format (e.g., user@example.com).");
            }
        }
    }

    // Validate and read phone number
    private String readValidPhone() {
        while (true) {
            System.out.print("| Enter your phone # (XXX-XXX-XXXX): ");
            String phone = scanner.nextLine().trim();
            
            String formatted = InputValidator.formatPhoneNumber(phone);
            
            if (InputValidator.isValidPhone(formatted)) {
                return formatted;
            } else {
                System.out.println("Invalid phone. Use format: 123-456-7890 (or enter 10 digits).");
            }
        }
    }

    private String readValidAddress() {
        while (true) {
            System.out.print("| Enter your address: ");
            String address = scanner.nextLine().trim();
            
            if (InputValidator.isValidAddress(address)) {
                return address;
            } else {
                System.out.println("Address must be at least 5 characters.");
            }
        }
    }

    private String readValidPin() {
        while (true) {
            System.out.print("| Enter your pin (4-6 digits): ");
            String pin = scanner.nextLine().trim();
            
            if (InputValidator.isValidPin(pin)) {
                return pin;
            } else {
                System.out.println("Invalid PIN. Must be 4-6 digits only.");
            }
        }
    }

    private double readValidDepositAmount() {
        while (true) {
            System.out.print("| Initial deposit: $");
            double amount = readDouble();
            
            if (amount == -1) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }
            
            if (amount < 0) {
                System.out.println("Amount cannot be negative.");
                continue;
            }
            
            if (amount < 10) {
                System.out.println("Minimum deposit is $10.00");
                continue;
            }
            
            if (InputValidator.isValidAmount(amount)) {
                return amount;
            } else {
                System.out.println("Invalid amount. Must be between $10 and $1,000,000.");
            }
        }
    }
}
