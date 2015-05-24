package gui;

import database.DataBaseAccess;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TableManager extends JFrame
{

    private DataBaseAccess dataBaseAccess;
    private JRadioButton[] rbTables;
    private ViewerGUI viewerGUI;
    private ButtonGroup bgTables;
    private JDialog dlgDelete;
    private JDialog dlgAdd;
    private int rows, height;
    private Vector<JTextField> names;
    private Vector<JComboBox> dataTypes;
    private Vector<JTextField> lengths;
    private Vector<JCheckBox> notNull;
    private Vector<JCheckBox> primaryKey;
    private String tableName;

    public TableManager(DataBaseAccess dataBaseAccess, ViewerGUI viewerGUI)
    {
        this.rows = 4;
        this.height = 210;
        this.dataBaseAccess = dataBaseAccess;
        this.viewerGUI = viewerGUI;

        names = new Vector<JTextField>();
        dataTypes = new Vector<JComboBox>();
        lengths = new Vector<JTextField>();
        notNull = new Vector<JCheckBox>();
        primaryKey = new Vector<JCheckBox>();

        initComponents();
    }

    private void initComponents()
    {
        bgTables = new ButtonGroup();
        this.setSize(300, 100);
        this.setLayout(new GridLayout(2, 1));
        this.setLocationRelativeTo(null);
        JButton btCreate = new JButton("Create Table");
        JButton btDelete = new JButton("Delete Table");

        btCreate.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                onCreate();
                ViewerGUI.frog();
            }
        });

        btDelete.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                onDelete();
                ViewerGUI.frog();
            }
        });

        this.getContentPane().add(btCreate);
        this.getContentPane().add(btDelete);

    }

    private void onDelete()
    {
        try
        {
            dlgDelete = new JDialog();
            dlgDelete.setTitle("Delete Tables");
            JLabel labWhat = new JLabel("Chooose table to delete...");
            JButton btOK = new JButton("OK");

            Vector<String> tableNames = dataBaseAccess.getTableNames();

            dlgDelete.getContentPane().add(labWhat);

            for (int i = 0; i < tableNames.size(); i++)
            {
                JRadioButton rbTable = new JRadioButton(tableNames.get(i));
                bgTables.add(rbTable);
                dlgDelete.getContentPane().add(rbTable);
            }

            dlgDelete.setLayout(new GridLayout(2 + bgTables.getButtonCount(), 1));

            dlgDelete.getContentPane().add(btOK);
            dlgDelete.setVisible(true);
            dlgDelete.setSize(200, tableNames.size() * 50 + 100);
            dlgDelete.setLocationRelativeTo(null);
            btOK.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent ae)
                {
                    ViewerGUI.frog();
                    onOK();
                }
            });
        }
        catch (Exception ex)
        {
            ViewerGUI.frog2();
            JOptionPane.showMessageDialog(null, ex.toString());
        }
    }

    private void onOK()
    {

        String delete = null;
        Enumeration<AbstractButton> elements = bgTables.getElements();
        while (elements.hasMoreElements())
        {
            JRadioButton btTable = (JRadioButton) elements.nextElement();

            if (btTable.isSelected())
            {
                try
                {
                    delete = btTable.getText();
                    dataBaseAccess.deleteTable(delete);
                }
                catch (Exception ex)
                {
                    ViewerGUI.frog2();
                    JOptionPane.showMessageDialog(null, ex.toString());
                }
            }
        }
        JOptionPane.showMessageDialog(null, "Tables deleted");
        viewerGUI.updateTableNames();

        this.dispose();
        dlgDelete.dispose();
    }

    private void onCreate()
    {
        dlgAdd = new JDialog();
        dlgAdd.setTitle("Add Table");
        dlgAdd.setLocationRelativeTo(null);
        final JTextField tfName = new JTextField("Name");
        JPanel firstRow = initColumn();
        final JButton btAddColumn = new JButton("Add column");
        final JButton btCreateTable = new JButton("Create Table");
        btAddColumn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                dlgAdd.remove(btAddColumn);
                dlgAdd.remove(btCreateTable);
                onAddColumn();
                dlgAdd.add(btAddColumn);
                dlgAdd.add(btCreateTable);
                ViewerGUI.frog();
            }
        });

        btCreateTable.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                tableName = tfName.getText();
                dlgAdd.remove(btAddColumn);
                dlgAdd.remove(btCreateTable);
                onCreateTable();
                dlgAdd.add(btAddColumn);
                dlgAdd.add(btCreateTable);
                ViewerGUI.frog();
                dlgAdd.dispose();
            }
        });


        dlgAdd.setLayout(new GridLayout(rows, 1));
        dlgAdd.setSize(600, height);
        dlgAdd.add(tfName);
        dlgAdd.add(firstRow);
        dlgAdd.add(btAddColumn);
        dlgAdd.add(btCreateTable);
        dlgAdd.setVisible(true);

    }

    private JPanel initColumn()
    {
        JPanel panColumn = new JPanel();
        panColumn.setLayout(new GridLayout(1, 5));

        JTextField tfColumn = new JTextField("Name");
        JComboBox cbTypes = new JComboBox();
        String[] types = {"INTEGER", "VARCHAR", "SERIAL", "TIMESTAMP"};
        for (String type : types)
        {
            cbTypes.addItem(type);
        }
        cbTypes.setSelectedIndex(0);

        JTextField tfLength = new JTextField("Length");
        JCheckBox cbNotNull = new JCheckBox("NOT NULL");
        JCheckBox cbPrimaryKey = new JCheckBox("Primary Key");
        panColumn.add(tfColumn);
        panColumn.add(cbTypes);
        panColumn.add(tfLength);
        panColumn.add(cbNotNull);
        panColumn.add(cbPrimaryKey);

        names.add(tfColumn);
        lengths.add(tfLength);
        dataTypes.add(cbTypes);
        notNull.add(cbNotNull);
        primaryKey.add(cbPrimaryKey);

        return panColumn;
    }

    private void onAddColumn()
    {
        rows++;
        height += 70;
        dlgAdd.setLayout(new GridLayout(rows, 1));
        dlgAdd.setSize(600, height);
        dlgAdd.add(initColumn());
    }

    private void onCreateTable()
    {

        Vector<String> accessNames = new Vector<>();
        Vector<String> accessDataTypes = new Vector<>();
        Vector<String> accessLengths = new Vector<>();
        Vector<Boolean> accessNotNull = new Vector<>();
        Vector<Boolean> accessPrimaryKey = new Vector<>();

        for (int i = 0; i < names.size(); i++)
        {
            accessNames.add(names.get(i).getText());
            accessDataTypes.add(dataTypes.get(i).getSelectedItem().toString());
            accessLengths.add(lengths.get(i).getText());
            accessNotNull.add(notNull.get(i).isSelected());
            accessPrimaryKey.add(notNull.get(i).isSelected());
        }

        //Fehlerbehandlung
        Set<String> accessNamesSet = new HashSet<String>();
        for (int i = 0; i < accessNames.size(); i++)
        {
            accessNamesSet.add(accessNames.get(i));
        }

        if (accessNames.size() != accessNamesSet.size())
        {
            try
            {
                throw new Exception("Please choose different column names");
            }
            catch (Exception ex)
            {
                ViewerGUI.frog2();
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        } 
        else
        {
            try
            {
                dataBaseAccess.addTable(accessNames, accessDataTypes, accessLengths, tableName, accessNotNull, accessPrimaryKey);
            } 
            catch (Exception ex) 
            {
                ViewerGUI.frog2();
                JOptionPane.showMessageDialog(null, ex.toString());
            }
        }
    }
}
