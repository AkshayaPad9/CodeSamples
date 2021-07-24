import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

class Node {
    int faceValue;
    int suit;
    int dup;
    boolean pseudo;

    Node(int faceValue, int suit, int dup, boolean pseudo) {
        this.faceValue= faceValue;
        this.suit= suit;
        this.dup= dup;
        this.pseudo= pseudo;
    }

    int getFV() {
        return faceValue;
    }

    int getSuit() {
        return suit;
    }

    void setDup(int newDup) {
        dup= newDup;
    }

    int getDup() {
        return dup;
    }

    boolean getPseudo() {
        return pseudo;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || obj.getClass() != this.getClass())
            return false;
        Node node= (Node) obj;
        return node.faceValue == faceValue && node.suit == suit && node.getPseudo() == pseudo;
    }

    @Override
    public String toString() {
        return "FV: " + getFV() + ", Suit: " + getSuit() + " Pseudo: " +
            getPseudo();
    }

    @Override
    public int hashCode() {
        int i= 0;
        if (pseudo == false) {
            i= 1;
        }
        return i + faceValue * 10 + suit * 10000;
    }
}

class Main {
    public static void main(String[] args) {
        Scanner in= new Scanner(System.in);
        int num_cards= in.nextInt();
        int face_value= in.nextInt();
        int num_suits= in.nextInt();
        Node[][] nodes= new Node[face_value + 1][num_suits + 1];
        int minSuit= num_suits;
        String emptyLine= in.nextLine();
        for (int i= 0; i < face_value + 1; i++ ) {
            for (int j= 0; j < num_suits + 1; j++ ) {
                nodes[i][j]= new Node(-1, -1, 0, false);
            }
        }
        for (int i= 0; i < num_cards; i++ ) {
            int fv= in.nextInt();
            int suit= in.nextInt();
            if (nodes[fv][suit].getFV() == fv) {
                Node m= nodes[fv][suit];
                m.setDup(m.getDup() + 1);
                nodes[fv][suit]= m;
            } else {
                // System.out.println("fv: " + fv + " suit: " + suit);
                Node n= new Node(fv, suit, 1, false);
                nodes[fv][suit]= n;
            }
        }
        in.close();
        int maxFlow= 0;
        Node s= new Node(-3, -1, 0, false);
        Node t= new Node(-2, -2, 0, false);
        HashMap<Node, HashMap<Node, Integer>> graph= new HashMap<>();
        for (int i= 2; i <= face_value; i++ ) {
            for (int j= 0; j < num_suits; j++ ) {
                if (nodes[i][j].getFV() != -1) {
                    if (nodes[i][j].getFV() != 2) {
                        Node pseudon= new Node(i, j, 0, true);
                        HashMap<Node, Integer> hm= new HashMap<>();
                        hm.put(nodes[i][j], nodes[i][j].getDup());
                        graph.put(pseudon, hm);
                    }
                }
            }
        }
        HashMap<Node, Integer> shm= new HashMap<>();
        for (int j= 0; j < num_suits; j++ ) {
            if (nodes[2][j].getFV() != -1) {
                shm.put(nodes[2][j], nodes[2][j].getDup());
            }
        }
        graph.put(s, shm);
        int inLoop= 0;
        for (int i= 2; i <= face_value; i++ ) {
            for (int j= 0; j < num_suits; j++ ) {
                if (nodes[i][j].getFV() != -1) {
                    inLoop++ ;
                    HashMap<Node, Integer> hm= new HashMap<>();
                    if (!graph.containsKey(nodes[i][j])) {
                        hm= new HashMap<>();
                    } else {
                        hm= graph.get(nodes[i][j]);
                    }
                    boolean exists= false;
                    if (i + 1 <= face_value && nodes[i + 1][j].getFV() != -1) {
                        // System.out.println("next fv i: " + i + " j: " + j);
                        exists= true;
                        // System.out.println(nodes[i][j]);
                        Node pseudon= new Node(i + 1, j, 0, true);
                        hm.put(pseudon, Integer.MAX_VALUE);
                    }
                    if (i + 2 <= face_value && j + 1 < num_suits &&
                        nodes[i + 2][j + 1].getFV() != -1) {
                        // System.out.println("plus 2 i: " + i + " j: " + j);
                        exists= true;
                        Node pseudon= new Node(i + 2, j + 1, 0, true);
                        hm.put(pseudon, Integer.MAX_VALUE);
                    } else if (i + 2 <= face_value && j + 1 >= num_suits &&
                        nodes[i + 2][0].getFV() != -1) {
                            // System.out.println("plus 2 out i: " + i + " j: " + j);
                            exists= true;
                            Node pseudon= new Node(i + 2, 0, 0, true);
                            hm.put(pseudon, Integer.MAX_VALUE);
                        } else if (!exists && i == face_value) {
                            // System.out.println("out i: " + i + " j: " + j);
                            hm.put(t, Integer.MAX_VALUE);
                        }
                    HashMap<Node, Integer> joker= new HashMap<>();
                    if (i != 2) {
                        for (int jo= 0; jo < num_suits; jo++ ) {
                            if (nodes[0][jo].getFV() != -1) {
                                if (graph.containsKey(nodes[0][jo])) {
                                    joker= graph.get(nodes[0][jo]);
                                }

                                Node pseudon= new Node(i, j, 0, true);
                                joker.put(pseudon, Integer.MAX_VALUE);
                            }
                            graph.put(nodes[0][jo], joker);
                        }
                    }
                    if (exists) {
                        for (int jo= 0; jo < num_suits; jo++ ) {
                            if (nodes[0][jo].getFV() != -1) {
                                // System.out.println("Here Joker");
                                Node pseudon= new Node(0, jo, 0, true);
                                hm.put(pseudon, Integer.MAX_VALUE);
                                // System.out.println("joker i: " + i + " j: " + j);
                            }
                        }
                    }
                    graph.put(nodes[i][j], hm);
                }
            }
        }
        for (int jo= 0; jo <= num_suits; jo++ ) {
            if (nodes[0][jo].getFV() != -1) {
                // System.out.println("Joker");
                Node pseudon= new Node(0, jo, 0, true);
                HashMap<Node, Integer> hm= new HashMap();
                hm.put(nodes[0][jo], 1);
                graph.put(pseudon, hm);
            }
        }
        // System.out.println("in loop: " + inLoop);
        FF f= new FF();
        // System.out.println(graph.get(s));
        System.out.println(f.FordFulkerson(graph, s, t));

    }
}

