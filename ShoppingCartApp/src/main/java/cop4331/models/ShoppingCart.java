package cop4331.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author S Hassan Shaikh
 */
public class ShoppingCart implements Serializable{
    public Item GetItem(int index)
    {return items.get(index);}
    
    public int GetPurchaseAmount(int index)
    {return amountPurchasing.get(index);}
    
    public int ContainsItem(Item item)
    {return items.indexOf(item);}
    
    public void SetPurchaseAmount(int index, int amount)
    {
        if (amount>0)
            amountPurchasing.set(index, amount);
        else
            RemoveItem(index);
    }
    
    public void RemoveItem(int index)
    {
        items.remove(index);
        amountPurchasing.remove(index);
    }
    
    public void ClearCart()
    {
        items.clear();
        amountPurchasing.clear();
    }
    
    public void AddItem(Item newItem)
    {
        items.add(newItem);
        amountPurchasing.add(1);
    }
    
    public void AddItem(Item newItem, int amount)
    {
        items.add(newItem);
        amountPurchasing.add(amount);
    }
    
    public int GetSize()
    {return items.size();}
    
    public float GetTotal()
    {
        float total = 0;
        int cartSize = GetSize();
        for(int i=0; i<cartSize; i++)
        {total += (GetItem(i).GetPrice()*amountPurchasing.get(i));}
        return total;
    }
    
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Integer> amountPurchasing = new ArrayList<>();
}
