package quadtree;

import java.util.ArrayList;

public class Node {

    private final Point origin;
    private final int width;
    private final int height;
    private final ArrayList<Point> points;
    private final ArrayList<Node> nodes;
    private int deepness;

    public int getDeepness() {
        return deepness;
    }

    public void setDeepness(int deepness) {
        this.deepness = deepness;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public Point getOrigin() {
        return origin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Node(Point o, int w, int h) {
        this.origin = o;
        this.width = w;
        this.height = h;
        nodes = new ArrayList<>();
        points = new ArrayList<>();
    }

    public boolean contains(Point p) {
        int px = p.getX();
        int py = p.getY();
        return px >= origin.getX() && py >= origin.getY() && px <= origin.getX() + width && py <= origin.getY() + height;
    }

    ArrayList<Point> getPointsBut(Point point) {
        ArrayList<Point> result = new ArrayList<>();
        for (Point childPoint : getPoints()) {
            if (childPoint.getX() != point.getX() || childPoint.getY() != point.getY()) {
                result.add(childPoint);
            }
        }
        return result;
    }
    
    protected int addPoint(Point point) {
        int maxdeep = getDeepness();
        if (contains(point)) {
            if (getNodes().isEmpty()) {
                getPoints().add(point);
                if (getPoints().size() > 4) {
                    int w = getWidth() / 2;
                    int h = getHeight() / 2;
                    Node child1 = new Node(new Point(getOrigin().getX(), getOrigin().getY()), w, h);
                    Node child2 = new Node(new Point(getOrigin().getX(), getOrigin().getY()).translate(w, 0), w, h);
                    Node child3 = new Node(new Point(getOrigin().getX(), getOrigin().getY()).translate(0, h), w, h);
                    Node child4 = new Node(new Point(getOrigin().getX(), getOrigin().getY()).translate(w, h), w, h);
                    child1.setDeepness(getDeepness() + 1);
                    child2.setDeepness(getDeepness() + 1);
                    child3.setDeepness(getDeepness() + 1);
                    child4.setDeepness(getDeepness() + 1);
                    if (getDeepness() + 1 > maxdeep) {
                        maxdeep = getDeepness() + 1;
                    }
                    getNodes().add(child1);
                    getNodes().add(child2);
                    getNodes().add(child3);
                    getNodes().add(child4);
                    ArrayList<Point> tmp = new ArrayList<>();
                    tmp.addAll(getPoints());
                    getPoints().clear();
                    for (Point childPoint : tmp) {
                        addPoint(childPoint);
                    }
                }
            } else {
                int isIn = pointIsInHowMuchChildNodes(point);
                if (isIn < 2) {
                    for (Node childNode : getNodes()) {
                        childNode.addPoint(point);
                    }
                } else {
                    getPoints().add(point);
                }
            }
        }
        return maxdeep;
    }

    protected int pointIsInHowMuchChildNodes(Point point) {
        int isIn = 0;
        for (Node childNode : getNodes()) {
            if (childNode.contains(point)) {
                isIn++;
            }
        }
        return isIn;
    }
}
