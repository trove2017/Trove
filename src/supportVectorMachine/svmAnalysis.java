package supportVectorMachine;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.JLabel;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_print_interface;
import libsvm.svm_problem;
import constants.integerConstant;
import constants.stringConstant;
import database.postgreSQL;
import fileObject.fReaderWriter;

public class svmAnalysis
{
	private svm_parameter param = new svm_parameter();
	private svm_problem prob = new svm_problem();
	private svm_model model;
	private int NUM_OF_FOLDS;
	private String TXTFILEEXTENSION;
	private String LINEAR;
	private ArrayList<Double> weight=new ArrayList<Double>();
	private ArrayList<Integer> weight_label=new ArrayList<Integer>();
	private fReaderWriter fWriter=new fReaderWriter();;
	private ArrayList<String> featureToRemove;
	private crossValidationData xValData;
	private stringConstant staticString=new stringConstant();
	private integerConstant staticInteger=new integerConstant();
	private String disease, organism;

	private String modelMessage; 
	private double bestCExp, bestValidationAccuracy, bestTestAccuracy;
	private int bestTargetWt;
	private String bestKernel, testSetPredictionFilename;
	private ArrayList<String> bestPredictiveFeatures=new ArrayList<String>();
	private ArrayList<String> fullFeatureSet=new ArrayList<String>();
	private postgreSQL DB=new postgreSQL();
	private JLabel status;
	private String percentComplete;
	private String currHallmark;
	private ArrayList<String> targetNodes=new ArrayList<String>();
	
	//refer for further information 
	//1. http://www.csie.ntu.edu.tw/~cjlin/papers/libsvm.pdf
	//2. http://www.csie.ntu.edu.tw/~cjlin/papers/guide/guide.pdf

	//on choosing kernel
	//cite: http://www.csie.ntu.edu.tw/~cjlin/libsvm/faq.html#/Q4:_Training_and_prediction
	//In general we suggest you to try the RBF kernel first. A recent result by Keerthi and Lin (http://www.csie.ntu.edu.tw/~cjlin/papers/limit.pdf)
	//shows that if RBF is used with model selection, then there is no need to consider the linear kernel. The kernel matrix using sigmoid may not 
	//be positive definite and in general it's accuracy is not better than RBF. (see the paper by Lin and Lin (http://www.csie.ntu.edu.tw/~cjlin/papers/tanh.pdf).
	//Polynomial kernels are ok but if a high degree is used, numerical difficulties tend to happen (thinking about dth power of (<1) goes to 0 and (>1) goes to infinity). 
	public svmAnalysis() {
	
	}
	
	public void initializeSVM(String hallmark, int numFolds, crossValidationData xValidationData, String input_disease, String input_organism, ArrayList<String> featureListToStartWith, ArrayList<String> tNodes, JLabel statusLabel, String progress) {
		//initialize constants
		LINEAR=staticString.getLinear();
		TXTFILEEXTENSION=staticString.getTextFileExtension();
		NUM_OF_FOLDS=numFolds;
		xValData=xValidationData;
		disease=input_disease;
		organism=input_organism;
		//fullFeatureSet=getFeatureList(featureListToStartWith);
		fullFeatureSet=featureListToStartWith;
		status=statusLabel;
		targetNodes=tNodes;
		percentComplete=progress;
		currHallmark=hallmark;
		//initialize SVM settings;
		System.out.println("initialize SVM settings");
		initializeSVMSettings();
		//update normalized ranks of nodes
		System.out.println("update normalized node ranks");
		ArrayList<String> featureRanksToNormalize=new ArrayList<String>();
		for(int i=0; i<fullFeatureSet.size(); i++)
		{
			//if(DB.getFeature_featureNormRankComputed(fullFeatureSet.get(i))>0)
				featureRanksToNormalize.add(fullFeatureSet.get(i));
		}
		if(featureRanksToNormalize.size()>0)
			DB.updateNormalizedNodeRanks(featureRanksToNormalize, status);
	}
/*
	private ArrayList<String> getFeatureList(ArrayList<String> featureListWithDialogNames)
	{
		ArrayList<String> mappedFeatureList=new ArrayList<String>();

		if(featureListWithDialogNames.contains("In degree centrality")==true)
			mappedFeatureList.add("in_degree");
		if(featureListWithDialogNames.contains("Out degree centrality")==true)
			mappedFeatureList.add("out_degree");
		if(featureListWithDialogNames.contains("Total degree centrality")==true)
			mappedFeatureList.add("total_degree");
		if(featureListWithDialogNames.contains("Eigenvector centrality")==true)
			mappedFeatureList.add("eigenvector");
		if(featureListWithDialogNames.contains("Closeness centrality")==true)
			mappedFeatureList.add("closeness");
		if(featureListWithDialogNames.contains("Eccentricity centrality")==true)
			mappedFeatureList.add("eccentricity");
		if(featureListWithDialogNames.contains("Betweenness centrality")==true)
			mappedFeatureList.add("betweenness");
		if(featureListWithDialogNames.contains("Bridging coefficient")==true)
			mappedFeatureList.add("bridging_coefficient");
		if(featureListWithDialogNames.contains("Bridging centrality")==true)
			mappedFeatureList.add("bridging_centrality");
		if(featureListWithDialogNames.contains("Undirected clustering coefficient")==true)
			mappedFeatureList.add("undirected_clustering_coefficient");
		if(featureListWithDialogNames.contains("In clustering coefficient")==true)
			mappedFeatureList.add("in_clustering_coefficient");
		if(featureListWithDialogNames.contains("Out clustering coefficient")==true)
			mappedFeatureList.add("out_clustering_coefficient");
		if(featureListWithDialogNames.contains("Middleman clustering coefficient")==true)
			mappedFeatureList.add("middleman_clustering_coefficient");
		if(featureListWithDialogNames.contains("Cycle clustering coefficient")==true)
			mappedFeatureList.add("cycle_clustering_coefficient");
		if(featureListWithDialogNames.contains("Proximity prestige")==true)
			mappedFeatureList.add("proximity_prestige");
		if(featureListWithDialogNames.contains("Target downstream effect")==true)
			mappedFeatureList.add("target_downstream_effect");

		return mappedFeatureList;
	}
*/
	public double getBestCExp(){
		return bestCExp;
	}

