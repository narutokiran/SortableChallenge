import java.util.*;
import java.io.*;
public class Trie
{

    private static final int ALPH = 36;
    private static final int BUFSIZ = 1024;
    private Trie[]  links;
    private boolean isWord;
    private String productName;
    private ArrayList<String> result; 
    /**
     *  create a new Trie (characters a-z)
     */
    
    public Trie()
    {
    	result=new ArrayList<String>();
		links = new Trie[ALPH];
		isWord = false;
    }

    /**
    * Check a given character lies between 0-9 or a-z
    * @param k The char to be checked
    * return 1 if lies in 0-9 else 0
    */
    int checkNumOrChar(char k)
    {
    	if(k >= '0' && k <= '9')
    		return 1;
    	else
    		return 0;
    }
    /**
     * Add a string to the trie
     *
     * @param s The string added to Trie
     */
    
    public void addString(String s,String productName)
    {
	Trie t = this;
	int k;
	int limit = s.length();
	for(k=0; k < limit; k++)
	{
		int index;
		if(s.charAt(k)==' ')
			continue;
		int numOrChar=checkNumOrChar(s.charAt(k));
		if(numOrChar==0)
	    	index = s.charAt(k)-'a';
	    else
	    	index=26+s.charAt(k)-'0';
	    if (t.links[index] == null)
	    {
		t.links[index] = new Trie();
	    }
	    t = t.links[index];
	}
	t.isWord = true;
	t.productName=productName;
    }

    /**
     * determine if a word is in the trie (here or below)
     *
     * @param s The string searched for
     * @return true iff s is in trie (rooted here)
     */
    
    public boolean isWord(String s)
    {
	Trie t = this;
	int k;
	int limit = s.length();
	for(k=0; k < limit; k++)
	{
		int index;
		if(s.charAt(k)==' ')
			continue;
		int numOrChar=checkNumOrChar(s.charAt(k));
		if(numOrChar==0)
	    	index = s.charAt(k)-'a';
	    else
	    	index=26+s.charAt(k)-'0';
	    if (t.links[index] == null) return false;
	    t = t.links[index];
	}
	return t.isWord;
    }

    public boolean isMatch(String s, int start, String line)
    {
    	//System.out.println("hi");
    	Trie t = this;
		int k;
		int limit = s.length();
		for(k=start; k < limit; k++)
		{
			int index;
			if(s.charAt(k)==' ')
				continue;
			int numOrChar=checkNumOrChar(s.charAt(k));
			if(numOrChar==0)
	    		index = s.charAt(k)-'a';
	    	else
	    		index=26+s.charAt(k)-'0';
	    if (t.links[index] == null) return false;
	    t = t.links[index];
	    if(k+1<limit && s.charAt(k+1)==' ' && t.isWord)
	    {
	    	System.out.println(t.productName);
	    	t.result.add(line);
	    	return t.isWord;
	    }
	}
	if(t.isWord)
	{
		System.out.println(t.productName);
		t.result.add(line);
	}
	return t.isWord;
    }

    void doPrint(Trie t, BufferedWriter bw)throws Exception
    {
	if (t != null)
	{
	    if (t.isWord)
	    {
	    	bw.write("{\"product_name\":"+t.productName+",\"listings\":[");
			if(t.result.size()==0)
				bw.write("]}\n");
			else
			{
				for(int i=0;i<t.result.size();i++)
				{
					if(i!=t.result.size()-1)
						bw.write(t.result.get(i)+",");
					else
						bw.write(t.result.get(t.result.size()-1)+"]}\n");		
				}
			}
	    }
	    int k;
	    for(k=0; k < ALPH; k++)
	    {
		if (t.links[k] != null)
		{
		    doPrint(t.links[k],bw);
		}
	    }
	}
    }
}
