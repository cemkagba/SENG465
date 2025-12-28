public class Node implements Comparable<Node>{
    int row,col;
    Node parent;
    int gCost; // Distance from start
    int hCost; // Distance to goal(heuristic)

    public Node(int row , int col , Node parent){
        this.row = row;
        this.col = col;
        this.parent = parent;

        // Initialize costs to 0 by default (helpful for BFS/DFS)
        this.gCost = 0;
        this.hCost = 0;
    }

    public int getFCost(){
        return gCost+hCost;
    }

    @Override
    public int compareTo(Node other){

        if(this.getFCost() > other.getFCost()){
            return 1;
        } else if (this.getFCost() < other.getFCost()) {
            return -1;
        }
        return Integer.compare(this.hCost, other.hCost);
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
