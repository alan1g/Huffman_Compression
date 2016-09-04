package Huffmann;

import java.util.*;


/**
 * This class creates a Huffman code for a given String 
 * @author alan1g
 * @version 1.0
 */
public class HuffmanCompression 
{
	public static void main(String args[])
	{
		//Part A: Print full binary code of given String
		
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter your sentence:");
		String s = scan.nextLine();
		
		String binaryString="";      //this stores the string of binary code
	          
	    for(int i=0; i < s.length(); i++) //go through the sentence
	    {       
	        int decimalValue = (int)s.charAt(i);      //convert to decimal each character
	        String binaryValue = Integer.toBinaryString(decimalValue);      //convert to binary
	           
	        for(int j=7;j>binaryValue.length();j--)
	        {
	             binaryString+="0";           //this loop adds in leading zeroes
	        }
	        binaryString += binaryValue+" "; //add to the string of binary
	    }
	    System.out.println("This sentence in Binary Code Representation is:");
	    System.out.println(binaryString);    //print out the binary
	    System.out.println("Number of Bits: "+s.length()*7+"\n");
	    
	    ///////////////////////////////////////////////////////////////////////////////////////////////////
	    //PartB: Store characters and their frequency in Nodes and insert to Priority Queue
	    
	    int ar[] = new int[256];	//create array to store letters by ASCI value
		int counter =0;		//variable to store how many different characters appear
		
		for(int i=0;i<s.length();i++)	//loop through string and cast character to ASCI value
		{
			int value = (int)s.charAt(i);	
			if(ar[value] == 0)
			{
				counter++;
			}
			ar[value]++;	//increment frequency index
		}
		PQ nodes = new PQ(counter);	//Priority Queue of type Bucket 
				
		for(int i=0;i<ar.length;i++)	//insert Bucket to PQ
		{
			if(ar[i]>0)
			{
				char c = (char)i;
				String character = ""+c;
				int frequency = ar[i];
				Bucket a = new Bucket(character,frequency);
				nodes.insert(a);
			}
		}
		
		///////////////////////////////////////////////////////////////////////////////////////////////////
		//Part C: Create Tree Priority Queue And insert characters and their frequency to Trees
		//		  Merge all trees together to form ComboTree
		
		PriorityQueue < Tree > treePQ = new PriorityQueue < Tree >() ;//Create Java Class PQ

		System.out.println("The Frequency and Alphabetical order of this sentence is:");
		
		while(!nodes.isEmpty())//Create Tree objects and insert to treePQ
		{

			Bucket temp = nodes.remove();
			System.out.print(temp.character+",");//print each character to screen in order of frequency and then alphabetically

			Tree object = new Tree();
			object.weighing = temp.frequency;
			object.root = new Node();
			char c = temp.character.charAt(0);//convert from String to char
			object.root.letter = c;
			treePQ.add(object);
		}
		System.out.println("\n");
		System.out.println("Creating Huffmann Tree");
		System.out.println("---------------------------");
		
		while(treePQ.size()!=1)//Merge all trees in treePQ until one remaining, comboTree
		{	
			Tree temp1 = treePQ.remove();
			Tree temp2 = treePQ.remove();
			Tree combo = new Tree();
			combo.weighing = temp1.weighing+temp2.weighing;
			combo.root =new Node();
			combo.root.leftChild = temp1.root;
			combo.root.rightChild = temp2.root;
			treePQ.add(combo);
		}
		
		//////////////////////////////////////////////////////////////////////////////////////////////////
		//Part D: Remove comboTree and use Huffman algorithm to print code to screen, 
		//		  Calculate compression rate
		
		System.out.println("Huffman Code for each letter of the sentence:\n");
		
		Tree Huffman = treePQ.remove();//remove comboTree
		String huffmanCode  = "";
		for(int i= 0; i < s.length(); i++)//Create Huffman code from given String
		{
			String code = Huffman.getCode(s.charAt(i));
			huffmanCode += ""+code;
			System.out.print(s.charAt(i)+": "+code+", ");
		}
		
		//Calculate compression rate
		System.out.println("\n");
		System.out.println("Number of compressed bits are:");
		System.out.println(huffmanCode.length());
		double first = (double)huffmanCode.length();
		double second = (double)s.length()*7;
		double compression = (first/second)*100;
		System.out.println("Compression: "+compression+"%");
		
		scan.close();
	}

}//end of main
/**
 * This class describes object of type Node
 * @author alan1g
 * @version 1.0
 */
class Node 
{

	public char letter; // stores letter

	public Node leftChild; // this node's left child
	public Node rightChild; // this node's right child

} // end class Node

/**
 * This class describes object of type Tree
 * @author alan1g
 * @version 1.0
 */
class Tree implements Comparable<Tree> 
{
	public Node root;//first node of tree
	public int weighing=0;
	
	public Tree()
	{
		root = null;
	}
	public int compareTo(Tree object)
	{
		if(weighing-object.weighing>0)
		{
			 return 1;
		}
		else if(weighing-object.weighing<0)
		{
			 return -1;
		}
		else
		{
			 return 0; //return 0 if they're the same
		}

	}
	
	String path = "error";
	
	public String getCode(char letter) 
	{ 
		inOrder(root, letter, "");//call on inOrder Method 

		return path; 
	}


	private void inOrder(Node localRoot, char letter, String path) 
	{
		if (localRoot != null) //keep searching until end of branch
		{
			if (localRoot.letter==letter)
			{
				this.path = path; //Letter found and entire code is passed up to getCode and returned to main
			} 
			else 
			{
				inOrder(localRoot.leftChild, letter, path + "0");//go left add 0 to path
				inOrder(localRoot.rightChild, letter, path + "1");
			}
		}
		return; //letter was not found and recursion stops for given path
	}

} // end class Tree

/**
 * This class describes object of type Bucket
 * @author alan1g
 */
class Bucket//for Class PQ
{
	//Attributes
	int frequency;
	String character;
	
	//Constructor
	public Bucket(String character,int frequency)
	{
		this.character = character;
		this.frequency = frequency;
	}
}//end of class node

/**
 * Class which describes object of type PQ
 * @author alan1g
 * @version 1.0
 */
class PQ 
{
	//Attributes
	private int maxSize;
	private Bucket queArray[];
	private int front;
	private int nItems;
	
	//Constructor
	public PQ(int s)
	{
		maxSize=s;
		queArray=new Bucket[maxSize];
		front =0;
		nItems=0;
	}
	
	/**
	 * insertion sort method: orders Queue in order of highest frequency
	 * if frequency is the same nodes are ordered alphabetically
	 * @param item: Node
	 */
	public void insert(Bucket item)
	{
		if(nItems ==0)
		{
			queArray[0]=item;
		}
		else
		{
			int j= nItems;
			while(j>0  && (queArray[j-1].frequency > item.frequency || (queArray[j-1].frequency == item.frequency && queArray[j-1].character.compareTo(item.character)< 0)))
			{
				queArray[j]=queArray[j-1];
				j--;
			}
			queArray[j]=item;
		}
		front = nItems;
		nItems++;
		
	}
	public Bucket remove()
	{
		if(isEmpty())
		{
			return null;
		}
		Bucket temp = queArray[front];
		front--;
		if(front == maxSize)
		{
			front =0;
		}
		nItems--;
		return temp;
	}
	public boolean isEmpty()
	{
		return (nItems==0);
	}
	
}//end of class PQ





