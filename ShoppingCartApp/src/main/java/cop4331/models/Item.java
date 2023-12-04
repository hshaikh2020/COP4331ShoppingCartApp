package cop4331.models;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents an item available for sale within the system.
 * Implements Serializable to enable serialization of Item objects.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class Item implements Serializable {
    /**
     * Constructor for creating an Item object with specific details.
     * Initializes the item with provided attributes.
     * @param name The name of the item.
     * @param description The description of the item.
     * @param price The price of the item.
     * @param specsName The list of specification names for the item.
     * @param specs The list of specifications for the item.
     * @param soldBy The seller who sells this item.
     */
    public Item(String name, String description, float price, ArrayList<String> specsName, ArrayList<String> specs, Seller soldBy)
    {
        // Initialization of item attributes
        this.name = name;
        this.description = description;
        this.price = price;
        this.specsName = specsName;
        this.specs = specs;
        this.soldBy = soldBy;
        amountInStock = 0;
        available = false;
    }
    
    // Getter methods for item attributes
    /**
     * Retrieves the name of the item.
     * @return The name of the item as a String.
     */
    public String GetName() {
        return name;
    }

    /**
     * Retrieves the description of the item.
     * @return The description of the item as a String.
     */
    public String GetDescription() {
        return description;
    }

    /**
     * Retrieves the price of the item.
     * @return The price of the item as a float value.
     */
    public float GetPrice() {
        return price;
    }

    /**
     * Retrieves the amount of the item in stock.
     * @return The amount of the item in stock as an integer.
     */
    public int GetStock() {
        return amountInStock;
    }

    /**
     * Retrieves the size of the specifications list.
     * @return The size of the specifications list as an integer.
     */
    public int GetSpecSize() {
        return specs.size();
    }

    /**
     * Retrieves the availability status of the item.
     * @return The availability status of the item as a boolean.
     */
    public boolean GetAvailability() {
        return available;
    }

    /**
     * Retrieves the seller of the item.
     * @return The Seller object representing the seller of the item.
     */
    public Seller GetSeller() {
        return soldBy;
    }

    
   /**
    * Retrieves the specifications for an item at a given index.
    * @param index The index of the specifications to retrieve.
    * @return An array containing the specification name and details at the given index.
    */
   public String[] GetSpec(int index) {
       String specName = specsName.get(index);
       String spec = specs.get(index);
       String[] specification = {specName, spec};
       return specification;
   }

    /**
     * Changes the stock amount of the item by a specified amount.
     * @param amount The amount to be added to the current stock.
     * @throws Exception If an invalid amount is provided (e.g., negative amount).
     */
    public void ChangeStock(int amount) throws Exception {
        SetStock(amountInStock + amount);
    }

    /**
     * Sets the stock amount of the item to a specified value.
     * @param amount The new stock amount to be set.
     * @throws Exception If an invalid amount (negative) is attempted to set.
     */
    public void SetStock(int amount) throws Exception {
        if (amount < 0)
            throw new Exception();
        amountInStock = amount;
        if (amount == 0)
            available = false;
    }

    /**
     * Sets the availability status of the item.
     * @param available The new availability status to be set.
     * @throws Exception If the item's stock is 0 and an attempt is made to set it as available.
     */
    public void SetAvailability(boolean available) throws Exception {
        if (amountInStock == 0 && available == true)
            throw new Exception();
        this.available = available;
    }

    /**
     * Sets the name of the item.
     * @param name The new name for the item.
     */
    public void SetName(String name)
    {this.name=name;}
    
    // Other setter methods for description, price, stock, etc.
    /**
     * Sets the description of the item.
     * @param description The new description to be set.
     */
    public void SetDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the price of the item.
     * @param price The new price to be set.
     */
    public void SetPrice(float price) {
        this.price = price;
    }

    /**
     * Sets the specifications of the item.
     * @param specs The new specifications to be set (as an ArrayList of strings).
     */
    public void SetSpecs(ArrayList<String> specs) {
        this.specs = specs;
    }

    /**
     * Sets the specification names of the item.
     * @param specsName The new specification names to be set (as an ArrayList of strings).
     */
    public void SetSpecNames(ArrayList<String> specsName) {
        this.specsName = specsName;
    }

    /**
     * Sets the seller of the item.
     * @param soldBy The Seller object representing the new seller to be set.
     */
    public void SetSeller(Seller soldBy) {
        this.soldBy = soldBy;
    }
    
    /**
     * Checks equality between two Item objects based on their attributes.
     * @param other The object to compare for equality.
     * @return True if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other)
    {
        // Check for equality based on attributes
        if(other == null||getClass() != other.getClass())
            return false;
        Item otherItem = (Item) other;
        return(this.hashCode() == otherItem.hashCode());
    }
    
    /**
     * Generates a hash code for the Item object.
     * @return The generated hash code for the Item object.
     */
    @Override
    public int hashCode() {
        // Generate a hash code based on attributes
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Float.floatToIntBits(this.price);
        hash = 97 * hash + Objects.hashCode(this.specs);
        hash = 97 * hash + Objects.hashCode(this.specsName);
        return hash;
    }
    
    /** The name of the item. */
    private String name;
    /** The description of the item. */
    private String description;
    /** The price of the item. */
    private float price;
    /** The amount of the item currently in stock. */
    private int amountInStock;
    /** The availability status of the item.. */
    private boolean available;
    /** The seller of the item. */
    private transient Seller soldBy;
    /** A list of specs for the item. */
    private ArrayList<String> specs;
    /** The associated names for the list of specs. */
    private ArrayList<String> specsName;
}
