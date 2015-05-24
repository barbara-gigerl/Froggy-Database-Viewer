package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ViewerRenderer implements TableCellRenderer
{
    private int [] pkcols;
    
    public ViewerRenderer(int[] pkcols)
    {
        this.pkcols = pkcols;
    }
       
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column)
    {
        JLabel labLabel = new JLabel(value.toString());
        labLabel.setOpaque(true);
       
        int col = table.convertColumnIndexToModel(column);

        for (int i = 0; i < pkcols.length; i++)
        {
            if(col == pkcols[i])
            {
                labLabel.setBackground(Color.gray);
            }
        }
              
        return labLabel;
    }
    
    
}
