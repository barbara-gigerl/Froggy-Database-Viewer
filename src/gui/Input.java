package gui;

import businessLayer.TableContentModel;
import database.DataBaseAccess;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Input extends JDialog
{

    private Vector<String> columnNames;
    private JLabel[] labBez;
    private JTextField[] tfIn;
    private TableContentModel tableContentModel;
    private DataBaseAccess dataBaseAccess;
    private String tableName;

    public Input(Vector<String> columnNames, TableContentModel tableContentModel,
            DataBaseAccess dataBaseAccess, String tableName)
    {
        this.columnNames = columnNames;
        this.tableContentModel = tableContentModel;
        this.dataBaseAccess = dataBaseAccess;
        this.tableName = tableName;
        initComponents();
    }

    private void initComponents()
    {
        this.setTitle(tableName);
        this.setLayout(new GridLayout(columnNames.size() + 1, 2));
        this.setLocationRelativeTo(null);
        this.setSize(300, columnNames.size() * 50);
        labBez = new JLabel[columnNames.size()];
        tfIn = new JTextField[columnNames.size()];

        for (int i = 0; i < columnNames.size(); i++)
        {
            labBez[i] = new JLabel(columnNames.get(i));
            tfIn[i] = new JTextField();
            this.add(labBez[i]);
            this.add(tfIn[i]);
        }
        this.add(new JLabel());
        JButton btOK = new JButton("OK");
        btOK.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent ae)
            {
                onOK();
            }
        });

        this.add(btOK);

    }

    private void onOK()
    {
        try
        {
            Vector<String> inputValues = new Vector<String>();
            for (int i = 0; i < labBez.length; i++)
            {
                inputValues.add(tfIn[i].getText().trim());
            }
            dataBaseAccess.addRow(inputValues, tableName);

            tableContentModel.addData(inputValues);

            this.dispose();
        } catch (SQLException ex)
        {
            
            ViewerGUI.frog2();
            JOptionPane.showMessageDialog(null, "Einfügen nicht möglich!");
            
        }
        catch (Exception ex)
        {
            ViewerGUI.frog2();
            JOptionPane.showMessageDialog(null, "Einfügen nicht möglich!");
        }
    }
}
