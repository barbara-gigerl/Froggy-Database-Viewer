package gui;

import database.DataBaseAccess;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Console extends JFrame
{
    private DataBaseAccess dataBaseAccess;
    private JTextArea taInput;
    private JTextArea taOutput;
    private ViewerGUI viewerGUI;

    public Console(DataBaseAccess dataBaseAccess, ViewerGUI viewerGUI)
    {
        this.setLocationRelativeTo(null);
        this.setSize(400, 300);
        this.dataBaseAccess = dataBaseAccess;
        this.viewerGUI = viewerGUI;
        initComponents();
    }

    private void initComponents()
    {
        this.setLayout(new BorderLayout());

        JScrollPane spInput = new JScrollPane();
        JScrollPane spOutput = new JScrollPane();

        JPanel panIO = new JPanel();
        taInput = new JTextArea();
        taOutput = new JTextArea();
        JButton btExecute = new JButton("Execute");

        btExecute.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                onExecute();
            }
        });

        this.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                onClosing();
            }
        });
        
        spInput.setViewportView(taInput);
        spOutput.setViewportView(taOutput);
        panIO.setLayout(new GridLayout(2, 1));
        panIO.add(spInput);
        panIO.add(spOutput);

        this.getContentPane().add(panIO, BorderLayout.CENTER);
        this.getContentPane().add(btExecute, BorderLayout.SOUTH);
    }

    private void onExecute()
    {
        ViewerGUI.frog();
        String sqlStatement = taInput.getText();

        if (sqlStatement.startsWith("SELECT"))
        {
            JOptionPane.showMessageDialog(null, "SELECT not supported in SQLConsole");
            ViewerGUI.frog2();
        }
        else
        {
            try
            {
                String result = dataBaseAccess.executeStatement(sqlStatement);
                taOutput.setText(result);
            }
            catch (Exception ex)
            {
                JOptionPane.showMessageDialog(null, ex.toString());
                ViewerGUI.frog2();
            }
        }
    }

    private void onClosing()
    {
        viewerGUI.updateTableNames();
        this.dispose();
    }
}
