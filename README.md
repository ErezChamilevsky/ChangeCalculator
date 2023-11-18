# ChangeCalculator
A cash desk that returns the bills and coins that make the change, by the restrictions of the desk's content.

# **Background**
Ocassionaly, while making a transaction, the cashier needs to return a change. In many countries there are machines that make it automatically - they consider the bills and coins in the cash desk and return it.

# **Logic**
In order to find out what bills and coins to return, first it will find the optimal composition of the return - without any restrictions, as least components as possiblle. After that, it will correct the change as the situation in the cash box. 
Finding the optimal - kind of bites/array scan.
Correct the optimal - for every bill - checks if there are enough bills in the cash (from this component) - if it is short - making a exchange to lower bill. 

Notice -
Bills and coins in Israel are 200, 100, 50, 20, 10, 5, 2, 1, 0.5, 0.1. Could be a problem to exchange (for example) 100 to 50,20*2,10 - therefore the algorithem is not perfect - it means that it needed to be adjust for every country. On the other side, it returns the change in O(1).
