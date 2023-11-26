package cop4331.models;

import java.io.Serializable;

/**
 *
 * @author S Hassan Shaikh
 */
public class User implements Serializable {
    public User(String username, String password, String name, String userType)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userType = userType;
    }
    
    public void ChangeUsername(String newUsername)
    {username = newUsername;}
    
    public void ChangePassword(String oldPassword, String newPassword) throws Exception
    {
        if(CheckPassword(oldPassword))
            password = newPassword;
        else
            throw new Exception();
    }
    
    public void AddFunds(float amount) throws Exception
    {
        if(amount>=0)
            balance += amount;
        else
            throw new Exception();
    }
    
    public void RemoveFunds(float amount) throws Exception
    {
        if(amount>=0&&amount<=balance)
            balance -= amount;
        else
            throw new Exception();
    }
    
    public void ChangeName(String newName)
    {name = newName;}
    
    public String GetName()
    {return name;}
    
    public String GetUsername()
    {return username;}
    
    public String GetUserType()
    {return userType;}
    
    public boolean CheckPassword(String input)
    {return password.equals(input);}
    
    public float GetBalance()
    {return balance;}
    
    private String username;
    private String password;
    private String name;
    private float balance;
    private final String userType;
}
