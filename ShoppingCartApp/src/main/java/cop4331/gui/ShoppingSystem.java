package cop4331.gui;

import cop4331.models.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * The ShoppingSystem class manages the main functionality of the shopping application.
 * It handles user interactions, data storage, and screen transitions.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class ShoppingSystem {
    /**
     * Private default constructor for the ShoppingSystem singleton.
     */
    private ShoppingSystem(){}
    
    /**
     * Starts the shopping application by setting up the main view, loading data from files,
     * and displaying the login screen.
     */
    public void StartApp()
    {
        GetDataFromFiles();
        mainView.setTitle("Shopping Cart Application");
        mainView.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mainView.addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {
                try
                    (FileOutputStream writeUserData = new FileOutputStream("userData.txt");
                    ObjectOutputStream userWriter = new ObjectOutputStream(writeUserData))
                {
                    ArrayList<User> usersList = new ArrayList<User>();
                    for(int i=0;i<sellersList.size();i++)
                    {usersList.add((User) sellersList.get(i));}
                    for(int i=0;i<customersList.size();i++)
                    {usersList.add((User) customersList.get(i));}
                    userWriter.writeObject(usersList);
                    writeUserData.close();
                    userWriter.close();
                } catch (IOException ex) {System.out.println("Failed to save data.");}
                System.exit(0);
            }
            @Override
            public void windowClosed(WindowEvent e) {}
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        mainView.setVisible(true);
        mainView.add(container);
        OpenLoginScreen();
    }
    /**
     * Loads user data from files when the application starts.
     */
    private void GetDataFromFiles()
    {
        sellersList = new ArrayList<Seller>();
        customersList = new ArrayList<Customer>();
        try
            (FileInputStream userData = new FileInputStream("userData.txt");
            ObjectInputStream userReader = new ObjectInputStream(userData))
        {
            ArrayList<User> usersList = (ArrayList<User>) userReader.readObject();
            userReader.close();
            userData.close();
            for(int i=0;i<usersList.size();i++)
            {
                User tempUser = usersList.get(i);
                if(tempUser.GetUserType().equals("Seller"))
                {
                    Seller currentSeller = (Seller) tempUser;
                    sellersList.add(currentSeller);
                    for(int j=0;j<currentSeller.GetInventorySize();j++)
                    {currentSeller.GetItemInInventory(j).SetSeller(currentSeller);}
                }
                else if(tempUser.GetUserType().equals("Customer"))
                {customersList.add((Customer) tempUser);}
            }
        }
        catch(FileNotFoundException e)
        {
            File userDataFile = new File("userData.txt");
            System.out.println("File not found, created file "+userDataFile.getName());
        }
        catch(IOException | ClassNotFoundException e){}
    }
    
    /**
     * Resets the main view by removing its components.
     */
    private void ResetMainView()
    {
        container.removeAll();
        container.revalidate();
        container.repaint();
    }
    
    /**
     * Opens the login screen where users can log in to their accounts.
     */
    public void OpenLoginScreen()
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        // Username Field
        JPanel usernameContainer = new JPanel();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.LINE_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(10);
        usernameContainer.add(usernameLabel);
        usernameContainer.add(usernameTextField);
        container.add(usernameContainer);
        // Password Field
        JPanel passwordContainer = new JPanel();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.LINE_AXIS));
        JLabel passwordLabel = new JLabel("Password: ");
        JTextField passwordTextField = new JTextField(10);
        passwordContainer.add(passwordLabel);
        passwordContainer.add(passwordTextField);
        container.add(passwordContainer);
        // Buttons
        JButton registerInstead = new JButton("Don't have an account? Register here.");
        container.add(registerInstead);
        registerInstead.setAlignmentX(JButton.CENTER_ALIGNMENT);
        JButton loginButton = new JButton("Log in");
        container.add(loginButton);
        loginButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        loginButton.addActionListener((ActionEvent e)->{
            if(usernameTextField.getText().equals("")||passwordTextField.getText().equals(""))
            {errorView.ShowError("One or more of the fields are empty.");}
            else
            {
                try{
                    Seller foundAccount = GetSellerFromUsername(usernameTextField.getText());
                    if(foundAccount.CheckPassword(passwordTextField.getText()))
                    {
                        currentUser = foundAccount;
                        OpenInventoryScreen(foundAccount);
                    }else{errorView.ShowError("Incorrect credentials.");}
                }catch(NoSuchElementException ex1){
                    try{
                        Customer foundAccount = GetCustomerFromUsername(usernameTextField.getText());
                        if(foundAccount.CheckPassword(passwordTextField.getText()))
                        {
                            currentUser = foundAccount;
                            OpenShoppingScreen(foundAccount);
                        }else{errorView.ShowError("Incorrect credentials.");}
                    }catch(NoSuchElementException ex2)
                    {errorView.ShowError("Incorrect credentials.");}
                }
            }
        });
        // Adding an ActionListener to registerInstead button
registerInstead.addActionListener((ActionEvent e) -> {
    // Calling the method to open the register screen
    OpenRegisterScreen();
});

