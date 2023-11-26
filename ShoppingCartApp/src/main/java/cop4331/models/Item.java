package cop4331.models;

import cop4331.gui.ShoppingSystem;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * @author S Hassan Shaikh
 */
public class Item implements Serializable {
    public Item(String name, String description, float price, ArrayList<String> specsName, ArrayList<String> specs, Seller soldBy)
    {
        this.name = name;
        this.description = description;
        this.price = price;
        this.specsName = specsName;
        this.specs = specs;
        this.soldBy = soldBy;
        amountInStock = 0;
        available = false;
    }
    
    public String GetName()
    {return name;}
    
    public String GetDescription()
    {return description;}
    
    public float GetPrice()
    {return price;}
    
    public int GetStock()
    {return amountInStock;}
    
    public int GetSpecSize()
    {return specs.size();}
    
    public boolean GetAvailability()
    {return available;}
    
    public Seller GetSeller()
    {return soldBy;}
    
    public String[] GetSpec(int index)
    {
        String specName = specsName.get(index);
        String spec = specs.get(index);
        String[] specification = {specName, spec};
        return specification;
    }
    
    public void ChangeStock(int amount) throws Exception
    {SetStock(amountInStock+amount);}
    
    public void SetStock(int amount) throws Exception
    {
        if (amount<0)
            throw new Exception();
        amountInStock = amount;
        if (amount == 0)
            available = false;
    }
    
    public void SetAvailability(boolean available) throws Exception
    {
        if (amountInStock==0&&available==true)
            throw new Exception();
        this.available = available;
    }
    
    public void SetName(String name)
    {this.name=name;}
    
    public void SetDescription(String description)
    {this.description=description;}
    
    public void SetPrice(float price)
    {this.price=price;}
    
    public void SetSpecs(ArrayList<String> specs)
    {this.specs=specs;}
    
    public void SetSpecNames(ArrayList<String> specsName)
    {this.specsName=specsName;}
    
    public void SetSeller(Seller soldBy)
    {this.soldBy = soldBy;}
    
    @Override
    public boolean equals(Object other)
    {
        if(other == null||getClass() != other.getClass())
            return false;
        Item otherItem = (Item) other;
        return(this.hashCode() == otherItem.hashCode());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Float.floatToIntBits(this.price);
        hash = 97 * hash + Objects.hashCode(this.specs);
        hash = 97 * hash + Objects.hashCode(this.specsName);
        return hash;
    }
    
    private String name;
    private String description;
    private float price;
    private int amountInStock;
    private boolean available;
    private transient Seller soldBy;
    private ArrayList<String> specs;
    private ArrayList<String> specsName;
}
