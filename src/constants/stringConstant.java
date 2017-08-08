package constants;


public class stringConstant 
{
	protected final static String virtualNodePrefix = "vNode_";
	protected final static String sccPrefix = "SCC_";
	protected final static String reactionPrefix = "reaction_";
	protected final static String na = "NA";
	protected final static String activator = "activator";
	protected final static String inhibitor = "inhibitor";
	protected final static String activation = "activation";
	protected final static String inhibition = "inhibition";
	protected final static String neither = "neither";
	protected final static String newCandidateValueFile = "newCandidateValue";
	protected final static String newOffTargetEffectsFile = "newOffTargetEffects";
	protected final static String activatorPrefix = "Ac_";
	protected final static String inhibitorPrefix = "In_";
	protected final static String activatorShortform = "ACT";
	protected final static String inhibitorShortform = "IN";
	protected final static String forward = " (forward)";
	protected final static String backward = " (backward)";
	protected final static String forwardShortform = "f";
	protected final static String backwardShortform = "b";
	protected final static String reactionShortform = "R";
	protected final static String linear = "Linear";
	protected final static String bse = "Backward stepwise elimination (BSE)";
	protected final static String wre = "Wilcoxon-ROC elimination (WRE)";
	protected final static String wreBse = "WRE-BSE (hybrid)";
	protected final static String none = "None";
	protected final static String cancel = "Cancel";
	protected final static String done = "Done";
	protected final static String resetToDefaults = "Reset to defaults";
	protected final static String inDegree = "In degree centrality";
	protected final static String outDegree = "Out degree centrality";
	protected final static String totalDegree = "Total degree centrality";
	protected final static String eigenvector = "Eigenvector centrality";
	protected final static String closeness = "Closeness centrality";
	protected final static String eccentricity = "Eccentricity centrality";
	protected final static String betweenness = "Betweenness centrality";
	protected final static String bridgingCoeff = "Bridging coefficient";
	protected final static String bridgingCentrality = "Bridging centrality";
	protected final static String undirClustering = "Undirected clustering coefficient";
	protected final static String inClustering = "In clustering coefficient";
	protected final static String outClustering = "Out clustering coefficient";
	protected final static String midClustering = "Middleman clustering coefficient";
	protected final static String cycClustering = "Cycle clustering coefficient";
	protected final static String proximityPrestige = "Proximity prestige";
	protected final static String tde = "Target downstream effect";
	protected final static String DB_inDegree = "in_degree";
	protected final static String DB_outDegree = "out_degree";
	protected final static String DB_totalDegree = "total_degree";
	protected final static String DB_eigenvector = "eigenvector";
	protected final static String DB_closeness = "closeness";
	protected final static String DB_eccentricity = "eccentricity";
	protected final static String DB_betweenness = "betweenness";
	protected final static String DB_bridgingCoeff = "bridging_coefficient";
	protected final static String DB_bridgingCentrality = "bridging_centrality";
	protected final static String DB_undirClustering = "undirected_clustering_coefficient";
	protected final static String DB_inClustering = "in_clustering_coefficient";
	protected final static String DB_outClustering = "out_clustering_coefficient";
	protected final static String DB_midClustering = "middleman_clustering_coefficient";
	protected final static String DB_cycClustering = "cycle_clustering_coefficient";
	protected final static String DB_proximityPrestige = "proximity_prestige";
	protected final static String DB_tde = "target_downstream_effect";
	protected final static String DB_probCoeff = "probability_coefficient";
	
	protected final static String DB_proliferation = "PROLIFERATION";
	protected final static String DB_growth_repressor = "GROWTH_REPRESSOR";
	protected final static String DB_apoptosis = "APOPTOSIS";
	protected final static String DB_replicative_immortality = "REPLICATIVE_IMMORTALITY";
	protected final static String DB_angiogenesis = "ANGIOGENESIS";
	protected final static String DB_metastasis = "METASTASIS";
	protected final static String DB_metabolism = "METABOLISM";
	protected final static String DB_immune_destruction = "IMMUNE_DESTRUCTION";
	protected final static String DB_genome_instability = "GENOME_INSTABILITY";
	protected final static String DB_tumor_promoting_inflammation = "TUMOR_PROMOTING_INFLAMMATION";
	
	protected final static String DB_GO = "GO";
	protected final static String DB_Entrez = "Entrez";
	protected final static String DB_others = "Others";
	protected final static String DB_molecularFunction = "molecular function";
	protected final static String DB_localization = "localization";
	protected final static String DB_biologicalProcess = "biological process";
	protected final static String DB_KEGG_pathway = "KEGG pathway";
	
	protected final static String DB_edge_pos="Pos";
	protected final static String DB_edge_neg="Neg";
	protected final static String DB_edge_phy="Phy";
	
