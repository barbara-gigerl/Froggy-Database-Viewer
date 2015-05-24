package database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBase
{
    private CashedConnection cashedConnection;
    private final String fileName = System.getProperty("user.dir")
            + File.separator + "src"
            + File.separator + "data"
            + File.separator + "dbproperties.properties";
    private String DB_Driver;
    private String DB_Username;
    private String DB_Password;
    private String DB_Url;
    private static DataBase theInstance = null;

    public static DataBase getInstance() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        if (theInstance == null)
        {
            theInstance = new DataBase();
        }

        return theInstance;
    }

    private DataBase() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        loadProperties();
        Class.forName(DB_Driver);
    }

    public void connect(String DB_Name) throws SQLException
    {
        Connection connection = DriverManager.getConnection(DB_Url + DB_Name, DB_Username, DB_Password);
        cashedConnection = new CashedConnection(connection);
    }

    public void disconnect()
    {
        cashedConnection = null;
    }

    private void loadProperties() throws FileNotFoundException, IOException
    {
        Properties properties = new Properties();
        FileInputStream fileInputStream = new FileInputStream(fileName);
        properties.load(fileInputStream);
        DB_Driver = properties.getProperty("driver");
        DB_Username = properties.getProperty("username");
        DB_Password = properties.getProperty("password");
        DB_Url = properties.getProperty("url");
    }

    public static void main(String[] args)
    {
        DataBase database;

        try
        {
            database = new DataBase();
            database.loadProperties();
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(DataBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Statement getStatement() throws Exception
    {
        if (cashedConnection != null)
        {
            return cashedConnection.getStatement();
        }
        else
        {
            throw new Exception("Not connected");
        }
    }

    public void releaseStatement(Statement statement) throws Exception
    {
        if (cashedConnection != null)
        {
            cashedConnection.releaseStatement(statement);
        }
        else
        {
            throw new Exception("Not connected");
        }
    }
}
