package enums;

public class enums {
    public static void main(String[] args) {

        OrderStatus currentStatus = OrderStatus.NEW;
            System.out.println("Can be process CANCELLED switch to NEW " + currentStatus.canTransitionTo(OrderStatus.CANCELLED));

    }
}

enum OrderStatus{
    NEW,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED;

    public boolean canTransitionTo(OrderStatus newStatus){
        switch (this){
            case NEW:
                return newStatus == PROCESSING ||  newStatus == CANCELLED;
            case PROCESSING:
                return  newStatus == SHIPPED ||  newStatus == CANCELLED;
            case SHIPPED:
                return newStatus == DELIVERED ||  newStatus == CANCELLED;
            case DELIVERED:
                return newStatus == CANCELLED;
            case CANCELLED:
                return false;
        }
        return false;
    }
}