	protected final static String GUI_proliferation = "1:Promote proliferation";
	protected final static String GUI_growth_repressor = "2:Evade growth repressor";
	protected final static String GUI_apoptosis = "3:Resist cell death";
	protected final static String GUI_replicative_immortality = "4:Enable replicative immortality";
	protected final static String GUI_angiogenesis = "5:Induce angiogenesis";
	protected final static String GUI_metastasis = "6:Activate invasion & metastasis";
	protected final static String GUI_metabolism = "7:Reprogram energy metabolism";
	protected final static String GUI_immune_destruction = "8:Evade immune destruction";
	protected final static String GUI_genome_instability = "9:Genome instability";
	protected final static String GUI_tumor_promoting_inflammation = "10:Tumor promoting inflammation";
	
	protected final static String gephi_hallmarkColorList = "HallmarkColorList";
	protected final static String gephi_hallmarkColorListDefault = "-1";
	protected final static String gephi_weight = "Weight";
	protected final static String gephi_small = "small";
	protected final static String gephi_medium = "medium";
	protected final static String gephi_large = "large";
	protected final static String gephi_extra_large = "extra_large";
	
	protected final static String testSetAccuracy = "test set accuracy";
	protected final static String averageAccuracy = "average accuracy";
	protected final static String actual = "actual";
	protected final static String prediction = "prediction";
	protected final static String svmTrainFull = "trainFull";
	protected final static String svmTest = "test";
	protected final static String svmTrain = "train";
	protected final static String svmValidate = "validate";
	protected final static String svmCExponent = "C exponent";
	protected final static String textFileExtension = ".txt";
	protected final static String fold = "fold";
	
	protected final static String node_only = "Node only";
	protected final static String induced_1_hop = "Induced (1-hop) graph";
	protected final static String overlay = "Overlay current graph";
	protected final static String induced = "Induced graph";
	
/* Symbols and punctuation*/	
	protected final static String newline = "\r\n";
	protected final static String tab = "\t";
	protected final static String comma = ",";
	
	public stringConstant() {
		
	}
	
	public String getVirtualNodePrefix() {
		return virtualNodePrefix;
	}
	public String getSccPrefix() {
		return sccPrefix;
	}
	public String getNA() {
		return na;
	}
	public String getActivator() {
		return activator;
	}
	public String getInhibitor() {
		return inhibitor;
	}
	public String getActivation() {
		return activation;
	}
	public String getInhibition() {
		return inhibition;
	}
	public String getNeither() {
		return neither;
	}
	public String getNewCandidateValueFile() {
		return newCandidateValueFile;
	}
	public String getNewOffTargetEffectsFile() {
		return newOffTargetEffectsFile;
	}
	public String getActivatorPrefix() {
		return activatorPrefix;
	}
	public String getInhibitorPrefix() {
		return inhibitorPrefix;
	}
	public String getActivatorShortform() {
		return activatorShortform;
	}
	public String getInhibitorShortform() {
		return inhibitorShortform;
	}
	public String getForward() {
		return forward;
	}
	public String getBackward() {
		return backward;
	}
	public String getForwardShortform() {
		return forwardShortform;
	}
	public String getBackwardShortform() {
		return backwardShortform;
	}
	public String getReactionPrefix() {
		return reactionPrefix;
	}
	public String getReactionShortform() {
		return reactionShortform;
	}
	
	public String getNewline() {
		return newline;
	}
	public String getTab() {
		return tab;
	}
	public String getComma() {
		return comma;
	}
	
	public String getLinear() {
		return linear;
	}
	public String getNone() {
		return none;
	}
	public String getBSE() {
		return bse;
	}
	public String getWRE() {
		return wre;
	}
	public String getWREBSE() {
		return wreBse;
	}
	public String getCancel() {
		return cancel;
	}
	public String getDone() {
		return done;
	}
	public String getResetToDefaults() {
		return resetToDefaults;
	}
	public String getInDegree() {
		return inDegree;
	}
	public String getOutDegree() {
		return outDegree;
	}
	public String getTotalDegree() {
		return totalDegree;
	}
	public String getEigenvector() {
		return eigenvector;
	}
	public String getCloseness() {
		return closeness;
	}
	public String getEccentricity() {
		return eccentricity;
	}
	public String getBetweenness() {
		return betweenness;
	}
	public String getBridgingCoeff() {
		return bridgingCoeff;
	}
	public String getBridgingCentrality() {
		return bridgingCentrality;
	}
	public String getUndirClustering() {
		return undirClustering;
	}
	public String getInClustering() {
		return inClustering;
	}
	public String getOutClustering() {
		return outClustering;
	}
	public String getMidClustering() {
		return midClustering;
	}
	public String getCycClustering() {
		return cycClustering;
	}
	public String getProximityPrestige() {
		return proximityPrestige;
	}
	public String getTDE() {
		return tde;
	}
	
