package sample;

import java.util.*;

class Triple<f, s, t> implements Comparable<Triple<f, s, t>>{
    public final f first;
    public final s second;
    public final t third;

    public Triple(f first, s second, t third){
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public f getFirst(){
        return first;
    }

    public s getSecond(){
        return second;
    }

    public t getThird(){
        return third;
    }

    @Override
    public int compareTo(Triple o){
        if((Integer) this.getFirst() > (Integer) o.getFirst()){
            return 1;
        }
        return (this.getFirst()).equals(o.getFirst()) ? 0 : -1;
    }

    @Override
    public String toString() {
        return "Triple{" +
                "first=" + first +
                ", second=" + second +
                ", third=" + third +
                '}';
    }
}

class Pair<f, s> implements Comparable<Pair<f, s>>{
    public final f first;
    public final s second;

    public Pair(f first, s second) {
        this.first = first;
        this.second = second;
    }

    public f getFirst() {
        return first;
    }

    public s getSecond() {
        return second;
    }

    @Override
    public int compareTo(Pair o) {
        if((Integer) this.getFirst() > (Integer) o.getFirst()){
            return 1;
        }
        return (this.getFirst()).equals(o.getFirst()) ? 0 : -1;
    }
}

public class Algorithms {
    final static int N = 20, infinity = 100000000;
    static String startNode = null, endNode = null;

    public static Pair<int[], int[]> UniformCost(ArrayList<ArrayList<ArrayList<Integer>>> adj, HashMap<String, Integer> id){
        PriorityQueue<Pair<Integer, Integer>> priorityQueue = new PriorityQueue<>();
        int [] cost = new int [N + 1];
        int [] parent = new int [N + 1];
        for(int i = 1; i <= N; i++){
            cost[i] = infinity;
            parent[i] = -1;
        }
        int s = id.get(startNode);
        cost[s] = 0;
        Pair<Integer, Integer> p = new Pair<>(0, s);
        priorityQueue.add(p);
        while(!priorityQueue.isEmpty()){
            Pair<Integer, Integer> top = priorityQueue.poll();
            int crnt_cost = top.getFirst(), node = top.getSecond();
            for(int i = 0; i < adj.get(node).size(); i++){
                int child = adj.get(node).get(i).get(0);
                int new_cost = crnt_cost + adj.get(node).get(i).get(1);
                if(new_cost < cost[child]){
                    cost[child] = new_cost;
                    Pair<Integer, Integer> p2 = new Pair<>(new_cost, child);
                    priorityQueue.add(p2);
                    parent[child] = node;
                }
            }
        }
        return new Pair<>(cost, parent);
    }

    public static Pair<int[], int[]> breadthFirstSearch(ArrayList<ArrayList<ArrayList<Integer>>> adj, HashMap<String, Integer> id) {
        Queue<Pair<Integer, Integer>> queue = new LinkedList<> ();
        int s = id.get(startNode);
        Pair<Integer, Integer> p = new Pair<>(0, s);
        queue.add(p);
        int [] cost = new int[N+1];
        int [] parent = new int[N+1];
        boolean [] visited = new boolean[N + 1];
        for(int i=0;i<=N;i++) {
            cost[i]=infinity;
            parent[i]=-1;
            visited[i] = false;
        }
        cost[s]=0;
        visited[s] = true;
        while(!queue.isEmpty()) {
            Pair<Integer, Integer> top = queue.poll();
            int cost2=top.getFirst();
            int current=top.getSecond();
            for(int i = 0; i < adj.get(current).size(); i++){
                int child=adj.get(current).get(i).get(0);
                if(!visited[child]) {
                    visited[child] = true;
                    cost[child]=cost2 + adj.get(current).get(i).get(1);
                    queue.add(new Pair<>(cost[child], child));
                    parent[child]=current;
                }
            }
        }
        return new Pair<>(cost, parent);
    }

