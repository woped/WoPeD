package textGenerator;

import contentDetermination.labelAnalysis.EnglishLabelDeriver;
import contentDetermination.labelAnalysis.EnglishLabelHelper;
import dataModel.dsynt.DSynTSentence;
import dataModel.pnmlReader.PNMLReader;
import dataModel.pnmlReader.PetriNet.PetriNet;
import dataModel.pnmlReader.PetriNetToProcessConverter;
import dataModel.process.ProcessModel;
import de.hpi.bpt.graph.algo.rpst.RPST;
import de.hpi.bpt.process.ControlFlow;
import de.hpi.bpt.process.Node;
import preprocessing.FormatConverter;
import preprocessing.RigidStructurer;
import sentencePlanning.DiscourseMarker;
import sentencePlanning.ReferringExpressionGenerator;
import sentencePlanning.SentenceAggregator;
import sentenceRealization.SurfaceRealizer;
import textPlanning.PlanningHelper;
import textPlanning.TextPlanner;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TextGenerator {
    private String contextPath = "";

    public TextGenerator(String contextPath) {
        this.contextPath = contextPath;
    }

    public TextGenerator() {
    }

    public String toText(String input) throws Exception {
        return toText(input, false);
    }

    public String toText(String input, boolean surfaceOnly) throws Exception {
        String imperativeRole = "";
        File inputFile = new File(input);
        PNMLReader pnmlReader = new PNMLReader();
        PetriNet petriNet = pnmlReader.getPetriNetFromPNML(inputFile);
        PetriNetToProcessConverter pnConverter = new PetriNetToProcessConverter();
        ProcessModel model = pnConverter.convertToProcess(petriNet);

        HashMap<Integer, String> transformedElemsRev = pnConverter.transformedElemsRev;

        EnglishLabelHelper lHelper = new EnglishLabelHelper(contextPath);
        EnglishLabelDeriver lDeriver = new EnglishLabelDeriver(lHelper);

        // Annotate model
        model.annotateModel(lDeriver, lHelper);

        // Convert to RPST
        FormatConverter formatConverter = new FormatConverter();
        de.hpi.bpt.process.Process p = formatConverter.transformToRPSTFormat(model);
        RPST<ControlFlow, Node> rpst = new RPST<>(p);

        // Check for Rigids
        boolean containsRigids = PlanningHelper.containsRigid(rpst.getRoot(), rpst);

        // Structure Rigid and convert back
        if (containsRigids) {
            p = formatConverter.transformToRigidFormat(model);
            RigidStructurer rigidStructurer = new RigidStructurer();
            p = rigidStructurer.structureProcess(p);
            model = formatConverter.transformFromRigidFormat(p);
            p = formatConverter.transformToRPSTFormat(model);
            rpst = new RPST<>(p);
        }

        // Convert to Text
        TextPlanner converter = new TextPlanner(rpst, model, lDeriver, lHelper, imperativeRole, false, false);
        converter.convertToText(rpst.getRoot(), 0);
        ArrayList<DSynTSentence> sentencePlan = converter.getSentencePlan();

        // Aggregation
        SentenceAggregator sentenceAggregator = new SentenceAggregator();
        sentencePlan = sentenceAggregator.performRoleAggregation(sentencePlan);

        // Referring Expression
        ReferringExpressionGenerator refExpGenerator = new ReferringExpressionGenerator(lHelper);
        sentencePlan = refExpGenerator.insertReferringExpressions(sentencePlan, false);

        // Discourse Marker
        DiscourseMarker discourseMarker = new DiscourseMarker();
        sentencePlan = discourseMarker.insertSequenceConnectives(sentencePlan);

        // Realization
        SurfaceRealizer surfaceRealizer = new SurfaceRealizer();
        String surfaceText = surfaceRealizer.realizeSentenceMap(sentencePlan, transformedElemsRev);

        // Cleaning
        surfaceText = surfaceRealizer.postProcessText(surfaceText);

        if (surfaceOnly) {
            return surfaceText;
        }

        return appendTextToFile(input, surfaceText);
    }

    private String appendTextToFile(String file, String text) {
        StringBuilder newFile = new StringBuilder();
        try {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                if (!strLine.equals("</pnml>")) {
                    newFile.append(strLine).append("\n");
                } else {
                    newFile.append(text).append("\n</pnml>");
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newFile.toString();
    }
}