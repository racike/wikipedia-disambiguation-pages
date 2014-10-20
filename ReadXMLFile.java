import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class ReadXMLFile {
 
  public static void main(String[] args) {
 
    try {
 
	File file = new File("sample-enwiki-latest-pages-articles2.xml");
 
	DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
                             .newDocumentBuilder();
 
	Document doc = dBuilder.parse(file);
 
	if (doc.hasChildNodes()) {
 
		findNote(doc.getChildNodes());
 
	}
 
    } catch (Exception e) {
	System.out.println(e.getMessage());
    }
 
  }
  
  private static boolean test(String output) throws FileNotFoundException
  {
	  String pattern = new Scanner(new File("sample-enwiki-latest-pages-articles2_output.xml")).useDelimiter("\\A").next();
	  
	  pattern = pattern.trim();
	  output = output.trim();
	  
	  System.out.println(pattern);
	  
	  if(output.equals(pattern))
	  {
		  System.out.println("Test succeeded.");
	  }
	  else
	  {
		  System.out.println("Test failed."); 
	  }
	  
	  return false;
  }
  
  private static String printNote(String text)
  {
	  StringBuilder output = new StringBuilder();
	  
	  Pattern pattern = Pattern.compile("\\*\\[\\[.*");
	  Matcher matcher = pattern.matcher(text);
	  
	  String tempItem;
	  
	  output.append("<disambiguation>\n");
	  
	  while(matcher.find())
	  {
		  tempItem = matcher.group();
		  
		  tempItem = tempItem.replace("*[[","");
		  tempItem = tempItem.replace("]]","");
		  
		  String[] element = tempItem.split(",");
		  
		  element[0] = element[0].trim();
		  element[1] = element[1].trim();
		  
		  
		  output.append("  <item>\n");
		  output.append("    <title>" + element[0] + "</title>\n");
		  output.append("    <description>" + element[1] + "</description>\n");
		  output.append("  </item>\n");		
	  }

	  output.append("</disambiguation>\n");
	  System.out.println(output.toString());
	  
	  return output.toString();
  }
 
  private static void findNote(NodeList nodeList) throws FileNotFoundException {
		  
	Node rootNode = nodeList.item(0);				
	NodeList rootNodeList = rootNode.getChildNodes();

    for (int count = 0; count < rootNodeList.getLength(); count++) {
 
	Node tempNode = rootNodeList.item(count);
	
	if(tempNode.getNodeName().equals("page")) {
		
		NodeList tmpNodeList = tempNode.getChildNodes();
		
		for (int i = 0; i < tmpNodeList.getLength(); i++) {
			Node revisionNode = tmpNodeList.item(i);
			
			if(revisionNode.getNodeName().equals("revision"))
			{
				NodeList tmp2NodeList = revisionNode.getChildNodes();
				
				for (int j = 0; j < tmp2NodeList.getLength(); j++) {
					Node textNode = tmp2NodeList.item(j);
					
					if(textNode.getNodeName().equals("text") &&  textNode.getTextContent().contains("{{disambiguation}}"))
					{
						String output = printNote(textNode.getTextContent());
						test(output);
					}
				}
			}
		}

	}
	 
    }
 
  }
 
}