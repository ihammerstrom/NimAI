
import java.util.*;


//node class representing the different numbers on the graph
public class Node{
	int[] values;
	LinkedList<Node> children = new LinkedList<Node>();
	Node parent;
	boolean max = false;
	boolean goodPath = false;
	//boolean goalState = false;
	boolean discovered = false;

	public Node(int[] values, Node parent){
		this.values=values;
		this.parent = parent;
	}

	public void addChild(Node child){

		children.add(child);
		System.out.println("Added node: "+child+" as child to: "+ this);

	}

	public void setDiscoveredTrue(){//boolean discovered){
		discovered=true;
	}
	public boolean getDiscovered(){
		return discovered;
	}

	public int[] getValues(){
		return values;
	}
	public boolean getMax(){
		return max;
	}
	public void maxTrue(){
		max=true;
	}

	public boolean getGoodPath(){
		return goodPath;
	}
	public void setGoodPath(boolean good){
		this.goodPath=good;
	}

	public LinkedList<Node> getChildren(){
		return children;
	}

	@Override
	public String toString(){
		return Arrays.toString(values);
	}

	@Override
	public boolean equals(Object other){
		Node otherNode = (Node)other;
		//System.out.println("Comparing "+this+" to "+other);
		if(Arrays.equals(otherNode.getValues(), this.getValues())){
			//System.out.println("equal");
			return true;
		}else{
			//System.out.println("false");
			return false;
		}
	}


	
}