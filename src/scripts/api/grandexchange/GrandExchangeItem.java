package scripts.api.grandexchange;

public class GrandExchangeItem {
    private int ID, quantity, price;
    public GrandExchangeItem(int ID, int quantity, int price){
        this.ID = ID;
        this.quantity = quantity;
        this.price = price;
    }

    public int getPrice(){
        return price;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
