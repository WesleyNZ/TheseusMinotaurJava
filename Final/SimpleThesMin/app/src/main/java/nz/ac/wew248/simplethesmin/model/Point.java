package nz.ac.wew248.simplethesmin.model;

public class Point implements Cloneable{
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(Point move) {
        this.x += move.xAxis();
        this.y += move.yAxis();
    }

    public int xAxis() {
        return this.x;
    }

    public int yAxis() {
        return this.y;
    }
    
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}