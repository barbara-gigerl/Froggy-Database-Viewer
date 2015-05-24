package businessLayer;

import java.util.Map;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableContentModel extends AbstractTableModel
{
    private Vector<Vector<String>> tableData;
    private int[] pkcols;

    public TableContentModel(Vector<Vector<String>> tableData, int[] pkcols)
    {
        this.tableData = tableData;
        this.pkcols = pkcols;
    }

    @Override
    public int getRowCount()
    {
        return tableData.size() - 1; //weil erster eintrag spaltennamen
    }

    public void removeRow(int row)
    {
        tableData.remove(row + 1);
    }

    @Override
    public int getColumnCount()
    {
        return tableData.get(0).size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return tableData.get(rowIndex + 1).get(columnIndex);
    }

    @Override
    public String getColumnName(int column)
    {
        return tableData.get(0).get(column);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        for (int i = 0; i < pkcols.length; i++)
        {
            if (columnIndex == pkcols[i])
            {
                return false;
            }
        }
        return true;
    }

    public void addData(Vector<String> inputValues)
    {
        tableData.add(inputValues);
        super.fireTableDataChanged();
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        tableData.get(rowIndex + 1).set(columnIndex, aValue.toString());
        super.fireTableDataChanged();
    }
}
