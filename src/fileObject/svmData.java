package fileObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import constants.stringConstant;

public class svmData {
	private ArrayList<ArrayList<Double>> data;
	private ArrayList<Double> classLabels;
	private stringConstant staticString;

	public svmData() {
		data=new ArrayList<ArrayList<Double>>();
		classLabels=new ArrayList<Double>();
		staticString=new stringConstant();
	}

	public int getNumberOfData() {
		return data.size();
	}
	
	public int getNumberOfFeature() {
		if(data!=null && data.size()>0)
			return data.get(0).size();
		else
		{
			System.out.println("[svmData:getNumberOfFeature]: Error! data==null or data.size()==0");
			return -1;
		}
	}
	
	public double[] getDataRow(int i) {
		double[] row;
		
		if(i>=0 && i<data.size())
		{
			row=new double[data.get(i).size()];
			for(int j=0; j<data.get(i).size(); j++)
				row[j]=data.get(i).get(j);
			return row;
		}
		
		return null;
	}

	public double getClassLabel(int i) {
		if(i>=0 && i<data.size())
			return classLabels.get(i);
		else
			return Double.NaN;
	}
	
	public void readFile(String fileName) {
		String TAB=staticString.getTab();
		int numFrontColToSkip=3;
		
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
			int lineCounter=0, tabIndex;
			String lineStr, isTarget, normRank;
			
			while((lineStr=fileReader.readLine())!=null)
			{
				ArrayList<Double> normRankList=new ArrayList<Double>();
				if(lineCounter!=0) //firstLine is the header
				{
					for(int i=0; i<numFrontColToSkip; i++)
					{
						tabIndex=lineStr.indexOf(TAB);
						lineStr=lineStr.substring(tabIndex+1);
					}
					tabIndex=lineStr.indexOf(TAB);
					isTarget=lineStr.substring(0, tabIndex);
					if(isTarget!=null && isTarget.length()>0 && isTarget.compareTo(" ")!=0)
						classLabels.add(Double.valueOf(isTarget));
					else
						classLabels.add(Double.NaN);
					
					lineStr=lineStr.substring(tabIndex+1);
					tabIndex=lineStr.indexOf(TAB);
					
					while(tabIndex!=-1)
					{
						normRank=lineStr.substring(0, tabIndex);
						normRankList.add(Double.parseDouble(normRank));
						lineStr=lineStr.substring(tabIndex+1);
						tabIndex=lineStr.indexOf(TAB);
					}
					//last normalized property rank
					normRank=lineStr;
					normRankList.add(Double.parseDouble(normRank));
					data.add(normRankList);
				}
				lineCounter++;
			}

			fileReader.close();
		} catch (IOException e) {
			System.out.println("Exception "+e.getMessage());
		}
	}

	public void print(){
		String TAB=staticString.getTab();
		System.out.println("svm data");
		for(int i=0; i<classLabels.size(); i++)
			System.out.println(i+TAB+classLabels.get(i)+TAB+data.get(i).toString());
	}
}