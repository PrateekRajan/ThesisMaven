package com.prateek.master.project;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.prateek.master.project.HelperMethods;
import com.prateek.master.project.GettingNouns;
import edu.stanford.nlp.trees.TypedDependency;

public class HelperMethods {

    private static Logger LOGGER = Logger.getLogger(HelperMethods.class);

    static ArrayList<String> getStructure() {
	// StringBuilder structure = new StringBuilder("");
	ArrayList<String> structure = new ArrayList<String>();
	try {
	    FileInputStream fstream = new FileInputStream("src\\main\\resources\\data\\test\\1.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = "";
	    String review = "";

	    while ((strline = br.readLine()) != null) {
		review = review + strline;
	    }
	    in.close();
	    String[] reviewSentences = review.split("\\.\\/\\.");
	    for (String sentence : reviewSentences) {
		if (HelperMethods.isPresentAdjective(sentence) && HelperMethods.isPresentFeature(sentence)) {
		    structure.add(sentence);
		}
	    }
	} catch (Throwable ex) {
	    LOGGER.error("Error reading file " + ex.toString() + new java.util.Date());
	}
	LOGGER.info("getStructure method ran successfully on " + new java.util.Date());
	return structure;
    }

    static boolean isPresentAdjective(String input) {
	try {
	    for (String i : GettingNouns.adjective) {
		if (input.toLowerCase().contains(i)) {
		    return true;
		}
	    }
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong in isPresentAdjective" + ex.toString());
	}
	LOGGER.info("isPresentAdjective method executed successfully " + new java.util.Date());
	return false;
    }

    static boolean isPresentNouns(String input) {
	try {
	    for (String i : GettingNouns.nouns) {
		if (input.contains(i)) {
		    return true;
		}
	    }
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong in isPresentNouns" + ex.toString());
	}
	LOGGER.info("isPresentNouns method executed successfully " + new java.util.Date());
	return false;
    }

    static List<List<TypedDependency>> getDependency() {
	List<List<TypedDependency>> tdl = GettingNouns.read();
	return tdl;
    }

    static boolean isComponent(String input) {
	boolean isComponent = false;
	try {

	    FileInputStream fstream = new FileInputStream("src\\main\\resources\\data\\test\\ComponentDatabase.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = null;
	    while ((strline = br.readLine()) != null) {
		if (strline.equals(input)) {
		    isComponent = true;
		    break;
		}
	    }
	    br.close();
	    LOGGER.info("isComponenet executed successfully " + new java.util.Date());
	} catch (Throwable ex) {
	    LOGGER.error("Something went wrong while reading component database");
	}
	return isComponent;
    }

    static boolean isFeature(String input) {
	boolean isFeature = false;
	try {
	    FileInputStream fstream = new FileInputStream("src\\main\\resources\\data\\test\\FeatureDatabase.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = null;
	    while ((strline = br.readLine()) != null) {
		if (strline.equals(input)) {
		    isFeature = true;
		    break;
		}
	    }
	    br.close();
	    LOGGER.info("isFeature executed successfully " + new java.util.Date());
	} catch (Throwable ex) {
	    LOGGER.error("Something went wrong while reading Feature database");
	}
	return isFeature;
    }

    static String isNeg(String dependencyString) {
	StringBuffer output = new StringBuffer();
	String[] arr = dependencyString.split(" ");
	for (int i = 0; i < arr.length; i++) {
	    if (arr[i].equals("neg")) {
		output.append(arr[i + 1]);// Check if we need to add governor or
					  // dependent here
	    }
	}
	return output.toString();
    }

    static String getComposite(List<TypedDependency> tdl) {
	System.out.println(GettingNouns.sentence);
	StringBuffer output = new StringBuffer();
	String temp = "";
	int distance = 100;
	String dependencyString = tdl.toString().replace(",", "").replace("(", " ").replace(")", " ").replace("-", " ");
	String[] arr = dependencyString.split(" ");
	for (int i = 0; i < arr.length; i++) {
	    if (arr[i].equals("nn") || arr[i].equals("conj_and")) {
		for (int j = 0; j < arr.length; j++) {
		    if (arr[j].equals("nsubj") && arr[i + 1].equals(arr[j + 3])) {
			if (Math.abs(i - j) < distance) {
			    distance = Math.abs(i - j);
			    System.out.println(distance);
			    temp = " ";
			    temp = arr[i + 3] + " " + arr[i + 1];
			}
		    }
		}
	    }
	}
	output.append(temp);
	return output.toString();
    }

    static String getComposite1(List<TypedDependency> tdl) {
	StringBuffer output = new StringBuffer();
	String dependencyString = tdl.toString().replace(",", "").replace("(", " ").replace(")", " ").replace("-", " ").replace("[", "");
	String[] arr = dependencyString.split(" ");
	for (int i = 0; i < arr.length; i++) {
	    if ((arr[i].trim().equals("nn") && isFeature(arr[i + 1]) && !isFeature(arr[i + 3]))
		    || (arr[i].trim().equals("nn") && isFeature(arr[i + 3]) && !isFeature(arr[i + 1]))) {
		if (Math.abs(GettingNouns.sentence.indexOf(arr[i + 1]) - GettingNouns.sentence.indexOf(arr[i + 3])) <= arr[i + 1].length() + 1) {
		    output.append(arr[i + 3]).append(" ").append(arr[i + 1]);
		}
	    }
	}
	return output.toString();
    }

    static boolean isPresentFeature(String input) {
	boolean isPresentFeature = false;
	try {

	    FileInputStream fstream = new FileInputStream("src\\main\\resources\\data\\test\\FeatureDatabase.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = null;
	    while ((strline = br.readLine()) != null) {
		if (input.contains(strline)) {
		    isPresentFeature = true;
		    break;
		}
	    }
	    br.close();
	    LOGGER.info("isFeature executed successfully " + new java.util.Date());
	} catch (Throwable ex) {
	    LOGGER.error("Something went wrong while reading Feature database");
	}
	return isPresentFeature;
    }

    static boolean isAdjective(String input) {
	try {
	    for (String i : GettingNouns.adjective) {
		if (input.equalsIgnoreCase(i)) {
		    return true;
		}
	    }
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong in isAdjective" + ex.toString());
	}
	LOGGER.info("isAdjective method executed successfully " + new java.util.Date());
	return false;
    }

    static boolean isNoun(String input) {
	try {
	    for (String i : GettingNouns.nouns) {
		if (input.equalsIgnoreCase(i)) {
		    return true;
		}
	    }
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong in isNoun" + ex.toString());
	}
	LOGGER.info("isNoun method executed successfully " + new java.util.Date());
	return false;
    }

    static String nearestAdjective(String structure, String feature) {
	String adjective = "";
	int feature_index = 0;
	int difference = Integer.MAX_VALUE;
	String structureArr[] = structure.split(" ");
	for (int i = 0; i < structureArr.length; i++) {
	    if (structureArr[i].contains(feature)) {
		feature_index = i;
		break;
	    }
	}
	for (int i = 0; i < structureArr.length; i++) {
	    if (structureArr[i].endsWith("/JJ") && Math.abs(feature_index - i) <= difference) {
		difference = Math.abs(feature_index - i);
		adjective = structureArr[i].substring(0, structureArr[i].indexOf("/"));
	    }
	}
	return adjective;
    }

    static boolean isPositive(String word) {
	if (word == null || word.isEmpty()) {
	    return false;
	}
	boolean isPositive = false;
	FileInputStream fstream;
	try {
	    fstream = new FileInputStream("src\\main\\resources\\data\\test\\positive-words.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = null;
	    while ((strline = br.readLine()) != null) {
		if (strline.equalsIgnoreCase(word)) {
		    isPositive = true;
		    break;
		}
	    }
	    br.close();
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return isPositive;
    }

    static boolean isNegative(String word) {
	if (word == null || word.isEmpty()) {
	    return false;
	}
	boolean isNegative = false;
	FileInputStream fstream;
	try {
	    fstream = new FileInputStream("src\\main\\resources\\data\\test\\negative-words.txt");
	    DataInputStream in = new DataInputStream(fstream);
	    BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    String strline = null;
	    while ((strline = br.readLine()) != null) {
		if (strline.equalsIgnoreCase(word)) {
		    isNegative = true;
		    break;
		}
	    }
	    br.close();
	} catch (FileNotFoundException ex) {
	    LOGGER.error("Something went wrong in isNegative method " + ex.toString() + " " + new java.util.Date());
	} catch (IOException ex) {
	    LOGGER.error("Something went wrong in isNegative method " + ex.toString() + " " + new java.util.Date());
	}
	LOGGER.info("isNegative method executed successfully " + new java.util.Date());
	return isNegative;
    }

    static void addToMap(String feature, String opinion, boolean isNegative) {
	System.out.println(feature + " " + opinion);

	if (isNegative && isPositive(opinion)) {
	    opinion = "bad";
	} else if (isNegative && isNegative(opinion)) {
	    opinion = "good";
	}
	if (!SharedLists.hierarchy.containsKey(feature)) {
	    SharedLists.hierarchy.put(feature, new HashMap<String, Integer>());
	    if (HelperMethods.isPositive(opinion)) {
		SharedLists.hierarchy.get(feature).put("Liked", 1);
		SharedLists.hierarchy.get(feature).put("Disliked", 0);
	    } else if (HelperMethods.isNegative(opinion)) {
		SharedLists.hierarchy.get(feature).put("Disliked", 1);
		SharedLists.hierarchy.get(feature).put("Liked", 0);
	    } else {
		SharedLists.hierarchy.get(feature).put("Disliked", 0);
		SharedLists.hierarchy.get(feature).put("Liked", 0);
	    }
	} else if (SharedLists.hierarchy.containsKey(feature)) {
	    if (HelperMethods.isPositive(opinion)) {
		int value = SharedLists.hierarchy.get(feature).get("Liked");
		SharedLists.hierarchy.get(feature).put("Liked", value + 1);
	    } else if (HelperMethods.isNegative(opinion)) {
		int value = SharedLists.hierarchy.get(feature).get("Disliked");
		SharedLists.hierarchy.get(feature).put("Disliked", value + 1);
	    }
	}
    }
}
