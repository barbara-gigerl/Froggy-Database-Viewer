package database;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

public class DataBaseAccess
{
    private DataBase dataBase;

    public DataBaseAccess(String DB_Name) throws FileNotFoundException, IOException, ClassNotFoundException, SQLException
    {
        dataBase = DataBase.getInstance();
        dataBase.disconnect();
        dataBase.connect(DB_Name);
    }

    public Vector<String> getTableNames() throws Exception
    {
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";
        ResultSet result = statement.executeQuery(sqlString);

        Vector<String> tableNames = new Vector<>();

        while (result.next())
        {
            String table_name = result.getString("table_name");
            tableNames.add(table_name);
        }
        dataBase.releaseStatement(statement);

        return tableNames;
    }

    public Vector<Vector<String>> getTableData(String tableName) throws SQLException, Exception
    {
        Vector<Vector<String>> tableData = new Vector<>();

        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT * FROM " + tableName + "";
        ResultSet result = statement.executeQuery(sqlString);

        ResultSetMetaData resultSetMetaData = result.getMetaData();
        int noColumns = resultSetMetaData.getColumnCount();

        //Retrieve column names from table:
        Vector<String> columnNames = new Vector<>();

        for (int i = 1; i <= noColumns; i++)
        {
            columnNames.add(resultSetMetaData.getColumnName(i));
        }
        tableData.add(columnNames);

        //Retrieve table content:
        while (result.next())
        {
            Vector<String> row = new Vector<>();

            for (int i = 1; i <= noColumns; i++)
            {
                String columnName = resultSetMetaData.getColumnName(i);
                String value = result.getString(columnName);
                row.add(value);
            }

            tableData.add(row);
        }
        dataBase.releaseStatement(statement);
        return tableData;
    }

    public void deleteRow(Vector<String> row, String tableName) throws Exception
    {
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT * FROM " + tableName + "";
        ResultSet result = statement.executeQuery(sqlString);
        ResultSetMetaData resultSetMetaData = result.getMetaData();
        
        sqlString = "DELETE FROM " + tableName + " WHERE ";

        for (int i = 1; i < resultSetMetaData.getColumnCount(); i++)
        {
            if (i == resultSetMetaData.getColumnCount() - 1)
            {
                sqlString += resultSetMetaData.getColumnName(i) + " = '" + row.get(i - 1) + "'";
                continue;
            }
            sqlString += resultSetMetaData.getColumnName(i) + " =  '" + row.get(i - 1) + "'  AND ";
        }

        sqlString += ";";

        statement.execute(sqlString);
        dataBase.releaseStatement(statement);
    }

    public int[] getColumnSizes(String tableName) throws Exception
    {
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT character_maximum_length FROM information_schema.columns WHERE table_name  = '" + tableName + "'";
        ResultSet result = statement.executeQuery(sqlString);
        dataBase.releaseStatement(statement);

        
        Vector<Integer> sizeVector = new Vector<>();

        while (result.next())
        {
            sizeVector.add(result.getInt(1));
        }

        Object[] sizeObjects = sizeVector.toArray();
        int[] sizes = new int[sizeObjects.length];

        for (int j = 0; j < sizeObjects.length; j++)
        {
            sizes[j] = sizeVector.get(j);
        }
        return sizes;
    }

    public void disconnect()
    {
        dataBase.disconnect();
    }

    public Vector<Integer> getPrimaryKeys(String tableName) throws Exception
    {
        Vector<Integer> columnNames = new Vector<>();

        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT ordinal_position FROM information_schema.key_column_usage "
                + "WHERE constraint_name LIKE '%_pkey' AND table_name = '" + tableName + "'";
        ResultSet result = statement.executeQuery(sqlString);
        dataBase.releaseStatement(statement);

        while (result.next())
        {
            columnNames.add(result.getInt(1));
        }

        return columnNames;
    }

    public void updateRow(Vector<String> newRow, String tableName) throws Exception
    {
        Map<Integer, String> all = new HashMap<>();
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT ordinal_position, column_name FROM information_schema.columns "
                + "WHERE table_name = '" + tableName + "'";
        ResultSet result = statement.executeQuery(sqlString);
        dataBase.releaseStatement(statement);
        while (result.next())
        {
            all.put(result.getInt(1), result.getString(2));
        }

        //-----

        Map<Integer, String> pk = new HashMap<>();
        statement = dataBase.getStatement();
        sqlString = "SELECT ordinal_position, column_name FROM information_schema.key_column_usage "
                + "WHERE table_name = '" + tableName + "'";
        result = statement.executeQuery(sqlString);
        while (result.next())
        {
            pk.put(result.getInt(1), result.getString(2));
        }
        dataBase.releaseStatement(statement);

        //-----


        Set<Entry<Integer, String>> entrySet = pk.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            all.remove(entry.getKey());
        }


