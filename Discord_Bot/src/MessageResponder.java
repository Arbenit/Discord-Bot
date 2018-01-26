import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class MessageResponder extends ListenerAdapter{
	

	public void onMessageReceived (MessageReceivedEvent event) {
		String sendMessageBack;
		String message = event.getMessage().getContent();
		
		if(message.startsWith(".help"))	
		{
			String name = event.getAuthor().getName();
			event.getTextChannel().sendMessage("I can't help you please go ask someone else " + name).queue();
		}
		else if(message.startsWith(".Find"))
		{

			 HttpURLConnection connection = null;
			try {
				URL url = new URL("https://kitsu.io/api/edge/anime?filter%5Bslug%5D=cowboy-bebop");
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
				    StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
				    String line;
				    while ((line = rd.readLine()) != null) {
				      response.append(line);
				      response.append('\r');
				    }
				    rd.close();
				    String pleaseWork =  response.toString();
				
				 int loc = pleaseWork.indexOf("slug");
				   String print = pleaseWork.substring(loc + 7,loc+19);
				    
				    event.getTextChannel().sendMessage(print).queue();
				    
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
			    if (connection != null) {
			        connection.disconnect();
			      }
			    }
			
		}
	}
}
