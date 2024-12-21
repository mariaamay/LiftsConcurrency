import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Lift {
    int number;
    int currentFloor;
    BlockingQueue<Request> people;
    int toFloor;

    public Lift(int number, int currentFloor) {
        this.number = number;
        this.currentFloor = currentFloor;
        this.toFloor = currentFloor;
        this.people = new ArrayBlockingQueue<>(100);
    }
}
