import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class BotSettings {
	public double averageRatingLimit;
	public int popularityLimit;
	public int rankingLimit;
	public boolean hasEnglishTitle;
	
	//to be used if no config file is found
	public BotSettings()
	{
		averageRatingLimit = 0;
		popularityLimit = 10000;
		rankingLimit =  10000;
		hasEnglishTitle = false;
	}
	
	//to be used with config file
	public BotSettings(double average, int popularity,int ranking, boolean hasEnglish)
	{
		averageRatingLimit = average;
		popularityLimit = popularity;
		rankingLimit =  ranking;
		hasEnglishTitle = hasEnglish;
	}
	
	public void readConfig()
	{
		String workingDir = System.getProperty("user.dir");
		String configFile = workingDir + "\\config.config";//stores the location of the config file
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream(configFile);
			prop.load(input);
		} catch (FileNotFoundException e) {
			return;//leave the function if you cant find the config file and just use the default values
		} catch (IOException e) {
			return;//leave the function if you cant find the config file and just use the default values
		}	
		this.averageRatingLimit = Double.parseDouble(prop.getProperty("averageRatingLimit"));
		this.popularityLimit = Integer.parseInt( prop.getProperty("popularityLimit"));
		this.rankingLimit = Integer.parseInt( prop.getProperty("rankingLimit"));
		this.hasEnglishTitle = Boolean.parseBoolean(prop.getProperty("hasEnglishTitle"));	
	}
}
