package secondlastcommon.Day19;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class discoverpattern {
	public static void main(String[] args) throws IOException {		
		String longestcommonPattern = discoverPattern("http://www.alexa.com/topsites/global;0", "Google.com,Youtube.com,Facebook.com,Baidu.com,Yahoo.com");
//		String longestcommonPattern = discoverPattern("https://moz.com/top500", "Google.com,Youtube.com,Facebook.com,Yahoo.com");
//		String longestcommonPattern = discoverPattern("https://www.quantcast.com/top-sites", "Google.com,Youtube.com,Facebook.com,Yahoo.com");
//		String longestcommonPattern = discoverPattern("https://www.similarweb.com/global", "Google.com,Youtube.com,Facebook.com,Yahoo.com");
//		System.out.println(longestcommonPattern);

		/* This is a drive function to find links from a site called alexa.com based on the longest common 
		 * pattern discovered above */
		if(!longestcommonPattern.equals("ERROR!"))
			System.out.println(scrapeBasedOnPattern("http://www.alexa.com/topsites/global;", longestcommonPattern, 7));
		else
			System.out.println("This API needs at least two comma separated strings to find a pattern...");
	}
		
	/* Returns all common substring between two strings sorted in descending order of length of the common 
	 * substring */
	static void findcandidatePatterns(String s1, String s2, Set<String> candidatePatterns)
	{
		int[][] DP = new int[s1.length()][s2.length()];
		for(int i = 0;i<s1.length();i++)
		{
			for(int j = 0;j<s2.length();j++)
			{
				if(s1.charAt(i) == s2.charAt(j))
				{
					if(i-1 >= 0 && j-1>=0)
						DP[i][j] = DP[i-1][j-1] + 1;
						
					else
						DP[i][j] = 1;		
				}
			}
		}
		
		/* recording all the common substrings */
		for(int i = 0;i<DP.length;i++)
		{
			for(int j = 0;j<DP[0].length;j++)
			{
				if(DP[i][j] > 0)
					candidatePatterns.add(s1.substring(i-DP[i][j]+1, i+1));
			}
		}
	}
	
	static String discoverPattern(String url, String keywordsstring) throws IOException
	{
		String[] keywords  = keywordsstring.toLowerCase().split(",");
		ArrayList<Set<String>> sets = new ArrayList<Set<String>>(); 
		
		if(keywords.length < 2)
			return "ERROR!";
		
		for(int i = 0;i<keywords.length;i++)
			sets.add(getallLines(url, keywords[i]));
		
		Set<String> candidatePatterns = new HashSet<>();
		for(String s1 : sets.get(0))
			for(String s2 : sets.get(1))
				findcandidatePatterns(s1, s2, candidatePatterns);
		
		String[] candidatePatternsarray = candidatePatterns.toArray(new String[candidatePatterns.size()]);
	    Arrays.sort(candidatePatternsarray, new Comparator<String>() {
	        @Override
	        public int compare(String o1, String o2) {
	            return o2.length()-o1.length();
	        }
	    });
	    	    
	    /* find longestcommonPattern contained in sets[2:keywords-1] */
	    String longestcommonPattern = candidatePatternsarray[0];
	    if(keywords.length == 2)
	    	return longestcommonPattern;
	    
	    for(int i = 0;i<candidatePatternsarray.length;i++)
	    {
	    	longestcommonPattern = candidatePatternsarray[i];
	    	int found = 0;
	    	for(int j = 2;j<sets.size();j++)
	    	{
	    		found = 0;
	    		for(String line : sets.get(j))
	    		{
	    			if(line.contains(longestcommonPattern))
	    			{
	    				found = 1;
	    				break;
	    			}	
	    		}
	    		if(found == 0)
	    			break;
	    	}
	    	if(found == 1)
	    		break;
	    }
	
		return longestcommonPattern;
	}
	
	static Set<String> getallLines(String url, String pattern) throws IOException
	{
		/* output container */
		Set<String> set = new HashSet<>();
		
		/* setting the base url */
		URL baseURL = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(baseURL.openStream()));

        /* reading in the entire file */
        String line;
        while ((line = in.readLine()) != null)
        {
        	/* found a line containing the link 
        	 * looks like this - <li class="site-listing"><div class="count">2</div><div class="desc-container"><p class="desc-paragraph"><a href="/siteinfo/youtube.com">Youtube.com</a> */
        	if(line.contains(pattern))
        		set.add(line.trim());        			        		
        }
        in.close();
        
        return set;
	}
	
	static void print(String[] strings)
	{
		for(String string : strings)
			System.out.println(string);
		System.out.println("");
	}
	
	static ArrayList<String> scrapeBasedOnPattern(String url, String pattern, int n) throws IOException
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
			URL baseURL = new URL(url+ Integer.toString(page));
	        BufferedReader in = new BufferedReader(
	        new InputStreamReader(baseURL.openStream()));

	        /* reading in the entire file */
	        String line;
	        while ((line = in.readLine()) != null)
	        {
	        	/* found a line containing the link 
	        	 * looks like this - <li class="site-listing"><div class="count">2</div><div class="desc-container"><p class="desc-paragraph"><a href="/siteinfo/youtube.com">Youtube.com</a> */
	        	if(line.contains(pattern))
	        	{        		
	        		int start = line.indexOf(pattern) + pattern.length();
	        		int end = start;
	        		String domainname = "";
	        		String allowedspecialcharacters = "_.~!*''();:@&=+$,/?#[%-]+";//RFC spec : http://www.faqs.org/rfcs/rfc1738.html
	        		while(true)
	        		{
	        			if(end == line.length())
	        				break;
	        			
	        			char here = line.charAt(end); 
	        			if( (allowedspecialcharacters.contains(""+here)) || (here>='A' && here<='Z') || (here>='a' && here<='z') || (here >= '0' && here<='9') )
	        				end++;
	        			else
	        				break;
	        		}
	        		domainname = line.substring(start, end);
	        		output.add(domainname);
	        		
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