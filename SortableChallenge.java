import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
import java.util.*;

public class SortableChallenge
{
  //Removes special characters of the String s.
  public static String formatString(String s)
  {
    s=s.trim();
    s= s.replaceAll("[-'!/*=&+.#?$]","");
    s=s.replace("(","");
    s=s.replace(")","");
    s=s.replaceAll("[_,:;]"," ");
    s=s.replace("\\","");
    s=s.replaceAll("\"","");
    s=s.replace("[","");
    s=s.replace("]","");
    s=s.replaceAll("·","");
    s=s.replaceAll("( )+"," ");
    s=s.toLowerCase();
    return s;
  }

  //print all models in the Trie
  public static void printTrie(Trie t, BufferedWriter bw)throws Exception
  {
    if(t!=null)
      t.doPrint(t,bw);
  }
   public static void main(String[] args) throws Exception
   {
      //Check if given arguments are correct
      if(args.length<3){
        System.out.println("Check readme file to correct the arguments");
        System.exit(1);}
      //Read product.txt file
   		 BufferedReader br=new BufferedReader(new FileReader(args[0]));
        String product;
        JSONParser parser=new JSONParser();
        //Hashmap with manufacturer name as Key and Trie which stores models of the manufacturer as value
        HashMap<String,Trie> manufacturerLookUp=new HashMap<String,Trie>();
        while((product=br.readLine())!=null)
        {
        	try 
        	{
     			  JSONObject obj =(JSONObject) parser.parse(product);
            String manufacturer=(String)obj.get("manufacturer");
            manufacturer=manufacturer.toLowerCase();
            String modelName=SortableChallenge.formatString((String)obj.get("model"));
            String productName=(String)obj.get("product_name");
            if(manufacturerLookUp.containsKey(manufacturer))
            {
             	if(!manufacturerLookUp.get(manufacturer).isWord(modelName))
             		{
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
      //read the listing.txt file
      br=new BufferedReader(new FileReader(args[1]));
      String listing;
      int count=0;
      while((listing=br.readLine())!=null)
      {
        try
        {
          JSONObject obj =(JSONObject) parser.parse(listing);
          String manufacturer=(String)obj.get("manufacturer");
          manufacturer=manufacturer.toLowerCase();   
          String formatedListing=formatString((String)obj.get("title"));
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
          catch (Exception e)
          {
              //JSON parsing error from listing.txt file
            e.printStackTrace();
          }
        }
      br.close();  
      //copy the result to out.txt by iterating the HashMap
      BufferedWriter bw=new BufferedWriter(new FileWriter(args[2]));
      for(String key:manufacturerLookUp.keySet())
      {
        SortableChallenge.printTrie(manufacturerLookUp.get(key),bw);
      }
      bw.close();
   }
}