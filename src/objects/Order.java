/**
 * class Order
 * used to create and manipulate Order objects
 */

package objects;

/**
 *
 * @author Gage Kehoe
 */
public class Order 
{
    //member variables
    private int m_Id;
    private String m_Side;
    private int m_Qty;
    private double m_Price;
    
    //Constructor
    public Order( int id, String side, int qty, double price )
    {
        m_Id    = id;
        m_Side  = side.toUpperCase();
        m_Qty   = qty;
        m_Price = price;
    }
    
    //Setters
    public void setSide(String side)
    {
        m_Side = side;
    }
    
    public void setQty(int qty)
    {
        m_Qty = qty;
    }
    
    public void setPrice(double price)
    {
        m_Price = price;
    }
    
    //Getters
    public int getId()
    {
        return m_Id;
    }
    
    public String getSide()
    {
        return m_Side;
    }
    
    public int getQty()
    {
        return m_Qty;
    }
    
    public double getPrice()
    {
        return m_Price;
    }
}
