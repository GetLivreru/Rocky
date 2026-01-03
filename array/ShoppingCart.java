package array;

public class ShoppingCart {
    public static void main(String[] args) {
    ShoppingCart c = new ShoppingCart(3);

    c.addItem(0,"Earth");
    c.addItem(1,"Water");
    c.addItem(2,"Fire");


        System.out.println(c.getItem(1));
        System.out.println(c.getSize());
    }
    private final String[] items;

    public ShoppingCart(int size) {
        this.items = new String[size];
    }
    public void addItem(int index, String item) {
        items[index] = item;
    }
    public String getItem(int index){
        return items[index];
    }
    public int getSize(){
        return items.length;
    }
}
