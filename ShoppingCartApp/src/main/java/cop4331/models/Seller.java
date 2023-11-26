package cop4331.models;

import java.util.ArrayList;

/**
 *
 * @author S Hassan Shaikh
 */
public class Seller extends User {
    public Seller(String username, String password, String name) {
        super(username, password, name, "Seller");
        inventory = new ArrayList<Item>();
    }
    
    public void SellItem(Item product)
    {
        inventory.add(product);
    }
    
    public void SellItem(Item product, int stock)
    {
        try {product.SetStock(stock);} catch (Exception ex) {}
        try {product.SetAvailability(true);}catch (Exception ex){}
        SellItem(product);
    }
    
    public void UpdateItem(Item updatedProduct, int index)
    {inventory.set(index, updatedProduct);}
    
    public int GetInventorySize()
    {return inventory.size();}
    
    public Item GetItemInInventory(int index)
    {return inventory.get(index);}
    
    public void RemoveFromSale(int index)
    {
        try{inventory.get(index).SetAvailability(false);}catch(Exception e){}
        inventory.remove(index);
    }
    
    public void ChangeStock(int index, int amount) throws Exception
    {GetItemInInventory(index).SetStock(amount);}
    
    public void ChangeAvailability(int index, boolean available) throws Exception
    {GetItemInInventory(index).SetAvailability(available);}
    
    private final ArrayList<Item> inventory;
}
