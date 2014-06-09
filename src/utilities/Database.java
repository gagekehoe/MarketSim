/**
 * class Database
 * Contains all the methods needed to connect to the database and make necessary 
 * manipulations to the database.
 */

package utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import objects.Order;

/**
 *
 * @author Gage Kehoe
 */
public class Database 
{
    //member variables
    static final String m_Url = "jdbc:mysql://localhost:3306/"; 
    static final String m_DbName = "marketsim"; 
    static final String m_Driver = "com.mysql.jdbc.Driver";
    static final String m_UserName = "root";
    static final String m_Password = "root";
    
    //removes a row from the Orderbook table at the id
    public static void removeRow(int id)
    {
        //query to remove row
        String removeRowQuery = "DELETE FROM orderbook WHERE id = ?";
        //connect to DB
        Connection conn = connectToDB();
        //check to see if we connected
        if( conn != null )
        {
            try
            {
                //setting up the prepared statement to send to the database
                PreparedStatement statement = conn.prepareStatement(removeRowQuery);
                //set id for the ? in the query
                statement.setInt(1, id);
                //execute query
                statement.executeUpdate();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            //close connection to the database
            closeDB(conn);
        }
    }
    
    //updates a rows qty in the database
    public static void updateRowQty(int id, int qty)
    {
        //update query
        String updateRowQuery = "UPDATE orderbook SET qty = ? WHERE id = ?";
        //connect to the DB
        Connection conn = connectToDB();
        //check to see if we connected
        if( conn != null )
        {
            try
            {
                //setting up the prepared statement to send to the database
                PreparedStatement statement = conn.prepareStatement(updateRowQuery);
                //setting ?s to qty and id. qty is 1, id is 2
                statement.setInt(1, qty);
                statement.setInt(2, id);
                //execute the query
                statement.executeUpdate();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            //close connection to the database
            closeDB(conn);
        }
    }
    
    //gets the orders based off what side is passed in that are currently in the
    //orderbook table and returns an ArrayList of those Orders
    public static ArrayList getOrders(String side)
    {
        ArrayList<Order> orders = new ArrayList<Order>();
        //get orders by side query
        String getOrdersQuery = "SELECT * FROM orderbook WHERE side = ?";
        //connect to the DB
        Connection conn = connectToDB();
        //check to see if we connected
        if( conn != null )
        {
            try
            {
                //setting up the prepared statement to send to the database
                PreparedStatement statement = conn.prepareStatement(getOrdersQuery);
                //set side for the ? in the query
                statement.setString(1, side);
                //execute the query
                //ResultSet rs holds the orders recieved from the database
                ResultSet rs = statement.executeQuery();
                //iterate through the ResultSet until no more exist
                while(rs.next())
                {
                    //create an order from the current set in ResultSet
                    //add it to the orders ArrayList
                    Order tempOrder = new Order(rs.getInt("id"), rs.getString("side"), rs.getInt("qty"), rs.getDouble("price"));
                    orders.add(tempOrder);
                }
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            //close connection to the database
            closeDB(conn);
        }
        //returning orders ArrayList
        return orders; 
    }   
    
    //whats better?
    //public static void inputOrder( String side, int qty, double price)
    //puts a new order into the database
    public static void inputOrder( Order order )
    {
        //input query to be sent to the database
        String inputQuery = "INSERT INTO orderbook (side, qty, price) VALUES (?, ?, ?)";
        //connect to database
        Connection conn = connectToDB();
        //check to see if we connected
        if( conn != null )
        {
            try
            {
                //setting up the prepared statement to send to the database
                PreparedStatement statement = conn.prepareStatement(inputQuery);
                //set first ? to side, second to qty, and third to price
                statement.setString(1, order.getSide());
                statement.setInt(2, order.getQty());
                statement.setDouble(3, order.getPrice());
                //send the query to the database
                statement.executeUpdate();
            }
            catch(SQLException e)
            {
                e.printStackTrace();
            }
            //close connection to the database
            closeDB(conn);
        }
    }
    
    //closes the connection to the database
    public static void closeDB(Connection conn)
    {
        try
        {
            conn.close();
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    //connects to the database using the given 
    public static Connection connectToDB()
    {
        //set connection to null so we can return a null connection if it fails
        Connection conn = null;
        try
        {
            //force class representing MySQL to load and initialize
            Class.forName(m_Driver).newInstance();
            //connecting to the database
            conn = DriverManager.getConnection(m_Url+m_DbName, m_UserName, m_Password);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        //returning the connection
        return conn;
    }
}
