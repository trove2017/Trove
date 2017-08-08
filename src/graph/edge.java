package graph;

public class edge {

	private String sourceID;
	private String sourceName;
	private String targetID;
	private String targetName;
	private String type;
	
	public edge(String sID, String sName, String tID, String tName, String edgeType) {
		sourceID=sID;
		sourceName=sName;
		targetID=tID;
		targetName=tName;
		type=edgeType;
	}

	public String getSourceID() {
		return sourceID;
	}

	public String getSourceName() {
		return sourceName;
	}	

	public String getTargetID() {
		return targetID;
	}

	public String getTargetName() {
		return targetName;
	}	

	public String getType() {
		return type;
	}	
	
	public void print() {
		System.out.println("["+sourceID+"]"+sourceName+"->["+targetID+"]"+targetName+" ("+type+")");
	}
}