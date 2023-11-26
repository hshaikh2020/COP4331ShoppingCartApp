package cop4331.models;

import cop4331.gui.ErrorView;
import java.util.ArrayList;

/**
 *
 * @author S Hassan Shaikh
 */
public class Customer extends User{
    public Customer(String username, String password, String name) {
        super(username, password, name, "Customer");
    }
    
    public ArrayList<String[]> CompleteTransaction() throws Exception
    {
        RemoveFunds(customerCart.GetTotal());
        ArrayList<String[]> Receipt = new ArrayList<String[]>();
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
    
    public final ShoppingCart customerCart = new ShoppingCart();
}
