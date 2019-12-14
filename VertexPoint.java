public class VertexPoint {
    public int x;
    public int y;
    public int name;

    public VertexPoint(int a, int b, int n) {
        x = a;
        y = b;
        name = n;
    }

    public int getDistance(VertexPoint other) {
        return (int) Math.sqrt((other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y));
    }
}