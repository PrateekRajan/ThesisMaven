package com.prateek.master.project;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import edu.stanford.nlp.trees.TypedDependency;

public class OpinionExtraction {
    private static Logger LOGGER = Logger.getLogger(OpinionExtraction.class);

    public void generateOpinions(String parent, List<List<TypedDependency>> tdl, ArrayList<String> structure) {
	try {
	    String dependencyString = "";
	    boolean extracted = false;
	    List<String> TDL = new ArrayList<String>();
	    for (List<TypedDependency> item : tdl) {
		dependencyString = item.toString().replace(",", "").replace("(", " ").replace(")", " ").replace("-", " ");
		TDL.add(dependencyString);
	    }
	    for (String feature : SharedLists.features) {
		int counter = 0;
		while (counter < TDL.size()) {
		    String arr[] = TDL.get(counter).split(" ");
		    for (int i = 0; i < arr.length; i++) {
			// System.out.println(feature + " " + arr[i] + " " +
			// arr[i + 1] + " " + arr[i + 3]);
			if (arr[i].equals("nsubj") && feature.contains(arr[i + 3]) && HelperMethods.isAdjective(arr[i + 1])) {
			    if (HelperMethods.isNeg(TDL.get(counter)).contains(arr[i + 1])) {
				HelperMethods.addToMap(feature, arr[i + 1], true);
				extracted = true;
			    } else {
				HelperMethods.addToMap(feature, arr[i + 1], false);
				extracted = true;
			    }
			}
			if (arr[i].contains("amod") && feature.contains(arr[i + 1]) && HelperMethods.isAdjective(arr[i + 3])) {
			    if (HelperMethods.isNeg(TDL.get(counter)).contains(arr[i + 3])) {
				HelperMethods.addToMap(feature, arr[i + 3], true);
				extracted = true;
			    } else {
				HelperMethods.addToMap(feature, arr[i + 3], false);
				extracted = true;
			    }
			}

			if (arr[i].equals("nsubj") && feature.contains(arr[i + 3]) && !HelperMethods.isAdjective(arr[i + 1])) {
			    for (int j = 0; j < arr.length; j++) {
				if (arr[j].equals("advmod") && arr[j + 1].equals(arr[i + 1]) && HelperMethods.isAdjective(arr[j + 3])) {
				    if (HelperMethods.isNeg(TDL.get(counter)).contains(arr[j + 3])) {
					HelperMethods.addToMap(feature, arr[j + 3], true);
					extracted = true;
				    } else {
					HelperMethods.addToMap(feature, arr[j + 3], false);
					extracted = true;
				    }
				}
			    }
			}
		    }
		    if (structure.get(counter).contains(feature) && extracted == false) {
			HelperMethods.addToMap(feature, HelperMethods.nearestAdjective(structure.get(counter), feature), false);
		    }
		    counter++;
		    extracted = false;
		}
	    }
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong while extracting Opinions " + ex.toString() + " " + new java.util.Date());
	}
	LOGGER.info("Opinion Extraction completed successfully on " + new java.util.Date());
    }
}
