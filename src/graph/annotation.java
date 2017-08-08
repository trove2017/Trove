package graph;

public class annotation {

	private String annotationSource;
	private String annotationID;
	private String annotationURL;
	private String annotationType;
	
	public annotation(String source, String id, String url, String type) {
		annotationSource=source;
		annotationID=id;
		annotationURL=url;
		annotationType=type;
	}

	public String getSource() {
		return annotationSource;
	}

	public String getID() {
		return annotationID;
	}	

	public String getURL() {
		return annotationURL;
	}

	public String getType() {
		return annotationType;
	}
	
	public void print() {
		System.out.println("["+annotationID+"]"+annotationSource+"="+annotationURL+" ("+annotationType+")");
	}
}