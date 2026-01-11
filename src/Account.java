import java.math.BigDecimal;       
import java.math.RoundingMode;    

public class Account {
    private User user;
    private String accountNumber;
    private String pin;
    private BigDecimal balance;

    public Account(User user, String accountNumber, String pin, BigDecimal initialDeposit) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialDeposit;
    }

    public boolean deposit (BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        
        BigDecimal interestRate = new BigDecimal("0.002");
        BigDecimal interest = amount.multiply(interestRate).setScale(2, RoundingMode.HALF_UP);
        
        balance = balance.add(amount).add(interest);
        return true;
    }


    public boolean withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) return false;
        
        BigDecimal feeRate = new BigDecimal("0.02");
        BigDecimal fee = amount.multiply(feeRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalNeeded = amount.add(fee);
        
        if (balance.compareTo(totalNeeded) >= 0) {
            balance = balance.subtract(totalNeeded);
            return true;
        }
        return false;
    }

    public User getUser() { return user; }
    public String getAccountNumber() { return accountNumber; }
    public String getPin() { return pin; }
    public BigDecimal getBalance() { return balance; }

    public void setPin(String pin) { this.pin = pin; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}