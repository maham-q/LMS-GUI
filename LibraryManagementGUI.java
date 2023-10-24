import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;

interface Configuration {
    void displayInfo();
}

class Item implements Configuration {
    public String title;
    public static int nextId = 1;
    public int id;
    public int popularity;
    public boolean status;

    public Item(String title) {
        this.title = title;
        id = nextId++;
        popularity = 0;
        status = false;
    }

    public void setTitle(String t) {
        title = t;
    }

    public void setStatus(boolean s) {
        status = s;
    }

    public boolean getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getID() {
        return id;
    }

    public String getType() {
        return " item";
    }

    @Override
    public void displayInfo() {
        System.out.println("ID: " + id);
        System.out.println("Popularity Count: " + popularity);
        System.out.println("Title: " + title);

    }
}

class Book extends Item {

    public String author;
    public int year;

    public Book(String t, String a, int y) {
        super(t);
        author = a;
        year = y;
    }

    public void setBook(Book b) {
        setAuthor(b.author);
        setTitle(b.title);
        setYear(b.year);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getType() {
        return "book";
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    @Override
    public void displayInfo() {
        System.out.println("----------Book----------");
        super.displayInfo();
        System.out.println("Author: " + getAuthor());
        System.out.println("Year: " + getYear());
    }

}

class Library {
    public ArrayList<Item> itemList;

    public Library() {
        itemList = new ArrayList<>();
    }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void deleteItem(int id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getID() == id) {
                itemList.remove(i);
                return;
            }
        }
        System.out.println("Item not Found");
    }

    public void updateItem(Item i1, Item i2) {
        Scanner obj = new Scanner(System.in);
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getTitle() == i1.getTitle()) {
                itemList.set(i, i2);
                return;
            }
        }
        System.out.println("Item not found in the record");
        obj.close();
    }

    public void displayItem(Item item) {
        item.displayInfo();
    }

    public void hotPicks() {
        ArrayList<Item> hotList = itemList;
        hotList.sort(Comparator.comparingInt(Item::getPopularity).reversed());
        System.out.println("-----Hot Picks-----");
        for (int i = 0; i < hotList.size(); i++) {
            hotList.get(i).displayInfo();
        }
    }

    public void readData() {
        try {
            File myfile = new File("data.txt");
            Scanner obj = new Scanner(myfile);
            while (obj.hasNextLine()) {
                String data = obj.nextLine();
                System.out.println(data);
            }
            obj.close();
        } catch (Exception e) {
            System.err.println("Error reading file.");
        }
    }

    public void saveData(String s1) throws FileNotFoundException {
        File myfile = new File("data.txt");
        Scanner obj = new Scanner(myfile);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(myfile))) {
            bw.write(s1);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to write on file");
            ex.printStackTrace();
        }
    }
}

public class LibraryManagementGUI extends JFrame {
    Scanner obj = new Scanner(System.in);
    Library lib = new Library();
    JTextField titleF = new JTextField(50);
    JTextField authorF = new JTextField(50);
    JTextField yearF = new JTextField(4);
    JPanel mainP = new JPanel();
    DefaultTableModel tableModel = new DefaultTableModel();
    JTable table = new JTable(tableModel);

    public LibraryManagementGUI() {
        mainP.setLayout(new BorderLayout());
        mainP.add(table);
    }

    public void setTitle() {
        setTitle("Library Management System");
        setSize(1000, 500);
        setVisible(true);

    }

    public void setAddButton() {
        JButton addButton = new JButton("Add Item");
        mainP.add(addButton);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleF.getText();
                String author = authorF.getText();
                String year = yearF.getText();
                Book book = new Book(title, author, Integer.parseInt(year));
                lib.addItem(book);
                String row[] = { title, author, year };
                tableModel.addRow(row);
                JOptionPane.showMessageDialog(null, "Item Added Successfully");
            }
        });

    }

    public void setDeleteButton() {
        JButton deleteButton = new JButton("Delete Item");
        mainP.add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    int selectedIndex = table.getSelectedRow();
                    tableModel.removeRow(table.getSelectedRow());
                    lib.deleteItem(selectedIndex);
                    JOptionPane.showMessageDialog(null, "Item Deleted Successfully!");
                }
            }
        });
    }

    public void setEditButton() {
        JButton editButton = new JButton("Edit Item");
        mainP.add(editButton);
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    int selectedIndex = table.getSelectedRow();
                    String oldTitle = (String) tableModel.getValueAt(selectedIndex, 0);
                    String oldAuthor = (String) tableModel.getValueAt(selectedIndex, 1);
                    String oldYear = (String) tableModel.getValueAt(selectedIndex, 2);
                    String newTitle = JOptionPane.showInputDialog(null, "Enter New Title: ");
                    String newAuthor = JOptionPane.showInputDialog(null, "Enter New Author: ");
                    String newYear = JOptionPane.showInputDialog(null, "Enter New Year Published: ");
                    Book bookToBeUpdated = new Book(oldTitle, oldAuthor, Integer.parseInt(oldYear));
                    Book updatedBook = new Book(newTitle, newAuthor, Integer.parseInt(newYear));
                    lib.updateItem(bookToBeUpdated, updatedBook);
                    Object[] newData = { newTitle, newAuthor, newYear };
                    tableModel.setValueAt(newData, selectedIndex, 0);
                    JOptionPane.showMessageDialog(null, "Item Updated Successfully!");

                }

            }
        });
    }

    public void setReadButton() {
        JButton readButton = new JButton("Read Item");
        mainP.add(readButton);
        readButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (table.getSelectedRow() >= 0) {
                    int selectedIndex = table.getSelectedRow();
                }
            }
        });
    }

    public static void main(String[] args) {
        

    }
}