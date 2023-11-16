
public class Bill {
    final double value;
    int amount;

    public Bill(double value, int amount){
        this.value = value;
        this.amount = amount;
    }

    
    public void add(int num){
        this.amount = this.amount + num;
    }

    public int getAmount(){
        return this.amount;
    }

    public double getValue(){
        return this.value;
    }

    //returns amount of bills. need to be changed, changes amount instead
    public int exchange(Bill bill){
        return (int) (this.getValue() / bill.getValue());
    }

    public boolean isThereEnough(int amount){
        return this.getAmount() >= amount;
    }

}
