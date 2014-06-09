/**
 * Main class of the Project MarketSim.
 * MarketSim is a light-weight market simulator used to test new algorithmic
 * trading strategies.
 * Supports only one stock and only buy and sell orders.
 * Only supports orders with a limit price and does not support revisions or 
 * cancellations of orders that have been submitted.
 */

package marketsim;

import objects.Order;
import utilities.Trade;

/**
 *
 * @author Gage Kehoe
 */
public class MarketSim 
{
    /**
     * @param args
     * String side
     * int quantity
     * double price
     */
    public static void main(String[] args) 
    {
        //send args for validation
        if(validateArgs(args))
        {
            //get args into variables to create an Order
            String side = args[0];
            String tempQty = args[1];
            int qty = Integer.parseInt(tempQty);
            String tempPrice = args[2];
            double price = Double.parseDouble(tempPrice);
            
            //creating order with the args
            //we make ID 0 because the database will be assigning that value
            Order newOrder = new Order(0, side, qty, price);
            
            //sending the order to be processed
            Trade.processTrades(newOrder);
        }
    }
    
    // validates the values in the arguments
    // if more or less args are ever needed just change the 3 to match number of
    // args and add to/remove from/rearange the switch statement
    public static boolean validateArgs(String[] args)
    {
        //checking for 3 arguments
        if( args.length != 3 )
        {
            System.err.println("Invalid number of arguments.");
            System.err.println("Enter: [side] [quantity] [price]");
            return false;
        }
        for(int i = 0; i < args.length; i++)
        {
            switch(i)
            {
                //checks for first argument to be s,b,S, or B
                case 0:
                {
                    String tempStr = args[0];
                    tempStr = tempStr.toUpperCase();
                    if( tempStr.compareTo("B") != 0 && tempStr.compareTo("S") != 0 )
                    {
                        System.err.println("Invalid input for side.");
                        System.err.println("Enter: 'B' or 'S'");
                        return false;
                    }
                    break;
                }
                //checks that second argument is an integer larger then 0
                case 1:
                {
                    String tempStr = args[1];
                    int qty;
                    try
                    {
                        qty = Integer.parseInt(tempStr);
                    }
                    //if exception thrown argument is not an integer
                    catch(NumberFormatException e)
                    {
                        System.err.println("You must enter an integer for quantity.");
                        return false;
                    }
                    //checking if the integer is over 0
                    if( qty < 1 )
                    {
                        System.err.println("Quantity must be larger then 0.");
                        return false;
                    }
                    break;
                }
                //checks that the third argument is a number with up to 3 decimals
                //that is larger then 0
                case 2:
                {
                    String tempStr = args[2];
                    double price;
                    try
                    {
                        price = Double.parseDouble(tempStr);
                    }
                    //checking if argument is a number (double)
                    catch(NumberFormatException e)
                    {
                        System.err.println("You must enter a double for price.");
                        return false;
                    }
                    //making sure double is larger then 0
                    if( price <= 0 )
                    {
                        System.err.println("Price must be larger then 0.");
                        return false;
                    }
                    //looking for '.' for decimal places
                    int index = tempStr.indexOf(".");
                    if( index > -1)
                    {
                        //found decimal place
                        String endStr = tempStr.substring(index + 1);
                        //making sure there are no more then 3 decimal places
                        if( endStr.length() > 3 )
                        {
                            System.err.println("Too many decimal places. Will only accept up to 3 decimals.");
                            System.err.println("Ex:9.999");
                            return false;
                        }
                    }
                    break;
                }
                default:
                {
                    System.err.println("An unknown error has occured.");
                    return false;
                }
            }
        }
        //passed all checks
        return true;
    }    
}
