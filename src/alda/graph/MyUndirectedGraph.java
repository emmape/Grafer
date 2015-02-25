package alda.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
		depthFirstSearch(start, end, visited);
		if (!visited.contains(end))
			return new ArrayList<T>();
		
		return visited;
	}
	
	private void depthFirstSearch(T start,T end, List<T> visited){
			visited.add(start);
			
			for (Entry<T, Integer> edge : nodes.get(start).entrySet()){
				if (!visited.contains(edge.getKey()) && !visited.contains(end))
					depthFirstSearch(edge.getKey(), end, visited);
			}
			
			removeWrong(visited);
	}
	
	//ta bort vägar som inte leder nåt det
	private void removeWrong(List<T> visited){
		for (int i = visited.size()-1; i > 0; i--) {
			if (!isConnected(visited.get(i), visited.get(i-1)))
				visited.remove(i-1);
		}
	}

	@Override
	public List<T> breadthFirstSearch(T start, T end) {
		// TODO This could be solved using a queue (k�).
		return null;
	}

	@Override
	public UndirectedGraph<T> minimumSpanningTree() {
		// TODO Solvable with Prim's algorithm, Kruskal's algorithm, etc.
		return null;
	}
	

}