class FF {
    boolean bfs(HashMap<Node, HashMap<Node, Integer>> graph_r, HashMap<Node, Node> path, Node s,
        Node t) {
        HashMap<Node, Boolean> visited= new HashMap<>();
        for (Map.Entry mapElement : graph_r.entrySet()) {
            Node tn= (Node) mapElement.getKey();
            // System.out.println(tn);
            visited.put(tn, false);
        }
        LinkedList<Node> enqueue= new LinkedList<>();
        enqueue.add(s);
        // make all nodes unvisited
        visited.put(s, true);
        Node dummy= new Node(-3, -3, 0, false);
        path.put(s, dummy);
        // pick up entries from enqueue
        while (enqueue.size() != 0) {
            Node u= enqueue.poll();
            for (Map.Entry mapElement : graph_r.entrySet()) {
                Node tn= (Node) mapElement.getKey();
                // entries that are not yet visited, have an edge from u to tn, and capacity left
                // greater than 0
                if (visited.get(tn) == false && graph_r.get(u).containsKey(tn) &&
                    graph_r.get(u).get(tn) > 0) {
                    if (tn.equals(t)) {
                        path.put(tn, u);
                        // System.out.println();
                        // System.out.println();
                        // System.out.println("Here True");
                        return true;
                    }
                    visited.put(tn, true);
                    enqueue.add(tn);
                    path.put(tn, u);
                }
            }
        }
        // System.out.println("Here False");
        return false;
    }

    int FordFulkerson(HashMap<Node, HashMap<Node, Integer>> graph, Node s, Node t) {
        int maxFlow= 0;
        // add entries to residual graph
        HashMap<Node, HashMap<Node, Integer>> graph_r= new HashMap<>();
        for (Map.Entry mapElement : graph.entrySet()) {
            Node tn= (Node) mapElement.getKey();
            HashMap<Node, Integer> hm= graph.get(tn);
            // System.out.println("node: " + tn + " map: " + hm);
            HashMap<Node, Integer> hm2= new HashMap<>();
            if (graph_r.containsKey(tn)) {
                hm2= graph_r.get(tn);
            }
            for (Map.Entry mapElement2 : hm.entrySet()) {
                Node tn2= (Node) mapElement2.getKey();
                hm2.put(tn2, hm.get(tn2));
            }
            // System.out.println(tn + " map:" + hm2);
            graph_r.put(tn, hm2);
        }
        // System.out.println(graph_r);
        // System.out.println(graph_r.size());
        for (Map.Entry mapElement : graph.entrySet()) {
            Node tn= (Node) mapElement.getKey();
            HashMap<Node, Integer> hm= graph.get(tn);
            // System.out.println("node: " + tn + " map: " + hm);
            for (Map.Entry mapElement2 : hm.entrySet()) {
                Node tn2= (Node) mapElement2.getKey();
                HashMap<Node, Integer> hm3= new HashMap<>();
                // System.out.println("Node: " + tn2);
                if (graph_r.containsKey(tn2)) {
                    // System.out.println(tn2 + " map:" + graph_r.get(tn2));
                    hm3= graph_r.get(tn2);
                }
                hm3.put(tn, 0);
                graph_r.put(tn2, hm3);
            }
        }
        Node t_p= new Node(-2, -2, 1, false);
        // System.out.println(graph_r.get(t_p));

        HashMap<Node, Node> path= new HashMap<>();
        while (bfs(graph_r, path, s, t)) {
            // System.out.println("Here");
            // System.out.println("Here");
            int indMaxFlow= Integer.MAX_VALUE;
            Node pass= t;
            // go through entries, find bottleneck flow
            // System.out.println(pass);
            while (!pass.equals(s)) {
                Node u= path.get(pass);
                // System.out.println("u: " + u + " pass: " + pass);
                int flow= graph_r.get(u).get(pass);
                // System.out.println("flow: " + flow);
                if (flow < indMaxFlow) {
                    indMaxFlow= flow;
                }
                pass= u;
            }
            // we always have forward edges
            maxFlow+= indMaxFlow;
            // System.out.println("Here");
            Node new_tn= t;
            while (!new_tn.equals(s)) {
                // find the new capacity of each edge
                Node u= path.get(new_tn);
                int cap= graph_r.get(u).get(new_tn);
                int cap_r= graph_r.get(new_tn).get(u);
                HashMap<Node, Integer> hm= graph_r.get(u);
                hm.put(new_tn, cap - indMaxFlow);
                graph_r.put(u, hm);
                HashMap<Node, Integer> hm2= graph_r.get(new_tn);
                if (new_tn.equals(new Node(2, 0, 0, false))) {
                    // System.out.println("cap: " + cap);
                    // System.out.println("cap_r: " + cap_r);
                }
                hm2.put(u, cap_r + indMaxFlow);
                graph_r.put(new_tn, hm2);
                new_tn= u;
            }
            // System.out.println();
        }
        return maxFlow;

    }
}
