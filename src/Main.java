import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Main implements ActionListener {
    private JTextField userIdTextField;
    private JTextField userPwTextField;

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }

    public void start() {
        JFrame flame = new JFrame("log in");
        flame.setVisible(true);

        JPanel idPanel = new JPanel();
        JPanel pwPanel = new JPanel();

        JButton logInButton = new JButton("ログイン");
        logInButton.addActionListener(this);

        userIdTextField = new JTextField();
        userPwTextField = new JTextField();

        idPanel.setLayout(new BorderLayout());
        idPanel.add(new JLabel("USER ID"), BorderLayout.NORTH);
        idPanel.add(userIdTextField, BorderLayout.SOUTH);

        pwPanel.setLayout(new BorderLayout());
        pwPanel.add(new JLabel("USER PASSWORD"), BorderLayout.NORTH);
        pwPanel.add(userPwTextField, BorderLayout.SOUTH);

        userIdTextField.setPreferredSize(new Dimension(200, userIdTextField.getPreferredSize().height));
        userPwTextField.setPreferredSize(new Dimension(200, userPwTextField.getPreferredSize().height));

        flame.setDefaultCloseOperation(EXIT_ON_CLOSE);

        flame.getContentPane().add(idPanel, BorderLayout.NORTH);
        flame.getContentPane().add(pwPanel, BorderLayout.CENTER);
        flame.getContentPane().add(logInButton, BorderLayout.SOUTH);

        flame.setBounds(0, 0, 350, 160);

    }

    public void actionPerformed(ActionEvent event) {
        String id = userIdTextField.getText();
        String pw = userPwTextField.getText();

        System.out.println(id);
        System.out.println(pw);
    }

}
