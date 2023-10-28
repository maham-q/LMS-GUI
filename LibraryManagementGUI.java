import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.*;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

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
        return "item";
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

    public String deleteItem(int id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getID() == id) {
                itemList.remove(i);
                return itemList.get(i).toString();
            }
        }
        System.out.println("Item not Found");
        return null;
    }

    public void updateItem(Item i1, Item i2) {
        for (int i = 0; i < itemList.size(); i++) {
            if (itemList.get(i).getTitle().equals(i1.getTitle())) {
                itemList.set(i, i2);
                return;
            }
        }
        System.out.println("Item not found in the record");
    }

    public void displayItem(Item item) {
        item.displayInfo();
    }

    public void hotPicks() {
        ArrayList<Item> hotList = new ArrayList<>(itemList);
        hotList.sort(Comparator.comparingInt(Item::getPopularity).reversed());
        System.out.println("-----Hot Picks-----");
        for (Item item : hotList) {
            item.displayInfo();
        }
    }

    public void saveData(String[] s1) {
        String name = "data.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
            for (String line : s1) {
                bw.write(line + " ");
            }
            bw.newLine();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleteData(String[] s1) {
        String name = "data.txt";
        List<String> dl = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                dl.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ;
        List<String> ul = new ArrayList<>();
        for (String line : dl) {
            boolean check = false;
            for (String str : s1) {
                if (line.contains(str)) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                ul.add(line);
            }
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(name))) {
            for (String line : ul) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public void editData(String[] s1, String[] s2) {
        String name = "data.txt";
        List<String> dl = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                dl.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ;
        List<String> ul = new ArrayList<>();
        for (String line : dl) {
            for (int i = 0; i < s1.length; i++) {
                if (line.contains(s1[i])) {
                    line = line.replace(s1[i], s2[i]);
                }
            }
            ul.add(line);
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(name))) {
            for (String line : ul) {
                bw.write(line);
                bw.newLine();
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

class ButtonCellRenderer extends JButton implements TableCellRenderer {
    public ButtonCellRenderer() {
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        setText("read");
        return this;
    }
}

class ButtonCellEditor extends DefaultCellEditor {
    private JButton readButton = new JButton();
    private int popularityCount = 0;

    public ButtonCellEditor() {
        super(new JCheckBox());

        readButton.addActionListener(new ActionListener() {
            JTextArea ta = new JTextArea();
            JDialog dialog;
            public void actionPerformed(ActionEvent e) {
                String name = "book.txt";
                popularityCount++;
                try (BufferedReader br = new BufferedReader(new FileReader(name))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        ta.append(line + "\n");
                    }
                    JOptionPane.showMessageDialog(null, new JScrollPane(ta));
                } 
                catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error reading the file.");
                }
                int x = JOptionPane.showConfirmDialog(readButton, "Do you want to exit reading book?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
                if (x == JOptionPane.YES_OPTION) {
                    if(dialog!=null){
                        dialog.dispose();
                    }
                }
            }

            
        });
        editorComponent = readButton;
    }
}
public class LibraryManagementGUI extends JFrame {
    Library lib = new Library();
    JTextField titleF = new JTextField(50);
    JTextField authorF = new JTextField(50);
    JTextField yearF = new JTextField(4);
    JPanel mainP = new JPanel();
    DefaultTableModel tableModel;
    JTable table;

    public LibraryManagementGUI() {
        setVisible(true);
        setSize(900, 400);
        setTitle("Library Management System");

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane jsp = new JScrollPane(table);
        mainP.add(jsp, BorderLayout.CENTER);

        table.setLayout(new FlowLayout());
        tableModel.addColumn("Title");
        tableModel.addColumn("Author");
        tableModel.addColumn("Year of Publication");
        tableModel.addColumn("Read");
        JPanel tp = new JPanel(new GridLayout(3, 0));
        tp.add(new JLabel("Title"));
        tp.add(titleF);
        tp.add(new JLabel("Author"));
        tp.add(authorF);
        tp.add(new JLabel("Year of Publication"));
        tp.add(yearF);

        String name = "data.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(name))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(" ");
                tableModel.addRow(row);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        table.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int r = table.rowAtPoint(e.getPoint());
                if (r >= 0) {
                    table.setSelectionBackground(Color.magenta);
                }
            }

        });

        JButton addButton = new JButton("Add Item");
        table.getColumnModel().getColumn(3).setCellRenderer(new ButtonCellRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(new ButtonCellEditor());
       

        mainP.add(addButton, BorderLayout.SOUTH);
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int result = JOptionPane.showConfirmDialog(null, tp, "Enter Item Information",
                        JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String title = titleF.getText();
                    String author = authorF.getText();
                    String year = yearF.getText();
                    Book book = new Book(title, author, Integer.parseInt(year));
                    lib.addItem(book);
                    String[] row = { title, author, year };
                    tableModel.addRow(row);

                    JOptionPane.showMessageDialog(null, "Item Added Successfully");
                    lib.saveData(row);
                }
            }

        });

        JButton deleteButton = new JButton("Delete Item");
        mainP.add(deleteButton, BorderLayout.SOUTH);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String deletedRow = JOptionPane.showInputDialog("Enter Title to Book You want to Delete");
                if (table.getSelectedRow() >= 0) {
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String Title = tableModel.getValueAt(i, 0).toString();
                        String Author = tableModel.getValueAt(i, 1).toString();
                        String Year = tableModel.getValueAt(i, 2).toString();
                        if (Title.equals(deletedRow)) {
                            tableModel.removeRow(i);
                            String[] row = { Title, Author, Year };
                            lib.deleteData(row);
                            break;
                        }
                    }
                }
            }
        });

        JButton editButton = new JButton("Edit Item");
        mainP.add(editButton, BorderLayout.PAGE_END);
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
                    tableModel.setValueAt(newTitle, selectedIndex, 0);
                    tableModel.setValueAt(newAuthor, selectedIndex, 1);
                    tableModel.setValueAt(newYear, selectedIndex, 2);
                    String[] s1 = { oldTitle, oldAuthor, oldYear };
                    String[] s2 = { newTitle, newAuthor, newYear };
                    lib.editData(s1, s2);
                    JOptionPane.showMessageDialog(null, "Item Updated Successfully!");
                }
            }
        });
        JButton popularityButton = new JButton("View Popularity");
        mainP.add(popularityButton, BorderLayout.SOUTH);
        // popularityButton.addActionListener(new ActionListener(){
        // public void actionPerformed(ActionEvent e){
        // CategoryDataset dataset = createDataset();
        // JFreeChart chart=ChartFactory.createBarChart(
        // "Popularity Chart",
        // "Book Title",
        // "Popularity Count"
        // dataset,
        // PlotOrientation.VERTICAL,
        // true,true,false
        // );

        // }

        // private CategoryDataset createDataset() {
        // return null;
        // }
        // });

        add(mainP);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryManagementGUI().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }

}
