

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonValue;

import org.apache.commons.io.FileUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.json.JSONException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONCompare;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
//import org.springframework.test.context.junit4.SpringRunner;
import org.xml.sax.InputSource;

import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import edu.uth.clamp.nlp.cancer.PerformanceCollector;
//import com.melax.poc.inferscience.InferScienceNote;
//import com.melax.poc.inferscience.evaluation.PerformanceCollector;
//import com.melax.poc.qualitycontrol.CompareJsonFile;
import edu.uth.clamp.config.ConfigurationException;
import edu.uth.clamp.io.DocumentIOException;
import edu.uth.clamp.nlp.structure.ClampNameEntity;
import edu.uth.clamp.nlp.structure.ClampRelation;
import edu.uth.clamp.nlp.structure.ClampSection;
import edu.uth.clamp.nlp.structure.ClampSentence;
import edu.uth.clamp.nlp.structure.ClampToken;
import edu.uth.clamp.nlp.structure.Document;

import org.apache.commons.cli.*;
//import com.melax.poc.qualitycontrol.CallWebService;

public class ReleaseTest {
	Set<ClampNameEntity> goldSet = new TreeSet<ClampNameEntity>();
	Set<ClampNameEntity> predictSet = new TreeSet<ClampNameEntity>();
	Set<ClampNameEntity> goldSetCopy = new TreeSet<ClampNameEntity>();
	Set<ClampSentence> sentGoldSet = new TreeSet<ClampSentence>();
	Set<ClampSentence> sentPredictSet = new TreeSet<ClampSentence>();
	Set<ClampSentence> sentGoldSetCopy = new TreeSet<ClampSentence>();
	Set<ClampToken> tokenGoldSet = new TreeSet<ClampToken>();
	Set<ClampToken> tokenPredictSet = new TreeSet<ClampToken>();
	Set<ClampToken> tokenGoldSetCopy = new TreeSet<ClampToken>();
	Set<ClampSection> secGoldSet = new TreeSet<ClampSection>();
	Set<ClampSection> secPredictSet = new TreeSet<ClampSection>();
	Set<ClampSection> secGoldSetCopy = new TreeSet<ClampSection>();
	Set<ClampRelation> relGoldSet = new TreeSet<ClampRelation>();
	Set<ClampRelation> relPredictSet = new TreeSet<ClampRelation>();
	Set<ClampRelation> relGoldSetCopy = new TreeSet<ClampRelation>();
	
	String[] attrList = {"name",
			"sectionName",
			"text",
			"derivedGeneric",
			"polarity",
			"relTime",
			"familyMember",
			"date",
			"medDosage",
			"medForm",
			"medFrequencyNumber",
			"medFrequencyUnit",
			"medRoute",
			"medStrengthNum",
			"medStrengthUnit",
			"labUnit",
			"labValue",
			"status",
			"cui"};
	
	public void init() throws UIMAException, IOException, ConfigurationException, DocumentIOException {
		goldSet.clear();
		predictSet.clear();
		goldSetCopy.clear();	
		sentGoldSet.clear();
		sentPredictSet.clear();
		sentGoldSetCopy.clear();
		tokenGoldSet.clear();
		tokenPredictSet.clear();
		tokenGoldSetCopy.clear();
		secGoldSet.clear();
		secPredictSet.clear();
		secGoldSetCopy.clear();
		relGoldSet.clear();
		relPredictSet.clear();
		relGoldSetCopy.clear();

	}


	public void printEntitySet(Set<ClampNameEntity> set, FileWriter report) throws IOException {
		for(ClampNameEntity entity:set) {
			System.out.println("\t\t"+ entity.toJson(attrList).toString());
			report.write("\t\t"+ entity.toJson(attrList).toString() +"\n");
		}
	}
	
	
	public void printTokenSet(Set<ClampToken> set, FileWriter report) throws IOException {
		for(ClampToken entity:set) {
			System.out.println("\t\t" + entity.toJson(attrList).toString());
			report.write("\t\t" + entity.toJson(attrList).toString() +"\n");

		}
	}
	
