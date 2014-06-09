/**
 * Contains all the necessary methods needed to make all trades and print out 
 * the results
 */

package utilities;

import java.util.ArrayList;

import objects.Order;

/**
 *
 * @author Gage Kehoe
 */
public class Trade 
{
    //trading method called from main
    public static void processTrades(Order order)
    {
        //puts the order in the database
         Database.inputOrder(order);
         //executes any trades that can happen
         executeTrades(order.getSide());
         //prints the orderbook after  the trades have completed
         printOrderbook();
    }
    
    //based on what order is inputed it does a sell trade or buy trade
    public static void executeTrades(String side)
    {
        if( side.compareTo("S") == 0 )
        {
            executeTradeSell();
        }
        else
        {
            executeTradeBuy();
        }
    }
    
    //trading algorithm for a buy order
    public static void executeTradeBuy()
    {
        ArrayList<Order> buyOrders = new ArrayList<Order>();
        ArrayList<Order> sellOrders = new ArrayList<Order>();
        
        //gets buy and sell orders from the database
        buyOrders = Database.getOrders("B");
        sellOrders = Database.getOrders("S");
            
        //order the sells in ascending order because when a buy comes in you
        //want to match it with the most aggressive orders in the book (the
        //lowest priced sells)
        sellOrders = Sort.sortOrderAsc(sellOrders);
        
        for(int s = 0; s < sellOrders.size(); s++)
        {
            Order sellOrder = sellOrders.get(s);
            for(int b = 0; b < buyOrders.size(); b++)
            {
                Order buyOrder = buyOrders.get(b);
                //do trade if the sellOrder price is less than or equal to buyOrder price
                if( sellOrder.getPrice() <= buyOrder.getPrice() )
                {
                    //did not want to deal with negatives so find out which has a larger quantity
                    if( sellOrder.getQty() <= buyOrder.getQty() )
                    {
                        int remainder = buyOrder.getQty() - sellOrder.getQty();
                        if( remainder > 0 )
                        {
                            //print the trade that has made place
                            System.out.println( sellOrder.getQty() + "@" + sellOrder.getPrice());
                            //remainder is whats left of buyOrder's quantity sellOrder's quantity is 0
                            //update buyOrder's quantity and remove sellOrder in database
                            Database.updateRowQty( buyOrder.getId(), remainder );
                            Database.removeRow(sellOrder.getId());
                            //updates buyOrder in the ArrayList
                            buyOrder.setQty(remainder);
                            buyOrders.set(b, buyOrder);
                            break;
                        }
                        else if( remainder == 0 )
                        {
                            //print trade that has taken place
                            System.out.println( sellOrder.getQty() + "@" + sellOrder.getPrice());
                            //both sell and buy Order's quantities are now 0
                            //remove both from database and ArrayLists
                            Database.removeRow(buyOrder.getId());
                            Database.removeRow(sellOrder.getId());
                            buyOrders.remove(b);
                            sellOrders.remove(s);
                            break;
                        }
                    }
                    else
                    {
                        int remainder = sellOrder.getQty() - buyOrder.getQty();
                        if( remainder > 0 )
                        {
                            //print trade that has taken place
                            System.out.println( buyOrder.getQty() + "@" + sellOrder.getPrice());
                            //remainder is whats left of the sellOrder's quantity
                            //update the sellorder in database and the ArrayList
                            Database.updateRowQty(sellOrder.getId(), remainder);
                            sellOrder.setQty(remainder);
                            sellOrders.set(s, sellOrder);
                            //buyOrder's quantity is 0 so remove it from the database
                            Database.removeRow(buyOrder.getId());
                            break;
                        }
                    }
                }
            }
        }
    }
    
