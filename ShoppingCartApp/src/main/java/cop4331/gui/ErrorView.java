package cop4331.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author S Hassan Shaikh
 */
public class ErrorView extends JFrame{
    private ErrorView()
    {
        super();
        errorMessage = new JLabel();
        setLayout(new BorderLayout());
        add(errorMessage,BorderLayout.CENTER);
        JButton OkButton = new JButton("OK");
        OkButton.addActionListener((ActionEvent e) -> {this.setVisible(false);});
        add(OkButton,BorderLayout.PAGE_END);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        OkButton.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent theKey) {
                if(theKey.getExtendedKeyCode()==KeyEvent.VK_ENTER)
                {OkButton.doClick();}
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
    }
    
    public void ShowError(String message)
    {
        setTitle("An error has occured!");
        errorMessage.setText(message);
        errorMessage.repaint();
        pack();
        setVisible(true);
    }
    
    public void showStatusSuccess()
    {
        setTitle("Success!");
        errorMessage.setText("Action was successful!");
        errorMessage.repaint();
        pack();
        setVisible(true);
    }
    
    public static ErrorView GetInstance()
    {
        return singletonErrorView;
    }
    
    private final JLabel errorMessage;
    private final static ErrorView singletonErrorView = new ErrorView();
}
