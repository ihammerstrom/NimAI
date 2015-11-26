import java.util.*;
import java.io.*;

/*
Ian Hammerstrom
CS402 Assignment 1 - NimAI

An implementation of the variant of the game Nim as described in lecture and the text.
Works on any value up to 22 on CF414 computers. (32GB RAM)

*/

public class NimAI{

	//Sets up the game/graph
	public static void main (String[] args){
		Scanner reader = new Scanner(System.in);
		int num;
		int[] arr = new int[1];
		System.out.println("The user will begin a game by making a call to the function Nim which takes a positive integer as input indicating the number of tokens in the game."+
		                   "   The program makes the next move by dividing the pile of tokens into two unequal piles.  The user responds by dividing one of the piles into two uneven piles, "+
		                   "until one of the players no longer has a valid move available.\nYou are the user, go forth.");
		arr[0] = reader.nextInt();
		Node root = new Node(arr, null);
		
		System.out.println("\nCreating graph of all possible game states: ");
		System.out.println("Root node: "+root);
		makeTree(root);

		root.maxTrue();
		for(Node child : root.getChildren()){
			setMINMAX(child);
		}
		System.out.println("\nFinding Guaranteed Path: ");
		makeGoodPath(root);
		
		System.out.println("\n\nPlaying the game: ");
		playGame(root);

	}


	//Continues game loop for as long as there's no winner.
	public static void playGame(Node root){
		Node current = root;


		System.out.println(current+" was chosen as the root");


		while(current !=null && !current.getChildren().isEmpty()){


			current = breadthFirstComputerTurn(current);
			if(current==null){ //User won, computer forced to return null
				//System.out.println("User won! How'd you do that?...");
			}else{
				if(current.getChildren().isEmpty()){//User has no options
					System.out.println("You have no options: Computer won!");
				}else{
					current = humanTurn(current);
				}
			}

			
		}
		
	}

	//Human's turn, passes in current node and return's human's choice.
	public static Node humanTurn(Node node){
		Scanner reader = new Scanner(System.in);
		char option = 'a', choice;
		int count=0, chosen=0;

		System.out.println("User's turn: ");
		
		for (Node child: node.getChildren()){
			System.out.println("Enter "+"'"+option+"' to choose: "+child);
			option++;
		}

		choice = reader.next().charAt(0);
		while(choice>=option || choice < 97){
			System.out.println("Enter a character between 'a' and '"+(char)(option-1)+"'.");
			choice = reader.next().charAt(0);
		}
		chosen= choice-'a';

		for (Node child: node.getChildren()){
			if(count == chosen){
				System.out.println("User chose: "+child);
				return child;
			}
			count++;
		}
		return null;
	}


	//Computer's decision making turn by breadth-first search of the graph.
	//Passes in current node and return's computer's choice.
	public static Node breadthFirstComputerTurn(Node node){
		LinkedList<Node> options = node.getChildren();
		Queue<Node> nodes= new LinkedList<Node>();
		Node temp;
		System.out.println("Computer's turn: ");
		System.out.println("Computer choosing between: "+ node.getChildren());
		for(Node child: node.getChildren()){
			nodes.add(child);
		}


		while(!nodes.isEmpty() && !nodes.peek().getDiscovered()){
			temp = nodes.poll();
			temp.setDiscoveredTrue();
			if(temp.getGoodPath()){
				return parentReturn(options, temp);
			}
			for(Node child: temp.getChildren()){
				nodes.add(child);
			}
		}
		System.out.println("Computer has realized it cannot win, and gives up out of shame :'(");
		return null;
	}

	//Helper function to computer to ensure it returns a valid choice.
	public static Node parentReturn(LinkedList<Node> options, Node node){
		if(!options.contains(node)){
			return parentReturn(options, node.parent);
		}else{
			System.out.println("Computer chose: "+node);
			if(node.getGoodPath()&&!node.getChildren().isEmpty()){
				System.out.println("Computer has decided your fate.");
			}
			return node;
		}
	}

	
	//makes tree from root node.
	public static void makeTree(Node node){
		int n=1, temp=0;
		for(int val : node.getValues()){
			temp = val - n;
			while(temp > val/2){
				//new array of old numbers
				//remove old value
				//replace with old value - n and n
				ArrayList<Integer> valuesList = new ArrayList<Integer>(node.getValues().length+1);
				for(int z : node.getValues()){
					valuesList.add(z);
				}

				valuesList.remove(Integer.valueOf(val));
				valuesList.add(temp);
				valuesList.add(n);
				int[] arr = convertIntegers(valuesList);
				Arrays.sort(arr);
				Node child = new Node(arr, node);
				node.addChild(child);
				n++;
				temp = val - n;
			}
		}
		for(Node child: node.getChildren()){
			makeTree(child);
		}
	}

	//converts Integer Arraylist to int array.
	public static int[] convertIntegers(ArrayList<Integer> valuesList){
		int[] ret = new int[valuesList.size()];
		for (int i=0; i < ret.length; i++)
		{
			ret[i] = valuesList.get(i).intValue();
		}
		return ret;
	}

	//Recursively min/max values for all nodes in graph.
	//Takes in root as parameter.
	public static void setMINMAX(Node node){
		boolean goodPath=true;
		if(!node.parent.getMax()){ //if parent is min
			node.maxTrue(); //set node's max bool to true
		}
		for(Node child : node.getChildren()){
			setMINMAX(child);
		}
	}

	//Figures out guaranteed path to computer's success and marks nodes accordingly.
	//Takes in root as parameter.
	public static void makeGoodPath(Node node){		
		for(Node child : node.getChildren()){
			makeGoodPath(child);
		}
		System.out.println("Examining: "+node);

		if(node.getMax()){ //if node is max
			for(Node child : node.getChildren()){
				if(child.getGoodPath()){
					node.setGoodPath(true);
					System.out.println("IS A Guaranteed Path: "+node);
				}
			}
		}else{ //if node is min 
			node.setGoodPath(true);
			for(Node child : node.getChildren()){
				if (!child.getGoodPath()){
					node.setGoodPath(false);
					System.out.println("NOT A Guaranteed Path: "+node);
				}
			}
			if(node.getChildren().isEmpty()){
				System.out.println("GOAL STATE: "+node);
			}
		}
		
	}

}