        //-----

        sqlString = "UPDATE " + tableName + " SET ";
        entrySet = all.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            sqlString += entry.getValue() + " = '" + newRow.get(entry.getKey() - 1) + "', ";
        }

        sqlString = sqlString.substring(0, sqlString.length() - 2);

        //-----

        statement = dataBase.getStatement();
        sqlString += " WHERE ";
        entrySet = pk.entrySet();
        for (Entry<Integer, String> entry : entrySet)
        {
            sqlString += entry.getValue() + " = '" + newRow.get(entry.getKey() - 1) + "' AND";
        }

        sqlString = sqlString.substring(0, sqlString.length() - 3);
        statement.execute(sqlString);
        dataBase.releaseStatement(statement);
    }

    public Vector<String> getColumnNames(String tableName) throws Exception
    {
        Vector<String> all = new Vector<>();
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT column_name FROM information_schema.columns "
                + "WHERE table_name = '" + tableName + "' ORDER BY ordinal_position";
        ResultSet result = statement.executeQuery(sqlString);

        while (result.next())
        {
            all.add(result.getString(1));
        }
        dataBase.releaseStatement(statement);
        return all;
    }

    public void addRow(Vector<String> inputValues, String tableName) throws SQLException, Exception
    {
        Statement statement = dataBase.getStatement();
        String sqlString = "INSERT INTO " + tableName + " VALUES (";

        for (String value : inputValues)
        {
            sqlString += "'" + value + "'" + ", ";
        }
        sqlString = sqlString.substring(0, sqlString.length() - 2);
        sqlString += ")";

        statement.execute(sqlString);
        dataBase.releaseStatement(statement);
    }

    public void deleteTable(String deleteTable) throws Exception
    {
        Statement statement = dataBase.getStatement();
        String sqlString = "SELECT constraint_name, s2.table_name FROM information"
                + "_schema.constraint_table_usage s1 JOIN information_schema.key"
                + "_column_usage s2 USING(constraint_name) WHERE "
                + "constraint_name LIKE '%fk%' AND s1.table_name = '" + deleteTable + "'";
        ResultSet result = statement.executeQuery(sqlString);

        while (result.next())
        {
            String constraint = result.getString(1);
            String toAlterTable = result.getString(2);

            statement = dataBase.getStatement();
            sqlString = "ALTER TABLE " + toAlterTable + " DROP CONSTRAINT " + constraint;
            statement.execute(sqlString);
            dataBase.releaseStatement(statement);
        }

        dataBase.releaseStatement(statement);
        statement = dataBase.getStatement();
        sqlString = "DROP TABLE " + deleteTable;
        statement.execute(sqlString);
        dataBase.releaseStatement(statement);
    }

    public void addTable(Vector<String> accessNames, Vector<String> accessDataTypes, Vector<String> accessLengths, String tableName,
            Vector<Boolean> accessNotNull, Vector<Boolean> accessPrimaryKey) throws Exception
    {

        String sqlString = "CREATE TABLE " + tableName + "(";

        for (int i = 0; i < accessDataTypes.size(); i++)
        {
            if (accessDataTypes.get(i).equals("VARCHAR"))
            {
                sqlString += accessNames.get(i) + " " + accessDataTypes.get(i) + "(" + accessLengths.get(i) + ")";
            }
            else
            {
                sqlString += accessNames.get(i) + " " + accessDataTypes.get(i);
            }

            if (accessNotNull.get(i))
            {
                sqlString += " NOT NULL";
            }
            sqlString += ", \n";
        }

        sqlString = sqlString.substring(0, sqlString.length() - 2);
        sqlString += "\n PRIMARY KEY (";

        if (accessPrimaryKey.contains(true))
        {
            for (int i = 0; i < accessDataTypes.size(); i++)
            {
                if (accessPrimaryKey.get(i))
                {
                    sqlString += accessNames.get(i) + ", ";
                }
            }
            sqlString = sqlString.substring(0, sqlString.length() - 2);
        }
        else
        {
            sqlString += accessNames.get(0);
        }

        sqlString += "))";

        Statement statement = dataBase.getStatement();
        statement.execute(sqlString);
        dataBase.releaseStatement(statement);
    }

    public String executeStatement(String sqlString) throws Exception
    {
        String result = "SUCCESS.";

        Statement statement = dataBase.getStatement();
        statement.execute(sqlString);
        dataBase.releaseStatement(statement);

        return result;
    }
}
