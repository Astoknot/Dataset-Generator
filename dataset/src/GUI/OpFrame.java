package GUI;

import java.awt.List;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Aditi
 */
public class OpFrame extends JFrame {

    JLabel heading = new JLabel("DATASET");
    JTable jTable = new JTable();
    JScrollPane sp = new JScrollPane(jTable);
    JButton filter = new JButton("Filter");
    JButton back = new JButton("Back");
    JButton next = new JButton("Next");
    List l1 = new List(5);

    private String query, query1;
    private int numberOfPackets = 0, wIndex, gIndex, oIndex;
    private String information = "";
    private ArrayList<String> tables;
    private int prefColSize[] = new int[8];
    boolean flag = true;

//    JLabel nPackets = new JLabel();
    JLabel info = new JLabel();

    OpFrame(String query, ArrayList<String> tables1) {
        this.query = query;
        this.tables = tables1;
////        System.out.println("size" + tables.size());
//        
//        for (int i = 0; i < tables.size(); i++) {
//            System.out.println("Queries "+tables.get(i));
//        }

        extractClauses();

        // For the groups of different protocols
        if (wIndex != -1 && oIndex != -1) {
            this.query1 = "select protocol, count(protocol) from packets "
                    + query.substring(wIndex, oIndex) + " group by protocol " + query.substring(oIndex);
        } else if (wIndex != -1) {
            this.query1 = "select protocol, count(protocol) from packets "
                    + query.substring(wIndex) + " group by protocol";
        } else if (oIndex != -1) {
            this.query1 = "select protocol, count(protocol) from packets"
                    + " group by protocol " + query.substring(oIndex);
        } else {
            this.query1 = "select protocol, count(protocol) from packets group by protocol";
        }
        //--------------------------------------------------------
        heading.setBounds(480, 10, 60, 30);
        sp.setBounds(10, 50, 1300, 500);

        DefaultTableModel dtm = new DefaultTableModel();
        for (int i = 0; i < 8; i++) {
            dtm.addColumn(getColumnName(i, false));
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/soe", "root", "lekhika");

            Statement s = con.prepareStatement(this.query);
            ResultSet rs = s.executeQuery(this.query);

            while (rs.next()) {
                dtm.addRow(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)});
                numberOfPackets++;
//                System.out.println(rs.getString(1)+ rs.getString(2)+ rs.getString(3)+ rs.getString(4)+rs.getString(5));
            }
            jTable.setModel(dtm);

            Statement s1 = con.createStatement();
            ResultSet rs1 = s1.executeQuery(query1);
            while (rs1.next()) {
                information = "Number of " + rs1.getString(1) + "   :  " + rs1.getString(2) + "\n";
                l1.add(information);
//                System.out.println("info:" + information);
            }

            s.close();
            s1.close();
            rs.close();
            rs1.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Buttons
        filter.setBounds(900, 600, 100, 30);
        filter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterf(true);
            }
        });

        if (tables.indexOf(query) == 0) {
            back.setEnabled(false);
        }
        back.setBounds(150, 10, 60, 30);
        back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh1(true);
            }
        });
        if (tables.indexOf(query) == (tables.size() - 1)) {
            next.setEnabled(false);
        }
        next.setBounds(700, 10, 60, 30);
        next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refresh1(false);
            }
        });

        jTable.setCellSelectionEnabled(true);
        ListSelectionModel select = jTable.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        select.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String Data = null;
                int[] row = jTable.getSelectedRows();
                int[] columns = jTable.getSelectedColumns();
                Data = jTable.getValueAt(row[0], columns[0]).toString();
                if (flag) {
                    refresh(Data, columns[0]);
                    flag = false;
                }
            }
        });
        l1.setBounds(10, 580, 600, 110);
        l1.add("Total Packets: " + numberOfPackets);
