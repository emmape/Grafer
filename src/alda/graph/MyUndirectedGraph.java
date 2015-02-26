package alda.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Stack;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

	private Map<T, Map<T, Integer>> nodes = new HashMap<T, Map<T, Integer>>();
	

	
	@Override
	public int getNumberOfNodes() {
		return nodes.size();
	}

	@Override
	public int getNumberOfEdges() {
		int total = 0;
		
		for (Entry<T, Map<T, Integer>> node : nodes.entrySet()) {
			total += node.getValue().size();
		}
		
		return total;
	}

	@Override
	public boolean add(T newNode) {
		if(nodes.containsKey(newNode)){
			return false;
		}else{
			nodes.put(newNode, new HashMap<T, Integer>());
			return true;
		}
	}

	@Override
	public boolean connect(T node1, T node2, int cost) {
		if(nodes.containsKey(node1) == false || nodes.containsKey(node2) == false || cost <= 0)
			return false;
		
		Map<T, Integer> node1Edges = nodes.get(node1);
		Map<T, Integer> node2Edges = nodes.get(node2);
		
		node2Edges.put(node1, cost);
		nodes.put(node2, node2Edges);
		
		node1Edges.put(node2, cost);
		nodes.put(node1, node1Edges);
		
		return true;
	}

	@Override
	public boolean isConnected(T node1, T node2) {
		if(nodes.containsKey(node1) == false || nodes.containsKey(node2) == false)
			return false;
		
		if(nodes.get(node1).containsKey(node2))
			return true;
		
		if(nodes.get(node2).containsKey(node1))
			return true;
		
		return false;
	}

	@Override
	public int getCost(T node1, T node2) {
		int cost = -1;
		if(isConnected(node1, node2)){
			for (Entry<T, Integer> edge : nodes.get(node1).entrySet()) {
				if(edge.getKey()==node2){
					cost=edge.getValue();
				}
			}
		}
		return cost;
	}

	@Override
	public List<T> depthFirstSearch(T start, T end) {
		ArrayList<T> visited = new ArrayList<T>();
		ArrayList<T> via = new ArrayList<T>();
		
		dfs(start, end, visited, via);
		
		via.add(start);
		Collections.reverse(via);
		
		if(via.contains(end)){
			return via;
		}
		
		return new ArrayList<T>();
	}
	
	private boolean dfs(T start, T end, ArrayList<T> visited, ArrayList<T> via){
		visited.add(start);
		if(start == end){
			return true;
		}
		
		if(nodes.get(start) != null)
		for (Entry<T, Integer> edge : nodes.get(start).entrySet()) {
			
			if(!visited.contains(edge.getKey())){
				if(dfs(edge.getKey(), end, visited, via)){
					via.add(edge.getKey());
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public List<T> breadthFirstSearch(T start, T end) {
		ArrayList<T> visited=new ArrayList<T>();
		ArrayList<T> helaBredden=new ArrayList<T>();
		Queue<T> queue=new LinkedList<T>();
		queue.add(start);
		helaBredden.add(start);
		bfs( end, queue, visited, helaBredden);
	
		return visited;
	}
	private void bfs(T end, Queue<T> queue, ArrayList<T> visited, ArrayList<T> helaBredden){
		
		for(T b:helaBredden){
			for (Entry<T, Integer> edge : nodes.get(b).entrySet()) {
				if(!visited.contains(edge.getKey())){
					queue.add(edge.getKey());	
				}
			}
		}
		helaBredden.clear();
		boolean hittad=false;
		while(!queue.isEmpty()){
			T nod= queue.remove();
			visited.add(nod);
			helaBredden.add(nod);
			if(nod==end){
				hittad=true;
				rensaVisited(visited);
				break;
			}	
		}
		if(hittad==false){
			bfs(end, queue, visited, helaBredden);
		}
		

	}
	private void rensaVisited(ArrayList<T> visited){
		for (int i = visited.size()-1; i > 0; i--) {
			if (!isConnected(visited.get(i), visited.get(i-1))){
				visited.remove(i-1);
			}		
		}
	}

	private boolean contains(T nod){
		boolean exists=false;
		
		for (Entry<T, Map<T, Integer>> noden : nodes.entrySet()) {
			if(noden.getKey()==nod){
				exists=true;
			}
		}
		
		return exists;
	}
	
	@Override
	public UndirectedGraph<T> minimumSpanningTree() {
		MyUndirectedGraph<T> mst=new MyUndirectedGraph<T>(); //Tom undirected graph, ska fyllas på med spanningtree
		Map<T, Map<T, Integer>> notVisited = nodes;//Kopia av grafen
		
		while(!notVisited.isEmpty()){
			for (Entry<T, Map<T, Integer>> nod : nodes.entrySet()) {
				if (!mst.contains(nod.getKey())){
					mst.add(nod.getKey());
					Integer minst=1000;
					T smallestEdge=null;
					for (Entry<T, Integer> edge : nodes.get(nod).entrySet()) {
						if (edge.getValue()<minst){
							minst=edge.getValue();
							smallestEdge=edge.getKey();
						}
					}
					if (smallestEdge!=null){
						mst.add(smallestEdge);
						mst.connect(nod.getKey(), smallestEdge, minst);
						notVisited.remove(smallestEdge);
					}
					notVisited.remove(nod.getKey());
					
					
				}
			}
		
		}
		
		
		
		
		return mst;
	}
	
	

	

}
