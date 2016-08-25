package secondlastcommon.Day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class scraping {
	public static void main(String[] args) throws IOException {
		System.out.println(fun(20));
	}
	
	static ArrayList<String> fun(int n) throws IOException
	{
		/* final output holder */
		ArrayList<String> output = new ArrayList<String>();
		int page = 0;
	
		/* don't even want to read the page */
		if(n <= 0)
			return output;
		
		while(true)
		{
			/* setting the base url */
			URL baseURL = new URL("http://www.alexa.com/topsites/global;"+ Integer.toString(page));
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(baseURL.openStream()));

	        String line;
	        /* setting the pattern */
	        String pattern = "<a href=\"/siteinfo/";
	        
	        /* reading in the entire file */
	        while ((line = in.readLine()) != null)
	        {
	        	/* found a line containing the link 
	        	 * looks like this - <li class="site-listing"><div class="count">2</div><div class="desc-container"><p class="desc-paragraph"><a href="/siteinfo/youtube.com">Youtube.com</a> */
	        	if(line.contains(pattern))
	        	{        		
	        		/* now that we've found the line, search within to find the exact link */
	        		String result = line.substring(line.lastIndexOf("\">") + ">".length()+1, line.indexOf("</a>"));
	        		output.add(result);
//	        		System.out.println(result);
	        		
	        		/* stop reading as soon as we have n links */
	        		if(output.size() == n)
	    				return output;
	        	}	        		
	        }
	        in.close();
	        
	        /* incrementing the page number */
	        page++;
		}
	}
}	
