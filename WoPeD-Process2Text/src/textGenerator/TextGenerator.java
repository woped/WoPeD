package textGenerator;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.validator.util.privilegedactions.GetConstructor;

import preprocessing.FormatConverter;
import preprocessing.RigidStructurer;
import sentencePlanning.DiscourseMarker;
import sentencePlanning.ReferringExpressionGenerator;
import sentencePlanning.SentenceAggregator;
import sentenceRealization.SurfaceRealizer;
import textPlanning.PlanningHelper;
import textPlanning.TextPlanner;
import contentDetermination.labelAnalysis.EnglishLabelDeriver;
import contentDetermination.labelAnalysis.EnglishLabelHelper;
import dataModel.dsynt.DSynTSentence;
import dataModel.pnmlReader.PNMLReader;
import dataModel.pnmlReader.PetriNetToProcessConverter;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.process.ProcessModel;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Node;

public class TextGenerator {
	
	private String contextPath = "";
	
	public TextGenerator(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public TextGenerator() {
	}
	
	public String getContextPath() {
		return this.contextPath;
	}
	
	public String toText(String input) throws Exception {
		
			String imperativeRole = ""; 
			boolean imperative = false;	
			
			File inputFile = new File(input); 
			PNMLReader pnmlReader = new PNMLReader();
			PetriNet petriNet = pnmlReader.getPetriNetFromPNML(inputFile);
			PetriNetToProcessConverter pnConverter = new PetriNetToProcessConverter();
			ProcessModel model = pnConverter.convertToProcess(petriNet);
	
			HashMap<Integer, String> transformedElemsRev = pnConverter.transformedElemsRev;
			
			EnglishLabelHelper lHelper = new EnglishLabelHelper(contextPath);
			EnglishLabelDeriver lDeriver  = new EnglishLabelDeriver(lHelper);
			
			// Annotate model
			model.annotateModel(0,lDeriver,lHelper);
			
			// Convert to RPST
			FormatConverter formatConverter = new FormatConverter();
			de.hpi.bpt.process.Process p = formatConverter.transformToRPSTFormat(model);
			RPST<ControlFlow,Node> rpst = new RPST<ControlFlow,Node>(p);
			
			// Check for Rigids
			boolean containsRigids = PlanningHelper.containsRigid(rpst.getRoot(), 1, rpst);
			
			// Structure Rigid and convert back
			if (containsRigids) {
				p =  formatConverter.transformToRigidFormat(model);
				RigidStructurer rigidStructurer = new RigidStructurer();
				p = rigidStructurer.structureProcess(p);
				model = formatConverter.transformFromRigidFormat(p);
				p = formatConverter.transformToRPSTFormat(model);
				rpst = new RPST<ControlFlow, Node>(p);
			}
			
			// Convert to Text
			TextPlanner converter = new TextPlanner(rpst, model, lDeriver, lHelper, imperativeRole, imperative, false);
			converter.convertToText(rpst.getRoot(), 0);
			ArrayList <DSynTSentence> sentencePlan = converter.getSentencePlan();
			
			// Aggregation
			SentenceAggregator sentenceAggregator = new SentenceAggregator(lHelper);
			sentencePlan = sentenceAggregator.performRoleAggregation(sentencePlan, model);
			
			// Referring Expression
			ReferringExpressionGenerator refExpGenerator = new ReferringExpressionGenerator(lHelper);
			sentencePlan  = refExpGenerator.insertReferringExpressions(sentencePlan, model, false);
			
			// Discourse Marker 
			DiscourseMarker discourseMarker = new DiscourseMarker();
			sentencePlan = discourseMarker.insertSequenceConnectives(sentencePlan);
	
			// Realization
			SurfaceRealizer surfaceRealizer = new SurfaceRealizer();
			String surfaceText = surfaceRealizer.realizeSentenceMap(sentencePlan, transformedElemsRev);
			
			// Cleaning
			if (imperative == true) {
				surfaceText = surfaceRealizer.cleanTextForImperativeStyle(surfaceText, imperativeRole, model.getLanes());
			}			
			surfaceText = surfaceRealizer.postProcessText(surfaceText);
			String newFile = appendTextToFile(input, surfaceText);
//			System.out.println(newFile);
			return newFile;
	}
	
	private String appendTextToFile(String file, String text) {
		String newFile = "";
		try {	
			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				if (!strLine.equals("</pnml>")) {
					newFile = newFile + strLine + "\n";
				} else {
					newFile = newFile + text + "\n</pnml>"; 
				}
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFile;
	}

}
