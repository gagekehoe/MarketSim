/**
 * Has all the necessary methods needed to sort the ArrayLists of Orders
 */

package utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import objects.Order;

/**
 *
 * @author Gage Kehoe
 */
public class Sort implements Comparator<Order>
{
    //Overriding the Comparator for Order to compare Orders by their price
    @Override
    public int compare(Order order1, Order order2)
    {
        if(order1.getPrice() == order2.getPrice())
        {
            return 0;
        }
        else
        {
            return order1.getPrice() > order2.getPrice() ? 1 : -1;
        }
    }
    
    //sorts ArrayList of Orders in ascending order
    public static ArrayList sortOrderAsc( ArrayList<Order> orders )
    {
        //sorts in ascending order
        Collections.sort(orders, new Sort());
        //sorts like prices by the database id to put oldest order first and 
        //returns the given ArrayList of Orders
        return sortLikePrices(orders);
    }
    
    //sorts ArrayList of Orders in descending order
    public static ArrayList sortOrderDesc( ArrayList<Order> orders )
    {
        //sorts in ascending order
        Collections.sort(orders, new Sort());
        //reverse the ArrayList to get it in descending order
        Collections.reverse(orders);
        //sorts like prices by the database id to put oldest order first and 
        //returns the given ArrayList of Orders
        return sortLikePrices(orders);
    }
    
    //sorts like prices in the ArrayList so the oldest Order is seen first
    public static ArrayList sortLikePrices(ArrayList<Order> orders )
    {
        for( int i = 0; i < orders.size(); i++ )
        {
            //if we are on last iteration we dont need to check
            if( i != orders.size() -1 )
            {
                //checks to see if price of current Order is equal to the price 
                //of the next Order in the ArrayList
                if( orders.get(i).getPrice() == orders.get(i+1).getPrice() )
                {
                    //if current Order ID is larger then the next Order Id we need to swap them
                    if( orders.get(i).getId() > orders.get(i+1).getId() )
                    {
                        //swapping the two Orders
                        Order tempOrder = orders.get(i);
                        orders.set(i, orders.get(i+1));
                        orders.set(i+1, tempOrder);
                        //makes the lowest ID in the same price go to the top of its price
                        sortLikePrices(orders);
                    }
                }
            }
        }
        return orders;
    }
    
    //combines quantities of like priced orders to be displayed in the orderbook printout
    public static ArrayList<Order> combineLikePrices(ArrayList<Order> orders)
    {
        ArrayList<Order> tempOrders = new ArrayList<Order>();
        //flag to see if an Order was placed in the new ArrayList
        boolean placement = false;
        for(int o = 0; o < orders.size(); o++)
        {
            //only checking tempOrders if there are Orders in it
            if( tempOrders.size() > 0 )
            {
                //make sure placement flag is false before checking tempOrders
                placement = false;
                for(int t = 0; t < tempOrders.size(); t++)
                {
                    //check for price to be the same
                    if( orders.get(o).getPrice() == tempOrders.get(t).getPrice() )
                    {
                        //add orders quantity to tempOrders quantity
                        Order tempOrder = tempOrders.get(t);
                        tempOrder.setQty(tempOrder.getQty() + orders.get(o).getQty());
                        tempOrders.set(t, tempOrder);
                        //set placement true so we dont add Order to the tempOrders
                        placement = true;
                        break;
                    }
                }
                if( placement != true )
                {
                    //no tempOrder quantity was updated so add it to the tempOrders array
                    tempOrders.add(orders.get(o)); 
                }               
            }
            else
            {
                //tempOrtders was empty so place first Order in it
                tempOrders.add(orders.get(o));
            }
        }
        return tempOrders;
    }
}