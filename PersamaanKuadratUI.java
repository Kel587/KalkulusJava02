import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PersamaanKuadratUI extends JFrame implements ActionListener {
    private JTextField aField, bField, cField, hasilField, x1Field, x2Field, xLimitField;
    private JTextArea langkahArea;
    private JComboBox<String> pilihanComboBox;

    public PersamaanKuadratUI() {
        setTitle("Kalkulator Persamaan Kuadrat dan Limit");
        setSize(450, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel utama
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Label dan input untuk pilihan
        JLabel pilihanLabel = new JLabel("Pilihan:");
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(pilihanLabel, constraints);

        pilihanComboBox = new JComboBox<>(new String[]{"Persamaan Kuadrat", "Limit"});
        pilihanComboBox.addActionListener(this);
        constraints.gridx = 1;
        panel.add(pilihanComboBox, constraints);

        // Label dan input untuk nilai a, b, c
        String[] labelNames = {"Nilai a:", "Nilai b:", "Nilai c:"};
        JTextField[] fields = new JTextField[labelNames.length];
        for (int i = 0; i < labelNames.length; i++) {
            JLabel label = new JLabel(labelNames[i]);
            constraints.gridx = 0;
            constraints.gridy = i + 1;
            panel.add(label, constraints);
            fields[i] = new JTextField(10);
            constraints.gridx = 1;
            panel.add(fields[i], constraints);
        }
        aField = fields[0];
        bField = fields[1];
        cField = fields[2];

        // Label dan input untuk nilai x pada limit
        JLabel labelXLimit = new JLabel("Nilai x:");
        constraints.gridx = 0;
        constraints.gridy = 4; // Pindahkan ke bawah nilai c
        panel.add(labelXLimit, constraints);
        xLimitField = new JTextField(10);
        constraints.gridx = 1;
        panel.add(xLimitField, constraints);

        // Tombol Hitung
        JButton hitungButton = new JButton("Hitung");
        hitungButton.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(hitungButton, constraints);

        // Tombol Clear
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(clearButton, constraints);

        // Field untuk menampilkan hasil
        hasilField = createResultField(panel, constraints, "Hasil:", 7);
        x1Field = createResultField(panel, constraints, "Nilai x1:", 8);
        x2Field = createResultField(panel, constraints, "Nilai x2:", 9);

        // Area untuk menampilkan langkah-langkah
        langkahArea = new JTextArea(10, 20);
        langkahArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(langkahArea);
        constraints.gridx = 0;
        constraints.gridy = 10;
        constraints.gridwidth = 2;
        panel.add(scrollPane, constraints);

        // Label untuk menampilkan rumus persamaan kuadrat dan limit umum
        createEquationLabel(panel, constraints, "Persamaan Kuadrat:", 11);
        createEquationLabel(panel, constraints, "Limit Umum:", 12);

        // Text untuk menampilkan rumus persamaan kuadrat
        createEquationText(panel, constraints, "ax^2 + bx + c = 0", 11);

        // Text untuk menampilkan rumus limit umum
        createEquationText(panel, constraints, "lim (f(x)) = lim (ax^2 + bx + c)", 12);

        // Menambahkan panel ke frame
        add(panel);

        setVisible(true);
    }

    private JTextField createResultField(JPanel panel, GridBagConstraints constraints, String labelText, int gridY) {
        JLabel label = new JLabel(labelText);
        constraints.gridx = 0;
        constraints.gridy = gridY;
        panel.add(label, constraints);
        JTextField field = new JTextField(20);
        field.setEditable(false);
        constraints.gridx = 1;
        constraints.gridy = gridY;
        panel.add(field, constraints);
        return field;
    }

    private void createEquationLabel(JPanel panel, GridBagConstraints constraints, String labelText, int gridY) {
        JLabel label = new JLabel(labelText);
        constraints.gridx = 0;
        constraints.gridy = gridY;
        constraints.gridwidth = 1;
        panel.add(label, constraints);
    }

    private void createEquationText(JPanel panel, GridBagConstraints constraints, String text, int gridY) {
        JLabel label = new JLabel(text);
        constraints.gridx = 1;
        constraints.gridy = gridY;
        constraints.gridwidth = 1;
        panel.add(label, constraints);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedOption = (String) pilihanComboBox.getSelectedItem();

        switch (e.getActionCommand()) {
            case "Hitung":
                if (selectedOption.equals("Persamaan Kuadrat")) {
                    hitungPersamaanKuadrat();
                } else if (selectedOption.equals("Limit")) {
                    hitungLimit();
                }
                break;
            case "Clear":
                clearInput();
                break;
            default:
                if (selectedOption.equals("Persamaan Kuadrat")) {
                    xLimitField.setEditable(false);
                } else if (selectedOption.equals("Limit")) {
                    xLimitField.setEditable(true);
                }
                break;
        }
    }

    private void hitungPersamaanKuadrat() {
        double a, b, c;
        try {
            a = Double.parseDouble(aField.getText());
            b = Double.parseDouble(bField.getText());
            c = Double.parseDouble(cField.getText());
        } catch (NumberFormatException ex) {
            showErrorDialog("Masukkan hanya angka!");
            return;
        }

        double diskriminan = b * b - 4 * a * c;

        StringBuilder langkahLangkah = new StringBuilder("Langkah-langkah perhitungan:\n");
        langkahLangkah.append("Diskriminan = b^2 - 4ac\n");
        langkahLangkah.append("            = ").append(b).append("^2 - 4 * ").append(a).append(" * ").append(c).append("\n");
        langkahLangkah.append("            = ").append(diskriminan).append("\n\n");

        if (diskriminan < 0) {
            langkahLangkah.append("Karena diskriminan negatif, persamaan tidak memiliki akar real.");
            displayResults("", "", "Persamaan tidak memiliki akar real", langkahLangkah.toString());
        } else if (diskriminan == 0) {
            double x1 = -b / (2 * a);
            langkahLangkah.append("Akar = -b / (2a)\n");
            langkahLangkah.append("     = ").append(-b).append(" / (2 * ").append(a).append(")\n");
            langkahLangkah.append("     = ").append(x1);
            displayResults(String.format("%.2f", x1), "", "Persamaan memiliki satu akar: " + String.format("%.2f", x1), langkahLangkah.toString());
        } else {
            double x1 = (-b + Math.sqrt(diskriminan)) / (2 * a);
            double x2 = (-b - Math.sqrt(diskriminan)) / (2 * a);
            langkahLangkah.append("Akar1 = (-b + √diskriminan) / (2a)\n");
            langkahLangkah.append("      = (").append(-b).append(" + √").append(diskriminan).append(") / (2 * ").append(a).append(")\n");
            langkahLangkah.append("      = ").append(x1).append("\n");
            langkahLangkah.append("Akar2 = (-b - √diskriminan) / (2a)\n");
            langkahLangkah.append("      = (").append(-b).append(" - √").append(diskriminan).append(") / (2 * ").append(a).append(")\n");
            langkahLangkah.append("      = ").append(x2);
            displayResults(String.format("%.2f", x1), String.format("%.2f", x2), "Persamaan memiliki dua akar: " + String.format("%.2f", x1) + " dan " + String.format("%.2f", x2), langkahLangkah.toString());
        }
    }

    private void hitungLimit() {
        double a, b, c, x;
        try {
            a = Double.parseDouble(aField.getText());
            b = Double.parseDouble(bField.getText());
            c = Double.parseDouble(cField.getText());
            x = Double.parseDouble(xLimitField.getText());
        } catch (NumberFormatException ex) {
            showErrorDialog("Masukkan hanya angka!");
            return;
        }

        double result = a * x * x + b * x + c;

        StringBuilder langkahLangkah = new StringBuilder("Langkah-langkah perhitungan limit:\n");
        langkahLangkah.append("Limit f(x) saat x mendekati ").append(x).append(" adalah\n");
        langkahLangkah.append("lim (f(x)) = lim (ax^2 + bx + c)\n");
        langkahLangkah.append("x->").append(x).append("       = a * lim (x^2) + b * lim (x) + c\n");
        langkahLangkah.append("           = a * ").append(x * x).append(" + b * ").append(x).append(" + c\n");
        langkahLangkah.append("           = ").append(result).append("\n");

        displayResults("", "", "Limit dari f(x) saat x mendekati " + x + " adalah " + String.format("%.2f", result), langkahLangkah.toString());
    }

    private void displayResults(String x1Result, String x2Result, String mainResult, String langkahLangkah) {
        x1Field.setText("x1 = " + x1Result);
        x2Field.setText("x2 = " + x2Result);
        hasilField.setText(mainResult);
        langkahArea.setText(langkahLangkah);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void clearInput() {
        JTextField[] fields = {aField, bField, cField, xLimitField, hasilField, x1Field, x2Field};
        for (JTextField field : fields) {
            field.setText("");
        }
        langkahArea.setText("");
    }

    public static void main(String[] args) {
        new PersamaanKuadratUI();
    }
}
