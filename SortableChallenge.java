import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class SortableChallenge
{
  public static String formatString(String s)
  {
    s=s.trim();
    s= s.replaceAll("[-()!/.]","");
    s=s.replace("_"," ");
    s=s.toLowerCase();
    return s;
  }

  public static void printTrie(Trie t, BufferedWriter bw)throws Exception
  {
    if(t!=null)
      t.doPrint(t,bw);
  }
   public static void main(String[] args) throws Exception
   {
   		 BufferedReader br=new BufferedReader(new FileReader("products.txt"));
        String product;
        JSONParser parser=new JSONParser();
        HashMap<String,Trie> manufacturerLookUp=new HashMap<String,Trie>();
        int count=0;
        while((product=br.readLine())!=null)
        {
        	try 
        	{
        		//System.out.println(product);
     			  JSONObject obj =(JSONObject) parser.parse(product);
            String manufacturer=(String)obj.get("manufacturer");
            manufacturer=manufacturer.toLowerCase();
            String modelName=SortableChallenge.formatString((String)obj.get("model"));
            String productName=(String)obj.get("product_name");
            //System.out.println(++count);
            if(manufacturerLookUp.containsKey(manufacturer))
            {
             	if(!manufacturerLookUp.get(manufacturer).isWord(modelName))
             		{
             			//System.out.println(manufacturer+" "+modelName);
             			Trie temp = manufacturerLookUp.get(manufacturer);
             			temp.addString(modelName,productName);
             			manufacturerLookUp.put(manufacturer,temp);
             		}
             	}
             	else
             	{
             		Trie temp=new Trie();
                temp.addString(modelName,productName);
             		manufacturerLookUp.put(manufacturer,temp);
             	}
            }
   			catch (Exception e) 
   			{
            	// JSON Parsing error
            	e.printStackTrace();
        	}
    	}
    	br.close();
      br=new BufferedReader(new FileReader("listings1.txt"));
      String listing;
      while((listing=br.readLine())!=null)
      {
          //System.out.println(listing);
          JSONObject obj =(JSONObject) parser.parse(listing);
          String manufacturer=(String)obj.get("manufacturer");
          manufacturer=manufacturer.toLowerCase();
          //System.out.println(manufacturer);   
          String formatedListing=formatString((String)obj.get("title"));
          //System.out.println(formatedListing);
          String words[]=formatedListing.split(" ");
          int wordPosition=0;
          for(int i=0;i<words.length;i++)
          {
              Trie temp=manufacturerLookUp.get(manufacturer);
              if(temp==null)
              {
                String manufacturer_split[]=manufacturer.split(" ");
                //example Nokia Canada - it is enough to check for Nokia.
                temp=manufacturerLookUp.get(manufacturer_split[0]);
              }
                if(i!=0)
                  wordPosition+=words[i-1].length()+1;
                if(temp!=null && temp.isMatch(formatedListing,wordPosition,listing))
                {
                  break;
                }
          }
      }
      br.close();  
      BufferedWriter bw=new BufferedWriter(new FileWriter("out.txt"));
      for(String key:manufacturerLookUp.keySet())
      {
        SortableChallenge.printTrie(manufacturerLookUp.get(key),bw);
      }
      bw.close();
   }
}