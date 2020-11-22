package ex1.src;


import java.util.*;
/** This class implements weighted_graph interface. This class represents undirected and weighted graph.**/
public class WGraph_DS implements weighted_graph {
    /** Holds the vertexes of this graph - when key holds the vertex's ID and value holds the associated node to this ID.
     * Using HashMap allow access to graph's nodes in O(1).
     * **/
    private HashMap<Integer, node_info> vertexes;
    /**Counter for number of nodes in graph.**/
    private int nodesSize;
    /**Counter for number of changes that accrued until now in graph.**/
    private int modeCount;
    /**Counter for number of edges in graph.**/
    private double edgesSize;
    private static int i = 0;

    /**constructor**/
    public WGraph_DS(){
        vertexes = new HashMap<>();
        nodesSize = 0;
        edgesSize = 0;
        modeCount = 0;
    }

    @Override
    /**Returns a node which associated to the given key in the graph.
     * @param key
     * @return node
     * **/
    public node_info getNode(int key) {
        if(!vertexes.containsKey(key)) return null;
        return vertexes.get(key);
    }

    @Override
    /**Returns true if there's an edge between two nodes, false if there isn't.
     * @param node1
     * @param node2
     * @return ans
     * **/
    public boolean hasEdge(int node1, int node2) {
        //if one of the nodes isn't in the graph
        if(this.getNode(node1)==null || this.getNode(node2)==null) return false;
        else{
            WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
            WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);
            //search in the nodes neighbors listen
            boolean ans = n1.neighbors.containsKey(n2);
            return ans;
        }
    }

    @Override
    /**Returns the existing edge between two nodes in the graph. If there isn't one, returns -1.
     * @param node1
     * @param node2
     * @return ans
     * **/
    public double getEdge(int node1, int node2) {
        if(!this.hasEdge(node1, node2)) return -1;
        WGraph_DS.NodeInfo n1 = (WGraph_DS.NodeInfo) this.getNode(node1);
        WGraph_DS.NodeInfo n2 = (WGraph_DS.NodeInfo) this.getNode(node2);
        //return the edge weight which associate to this node in the neighbors list
        double ans = n1.neighbors.get(n2);
        return ans;
    }

    @Override
    /**Adds a new node to this graph. In case an identical node exist(same ID), does nothing.
     * @param key
     **/
    public void addNode(int key) {
        //if node with the same key already exist in this graph
        if(this.getNode(key)!=null) return;
        WGraph_DS.NodeInfo newNode = new NodeInfo(key);
        //adding to the vertexes list
        vertexes.put(key, newNode);
        nodesSize++;
        modeCount++;
    }

    @Override
    /**Connect between two nodes in this graph with a given weight.
     * @param node1
     * @param node2
     * @param w
     * **/
    public void connect(int node1, int node2, double w) {
        //if one of the nodes isn't in the graph
        if(this.getNode(node1)==null || this.getNode(node2)==null){
            return;
        }
        //if input weight is negative or if edge with the same weight already exist
        if(w<0 || w == this.getEdge(node1, node2) ) return;

        //if node1 equals node2 - do nothing
        if(node1==node2) return;

        else {
            WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
            WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);

            //if there's existing edge - update
            if (this.getEdge(node1, node2) != -1) {
                n1.neighbors.replace(n2, w);
                n2.neighbors.replace(n1, w);
            }
            else {
                n1.neighbors.put(n2, w);
                n2.neighbors.put(n1, w);
                edgesSize++;
            }
            modeCount++;
        }
    }

    @Override
    /**Returns a pointer to this graph neighbors collection.
     * @return pointer
     * **/
    public Collection<node_info> getV() {
        //creating shallow copy
        Collection<node_info> pointer = vertexes.values();
        return pointer;
    }

    @Override
    /**Returns a pointer to given node's neighbors list.
     * @param node_id
     * @return Collection<node_data>
     * **/
    public Collection<node_info> getV(int node_id) {
        //getting the node
        WGraph_DS.NodeInfo node = (NodeInfo) this.getNode(node_id);
        //shallow copy to the node's neighbors
        Collection<node_info> pointer = node.neighbors.keySet();
        return pointer;
    }

    @Override
    /**Removes a node from this graph. This method will remove all existing edges between the given node to it's neighbors.
     * @param key
     * @return node
     */
    public node_info removeNode(int key) {
        if(this.getNode(key) == null) return null;
        WGraph_DS.NodeInfo node = (NodeInfo) this.getNode(key);
        //going over all of nodes neighbors
        Iterator<node_info> it =  this.getV(key).iterator();
        while (it.hasNext()){
            node_info neighbor = it.next();//current neighbor
            int neighborKey = neighbor.getKey();
            it.remove();
            this.removeEdge(key, neighbor.getKey());
        }
        this.vertexes.remove(key);
        nodesSize--;
        modeCount++;
        return node;
        }

    @Override
    /**Removes an edge between Two nodes (if exists).
     * @param node1
     * @param node2
     * **/
    public void removeEdge(int node1, int node2) {
        if(this.getNode(node1)==null || this.getNode(node2)==null) return;
        WGraph_DS.NodeInfo n1 = (NodeInfo) this.getNode(node1);
        WGraph_DS.NodeInfo n2 = (NodeInfo) this.getNode(node2);
        n1.neighbors.remove(n2);
        n2.neighbors.remove(n1);
        edgesSize--;
        modeCount++;
    }


    @Override
    /**Override Equals method. Two graphs will considered equal if they have the exact same properties.
     * @param graph
     * @return flag
     */
    public boolean equals(Object graph){
        //making sure the input object is weighted_graph object
        if(!(graph instanceof weighted_graph)){
            return false;
        }
        weighted_graph g = (WGraph_DS) graph;
        //basic condition to be equals
        if(this.edgeSize() != g.edgeSize() || this.nodeSize() != g.nodeSize()) return false;

        boolean flag = true;
        Collection<node_info> gPointer = g.getV();
        Collection<node_info> thisPointer = this.getV();
        int[] gVertex = new int[gPointer.size()];
        int[] thisVertex = new int[thisPointer.size()];
        int i = 0;
        //copying all the vertexes keys of each graph to it's own array for simplification use
        for (node_info node: gPointer){
            gVertex[i++] = node.getKey();
        }
        i = 0;
        for(node_info node:thisPointer){
            thisVertex[i++] = node.getKey();
        }
        Arrays.sort(gVertex);
        Arrays.sort(thisVertex);
        for (int k = 0; k < thisVertex.length; k++) {
            if (flag == false) break;
            //if the nodes doesn't have the same key
            if(gVertex[k] != thisVertex[k]){
                flag = false;
                break;
            }
            Collection<node_info> gNodeNeighbors = g.getV(gVertex[k]);//current g's node's neighbors
            Collection<node_info> thisNodeNeighbors = this.getV(thisVertex[k]);//current graph current node's neighbors
            int[] gNeighbors = new int[gNodeNeighbors.size()];
            int[] tNeighbors = new int[thisNodeNeighbors.size()];
            int t = 0;
            //copying all the current nodes neighbors keys it's an array for simplification use
            for (node_info n : gNodeNeighbors) {
                gNeighbors[t++] = n.getKey();
            }
            t = 0;
            for (node_info n : thisNodeNeighbors) {
                tNeighbors[t++] = n.getKey();
            }
            Arrays.sort(tNeighbors);
            Arrays.sort(gNeighbors);
            //comparing between all the neighbors of the current nodes
            for (int l = 0; l < tNeighbors.length; l++) {
                double gEdge = g.getEdge(gVertex[k], gNeighbors[l]);
                double thisEdge = this.getEdge(thisVertex[k], tNeighbors[l]);
                //if neighbor's keys or the existing edge between them to current nodes are different
                if (gNeighbors[l] != tNeighbors[l] || gEdge != thisEdge) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    /**Returns the size of nodes which this graph holds.
     * @return nodesSize
     * **/
    public int nodeSize() { return nodesSize; }

    @Override
    /**Returns the number of edges which this graph has.
     * @return edgesSize
     * **/
    public int edgeSize() { return (int)edgesSize; }

    @Override
    /**Returns the number of changes this graph has been through from it's initiation until now.
     * @return modeCount
     * **/
    public int getMC() { return modeCount; }


    /**NodeInfo based implementation of the node_info interface. A node represents a vertex in a graph with ID, Tag, Info and
     * a collection of it's connected neighbors vertexes.
     */
     public class NodeInfo implements node_info {
        /** the node ID**/
        private int key;
        /** used in complex graphs algorithms **/
        private double tag;
        /** used in complex graphs algorithms **/
        private String info;
        /** Holds neighbors which connected to this node and the weight of their edge. HashMap allows access to neighbors with O(1)**/
        private HashMap<node_info, Double> neighbors;

        /**Constructor.
         * @param key  **/
        public NodeInfo(int key) {
             this.key =key;
             info = "";
             neighbors = new HashMap<>();
         }

         /** constructor **/
         public NodeInfo() {
             this.key =i;
             i++;
             info = "";
             neighbors = new HashMap<>();
         }

        @Override
        /**Returns this node's ID.
         * @return key
         * **/
        public int getKey() {
            return key;
        }

        @Override
        /**Returns this node's Info.
         * @return info
         * **/
        public String getInfo() {
            return info;
        }

        @Override
        /**Sets this node's Info.
         * @param s
         * **/
        public void setInfo(String s) {
            info = s;
        }

        @Override
        /**Returns this node's Tag.
         * @return tag
         * **/
        public double getTag() {
            return tag;
        }

        @Override
        /**Sets this node's Tag.
         * @param t
         **/
        public void setTag(double t) {
            tag = t;
        }
    }
}