import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;
import static java.lang.Math.*;

public class LiftThread extends Thread {
    public boolean isGoingToNewFloor = false;
    private AtomicReferenceArray<BlockingQueue<Request>> building;
    private AtomicBoolean isRun;
    public Lift lift;
    private final int MAX_FLOOR;

    public LiftThread(AtomicReferenceArray<BlockingQueue<Request>> building,
                      AtomicBoolean isRun,
                      Lift lift,
                      int MAX_FLOOR) {
        this.building = building;
        this.isRun = isRun;
        this.lift = lift;
        this.MAX_FLOOR = MAX_FLOOR;

    }

    public void run() {
        while (isRun.get()) {

            moveLift();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                System.exit(-3);
            }
        }

    }

    private void letPeopleOutOnCurrentFloor() {
        BlockingQueue<Request> people = lift.people;

        for (Request request: lift.people) {
            if (request.toFloor == lift.currentFloor) {
                try {
                    people.remove(request);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                    System.exit(-7);
                }
            }
        }
        lift.people = people;
    }

    private void takeNewPeopleOnCurrentFloor() {
        BlockingQueue<Request> current = building.get(lift.currentFloor);
        if (current.isEmpty()) {
            return;
        }

        boolean up;
        if (lift.people.isEmpty()) {
            Request inside = null;
            try {
                inside = current.take();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                System.exit(-4);
            }
            lift.people.add(inside);
            lift.toFloor = inside.toFloor;
            up = inside.up;
        } else {
            up = lift.people.peek().up;
        }

        for (Request request : current) {
            if (request.up == up) {
                lift.people.add(request);
                if (up) {
                    lift.toFloor = max(lift.toFloor, request.toFloor);
                } else {
                    lift.toFloor = min(lift.toFloor, request.toFloor);
                }
            }
        }

        for (Request request : lift.people) {
            try {
                current.remove(request);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                System.exit(-7);
            }
        }
        building.set(lift.currentFloor, current);
    }

    // To find the next floor (if the lift is empty), we count coefficients, than take the maximum one
    int[] countCoefficients() {
        int[] result = new int[MAX_FLOOR + 1];

        for (int floor = 1; floor <= MAX_FLOOR; ++floor) {
            BlockingQueue<Request> currentPeople = building.get(floor);
            if (floor == lift.currentFloor || currentPeople.isEmpty() || currentPeople.peek().taken) {
                result[floor] = -1;
                continue;
            }
            result[floor] = MAX_FLOOR - abs(lift.currentFloor - floor);
            result[floor] += currentPeople.size() * 4;
        }
        return result;
    }

    public synchronized void findNewDestination() {
        int[] coefficients = countCoefficients();

        int maxCoefficient = -1;
        int maxFloor = lift.currentFloor;
        for (int floor = 1; floor <= MAX_FLOOR; ++floor) {
            if (coefficients[floor] > maxCoefficient) {
                maxCoefficient = coefficients[floor];
                maxFloor = floor;
            }
        }
        lift.toFloor = maxFloor;
        if (maxCoefficient == -1) {
            return;
        }
        BlockingQueue<Request> tmp = building.get(maxFloor);
        if (!tmp.isEmpty()) {
            tmp.peek().taken = true;
        }
        building.set(maxFloor, tmp);
    }

    private void moveLift() {

        if (isGoingToNewFloor) {
            if (lift.currentFloor == lift.toFloor) {
                isGoingToNewFloor = false;
            } else {
                BlockingQueue<Request> destinationFloor = building.get(lift.toFloor);
                if (destinationFloor.isEmpty() || !destinationFloor.peek().taken) {
                    isGoingToNewFloor = false;
                }
            }
        }

        if (!isGoingToNewFloor) {
            letPeopleOutOnCurrentFloor();
            takeNewPeopleOnCurrentFloor();
        }

        if (lift.toFloor < lift.currentFloor) {
            --lift.currentFloor;
            return;
        }
        if (lift.toFloor > lift.currentFloor) {
            ++lift.currentFloor;
            return;
        }

        isGoingToNewFloor = true;
        findNewDestination();
    }

}
