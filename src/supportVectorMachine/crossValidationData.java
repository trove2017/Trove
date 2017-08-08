package supportVectorMachine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class crossValidationData {
	private ArrayList<String> testSet;
	private ArrayList<String> validationSet;
	private ArrayList<Integer> valFoldLastIndex;
	private ArrayList<String> tList;
	private ArrayList<String> aList;

	//generate xValidation internally in the system
	public crossValidationData(int numFold, ArrayList<String> targetList, ArrayList<String> allNodeList) {
		int minTargetPerFold, maxTargetPerFold, minSizePerFold, maxSizePerFold;
		int expectedNumMinSizeFold, expectedNumMaxSizeFold, expectedNumMinTargetFold, expectedNumMaxTargetFold;
		int currFoldNumTarget, currFoldSize, currNumMinTargetFold=0,  currNumMaxTargetFold=0, currNumMinSizeFold=0, currNumMaxSizeFold=0;
		int index;
		Random randomGenerator = new Random();

		testSet=new ArrayList<String>();
		validationSet=new ArrayList<String>();
		valFoldLastIndex=new ArrayList<Integer>();
		tList=new ArrayList<String>();
		for(int i=0; i<targetList.size(); i++)
			tList.add(targetList.get(i));
		aList=new ArrayList<String>();
		for(int i=0; i<allNodeList.size(); i++)
			aList.add(allNodeList.get(i));
		/*
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("aList: "+aList);
		System.out.println("tList: "+tList);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		*/
		minSizePerFold=(int)Math.floor((double)aList.size()/(double)(numFold+1));
		maxSizePerFold=(int)Math.ceil((double)aList.size()/(double)(numFold+1));
		minTargetPerFold=(int)Math.floor((double)tList.size()/(double)(numFold+1));
		maxTargetPerFold=(int)Math.ceil((double)tList.size()/(double)(numFold+1));

		System.out.println("------------------------------------");
		System.out.println(minSizePerFold);
		System.out.println(maxSizePerFold);
		System.out.println(minTargetPerFold);
		System.out.println(maxTargetPerFold);
		System.out.println("------------------------------------");
		
		//remove target nodes from allNodeList
		for(int i=0; i<tList.size(); i++)
			aList.remove(tList.get(i));

		//generate test set first
		currFoldNumTarget=getNumber(minTargetPerFold, maxTargetPerFold);
		for(int i=0; i<currFoldNumTarget; i++)
		{
			index=randomGenerator.nextInt(tList.size());
			testSet.add(tList.get(index));
			tList.remove(index);
		}
		currFoldSize=getNumber(minSizePerFold, maxSizePerFold)-currFoldNumTarget;
		for(int i=0; i<currFoldSize; i++)
		{
			index=randomGenerator.nextInt(aList.size());
			testSet.add(aList.get(index));
			aList.remove(index);
		}

		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		System.out.println("aList: ["+aList.size()+"] "+aList);
		System.out.println("tList: ["+tList.size()+"] "+tList);
		System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
		
		//recompute fold information
		minSizePerFold=(int)Math.floor((double)(aList.size()+tList.size())/(double)numFold);
		maxSizePerFold=(int)Math.ceil((double)(aList.size()+tList.size())/(double)numFold);
		if(minSizePerFold==maxSizePerFold)
		{
			expectedNumMinSizeFold=numFold;
			expectedNumMaxSizeFold=numFold;
		}
		else
		{
			expectedNumMinSizeFold=((aList.size()+tList.size())-maxSizePerFold*numFold)/(minSizePerFold-maxSizePerFold);
			expectedNumMaxSizeFold=numFold-expectedNumMinSizeFold;
		}
		if(minTargetPerFold==maxTargetPerFold)
		{
			expectedNumMinTargetFold=numFold;
			expectedNumMaxTargetFold=numFold;
		}
		else
		{
			expectedNumMinTargetFold=(maxTargetPerFold*numFold-tList.size())/(maxTargetPerFold-minTargetPerFold);
			expectedNumMaxTargetFold=numFold-expectedNumMinTargetFold;
		}
		minTargetPerFold=(int)Math.floor((double)tList.size()/(double)numFold);
		maxTargetPerFold=(int)Math.ceil((double)tList.size()/(double)numFold);

		System.out.println(testSet.toString());
		System.out.println("------------------------------------");
		System.out.println("minFoldSize:"+minSizePerFold);
		System.out.println("maxFoldSize:"+maxSizePerFold);
		System.out.println("expected#MinFold:"+expectedNumMinSizeFold);
		System.out.println("expected#MaxFold:"+expectedNumMaxSizeFold);
		System.out.println("minTarget:"+minTargetPerFold);
		System.out.println("maxTarget:"+maxTargetPerFold);
		System.out.println("expected#FoldWithMinTarget:"+expectedNumMinTargetFold);
		System.out.println("expected#FoldWithMaxTarget:"+expectedNumMaxTargetFold);
		System.out.println("------------------------------------");
		
		currNumMinTargetFold=0;
		currNumMaxTargetFold=0;
		currNumMinSizeFold=0;
		currNumMaxSizeFold=0;
		
		//generate validationSet
		for(int f=0; f<numFold; f++)
		{
			System.out.println("-------------------------------------------------");
			System.out.println("currNumMinTargetFold: "+currNumMinTargetFold);
			System.out.println("currNumMaxTargetFold: "+currNumMaxTargetFold);
			System.out.println("currNumMinSizeFold: "+currNumMinSizeFold);
			System.out.println("currNumMaxSizeFold: "+currNumMaxSizeFold);
			System.out.println("-------------------------------------------------");
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			System.out.println("aList: ["+aList.size()+"] "+aList);
			System.out.println("tList: ["+tList.size()+"] "+tList);
			System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			
			if(currNumMinTargetFold<expectedNumMinTargetFold && currNumMaxTargetFold<expectedNumMaxTargetFold)
				currFoldNumTarget=getNumber(minTargetPerFold, maxTargetPerFold);
			else 
			{
				if(currNumMinTargetFold<expectedNumMinTargetFold)
					currFoldNumTarget=minTargetPerFold;
				else
					currFoldNumTarget=maxTargetPerFold;
			}
			
			for(int i=0; i<currFoldNumTarget; i++)
			{
				index=randomGenerator.nextInt(tList.size());
				validationSet.add(tList.get(index));
				tList.remove(index);
			}
			
			if(currNumMinSizeFold<expectedNumMinSizeFold && currNumMaxSizeFold<expectedNumMaxSizeFold)
				currFoldSize=getNumber(minSizePerFold, maxSizePerFold)-currFoldNumTarget;
			else 
			{
				if(currNumMinSizeFold<expectedNumMinSizeFold)
					currFoldSize=minSizePerFold-currFoldNumTarget;
				else
					currFoldSize=maxSizePerFold-currFoldNumTarget;
			}
			for(int i=0; i<currFoldSize; i++)
			{
				//System.out.println("aList.size:"+aList.size());
				index=randomGenerator.nextInt(aList.size());
				validationSet.add(aList.get(index));
				aList.remove(index);
			}
			
			System.out.println("thisFold target:"+currFoldNumTarget+" targetLeft: "+tList.size());
			System.out.println("thisFold size:"+(currFoldSize+currFoldNumTarget)+" restLeft: "+(aList.size()+tList.size()));
			
			if((currFoldSize+currFoldNumTarget)==minSizePerFold)
				currNumMinSizeFold++;
			else
				currNumMaxSizeFold++;
			
			if(currFoldNumTarget==minTargetPerFold)
				currNumMinTargetFold++;
			else
				currNumMaxTargetFold++;
			valFoldLastIndex.add(validationSet.size()-1);
			System.out.println("Fold "+f);
			print();
		}
	}

	//load cross validation file
	public crossValidationData(String fName, ArrayList<String> allNodeList) {
		BufferedReader reader;
		
		testSet=new ArrayList<String>();
		validationSet=new ArrayList<String>();
		valFoldLastIndex=new ArrayList<Integer>();

		String nextLine, delimiter=",", nodeName;
		int delimiterIndex, lineCount=0;
		
		try {
			reader = new BufferedReader(new FileReader(fName));
			while ((nextLine = reader.readLine()) != null) 
			{
				delimiterIndex=nextLine.indexOf(delimiter);
				nextLine=nextLine.substring(delimiterIndex+1);
				
				while(nextLine!=null)
				{
					delimiterIndex=nextLine.indexOf(delimiter);
					if(delimiterIndex>0)
					{
						nodeName=nextLine.substring(0, delimiterIndex).trim();
						nextLine=nextLine.substring(delimiterIndex+1);
					}
					else
					{
						nodeName=nextLine.trim();
						nextLine=null;
					}
					if(allNodeList.contains(nodeName)==false)
					{
						//System.out.println(lineCount+" nodeName:"+nodeName);
						JOptionPane.showMessageDialog(new JPanel(), "This node does not exist in the network:"+nodeName, "Please check cross validation data file", JOptionPane.ERROR_MESSAGE);
						reader.close();
						return;
					}
					if(lineCount==0)
						testSet.add(nodeName);
					else
						validationSet.add(nodeName);
				}
				if(lineCount>0)
					valFoldLastIndex.add(validationSet.size()-1);
				
				//System.out.println("Fold "+lineCount);
				//print();
				
				lineCount++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getNumber(int lowerBound, int upperBound)
	{
		if(lowerBound==upperBound)
			return lowerBound;
		else
		{
			Random randomGenerator = new Random();
			if(randomGenerator.nextInt(100)>50)
				return upperBound;
			else
				return lowerBound;
		}
	}

	public ArrayList<String> getTestSet(){
		return testSet;
	}

	public ArrayList<String> getValidationTestSet(int i){
		ArrayList<String> list=new ArrayList<String>();
		int startIndex, lastIndex;

		if(i<0 || i>=valFoldLastIndex.size())
			return null;

		lastIndex=valFoldLastIndex.get(i);
		if(i==0)
			startIndex=0;
		else
			startIndex=valFoldLastIndex.get(i-1)+1;

		for(int j=startIndex; j<=lastIndex; j++)
			list.add(validationSet.get(j));

		return list;
	}

	public ArrayList<String> getValidationTrainingSet(int i){
		ArrayList<String> list=new ArrayList<String>();
		int startIndex, lastIndex;

		if(i<0 || i>=valFoldLastIndex.size())
			return null;

		lastIndex=valFoldLastIndex.get(i);
		if(i==0)
			startIndex=0;
		else
			startIndex=valFoldLastIndex.get(i-1)+1;

		for(int j=0; j<validationSet.size(); j++)
		{
			if(j<startIndex || j>lastIndex)
				list.add(validationSet.get(j));
		}
		return list;
	}

	public ArrayList<String> getValidationFullTrainingSet(){
		ArrayList<String> list=new ArrayList<String>();
		
		for(int j=0; j<validationSet.size(); j++)
			list.add(validationSet.get(j));
		
		return list;
	}
	
	public int getNumberOfFolds(){
		return valFoldLastIndex.size();
	}

	public void print() {
		System.out.println("testSet");
		System.out.println(testSet.toString());
		System.out.println("validationSet");
		System.out.println(validationSet.toString());
		System.out.println("valFoldLastIndex");
		System.out.println(valFoldLastIndex);
	}
}