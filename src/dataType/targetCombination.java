package dataType;

import java.util.ArrayList;

public class targetCombination {

	private ArrayList<ArrayList<String>> targetCombination;
	private ArrayList<String> diseaseNodeList;
	private ArrayList<Double> offTargetScore;
	
	public targetCombination() {
		targetCombination=new ArrayList<ArrayList<String>>();
		diseaseNodeList=new ArrayList<String>();
		offTargetScore=new ArrayList<Double>();
	}

	public int size() {
		return offTargetScore.size();
	}

	public void addTargetCombination(ArrayList<String> tc, String nodelist, Double offTarget) {
		targetCombination.add(tc);
		diseaseNodeList.add(nodelist);
		offTargetScore.add(offTarget);
	}	

	public ArrayList<String> getTargetCombination(int i)
	{
		if(i>=0 && i<size())
			return targetCombination.get(i);
		else
			return null;
	}
	
	public String getDiseaseNodeList(int i)
	{
		if(i>=0 && i<size())
			return diseaseNodeList.get(i);
		else
			return "";
	}
	
	public Double getOffTargetScore(int i)
	{
		if(i>=0 && i<size())
			return offTargetScore.get(i);
		else
			return Double.valueOf(0f);
	}
	
	public void print() {
		System.out.println("\tTarget Combi\tOff-target score\tDisease node list");
		for(int i=0; i<size(); i++)
			System.out.println("["+i+"]:\t"+targetCombination.get(i).toString()+"\t"+offTargetScore.get(i)+"\t"+diseaseNodeList.get(i));
	}
}