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
		
		
		
		
		
	}
	
	 
	  
}
