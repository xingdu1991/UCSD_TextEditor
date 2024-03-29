package spelling;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteMatchCase implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;
    

    public AutoCompleteMatchCase()
	{
		root = new TrieNode();	
		size = 0;
	}
	
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should ignore the word's case.
	 * That is, you should convert the string to all lower case as you insert it. */
	public boolean addWord(String word)
	{
	    //TODO: Implement this method.
		String wordtoAdd = word.toLowerCase();
		TrieNode node = root;
		if (isWord(wordtoAdd)||wordtoAdd.isEmpty()){
			return false;
		}
		for (int i=0; i<wordtoAdd.length();i++){
			char c = wordtoAdd.charAt(i);
			if (node.getValidNextCharacters().contains(c)){
				node = node.getChild(c);
			}else{
				node = node.insert(c);
			}
		}
		if(!node.endsWord()){
			node.setEndsWord(true);
			size += 1;
			return true;
		}
		return false;
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //TODO: Implement this method
	    return size;
	}
	
	
	/** Returns whether the string is a word in the trie */
	@Override
	public boolean isWord(String s) 
	{
	    // TODO: Implement this method
		
		String wordtoCheck = s.toLowerCase();
		TrieNode node = root;
		if (wordtoCheck.isEmpty()){
			return false;
		}
		for (int i=0; i<wordtoCheck.length();i++){
			char c = wordtoCheck.charAt(i);
			if (node.getValidNextCharacters().contains(c)){
				node = node.getChild(c);
			}else{
				return false;
			}
		}
		if(node.endsWord()){
			return true;
		}
		return false;
    }
	

	/** 
	 *  * Returns up to the n "best" predictions, including the word itself,
     * in terms of length
     * If this string is not in the trie, it returns null.
     * @param text The text to use at the word stem
     * @param n The maximum number of predictions desired.
     * @return A list containing the up to n best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie.  If the stem does not appear in the trie, return an
    	 //    empty list (result)
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList, named newQueue) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 String prefixToMatch = prefix.toLowerCase();
    	 List<String> result = new LinkedList<String>();
    	 TrieNode node = root;
    	 for (int i=0; i< prefixToMatch.length();i++){
    		char c = prefixToMatch.charAt(i);
 			if (node.getValidNextCharacters().contains(c)){
 				node = node.getChild(c);
 			}else{
 				return result;
 			}
    	 }
    	 int count = 0;
    	 if (node.endsWord()){     //stem found
    		 result.add(node.getText());
    		 //add the node that completes the stem to the back
        	 //of the list.
    		 count ++;
    	 }
    	 //Create a queue (LinkedList, named newQueue) and add the node that completes the stem to the back
    	 //       of the list.
    	 List <TrieNode> newQueue = new LinkedList<TrieNode>();
    	 //Create a list of completions to return (initially empty)
    	 List <Character> autofill= new LinkedList<Character>( node.getValidNextCharacters());
    	 
  		 for (int i=0; i<autofill.size(); i++) {
  			char c = autofill.get(i);
  			newQueue.add(node.getChild(c));
  		 }
  		 while (count<numCompletions && ! newQueue.isEmpty() ){
  			 TrieNode tn = newQueue.remove(0);//remove the first Node from the queue
  			 if (tn.endsWord()){
  				 result.add(tn.getText());//If it is a word, add it to the completions list
  				 count ++;
  			 }
  			 //Add all of its child nodes to the back of the queue
  			 List<Character> temp = new LinkedList<Character> (tn.getValidNextCharacters());
  			 
  			 for (int i=0; i< temp.size();i++){
  				 char c = temp.get(i);
  				 newQueue.add(tn.getChild(c));
  			 }
  		 }
  		
         return result;
     }

 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) 
 			return;
 		
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}