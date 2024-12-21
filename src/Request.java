public class Request {
    public int fromFloor;
    public int toFloor;
    public boolean up;
    public boolean taken = false;

    public Request(int fromFloor, int toFloor) {
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;

        up = fromFloor < toFloor;
    }
}
