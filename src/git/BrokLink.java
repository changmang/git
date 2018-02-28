package git;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class BrokLink {

	static {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
	}
	public static void main(String[] args) 
	{	
		String brokLink = "./BrokenLinks.txt";
		String thirdLink = "./ThirdLinks.txt";
		String workLink = "./WorkingLinks.txt";
		
		File bl=new File(brokLink);
		File tl=new File(thirdLink);
		File wl=new File(workLink);
		FileWriter writer;
		
		
		try {
			writer = new FileWriter(tl);
			BufferedWriter bufthird = new BufferedWriter(writer);
		}catch(Exception e) {e.printStackTrace();}
		
		try {
			writer = new FileWriter(wl);
			BufferedWriter buffwork = new BufferedWriter(writer);
		}catch(Exception e) {e.printStackTrace();}
		
		int status =200;
		HttpURLConnection huc = null;
		String homePage = "https://www.facebook.com";
		WebDriver d = new ChromeDriver();
		d.get("https://www.facebook.com");

		List<WebElement> allLinks = d.findElements(By.xpath("//a"));
		int noOfLinks = allLinks.size();

		System.out.println(" Total no of links " + allLinks);



		for(WebElement link:allLinks) 
		{
			String url = link.getAttribute("href");
			if(url== null || url.isEmpty())
			{
				System.out.println("Anchor is not configured"+url);
				continue;
			}
			
			if(!url.startsWith(homePage))
			{
				try {
					writer = new FileWriter(bl);
					BufferedWriter bufbrok = new BufferedWriter(writer);
					bufbrok.write(url + "\n");
					bufbrok.flush();
				}catch(Exception e) {e.printStackTrace();}
				System.out.println("Third Party Link "+url);
				
				continue;
			}

			if(url.startsWith(homePage))
			{
				try {
					URL u = new URL(url);
					huc=(HttpURLConnection)(u.openConnection());
					huc.setRequestMethod("HEAD");
					huc.connect();
					int respCode = huc.getResponseCode();
					if(respCode>=400 && respCode<=499)
					{
						System.out.println(url + "link is broken");
					}
					else
					{
						System.out.println(url  + "link is working fine");
					}	
				}

				catch(Exception e)
				{
					e.printStackTrace();
				}
			}

		}
		System.out.println("Total no of Links" + noOfLinks);
		d.close();
	}
}

