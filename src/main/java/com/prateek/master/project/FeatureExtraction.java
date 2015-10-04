package com.prateek.master.project;

import java.util.List;
import org.apache.log4j.Logger;
import com.prateek.master.project.HelperMethods;
import com.prateek.master.project.SharedLists;
import edu.stanford.nlp.trees.TypedDependency;

public class FeatureExtraction {
    private static Logger LOGGER = Logger.getLogger(FeatureExtraction.class);

    void generateFeatures(String parent, List<TypedDependency> tdl, String structure) {
	try {
	    // System.out.println("Dependency String " + tdl.toString());
	    String compositeFeature = HelperMethods.getComposite1(tdl);
	    // System.out.println("I am from composite Feautes " +
	    // compositeFeature);
	    String dependencyString = tdl.toString().replace(",", "").replace("(", " ").replace(")", " ").replace("-", " ");
	    String[] arr = dependencyString.split(" ");

	    for (int first_element = 0; first_element < arr.length; first_element++) {
		if (arr[first_element].equals("nsubj")) {
		    if (HelperMethods.isAdjective(arr[first_element + 1]) && HelperMethods.isNoun(arr[first_element + 3])) {
			SharedLists.features.add(arr[first_element + 3]);
		    }
		    for (int second_element = 0; second_element < arr.length; second_element++) {
			if (arr[second_element].equals("dobj")) {
			    if (compositeFeature.isEmpty()) {
				if (HelperMethods.isNoun(arr[second_element + 3])) {
				    // System.out.println("I am from nsubj " +
				    // arr[first_element + 3]);
				    SharedLists.features.add(arr[first_element + 3]);
				} else if (HelperMethods.isNoun(arr[first_element + 3])) {
				    // System.out.println("I am from nsubj " +
				    // arr[first_element + 3]);
				    SharedLists.features.add(arr[second_element + 3]);
				}
			    } else {
				if (compositeFeature.contains(arr[second_element + 3])) {
				    SharedLists.features.add(arr[first_element + 3]);
				} else if (compositeFeature.contains(arr[first_element + 3])) {
				    SharedLists.features.add(arr[second_element + 3]);
				}
			    }
			}
		    }
		}

		if (arr[first_element].equals("poss")) {
		    if (arr[first_element + 3].equals(parent)) {
			if (compositeFeature.contains(arr[first_element + 1].toLowerCase())) {
			    SharedLists.features.add(compositeFeature);
			    SharedLists.parents.add(arr[first_element + 3].toLowerCase());
			} else {
			    // System.out.println("I am from poss");
			    SharedLists.features.add(arr[first_element + 1].toLowerCase());
			    SharedLists.parents.add(arr[first_element + 3].toLowerCase());
			}
		    }
		}

		if (arr[first_element].equals("nn")) {
		    if (HelperMethods.isNoun(arr[first_element + 1]) && HelperMethods.isNoun(arr[first_element + 3])) {
			if (compositeFeature.contains(arr[first_element + 1].toLowerCase())) {
			    // System.out.println("I am from nn " +
			    // compositeFeature);
			    SharedLists.features.add(compositeFeature);
			} else {
			    // System.out.println("I am from nn " +
			    // arr[first_element + 1]);
			    SharedLists.features.add(arr[first_element + 1].toLowerCase());
			}
			if (compositeFeature.contains(arr[first_element + 3].toLowerCase())) {
			    // System.out.println("I am from nn " +
			    // compositeFeature);
			    SharedLists.features.add(compositeFeature);
			} else {
			    // System.out.println("I am from nn " +
			    // arr[first_element + 3]);
			    SharedLists.features.add(arr[first_element + 3].toLowerCase());
			}
		    }
		}

		if (arr[first_element].equals("amod")) {
		    if (HelperMethods.isNoun(arr[first_element + 1].toLowerCase())) {
			SharedLists.features.add(arr[first_element + 1].toLowerCase());
		    }
		}
	    }
	    generateFeatures1(parent, tdl, structure, compositeFeature);
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong while extracting features " + ex.toString() + " " + new java.util.Date());
	}
	LOGGER.info("Feature Extraction completed successfully on " + new java.util.Date());
    }

    private static void generateFeatures1(String parent, List<TypedDependency> tdl, String structure, String compositeFeature) {
	String[] structureWords = structure.split(" ");
	for (int i = 0; i < structureWords.length - 3; i++) {
	    if ((structureWords[i].endsWith("NN") && structureWords[i + 1].endsWith("IN") && structureWords[i + 2].endsWith("DT") && structureWords[i + 3]
		    .endsWith("NN")) || (structureWords[i].endsWith("NN") && structureWords[i + 1].endsWith("IN") && structureWords[i + 2].endsWith("NN"))) {
		if (structureWords[i + 3].contains(parent) || structureWords[i + 2].contains(parent)) {
		    SharedLists.features.add(structureWords[i].substring(0, structureWords[i].indexOf('/')));
		} else if (structureWords[i].contains(parent)) {
		    if (structureWords[i + 2].endsWith("IN")) {
			SharedLists.features.add(structureWords[i + 3]);
		    } else {
			SharedLists.features.add(structureWords[i + 2]);
		    }
		}
	    }
	    if (structureWords[i].endsWith("JJ") && i < structureWords.length - 1) {
		if (structureWords[i + 1].endsWith("NN")) {
		    SharedLists.features.add(structureWords[i + 1].substring(0, structureWords[i + 1].indexOf('/')));
		}
	    }
	}
	finalFeatureExtraction(parent, structure, compositeFeature);
    }

    private static void finalFeatureExtraction(String parent, String structure, String compositeFeature) {
	String tempStruct[] = structure.split(" ");
	for (String word : tempStruct) {
	    if (!word.isEmpty()) {
		if (HelperMethods.isFeature(word.trim().substring(0, word.indexOf("/")))
			&& !compositeFeature.contains(word.trim().substring(0, word.indexOf("/")))) {
		    SharedLists.features.add(word.substring(0, word.indexOf("/")));
		}
	    }
	}
    }
}
