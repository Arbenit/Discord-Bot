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
import java.util.Random;

import org.apache.commons.collections4.multiset.SynchronizedMultiSet;
public class MessageResponder extends ListenerAdapter{
	
	//checks to see if the value is null and replaces it with a string
	  public Anime removeNulls(Anime a, int index)
	  {
		  if(a.data.get(index).attributes.titles.en == null)
			  a.data.get(index).attributes.titles.en = "no english name";
		  if(a.data.get(index).attributes.titles.en_jp == null)
			  a.data.get(index).attributes.titles.en_jp = "no romanized name";
		  if(a.data.get(index).attributes.averageRating == null)
			  a.data.get(index).attributes.averageRating = "no average rating";
		  if(a.data.get(index).attributes.episodeCount == null)
			  a.data.get(index).attributes.episodeCount = "unknown";
		return a;
		  
	  }

	  
	  public int checkRequirements(Anime a)
	  {
		   for(int i = 0;i < a.data.size();i++)
		   {
			   if(a.data.get(i).attributes.averageRating != null && a.data.get(i).attributes.popularityRank != null && a.data.get(i).attributes.ratingRank != null)
			   {
				   if(Double.parseDouble(a.data.get(i).attributes.averageRating) >= Connection.discordBot.averageRatingLimit && //averageRating has to be higher than the limit. higher scores better
					  Integer.parseInt(a.data.get(i).attributes.popularityRank) <= Connection.discordBot.popularityLimit && //the popularityRank has to be lower. lower scores are better
					  Integer.parseInt(a.data.get(i).attributes.ratingRank) <= Connection.discordBot.rankingLimit) //the rating rank has the be lower than limit. lower scores are better
					return i;
			   }
		   }
		   System.out.println("Next 20");
		  return -1;
	  }
	  
	  
	public void onMessageReceived (MessageReceivedEvent event) {
		String message = event.getMessage().getContent();
	
	
		
		if(message.startsWith(".help"))	
		{
			String name = event.getAuthor().getName();
			event.getTextChannel().sendMessage("I can't help you please go ask someone else " + name).queue();//replace with useful help later
		}
		else if(message.startsWith(".Settings"))
		{
			event.getTextChannel().sendMessage("Use\n"
					+ ".MinimumAverageLimit" 
					+ "\n.MaxRanking" 
					+ "\n.MaxPopularity" 
					+ "\n to change the limits for the random search").queue();
		}
		else if(message.startsWith(".MinimumAverageLimit"))
		{
			Connection.discordBot.averageRatingLimit = Double.parseDouble(message.substring(20));
		}
		else if(message.startsWith(".MaxRanking"))
		{
			Connection.discordBot.rankingLimit = Integer.parseInt(message.substring(12));
		}
		else if(message.startsWith(".MaxPopularity"))
		{
			Connection.discordBot.rankingLimit = Integer.parseInt(message.substring(15));
		}
		else if(message.startsWith(".Limits"))
		{
			event.getTextChannel().sendMessage("The limits for the random anime searches are\n"
					+ "Minimum average rating is " + Connection.discordBot.averageRatingLimit
					+ "\nMax ranking = " + Connection.discordBot.rankingLimit
					+ "\nMax popularity = " + Connection.discordBot.popularityLimit).queue();
		}
		else if(message.startsWith(".Find"))
		{
			
			message = message.substring(6);//remove the .Find and a space after from the string
			message = message.toLowerCase();//sever only takes in lower case searches
			message = message.replaceAll("\\s" ,"-");//converts spaces to dashes
			 HttpURLConnection connection = null;
			try {
				URL url = new URL("https://kitsu.io/api/edge/anime?filter%5Btext%5D=" + message);
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
				 

				    
				   if(a.data.size() != 0)//if the size is not 0 that means it found an anime
				   {
					   
					 a = removeNulls(a,0);
					   
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
		else if (message.startsWith(".Random")) {
			HttpURLConnection connection = null;
			try {

				URL url;
				Random rand = new Random();
				String values = "";

				int index = -1;
				Anime a = null;
				while(index == -1)
				{
				values = "";
				
				for (int i = 0; i < 20; i++)// generate 20 random numbers
				{
					values += (rand.nextInt(7500) + 1) + "%2C";
				}
				values = values.substring(0, values.length() - 3);
				url = new URL("https://kitsu.io/api/edge/anime?filter%5Bid%5D=" + values + "&page%5Blimit%5D=20&page%5Boffset%5D=0");
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("GET");// set as get request
				connection.setRequestProperty("Accept", "application/vnd.api+json");
				connection.setRequestProperty("Content-Type", "application/vnd.api+json");
				connection.setRequestProperty("User-Agent",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

				connection.setUseCaches(false);
				connection.setDoOutput(true);// set to true since I need to	accept output
											
			
							    
				// Get Response
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

				a = gson.fromJson(response.toString(), Anime.class);// store the json in the Anime class

				 index = checkRequirements(a);
				}
					  
						
					   		a = removeNulls(a,index);
					   		event.getTextChannel().sendMessage("The english name of the show is " + a.data.get(index).attributes.titles.en + 
								   	"\nThe romanized name is " + a.data.get(index).attributes.titles.en_jp +	//Titles
						    		"\nThere are " +  a.data.get(index).attributes.episodeCount + " episodes of this show" + // episode count
						    		"\nThe average score of the show is " + a.data.get(index).attributes.averageRating +	// average rating
						    		"\n" + a.data.get(index).attributes.posterImage.original // poster image
						    		).queue();//Display the information about the show
					   
					  
			   
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
