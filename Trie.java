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
    
    // Trie that stores the values between A-Z and 0-9
    public Trie()
    {
    	result=new ArrayList<String>();
		links = new Trie[ALPH];
		isWord = false;
    }

    //check if the given char is number or alphabet
    int checkNumOrChar(char k)
    {
    	if(k >= '0' && k <= '9')
    		return 1;
    	else
    		return 0;
    }
   
    //add model name of the manufacturer into the trie
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

    
    //check if the trie contains the model name of the manufacturer
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

    //Append the listing to the model's result if it matches
    public boolean isMatch(String s, int start, String line)
    {
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
	    	if(index>35)
	    		continue;
	    if (t.links[index] == null) return false;
	    t = t.links[index];
	    if(k+1<limit && (s.charAt(k+1)==' ' || index>35) && t.isWord)
	    {
	    	t.result.add(line);
	    	return t.isWord;
	    }
	}
	if(t.isWord)
	{
		t.result.add(line);
	}
	return t.isWord;
    }
    //print all models' results of the manufacturer
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