// Creating a KeyListener for login functionality
KeyListener loginOnEnter = new KeyListener() {
    @Override
    public void keyTyped(KeyEvent e) {
        // Not handling keyTyped event, leaving it empty
    }

    @Override
    public void keyPressed(KeyEvent theKey) {
        // Checking if the pressed key is the 'Enter' key
        if (theKey.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            // If 'Enter' is pressed, simulate a click on the loginButton
            loginButton.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not handling keyReleased event, leaving it empty
    }
};

// Adding the loginOnEnter KeyListener to the username and password text fields
usernameTextField.addKeyListener(loginOnEnter);
passwordTextField.addKeyListener(loginOnEnter);
        //mainView.add(container);
        mainView.pack();
    }
    
    /**
     * Opens the registration screen where new users can create accounts.
     */
    public void OpenRegisterScreen()
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        // Username Field
        JPanel usernameContainer = new JPanel();
        usernameContainer.setLayout(new BoxLayout(usernameContainer, BoxLayout.LINE_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(10);
        usernameContainer.add(usernameLabel);
        usernameContainer.add(usernameTextField);
        container.add(usernameContainer);
        // Password Field
        JPanel passwordContainer = new JPanel();
        passwordContainer.setLayout(new BoxLayout(passwordContainer, BoxLayout.LINE_AXIS));
        JLabel passwordLabel = new JLabel("Password: ");
        JTextField passwordTextField = new JTextField(10);
        passwordContainer.add(passwordLabel);
        passwordContainer.add(passwordTextField);
        container.add(passwordContainer);
        // Name Field
        JPanel nameContainer = new JPanel();
        nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.LINE_AXIS));
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(10);
        nameContainer.add(nameLabel);
        nameContainer.add(nameTextField);
        container.add(nameContainer);
        // Account Type
        JPanel selectionContainer = new JPanel();
        selectionContainer.setLayout(new BoxLayout(selectionContainer, BoxLayout.LINE_AXIS));
        ButtonGroup accountTypeSelection = new ButtonGroup();
        JRadioButton customerSelection = new JRadioButton("Customer");
        JRadioButton sellerSelection = new JRadioButton("Seller");
        selectionContainer.add(customerSelection);
        selectionContainer.add(sellerSelection);
        accountTypeSelection.add(customerSelection);
        accountTypeSelection.add(sellerSelection);
        container.add(selectionContainer);
        customerSelection.setSelected(true);
        // Buttons
        JButton loginInstead = new JButton("Already have an account? Log in here.");
        container.add(loginInstead);
        loginInstead.setAlignmentX(JButton.CENTER_ALIGNMENT);
        JButton registerButton = new JButton("Register");
        container.add(registerButton);
        registerButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        registerButton.addActionListener((ActionEvent e)->{
            if(usernameTextField.getText().equals("")||passwordTextField.getText().equals("")||nameTextField.getText().equals(""))
            {errorView.ShowError("One or more of the fields are empty.");}
            else
            {
                try{
                    GetSellerFromUsername(usernameTextField.getText());
                    errorView.ShowError("This username is already taken.");
                }catch(NoSuchElementException ex1){
                    try{
                        GetCustomerFromUsername(usernameTextField.getText());
                        errorView.ShowError("This username is already taken.");
                    }catch(NoSuchElementException ex2)
                    {
                        if(customerSelection.isSelected())
                        {
                            Customer currentCustomer = new Customer(usernameTextField.getText(), passwordTextField.getText(), nameTextField.getText());
                            customersList.add(currentCustomer);
                            currentUser = currentCustomer;
                            OpenShoppingScreen(currentCustomer);
                        }else if(sellerSelection.isSelected()){
                            Seller currentSeller = new Seller(usernameTextField.getText(), passwordTextField.getText(), nameTextField.getText());
                            sellersList.add(currentSeller);
                            currentUser = currentSeller;
                            OpenInventoryScreen(currentSeller);
                        }else{errorView.ShowError("An account type hasn't been selected.");}
                    }
                }
            }
        });
        // Adding an ActionListener to loginInstead button
loginInstead.addActionListener((ActionEvent e) -> {
    // Calling the method to open the login screen
    OpenLoginScreen();
});

// Creating a KeyListener for registration functionality
KeyListener registerOnEnter = new KeyListener() {
    @Override
    public void keyTyped(KeyEvent e) {
        // Not handling keyTyped event, leaving it empty
    }

    @Override
    public void keyPressed(KeyEvent theKey) {
        // Checking if the pressed key is the 'Enter' key
        if (theKey.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            // If 'Enter' is pressed, simulate a click on the registerButton
            registerButton.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not handling keyReleased event, leaving it empty
    }
};

// Adding the registerOnEnter KeyListener to the username, password, and name text fields
usernameTextField.addKeyListener(registerOnEnter);
passwordTextField.addKeyListener(registerOnEnter);
nameTextField.addKeyListener(registerOnEnter);

// Packing the mainView (assuming mainView is a Swing container/window)
mainView.pack();


    }
    
    /**
     * Opens the settings screen where users can manage their account settings.
     */
    public void OpenSettingsScreen()
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        // Top Bar
        JPanel titleBar = new JPanel();
        titleBar.setLayout(new BoxLayout(titleBar, BoxLayout.LINE_AXIS));
        JButton goBack = new JButton("Go Back");
        JLabel settingsLabel = new JLabel("Settings");
        JButton logoutButton = new JButton("Log out");
        titleBar.add(goBack);
        titleBar.add(settingsLabel);
        titleBar.add(logoutButton);
        goBack.addActionListener((ActionEvent e)->{
            switch (currentUser.GetUserType()) {
                case "Seller" -> OpenInventoryScreen((Seller)currentUser);
                case "Customer" -> OpenShoppingScreen((Customer)currentUser);
                default -> errorView.ShowError("Something went wrong! Unable to get user's account type, please close the app or log out.");
            }
        });
        logoutButton.addActionListener((ActionEvent e)->{
            currentUser = null;
            OpenLoginScreen();
        });
        container.add(titleBar);
        titleBar.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        // Username
        JPanel usernameBar = new JPanel();
        usernameBar.setLayout(new BoxLayout(usernameBar,BoxLayout.LINE_AXIS));
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField usernameTextField = new JTextField(currentUser.GetUsername());
        usernameBar.add(usernameLabel);
        usernameBar.add(usernameTextField);
        container.add(usernameBar);
        // Name
        JPanel nameBar = new JPanel();
        nameBar.setLayout(new BoxLayout(nameBar,BoxLayout.LINE_AXIS));
        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameTextField = new JTextField(currentUser.GetName());
        nameBar.add(nameLabel);
        nameBar.add(nameTextField);
        container.add(nameBar);
        // Password
        JPanel newPasswordBar = new JPanel();
        newPasswordBar.setLayout(new BoxLayout(newPasswordBar,BoxLayout.LINE_AXIS));
        JLabel newPasswordLabel = new JLabel("New Password: ");
        JTextField newPasswordTextField = new JTextField();
        newPasswordBar.add(newPasswordLabel);
        newPasswordBar.add(newPasswordTextField);
        JLabel newPasswordNote1 = new JLabel("*You only need to enter your current password when changing your password.");
        JLabel newPasswordNote2 = new JLabel("Leave the above field blank and you dont have to enter your current password.");
        container.add(newPasswordBar);
        container.add(newPasswordNote1);
        container.add(newPasswordNote2);
        // Current Password
        JPanel oldPasswordBar = new JPanel();
        oldPasswordBar.setLayout(new BoxLayout(oldPasswordBar,BoxLayout.LINE_AXIS));
        JLabel oldPasswordLabel = new JLabel("Current Password: ");
        JTextField oldPasswordTextField = new JTextField();
        oldPasswordBar.add(oldPasswordLabel);
        oldPasswordBar.add(oldPasswordTextField);
        container.add(oldPasswordBar);
        // Add Funds
        JPanel addFundsBar = new JPanel();
        addFundsBar.setLayout(new BoxLayout(addFundsBar,BoxLayout.LINE_AXIS));
        JLabel fundsLabel = new JLabel("Add Funds: ");
        JTextField fundsTextField = new JTextField();
        addFundsBar.add(fundsLabel);
        addFundsBar.add(fundsTextField);
        container.add(addFundsBar);
        // Save Button
        JButton saveSettings = new JButton("Save");
        container.add(saveSettings);
        saveSettings.setAlignmentX(JButton.CENTER_ALIGNMENT);
        saveSettings.addActionListener((ActionEvent e) -> {
            if(usernameTextField.getText().equals("")||nameTextField.getText().equals(""))
            {
                errorView.ShowError("One or more of the required fields are empty.");
                return;
            }
            try{
                Seller foundUser = GetSellerFromUsername(usernameTextField.getText());
                if(foundUser.GetUsername().equals(currentUser.GetUsername()))
                {throw new NoSuchElementException();}
                errorView.ShowError("This username is already taken.");
            }catch(NoSuchElementException ex1){
                try{
                    Customer foundUser = GetCustomerFromUsername(usernameTextField.getText());
                    if(foundUser.GetUsername().equals(currentUser.GetUsername()))
                    {throw new NoSuchElementException();}
                    errorView.ShowError("This username is already taken.");
                }catch(NoSuchElementException ex2)
                {
                    if(!newPasswordTextField.getText().equals(""))
                    {
                        try
                        {currentUser.ChangePassword(oldPasswordTextField.getText(), newPasswordTextField.getText());}
                        catch(Exception ex)
                        {
                            errorView.ShowError("You have attempted to change your password but entered your current password incorrectly. Please try again.");
                            return;
                        }
                    }
                    currentUser.ChangeName(nameTextField.getText());
                    currentUser.ChangeUsername(usernameTextField.getText());
                    oldPasswordTextField.setText("");
                    newPasswordTextField.setText("");
                    if(!fundsTextField.getText().equals(""))
                    {
                        try
                        {
                            float inputFunds = Float.parseFloat(fundsTextField.getText());
                            currentUser.AddFunds(inputFunds);
                            fundsTextField.setText("");
                            errorView.showStatusSuccess();
                        }
                        catch(NumberFormatException ex3)
                        {errorView.ShowError("Add funds field has been entered incorrectly. Other changes to account have been saved.");}
                        catch (Exception ex)
                        {errorView.ShowError("Can not add negative funds to balance. Other changes to account have been saced.");}
                    }
                    else
                    {errorView.showStatusSuccess();}
                }
            }
        });
        // Creating a KeyListener for saving settings functionality
