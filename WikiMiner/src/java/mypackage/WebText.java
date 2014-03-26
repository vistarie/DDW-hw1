package mypackage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Martina
 */
public class WebText {
    private String text;
    private String url;
    private String[][] lookup;
    private int lookupId;
    private String urlString;
    
    public WebText(){
        text = null;
        url = null;
    }
    
    public String getText() {
       return text;
    }

    public void setText(String text) {
       this.text = text;
    }
    
    public String getUrl() {
       return url;
    }

    public void setUrl(String url) {
       this.url = url;
    }
    
    public int getLookupId() {
       return lookupId;
    }

    public void setLookupId(int lookupId) {
       this.lookupId = lookupId;
    }
    
    public String getUrlString() {
       return urlString;
    }

    public void setUrlString(String urlString) {
       this.urlString = urlString;
    }
      
    private void setLookup(String[][] lookup){
        this.lookup = lookup;
    }
    
    public String[][] getLookup() {
       return lookup;
    }
    
    public void mine(){
        try {
            Document doc = Jsoup.connect(getUrl()).get();
            if(getUrl().contains("wikipedia")){
            Elements newsHeadlines = doc.select("#mw-content-text p");
            setText(newsHeadlines.text());
            } else {
            setText(doc.text());    
            }
            Gate gate = new Gate(getText());
            gate.run();
            setLookup(gate.getLookup());

        } catch (IOException ex) {
            Logger.getLogger(WebText.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void checkUrl(){
         try {
        
        String wikiUrl = "http://en.wikipedia.org/wiki/" + getUrlString().replace(" ", "_");
        Document doc = Jsoup.connect(wikiUrl).get();
        Elements newsHeadlines = doc.select("#mw-content-text p");
        String urlText = newsHeadlines.text();
        
        if(!urlText.contains("There were no results matching"))
         setUrlString("<a href='"+ wikiUrl + "'>" + getUrlString() + "</a>");
        
        } catch (IOException ex) {
            Logger.getLogger(WebText.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
