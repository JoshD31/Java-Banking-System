
public class Account {
    private User user;
    private String accountNumber;
    private String pin;
    private double balance;

    public Account(User user, String accountNumber, String pin, double initialDeposit) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialDeposit;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) return false;
        double interest = amount * 0.002;
        balance += amount + interest;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) return false;
        double fee = amount * 0.02;
        if (balance >= amount + fee) {
            balance -= amount + fee;
            return true;
        }
        return false;
    }

    public User getUser() { return user; }
    public String getAccountNumber() { return accountNumber; }
    public String getPin() { return pin; }
    public double getBalance() { return balance; }

    public void setPin(String pin) { this.pin = pin; }
    public void setBalance(double balance) { this.balance = balance; }
}



/*
public class Account {
	private User user;
    private String accountNumber;
    private String pin;
    private double balance;

    public Account(User user, String accountNumber, String pin, double initialDeposit) {
        this.user =user;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialDeposit;
    }

    public void deposit(double amount) {
        double interest = amount * 0.002;
        balance += amount + interest;
        System.out.printf("Deposited $%.2f with $%.2f interest.%n", amount, interest);
    }

    public void withdraw(double amount) {
        double charge = amount * 0.02;
        if (balance >= amount + charge) {
            balance -= (amount + charge);
            System.out.printf("Withdrew $%.2f with $%.2f fee.%n", amount, charge);
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
    public String getPin() { return pin; }
	public User getUser() {
		return user;
	}


}
*/