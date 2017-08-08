package kinetics;

public class rate {

	private String organism;
	private String cellLine;
	private String source;
	private String url;
	private String parameter;
	private String param_val;
	
	public rate(String org, String cell, String sourceName, String urlString, String param, String val) {
		organism=org;
		cellLine=cell;
		source=sourceName;
		url=urlString;
		parameter=param;
		param_val=val;
	}

	public String getOrganism() {
		return organism;
	}

	public String getCellLine() {
		return cellLine;
	}	

	public String getSource() {
		return source;
	}

	public String getURL() {
		return url;
	}
	
	public String getParameter() {
		return parameter;
	}
	
	public String getParamVal() {
		return param_val;
	}
	
	public void print() {
		System.out.println("["+organism+","+cellLine+"]"+source+"="+url+" ("+parameter+","+param_val+")");
	}
}