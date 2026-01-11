import java.math.BigDecimal;       
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class Account {
    private User user;
    private String accountNumber;
    private String hashedPin;
    private BigDecimal balance;

    public Account(User user, String accountNumber, String pin, BigDecimal initialDeposit) {
        this.user = user;
        this.accountNumber = accountNumber;
        this.hashedPin = hashPin(pin);
        this.balance = initialDeposit;
    }
    
    private String hashPin(String pin) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(pin.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash PIN", e);
        }
    }
    
    public boolean verifyPin(String pin) {
        return this.hashedPin.equals(hashPin(pin));
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
    public BigDecimal getBalance() { return balance; }

    public void setPin(String newPin) { this.hashedPin = hashPin(newPin); }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}