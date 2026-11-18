package mattresssales;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ProductApp extends JFrame {

    private final JTextField txtCustomerName = new JTextField(20);
    private final JComboBox<String> cmbProduct = new JComboBox<>(new String[]{
            "Cozy Nights Monarch", "Restonic Barbados", "Sealy Phuket"});
    private final JTextField txtPrice = new JTextField(20);
    private final JButton btnProcess = new JButton("PROCESS");

    public ProductApp() {
        super("Product App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);

        setJMenuBar(createMenuBar());

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 20));
        form.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        form.add(new JLabel("Customer Name:"));
        form.add(txtCustomerName);
        form.add(new JLabel("Select Product:"));
        form.add(cmbProduct);
        form.add(new JLabel("Enter Price:"));
        form.add(txtPrice);
        form.add(new JLabel(""));
        form.add(btnProcess);
        add(form);

        btnProcess.addActionListener(e -> processSale());
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu toolsMenu = new JMenu("Tools");
        JMenuItem processItem = new JMenuItem("Process");
        processItem.addActionListener(e -> processSale());   //the same logic as the button
        JMenuItem clearItem = new JMenuItem("Clear");
        clearItem.addActionListener(e -> clearForm());
        toolsMenu.add(processItem);
        toolsMenu.add(clearItem);

        menuBar.add(fileMenu);
        menuBar.add(toolsMenu);
        return menuBar;
    }

    private void processSale() {
        String customer = txtCustomerName.getText().trim();
        String product = (String) cmbProduct.getSelectedItem();

        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter the customer name.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double price;
        try {
            price = Double.parseDouble(txtPrice.getText().trim());
            if (price <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid price.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProductCalculations calc = new ProductCalculations(price);
        double vat = calc.GetVat();
        double commission = calc.GetCommission();
        double total = calc.GetTotal();

        String message = "Customer Name: " + customer
                + "\nProduct: " + product
                + "\nVAT: R " + vat
                + "\nCOMMISSION: R " + commission
                + "\nTOTAL: R " + total;

        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
        saveToFile(customer, product, vat, commission, total);
    }

    private void saveToFile(String customer, String product,
                            double vat, double commission, double total) {
        File file = new File("data.txt");
        boolean isNew = !file.exists() || file.length() == 0;

        try (PrintWriter out = new PrintWriter(new FileWriter(file, true))) {
            if (isNew) {
                out.println("DATA LOG");
            }
            out.println("*".repeat(30));
            out.println("Customer Name: " + customer);
            out.println("Product: " + product);
            out.println("VAT: R " + vat);
            out.println("COMMISSION: R " + commission);
            out.println("TOTAL: R " + total);
            out.println("*".repeat(30));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Could not save to data.txt: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        txtCustomerName.setText("");
        txtPrice.setText("");
        cmbProduct.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductApp().setVisible(true));
    }
}