//        nPackets.setText("Total Packets: "+numberOfPackets);
//        nPackets.setBounds(10,600,170,10);
//        info.setText(information);
//        info.setBounds(10,610,200,30);
        TableColumnModel columnModel = jTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(5);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(70);
        columnModel.getColumn(3).setPreferredWidth(70);
        columnModel.getColumn(4).setPreferredWidth(60);
        columnModel.getColumn(5).setPreferredWidth(60);
        columnModel.getColumn(6).setPreferredWidth(10);
        columnModel.getColumn(7).setPreferredWidth(200);

//        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        jTable.getColumnModel().getColumn(0).setPreferredWidth(27);
//        jTable.getColumnModel().getColumn(1).setPreferredWidth(50);
//        jTable.getColumnModel().getColumn(2).setPreferredWidth(50);
//        jTable.getColumnModel().getColumn(3).setPreferredWidth(50);
//        jTable.getColumnModel().getColumn(4).setPreferredWidth(50);
//        jTable.getColumnModel().getColumn(6).setPreferredWidth(100);
//        jTable.getColumnModel().getColumn(7).setPreferredWidth(100);
//        jTable.getColumnModel().getColumn(8).setPreferredWidth(95);
//        jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
//        jTable.getColumnModel().getColumn(9).setPreferredWidth(40);
//        jTable.getColumnModel().getColumn(10).setPreferredWidth(400);
        this.add(heading);
        this.add(sp);
//        this.add(nPackets);
        this.add(info);
        this.add(filter);
        this.add(back);
        this.add(next);
        this.add(l1);

        this.setSize(1300, 1000);
        this.setLayout(null);
        this.setVisible(true);
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(IpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IpFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ArrayList<String> str = new ArrayList<String>();
                str.add("select * from packets");
                new OpFrame("select * from packets", str).setVisible(true);
            }
        });
    }

    private void refresh(String Data, int x) {
        for (int i = tables.indexOf(query) + 1; i < tables.size(); i++) {
            tables.remove(i--);
        }
        int endIndex;
        if (gIndex != -1) {
            endIndex = gIndex;
        } else if (oIndex != -1) {
            endIndex = oIndex;
        } else {
            endIndex = query.length();
        }
        if (x > 1 && x < 9) {
            if (wIndex != -1) {
                query = query.substring(0, endIndex)
                        + " and " + getColumnName(x, true) + " = '" + Data
                        + "' " + query.substring(endIndex);
            } else {
                query = query.substring(0, endIndex)
                        + " where " + getColumnName(x, true) + " = '" + Data
                        + "' " + query.substring(endIndex);
            }

            tables.add(query);
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            new OpFrame(query, tables);
        }
    }

    private void refresh1(boolean isBack) {
        if (isBack) {
            query = tables.get(tables.indexOf(query) - 1);
        } else {
            query = tables.get(tables.indexOf(query) + 1);
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        new OpFrame(query, tables);
    }

    private void filterf(boolean b){
        if(b){
            this.setVisible(false);
        FilterFrame f = new FilterFrame();
        }
    }
    
    private String getColumnName(int x, boolean isQuery) {
        if (isQuery) {
            if (x == 0) {
                return "sno";
            }
            if (x == 1) {
                return "timestamp";
            }
            if (x == 2) {
                return "eth_src";
            }
            if (x == 3) {
                return "eth_dest";
            }
            if (x == 4) {
                return "ip_src";
            }
            if (x == 5) {
                return "ip_dest";
            }
            if (x == 6) {
                return "protocol";
            }
            if (x == 7) {
                return "data";
            }
        } else {
            if (x == 0) {
                return "S.No.";
            }
            if (x == 1) {
                return "Timestamp";
            }
            if (x == 2) {
                return "Src MAC Addr";
            }
            if (x == 3) {
                return "Dest MAC Addr";
            }
            if (x == 4) {
                return "Src IP";
            }
            if (x == 5) {
                return "Dest IP";
            }
            if (x == 6) {
                return "Protocol";
            }
            if (x == 7) {
                return "Information";
            }
        }
        return "";
    }

    private void extractClauses() {
        wIndex = query.indexOf("where");
        gIndex = query.indexOf("group by");
        oIndex = query.indexOf("order by");
    }
}