	public void printSectionSet(Set<ClampSection> set, FileWriter report) throws IOException {
		for(ClampSection entity:set) {
			System.out.println("\t"+ entity.toJson(attrList).toString());
			report.write("\t"+ entity.toJson(attrList).toString() +"\n");

		}
	}
	
	public void printSentSet(Set<ClampSentence> set, FileWriter report) throws IOException {
		for(ClampSentence entity:set) {
			System.out.println("\t\t"+ entity.toJson(attrList).toString());
			report.write("\t\t"+ entity.toJson(attrList).toString() +"\n");

		}
	}
	
	
	public void printRelationSet(Set<ClampRelation> set, FileWriter report) throws IOException {
		for(ClampRelation entity:set) {
			System.out.println("\t\t"+ entity.getSemanticTag());
			report.write("\t\t"+ entity.getSemanticTag().toString() +"\n");
			System.out.println("\t\t\t"+ entity.getEntFrom().toJson());
			report.write("\t\t\t"+ entity.getEntFrom().toJson() +"\n");
			System.out.println("\t\t\t"+ entity.getEntTo().toJson());
			report.write("\t\t\t"+ entity.getEntTo().toJson() +"\n");

		}
	}
	
	public void compareXmiFolder( String goldLoc, String predictLoc, String reportName) throws ConfigurationException, DocumentIOException {
		FileWriter report = null;
		try {
			report = new FileWriter( reportName);

			List<File> goldList = PerformanceCollector.getFileList( new File(goldLoc) );
			List<File> predictList = PerformanceCollector.getFileList(new File(predictLoc) );
	
			for(File goldFile: goldList) {
				try	{
					Document goldDoc = new Document(goldFile);
	
					for( File predictFile : predictList ) {
						//empty treeset
						init();  
						if( predictFile.getName().equals( goldFile.getName() ) ) {
							Document predictDoc = new Document(predictFile);
							System.out.println("====================================================");
							System.out.println("Comparing... "+ goldFile.getName());
							System.out.println("====================================================");
							report.write("====================================================\n");
							report.write("Comparing... "+ goldFile.getName()+"\n");
							report.write("====================================================\n");
							
							//Entity comparation
							for( ClampNameEntity cne : goldDoc.getNameEntity() ) {
								goldSet.add(cne);
								goldSetCopy.add(cne);
								// System.out.println("Standard NameEntity"+cne.toJson().toString());
							}
							
							for( ClampNameEntity cne : predictDoc.getNameEntity() ) {
								predictSet.add(cne);
								// System.out.println("Predicted NameEntity"+cne.toJson().toString());
							}
	
							System.out.println("\tClampNameEntity Diff Begin-------------------");
							report.write("\tClampNameEntity Diff Begin-------------------\n");						
							goldSet.removeAll(predictSet);
							printEntitySet(goldSet,report);
							System.out.println("\t-------------split between gold and predict----");
							report.write("\t-------------split between gold and predict----\n");	
							predictSet.removeAll(goldSetCopy);
							printEntitySet(predictSet,report);
							System.out.println("\n\r");
							report.write("\n\r");	
							
							//Sentence comparation						
							for( ClampSentence sent : goldDoc.getSentences() ) {
								sentGoldSet.add(sent);
								sentGoldSetCopy.add(sent);
								// System.out.println("Standard NameEntity"+cne.toJson().toString());
							}
							
							for( ClampSentence sent : predictDoc.getSentences() ) {
								sentPredictSet.add(sent);
								// System.out.println("Predicted NameEntity"+cne.toJson().toString());
							}
	
							System.out.println("\tClampSentence Diff Begin-------------------");
							report.write("\tClampSentence Diff Begin-------------------\n");							
							sentGoldSet.removeAll(sentPredictSet);
							printSentSet(sentGoldSet,report);
							System.out.println("\t-------------split between gold and predict----");
							report.write("\t-------------split between gold and predict----\n");		
							sentPredictSet.removeAll(sentGoldSetCopy);
							printSentSet(sentPredictSet,report);
							System.out.println("\n\r");
							report.write("\n\r");
	
							
							//tokens comparation						
							for( ClampToken token : goldDoc.getTokens()) {
								tokenGoldSet.add(token);
								tokenGoldSetCopy.add(token);
								// System.out.println("Standard NameEntity"+cne.toJson().toString());
							}
							
							for( ClampToken token : predictDoc.getTokens() ) {
								tokenPredictSet.add(token);
								// System.out.println("Predicted NameEntity"+cne.toJson().toString());
							}
	
							System.out.println("\tClampToken Diff Begin-------------------");
							report.write("\tClampToken Diff Begin-------------------\n");						
							tokenGoldSet.removeAll(tokenPredictSet);
							printTokenSet(tokenGoldSet,report);
							System.out.println("\t-------------split between gold and predict----");
							report.write("\t-------------split between gold and predict----\n");	
							tokenPredictSet.removeAll(tokenGoldSetCopy);
							printTokenSet(tokenPredictSet,report);
							System.out.println("\n\r");
							report.write("\n\r");
	
							
							//Section comparation						
							for( ClampSection sec : goldDoc.getSections()) {
								secGoldSet.add(sec);
								secGoldSetCopy.add(sec);
								// System.out.println("Standard NameEntity"+cne.toJson().toString());
							}
							
							for( ClampSection sec : predictDoc.getSections() ) {
								secPredictSet.add(sec);
								// System.out.println("Predicted NameEntity"+cne.toJson().toString());
							}
	
							System.out.println("\tClampSection Diff Begin-------------------");
							report.write("\tClampSection Diff Begin-------------------\n");					
							secGoldSet.removeAll(secPredictSet);
							printSectionSet(secGoldSet,report);
							System.out.println("\t-------------split between gold and predict----");
							report.write("\t-------------split between gold and predict----\n");	
							secPredictSet.removeAll(secGoldSetCopy);
							printSectionSet(secPredictSet,report);
							System.out.println("\n\r");
							report.write("\n\r");
							
							//Relation comparation						
							for( ClampRelation rel : goldDoc.getRelations()) {
								relGoldSet.add(rel);
								relGoldSetCopy.add(rel);
	//							 System.out.println("Standard ClampRelation"+rel.getKey()+toString()+"  size: "+relGoldSet.size());
							}
							
							for( ClampRelation rel : predictDoc.getRelations() ) {
								relPredictSet.add(rel);
	//							System.out.println("Predicted ClampRelation"+rel.getKey().toString());
							}
	
							System.out.println("\tClampRelation Diff Begin-------------------");
							report.write("\tClampRelation Diff Begin-------------------\n");				
							relGoldSet.removeAll(relPredictSet);
							printRelationSet(relGoldSet,report);
							System.out.println("\t-------------split between gold and predict----");
							report.write("\t-------------split between gold and predict----\n");		
							relPredictSet.removeAll(relGoldSetCopy);
							printRelationSet(relPredictSet,report);
							System.out.println("\n\r");
							report.write("\n\r");
							
							break;
						}
					}
				}catch(com.sun.org.apache.xerces.internal.impl.io.MalformedByteSequenceException e) {
					System.out.println("Exception :"+e.getClass().getName()+" from file "+ goldFile.getName()+"\n");
					report.write("Exception :"+e.getClass().getSimpleName()+" from file "+ goldFile.getName()+"\n");
				}
			}
		
		} catch (UIMAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				report.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	
	static String pipelineLoc = "pipeline/Inferscience_problem_pipeline/Components/Inferscience_problem_pipeline.pipeline";
	static String inLoc = "";//"test_corpus/release/in/";
	static String outLoc = "";//"test_corpus/release/out/";
	static String goldLoc = "";//"test_corpus/release/gold/";
	static String repLoc = "";//"test_corpus/release/diff/";
	static String remoteURL = "";
	
	
   public int post(String urlStr, File file, String output,String ext) throws IOException {
        System.out.println("post！！"+file.getName());
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);    // 可以发送数据
        conn.setDoInput(true);    // 可以接收数据
        conn.setRequestMethod("POST");    // POST方法
        // 必须注意此处需要设置UserAgent，否则google会返回403
        conn.setRequestProperty
                ("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.connect();

        //    写入的POST数据
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
        String content = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        osw.write("query="+ URLEncoder.encode(content, "UTF-8"));
        osw.flush();
        osw.close();
        int code = conn.getResponseCode();
        if( code != 200 ) {
        	// error from the server side;
        	conn.disconnect();
        	return code;
        }
        // 读取响应数据

//        BufferedReader in = new BufferedReader( new InputStreamReader(conn.getInputStream()));
//        String s;
//        StringBuilder sb=new StringBuilder();
//        FileWriter fileWriter=new FileWriter(output + file.getName().replace(".txt",ext));
//        while ((s = in.readLine()) != null){
//            sb.append(s);
//            //或者这里直接写入到文本中
//            fileWriter.write(s);
//        }
//        fileWriter.close();

        String path = output + file.getName().replace(".txt",ext);
        File outputfile = new File(path);
        byte[] buffer = new byte[conn.getContentLength()];
        int bytesRead = conn.getInputStream().read(buffer);
        String contentStr = new String(buffer, 0, bytesRead, "UTF-8");
        
        FileUtils.writeStringToFile(outputfile, contentStr);
        conn.disconnect();
        return code;
    }
	
	public static void main(String[] args) throws JSONException, IOException, UIMAException, DocumentIOException, ConfigurationException, InterruptedException {

		ReleaseTest instance = new ReleaseTest();
		instance.init();
		
		Options options = new Options();
        
        Option operation = new Option("a", "action", true, "which action will be executed?Parse note or Compare xmi?");
        operation.setRequired(true);
        options.addOption(operation);    
        
        Option input = new Option("i", "input", true, "Parse's input folder  or Compare's source folder");
        input.setRequired(true);
        options.addOption(input);

        Option predict = new Option("o", "output", true, "Parse output folder or Compare's target folder");
        predict.setRequired(true);
        options.addOption(predict);
        
        Option report = new Option("r", "report", true, "Report folder");
        options.addOption(report);
        
        Option serverurl = Option.builder("url").longOpt("server url").desc("Server url").hasArg().argName("url").build();
        options.addOption(serverurl);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, args);
            String actionStr = cmd.getOptionValue("action");
            if(actionStr != null && actionStr.toLowerCase().equals("parse")) {

             	inLoc = cmd.getOptionValue("input");
            	outLoc = cmd.getOptionValue("output");
            	remoteURL = cmd.getOptionValue("url");
        		System.out.println("Release Testing Start,connecting..." + remoteURL);
        		
        		try {
	        		
	        		//call the remote service to produce the gold standard
	        		CallWebService service = new CallWebService();
	        		File indir = new File( inLoc );
	        		for( File file : indir.listFiles() ) {
	                    int code = instance.post(remoteURL, file, outLoc,".xmi");
	                    System.out.println("server feedback code: "+code );
	                    TimeUnit.MILLISECONDS.sleep(5000L);
	        		}
        		}catch(java.net.ConnectException ex) {
        			System.out.println("connection failed, check the remote service is running and the setting parameter!");
        		}
            }
            
            if(actionStr != null && actionStr.toLowerCase().equals("compare")) {
             	inLoc = cmd.getOptionValue("input");
            	outLoc = cmd.getOptionValue("output");
             	repLoc = cmd.getOptionValue("report");
            	instance.compareXmiFolder(inLoc,outLoc,repLoc+"\\report.txt");
            }
            
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }


	}
}
