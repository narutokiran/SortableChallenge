import org.json.simple.*;
import org.json.simple.parser.*;
import java.io.*;
public class JSONChecker
{
    public static boolean isJSONValid(String test) {
        JSONParser parser = new JSONParser();
        try {
               parser.parse(test);
            }
            catch(ParseException pe){
                 System.out.println("position: " + pe.getPosition());
                System.out.println(pe);
                return false;
            }
        return true;
    }
    public static void main(String args[])throws Exception
    {
        BufferedReader br=new BufferedReader(new FileReader(args[0]));
        String s;
        while((s=br.readLine())!=null)
        {
            if(!JSONChecker.isJSONValid(s))
            {
                System.out.println(s);
                break;
            }
        }
    }
}