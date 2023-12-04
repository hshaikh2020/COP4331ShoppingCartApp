package cop4331.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import cop4331.models.Item;

/**
 * The ItemDetailsView class represents a view for displaying details of an item in a graphical user interface.
 * This class extends JFrame and follows the Singleton design pattern to maintain a single instance throughout the application.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class ItemDetailsView extends JFrame {
    /**
     * Private constructor to prevent external instantiation.
     */
    private ItemDetailsView() {
        super();
        setLayout(new BorderLayout());
        JPanel superPanel = new JPanel();
        superPanel.setLayout(new BoxLayout(superPanel, BoxLayout.PAGE_AXIS));
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout());
        namePanel.add(itemNameLabel);
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.LINE_AXIS));
        descPanel.add(itemDescriptionLabel);
        JPanel sellerPanel = new JPanel();
        sellerPanel.setLayout(new BoxLayout(sellerPanel, BoxLayout.LINE_AXIS));
        sellerPanel.add(sellerNameLabel);
        superPanel.add(namePanel);
        superPanel.add(descPanel);
        superPanel.add(sellerPanel);
        JPanel valuePanel = new JPanel();
        valuePanel.setLayout(new BoxLayout(valuePanel, BoxLayout.LINE_AXIS));
        valuePanel.add(priceTag);
        JPanel spacerPanel = new JPanel();
        spacerPanel.setSize(10, 0);
        valuePanel.add(spacerPanel);
        valuePanel.add(stockCount);
        superPanel.add(valuePanel);
        superPanel.add(specSheet);
        add(superPanel, BorderLayout.CENTER);
        JButton OkButton = new JButton("Done");
        OkButton.addActionListener((ActionEvent e) -> { this.setVisible(false); });
        add(OkButton, BorderLayout.PAGE_END);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        OkButton.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent theKey) {
                if (theKey.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
                    OkButton.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }

    /**
     * Displays the details of the given Item in the ItemDetailsView.
     * @param item The Item object whose details are to be displayed.
     */
    public void ShowItem(Item item) {
        setTitle("Item Details");
        itemNameLabel.setText(item.GetName());
        itemDescriptionLabel.setText(item.GetDescription());
        sellerNameLabel.setText(item.GetSeller().GetName());
        priceTag.setText("Price: " + String.valueOf(item.GetPrice()) + "$");
        stockCount.setText("Current Stock: " + String.valueOf(item.GetStock()));
        JPanel specSheetPanel = new JPanel();
        specSheetPanel.setLayout(new BoxLayout(specSheetPanel, BoxLayout.PAGE_AXIS));
        for (int i = 0; i < item.GetSpecSize(); i++) {
            JPanel specPanel = new JPanel();
            specPanel.setLayout(new BoxLayout(specPanel, BoxLayout.LINE_AXIS));
            String[] itemSpec = item.GetSpec(i);
            JLabel specName = new JLabel(itemSpec[0]);
            JLabel specDesc = new JLabel(itemSpec[1]);
            JPanel spacerPanel = new JPanel();
            spacerPanel.setSize(10, 0);
            specPanel.add(specName);
            specPanel.add(spacerPanel);
            specPanel.add(specDesc);
            if (i % 2 == 1) {
                specPanel.setBackground(Color.LIGHT_GRAY);
                spacerPanel.setBackground(Color.LIGHT_GRAY);
            } else {
                specPanel.setBackground(Color.WHITE);
                spacerPanel.setBackground(Color.WHITE);
            }
            specSheetPanel.add(specPanel);
        }
        specSheet.setViewportView(specSheetPanel);
        specSheet.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        specSheet.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        repaint();
        pack();
        setVisible(true);
    }

    /**
     * Retrieves the singleton instance of ItemDetailsView.
     * @return The singleton instance of ItemDetailsView.
     */
    public static ItemDetailsView GetInstance() {
        return singletonItemDetails;
    }

    // Private fields

    /** JLabel to display item name */
    private final JLabel itemNameLabel = new JLabel();

    /** JLabel to display item description */
    private final JLabel itemDescriptionLabel = new JLabel();

    /** JLabel to display seller name */
    private final JLabel sellerNameLabel = new JLabel();

    /** JLabel to display item price */
    private final JLabel priceTag = new JLabel();

    /** JLabel to display stock count */
    private final JLabel stockCount = new JLabel();

    /** JScrollPane to contain item specifications */
    private final JScrollPane specSheet = new JScrollPane();

    /** Singleton instance of ItemDetailsView */
    private final static ItemDetailsView singletonItemDetails = new ItemDetailsView();
}

