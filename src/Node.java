public class Node implements Comparable<Node>{
    int row,col;
    Node parent;
    double gCost; // Distance from start
    double hCost; // Distance to goal(heuristic)

    public Node(int row , int col , Node parent){
        this.row = row;
        this.col = col;
        this.parent = parent;

        // Initialize costs to 0 by default (helpful for BFS/DFS)
        this.gCost = 0;
        this.hCost = 0;
    }

    // Node sınıfının içine:
    public void setHeuristic(Node target) {
        double X = Math.pow(this.row - target.row, 2);
        double Y = Math.pow(this.col - target.col, 2);
        this.hCost = Math.sqrt(X + Y);
    }

    public double getFCost() {
        return gCost + hCost;
    }

    @Override
    public int compareTo(Node other){

        if(this.getFCost() > other.getFCost()){
            return 1;
        } else if (this.getFCost() < other.getFCost()) {
            return -1;
        }
        return Double.compare(this.hCost, other.hCost);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        Node other = (Node) obj;
        return this.row == other.row && this.col == other.col;
    }


    @Override
    public int hashCode(){
        return 31 * row + col;
    }

}
