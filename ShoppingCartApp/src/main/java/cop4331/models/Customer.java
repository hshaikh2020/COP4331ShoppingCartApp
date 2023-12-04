package cop4331.models;

import cop4331.gui.ErrorView;
import java.util.ArrayList;

/**
 * Represents a customer user within the system, extending the User class.
 * Manages transactions and the customer's shopping cart.
 * Provides functionality to complete transactions and handle cart operations.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class Customer extends User{
    /**
     * Constructor for creating a Customer object with username, password, and name.
     * Initializes the user with a role of "Customer".
     * @param username The username for the customer.
     * @param password The password for the customer.
     * @param name The name of the customer.
     */
    public Customer(String username, String password, String name) {
        super(username, password, name, "Customer");
    }
    
    /**
     * Completes a transaction, updating stock, funds, and generating a receipt.
     * Removes funds from the customer's account based on the cart's total.
     * Processes items in the cart, updates stock, and generates a receipt.
     * @return An ArrayList containing String arrays representing transaction information.
     * @throws Exception If an error occurs during the transaction process.
     */
    public ArrayList<String[]> CompleteTransaction() throws Exception
    {
        RemoveFunds(customerCart.GetTotal());
        ArrayList<String[]> Receipt = new ArrayList<String[]>();
        // Process items in the cart
        for(int i=0;i<customerCart.GetSize();i++)
        {
            Item indexedItem = customerCart.GetItem(i);
            try{
                float itemPurchasePriceTotal = customerCart.GetPurchaseAmount(i)*indexedItem.GetPrice();
                indexedItem.ChangeStock(customerCart.GetPurchaseAmount(i)*-1);
                indexedItem.GetSeller().AddFunds(itemPurchasePriceTotal);
                String[] InfoArray = 
                {
                    indexedItem.GetName(),
                    String.valueOf(indexedItem.GetPrice()),
                    String.valueOf(customerCart.GetPurchaseAmount(i))
                };
                Receipt.add(InfoArray);
            }catch(Exception e)
            {ErrorView.GetInstance().ShowError("An error occured with purchasing one or more item(s).");}
        }
        customerCart.ClearCart();
        return Receipt;
    }
    
    /** The customer's shopping cart containing items for purchase. */
    public final ShoppingCart customerCart = new ShoppingCart();
}
