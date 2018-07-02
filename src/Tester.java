import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

public class Tester extends JFrame implements ActionListener{
    JLabel label;

    public static void main(String[] args){
        Tester test = new Tester("SwingTest");

        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setVisible(true);
    }

    Tester(String title){
        setTitle(title);
        setBounds( 10, 10, 300, 200);

        label = new JLabel("");
        label.setHorizontalAlignment(JLabel.CENTER);

        JButton btn1 = new JButton("Button 1");
        btn1.addActionListener(this);
        btn1.setActionCommand("Button 1");

        JButton btn2 = new JButton("Button 2");
        btn2.addActionListener(this);
        btn2.setActionCommand("Button 2");

        JPanel p = new JPanel();
        p.add(btn1);
        p.add(btn2);

        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(label, BorderLayout.PAGE_END);
    }

    public void actionPerformed(ActionEvent e){
        String cmd = e.getActionCommand();

        if (cmd.equals("Button 1")){
            label.setText("ボタン1が押されました");
        }else if (cmd.equals("Button 2")){
            label.setText("ボタン2が押されました");
        }
    }
}