import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
class UserData {
    static String username = null;
    static String password = null;
}
class UserLogin extends JFrame implements ActionListener {
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JPanel contentPane;
    public UserLogin() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Login to Database");
        lblNewLabel.setForeground(Color.WHITE);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 42));
        lblNewLabel.setBounds(350, 13, 500, 93);
        contentPane.setBackground(Color.decode("#d2346b"));
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 24));
        textField.setBounds(440, 170, 281, 40);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 24));
        passwordField.setBounds(440, 286, 281, 40);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username: ");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblUsername.setBounds(280, 166, 193, 40);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password: ");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblPassword.setBounds(287, 284, 193, 40);
        contentPane.add(lblPassword);


        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        loginButton.setBounds(420, 392, 160, 50);
        loginButton.setForeground(Color.DARK_GRAY);
        contentPane.add(loginButton);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        UserData.username = textField.getText();
        UserData.password = passwordField.getText();
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection(url,UserData.username,UserData.password);
            if(con != null) {
                JOptionPane.showMessageDialog(this, "Login Successful..", "Logged In", JOptionPane.PLAIN_MESSAGE);
                this.dispose();
                new Views();
                con.close();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Internal Database Connection Error..", "Error!", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Wrong Credentials Entered..", "Login Denied!..", JOptionPane.WARNING_MESSAGE);
        }
    }
}
class Views extends JFrame implements ActionListener {
    JButton sellButton;
    JButton buyButton;
    public Views() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.decode("#d31568"));

        JLabel label = new JLabel("Choose View");
        label.setForeground(Color.decode("#efe8eb"));
        label.setFont(new Font("Consolas", Font.BOLD, 36));
        label.setBounds(370,100,240,60);
        contentPane.add(label);

        buyButton = new JButton();
        buyButton.setBounds(220, 170, 260, 260);
        buyButton.setOpaque(true);
        buyButton.setContentAreaFilled(false);
        buyButton.setBorderPainted(false);
        buyButton.setFocusPainted(false);
        buyButton.addActionListener(this);
        buyButton.addActionListener(this);
        buyButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            buyButton.setIcon(new ImageIcon(this.getClass().getResource("/resources/buyerView.png")));
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        contentPane.add(buyButton);

        sellButton = new JButton();
        sellButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        sellButton.setBounds(520, 170, 260, 260);
        sellButton.setOpaque(true);
        sellButton.setContentAreaFilled(false);
        sellButton.setBorderPainted(false);
        sellButton.setFocusPainted(false);
        sellButton.addActionListener(this);
        sellButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        try {
            sellButton.setIcon(new ImageIcon(this.getClass().getResource("/resources/sellerView.png")));
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }
        contentPane.add(sellButton);
        contentPane.add(new JButton()); // false button

        this.setTitle("Choose View");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(270,130,1000,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == sellButton) {
            this.dispose();
            new SellerView();
        } else if (e.getSource() == buyButton) {
            this.dispose();
            new BuyerView();
        }
    }
}
class BuyerView extends JFrame implements ActionListener, MouseListener {
    JButton switchViewButton;
    JButton viewCartButton;
    JTable table;
    Integer availQuantity;
    Integer reqQuantity;
    ArrayList<String> infoList;
    public BuyerView() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.decode("#d31568"));

        Products ob = new Products();
        ArrayList<Products> list = ob.getList();
        Object [][]data = new Object[list.size()][6];
        for(int i=0;i<data.length;i++) {
            data[i][0] = list.get(i).getPid();
            data[i][1] = list.get(i).getpName();
            data[i][2] = list.get(i).getcName();
            data[i][3] = list.get(i).getQuantity();
            data[i][4] = list.get(i).getPrice();
            data[i][5] = list.get(i).getInfo();
        }
        JLabel label = new JLabel("Click on a Product to Add to Cart");
        label.setFont(new Font("Verdana", Font.PLAIN, 20));
        label.setBounds(200,0,1000,40);
        contentPane.add(label);
        label.setBackground(Color.decode("#879cbd"));
        label.setForeground(Color.decode("#ffffff"));

        String []header = {"Product-Id","Product-Name","Category","Quantity","Price","Description"};
        table = new JTable(data,header);
        table.setFont(new Font("Verdana", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.addMouseListener(this);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Verdana", Font.BOLD, 20));
        tableHeader.setBounds(200,40,1000,40);
        table.setBounds(200,80,1000,700);
        contentPane.add(tableHeader);
        contentPane.add(table);
        tableHeader.setBackground(Color.decode("#879cbd"));
        tableHeader.setForeground(Color.decode("#ffffff"));
        tableHeader.setResizingAllowed(false);

        viewCartButton = new JButton("View Cart");
        viewCartButton.setBounds(0,280,200,50);
        contentPane.add(viewCartButton);
        viewCartButton.setBackground(Color.decode("#053d45"));
        viewCartButton.setForeground(Color.decode("#ccd1cd"));
        viewCartButton.setFont(new Font("Optima", Font.PLAIN, 24));
        viewCartButton.setFocusPainted(false);
        viewCartButton.setOpaque(true);
        viewCartButton.setBorderPainted(false);
        viewCartButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewCartButton.addActionListener(this);

        switchViewButton = new JButton("Switch View");
        switchViewButton.setBounds(0,360,200,50);
        contentPane.add(switchViewButton);
        switchViewButton.setBackground(Color.decode("#053d45"));
        switchViewButton.setForeground(Color.decode("#ccd1cd"));
        switchViewButton.setFont(new Font("Optima", Font.PLAIN, 24));
        switchViewButton.setFocusPainted(false);
        switchViewButton.setOpaque(true);
        switchViewButton.setBorderPainted(false);
        switchViewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchViewButton.addActionListener(this);

        this.setTitle("Buyer View");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(170,80,1200,700);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == switchViewButton) {
            switchViewButton.setBackground(Color.decode("#012b31"));
            viewCartButton.setBackground(Color.decode("#053d45"));
            this.dispose();
            new Views();
        } else if (e.getSource() == viewCartButton) {
            switchViewButton.setBackground(Color.decode("#053d45"));
            viewCartButton.setBackground(Color.decode("#012b31"));
            new ViewCart().view();
        }
    }
    public ArrayList<String> getInfo(String val) {
        infoList = new ArrayList<>();
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{?=call getQuantity(?)}");
            cs.setString(2,val);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infoList;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TableModel model = table.getModel();
        int selectedRowIndex = table.getSelectedRow();
        //System.out.println(selectedRowIndex + " " + model.getValueAt(selectedRowIndex,0).toString());
        String pid = model.getValueAt(selectedRowIndex,0).toString();
        availQuantity = Integer.parseInt(model.getValueAt(selectedRowIndex,3).toString());
        reqQuantity = Integer.parseInt(JOptionPane.showInputDialog(this,"Enter Required Quantity"));
        if(reqQuantity > availQuantity || reqQuantity <= 0) {
            JOptionPane.showMessageDialog(this, "Enter Valid Quantity", "Couldn't add!!", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                java.sql.Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
                String cat = model.getValueAt(selectedRowIndex,2).toString();
                Integer cid = getCid(cat);
                System.out.println(pid+ " " + cid);
                Double price = Double.parseDouble(model.getValueAt(selectedRowIndex,4).toString());
                if(reqQuantity == availQuantity) {
                    CallableStatement cs = con.prepareCall("{call del(?,?,?,?)}");
                    cs.setInt(1,Integer.parseInt(pid));
                    cs.setInt(2,cid);
                    cs.setInt(3,reqQuantity);
                    cs.setDouble(4,price);
                    cs.execute();
                } else if(reqQuantity <= availQuantity) {
                    CallableStatement cs = con.prepareCall("{call decr(?,?,?,?)}");
                    cs.setInt(1,Integer.parseInt(pid));
                    cs.setInt(2,cid);
                    cs.setInt(3,reqQuantity);
                    cs.setDouble(4,price);
                    cs.execute();
                }
                JOptionPane.showMessageDialog(this, "SuccessFully Added to cart", "Added to Cart..", JOptionPane.PLAIN_MESSAGE);
                this.dispose();
                new BuyerView();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Couldn't Add..operation failed..", "Failed to Add!!", 2);
                ex.printStackTrace();
            }
        }
        System.out.println(reqQuantity > availQuantity);
    }
    public Integer getCid(String val) {
        Integer ccid = 0;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{?=call getCid(?)}");
            cs.setString(2,val);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            ccid = cs.getInt(1);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ccid;
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
class Cart {
    String pname;
    String cname;
    Integer qty;
    Double price;
    String about;
    public Cart() {
    }
    public Cart(String pname, String cname, Integer qty, Double price, String info) {
        this.pname = pname;
        this.cname = cname;
        this.qty = qty;
        this.price = price;
        this.about = info;
    }
    public String getPname() {
        return pname;
    }
    public String getCname() {
        return cname;
    }
    public Integer getQty() {
        return qty;
    }
    public Double getPrice() {
        return price;
    }
    public String getInfo() {
        return about;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "pname='" + pname + '\'' +
                ", cname='" + cname + '\'' +
                ", qty=" + qty +
                ", price=" + price +
                ", about='" + about + '\'' +
                '}';
    }

    ArrayList<Cart> list = new ArrayList<>();
    Connection con = null;
    ResultSet rs = null;
    public ArrayList<Cart> getList() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{call viewCart(?)}");
            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cs.execute();
            rs = (ResultSet)cs.getObject(1);
            while (rs.next()) {
                String pnm = rs.getString(1);
                String cnm = rs.getString(2);
                Integer qty = rs.getInt(3);
                Double cost = rs.getDouble(4);
                String info = rs.getString(5);
                list.add(new Cart(pnm, cnm, qty, cost, info));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
class ViewCart {
    Cart ob = new Cart();
    ArrayList<Cart> list = ob.getList();
    public void view() {
        Object [][]data = new Object[list.size()][5];
        for(int i=0;i<data.length;i++) {
            data[i][0] = list.get(i).getPname();
            data[i][1] = list.get(i).getCname();
            data[i][2] = list.get(i).getQty();
            data[i][3] = list.get(i).getPrice();
            data[i][4] = list.get(i).getInfo();
        }
        String []header = {"Product-Name","Category","Quantity","Price","Description"};
        JTable table = new JTable(data,header);
        table.setFont(new Font("Verdana", Font.PLAIN, 14));
        table.setRowHeight(30);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#879cbd"));
        tableHeader.setForeground(Color.decode("#ffffff"));
        tableHeader.setFont(new Font("Tahoma", Font.TRUETYPE_FONT,18));
        tableHeader.setResizingAllowed(false);
        JFrame frame = new JFrame();
        frame.setBounds(270,130,1000,600);
        frame.add(new JScrollPane(table));
        frame.setTitle("My Cart");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
class SellerView extends JFrame implements ActionListener, FocusListener {
    JButton switchViewButton;
    JButton viewProductsButton;
    JButton addProductButton;
    JComboBox selCategory;
    JLabel pId;
    JLabel cId;
    JLabel quantity;
    JLabel price;
    JLabel pName;
    JLabel description;
    JTextField inPid;
    JTextField inQuantity;
    JTextField inPrice;
    JTextField inPname;
    JTextArea info;
    String cName = "CLOTHING";
    int pid;
    int cid;
    int qty;
    double cost;
    String pnm = null;
    String about = null;
    public SellerView() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(Color.decode("#d31568"));

        viewProductsButton = new JButton("View All Products");
        viewProductsButton.setBounds(0,220,250,50);
        contentPane.add(viewProductsButton);
        viewProductsButton.setBackground(Color.decode("#053d45"));
        viewProductsButton.setForeground(Color.decode("#ccd1cd"));
        viewProductsButton.setFont(new Font("Optima", Font.PLAIN, 24));
        viewProductsButton.setFocusPainted(false);
        viewProductsButton.setOpaque(true);
        viewProductsButton.setBorderPainted(false);
        viewProductsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        viewProductsButton.addActionListener(this);

        switchViewButton = new JButton("Switch View");
        switchViewButton.setBounds(0,300,250,50);
        contentPane.add(switchViewButton);
        switchViewButton.setBackground(Color.decode("#053d45"));
        switchViewButton.setForeground(Color.decode("#ccd1cd"));
        switchViewButton.setFont(new Font("Optima", Font.PLAIN, 24));
        switchViewButton.setFocusPainted(false);
        switchViewButton.setOpaque(true);
        switchViewButton.setBorderPainted(false);
        switchViewButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchViewButton.addActionListener(this);

        String[] categories = {"CLOTHING","ELECTRONICS","FOOTWEAR","GAMES","STATIONARY","FURNITURE"};
        selCategory = new JComboBox();
        for (String s : categories)
            selCategory.addItem(s);
        selCategory.setBounds(510,100,240,40);
        contentPane.add(selCategory);
        selCategory.setFont(new Font("Optima", Font.PLAIN, 20));
        selCategory.setSelectedIndex(0);
        selCategory.addActionListener(this);

        pId = new JLabel("Enter Product-Id: ");
        pId.setBounds(270,50,200,40);
        pId.setForeground(Color.decode("#e1e5e6"));
        pId.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(pId);

        inPid = new JTextField();
        inPid.setBounds(510,50,240,40);
        inPid.setBackground(Color.WHITE);
        inPid.setForeground(Color.decode("#021215"));
        inPid.setFont(new Font("Consolas",Font.PLAIN,20));
        inPid.setOpaque(true);
        contentPane.add(inPid);

        cId = new JLabel("Select Category: ");
        cId.setBounds(270,100,200,40);
        cId.setForeground(Color.decode("#e1e5e6"));
        cId.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(cId);

        quantity = new JLabel("Enter Quantity: ");
        quantity.setBounds(270,150,200,40);
        quantity.setForeground(Color.decode("#e1e5e6"));
        quantity.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(quantity);

        inQuantity = new JTextField();
        inQuantity.setBounds(510,150,240,40);
        inQuantity.setBackground(Color.WHITE);
        inQuantity.setForeground(Color.decode("#021215"));
        inQuantity.setFont(new Font("Consolas",Font.PLAIN,20));
        inQuantity.setOpaque(true);
        contentPane.add(inQuantity);

        price = new JLabel("Enter Price: ");
        price.setBounds(270,200,200,40);
        price.setForeground(Color.decode("#e1e5e6"));
        price.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(price);

        inPrice = new JTextField();
        inPrice.setBounds(510,200,240,40);
        inPrice.setBackground(Color.WHITE);
        inPrice.setForeground(Color.decode("#021215"));
        inPrice.setFont(new Font("Consolas",Font.PLAIN,20));
        inPrice.setOpaque(true);
        contentPane.add(inPrice);

        pName = new JLabel("Enter Product-Name: ");
        pName.setBounds(270,250,250,40);
        pName.setForeground(Color.decode("#e1e5e6"));
        pName.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(pName);

        inPname = new JTextField();
        inPname.setBounds(510,250,240,40);
        inPname.setBackground(Color.WHITE);
        inPname.setForeground(Color.decode("#021215"));
        inPname.setFont(new Font("Consolas",Font.PLAIN,20));
        inPname.setOpaque(true);
        contentPane.add(inPname);

        description = new JLabel("Enter Description: ");
        description.setBounds(270,300,250,40);
        description.setForeground(Color.decode("#e1e5e6"));
        description.setFont(new Font("Tahoma",Font.PLAIN,25));
        contentPane.add(description);

        info = new JTextArea();
        info.setBounds(270,350,600,200);
        info.setForeground(Color.decode("#021215"));
        info.setBackground(Color.decode("#d6dadb"));
        info.setFont(new Font("Consolas",Font.PLAIN,20));
        contentPane.add(info);

        addProductButton = new JButton("Add Product");
        addProductButton.setBounds(430,575,250,50);
        contentPane.add(addProductButton);
        addProductButton.setBackground(Color.decode("#053d45"));
        addProductButton.setForeground(Color.decode("#ccd1cd"));
        addProductButton.setFont(new Font("Optima", Font.PLAIN, 24));
        addProductButton.setFocusPainted(false);
        addProductButton.setOpaque(true);
        addProductButton.setBorderPainted(false);
        addProductButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addProductButton.addActionListener(this);
        addProductButton.addFocusListener(this);

        this.setTitle("Seller View");
        this.setVisible(true);
        this.setResizable(false);
        this.setBounds(270,80,1000,700);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == switchViewButton) {
            switchViewButton.setBackground(Color.decode("#012b31"));
            viewProductsButton.setBackground(Color.decode("#053d45"));
            this.dispose();
            new Views();
        } else if(e.getSource() == viewProductsButton) {
            switchViewButton.setBackground(Color.decode("#053d45"));
            viewProductsButton.setBackground(Color.decode("#012b31"));
            new ViewProducts().view();
        }
        if (e.getSource() == selCategory) {
            JComboBox cb = (JComboBox) e.getSource();
            cName = (String) cb.getSelectedItem();
        }
        if(e.getSource() == addProductButton) {
            if(inPid.getText().length() == 0 || inQuantity.getText().length() == 0 || inPrice.getText().length() == 0 || inPname.getText().length() == 0 || info.getText().length() == 0) {
                JOptionPane.showMessageDialog(this, "Fill all the Fields", "Fill Out", JOptionPane.WARNING_MESSAGE);
            } else {
                addProduct();
            }

        }
    }
    public void addProduct() {
        try {
            cid = getCid(cName);
            System.out.println(cid);
            pid = Integer.parseInt(inPid.getText());
            qty = Integer.parseInt(inQuantity.getText());
            cost = Double.parseDouble(inPrice.getText());
            pnm = inPname.getText();
            about = info.getText();
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{?=call addProd(?,?,?,?,?,?)}");
            cs.setInt(2,pid);
            cs.setInt(3,cid);
            cs.setDouble(4,cost);
            cs.setInt(5,qty);
            cs.setString(6,pnm);
            cs.setString(7,about);
            cs.registerOutParameter(1,Types.INTEGER);
            cs.execute();
            if(cs.getInt(1) == -1) {
                JOptionPane.showMessageDialog(this, "Product-Id already exists", "Failed!!..", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Product SuccessFully Added.", "Added..", JOptionPane.PLAIN_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid Entry..", "Failed!!..", JOptionPane.WARNING_MESSAGE);
            ex.printStackTrace();
        }
    }
    @Override
    public void focusGained(FocusEvent e) {
        this.viewProductsButton.setBackground(Color.decode("#012b31"));
    }
    @Override
    public void focusLost(FocusEvent e) {
        this.viewProductsButton.setBackground(Color.decode("#053d45"));
    }
    public Integer getCid(String val) {
        Integer ccid = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{?=call getCid(?)}");
            cs.setString(2,val);
            cs.registerOutParameter(1, Types.INTEGER);
            cs.execute();
            ccid = cs.getInt(1);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ccid;
    }
}
class Products {
    Integer pid;
    String cName;
    Integer quantity;
    Double price;
    String pName;
    String info;
    public Products() {
    }
    public Products(Integer pid, String pName,String cName,Integer quantity, Double price, String info) {
        this.pid = pid;
        this.cName = cName;
        this.quantity = quantity;
        this.price = price;
        this.pName = pName;
        this.info = info;
    }

    public Integer getPid() {
        return pid;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public String getcName() {
        return cName;
    }

    public Double getPrice() {
        return price;
    }
    public String getpName() {
        return pName;
    }
    public String getInfo() {
        return info;
    }
    @Override
    public String toString() {
        return "Products{" +
                "pid=" + pid +
                ", cName='" + cName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", pName='" + pName + '\'' +
                ", info='" + info + '\'' +
                '}';
    }
    ArrayList<Products> list = new ArrayList<>();
    Connection con;
    ResultSet rs;
    public ArrayList<Products> getList() {
        try {
            con = null;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", UserData.username, UserData.password);
            CallableStatement cs = con.prepareCall("{call getAllProducts(?)}");
            rs = null;
            cs.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            cs.execute();
            rs = (ResultSet)cs.getObject(1);
            while (rs.next()) {
                Integer pid = rs.getInt(1);
                String pname = rs.getString(2);
                String cname = rs.getString(3);
                Integer quantity = rs.getInt(4);
                Double price = rs.getDouble(5);
                String info = rs.getString(6);
                list.add(new Products(pid, pname, cname, quantity, price, info));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
class ViewProducts {
    Products ob = new Products();
    ArrayList<Products> list = ob.getList();
    public void view() {
        Object [][]data = new Object[list.size()][6];
        for(int i=0;i<data.length;i++) {
            data[i][0] = list.get(i).getPid();
            data[i][1] = list.get(i).getpName();
            data[i][2] = list.get(i).getcName();
            data[i][3] = list.get(i).getQuantity();
            data[i][4] = list.get(i).getPrice();
            data[i][5] = list.get(i).getInfo();
        }
        String []header = {"Product-Id","Product-Name","Category","Quantity","Price","Description"};
        JTable table = new JTable(data,header);
        table.setFont(new Font("Verdana", Font.PLAIN, 14));
        table.setRowHeight(30);
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(Color.decode("#879cbd"));
        tableHeader.setForeground(Color.decode("#ffffff"));
        tableHeader.setFont(new Font("Tahoma", Font.TRUETYPE_FONT,18));
        tableHeader.setResizingAllowed(false);
        JFrame frame = new JFrame();
        frame.setBounds(270,130,1000,600);
        frame.add(new JScrollPane(table));
        frame.setTitle("All Products");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }
}
public class DbmsProjectMain {
    public static void main(String[] args) {
        UserLogin frame = new UserLogin();
        frame.setTitle("Cart Management DBMS Project @Ashish_Singh(21BCSE27) & @Ankit_Singh(21BCSE40)");
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setBounds(270,130,1000,600);
    }
}