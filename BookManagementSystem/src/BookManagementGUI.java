import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BookManagementGUI extends JFrame {
    private BookDAO dao = new BookDAO();
    private DefaultTableModel tableModel;
    private JTable table;

    public BookManagementGUI() {
        setTitle("Book Management System - JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField priceField = new JTextField();

        formPanel.add(new JLabel("Book ID:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(new JLabel("Author:"));
        formPanel.add(authorField);
        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete by ID");
        formPanel.add(addButton);
        formPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(new String[]{"ID", "Title", "Author", "Price"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Search
        JPanel searchPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search Title");
        JButton refreshButton = new JButton("Refresh");
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        add(searchPanel, BorderLayout.SOUTH);

        // Actions
        addButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String title = titleField.getText();
                String author = authorField.getText();
                double price = Double.parseDouble(priceField.getText());

                dao.addBook(new Book(id, title, author, price));
                loadBooks();
                JOptionPane.showMessageDialog(this, "Book added.");
                idField.setText(""); titleField.setText(""); authorField.setText(""); priceField.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!");
            }
        });

        deleteButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter Book ID to delete:"));
                dao.deleteBook(id);
                loadBooks();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID!");
            }
        });

        searchButton.addActionListener(e -> {
            String title = searchField.getText();
            ArrayList<Book> books = dao.searchByTitle(title);
            updateTable(books);
        });

        refreshButton.addActionListener(e -> loadBooks());

        loadBooks();
        setVisible(true);
    }

    private void loadBooks() {
        ArrayList<Book> books = dao.getAllBooks();
        updateTable(books);
    }

    private void updateTable(ArrayList<Book> books) {
        tableModel.setRowCount(0);
        for (Book b : books) {
            tableModel.addRow(new Object[]{b.getId(), b.getTitle(), b.getAuthor(), b.getPrice()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookManagementGUI::new);
    }
}

