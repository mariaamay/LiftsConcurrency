import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class RequestGenerator extends Thread {
    private AtomicBoolean isRun;
    private final int avgRequestTime;
    private final int MAX_FLOOR;
    AtomicReferenceArray<BlockingQueue<Request>> building;

    public RequestGenerator(AtomicBoolean isRun, int avgRequestTime, int MAX_FLOOR, AtomicReferenceArray<BlockingQueue<Request>> building) {
        this.isRun = isRun;
        this.avgRequestTime = avgRequestTime;
        this.MAX_FLOOR = MAX_FLOOR;
        this.building = building;
    }

    public void run() {
        Random random = new Random(28);
        while (isRun.get()) {
            // Generate new request
            int fromFloor = random.nextInt(MAX_FLOOR) + 1;
            int toFloor = random.nextInt(MAX_FLOOR) + 1;
            while (fromFloor == toFloor) {
                toFloor = random.nextInt(MAX_FLOOR) + 1;
            }
            Request newRequest = new Request(fromFloor, toFloor);

            // Add new request to the queue
            BlockingQueue<Request> updatedFloor = building.get(fromFloor);
            updatedFloor.add(newRequest);
            building.set(fromFloor, updatedFloor);

            // Sleep for randomTime ~ averageRequestTime
            int sleepTime = random.nextInt(avgRequestTime / 4) + avgRequestTime;
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }

        }
    }

}
