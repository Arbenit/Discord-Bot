import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


import com.google.gson.*;

public class MessageResponder extends ListenerAdapter{
	

	  
	public void onMessageReceived (MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
	
	
		
		if(message.startsWith(".help"))	
		{
			String name = event.getAuthor().getName();
			event.getTextChannel().sendMessage("I can't help you please go ask someone else " + name).queue();//replace with useful help later
		}
		else if(message.startsWith(".Find"))
		{
			
			message = message.substring(6);//remove the .Find and a space after from the string
			message = message.toLowerCase();//sever only takes in lower case searches
			message = message.replaceAll("\\s" ,"-");//converts spaces to dashes
			 HttpURLConnection connection = null;
			try {
				URL url = new URL("https://kitsu.io/api/edge/anime?filter%5Bslug%5D=" + message);
				connection = (HttpURLConnection) url.openConnection();
				   connection.setRequestMethod("GET");//set as get request
				   connection.setRequestProperty("Accept", 
					        "application/vnd.api+json");
				   connection.setRequestProperty("Content-Type", 
					        "application/vnd.api+json");
				   connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				   
				   connection.setUseCaches(false);
				    connection.setDoOutput(true);//set to true since I need to accept output


				   
				    //Get Response  
				    InputStream is = connection.getInputStream();
				    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				    StringBuilder response = new StringBuilder();
				    String line;
				    while ((line = rd.readLine()) != null) {
				      response.append(line);
				      response.append('\r');
				    }
				    rd.close();
	
				    Gson gson = new Gson();
				    

				  Anime a = gson.fromJson(response.toString(), Anime.class);//store the json in the Anime class
				 

				    
				   if(a.data.size() != 0)//if the size is not 0 that means it find an anime
				   {
					   
					   if(a.data.get(0).attributes.episodeCount == null)
						   a.data.get(0).attributes.episodeCount = "unknown";
					   
					   event.getTextChannel().sendMessage("The english name of the show is " + a.data.get(0).attributes.titles.en + 
							   	"\nThe romanized name is " + a.data.get(0).attributes.titles.en_jp +	//Titles
					    		"\nThere are " +  a.data.get(0).attributes.episodeCount + " episodes of this show" + // episode count
					    		"\nThe average score of the show is " + a.data.get(0).attributes.averageRating +	// average rating
					    		"\n" + a.data.get(0).attributes.posterImage.original // poster image
					    		).queue();//Display the information about the show
				   }
				   else
					   event.getTextChannel().sendMessage("No anime found with that name").queue();
				   
				  
				    
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e)
			{
				e.printStackTrace();
			}finally {
			    if (connection != null) {
			        connection.disconnect();
			      }
			    }
			
		}
	}
	
}
