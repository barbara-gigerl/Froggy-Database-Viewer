package gui;

import businessLayer.TableContentModel;
import database.DataBaseAccess;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class ViewerGUI extends javax.swing.JFrame
{
    private DataBaseAccess dataBaseAccess;
    private TableContentModel tableContentModel;
    private DefaultListModel dlm;

    public ViewerGUI()
    {
        initComponents();
        addSoundToComponents();
        frog();
        try
        {
            designComponents();
        }
        catch (Exception ex)
        {
            Logger.getLogger(ViewerGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        lsTables.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabContent.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dlm = new DefaultListModel();
        lsTables.setModel(dlm);
        this.setLocationRelativeTo(null);
        Thread t = new Thread(new WelcomeWindow());
        t.start();

        String fileName = System.getProperty("user.dir")
                + File.separator + "src"
                + File.separator + "data" + File.separator + "frog.jpg";
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(fileName));
        this.setTitle("FroggyDatabaseViewer");

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        spViewer = new javax.swing.JSplitPane();
        scpList = new javax.swing.JScrollPane();
        lsTables = new javax.swing.JList();
        scpTable = new javax.swing.JScrollPane();
        tabContent = new javax.swing.JTable();
        mbMenu = new javax.swing.JMenuBar();
        menDataBase = new javax.swing.JMenu();
        miConnect = new javax.swing.JMenuItem();
        miDisconnect = new javax.swing.JMenuItem();
        spSeparator = new javax.swing.JPopupMenu.Separator();
        miExit = new javax.swing.JMenuItem();
        menDataSet = new javax.swing.JMenu();
        miDelete = new javax.swing.JMenuItem();
        miUpdate = new javax.swing.JMenuItem();
        miAdd = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        miTables = new javax.swing.JMenuItem();
        miConsole = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(0, 0, 0));

        lsTables.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                onSelectTable(evt);
            }
        });
        scpList.setViewportView(lsTables);

        spViewer.setLeftComponent(scpList);

        scpTable.setBackground(new java.awt.Color(0, 0, 0));

        tabContent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        scpTable.setViewportView(tabContent);

        spViewer.setRightComponent(scpTable);

        getContentPane().add(spViewer, java.awt.BorderLayout.CENTER);

        menDataBase.setBackground(new java.awt.Color(154, 205, 50));
        menDataBase.setText("DataBase");
        menDataBase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onConnect(evt);
            }
        });

        miConnect.setText("Connect");
        miConnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onConnect(evt);
            }
        });
        menDataBase.add(miConnect);

        miDisconnect.setText("Disconnect");
        miDisconnect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDisconnect(evt);
            }
        });
        menDataBase.add(miDisconnect);
        menDataBase.add(spSeparator);

        miExit.setText("Exit");
        menDataBase.add(miExit);

        mbMenu.add(menDataBase);

        menDataSet.setBackground(new java.awt.Color(154, 205, 50));
        menDataSet.setText("DataBaseManipulation");

        miDelete.setText("Delete");
        miDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onDelete(evt);
            }
        });
        menDataSet.add(miDelete);

        miUpdate.setText("Update");
        miUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onUpdate(evt);
            }
        });
        menDataSet.add(miUpdate);

        miAdd.setText("Add");
        miAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdd(evt);
            }
        });
        menDataSet.add(miAdd);
        menDataSet.add(jSeparator1);

        miTables.setText("Tables");
        miTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onTables(evt);
            }
        });
        menDataSet.add(miTables);

        miConsole.setText("SQLConsole");
        miConsole.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onConsole(evt);
            }
        });
        menDataSet.add(miConsole);

        mbMenu.add(menDataSet);

        setJMenuBar(mbMenu);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void onConnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onConnect
        frog();
        String DB_Name = JOptionPane.showInputDialog("Please enter database name", "Mitpro");
        if (!DB_Name.trim().isEmpty())
        {
            try
            {
                dataBaseAccess = new DataBaseAccess(DB_Name);
                lsTables.setListData(dataBaseAccess.getTableNames());
                lsTables.setSelectedIndex(0);
                this.setTitle("FroggyDatabaseViewer: " + DB_Name);
            }
            catch (SQLException ex)
            {
                JOptionPane.showMessageDialog(this, "Connecton failed");
                frog2();
            }
            catch (FileNotFoundException ex)
            {
                JOptionPane.showMessageDialog(this, "Porperty file missing");
                frog2();
            }
            catch (IOException ex)
            {
                JOptionPane.showMessageDialog(this, "Could not load driver");
                frog2();
            }
            catch (ClassNotFoundException ex)
            {
                JOptionPane.showMessageDialog(this, "Could not load driver");
                frog2();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(this, "Error while loading table names");
                frog2();
            }
        }
    }//GEN-LAST:event_onConnect

    private void onSelectTable(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_onSelectTable
        if (evt.getValueIsAdjusting())
        {
            frog();
        }
        String tableName = (String) lsTables.getSelectedValue();
        if (tableName != null)
        {
            try
            {
                Vector<Integer> primaryKeys = dataBaseAccess.getPrimaryKeys(tableName);
                int[] pkcols = new int[primaryKeys.size()];

                for (int i = 0; i < primaryKeys.size(); i++)
                {
                    pkcols[i] = primaryKeys.get(i) - 1;
                }
                tabContent.setDefaultRenderer(Object.class, new ViewerRenderer(pkcols));

                tableContentModel = new TableContentModel(dataBaseAccess.getTableData(tableName), pkcols);
                tabContent.setModel(tableContentModel);

                tabContent.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                int numberCol = tabContent.getColumnModel().getColumnCount();
                int[] sizes = dataBaseAccess.getColumnSizes(tableName);

                for (int i = 0; i < numberCol; i++)
                {
                    tabContent.getColumnModel().getColumn(i).setPreferredWidth(sizes[i] * 2 + 20);
                }

            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString());
                frog2();
            }
        }
    }//GEN-LAST:event_onSelectTable

    private void onDisconnect(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDisconnect
        frog();
        lsTables.setModel(new DefaultListModel());
        tabContent.setModel(new DefaultTableModel());
    }//GEN-LAST:event_onDisconnect

    private void onDelete(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onDelete
        frog();
        Vector<String> row = new Vector<>();
        int index = tabContent.getSelectedRow();

        if (index < 0 || index > tabContent.getRowCount())
        {
            JOptionPane.showMessageDialog(null, "Invalid Selection");
        }
        else
        {
            for (int i = 0; i < tabContent.getColumnCount(); i++)
            {
                row.add(tabContent.getValueAt(index, i).toString());
            }

            try
            {
                dataBaseAccess.deleteRow(row, lsTables.getSelectedValue().toString());
                tableContentModel.removeRow(index);
                tableContentModel.fireTableDataChanged();
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString());
                frog2();
            }
        }
    }//GEN-LAST:event_onDelete

    private void onUpdate(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onUpdate
        frog();
        Vector<String> row = new Vector<>();
        int index = tabContent.getSelectedRow();
        
        if (index < 0 || index > tabContent.getRowCount())
        {
            JOptionPane.showMessageDialog(null, "Invalid Selection");
        }
        else
        {
            try
            {
                for (int i = 0; i < tabContent.getColumnCount(); i++)
                {
                    row.add(tabContent.getValueAt(index, i).toString());
                }

                dataBaseAccess.updateRow(row, lsTables.getSelectedValue().toString());
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString());
                frog2();
            }
        }
    }//GEN-LAST:event_onUpdate

    private void onAdd(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAdd
        try
        {
            frog();
            Vector<String> allNames = dataBaseAccess.getColumnNames(lsTables.getSelectedValue().toString());

            Input input = new Input(allNames, tableContentModel, dataBaseAccess, lsTables.getSelectedValue().toString());
            input.setVisible(true);
        }
        catch (Exception ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
            frog2();
        }
    }//GEN-LAST:event_onAdd

    private void onTables(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onTables
        TableManager tableManager = new TableManager(dataBaseAccess, this);
        tableManager.setVisible(true);
        frog();
    }//GEN-LAST:event_onTables

    private void onConsole(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onConsole
        frog();
        Console console = new Console(dataBaseAccess, this);
        console.setVisible(true);
    }//GEN-LAST:event_onConsole

    public void updateTableNames()
    {
        try
        {
            dlm.clear();
            lsTables.setListData(dataBaseAccess.getTableNames());
            lsTables.setSelectedIndex(0);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.toString());
            frog2();
        }
    }

    private void addSoundToComponents()
    {
        menDataBase.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                frog();
            }
        });

        menDataSet.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                frog();
            }
        });
    }

    public static void main(String args[]) {
        
        System.out.println(System.getProperty("user.dir"));
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
            java.util.logging.Logger.getLogger(ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ViewerGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ViewerGUI().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JList lsTables;
    private javax.swing.JMenuBar mbMenu;
    private javax.swing.JMenu menDataBase;
    private javax.swing.JMenu menDataSet;
    private javax.swing.JMenuItem miAdd;
    private javax.swing.JMenuItem miConnect;
    private javax.swing.JMenuItem miConsole;
    private javax.swing.JMenuItem miDelete;
    private javax.swing.JMenuItem miDisconnect;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miTables;
    private javax.swing.JMenuItem miUpdate;
    private javax.swing.JScrollPane scpList;
    private javax.swing.JScrollPane scpTable;
    private javax.swing.JPopupMenu.Separator spSeparator;
    private javax.swing.JSplitPane spViewer;
    private javax.swing.JTable tabContent;
    // End of variables declaration//GEN-END:variables

    private void designComponents() throws Exception
    {
        UIManager.put("nimbusBase", new Color(154, 205, 50));
        UIManager.put("nimbusBlueGrey", new Color(154, 205, 50));
        UIManager.put("control", new Color(154, 205, 50));
        UIManager.put("defaultFont", new Font("Comic Sans MS", Font.PLAIN, 13));

        SwingUtilities.updateComponentTreeUI(this);
    }

    public static void frog()
    {
        InputStream in = null;
        AudioStream as = null;
        try
        {
            String fileName = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "data" + File.separator + "frog1.wav";

            in = new FileInputStream(fileName);
            as = new AudioStream(in);
            AudioPlayer.player.start(as);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
            frog2();
        }
    }

    public static void frog2()
    {
        InputStream in = null;
        AudioStream as = null;
        try
        {
            String fileName = System.getProperty("user.dir")
                    + File.separator + "src"
                    + File.separator + "data" + File.separator + "frog2.wav";

            in = new FileInputStream(fileName);
            as = new AudioStream(in);
            AudioPlayer.player.start(as);
        }
        catch (IOException ex)
        {
            JOptionPane.showMessageDialog(null, ex.toString());
            frog2();
        }
    }
}
