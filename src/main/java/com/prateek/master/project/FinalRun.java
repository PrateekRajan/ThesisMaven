package com.prateek.master.project;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.prateek.master.project.FeatureExtraction;
import com.prateek.master.project.HelperMethods;
import com.prateek.master.project.SharedLists;
import edu.stanford.nlp.trees.TypedDependency;

public class FinalRun {
    private static Logger LOGGER = Logger.getLogger(FinalRun.class);

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {

	    LOGGER.info("Final Run class ran successfully " + new java.util.Date());
	    List<List<TypedDependency>> tdl = HelperMethods.getDependency();
	    System.out.println("I am tdl " + tdl.toString());
	    FeatureExtraction object = new FeatureExtraction();
	    ArrayList<String> structure = HelperMethods.getStructure();
	    // System.out.println("TDL size " + tdl.size() + " structure size "
	    // + structure.size());
	    // System.out.println("I am structu " + structure);
	    for (int i = 0; i < tdl.size(); i++) {
		object.generateFeatures("camera", tdl.get(i), structure.get(i));
	    }
	    System.out.println("Features " + SharedLists.features.toString());
	    // System.out.println("Opinions " +
	    // SharedLists.opinions.toString());
	    // System.out.println("Parents " + SharedLists.parents.toString());
	    OpinionExtraction obj = new OpinionExtraction();
	    obj.generateOpinions("camera", tdl, structure);

	    System.out.println(SharedLists.hierarchy.toString());
	} catch (Exception ex) {
	    LOGGER.error("Something went wrong in Final Run class " + ex.toString() + " " + new java.util.Date());
	}
    }

}
