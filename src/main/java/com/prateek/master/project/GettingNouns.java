package com.prateek.master.project;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.CoreLabelTokenFactory;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.GrammaticalStructureFactory;
import edu.stanford.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreebankLanguagePack;
import edu.stanford.nlp.trees.TypedDependency;

public class GettingNouns {
    private static Logger LOGGER = Logger.getLogger(GettingNouns.class);

    static String[] temp = new String[100];
    static ArrayList<String> nouns = new ArrayList<String>();
    static ArrayList<String> adjective = new ArrayList<String>();

    static String sentence = "";
    static List<List<TypedDependency>> gdependency = new LinkedList<List<TypedDependency>>();

    public static List<List<TypedDependency>> read() {
	try {
	    int l = 0;

	    FileInputStream fi = new FileInputStream("src\\main\\resources\\data\\test\\1.txt");// You
	    // can
	    // change
	    // the
	    // path
	    // according
	    // to
	    // your
	    // location
	    // of
	    // the
	    // 1.
	    // txt
	    // file
	    DataInputStream ds = new DataInputStream(fi);
	    BufferedReader br = new BufferedReader(new InputStreamReader(ds));
	    String str;

	    while ((str = br.readLine()) != null) {
		temp = str.split(" ");
		for (int i = 0; i <= temp.length - 1; i++) {
		    if (temp[i].endsWith("NN") || temp[i].endsWith("NNS")) {
			nouns.add(temp[i].substring(0, temp[i].indexOf("/")));
		    }
		    if (temp[i].endsWith("JJ") || temp[i].endsWith("VBP")) {
			adjective.add(temp[i].substring(0, temp[i].indexOf("/")));
			l++;
		    }

		}
	    }
	    System.out.println("The Nouns in the sentence are :");
	    for (int k = 0; k < nouns.size(); k++) {
		System.out.println(nouns.get(k));// Displaying nouns

	    }
	    System.out.println("The Adjectives and verbs in the sentence are :");
	    for (l = 0; l < adjective.size(); l++) {
		System.out.println(adjective.get(l));// Displaying
						     // adjectives.
	    }
	    br.close();
	} catch (Throwable ex) {
	    LOGGER.error("Something went wrong while extracting nouns and adjectives " + ex.toString() + new java.util.Date());
	}
	LOGGER.info("Nouns and adjectives extracted successfully " + new java.util.Date());
	gdependency = prune(nouns, adjective);
	return gdependency;
    }

    private static List<List<TypedDependency>> prune(ArrayList<String> nouns, ArrayList<String> adjective) {

	LexicalizedParser lp = LexicalizedParser.loadModel("edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz");
	List<TypedDependency> tdl = null;
	String str = null;
	try {
	    FileInputStream fi = new FileInputStream("src\\main\\resources\\data\\test\\test.txt");
	    DataInputStream di = new DataInputStream(fi);
	    BufferedReader br = new BufferedReader(new InputStreamReader(di));
	    while ((str = br.readLine()) != null) {
		sentence = sentence + str;
	    }
	    br.close();

	    String lines[] = sentence.toLowerCase().split("\\.");
	    for (String line : lines) {
		if (HelperMethods.isPresentFeature(line) && HelperMethods.isPresentAdjective(line)) {
		    TokenizerFactory<CoreLabel> tokenizerFactory = PTBTokenizer.factory(new CoreLabelTokenFactory(), "");
		    List<CoreLabel> rawWords2 = tokenizerFactory.getTokenizer(new StringReader(line)).tokenize();
		    // System.out.println("I am raw words " + rawWords2);
		    Tree parse = lp.apply(rawWords2);
		    TreebankLanguagePack tlp = new PennTreebankLanguagePack();
		    GrammaticalStructureFactory gsf = tlp.grammaticalStructureFactory();
		    GrammaticalStructure gs = gsf.newGrammaticalStructure(parse);
		    tdl = (List<TypedDependency>) gs.typedDependenciesCCprocessed();
		    gdependency.add(tdl);
		}
	    }
	} catch (Throwable ex) {
	    LOGGER.error("Something went wrong while pruning the sentences -->" + ex.toString() + new java.util.Date());
	}
	LOGGER.info("Sentence pruning done successfully -->" + new java.util.Date());
	return gdependency;
    }
}