    //trading algorithm for a sell order
    public static void executeTradeSell()
    {
        ArrayList<Order> buyOrders = new ArrayList<Order>();
        ArrayList<Order> sellOrders = new ArrayList<Order>();
        
        //gets buy and sell orders from the database
        buyOrders = Database.getOrders("B");
        sellOrders = Database.getOrders("S");
            
        //order the buys in descending order because when a sell comes in you
        //want to match it with the most aggressive orders in the book (the
        //highest priced buys)
        buyOrders = Sort.sortOrderDesc(buyOrders);
        
        for(int b = 0; b < buyOrders.size(); b++)
        {
            Order buyOrder = buyOrders.get(b);
            for(int s = 0; s < sellOrders.size(); s++)
            {
                Order sellOrder = sellOrders.get(s);
                //do trade if the sellOrder price is less than or equal to buyOrder price
                if( sellOrder.getPrice() <= buyOrder.getPrice() )
                {
                    //did not want to deal with negatives so find out which has a larger quantity
                    if( sellOrder.getQty() <= buyOrder.getQty() )
                    {
                        int remainder = buyOrder.getQty() - sellOrder.getQty();
                        if( remainder > 0 )
                        {
                            //print trade that has taken place
                            System.out.println( sellOrder.getQty() + "@" + buyOrder.getPrice());
                            //remainder is whats left of buyOrder's quantity sellOrder's quantity is 0
                            //update buyOrder's quantity and remove sellOrder in database and ArrayList
                            Database.updateRowQty( buyOrder.getId(), remainder );
                            Database.removeRow(sellOrder.getId());
                            sellOrders.remove(s);
                            break;
                        }
                        else if( remainder == 0 )
                        {
                            //print trade that has taken place
                            System.out.println( sellOrder.getQty() + "@" + buyOrder.getPrice());
                            //both sell and buy Order's quantities are now 0
                            //remove both from database and ArrayLists
                            Database.removeRow(buyOrder.getId());
                            Database.removeRow(sellOrder.getId());
                            buyOrders.remove(b);
                            sellOrders.remove(s);
                            break;
                        }
                    }
                    else
                    {
                        int remainder = sellOrder.getQty() - buyOrder.getQty();
                        if( remainder > 0 )
                        {
                            //print trade that has taken place
                            System.out.println( buyOrder.getQty() + "@" + buyOrder.getPrice());
                            //remainder is whats left of the sellOrder's quantity
                            //update the sellorder in database and the ArrayList
                            Database.updateRowQty(sellOrder.getId(), remainder);
                            sellOrder.setQty(remainder);
                            sellOrders.set(s, sellOrder);
                            //buyOrder's quantity is 0 so remove it from the database
                            Database.removeRow(buyOrder.getId());
                            break;
                        }
                    }
                }
            }
        }
    }

    //prints the orderbook to the standard output
    public static void printOrderbook()
    {
        ArrayList<Order> buyOrders = new ArrayList<Order>();
        ArrayList<Order> sellOrders = new ArrayList<Order>();
        
        //gets all orders out of the database
        buyOrders = Database.getOrders("B");
        sellOrders = Database.getOrders("S");
        
        //combines all quantities of orders with the same price
        buyOrders = Sort.combineLikePrices(buyOrders);
        sellOrders = Sort.combineLikePrices(sellOrders);
            
        //sorts buyOrders in descending order and sellOrders in ascending order
        buyOrders = Sort.sortOrderDesc(buyOrders);
        sellOrders = Sort.sortOrderAsc(sellOrders);
        
        //prints out Buy/Sell title
        System.out.println("      Buy      |      Sell     ");
        //figure out which ArrayList is larger so we can work with that one
        if( buyOrders.size() > sellOrders.size() )
        {
            //buyOrders is larger
            for(int i = 0; i < buyOrders.size(); i++)
            {
                //check to see if we have exceeded sellOrders size
                if(sellOrders.size() < (i+1))
                {
                    //no more sellOrders so print out the buyOrder with no sellOrder
                    System.out.println(buyOrders.get(i).getQty() + "@" + buyOrders.get(i).getPrice() + "        |");
                }
                else
                {
                    //there are still sellOrders so print out both buyOrder and sellOrder
                    System.out.println(buyOrders.get(i).getQty() + "@" + buyOrders.get(i).getPrice() + "        |  " + sellOrders.get(i).getQty()+ "@" + sellOrders.get(i).getPrice());
                }
            }
        }
        else
        {
            //sellOrders is larger
            for(int i = 0; i < sellOrders.size(); i++)
            {
                //check to see if we have exceeded buyOrders size
                if(buyOrders.size() < (i+1))
                {
                    //no more buyOrders so print out the buyOrder with no sellOrder
                    System.out.println( "               |  " + sellOrders.get(i).getQty() + "@" + sellOrders.get(i).getPrice());
                }
                else
                {
                    //there are still buyOrders so print out both buyOrder and sellOrder
                    System.out.println(buyOrders.get(i).getQty() + "@" + buyOrders.get(i).getPrice() + "        |  " + sellOrders.get(i).getQty()+ "@" + sellOrders.get(i).getPrice());
                }
            }
        }
    }
}