    public static Triple<int[], int[], int[]> AStarH1(ArrayList<ArrayList<ArrayList<Integer>>> adj, HashMap<String, Integer> id){
        PriorityQueue<Triple<Integer, Integer, Integer>> priorityQueue = new PriorityQueue<>();
        int [] cost=new int[N+1];
        int [] parent = new int [N + 1];
        int [] H2 = new int[N + 1];
        for(int i=0;i<(N+1);i++) {
            cost[i]=infinity;
            parent[i]=-1;
            H2[i] = infinity;
        }
        int sid = id.get(startNode);
        cost[sid]=0;
        H2[sid] = 0;
        for(int i=0;i < adj.get(sid).size();i++) {
            int node = adj.get(sid).get(i).get(0);
            int temp_cost = adj.get(sid).get(i).get(3) + adj.get(sid).get(i).get(2) - (node == id.get(endNode) ? adj.get(sid).get(i).get(3) : 0);
            priorityQueue.add(new Triple<>(temp_cost, node, adj.get(sid).get(i).get(3) -
                    (node == id.get(endNode) ? adj.get(sid).get(i).get(3) : 0)));
            cost[node] = temp_cost;
            parent[node] = sid;
            H2[node] = temp_cost;
        }
        while(!priorityQueue.isEmpty()) {
            Triple<Integer, Integer, Integer> top = priorityQueue.poll();
            assert top != null;
            int currentCost=top.getFirst()-top.getThird();
            int current=top.getSecond();
            if(cost[id.get(endNode)] != infinity && current == id.get(endNode)){
                break;
            }
            for(int i = 0; i < adj.get(current).size(); i++){
                int child = adj.get(current).get(i).get(0);
                int new_cost = currentCost + adj.get(current).get(i).get(2) + adj.get(current).get(i).get(3) - (child ==
                        id.get(endNode) ? adj.get(current).get(i).get(3) : 0);
                if(new_cost < cost[child]){
                    cost[child] = new_cost;
                    priorityQueue.add(new Triple<>(cost[child], child, adj.get(current).get(i).get(3) - (child ==
                            id.get(endNode) ? adj.get(current).get(i).get(3) : 0)));
                    parent[child] = current;
                    H2[child] = new_cost;
                }
            }
        }
        return new Triple<>(cost, parent, H2);
    }

    public static Pair<int[], int[]> AStarH2(ArrayList<ArrayList<ArrayList<Integer>>> adj, HashMap<String, Integer> id, int[] h2){
        PriorityQueue<Triple<Integer, Integer, Integer>> priorityQueue = new PriorityQueue<>();
        int []cost=new int[N+1];
        int [] parent = new int [N + 1];
        for(int i=0;i<(N+1);i++) {
            cost[i]=infinity;
            parent[i]=-1;
        }
        int sid = id.get(startNode);
        cost[sid]=0;
        for(int i=0;i<adj.get(sid).size();i++) {
            int node = adj.get(sid).get(i).get(0);
            int temp_cost = h2[adj.get(sid).get(i).get(0)] + adj.get(sid).get(i).get(1) - (node == id.get(endNode) ? h2[adj.get(sid).get(i).get(0)] : 0);
            priorityQueue.add(new Triple<>(temp_cost, node, h2[adj.get(sid).get(i).get(0)] - (node == id.get(endNode) ? h2[adj.get(sid).get(i).get(0)] : 0)));
            cost[adj.get(sid).get(i).get(0)] = temp_cost;
            parent[adj.get(sid).get(i).get(0)] = sid;

        }
        while(!priorityQueue.isEmpty()) {
            Triple<Integer, Integer, Integer> top = priorityQueue.poll();
            int currentCost=top.getFirst()-top.getThird();
            int current=top.getSecond();
            for(int i = 0; i < adj.get(current).size(); i++){
                int child = adj.get(current).get(i).get(0);
                int new_cost = currentCost + adj.get(current).get(i).get(1) + h2[adj.get(current).get(i).get(0)] - (child ==
                        id.get(endNode) ? h2[adj.get(current).get(i).get(0)] : 0);
                if(new_cost < cost[child]){
                    cost[child] = new_cost;
                    priorityQueue.add(new Triple<>(cost[child],child,h2[adj.get(current).get(i).get(0)] - (child ==
                            id.get(endNode) ? h2[adj.get(current).get(i).get(0)] : 0)));
                    parent[child] = current;
                }
            }
        }
        return new Pair<>(cost, parent);
    }
}