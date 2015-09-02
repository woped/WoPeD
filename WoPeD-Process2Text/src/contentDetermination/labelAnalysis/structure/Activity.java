package contentDetermination.labelAnalysis.structure;

import java.util.ArrayList;

public class Activity {

	protected String label;
	protected ArrayList<Activity> model;
	protected String label_tag;
	protected String oLabel;
	protected String[] editedLabelSplit;
	protected String editedLabel;

	public Activity(String oLabel, String label, String label_tag, ArrayList<Activity> model) {
		this.label = label;
		this.oLabel = oLabel;
		this.model = model;
		this.label_tag = label_tag;
		this.editedLabelSplit = null;
		this.editedLabel = "";
	}

	public String getEditedLabel() {
		return this.editedLabel;
	}

	public void setEditedLabel(String editedLabel) {
		this.editedLabel = editedLabel;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}

	public String[] getEditedLabelSplit() {
		return this.editedLabelSplit;
	}

	public void setEditedLabelSplit(String[] editedLabelSplit) {
		this.editedLabelSplit = editedLabelSplit;
	}

	public String getOLabel() {
		return oLabel;
	}

	public String getLabel() {
		return label;
	}

	public String getLabel_Tag() {
		return label_tag;
	}

	public ArrayList<Activity> getModel() {
		return model;
	}

	public int getLabelLength() {
		return label.split(" ").length;
	}

}