	public String getDBInDegree() {
		return DB_inDegree;
	}
	public String getDBOutDegree() {
		return DB_outDegree;
	}
	public String getDBTotalDegree() {
		return DB_totalDegree;
	}
	public String getDBEigenvector() {
		return DB_eigenvector;
	}
	public String getDBCloseness() {
		return DB_closeness;
	}
	public String getDBEccentricity() {
		return DB_eccentricity;
	}
	public String getDBBetweenness() {
		return DB_betweenness;
	}
	public String getDBBridgingCoeff() {
		return DB_bridgingCoeff;
	}
	public String getDBBridgingCentrality() {
		return DB_bridgingCentrality;
	}
	public String getDBUndirClustering() {
		return DB_undirClustering;
	}
	public String getDBInClustering() {
		return DB_inClustering;
	}
	public String getDBOutClustering() {
		return DB_outClustering;
	}
	public String getDBMidClustering() {
		return DB_midClustering;
	}
	public String getDBCycClustering() {
		return DB_cycClustering;
	}
	public String getDBProximityPrestige() {
		return DB_proximityPrestige;
	}
	public String getDBTDE() {
		return DB_tde;
	}
	public String getDBProbCoeff() {
		return DB_probCoeff;
	}
	public String getDBProliferation() {
		return DB_proliferation;
	}
	public String getDBGrowthRepressor() {
		return DB_growth_repressor;
	}
	public String getDBApoptosis() {
		return DB_apoptosis;
	}
	public String getDBReplicativeImmortality() {
		return DB_replicative_immortality;
	}
	public String getDBAngiogenesis() {
		return DB_angiogenesis;
	}
	public String getDBMetastasis() {
		return DB_metastasis;
	}
	public String getDBMetabolism() {
		return DB_metabolism;
	}
	public String getDBImmuneDestruction() {
		return DB_immune_destruction;
	}
	public String getDBGenomeInstability() {
		return DB_genome_instability;
	}
	public String getDBTumorPromotingInflammation() {
		return DB_tumor_promoting_inflammation;
	}
	public String getDBGO() {
		return DB_GO;
	}
	public String getDBEntrez() {
		return DB_Entrez;
	}
	public String getDBOthers() {
		return DB_others;
	}
	public String getDBMolecularFunction() {
		return DB_molecularFunction;
	}
	public String getDBLocalization() {
		return DB_localization;
	}
	public String getDBBiologicalProcess() {
		return DB_biologicalProcess;
	}
	public String getDBKEGGPathway() {
		return DB_KEGG_pathway;
	}
	public String getDBEdgePos(){
		return DB_edge_pos;
	}
	public String getDBEdgeNeg(){
		return DB_edge_neg;
	}
	public String getDBEdgePhy(){
		return DB_edge_phy;
	}
	
	public String getGUIProliferation() {
		return GUI_proliferation;
	}
	public String getGUIGrowthRepressor() {
		return GUI_growth_repressor;
	}
	public String getGUIApoptosis() {
		return GUI_apoptosis;
	}
	public String getGUIReplicativeImmortality() {
		return GUI_replicative_immortality;
	}
	public String getGUIAngiogenesis() {
		return GUI_angiogenesis;
	}
	public String getGUIMetastasis() {
		return GUI_metastasis;
	}
	public String getGUIMetabolism() {
		return GUI_metabolism;
	}
	public String getGUIImmuneDestruction() {
		return GUI_immune_destruction;
	}
	public String getGUIGenomeInstability() {
		return GUI_genome_instability;
	}
	public String getGUITumorPromotingInflammation() {
		return GUI_tumor_promoting_inflammation;
	}
	
	public String getGephiHallmarkColorList() {
		return gephi_hallmarkColorList;
	}
	public String getGephiHallmarkColorListDefault() {
		return gephi_hallmarkColorListDefault;
	}
	public String getGephiWeight() {
		return gephi_weight;
	}
	public String getGephiSmall() {
		return gephi_small;
	}
	public String getGephiMedium() {
		return gephi_medium;
	}
	public String getGephiLarge() {
		return gephi_large;
	}
	public String getGephiExtraLarge() {
		return gephi_extra_large;
	}
	
	public String getTestSetAccuracy() {
		return testSetAccuracy;
	}
	public String getAverageAccuracy() {
		return averageAccuracy;
	}
	public String getActual() {
		return actual;
	}
	public String getPrediction() {
		return prediction;
	}
	public String getSvmTrainFull() {
		return svmTrainFull;
	}
	public String getSvmTrain() {
		return svmTrain;
	}
	public String getSvmTest() {
		return svmTest;
	}
	public String getSvmValidate() {
		return svmValidate;
	}
	public String getTextFileExtension() {
		return textFileExtension;
	}
	public String getSvmCExponent() {
		return svmCExponent;
	}
	public String getFold() {
		return fold;
	}
	
	public String getNodeOnly() {
		return node_only;
	}
	public String getInduced1HopGraph() {
		return induced_1_hop;
	}
	public String getInducedGraph() {
		return induced;
	}
	public String getOverlayCurrentGraph() {
		return overlay;
	}
}

