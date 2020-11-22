package ex1.tests;
import ex1.src.*;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    private static Random rnd = new Random();
    private static String init = "init";
    private static String copy = "copy";
    private static String isConnected_a = "isConnected_a";
    private static String isConnected_b = "isConnected_b";
    private static String shortestPathDis = "shortestPathDis";
    private static String shortestPath = "shortestPath";
    private static String saveNloadl = "saveNloadl";
    private static int numOfErrors = 0;

    public static void main(String[] args) {
        System.out.println("Running tests for Ex1_b!");
        long start = new Date().getTime();
        try{ init(); init+=" - True";  }
        catch (AssertionError e){ init+=" - False"; numOfErrors++; }
        try { copy(); copy+=" - True"; }
        catch(AssertionError e){ copy+=" - False"; numOfErrors++; }
        try{isConnected_a(); isConnected_a+=" - True";}
        catch (AssertionError e){ isConnected_a+=" - False"; numOfErrors++;}
        try { isConnected_b(); isConnected_b+=" - True";}
        catch (AssertionError e){ isConnected_b+=" - False"; numOfErrors++;}
        try { shortestPathDis(); shortestPathDis+=" - True";}
        catch (AssertionError e){ shortestPathDis+=" - False"; numOfErrors++;}
        try { shortestPath(); shortestPath+=" - True";}
        catch (AssertionError e){ shortestPath+=" - False"; numOfErrors++;}
        try { saveNload(); saveNloadl+=" - True";}
        catch (AssertionError e){ saveNloadl+=" - False"; numOfErrors++;}
        long end = new Date().getTime();
        double dt = (end-start)/1000.0;
        System.out.println("Runtime: "+dt);
        System.out.println(init);
        System.out.println(copy);
        System.out.println(isConnected_a);
        System.out.println(isConnected_b);
        System.out.println(shortestPathDis);
        System.out.println(shortestPath);
        System.out.println(saveNloadl);
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


    public static void init(){
        weighted_graph g = graphCreator(1000, 100, 5);
        weighted_graph_algorithms k = new WGraph_Algo();
        k.init(g);
        assertSame(k.getGraph(),g);//checks the reference
    }


    public static void copy(){
        weighted_graph g = graphCreator(1000, 100, 5);
        weighted_graph_algorithms k = new WGraph_Algo();
        k.init(g);
        weighted_graph gCopy = new WGraph_DS();
        assertNotEquals(gCopy, g);
        gCopy = k.copy();
        assertNotSame(gCopy,g);//different references
        assertEquals(gCopy, g);
    }


    /**single node graph**/
    public static void isConnected_a(){
        weighted_graph g = graphCreator(1, 0,0);
        weighted_graph_algorithms k = new WGraph_Algo();
        k.init(g);
        assertTrue(k.isConnected());
    }


    /**two nodes graph**/
    public static void isConnected_b(){
        weighted_graph g = graphCreator(2,0,0);
        weighted_graph_algorithms k = new WGraph_Algo();
        k.init(g);
        assertFalse(k.isConnected());//with no edge between the nodes
        g.connect(0,1,1);
        assertTrue(k.isConnected());
        g.addNode(3);
        assertFalse(k.isConnected());
    }

    @Test
    public static void shortestPathDis(){
            weighted_graph g = new WGraph_DS();
            weighted_graph_algorithms k = new WGraph_Algo();
            k.init(g);
            g.addNode(1);
            g.addNode(2);
            g.addNode(3);
            g.addNode(5);
            g.addNode(9);
            g.connect(1, 2, 4);
            g.connect(1, 3, 2);
            g.connect(2, 3, 1);
            g.connect(2, 9, 6);
            g.connect(2, 5, 6);
            g.connect(5, 9, 2);
            assertEquals(9, k.shortestPathDist(1, 9));
            g.removeNode(5);
            g.removeNode(2);
            assertEquals(-1, k.shortestPathDist(1, 9));//no path yet
    }



    public static  void shortestPath(){
            weighted_graph g = graphCreator(10, 0, 0);
            weighted_graph_algorithms k = new WGraph_Algo();
            k.init(g);
            g.connect(1, 2, 4);
            g.connect(1, 3, 2);
            g.connect(2, 3, 1);
            g.connect(2, 9, 6);
            g.connect(2, 5, 6);
            g.connect(5, 9, 2);
            Collection<node_info> shortPath = k.shortestPath(1, 9);
            int[] shortPathKeys = new int[shortPath.size()];
            int i = 0;
            for (node_info node : shortPath) {
                shortPathKeys[i++] = node.getKey();
            }
            Arrays.sort(shortPathKeys);
            int[] neighbors = {1, 2, 3, 9};//shortest path
            assertTrue(Arrays.equals(neighbors, shortPathKeys));
    }


    public static void saveNload(){
        weighted_graph g = graphCreator(1000000,10000,50);
        weighted_graph_algorithms k = new WGraph_Algo();
        k.init(g);
        k.save("Test");
        k.load("Test");
        assertEquals(k.getGraph(), g);
    }



}