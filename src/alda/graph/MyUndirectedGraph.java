/**
 * 
 * ALDA - Grafer
 * 
 * @author Elise Edette (tero0337)
 * @author Aframyeos Rohoum (afro0793)
 * @author Emma Persson (empe5691)
 */

package alda.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

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
				if(edge.getKey()==node2 || edge.getKey().equals(node2)){
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
		//Recursively makes a depth first search
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
		ArrayList<T> neighbours=new ArrayList<T>();
		Queue<T> queue=new LinkedList<T>();
		queue.add(start);
		neighbours.add(start);
		bfs( end, queue, visited, neighbours);
		return visited;
	}
	
	private void bfs(T end, Queue<T> queue, ArrayList<T> visited, ArrayList<T> neighbours){
		//Recursively makes a breadth first search
		for(T node : neighbours){
			for (Entry<T, Integer> edge : nodes.get(node).entrySet()) {
				if(!visited.contains(edge.getKey())){
					queue.add(edge.getKey());	
				}
			}
		}
		neighbours.clear();
		boolean exists=false;
		while(!queue.isEmpty()){
			T nod= queue.remove();
			visited.add(nod);
			neighbours.add(nod);
			if(nod==end){
				exists=true;
				purge(visited);
				break;
			}	
		}
		if(exists==false){
			bfs(end, queue, visited, neighbours);
		}
	}
	
	private void purge(ArrayList<T> visited){
	//Removes nodes that do not lead to the endnode
		for (int i = visited.size()-1; i > 0; i--) {
			if (!isConnected(visited.get(i), visited.get(i-1))){
				visited.remove(i-1);
			}
		}
	//Removes nodes that takes a detour to the endnode
		for (int j = visited.size()-1; j > 1; j--){
			if (isConnected(visited.get(j), visited.get(j-2))){
				visited.remove(j-1);
			}
		}
	}

	@Override
	public UndirectedGraph<T> minimumSpanningTree() {
		MyUndirectedGraph<T> mst=new MyUndirectedGraph<T>(); 
		ArrayList<T> visited=new ArrayList<T>();
		Map.Entry<T, Map<T, Integer>> entry = nodes.entrySet().iterator().next();
		mst.add(entry.getKey());
		visited.add(entry.getKey());
		mst(mst, visited);

		return mst;
	}

	private void mst(UndirectedGraph<T> mst, ArrayList<T> visited){
		//Recursively build a minimum spanning tree
		if (mst.getNumberOfNodes() < nodes.size()){ 
			int minimum=Integer.MAX_VALUE;
			T destination=null;
			T node=null;

			for(T visitedNode: visited){
				for (Entry<T, Integer> edge : nodes.get(visitedNode).entrySet()) {
					if (edge.getValue().intValue() < minimum && !visited.contains(edge.getKey())){
						minimum=edge.getValue().intValue();
						destination=edge.getKey();
						node=visitedNode;
					}
				}
			}
			if(destination!=null){
				mst.add(destination);
				mst.connect(node, destination, minimum);	
			}
			visited.add(destination);

			mst(mst, visited);
		}else{
			return;
		}
	}
}


