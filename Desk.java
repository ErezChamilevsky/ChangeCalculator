import java.util.ArrayList;
import java.util.Scanner;

public class Desk {
    private static Desk instance; // singleton
    ArrayList<Bill> bills;
    private Scanner scanner;

    private Desk() {
        this.bills = new ArrayList<Bill>();
        Desk.instance = this;
        this.scanner = new Scanner(System.in);
    }

    static Desk getInstance() {
        if (instance == null) {
            Desk box = new Desk();
            box.initBox();
        }
        return instance;
    }

    public void initBox() {
        System.out.println("Initialize Box:");
        System.out.println("E - empty \t D - default \t M - manual");
        String answer = "e";
        if (this.scanner.hasNextLine()) {
            answer = this.scanner.nextLine();
        }
        if (answer.equals("M") || answer.equals("m")) {
            emptyInit();
            manuaInit();
        } else if (answer.equals("E") || answer.equals("e")) {
            emptyInit();
        } else {
            defaultInit();
        }
        System.out.println();
    }

    private void emptyInit() {
        this.bills.add(new Bill(200, 0));
        this.bills.add(new Bill(100, 0));
        this.bills.add(new Bill(50, 0));
        this.bills.add(new Bill(20, 0));
        this.bills.add(new Bill(10, 0));
        this.bills.add(new Bill(5, 0));
        this.bills.add(new Bill(2, 0));
        this.bills.add(new Bill(1, 0));
        this.bills.add(new Bill(0.5, 0));
        this.bills.add(new Bill(0.1, 0));
    }

    void defaultInit() {
        this.bills.add(new Bill(200, 5));
        this.bills.add(new Bill(100, 5));
        this.bills.add(new Bill(50, 5));
        this.bills.add(new Bill(20, 5));
        this.bills.add(new Bill(10, 5));
        this.bills.add(new Bill(5, 5));
        this.bills.add(new Bill(2, 5));
        this.bills.add(new Bill(1, 5));
        this.bills.add(new Bill(0.5, 5));
        this.bills.add(new Bill(0.1, 5));
    }

    private void manuaInit() {
        for (Bill a : this.bills) {
            System.out.print("Number of " + a.value + "'s added: ");
            if (this.scanner.hasNextInt()) {
                a.add(this.scanner.nextInt());
            }
        }
    }

    public double sumArray(int[] array) {
        int count = 0;
        double sum = 0;
        for (int i : array) {
            sum += i * this.bills.get(count).getValue();
            count++;
        }
        return sum;
    }

    public double shortIn(int optimal, int possible) {
        return (optimal - possible) * 0.1;
    }

    public void gotPaid(double paid) {
        int[] billsPaidBy = this.optimalChange(paid);
        updateBox(billsPaidBy, +1);
    }

    public void paying() {
        System.out.print("Price: ");
        double price = 0;
        double paid = 0;
        if (this.scanner.hasNextDouble()) {
            price = scanner.nextDouble();
        }
        System.out.print("Paid: ");
        if (this.scanner.hasNextDouble()) {
            paid = this.scanner.nextDouble();
        }
        changeCalculator(price, paid);
    }

    public void changeCalculator(double price, double paid) {
        this.gotPaid(paid);

        int[] optimal = this.optimalChange(paid - price);
        int[] possible = this.correctOptimalChange(optimal);

        double miss = this.shortIn(optimal[9], possible[9]);
        if (price * 0.001 > miss && miss > 0.1) { // need to be asked
            if (!this.problem(miss)) {
                System.out.println("The transaction has been cancelled.");
                updateBox(this.optimalChange(paid), -1);
                return;
            }
        } else {
            updateBox(possible, -1);
        }
        printChange(possible);
    }

    public boolean problem(double miss) {
        System.out.println("Short in " + miss + ". What would you like to continue?");
        System.out.println("K/N");
        String answer = "n";
        if (this.scanner.hasNextLine()) {
            answer = this.scanner.nextLine();
        }
        if (answer.equals("K") || answer.equals("k")) {
            return true;
        }
        return false;
    }

    public int[] optimalChange(double difference) {
        int[] change = new int[10];
        for (int i = 0; i < 4; i++) {
            double billsChecked = difference % (1 * Math.pow(10, i));
            if (i != 3 && billsChecked >= 0.5 * Math.pow(10, i)) {
                change[8 - i * 3] = 1;
                billsChecked -= 0.5 * Math.pow(10, i);
            }

            if (i != 0 && billsChecked >= 0.2 * Math.pow(10, i)) {
                change[9 - i * 3] = (int) (billsChecked / (0.2 * Math.pow(10, i)));
                billsChecked -= change[9 - i * 3] * 0.2 * Math.pow(10, i);
            }

            if (i == 0) {
                float num = (float) (billsChecked / this.bills.get(9).value);
                change[9] = (int) (num);
            } else {
                change[10 - i * 3] = (int) (billsChecked / this.bills.get(10 - i * 3).value);
            }
        }
        return change;
    }

    public int[] correctOptimalChange(int[] change) {
        int[] temp = new int[10];

        for (int i = 0; i < 10; i++) {
            if (this.bills.get(i).isThereEnough(change[i])) {
                temp[i] = change[i];

            } else if (i == 2 || i == 5) { // 50's or 5's

                temp[i] = this.bills.get(i).getAmount();

                if ((change[i] - temp[i]) % 2 == 0) { // even
                    change[i + 1] += (change[i] - temp[i]) * 2.5; // allways be int cause the dif is even
                } else { // odd
                    for (int j = 0; j < (change[i] - temp[i]); j++) { // converting 5 to 2*2+1
                        change[i + 1] += 2;
                        change[i + 2] += 1;
                    }
                }

            } else if (i == 9) {// 0.1's - taking all possible
                temp[i] = this.bills.get(i).getAmount();
            } else { // no enough and no special case
                change[i + 1] += this.bills.get(i).exchange(this.bills.get(i + 1)) * (change[i] - temp[i]); // exchange
            }
        }
        return temp;
    }

    public void updateBox(int[] change, int sign) { // sign will be 1 or -1
        for (int i = 0; i < 10; i++) {
            this.bills.get(i).add(change[i] * sign);
        }
    }

    public void printChange(int[] change) {
        for (int i = 0; i < 10; i++) {
            System.out.println(this.bills.get(i).value + ":" + "\t" + change[i]);
        }
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public static void main(String[] args) {
        Desk a = Desk.getInstance();
        int menu = 0;
        while (menu != 3) {
            System.out.println();
            System.out.println("1. Initialize the cash desk.");
            System.out.println("2. Making a transaction.");
            System.out.println("3. STOP.");

            if (a.getScanner().hasNextInt()) {
                menu = a.getScanner().nextInt();
            } else {
                a.getScanner().next(); // Consume the invalid input
                continue; // Skip the rest of the loop and start a new iteration
            }

            switch (menu) {
                case 1:
                    a.initBox();
                    break;

                case 2:
                    a.paying();
                    break;

                case 3:
                    break;
            }
        }

        a.getScanner().close();
    }
}