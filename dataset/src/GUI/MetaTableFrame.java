/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

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
 * @author lekhika
 */
public class MetaTableFrame extends JFrame {

    JLabel heading = new JLabel("DATASET DIRECTORY");
    JTable jTable = new JTable();
    JScrollPane sp = new JScrollPane(jTable);

    private String query;
    private boolean flag = true;

    MetaTableFrame() {
        query = "select * from meta";

        DefaultTableModel dtm = new DefaultTableModel();
        dtm.addColumn("S. No.");
        dtm.addColumn("Start Time");
        dtm.addColumn("End Time");
        dtm.addColumn("Duration");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/soe", "root", "lekhika");

            Statement s = con.prepareStatement(query);
            ResultSet rs = s.executeQuery(query);

            while (rs.next()) {
                dtm.addRow(new String[]{rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)});

                System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3) + rs.getString(4));
            }
            jTable.setModel(dtm);

            s.close();
            rs.close();
            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        jTable.setCellSelectionEnabled(true);
        ListSelectionModel select = jTable.getSelectionModel();
        select.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        select.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String Data = null;
                int[] row = jTable.getSelectedRows();
                if (flag) {
                    query = "select * from packets where timestamp between '"
                            + jTable.getValueAt(row[0], 1).toString()
                            + "' and '"
                            + jTable.getValueAt(row[0], 2).toString()
                            + "'";

                    System.out.println(query);
                    ArrayList<String> str = new ArrayList<String>();
                    str.add(query);
                    OpFrame O = new OpFrame(query, str);
                    flag=false;
                }

            }
        });

        heading.setBounds(280, 10, 160, 30);
        sp.setBounds(10, 50, 700, 500);
        this.add(heading);
        this.add(sp);

        TableColumnModel columnModel = jTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        columnModel.getColumn(1).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(2).setPreferredWidth(10);

        this.setSize(700, 500);
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
                new MetaTableFrame().setVisible(true);
            }
        });
    }
}
