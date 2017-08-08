package dataType;

public class stringArray {

	private String[] strArray;

	public stringArray(int size) {
		strArray=new String[size];
	}

	public int size() {
		return strArray.length;
	}

	public String[] getStringArray() {
		return strArray;
	}	

	public String getStringArrayAtIndex(int i) {
		if(i>=0 && i<size())
			return strArray[i];
		else
			return null;
	}

	public void setStringArrayAtIndex(int i, String s) {
		if(i>=0 && i<size())
			strArray[i]=s;
	}	

	public void print() {
		System.out.println("\tId\tValue");
		for(int i=0; i<size(); i++)
			System.out.println("["+i+"]:\t"+strArray[i]);
	}
}