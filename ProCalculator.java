import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ProCalculator extends JFrame implements ActionListener {
    private final JLabel expressionLabel = new JLabel("", SwingConstants.RIGHT);
    private final JTextField display = new JTextField("0");

    private double result = 0;
    private String lastOp = "=";
    private boolean startNewNumber = true;

    public ProCalculator() {
        setTitle("Professional Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(370, 480);
        setLocationRelativeTo(null);


        expressionLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        expressionLabel.setForeground(Color.GRAY);
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);

   
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("SansSerif", Font.BOLD, 32));
        display.setEditable(false);
        display.setBackground(Color.WHITE);

       
        String[] buttons = {
            "AC", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        JPanel buttonPanel = new JPanel(new GridLayout(5, 4, 8, 8));
        for (String b : buttons) {
            JButton btn = new JButton(b);
            btn.setFont(new Font("SansSerif", Font.PLAIN, 22));
            btn.addActionListener(this);
            buttonPanel.add(btn);
        }

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.add(expressionLabel);
        topPanel.add(display);

        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        main.add(topPanel, BorderLayout.NORTH);
        main.add(buttonPanel, BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

 
    public void actionPerformed(ActionEvent e) {
        String cmd = ((JButton) e.getSource()).getText();

        if ("0123456789.".contains(cmd)) {
            if (startNewNumber) {
                display.setText(cmd.equals(".") ? "0." : cmd);
                startNewNumber = false;
            } else {
                if (cmd.equals(".") && display.getText().contains(".")) return;
                display.setText(display.getText() + cmd);
            }
            return;
        }

        if (cmd.equals("AC")) {
            result = 0;
            lastOp = "=";
            display.setText("0");
            expressionLabel.setText("");
            startNewNumber = true;
            return;
        }

        if (cmd.equals("⌫")) {
            if (!startNewNumber) {
                String t = display.getText();
                if (t.length() <= 1) {
                    display.setText("0");
                    startNewNumber = true;
                } else {
                    display.setText(t.substring(0, t.length() - 1));
                }
            }
            return;
        }

        if (cmd.equals("±")) {
            if (!display.getText().equals("0")) {
                if (display.getText().startsWith("-"))
                    display.setText(display.getText().substring(1));
                else
                    display.setText("-" + display.getText());
            }
            return;
        }

 
        if (cmd.equals("%")) {
            try {
                double val = Double.parseDouble(display.getText()) / 100;
                display.setText(removeTrailingDotZero(val));
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
            return;
        }

        
        try {
            double x = Double.parseDouble(display.getText());
            calculate(x);

            if (cmd.equals("=")) {
                expressionLabel.setText(expressionLabel.getText() + " " + removeTrailingDotZero(x) + " =");
                display.setText(removeTrailingDotZero(result));
                lastOp = "=";
            } else {
            
                if (lastOp.equals("="))
                    expressionLabel.setText(removeTrailingDotZero(x) + " " + cmd);
                else
                    expressionLabel.setText(expressionLabel.getText() + " " + removeTrailingDotZero(x) + " " + cmd);

                lastOp = cmd;
            }

            startNewNumber = true;
            display.setText(removeTrailingDotZero(result));

        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private void calculate(double x) {
        switch (lastOp) {
            case "+": result += x; break;
            case "-": result -= x; break;
            case "×": result *= x; break;
            case "÷":
                if (x == 0) {
                    display.setText("Divide by 0");
                    result = 0;
                    lastOp = "=";
                    startNewNumber = true;
                    return;
                }
                result /= x;
                break;
            case "=": result = x;
            break;
        }
    }


    private String removeTrailingDotZero(double v) {
        if (v == (long) v) return String.format("%d", (long) v);
        else return String.valueOf(v);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProCalculator::new);
    }
}
