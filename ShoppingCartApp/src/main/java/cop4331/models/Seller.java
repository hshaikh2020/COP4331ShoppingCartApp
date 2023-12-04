package cop4331.models;

import java.util.ArrayList;

/**
 * Represents a seller within the system, extending the User class.
 * Manages inventory and selling of items.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class Seller extends User {
    /**
     * Constructor for creating a Seller object with username, password, and name.
     * Initializes the user with a role of "Seller" and an empty inventory.
     * @param username The username for the seller.
     * @param password The password for the seller.
     * @param name The name of the seller.
     */
    public Seller(String username, String password, String name) {
        
   
        super(username, password, name, "Seller");
        inventory = new ArrayList<Item>();
    }
    
    /**
     * Adds an item to the seller's inventory.
     * @param product The item to be added to the inventory.
     */
    public void SellItem(Item product)
    {
        inventory.add(product);
    }
    
    /**
     * Adds an item with a specified stock to the seller's inventory.
     * @param product The item to be added to the inventory.
     * @param stock The stock quantity for the item.
     */
    public void SellItem(Item product, int stock)
    {
        try {product.SetStock(stock);} catch (Exception ex) {}
        try {product.SetAvailability(true);}catch (Exception ex){}
        SellItem(product);
    }
    
    /**
     * Updates an item in the seller's inventory at a specific index.
     * @param updatedProduct The updated item to replace the existing one.
     * @param index The index of the item to be updated.
     */
    public void UpdateItem(Item updatedProduct, int index)
    {inventory.set(index, updatedProduct);}
    
    // Other methods for inventory management (size, retrieval, removal, etc.)
    /**
     * Retrieves the size of the inventory.
     * @return The size of the inventory as an integer.
     */
    public int GetInventorySize() {
        return inventory.size();
    }

    /**
     * Retrieves the item at the specified index in the inventory.
     * @param index The index of the item to retrieve from the inventory.
     * @return The Item object at the specified index in the inventory.
     */
    public Item GetItemInInventory(int index) {
        return inventory.get(index);
    }

    /**
     * Removes an item from the sale at the specified index.
     * @param index The index of the item to remove from sale.
     */
    public void RemoveFromSale(int index) {
        try {
            inventory.get(index).SetAvailability(false);
        } catch (Exception e) {
            // Exception handling if an error occurs when setting availability
        }
        inventory.remove(index);
    }

    /**
     * Changes the stock amount of the item at the specified index in the inventory.
     * @param index The index of the item in the inventory.
     * @param amount The new stock amount to be set.
     * @throws Exception If an invalid stock amount is attempted to set (e.g., negative amount).
     */
    public void ChangeStock(int index, int amount) throws Exception {
        GetItemInInventory(index).SetStock(amount);
    }

    /**
     * Changes the availability of the item at the specified index in the inventory.
     * @param index The index of the item in the inventory.
     * @param available The new availability status to be set.
     * @throws Exception If an invalid availability status is attempted to set based on stock quantity.
     */
    public void ChangeAvailability(int index, boolean available) throws Exception {
        GetItemInInventory(index).SetAvailability(available);
    }

    // Private attribute representing the inventory
    /** The inventory of the seller. */
    private final ArrayList<Item> inventory;
}

