package ex1.tests;
import  ex1.src.WGraph_DS;
import ex1.src.node_info;
import ex1.src.weighted_graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_DSTest {
    private static final Random rnd = new Random();
    private static int numOfErrors = 0;
    private static String newNode = "newNode";
    private static String createGraph = "createGraph";
    private static String edge = "edge";
    private static String pointer_a = "pointer_a";
    private static String pointer_b = "pointer_b";
    private static String remove = "remove";
    private static String graphChanges = "graphChanges";

    public static void main(String[] args) {
        System.out.println("Running tests for Ex1_a!");
        long start = new Date().getTime();
//        try{
//            createGraph();
//            createGraph+=" - True";}
//        catch (Exception e){
//            createGraph+=" - False";
//            numOfErrors++;
//        }
        try{newNode(); newNode+= " - True";}
        catch (AssertionError e){ newNode+=" - False"; numOfErrors++;}
        try{
            edge();
            edge+=" - True";
        }catch (AssertionError e){
            edge+=" - False";
            numOfErrors++;
        }
        try{
            pointer_a();
            pointer_a+=" - True";
        }catch (AssertionError e){
            pointer_a+=" - False";
            numOfErrors++;
        }
        try{
            pointer_b();
            pointer_b+=" - True";
        }catch (AssertionError e){
            pointer_b+=" - False";
            numOfErrors++;
        }
        try{
            remove();
            remove+=" - True";

        }catch (AssertionError e){
            remove+=" - False";
            numOfErrors++;
        }
        try{
            graphChanges();
            graphChanges+=" - True";
        }catch (AssertionError e){
            graphChanges+=" - False";
            numOfErrors++;
        }
        long end = new Date().getTime();
        double dt = (end-start)/1000.0;
        System.out.println("Runtime: "+dt);
       // System.out.println(createGraph);
        System.out.println(newNode);
        System.out.println(edge);
        System.out.println(pointer_a);
        System.out.println(pointer_b);
        System.out.println(remove);
        System.out.println(graphChanges);
        System.out.println("Total errors: "+numOfErrors);
    }
    /**
     * graphTest constructor
     **/
    public static WGraph_DS graphCreator(int Vsize, int Esize, int seed) {
        WGraph_DS g = new WGraph_DS();
        //adding random nodes to the graph
        int t = 0;
        while (g.nodeSize() < Vsize) {
            g.addNode(t);
            t++;
        }

        Collection<node_info> pointer = g.getV();
        int[] nodesKeys = new int[pointer.size()];
        int i = 0;
        for (node_info node : pointer) {
            nodesKeys[i] = node.getKey();
            i++;
        }
        Arrays.sort(nodesKeys);
        while (g.edgeSize() < Esize) {
            int rnd1 = rnd.nextInt(Vsize);//picking random vertex to connect
            int rnd2 = rnd.nextInt(Vsize);//picking random vertex to connect
            double edge = rnd.nextDouble()*seed;//random edge size
            int a = nodesKeys[rnd1];
            int b = nodesKeys[rnd2];
            if(g.hasEdge(a,b)){
                g.connect(a,b,edge);
            }
            else{
                g.connect(a,b,edge);
            }
        }
        return g;
    }

    @Test
    public static void createGraph() {
        WGraph_DS g = graphCreator(1000000, 10000000, 10);
        assertEquals(1000000, g.nodeSize());
        assertEquals(10000000, g.edgeSize());
    }

    @Test
    //testing addNode and getNode
    public static void newNode() {
        WGraph_DS g = new WGraph_DS();
        g.addNode(2);
        node_info nodeEq = g.getNode(2);
        node_info nodeUeq = g.new NodeInfo(2);
        //check the reference of th 2 nodes
        assertEquals(nodeEq, g.getNode(2));
        assertNotEquals(nodeUeq, g.getNode(2));

        //checks there aren't nodes with the same ID
        int numOfNodes = g.nodeSize();
        g.addNode(2);
        assertEquals(numOfNodes, g.nodeSize());
        //node isn't in the graph - return null
        assertNull(g.getNode(10));

    }

    @Test
    //testing connect, hasEdge and getEdge
    public static void edge() {
        weighted_graph g = graphCreator(20, 0, 10);
        g.connect(0,1,-2);
        //checks that graph can't connect negative edge
        assertEquals(0, g.edgeSize());
        //checks if there's an edge
        assertFalse(g.hasEdge(0, 2));
        assertFalse(g.hasEdge(0, 1));
        //checks if there's an edge
        g.removeEdge(1,3);
        assertFalse(g.hasEdge(1, 3));
        //checks that no edge equals -1;
        assertEquals(-1, g.getEdge(1, 3));
        //checks edge weight
        g.connect(1,2,10);
        assertEquals(10, g.getEdge(1, 2));
        //checks updating edge weight
        g.connect(1, 2, 5);
        assertEquals(5, g.getEdge(1, 2));
    }

    @Test
    //test getV
    public static void pointer_a() {
        weighted_graph g = graphCreator(1000000, 0 , 0);
        boolean flag = true;
        Collection<node_info> pointer = g.getV();
        //checking each node in pointer is a shallow copy of a node in the graph
        for (node_info node : pointer) {
            int id = node.getKey();//this node ID
            //if they don't have the same reference
            if (!node.equals(g.getNode(id))) {
                flag = false;
                break;
            }
        }
        assertTrue(flag);
    }

    @Test
    //test getV(int node_id)
    public static void pointer_b() {
        weighted_graph g = graphCreator(1000000,0,1);
        node_info[] neighbors = new node_info[500000];//array that contains all of node 0 neighbors
        //adding 500000 different neighbors to node 0
        for (int i = 1; i <=500000; i++) {
            node_info current = g.getNode(i);
            g.connect(0, i, i);
            neighbors[i-1] = current;
        }
        Collection<node_info> pointer = g.getV(0);
        boolean flag = true;
        for (int i = 0; i < neighbors.length; i++) {
            if (!pointer.contains(neighbors[i])) {
                flag = false;
                System.out.println(i);
                break;
            }
        }
        assertTrue(flag);
        assertEquals(pointer.size(), neighbors.length);
    }

    @Test
    //test remove
    public static void remove(){
        WGraph_DS g = graphCreator(100, 50, 50);
        Collection<node_info> pointer = g.getV();
        node_info[] nodes =new node_info[pointer.size()];
        int i = 0;
        for (node_info node: pointer) {
            nodes[i] = node;
            i++;
        }
        for (int j = 0; j <nodes.length; j++) {
            int key = nodes[j].getKey();
            g.removeNode(key);
        }
        assertEquals(0,g.nodeSize());
        assertEquals(0,g.edgeSize());
    }

    @Test
    public static void graphChanges(){
        weighted_graph g = new WGraph_DS();
        int changesCount = 0;
        for (int i = 0; i < 10 ; i++) {
            g.addNode(i);
            changesCount++;
        }
        for (int i = 1; i < 5 ; i++) {
            g.connect(0,i,1);
            changesCount++;
        }
        for (int i = 5; i < 10 ; i++) {
            g.removeNode(i);
            changesCount++;
        }
        g.removeNode(3);
        changesCount+=2; //for removing node 3 and the it's edge to node 0
        assertEquals(changesCount, g.getMC());
    }





}