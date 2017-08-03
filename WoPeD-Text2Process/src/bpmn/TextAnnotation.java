package bpmn;

public class TextAnnotation extends Artifact {
	
	 /** The font size */
    public final static String PROP_FONTSIZE = "font_size";

    public TextAnnotation() {
        super();
        initializeProperties();
    }

    private void initializeProperties() {
        setProperty(PROP_FONTSIZE, "10");
    }

}
