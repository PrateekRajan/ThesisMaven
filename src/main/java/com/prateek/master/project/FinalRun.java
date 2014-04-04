package com.prateek.master.project;

import java.util.ArrayList;
import java.util.List;

import com.prateek.master.project.FeatureExtraction;
import com.prateek.master.project.HelperMethods;
import com.prateek.master.project.SharedLists;
import edu.stanford.nlp.trees.TypedDependency;

public class FinalRun {
    /**
     * @param args
     */
    public static void main(String[] args) {
	List<List<TypedDependency>> tdl = HelperMethods.getDependency();
	System.out.println("I am tdl " + tdl.toString());
	FeatureExtraction object = new FeatureExtraction();
	// System.out.println("I am structure " + HelperMethods.getStructure());
	ArrayList<String> structure = HelperMethods.getStructure();

	for (int i = 0; i < tdl.size(); i++) {
	    object.generateFeatures("camera", tdl.get(i), structure.get(i));
	}
	System.out.println("Features " + SharedLists.features.toString());
	System.out.println("Opinions " + SharedLists.opinions.toString());
	System.out.println("Parents " + SharedLists.parents.toString());
	OpinionExtraction obj = new OpinionExtraction();
	obj.generateOpinions("camera", tdl, structure);

	System.out.println(SharedLists.hierarchy.toString());
    }

}
