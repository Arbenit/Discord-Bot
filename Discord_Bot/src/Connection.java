import java.io.IOException;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class Connection {

	public static BotSettings discordBot = new BotSettings();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JDA discord = null;
		
		try {
			discord = new JDABuilder(AccountType.BOT).setToken(Constants.discordToken).buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
			// TODO Auto-generated catch block
			System.out.println("Something went wrong and the bot could not connect");
		}
		
		discord.addEventListener(new MessageResponder());
		
		discordBot.readConfig();
	
	
		
		
		
		
	}

}
