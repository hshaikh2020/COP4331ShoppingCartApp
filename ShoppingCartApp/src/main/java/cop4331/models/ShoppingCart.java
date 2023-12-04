package cop4331.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents a shopping cart for items to be purchased.
 * Implements Serializable to enable serialization of ShoppingCart objects.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class ShoppingCart implements Serializable{
    /**
     * Default constructor for the shopping cart.
     */
    public ShoppingCart(){}
    
    /**
     * Retrieves the item at a specific index in the shopping cart.
     * @param index The index of the item to retrieve.
     * @return The item at the specified index in the shopping cart.
     */
    public Item GetItem(int index)
    {return items.get(index);}
    
    // Other getter and utility methods for shopping cart management
    /**
     * Retrieves the purchase amount of an item at a specified index in the cart.
     * @param index The index of the item in the cart.
     * @return The purchase amount of the item at the specified index.
     */
    public int GetPurchaseAmount(int index) {
        return amountPurchasing.get(index);
    }

    /**
     * Checks if the cart contains a specific item and returns its index.
     * @param item The item to check for in the cart.
     * @return The index of the item in the cart; returns -1 if the item is not present.
     */
    public int ContainsItem(Item item) {
        return items.indexOf(item);
    }

    /**
     * Sets the purchase amount for an item at a specified index in the cart.
     * @param index The index of the item in the cart.
     * @param amount The new purchase amount to be set.
     * If the amount is 0 or less, the item is removed from the cart.
     */
    public void SetPurchaseAmount(int index, int amount) {
        if (amount > 0)
            amountPurchasing.set(index, amount);
        else
            RemoveItem(index);
    }

    /**
     * Removes an item from the cart at the specified index.
     * @param index The index of the item to be removed from the cart.
     */
    public void RemoveItem(int index) {
        items.remove(index);
        amountPurchasing.remove(index);
    }

    /**
     * Clears the entire cart by removing all items and purchase amounts.
     */
    public void ClearCart() {
        items.clear();
        amountPurchasing.clear();
    }

    /**
     * Adds an item to the cart with a default purchase amount of 1.
     * @param newItem The item to be added to the cart.
     */
    public void AddItem(Item newItem) {
        items.add(newItem);
        amountPurchasing.add(1);
    }

    /**
     * Adds an item to the shopping cart with a specified purchase amount.
     * @param newItem The item to add to the shopping cart.
     * @param amount The purchase amount for the item.
     */
    public void AddItem(Item newItem, int amount)
    {
        items.add(newItem);
        amountPurchasing.add(amount);
    }
    
    /**
     * Gets the number of items in a shopping cart.
     * @return The size of the shopping cart.
     */
    public int GetSize()
    {return items.size();}
    
    // Other methods for managing items in the shopping cart
    /**
     * Retrieves the total price of items in the shopping cart.
     * @return The total price of items in the shopping cart.
     */
    public float GetTotal()
    // Calculates the total price of items in the cart
    {
        float total = 0;
        int cartSize = GetSize();
        for(int i=0; i<cartSize; i++)
        {total += (GetItem(i).GetPrice()*amountPurchasing.get(i));}
        return total;
    }
    
    /** The array representing the items in the shopping cart. */
    private final ArrayList<Item> items = new ArrayList<>();
    /** The array representing the amount of items being purchased for each item in the shopping cart. */
    private final ArrayList<Integer> amountPurchasing = new ArrayList<>();
}