KeyListener saveOnEnter = new KeyListener() {
    @Override
    public void keyTyped(KeyEvent e) {
        // Not handling keyTyped event, leaving it empty
    }

    @Override
    public void keyPressed(KeyEvent theKey) {
        // Checking if the pressed key is the 'Enter' key
        if (theKey.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
            // If 'Enter' is pressed, simulate a click on the saveSettings button
            saveSettings.doClick();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not handling keyReleased event, leaving it empty
    }
};

// Adding the saveOnEnter KeyListener to specific text fields
usernameTextField.addKeyListener(saveOnEnter);
nameTextField.addKeyListener(saveOnEnter);
newPasswordTextField.addKeyListener(saveOnEnter);
oldPasswordTextField.addKeyListener(saveOnEnter);

// Packing the mainView (assuming mainView is a Swing container/window)
mainView.pack();

    }
    
    /**
     * Opens the shopping screen for a specific customer, displaying available items for purchase.
     * This method sets up the GUI to show available items for the customer to browse and buy.
     * It populates the screen with a list of items from sellers and enables the user to interact
     * with each item to potentially add them to the shopping cart.
     * @param currentCustomer The customer currently logged into the shopping system.
     */
    public void OpenShoppingScreen(Customer currentCustomer)
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        // Title Bar
        JPanel titleBar = new JPanel();
        JLabel titleLabel = new JLabel("Shopping Menu");
        JButton logoutButton = new JButton("Log out");
        JButton settingsButton = new JButton("User Settings");
        JButton cartButton = new JButton("Shopping Cart ("+String.valueOf(currentCustomer.customerCart.GetSize())+")");
        JLabel balanceLabel = new JLabel("Balance: "+String.valueOf(currentCustomer.GetBalance()));
        titleBar.add(cartButton);
        titleBar.add(titleLabel);
        titleBar.add(logoutButton);
        titleBar.add(settingsButton);
        titleBar.add(balanceLabel);
        logoutButton.addActionListener((ActionEvent e)->{
            currentUser = null;
            OpenLoginScreen();
        });
        settingsButton.addActionListener((ActionEvent e)->{
            OpenSettingsScreen();
        });
        cartButton.addActionListener((ActionEvent e)->{
            OpenShoppingCartScreen(currentCustomer);
        });
        container.add(titleBar);
        // Inventory List
        JPanel shoppingPanel = new JPanel();
        shoppingPanel.setLayout(new BoxLayout(shoppingPanel,BoxLayout.PAGE_AXIS));
        ArrayList<Item> itemsListed = new ArrayList<Item>();
        int totalIndex = 0;
        for(int j=0;j<sellersList.size();j++)
        {
            Seller currentSeller = sellersList.get(j);
            for(int i=0;i<currentSeller.GetInventorySize();i++)
            {
                Item currentItem = currentSeller.GetItemInInventory(i);
                if (currentItem.GetAvailability())
                {
                    int myIndex = totalIndex;
                    totalIndex++;
                    itemsListed.add(currentItem);
                    JPanel itemSuperPanel = new JPanel();
                    itemSuperPanel.setLayout(new BoxLayout(itemSuperPanel, BoxLayout.LINE_AXIS));
                    JPanel itemLeftPanel = new JPanel();
                    itemLeftPanel.setLayout(new BoxLayout(itemLeftPanel, BoxLayout.PAGE_AXIS));
                    JPanel itemRightPanel = new JPanel();
                    itemRightPanel.setLayout(new BoxLayout(itemRightPanel, BoxLayout.PAGE_AXIS));
                    itemLeftPanel.add(new JLabel(currentItem.GetName()));
                    itemLeftPanel.add(new JLabel("Seller: "+currentItem.GetSeller().GetName()));
                    itemRightPanel.add(new JLabel("Price: "+String.valueOf(currentItem.GetPrice())+"$"));
                    itemRightPanel.add(new JLabel("Stock: "+String.valueOf(currentItem.GetStock())));
                    shoppingPanel.add(itemSuperPanel);
                    itemSuperPanel.add(itemLeftPanel);
                    JPanel spacePanel = new JPanel();
                    spacePanel.setSize(10, 0);
                    itemSuperPanel.add(spacePanel);
                    itemSuperPanel.add(itemRightPanel);
                    itemSuperPanel.addMouseListener(new MouseListener(){
                        @Override
                        public void mouseClicked(MouseEvent e)
                        {AddItemToCartScreen(currentCustomer, itemsListed.get(myIndex));}
                        @Override
                        public void mousePressed(MouseEvent e) {}
                        @Override
                        public void mouseReleased(MouseEvent e) {}
                        @Override
                        public void mouseEntered(MouseEvent e) {}
                        @Override
                        public void mouseExited(MouseEvent e) {}
                    });
                    if(myIndex%2==1){
                        itemSuperPanel.setBackground(Color.LIGHT_GRAY);
                        itemLeftPanel.setBackground(Color.LIGHT_GRAY);
                        spacePanel.setBackground(Color.LIGHT_GRAY);
                        itemRightPanel.setBackground(Color.LIGHT_GRAY);
                    }
                    else{
                        itemSuperPanel.setBackground(Color.WHITE);
                        itemLeftPanel.setBackground(Color.WHITE);
                        spacePanel.setBackground(Color.WHITE);
                        itemRightPanel.setBackground(Color.WHITE);
                    }
                }
            }
        }
        JScrollPane shoppingView = new JScrollPane(shoppingPanel);
        shoppingView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        shoppingView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(shoppingView);
        mainView.pack();
    }
    
    /**
     * Displays a screen for adding an item to the customer's cart.
     * This method shows a GUI screen where the user can view details of a specific item
     * and add it to their shopping cart with a specified quantity.
     * @param currentCustomer The customer currently logged into the shopping system.
     * @param item            The item the user intends to add to their shopping cart.
     */
    public void AddItemToCartScreen(Customer currentCustomer, Item item)
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        JLabel itemName = new JLabel(item.GetName());
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(itemName);
        JLabel itemDescription = new JLabel(item.GetDescription());
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new FlowLayout());
        descPanel.add(itemDescription);
        JPanel stockPanel = new JPanel();
        JLabel stockLabel = new JLabel("How many to add to cart?");
        JPanel stockSpacer = new JPanel();
        stockSpacer.setSize(10,0);
        JTextField stockTextField = new JTextField("1");
        stockTextField.setColumns(3);
        int itemCartIndex = currentCustomer.customerCart.ContainsItem(item);
        if(itemCartIndex>=0)
        {
            stockTextField.setText(String.valueOf(currentCustomer.customerCart.GetPurchaseAmount(itemCartIndex)));
            stockLabel.setText("Item is in the cart. Would you like to change the amount?");
        }
        stockPanel.add(stockLabel);
        stockPanel.add(stockSpacer);
        stockPanel.add(stockTextField);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
        JButton goBackButton = new JButton("Go Back");
        JButton addToCartButton = new JButton("Add to Cart");
        if(itemCartIndex>=0)
        {addToCartButton.setText("Update Purchase Amount");}
        JButton viewDetailsButton = new JButton("View Details");
        buttonPanel.add(goBackButton);
        buttonPanel.add(addToCartButton);
        buttonPanel.add(viewDetailsButton);
        goBackButton.addActionListener((ActionEvent e)->{OpenShoppingScreen(currentCustomer);});
        addToCartButton.addActionListener((ActionEvent e)->
        {
            try
            {
                int stockCount = Integer.parseInt(stockTextField.getText());
                if(itemCartIndex>=0)
                {
                    if(stockCount>=0)
                    {
                        if(stockCount>item.GetStock())
                        {errorView.ShowError("This amount is over the current stock of the item.");}
                        else
                        {
                            currentCustomer.customerCart.SetPurchaseAmount(itemCartIndex, stockCount);
                            OpenShoppingScreen(currentCustomer);
                        }
                    }else
                    {errorView.ShowError("When editing stock count for an item in your cart please keep the stock equal to or over 0.");}
                }else{
                    if(stockCount>0)
                    {
                        if(stockCount>item.GetStock())
                        {errorView.ShowError("This amount is over the current stock of the item.");}
                        else
                        {
                            currentCustomer.customerCart.AddItem(item,stockCount);
                            OpenShoppingScreen(currentCustomer);
                        }
                    }else{errorView.ShowError("When adding a new item stock must be greater than 0.");}
                }
            }catch(NumberFormatException ex)
            {errorView.ShowError("Invalid stock input.");}
        });
        viewDetailsButton.addActionListener((ActionEvent e)->{detailsView.ShowItem(item);});
        container.add(namePanel);
        container.add(descPanel);
        container.add(stockPanel);
        container.add(buttonPanel);
        mainView.pack();
        
    }
    
    /**
     * Opens the shopping cart screen for a specific customer.
     * This method constructs a graphical user interface displaying the items
     * currently added to the customer's shopping cart. It allows users to view
     * the cart contents, modify the quantities of items, remove items, and proceed
     * to complete the purchase transaction if all items are available and valid.
     * 
     * @param currentCustomer The customer whose shopping cart is being displayed.
     */
    public void OpenShoppingCartScreen(Customer currentCustomer)
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container,BoxLayout.PAGE_AXIS));
        // Title Bar
        JPanel titleBar = new JPanel();
        JLabel titleLabel = new JLabel("My Shopping Cart");
        JButton logoutButton = new JButton("Log out");
        JButton goBackButton = new JButton("Go Back");
        JLabel balanceLabel = new JLabel("Balance: "+String.valueOf(currentCustomer.GetBalance()));
        titleBar.add(titleLabel);
        titleBar.add(goBackButton);
        titleBar.add(logoutButton);
        titleBar.add(balanceLabel);
        logoutButton.addActionListener((ActionEvent e)->{
            currentUser = null;
            OpenLoginScreen();
        });
        goBackButton.addActionListener((ActionEvent e)->{
            OpenShoppingScreen(currentCustomer);
        });
        container.add(titleBar);
        // Cart Scroll Pane
        JScrollPane cartItemView = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        container.add(cartItemView);
        JPanel cartPanel = new JPanel();
        ArrayList<JTextField> amountTextFields = new ArrayList<JTextField>();
        cartPanel.setLayout(new BoxLayout(cartPanel,BoxLayout.PAGE_AXIS));
        for(int i=0;i<currentCustomer.customerCart.GetSize();i++)
        {
            Item currentItem = currentCustomer.customerCart.GetItem(i);
            int myIndex = i;
            JPanel itemSuperPanel = new JPanel();
            itemSuperPanel.setLayout(new BoxLayout(itemSuperPanel, BoxLayout.LINE_AXIS));
            JPanel itemLeftPanel = new JPanel();
            itemLeftPanel.setLayout(new BoxLayout(itemLeftPanel, BoxLayout.PAGE_AXIS));
            JPanel itemCenterPanel = new JPanel();
            itemCenterPanel.setLayout(new BoxLayout(itemCenterPanel, BoxLayout.PAGE_AXIS));
            JPanel itemRightPanel = new JPanel();
            itemRightPanel.setLayout(new BoxLayout(itemRightPanel, BoxLayout.PAGE_AXIS));
            itemLeftPanel.add(new JLabel(currentItem.GetName()));
            itemLeftPanel.add(new JLabel("Seller: "+currentItem.GetSeller().GetName()));
            itemCenterPanel.add(new JLabel("Price: "+String.valueOf(currentItem.GetPrice())+"$"));
            JTextField purchaseAmountTextField = new JTextField(String.valueOf(currentCustomer.customerCart.GetPurchaseAmount(myIndex)));
            purchaseAmountTextField.setColumns(3);
            purchaseAmountTextField.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) {
                    purchaseAmountTextField.setBackground(Color.WHITE);
                }
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            
            });
            itemCenterPanel.add(purchaseAmountTextField);
            amountTextFields.add(purchaseAmountTextField);
            JButton removeItemButton = new JButton("X");
            removeItemButton.setBackground(Color.RED);
            itemRightPanel.add(removeItemButton);
            removeItemButton.addActionListener((ActionEvent e)->{
                currentCustomer.customerCart.RemoveItem(myIndex);
                OpenShoppingCartScreen(currentCustomer);
            });
            cartPanel.add(itemSuperPanel);
            itemSuperPanel.add(itemLeftPanel);
            JPanel leftSpacePanel = new JPanel();
            leftSpacePanel.setSize(10, 0);
            itemSuperPanel.add(leftSpacePanel);
            itemSuperPanel.add(itemCenterPanel);
            JPanel rightSpacePanel = new JPanel();
            rightSpacePanel.setSize(10, 0);
            itemSuperPanel.add(rightSpacePanel);
            itemSuperPanel.add(itemRightPanel);
            itemSuperPanel.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e)
                {detailsView.ShowItem(currentItem);}
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            if(myIndex%2==1){
                itemSuperPanel.setBackground(Color.LIGHT_GRAY);
                itemLeftPanel.setBackground(Color.LIGHT_GRAY);
                leftSpacePanel.setBackground(Color.LIGHT_GRAY);
                itemCenterPanel.setBackground(Color.LIGHT_GRAY);
                rightSpacePanel.setBackground(Color.LIGHT_GRAY);
                itemRightPanel.setBackground(Color.LIGHT_GRAY);
            }
            else{
                itemSuperPanel.setBackground(Color.WHITE);
                itemLeftPanel.setBackground(Color.WHITE);
                leftSpacePanel.setBackground(Color.WHITE);
                itemCenterPanel.setBackground(Color.WHITE);
                rightSpacePanel.setBackground(Color.WHITE);
                itemRightPanel.setBackground(Color.WHITE);
            }
        }
        cartItemView.setViewportView(cartPanel);
        // Purchase Bar
        JPanel purchaseBar = new JPanel();
        purchaseBar.setLayout(new BoxLayout(purchaseBar, BoxLayout.LINE_AXIS));
        JLabel totalPurchasePriceLabel = new JLabel("Total: "+String.valueOf(currentCustomer.customerCart.GetTotal())+"$");
        JButton completeTransactionButton = new JButton("Complete Transaction");
        purchaseBar.add(totalPurchasePriceLabel);
        purchaseBar.add(completeTransactionButton);
        completeTransactionButton.addActionListener((ActionEvent e)->{
            //errorView.ShowError("Transacting currently uncoded.");
            // Verify amounts are valid.
            if(currentCustomer.customerCart.GetSize()>0)
            {
                boolean flag = false;
                for(int i=0;i<amountTextFields.size();i++)
                {
                    try
                    {
                        String typedValue = amountTextFields.get(i).getText();
                        int typedAmount = Integer.parseInt(typedValue);
                        if(
                                typedAmount<=0||
                                typedAmount>currentCustomer.customerCart.GetItem(i).GetStock()||
                                currentCustomer.customerCart.GetItem(i).GetAvailability()==false
                        )
                        {
                            flag = true;
                            amountTextFields.get(i).setBackground(Color.red);
                        }
                        else
                        {currentCustomer.customerCart.SetPurchaseAmount(i, typedAmount);}
                    }
                    catch(NumberFormatException ex)
                    {
                        flag = true;
                        amountTextFields.get(i).setBackground(Color.red);
                    }
                }
                if(flag == true)
                {errorView.ShowError("Transaction failed, some of the items are unavailabe or have an invalid amount typed. The other typed amounts have been saved.");}
                else
                {
                    float cartTotal = currentCustomer.customerCart.GetTotal();
                    try
                    {
                        ArrayList<String[]> receipt = currentCustomer.CompleteTransaction();
                        OpenReceiptView(currentCustomer, receipt, cartTotal);
                    } 
                    catch (Exception ex)
                    {errorView.ShowError("You lack the funds to complete this purchase.");}
                }
            }else{errorView.ShowError("There is nothing in your cart!");}
        });
        container.add(purchaseBar);
        mainView.pack();
    }
    
    /**
     * Opens a receipt view displaying the details of a completed transaction.
     * Constructs a graphical user interface to show the transaction receipt,
     * listing the purchased items, their prices, and quantities. Also includes
     * a "Done" button to return to the shopping screen.
     * @param currentCustomer The customer who completed the transaction.
     * @param receipt An ArrayList containing String arrays with item details for the receipt.
     * @param cartTotal The total cost of the items in the cart.
     */
    public void OpenReceiptView(Customer currentCustomer, ArrayList<String[]> receipt, float cartTotal)
    {
        ResetMainView();
        container.setLayout(new BorderLayout());
        Object[][] receipt2D = new Object[receipt.size()+2][3];
        receipt2D[0][0] = "Name";
        receipt2D[0][1] = "Price";
        receipt2D[0][2] = "Amount";
        for(int i=1;i<receipt.size()+1;i++)
        {
            System.arraycopy(receipt.get(i-1), 0, receipt2D[i], 0, 3);
        }
        receipt2D[receipt.size()+1][0] = "Total";
        receipt2D[receipt.size()+1][1] = String.valueOf(cartTotal);
        DefaultTableModel receiptTable = new DefaultTableModel(receipt2D,receipt2D[0]);
        JTable receiptDisplayTable = new JTable(receiptTable);
        DefaultTableCellRenderer receiptRenderer = new DefaultTableCellRenderer();
        receiptRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        receiptDisplayTable.setDefaultRenderer(Object.class, receiptRenderer);
        JPanel receiptPanel = new JPanel(new BorderLayout());
        receiptPanel.add(receiptDisplayTable,BorderLayout.CENTER);
        JScrollPane receiptScrollPane = new JScrollPane(receiptPanel);
        receiptScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        receiptScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton doneButton = new JButton("Done");
        doneButton.addActionListener((ActionEvent e)->{OpenShoppingScreen(currentCustomer);});
        container.add(new JLabel("Thank you for shopping!"), BorderLayout.NORTH);
        container.add(receiptScrollPane,BorderLayout.CENTER);
        container.add(doneButton, BorderLayout.SOUTH);
        mainView.pack();
    }
    
    /**
     * Opens the inventory screen for a specific seller.
     * Constructs a graphical user interface to display the seller's inventory,
     * allowing the seller to view their listed items, their availability, prices, stock,
     * and remove items from sale. It also provides an option to add new items.
     * @param currentSeller The seller whose inventory is being displayed.
     */
    public void OpenInventoryScreen(Seller currentSeller)
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container, BoxLayout.PAGE_AXIS));
        // Title Bar
        JPanel titleBar = new JPanel();
        JLabel titleLabel = new JLabel("Inventory");
        JButton logoutButton = new JButton("Log out");
        JButton settingsButton = new JButton("User Settings");
        JLabel balanceLabel = new JLabel("Balance: "+String.valueOf(currentSeller.GetBalance()));
        titleBar.add(titleLabel);
        titleBar.add(logoutButton);
        titleBar.add(settingsButton);
        titleBar.add(balanceLabel);
        logoutButton.addActionListener((ActionEvent e)->{
            currentUser = null;
            OpenLoginScreen();
        });
        settingsButton.addActionListener((ActionEvent e)->{
            OpenSettingsScreen();
        });
        container.add(titleBar);
        // Inventory List
        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel,BoxLayout.PAGE_AXIS));
        // Looping through the seller's inventory items
        for(int i=0;i<currentSeller.GetInventorySize();i++)
        {
            Item currentItem = currentSeller.GetItemInInventory(i);
            int myIndex = i;
            JPanel itemSuperPanel = new JPanel();
            itemSuperPanel.setLayout(new BoxLayout(itemSuperPanel, BoxLayout.LINE_AXIS));
            JPanel itemLeftPanel = new JPanel();
            itemLeftPanel.setLayout(new BoxLayout(itemLeftPanel, BoxLayout.PAGE_AXIS));
            JPanel itemRightPanel = new JPanel();
            itemRightPanel.setLayout(new BoxLayout(itemRightPanel, BoxLayout.PAGE_AXIS));
            JPanel itemRemovePanel = new JPanel();
            itemRemovePanel.setLayout(new BoxLayout(itemRemovePanel, BoxLayout.PAGE_AXIS));
            itemLeftPanel.add(new JLabel(currentItem.GetName()));
            itemLeftPanel.add(new JLabel("On Sale: "+String.valueOf(currentItem.GetAvailability())));
            itemRightPanel.add(new JLabel("Price: "+String.valueOf(currentItem.GetPrice())+"$"));
            itemRightPanel.add(new JLabel("Stock: "+String.valueOf(currentItem.GetStock())));
            JButton itemRemoveButton = new JButton("X");
            itemRemoveButton.setBackground(Color.RED);
            itemRemoveButton.addActionListener((ActionEvent e)->{
                currentSeller.RemoveFromSale(myIndex);
                OpenInventoryScreen(currentSeller);
            });
            itemRemovePanel.add(itemRemoveButton);
            inventoryPanel.add(itemSuperPanel);
            itemSuperPanel.add(itemLeftPanel);
            JPanel spacePanel = new JPanel();
            spacePanel.setSize(10, 0);
            itemSuperPanel.add(spacePanel);
            itemSuperPanel.add(itemRightPanel);
            JPanel removeSpacePanel = new JPanel();
            removeSpacePanel.setSize(10, 0);
            itemSuperPanel.add(removeSpacePanel);
            itemSuperPanel.add(itemRemovePanel);
            itemSuperPanel.addMouseListener(new MouseListener(){
                @Override
                public void mouseClicked(MouseEvent e) 
                {OpenItemEditScreen(currentSeller,myIndex);}
                @Override
                public void mousePressed(MouseEvent e) {}
                @Override
                public void mouseReleased(MouseEvent e) {}
                @Override
                public void mouseEntered(MouseEvent e) {}
                @Override
                public void mouseExited(MouseEvent e) {}
            });
            if(myIndex%2==1){
                itemSuperPanel.setBackground(Color.LIGHT_GRAY);
                itemLeftPanel.setBackground(Color.LIGHT_GRAY);
                spacePanel.setBackground(Color.LIGHT_GRAY);
                itemRightPanel.setBackground(Color.LIGHT_GRAY);
                removeSpacePanel.setBackground(Color.LIGHT_GRAY);
                itemRemovePanel.setBackground(Color.LIGHT_GRAY);
            }
            else{
                itemSuperPanel.setBackground(Color.WHITE);
                itemLeftPanel.setBackground(Color.WHITE);
                spacePanel.setBackground(Color.WHITE);
                itemRightPanel.setBackground(Color.WHITE);
                removeSpacePanel.setBackground(Color.WHITE);
                itemRemovePanel.setBackground(Color.WHITE);
            }
        }
        JButton newItemButton = new JButton("Add New Item");
        inventoryPanel.add(newItemButton);
        newItemButton.addActionListener((ActionEvent e)->
        {OpenItemEditScreen(currentSeller,-1);});
        newItemButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        JScrollPane inventoryView = new JScrollPane(inventoryPanel);
        inventoryView.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        inventoryView.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(inventoryView);
        mainView.pack();
    }
    
    /**
     * Opens the screen for editing an item's details.
     * If editing an existing item, it retrieves the item's details and sets up the screen.
     * If adding a new item, initializes an empty item for editing.
     * @param currentSeller The seller whose item is being edited or for whom a new item is being added.
     * @param itemIndex The index of the item being edited in the seller's inventory. 
     *                 Use -1 to initialize a new item for addition.
     */
    public void OpenItemEditScreen(Seller currentSeller, int itemIndex)
    {
        ResetMainView();
        container.setLayout(new BoxLayout(container,BoxLayout.PAGE_AXIS));
        // Get Values
        Item item;
        if(itemIndex>=0)
        {item = currentSeller.GetItemInInventory(itemIndex);}
        else
        {item = new Item("","",0,new ArrayList<String>(),new ArrayList<String>(),currentSeller);}
        String tempName = item.GetName();
        String tempDescription = item.GetDescription();
        float tempPrice = item.GetPrice();
        int tempStock = item.GetStock();
        boolean tempAvailability = item.GetAvailability();
        ArrayList<String> tempSpecName = new ArrayList<String>();
        ArrayList<String> tempSpec = new ArrayList<String>();
        for(int i=0;i<item.GetSpecSize();i++)
        {
            String[] specInfo = item.GetSpec(i);
            tempSpecName.add(specInfo[0]);
            tempSpec.add(specInfo[1]);
        }
        // Edit Item View
        MouseListener textFieldListener = new MouseListener(){
            @Override
            public void mouseClicked(MouseEvent e)
            {e.getComponent().setBackground(Color.WHITE);}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        };
        // Name
        JPanel itemNamePanel = new JPanel();
        itemNamePanel.setLayout(new BoxLayout(itemNamePanel, BoxLayout.LINE_AXIS));
        JLabel itemNameLabel = new JLabel("Name:");
        JTextField itemNameTextField = new JTextField(tempName);
        itemNameTextField.setColumns(20);
        itemNamePanel.add(itemNameLabel);
        itemNamePanel.add(itemNameTextField);
        itemNameTextField.addMouseListener(textFieldListener);
        container.add(itemNamePanel);
        // Description
        JPanel itemDescriptionPanel = new JPanel();
        itemDescriptionPanel.setLayout(new BoxLayout(itemDescriptionPanel, BoxLayout.LINE_AXIS));
        JLabel itemDescriptionLabel = new JLabel("Description:");
        JTextField itemDescriptionTextField = new JTextField(tempDescription);
        itemDescriptionTextField.setColumns(20);
        itemDescriptionPanel.add(itemDescriptionLabel);
        itemDescriptionPanel.add(itemDescriptionTextField);
        itemDescriptionTextField.addMouseListener(textFieldListener);
        container.add(itemDescriptionPanel);
        // Price
        JPanel itemPricePanel = new JPanel();
        itemPricePanel.setLayout(new BoxLayout(itemPricePanel, BoxLayout.LINE_AXIS));
        JLabel itemPriceLabel = new JLabel("Price:");
        JTextField itemPriceTextField = new JTextField(String.valueOf(tempPrice));
        itemPriceTextField.setColumns(3);
        itemPricePanel.add(itemPriceLabel);
        itemPricePanel.add(itemPriceTextField);
        itemPriceTextField.addMouseListener(textFieldListener);
        container.add(itemPricePanel);
        // Stock
        JPanel itemStockPanel = new JPanel();
        itemStockPanel.setLayout(new BoxLayout(itemStockPanel, BoxLayout.LINE_AXIS));
        JLabel itemStockLabel = new JLabel("Amount in stock:");
        JTextField itemStockTextField = new JTextField(String.valueOf(tempStock));
        itemStockTextField.setColumns(3);
        itemStockPanel.add(itemStockLabel);
        itemStockPanel.add(itemStockTextField);
        itemStockTextField.addMouseListener(textFieldListener);
        container.add(itemStockPanel);
        // Available
        JPanel itemAvailabilityPanel = new JPanel();
        itemAvailabilityPanel.setLayout(new BoxLayout(itemAvailabilityPanel, BoxLayout.LINE_AXIS));
        JCheckBox itemAvailableCheckBox = new JCheckBox("Item is on sale");
        itemAvailableCheckBox.setSelected(tempAvailability);
        itemAvailabilityPanel.add(itemAvailableCheckBox);
        container.add(itemAvailabilityPanel);
        // Specs Scrollpane
        JPanel itemSpecsPanel = new JPanel();
        ArrayList<JTextField> itemSpecNameTFList = new ArrayList<JTextField>();
        ArrayList<JTextField> itemSpecTFList = new ArrayList<JTextField>();
        itemSpecsPanel.setLayout(new BoxLayout(itemSpecsPanel, BoxLayout.PAGE_AXIS));
        for(int i=0;i<tempSpecName.size();i++)
        {
            int myIndex = i;
            JPanel specPanel = new JPanel();
            specPanel.setLayout(new BoxLayout(specPanel, BoxLayout.LINE_AXIS));
            JTextField specNameTF = new JTextField(tempSpecName.get(myIndex));
            JTextField specValueTF = new JTextField(tempSpec.get(myIndex));
            JButton deleteSpec = new JButton("X");
            deleteSpec.setBackground(Color.red);
            specPanel.add(specNameTF);
            specPanel.add(specValueTF);
            specPanel.add(deleteSpec);
            specNameTF.addMouseListener(textFieldListener);
            specValueTF.addMouseListener(textFieldListener);
            itemSpecNameTFList.add(specNameTF);
            itemSpecTFList.add(specValueTF);
            deleteSpec.addActionListener((ActionEvent e)->{
                itemSpecsPanel.remove(specPanel);
                int myCurrentIndex = itemSpecNameTFList.indexOf(specNameTF);
                itemSpecNameTFList.remove(myCurrentIndex);
                itemSpecTFList.remove(myCurrentIndex);
                tempSpec.remove(myCurrentIndex);
                tempSpecName.remove(myCurrentIndex);
                itemSpecsPanel.revalidate();
                itemSpecsPanel.repaint();
                mainView.pack();
            });
            itemSpecsPanel.add(specPanel);
        }
        JPanel newSpec = new JPanel();
        newSpec.setLayout(new FlowLayout());
        JButton newSpecButton = new JButton("Add new specification");
        newSpec.add(newSpecButton);
        newSpecButton.addActionListener((ActionEvent e)->{
            itemSpecsPanel.remove(newSpec);
            int myIndex = tempSpecName.size();
            tempSpecName.add("");
            tempSpec.add("");
            JPanel specPanel = new JPanel();
            specPanel.setLayout(new BoxLayout(specPanel, BoxLayout.LINE_AXIS));
            JTextField specNameTF = new JTextField(tempSpecName.get(myIndex));
            JTextField specValueTF = new JTextField(tempSpec.get(myIndex));
            JButton deleteSpec = new JButton("X");
            deleteSpec.setBackground(Color.red);
            specPanel.add(specNameTF);
            specPanel.add(specValueTF);
            specPanel.add(deleteSpec);
            specNameTF.addMouseListener(textFieldListener);
            specValueTF.addMouseListener(textFieldListener);
            itemSpecNameTFList.add(specNameTF);
            itemSpecTFList.add(specValueTF);
            deleteSpec.addActionListener((ActionEvent e2)->{
                itemSpecsPanel.remove(specPanel);
                int myCurrentIndex = itemSpecNameTFList.indexOf(specNameTF);
                itemSpecNameTFList.remove(myCurrentIndex);
                itemSpecTFList.remove(myCurrentIndex);
                tempSpec.remove(myCurrentIndex);
                tempSpecName.remove(myCurrentIndex);
                itemSpecsPanel.revalidate();
                itemSpecsPanel.repaint();
                mainView.pack();
            });
            itemSpecsPanel.add(specPanel);
            itemSpecsPanel.add(newSpec);
            itemSpecsPanel.revalidate();
            itemSpecsPanel.repaint();
            mainView.pack();
        });
        itemSpecsPanel.add(newSpec);
        JScrollPane itemSpecScrollPane = new JScrollPane(itemSpecsPanel);
        itemSpecScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        itemSpecScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        container.add(itemSpecScrollPane);
        // Return Button
        JPanel actionButtonPanel = new JPanel();
        actionButtonPanel.setLayout(new BoxLayout(actionButtonPanel,BoxLayout.LINE_AXIS));
        JButton returnButton = new JButton("Go Back");
        actionButtonPanel.add(returnButton);
        returnButton.addActionListener((ActionEvent e)->{
            OpenInventoryScreen(currentSeller);
        });
        // Preview Button
        if(itemIndex>=0)
        {
            JButton previewButton = new JButton("View as Customer");
            actionButtonPanel.add(previewButton);
            previewButton.addActionListener((ActionEvent e)->
            {detailsView.ShowItem(item);});
        }
        
        /**
        * Constructs a JButton labeled "Save" or "Add" based on the item index.
        * Adds an ActionListener to the button to handle saving or adding item details.
        * Highlights text fields with red if they are empty or contain invalid inputs.
        * Displays appropriate error messages for invalid inputs or actions.
        * Updates item details and performs actions based on the conditions.
        */
        // Save Button
        JButton saveButton = new JButton("Save");
        if(itemIndex==-1)
        {saveButton.setText("Add");}
        actionButtonPanel.add(saveButton);
        saveButton.addActionListener((ActionEvent e)->{
            // Collate Text Fields
            ArrayList<JTextField> textFieldChecks = new ArrayList<JTextField>();
            textFieldChecks.add(itemNameTextField);
            textFieldChecks.add(itemDescriptionTextField);
            textFieldChecks.add(itemPriceTextField);
            textFieldChecks.add(itemStockTextField);
            for(int i=0;i<itemSpecNameTFList.size();i++)
            {textFieldChecks.add(itemSpecNameTFList.get(i));}
            for(int i=0;i<itemSpecTFList.size();i++)
            {textFieldChecks.add(itemSpecTFList.get(i));}
            // Check for Problems
            boolean flag = false;
            for(int i=0;i<textFieldChecks.size();i++)
            {
                if(textFieldChecks.get(i).getText().equals(""))
                {
                    flag = true;
                    textFieldChecks.get(i).setBackground(Color.red);
                }
            }
            if(flag)
            {
                errorView.ShowError("One or more of the textfields (highlighted in red) are empty.");
                return;
            }
            float inputPriceValue;
            try
            {inputPriceValue = Float.parseFloat(itemPriceTextField.getText());}
            catch(NumberFormatException ex)
            {
                errorView.ShowError("Invalid input price.");
                itemPriceTextField.setBackground(Color.red);
                return;
            }
            if (inputPriceValue<0.01)
            {
                errorView.ShowError("Price is too small.");
                itemPriceTextField.setBackground(Color.red);
                return;
            }
            int inputStockValue;
            try
            {inputStockValue = Integer.parseInt(itemStockTextField.getText());}
            catch(NumberFormatException ex)
            {
                errorView.ShowError("Invalid stock input.");
                itemStockTextField.setBackground(Color.red);
                return;
            }
            if(inputStockValue<0)
            {
                errorView.ShowError("Stock can't be less than 0.");
                itemStockTextField.setBackground(Color.red);
                return;
            }
            if(inputStockValue==0&&itemAvailableCheckBox.isSelected())
            {
                errorView.ShowError("When stock is 0 the item can't be set to available.");
                return;
            }
            // Fill Spec Sheet
            for(int i=0;i<tempSpecName.size();i++)
            {tempSpecName.set(i, itemSpecNameTFList.get(i).getText());}
            for(int i=0;i<tempSpec.size();i++)
            {tempSpec.set(i, itemSpecTFList.get(i).getText());}
            // Saving
            item.SetName(itemNameTextField.getText());
            item.SetDescription(itemDescriptionTextField.getText());
            item.SetPrice(inputPriceValue);
            try{item.SetStock(inputStockValue);}catch(Exception ex){}
            try{item.SetAvailability(itemAvailableCheckBox.isSelected());}catch(Exception ex){}
            item.SetSpecNames(tempSpecName);
            item.SetSpecs(tempSpec);
            if(itemIndex==-1)
            {
                currentSeller.SellItem(item);
                detailsView.ShowItem(item);
                OpenInventoryScreen(currentSeller);
            }else
            {errorView.showStatusSuccess();}
        });
        container.add(actionButtonPanel);
        mainView.pack();
    }
    
   /**
    * Retrieves a seller object based on the provided username.
    * @param username The username of the seller to search for.
    * @return The Seller object associated with the provided username.
    * @throws NoSuchElementException If no seller is found with the given username.
    */
    public Seller GetSellerFromUsername(String username) throws NoSuchElementException {
        Seller foundSeller = null;
        for (int i = 0; i < sellersList.size(); i++) {
            Seller currentSeller = sellersList.get(i);
            if (currentSeller.GetUsername().equals(username)) {
                foundSeller = currentSeller;
                break;
            }
        }
        if (foundSeller == null)
            throw new NoSuchElementException();
        return foundSeller;
    }

    /**
     * Retrieves a customer object based on the provided username.
     * @param username The username of the customer to search for.
     * @return The Customer object associated with the provided username.
     * @throws NoSuchElementException If no customer is found with the given username.
     */
    public Customer GetCustomerFromUsername(String username) throws NoSuchElementException {
        Customer foundCustomer = null;
        for (int i = 0; i < customersList.size(); i++) {
            Customer currentCustomer = customersList.get(i);
            if (currentCustomer.GetUsername().equals(username)) {
                foundCustomer = currentCustomer;
                break;
            }
        }
        if (foundCustomer == null)
            throw new NoSuchElementException();
        return foundCustomer;
    }

    /**
     * Checks if a username is taken by either a seller or a customer.
     * @param testUsername The username to be checked for availability.
     * @return True if the username is taken by either a seller or a customer, otherwise false.
     */
    public boolean UsernameTaken(String testUsername) {
        try {
            GetSellerFromUsername(testUsername);
            return true;
        } catch (NoSuchElementException e1) {
            try {
                GetCustomerFromUsername(testUsername);
                return true;
            } catch (NoSuchElementException e2) {
                return false;
            }
        }
    }

    /**
     * Main entry point of the application. Initiates the starting of the system.
     * @param Args The command line arguments provided to the program.
     */
    public static void main(String[] Args)
    {systemSingleton.StartApp();}
    
    /**
     * Retrieves the instance of the ShoppingSystem (singleton pattern).
     * @return The singleton instance of the ShoppingSystem.
     */  
    public static ShoppingSystem GetInstance()
    {return systemSingleton;}
    
    private static final ShoppingSystem systemSingleton = new ShoppingSystem();
    private User currentUser;
    private ArrayList<Seller> sellersList;
    private ArrayList<Customer> customersList;
    private final JFrame mainView = new JFrame();
    private final JPanel container = new JPanel();
    private final ErrorView errorView = ErrorView.GetInstance();
    private final ItemDetailsView detailsView = ItemDetailsView.GetInstance();
}