	public double getBestValidationAccuracy(){
		return bestValidationAccuracy;
	}

	public double getBestTestAccuracy(){
		return bestTestAccuracy;
	}

	public int getBestTargetWt(){
		return bestTargetWt;
	}

	public String getBestKernel(){
		return bestKernel;
	}

	public String getTestSetPredictionFilename(){
		return testSetPredictionFilename;
	}

	public ArrayList<String> getBestPredictiveFeatures(){
		return bestPredictiveFeatures;
	}

	public String getModelMessage(){
		return modelMessage;
	}

	private void initializeSVMSettings()
	{
		// set param default values
		param.svm_type = svm_parameter.C_SVC;
		//svm learning tasks
		//1. C-SVC (C support vector classification) - param{C}
		//2. NU-SVC (v support vector classification) - param{nu}
		//3. ONE_CLASS (one-class SVM)
		//4. NU-SVR (v support vector regression) - param{C,nu}
		//5. EPSILON-SVR (e support vector regression)
		param.kernel_type = svm_parameter.LINEAR;
		//svm kernel
		//1. LINEAR (linear)
		//2. POLY (polyomial)
		//3. RBF (radial basis function) - param{C,gamma}
		//4. SIGMOID (sigmoid)
		//5. PRECOMPUTED (precomputed kernel)
		param.degree = 3; //degree of polynomial kernel function. Ignored by other kernels
		param.gamma = 0; //kernel coeff for RBF, POLY and SIGMOID. If gamma=0.0, then gamma=1/(#features) 
		param.coef0 = 0; //independent term in kernel function. Used in POLY and SIGMOID
		param.nu = 0.5; // range [0--1]
		param.cache_size = 40; //size of kernel cache (in MB)
		param.C = 1; // range [0--inf]
		param.eps = 1e-3;
		param.p = 0.1;
		param.shrinking = 0; //whether to use shrinking heuristics
		param.probability = 1; // whether it predicts probability (0=no, 1=yes)
		param.nr_weight = 0;
		param.weight_label = new int[0];
		param.weight = new double[0];	
	}

	public void set_param_svm_type(int svm_type)
	{
		param.svm_type=svm_type;
	}
	public void set_param_kernel_type(int kernel_type)
	{
		param.kernel_type=kernel_type;
	}
	public void set_param_degree(int degree)
	{
		param.degree=degree;
	}
	public void set_param_gamma(float gamma)
	{
		param.gamma=gamma;
	}
	public void set_param_coef0(float coef0)
	{
		param.coef0=coef0;
	}
	public void set_param_nu(float nu)
	{
		param.nu=nu;
	}	
	public void set_param_cache_size(float cache_size)
	{
		param.cache_size=cache_size;
	}
	public void set_param_C(float C)
	{
		param.C=C;
	}
	public void set_param_eps(float eps)
	{
		param.eps=eps;
	}
	public void set_param_p(float p)
	{
		param.p=p;
	}
	public void set_param_shrinking(int shrinking)
	{
		param.shrinking=shrinking;
	}
	public void set_param_probability(int probability)
	{
		param.probability=probability;
	}
	public void set_param_nr_weight(int nr_weight)
	{
		param.nr_weight=nr_weight;
	}
	public void set_param_weight_label(ArrayList<Integer> weight_label_array)
	{
		int[] weight_label=new int[weight_label_array.size()];
		for(int i=0; i<weight_label_array.size(); i++)
			weight_label[i]=weight_label_array.get(i);
		param.weight_label=weight_label;
	}
	public void set_param_weight(ArrayList<Double> weight_array)
	{
		double[] weight=new double[weight_array.size()];
		for(int i=0; i<weight_array.size(); i++)
			weight[i]=weight_array.get(i);
		param.weight=weight;
	}

	private ArrayList<Integer> getWeightLabel()
	{
		ArrayList<Integer> list=new ArrayList<Integer>();

		list.add(1);
		list.add(0);

		return list;
	}

	private ArrayList<Double> getWeight(Double target_cost, Double nonTarget_cost)
	{
		ArrayList<Double> list=new ArrayList<Double>();

		list.add(target_cost);
		list.add(nonTarget_cost);

		return list;
	}

