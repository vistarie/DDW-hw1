package mypackage;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CreoleRegister;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Node;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martina
 */
public class Gate {
 
    // corpus pipeline
    private static SerialAnalyserController annotationPipeline = null;
    
    // whether the GATE is initialised
    private static boolean isGateInitilised = false;
    
    private String text;
    private ArrayList locationsCity;
    private ArrayList locationsCountry ;
    private ArrayList company ;
    private ArrayList building ;
  //  private String[] locationsCountry;
    
    public Gate(String text){
     this.text = text;
    }

    private String[] getCompany(){
       return arrayListToString(company);
    }
    private String[] getBuilding(){
       return arrayListToString(building);
    }
    
    private String[] getLocationsCity(){
       return arrayListToString(locationsCity);
    }
    
    private String[] getLocationsCountry(){
       return arrayListToString(locationsCountry);
    }
    
    private int getLongestArray(String[] s1, String[] s2, String[] s3, String[] s4){
    int[] values = new int[4];
    values[0] = s1.length;
    values[1] = s2.length;
    values[2] = s3.length;
    values[3] = s4.length;
    int max = 0;
    
    for(int i = 0; i < values.length; i++){
        if(max < values[i]) max = values[i];
    }
        
    return max;
    }
    
    public String[][] getLookup(){
    String[][] lookup = new String[4][getLongestArray(getCompany(), getLocationsCountry(), getLocationsCity(), getBuilding())];
    
    lookup[0] = getCompany();
    lookup[1] = getLocationsCountry();
    lookup[2] = getLocationsCity();
    lookup[3] = getBuilding();
    
    return lookup;
    }
    
    private String[] arrayListToString(ArrayList list){
       String[] array = new String[list.size()];
       int i = 0;
       for (Object s : list){
           array[i] = s.toString();
           i++;
       }
       
       return array;
    }
    
    
    public void run(){
        
        //org.apache.log4j.BasicConfigurator.configure();
        
        if(!isGateInitilised){
            
            // initialise GATE
            initialiseGate();            
        }        

        try {                
            // create an instance of a Document Reset processing resource
            ProcessingResource documentResetPR = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");

            // create an instance of a English Tokeniser processing resource
            ProcessingResource tokenizerPR = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");

            // create an instance of a Sentence Splitter processing resource
            ProcessingResource sentenceSplitterPR = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
            
            // Task 2
            ProcessingResource ANNIE_gazetteer = (ProcessingResource) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer");
            
            // locate the JAPE grammar file
            File japeOrigFile = new File("C:\\Users\\Martina\\Documents\\NetBeansProjects\\WikiMiner\\src\\java\\mypackage\\grammer.jape");
            java.net.URI japeURI = japeOrigFile.toURI();
            
            // create feature map for the transducer
            FeatureMap transducerFeatureMap = Factory.newFeatureMap();

            try {
                // set the grammar location
                transducerFeatureMap.put("grammarURL", japeURI.toURL());
                // set the grammar encoding
                transducerFeatureMap.put("encoding", "UTF-8");
            } catch (MalformedURLException e) {
                System.out.println("Malformed URL of JAPE grammar");
                System.out.println(e.toString());
            }
            
            // create an instance of a JAPE Transducer processing resource
            ProcessingResource japeTransducerPR = (ProcessingResource) Factory.createResource("gate.creole.Transducer", transducerFeatureMap);

            // create corpus pipeline
            annotationPipeline = (SerialAnalyserController) Factory.createResource("gate.creole.SerialAnalyserController");

            // add the processing resources (modules) to the pipeline
            annotationPipeline.add(documentResetPR);
            annotationPipeline.add(tokenizerPR);
            annotationPipeline.add(sentenceSplitterPR);
            
            //Task 2
            annotationPipeline.add(ANNIE_gazetteer);
            annotationPipeline.add(japeTransducerPR);
            
            // create a document

            // create a corpus and add the document
            Corpus corpus = Factory.newCorpus("");
            corpus.add(Factory.newDocument(this.text));

            // set the corpus to the pipeline
            annotationPipeline.setCorpus(corpus);

            //run the pipeline
            annotationPipeline.execute();

            // loop through the documents in the corpus
            for(int i = 0; i< corpus.size(); i++){

                Document doc = corpus.get(i);

                // get the default annotation set
                AnnotationSet as_default = doc.getAnnotations();

             //  FeatureMap futureMap = null;
                // get all Token annotations
                //AnnotationSet annSetTokens = as_default.get("Token",futureMap);
                //System.out.println("Number of Token annotations: " + annSetTokens.size());
                
                AnnotationSet countries = as_default.get("Country");
 
                ArrayList tokenAnnotations = new ArrayList(countries);
 
                locationsCountry = new ArrayList();
                locationsCountry.add("--");
                
                for(int j = 0; j < tokenAnnotations.size(); ++j) {

                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);

                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    if(!locationsCountry.contains(underlyingString))
                    locationsCountry.add(underlyingString);
                    

                }
                locationsCountry.remove("--");
                
                AnnotationSet buildings = as_default.get("Building");
 
                tokenAnnotations = new ArrayList(buildings);
 
                building = new ArrayList();
                building.add("--");
                
                for(int j = 0; j < tokenAnnotations.size(); ++j) {

                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);

                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    if(!building.contains(underlyingString))
                    building.add(underlyingString);
                    

                }
                building.remove("--");
                
                AnnotationSet companies = as_default.get("Company");
 
                tokenAnnotations = new ArrayList(companies);
 
                company = new ArrayList();
                company.add("--");
                
                for(int j = 0; j < tokenAnnotations.size(); ++j) {

                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);

                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    if(!company.contains(underlyingString))
                    company.add(underlyingString);
                    

                }
               company.remove("--");
                
                AnnotationSet cities = as_default.get("City");
 
                tokenAnnotations = new ArrayList(cities);
 
                locationsCity = new ArrayList();
                locationsCity.add("--");
                
                for(int j = 0; j < tokenAnnotations.size(); ++j) {

                    // get a token annotation
                    Annotation token = (Annotation)tokenAnnotations.get(j);

                    // get the underlying string for the Token
                    Node isaStart = token.getStartNode();
                    Node isaEnd = token.getEndNode();
                    String underlyingString = doc.getContent().getContent(isaStart.getOffset(), isaEnd.getOffset()).toString();
                    if(!locationsCity.contains(underlyingString))
                    locationsCity.add(underlyingString);
                    

                }
                locationsCity.remove("--");

            }
        } catch (GateException ex) {
            Logger.getLogger(Gate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initialiseGate() {
        
        try {
                      
            // set GATE home folder
            // Eg. /Applications/GATE_Developer_7.0
            File gateHomeFile = new File("C:\\Program Files\\GATE_Developer_7.1");
            gate.Gate.setGateHome(gateHomeFile);
            
            File pluginsHome = new File("C:\\Program Files\\GATE_Developer_7.1\\plugins");
            gate.Gate.setPluginsHome(pluginsHome);            

            // initialise the GATE library
            gate.Gate.init();
            
            // load ANNIE plugin
            CreoleRegister register = gate.Gate.getCreoleRegister();
            URL annieHome = new File(pluginsHome, "ANNIE").toURL();
            register.registerDirectories(annieHome);
            
            // flag that GATE was successfuly initialised
            isGateInitilised = true;
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(Gate.class.getName()).log(Level.SEVERE, null, ex);
        } catch (GateException ex) {
            Logger.getLogger(Gate.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
