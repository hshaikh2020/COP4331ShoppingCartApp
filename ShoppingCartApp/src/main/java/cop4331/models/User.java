package cop4331.models;

import java.io.Serializable;

/**test
 * Represents a user within the system.
 * Implements Serializable to enable serialization of User objects.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class User implements Serializable {
    /**
     * Constructor for creating a User object with username, password, name, and user type.
     * @param username The username for the user.
     * @param password The password for the user.
     * @param name The name of the user.
     * @param userType The type of user (e.g., "Seller", "Customer").
     */
    public User(String username, String password, String name, String userType)
    {
        // Initialization of user attributes
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }
    
    /**
     * Changes the username of the user.
     * @param newUsername The new username to set for the user.
     */
    public void ChangeUsername(String newUsername)
    {username = newUsername;}
    
    /**
     * Changes the user's password if the old password matches.
     * @param oldPassword The old password to be verified.
     * @param newPassword The new password to be set if the old password matches.
     * @throws Exception If the old password doesn't match, preventing the password change.
     */
    public void ChangePassword(String oldPassword, String newPassword) throws Exception {
        if (CheckPassword(oldPassword)) {
            password = newPassword;
        } else {
            throw new Exception();
        }
    }

    /**
     * Adds funds to the user's balance.
     * @param amount The amount of funds to be added.
     * @throws Exception If an invalid amount (negative) is attempted to add.
     */
    public void AddFunds(float amount) throws Exception {
        if (amount >= 0) {
            balance += amount;
        } else {
            throw new Exception();
        }
    }

    /**
     * Removes funds from the user's balance if sufficient funds are available.
     * @param amount The amount of funds to be removed.
     * @throws Exception If an invalid amount (negative or exceeding balance) is attempted to remove.
     */
    public void RemoveFunds(float amount) throws Exception {
        if (amount >= 0 && amount <= balance) {
            balance -= amount;
        } else {
            throw new Exception();
        }
    }

    /**
     * Changes the name associated with an object.
     * @param newName The new name to be assigned to the object.
     */
    public void ChangeName(String newName)
    {name = newName;}
    
    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String GetName()
    {return name;}
    
    // Other getter methods for username, user type, balance, etc.
    /**
     * Retrieves the username of the user.
     * @return The username of the user as a String.
     */
    public String GetUsername() {
        return username;
    }

    /**
     * Retrieves the type of user.
     * @return The type of user as a String.
     */
    public String GetUserType() {
        return userType;
    }

    /**
     * Checks if the provided input matches the user's password.
     *
     * @param input The input to check against the user's password.
     * @return True if the input matches the password, false otherwise.
     */
    public boolean CheckPassword(String input)
    {return password.equals(input);}
    
    /**
     * Retrieves the current balance of the user.
     * @return The balance of the user as a floating-point value.
     */
    public float GetBalance() {
        return balance;
    }
    
    /** The username of the user. */
    private String username;
    /** The password of the user. */
    private String password;
    /** The name of the user. */
    private String name;
    /** The balance of the user. */
    private float balance;
    /** The account type of the user ("Seller" or "Customer"). */
    private final String userType;
}
