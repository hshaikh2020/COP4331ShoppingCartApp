package cop4331.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The ErrorView class represents a view for displaying error messages in a graphical user interface.
 * This class extends JFrame and provides methods to show error messages and success statuses.
 * It follows the Singleton design pattern to maintain a single instance throughout the application.
 * @author S Hassan Shaikh
 * @author Austin Vasquez
 * @author Divyesh Mangapuram
 * @author Jorge Martinez
 */
public class ErrorView extends JFrame {
    /**
     * Private constructor to prevent external instantiation.
     */
    private ErrorView() {
        super();
        errorMessage = new JLabel();
        setLayout(new BorderLayout());
        add(errorMessage, BorderLayout.CENTER);
        JButton OkButton = new JButton("OK");
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
     * Displays an error message in the ErrorView.
     * @param message The error message to be displayed.
     */
    public void ShowError(String message) {
        setTitle("An error has occurred!");
        errorMessage.setText(message);
        errorMessage.repaint();
        pack();
        setVisible(true);
    }

    /**
     * Displays a success status message in the ErrorView.
     */
    public void showStatusSuccess() {
        setTitle("Success!");
        errorMessage.setText("Action was successful!");
        errorMessage.repaint();
        pack();
        setVisible(true);
    }

    /**
     * Retrieves the singleton instance of ErrorView.
     * @return The singleton instance of ErrorView.
     */
    public static ErrorView GetInstance() {
        return singletonErrorView;
    }

    // Private fields

    /** JLabel to display error messages */
    private final JLabel errorMessage;

    /** Singleton instance of ErrorView */
    private final static ErrorView singletonErrorView = new ErrorView();
}