	//WRE feature selection
	public void WREModelSelection(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory)
	{
		//Parameters to modify are C and gamma in the range [0--Inf]
		//will use the grid based approach proposed by Lin, C-J et al. to tune the parameters.
		//will do a search in the range 2^{-10} to 2^{10}
		svm_print_interface svm_print_null=new svm_print_interface(){public void print(String s){}};
		double[] currBest=new double[3]; 
		ArrayList<double[]> actualVsPrediction;
		String dataStream, TEST_SET_ACCURACY=staticString.getTestSetAccuracy();
		ArrayList<String> featureConfirmedToRemove=new ArrayList<String>();
		System.out.println("WREModelSelection");

		DB.clearAccuracyTable();
		featureToRemove=new ArrayList<String>();
		svm.svm_set_print_string_function(svm_print_null);

		for(int i=0; i<currBest.length; i++)
			currBest[i]=0;

		modelMessage=null;
		bestKernel=kernel;
		bestPredictiveFeatures=DB.getFeaturesWithThreshold("0.05", "0.6");
		featureToRemove=getComplementaryFeatures(fullFeatureSet, bestPredictiveFeatures);

		if(featureToRemove.size()<DB.getAllFeatureHeadersOfNode().size())
			currBest=basicSVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, -1);
		else
		{
			currBest=BSE_SVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, featureConfirmedToRemove);
			bestPredictiveFeatures=getComplementaryFeatures(fullFeatureSet, featureToRemove);
			modelMessage="All features rejected by WRE. Switch to BSE feature selection instead.";
		}

		//bestValidationAccuracy=currBest[0];
		//bestCExp=currBest[2];

		//train with entire train set
		prob=readTrainData(prob, xValData.getValidationFullTrainingSet(), featureToRemove, -1, -1, -1);
		if(kernel.compareTo(LINEAR)==0)
			set_param_C((float)Math.pow(2, currBest[2]));
		//set_param_gamma((float)Math.pow(2, currBest.getVal3()));
		//set_param_coef0((float)Math.pow(2, currBest.getVal4()));
		//set_param_degree((int)currBest.getVal5());

		if(weight_label!=null)
		{
			set_param_weight_label(weight_label);
			set_param_weight(weight);
			set_param_nr_weight(weight_label.size());
		}

		model = svm.svm_train(prob, param);

		//test with test set
		testSetPredictionFilename=destDirectory+"testSetPrediction"+TXTFILEEXTENSION;
		fWriter.deleteFile(destDirectory, "testSetPrediction"+TXTFILEEXTENSION);
		actualVsPrediction=new ArrayList<double[]>();
		actualVsPrediction=readValidateData(actualVsPrediction, destDirectory, "testSetPrediction"+TXTFILEEXTENSION, 
				xValData.getTestSet(), true, featureToRemove, -1, -1, -1);
		//compute accuracy
		bestTestAccuracy=getAccuracy(actualVsPrediction).doubleValue();
		dataStream=TEST_SET_ACCURACY+":"+bestTestAccuracy+"\n";
		fWriter.concatenateToFileWithBufferedWriter(destDirectory, "testSetPrediction"+TXTFILEEXTENSION, dataStream);
	}

	private double[] basicSVM(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory, double progress)
	{
		double[] currBest=new double[3]; 

		if(useWMC==false)
		{
			bestTargetWt=0;

			for(int i=0; i<numTuneLevel; i++)
			{
				if(i==numTuneLevel-1)
				{
					if(kernel.compareTo(LINEAR)==0)
						currBest=tuneLinear(i, 0, sourceDirectory, destDirectory, currBest, true, true, null, null, 100);
				}
				else
				{
					if(kernel.compareTo(LINEAR)==0)
						currBest=tuneLinear(i, 0, sourceDirectory, destDirectory, currBest, false, true, null, null, 100);
				}
			}
			weight_label=null;
			bestValidationAccuracy=currBest[0];
			bestCExp=currBest[2];
		}
		else
		{
			double bestAccuracy=0, thisAccuracy, bestC=0;
			int bestW=-1;
			if(wmcWt==-1)
			{
				for(int w=1; w<10; w++)
				{
					weight_label=getWeightLabel();
					weight=getWeight(Double.valueOf(w), Double.valueOf(10-w));
					set_param_weight_label(weight_label);
					set_param_weight(weight);
					set_param_nr_weight(weight_label.size());
					System.out.println(w+" weightLabel:"+weight_label.toString());
					System.out.println(w+" weigh:"+weight.toString());
					System.out.println(String.valueOf(w));
					for(int i=0; i<currBest.length; i++)
						currBest[i]=0;
					for(int i=0; i<numTuneLevel; i++)
					{
						if(i==numTuneLevel-1)
						{
							if(kernel.compareTo(LINEAR)==0)
								currBest=tuneLinear(i, w, sourceDirectory, destDirectory, currBest, true, true, weight_label, weight, 100);
						}
						else
						{
							if(kernel.compareTo(LINEAR)==0)
								currBest=tuneLinear(i, w, sourceDirectory, destDirectory, currBest, false, true, weight_label, weight, 100);
						}
					}
				}
				for(int w=1; w<10; w++)
				{
					System.out.println(String.valueOf(w));
					thisAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(w)));
					if(thisAccuracy>bestAccuracy)
					{
						bestAccuracy=thisAccuracy;
						bestW=w;
						bestC=DB.getCOfModel(String.valueOf(bestAccuracy));
					}
				}
			}
			else
			{
				int w=wmcWt;
				weight_label=getWeightLabel();
				weight=getWeight(Double.valueOf(w), Double.valueOf(10-w));
				set_param_weight_label(weight_label);
				set_param_weight(weight);
				set_param_nr_weight(weight_label.size());
				System.out.println(w+" weightLabel:"+weight_label.toString());
				System.out.println(w+" weigh:"+weight.toString());
				System.out.println(String.valueOf(w));
				for(int i=0; i<currBest.length; i++)
					currBest[i]=0;
				for(int i=0; i<numTuneLevel; i++)
				{
					if(i==numTuneLevel-1)
					{
						if(kernel.compareTo(LINEAR)==0)
							currBest=tuneLinear(i, w, sourceDirectory, destDirectory, currBest, true, true, weight_label, weight, 100);
					}
					else
					{
						if(kernel.compareTo(LINEAR)==0)
							currBest=tuneLinear(i, w, sourceDirectory, destDirectory, currBest, false, true, weight_label, weight, 100);
					}
				}
				System.out.println(String.valueOf(w));
				thisAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(w)));
				if(thisAccuracy>bestAccuracy)
				{
					bestAccuracy=thisAccuracy;
					bestW=w;
					bestC=DB.getCOfModel(String.valueOf(bestAccuracy));
				}
			}
			bestValidationAccuracy=bestAccuracy;
			bestCExp=bestC;
			bestTargetWt=bestW;
			weight_label=getWeightLabel();
			weight=getWeight(Double.valueOf(bestTargetWt), Double.valueOf(10-bestTargetWt));
		}

		return currBest;
	}

	private double[] BSE_SVM(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory, ArrayList<String> featureConfirmedToRemove)
	{
		ArrayList<String> newFeatureConfirmedToRemove=new ArrayList<String>();
		ArrayList<ArrayList<String>> featureArrayToConsiderForRemoval=new ArrayList<ArrayList<String>>();
		boolean STOP_REGRESSION=false;
		double bestSVMAccuracy=0, bestFeatureRemovalAccuracy=0;
		double[] currBest=new double[3]; 
		
		//progressively remove single feature greedily until no improvement to model
		do{
			status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] Create candidate array to consider for feature removal");
			featureArrayToConsiderForRemoval=createCandidateArrayToConsiderForRemoval(featureConfirmedToRemove, fullFeatureSet);
			System.out.println("featureArrayToConsiderForRemoval.size():"+featureArrayToConsiderForRemoval.size());
			int totalCount=featureArrayToConsiderForRemoval.size();
			for(int f=0; f<totalCount; f++)
			{
				double percentageComplete=100.0*f/(totalCount-1);
				DecimalFormat df = new DecimalFormat();
				df.setMaximumFractionDigits(2);
				status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(percentageComplete)+"%] tune SVM");
				featureToRemove=featureArrayToConsiderForRemoval.get(f);
				for(int i=0; i<currBest.length; i++)
					currBest[i]=0;
				for(int i=0; i<numTuneLevel; i++)
				{
					if(i==numTuneLevel-1)
						currBest=tuneLinear(i, 0, sourceDirectory, destDirectory, currBest, true, true, null, null, percentageComplete);
					else
						currBest=tuneLinear(i, 0, sourceDirectory, destDirectory, currBest, false, true, null, null, percentageComplete);
				}
			}

			//using greedy approach, look for the one that has the highest accuracy and remove that feature
			//provided that this highest accuracy is >= to the bestSVMAccuracy. Stop entire regression when
			//highest accuracy < bestSVMAccuracy
			bestFeatureRemovalAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(0)));
			newFeatureConfirmedToRemove=DB.getFeatureToRemoveOfModel(String.valueOf(bestFeatureRemovalAccuracy));

			System.out.println("bestFeatureRemovalAccuracy:"+bestFeatureRemovalAccuracy);
			System.out.println("newFeatureConfirmedToRemove:"+newFeatureConfirmedToRemove.toString());
			System.out.println("bestSVMAccuracy:"+bestSVMAccuracy);
			if(bestFeatureRemovalAccuracy>=bestSVMAccuracy && newFeatureConfirmedToRemove.size()>featureConfirmedToRemove.size() 
					&& newFeatureConfirmedToRemove.size()<(fullFeatureSet.size()-1))
			{
				featureConfirmedToRemove=new ArrayList<String>();
				for(int i=0; i<newFeatureConfirmedToRemove.size(); i++)
					featureConfirmedToRemove.add(newFeatureConfirmedToRemove.get(i));
				bestSVMAccuracy=bestFeatureRemovalAccuracy;
				System.out.println("update featureConfirmedToRemove.................");
			}
			else
				STOP_REGRESSION=true;
		}while(!STOP_REGRESSION);

		featureToRemove=DB.getFeatureToRemoveOfModel(String.valueOf(bestFeatureRemovalAccuracy));
		System.out.println("featureToRemove:"+featureToRemove.toString());
		bestPredictiveFeatures=getComplementaryFeatures(fullFeatureSet, featureToRemove);
		
		if(useWMC==false)
		{
			bestTargetWt=0;
			bestValidationAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(0)));
			bestCExp=DB.getCOfModel(String.valueOf(bestValidationAccuracy));
		}
		else
		{
			basicSVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, 100);
			double bestAccuracy=0, thisAccuracy, bestC=0;
			int bestW=-1;
			if(wmcWt==-1)
			{
				for(int w=1; w<10; w++)
				{
					thisAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(w)));
					if(thisAccuracy>bestAccuracy)
					{
						bestAccuracy=thisAccuracy;
						bestW=w;
						bestC=DB.getCOfModel(String.valueOf(bestAccuracy));
					}
				}
			}
			else
			{
				int w=wmcWt;
				thisAccuracy=Double.valueOf(DB.getMaxAccuracy(String.valueOf(w)));
				bestAccuracy=thisAccuracy;
				bestW=w;
				bestC=DB.getCOfModel(String.valueOf(bestAccuracy));
			}
			bestValidationAccuracy=bestAccuracy;
			bestCExp=bestC;
			bestTargetWt=bestW;
			weight_label=getWeightLabel();
			weight=getWeight(Double.valueOf(bestTargetWt), Double.valueOf(10-bestTargetWt));
		}
		return currBest;
	}

	//no feature selection
	public void naiveModelSelection(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory)
	{
		//Parameters to modify are C and gamma in the range [0--Inf]
		//will use the grid based approach proposed by Lin, C-J et al. to tune the parameters.
		//will do a search in the range 2^{-10} to 2^{10}
		svm_print_interface svm_print_null=new svm_print_interface(){public void print(String s){}};
		double[] currBest=new double[3]; 
		ArrayList<double[]> actualVsPrediction;
		String dataStream, TEST_SET_ACCURACY=staticString.getTestSetAccuracy();

		System.out.println("naiveModelSelection");

		DB.clearAccuracyTable();
		featureToRemove=new ArrayList<String>();
		svm.svm_set_print_string_function(svm_print_null);

		for(int i=0; i<currBest.length; i++)
			currBest[i]=0;

		modelMessage=null;
		bestKernel=kernel;
		bestPredictiveFeatures=fullFeatureSet;

		currBest=basicSVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, -1);

		//train with entire train set
		prob=readTrainData(prob, xValData.getValidationFullTrainingSet(), featureToRemove, -1, -1, -1);
		if(kernel.compareTo(LINEAR)==0)
			set_param_C((float)Math.pow(2, currBest[2]));
		//set_param_gamma((float)Math.pow(2, currBest.getVal3()));
		//set_param_coef0((float)Math.pow(2, currBest.getVal4()));
		//set_param_degree((int)currBest.getVal5());

		if(weight_label!=null)
		{
			set_param_weight_label(weight_label);
			set_param_weight(weight);
			set_param_nr_weight(weight_label.size());
		}

		model = svm.svm_train(prob, param);

		//test with test set
		testSetPredictionFilename=destDirectory+"testSetPrediction"+TXTFILEEXTENSION;
		fWriter.deleteFile(destDirectory, "testSetPrediction"+TXTFILEEXTENSION);
		actualVsPrediction=new ArrayList<double[]>();
		actualVsPrediction=readValidateData(actualVsPrediction, destDirectory, "testSetPrediction"+TXTFILEEXTENSION, 
				xValData.getTestSet(), true, featureToRemove, -1, -1, -1);
		//compute accuracy
		bestTestAccuracy=getAccuracy(actualVsPrediction).doubleValue();
		dataStream=TEST_SET_ACCURACY+":"+bestTestAccuracy+"\n";
		fWriter.concatenateToFileWithBufferedWriter(destDirectory, "testSetPrediction"+TXTFILEEXTENSION, dataStream);
	}

	private ArrayList<ArrayList<String>> createCandidateArrayToConsiderForRemoval(ArrayList<String> confirmedFeature, ArrayList<String> fullFeatureSet)
	{
		ArrayList<ArrayList<String>> candidateArray=new ArrayList<ArrayList<String>>();

		for(int i=0; i<fullFeatureSet.size(); i++)
		{
			if(confirmedFeature.contains(fullFeatureSet.get(i))==false)
			{
				ArrayList<String> candidateItem=new ArrayList<String>();
				if(confirmedFeature.size()>0)
				{
					for(int j=0; j<confirmedFeature.size(); j++)
						candidateItem.add(confirmedFeature.get(j));
				}
				candidateItem.add(fullFeatureSet.get(i));
				candidateArray.add(candidateItem);
			}
		}

		return candidateArray;
	}

	private ArrayList<String> getComplementaryFeatures(ArrayList<String> fullList, ArrayList<String> toExcludeList)
	{
		ArrayList<String> finalList=new ArrayList<String>();

		for(int i=0; i<fullList.size(); i++)
		{
			if(toExcludeList.contains(fullList.get(i))==false)
				finalList.add(fullList.get(i));
		}

		return finalList;
	}

	//WRE-BSE feature selection
	public void WREBSEModelSelection(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory)
	{
		//Parameters to modify are C and gamma in the range [0--Inf]
		//will use the grid based approach proposed by Lin, C-J et al. to tune the parameters.
		//will do a search in the range 2^{-10} to 2^{10}
		svm_print_interface svm_print_null=new svm_print_interface(){public void print(String s){}};
		double[] currBest=new double[3]; 
		ArrayList<double[]> actualVsPrediction;
		String dataStream, TEST_SET_ACCURACY=staticString.getTestSetAccuracy();
		ArrayList<String> featureConfirmedToRemove=new ArrayList<String>();

		System.out.println("WREBSEModelSelection");

		DB.clearAccuracyTable();
		featureToRemove=new ArrayList<String>();
		svm.svm_set_print_string_function(svm_print_null);

		modelMessage=null;
		bestKernel=kernel;
		bestPredictiveFeatures=DB.getFeaturesWithThreshold("0.05", "0.6");
		featureConfirmedToRemove=getComplementaryFeatures(fullFeatureSet, bestPredictiveFeatures);
		featureToRemove=featureConfirmedToRemove;

		System.out.println("bestPredictiveFeatures:"+bestPredictiveFeatures.toString());
		System.out.println("bestPredictiveFeatures size:"+bestPredictiveFeatures.size());

		if(bestPredictiveFeatures.size()==1)
			currBest=basicSVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, 100);
		else
		{
			if(bestPredictiveFeatures.size()==0)
			{
				featureConfirmedToRemove=new ArrayList<String>();
				featureToRemove=new ArrayList<String>();
				modelMessage="All features rejected by WRE. Switch to BSE feature selection instead.";
			}
			currBest=BSE_SVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, featureConfirmedToRemove);
			bestPredictiveFeatures=getComplementaryFeatures(fullFeatureSet, featureToRemove);
		}

		//train with entire train set
		prob=readTrainData(prob, xValData.getValidationFullTrainingSet(), featureToRemove, -1, -1, -1);
		if(kernel.compareTo(LINEAR)==0)
			set_param_C((float)Math.pow(2, currBest[2]));
		//set_param_gamma((float)Math.pow(2, currBest.getVal3()));
		//set_param_coef0((float)Math.pow(2, currBest.getVal4()));
		//set_param_degree((int)currBest.getVal5());

		if(weight_label!=null)
		{
			set_param_weight_label(weight_label);
			set_param_weight(weight);
			set_param_nr_weight(weight_label.size());
		}

		model = svm.svm_train(prob, param);

		//test with test set
		testSetPredictionFilename=destDirectory+"testSetPrediction"+TXTFILEEXTENSION;
		fWriter.deleteFile(destDirectory, "testSetPrediction"+TXTFILEEXTENSION);
		actualVsPrediction=new ArrayList<double[]>();
		actualVsPrediction=readValidateData(actualVsPrediction, destDirectory, "testSetPrediction"+TXTFILEEXTENSION, 
				xValData.getTestSet(), true, featureToRemove, -1, -1, -1);
		//compute accuracy
		bestTestAccuracy=getAccuracy(actualVsPrediction).doubleValue();
		dataStream=TEST_SET_ACCURACY+":"+bestTestAccuracy+"\n";
		fWriter.concatenateToFileWithBufferedWriter(destDirectory, "testSetPrediction"+TXTFILEEXTENSION, dataStream);
	}

	//backward stepwise elimination selection
	public void BSEModelSelection(int numTuneLevel, boolean useWMC, int wmcWt, String kernel, String sourceDirectory, String destDirectory)
	{
		svm_print_interface svm_print_null=new svm_print_interface(){public void print(String s){}};
		double[] currBest=new double[3]; 
		//ArrayList<double[]> actualVsPrediction;
		//String dataStream, TEST_SET_ACCURACY=staticString.getTestSetAccuracy();
		ArrayList<String> featureConfirmedToRemove=new ArrayList<String>();
		System.out.println("BSEModelSelection");
		status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] Clearing accuracy table in database");
		DB.clearAccuracyTable();
		featureToRemove=new ArrayList<String>();
		svm.svm_set_print_string_function(svm_print_null);
		modelMessage=null;
		bestKernel=kernel;
		currBest=BSE_SVM(numTuneLevel, useWMC, wmcWt, kernel, sourceDirectory, destDirectory, featureConfirmedToRemove);
		status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] Reading entire training data");
		//train with entire train set
		prob=readTrainData(prob, xValData.getValidationFullTrainingSet(), featureToRemove, -1, -1, -1);
		if(kernel.compareTo(LINEAR)==0)
			set_param_C((float)Math.pow(2, currBest[2]));
		//set_param_gamma((float)Math.pow(2, currBest.getVal3()));
		//set_param_coef0((float)Math.pow(2, currBest.getVal4()));
		//set_param_degree((int)currBest.getVal5());

		if(weight_label!=null)
		{
			set_param_weight_label(weight_label);
			set_param_weight(weight);
			set_param_nr_weight(weight_label.size());
		}
		status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] training with entire training data");
		model = svm.svm_train(prob, param);
		status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] saving hallmark model");
		System.out.println(destDirectory);
		try {
			String filename=destDirectory+currHallmark+".dat";
			svm.svm_save_model(filename, model);
			System.out.println("bestPredictiveFeatures: "+bestPredictiveFeatures.toString());
			DB.addNewHallmarkCharacterization(currHallmark, filename, bestPredictiveFeatures, bestTargetWt, bestCExp, bestValidationAccuracy);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//test with test set
/*		actualVsPrediction=new ArrayList<double[]>();
		actualVsPrediction=readValidateData(actualVsPrediction, destDirectory, "testSetPrediction"+TXTFILEEXTENSION, 
				xValData.getTestSet(), true, featureToRemove);
		//compute accuracy
		bestTestAccuracy=getAccuracy(actualVsPrediction).doubleValue();
		dataStream=TEST_SET_ACCURACY+":"+bestTestAccuracy+"\n";
		fWriter.concatenateToFileWithBufferedWriter(destDirectory, "testSetPrediction"+TXTFILEEXTENSION, dataStream);
		*/
	}

	private double[] tuneLinear(int level, int target_wt, String sourceDirectory, String destDirectory, double[] currBest, boolean PRINT_BEST, 
			boolean PRINT, ArrayList<Integer> weight_label, ArrayList<Double> weight, double candidateProgress)
	{
		//currBest: <modelAccuracy><targetWt><bestCExp>
		double bestAccuracy=currBest[1], bestCExp=currBest[2];
		BigDecimal currModelAccuracy=BigDecimal.ZERO;
		int minVal=-10, maxVal=10, offset=2;
		double levelTuner=(double)2/maxVal;
		double thisCExp, multiplier=Math.pow(levelTuner, level);
		double[] thisBest=new double[3];
		String dataStream, NEWLINE=staticString.getNewline();

		//initialize thisBest with currBest;
		for(int i=0; i<thisBest.length; i++)
			thisBest[i]=currBest[i];

		int totalCount=maxVal-minVal;
		int counter=0;
		for(int CExp=minVal; CExp<=maxVal; CExp=CExp+offset)
		{
			float percentageComplete=(float)(100.0*counter/totalCount);
			counter=counter+offset;
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(2);
			status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["+df.format(percentageComplete)+"%]");
			thisCExp=multiplier*CExp+currBest[2];
			set_param_C((float)Math.pow(2, thisCExp));
			currModelAccuracy=train(false, sourceDirectory, destDirectory, featureToRemove, weight_label, weight, candidateProgress, percentageComplete);
			if(currModelAccuracy.compareTo(BigDecimal.valueOf(bestAccuracy))==1 || (currModelAccuracy.compareTo(BigDecimal.valueOf(bestAccuracy))==0 && target_wt>thisBest[1])) //i.e., currModelAccuracy>bestModelAccuracy
			{
				System.out.println("currModelAccuracy>bestModelAccuracy: currModelAccuracy="+currModelAccuracy.doubleValue()+" bestAccuracy="+bestAccuracy);
				bestAccuracy=currModelAccuracy.doubleValue();
				bestCExp=thisCExp;
				thisBest[0]=bestAccuracy;
				thisBest[1]=target_wt;
				thisBest[2]=bestCExp;
			}

			if(PRINT)
			{
				dataStream=thisBest[0]+NEWLINE+thisBest[2];
				System.out.println(dataStream);
				fWriter.writeToFileWithBufferedWriter(destDirectory+"Tune_"+level+"\\", "model_lvl_"+level+"_TargetWt_"+target_wt+"_CExp_"+CExp+TXTFILEEXTENSION, dataStream, true);
			}
		}
		if(PRINT_BEST)
		{
			dataStream=staticString.getAverageAccuracy()+":"+thisBest[0]+NEWLINE+
					"Target WMC:"+thisBest[1]+NEWLINE+
					staticString.getSvmCExponent()+":"+thisBest[2];
			if(featureToRemove.size()>0 || thisBest[1]!=0)
			{
				DB.addNewModelAccuracy(String.valueOf(thisBest[0]), (int)thisBest[1], String.valueOf(thisBest[2]), featureToRemove);
				fWriter.writeToFileWithBufferedWriter(destDirectory, "bestModelPrediction"+TXTFILEEXTENSION, dataStream, true);
			}
			else	
				fWriter.writeToFileWithBufferedWriter(destDirectory, "bestModelPrediction"+TXTFILEEXTENSION, dataStream, true);
		}
		return thisBest;
	}

	private BigDecimal getAccuracy(ArrayList<double[]> result)
	{
		int numTNTP=0;

		for(int i=0; i<result.size(); i++)
		{
			double[] resultItem=new double[4];
			resultItem=result.get(i);

			if(resultItem[2]==resultItem[3])
				numTNTP++;
		}
		return (BigDecimal.valueOf(numTNTP)).divide(BigDecimal.valueOf(result.size()), staticInteger.getScale(), BigDecimal.ROUND_UP);
	}
	
	private boolean isTarget(String node)
	{
		boolean isTarget=false;
		if(disease!=null && organism!=null)
			isTarget=DB.isTarget(disease, organism, node);
		else
			isTarget=targetNodes.contains(node);
		return isTarget;
	}
	//!!!!! modify
	private svm_problem readTrainData(svm_problem p, ArrayList<String> trainSet, ArrayList<String> featureToExclude, double candidateProgress, 
			double parameterProgress, double foldProgress)
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		p=new svm_problem();
		p.l=trainSet.size();
		p.y=new double[p.l];
		p.x=new svm_node[p.l][];
		int totalCount=p.l;
		for (int i=0; i<p.l; i++)
		{     
			float percentageComplete=(float)(100.0*i/(totalCount-1));
			if(candidateProgress==-1 && parameterProgress==-1 && foldProgress==-1)
				status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] reading training data ["+df.format(percentageComplete)+"%]");
			else
				status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["
						+df.format(parameterProgress)+"%] in each fold ["+df.format(foldProgress)+"%] reading train data ["+df.format(percentageComplete)+"%]");
			ArrayList<String> nodeFeatureValueList=DB.getFeatureValuesOfNode(fullFeatureSet, trainSet.get(i));
			p.x[i] = new svm_node[nodeFeatureValueList.size()-featureToExclude.size()];
			int k=0;
			for (int j=0; j<nodeFeatureValueList.size(); j++)
			{
				if(featureToExclude.contains(fullFeatureSet.get(j))==false)
				{
					svm_node node = new svm_node();
					node.index = j;
					node.value = Double.valueOf(nodeFeatureValueList.get(j));
					p.x[i][k++] = node;
				}
			}      
			if(isTarget(trainSet.get(i))==true)
				p.y[i] = 1;
			else
				p.y[i] = 0;
		}  
		return p;
	}
	//!!!!! modify
	private ArrayList<double[]> readValidateData(ArrayList<double[]> actualVsPrediction, String destDirectory, String outputFName, 
			ArrayList<String> testSet, boolean PRINT, ArrayList<String> featureToExclude, double candidateProgress, 
			double parameterProgress, double foldProgress)
	{
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		String dataStream="", ACTUAL=staticString.getActual(), NEWLINE=staticString.getNewline();
		String TAB=staticString.getTab(), PREDICTION=staticString.getPrediction();
		int totalCount=testSet.size();
		for (int i=0; i<testSet.size(); i++)
		{            
			float percentageComplete=(float)(100.0*i/(totalCount-1));
			if(candidateProgress==-1 && parameterProgress==-1 && foldProgress==-1)
				status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] reading validation data ["+df.format(percentageComplete)+"%]");
			else
				status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["
						+df.format(parameterProgress)+"%] in each fold ["+df.format(foldProgress)+"%] reading validation data ["+df.format(percentageComplete)+"%]");
			ArrayList<String> nodeFeatureValueList=DB.getFeatureValuesOfNode(fullFeatureSet, testSet.get(i));
			svm_node[] nodes = new svm_node[nodeFeatureValueList.size()-featureToExclude.size()];
			double[] actualVsPredictionItem=new double[4];
			int k=0;

			for (int j=0; j<nodeFeatureValueList.size(); j++)
			{
				if(featureToExclude.contains(fullFeatureSet.get(j))==false)
				{
					svm_node node = new svm_node();
					node.index = j;
					node.value = Double.valueOf(nodeFeatureValueList.get(j));
					nodes[k++] = node;
				}
			}

			double v1 = svm.svm_predict(model, nodes);
			int totalClasses = 2;       
			int[] labels = new int[totalClasses];
			svm.svm_get_labels(model,labels);

			double[] prob_estimates = new double[totalClasses];
			//double v2 = svm.svm_predict_probability(model, nodes, prob_estimates);

			//for (int j=0; j<totalClasses; j++){
			//    System.out.print("(" + labels[j] + ":" + prob_estimates[j] + ")");
			//}

			actualVsPredictionItem[0]=prob_estimates[0];
			actualVsPredictionItem[1]=prob_estimates[1];
			if(isTarget(testSet.get(i))==true)
				actualVsPredictionItem[2]=1;
			else
				actualVsPredictionItem[2]=0;
			actualVsPredictionItem[3]=v1;
			actualVsPrediction.add(actualVsPredictionItem);
			if(PRINT)
				dataStream=dataStream+labels[0]+":"+actualVsPredictionItem[0]+TAB+
				labels[1]+":"+actualVsPredictionItem[1]+TAB+
				ACTUAL+":"+actualVsPredictionItem[2]+TAB+
				PREDICTION+":"+actualVsPredictionItem[3]+NEWLINE;
		}

		if(PRINT)
			fWriter.concatenateToFileWithBufferedWriter(destDirectory, outputFName, dataStream);

		return actualVsPrediction;
	}

	private BigDecimal train(boolean PRINT, String sourceDirectory, String destDirectory, ArrayList<String> featureIndexToRemove, 
			ArrayList<Integer> weight_label, ArrayList<Double> weight, double candidateProgress, double parameterProgress)
	{
		String filename;
		ArrayList<double[]> actualVsPrediction;
		ArrayList<BigDecimal> accuracyList=new ArrayList<BigDecimal>();
		BigDecimal totalAccuracy=BigDecimal.ZERO, averageAccuracy;
		String dataStream="", COLON=":";
		String NEWLINE=staticString.getNewline(), AVERAGE_ACCURACY=staticString.getAverageAccuracy();
		String FOLD=staticString.getFold();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		
		int totalCount=NUM_OF_FOLDS;
		for (int f=0; f<totalCount; f++)
		{
			float percentageComplete=(float)(100.0*f/(totalCount-1));
			status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["+df.format(parameterProgress)+"%] in each fold ["+df.format(percentageComplete)+"%]");
			actualVsPrediction=new ArrayList<double[]>();
			if(PRINT)
			{
				dataStream=dataStream+FOLD+COLON+f+NEWLINE;
				filename="bestModelPrediction"+TXTFILEEXTENSION;
				fWriter.writeToFileWithBufferedWriter(destDirectory, filename, dataStream, true);
			}
			//read train data
			prob=readTrainData(prob, xValData.getValidationTrainingSet(f), featureIndexToRemove, candidateProgress, parameterProgress, percentageComplete);
			//set the weights and weight labels
			if(weight_label!=null)
			{
				System.out.println("set weight_label! weight_label:"+weight_label.toString()+" weight:"+weight.toString());
				set_param_weight_label(weight_label);
				set_param_nr_weight(weight_label.size());
				set_param_weight(weight);
				//System.out.println("setting param weight");
				//System.out.println(weight_label.toString());
			}
			status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["+df.format(parameterProgress)+"%] in each fold ["+df.format(percentageComplete)+"%] training model");
			model = svm.svm_train(prob, param);
			//read validate data
			actualVsPrediction=readValidateData(actualVsPrediction, sourceDirectory, "bestModelPrediction"+TXTFILEEXTENSION, 
					xValData.getValidationTestSet(f), PRINT, featureIndexToRemove, candidateProgress, parameterProgress, percentageComplete);
			status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["+df.format(parameterProgress)+"%] in each fold ["+df.format(percentageComplete)+"%] computing accuracy");
			//compute accuracy
			accuracyList.add(getAccuracy(actualVsPrediction));
		}
		status.setText("Characterizing hallmarks...for each hallmark ["+percentComplete+"%] for each candidate ["+df.format(candidateProgress)+"%] explore parameter ["+df.format(parameterProgress)+"%] updating average accuracy");
		for(int i=0; i<accuracyList.size(); i++)
			totalAccuracy=totalAccuracy.add(accuracyList.get(i));
		averageAccuracy=totalAccuracy.divide(BigDecimal.valueOf(accuracyList.size()), staticInteger.getScale(), BigDecimal.ROUND_UP);
		if(PRINT)
		{
			dataStream=AVERAGE_ACCURACY+COLON+averageAccuracy+NEWLINE;
			filename="bestModelPrediction"+TXTFILEEXTENSION;
			fWriter.concatenateToFileWithBufferedWriter(destDirectory, filename, dataStream);
		}
		return averageAccuracy;
	}
}