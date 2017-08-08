package hallmark;


public class allHallmark {

	private hallmark proliferation;
	private hallmark growth_repressor;
	private hallmark apoptosis;
	private hallmark replicative_immortality;
	private hallmark angiogenesis;
	private hallmark metastasis;
	private hallmark metabolism;
	private hallmark immune_destruction;
	private hallmark genome_instability;
	private hallmark tumor_promoting_inflammation;
	
	public allHallmark() {
		proliferation=new hallmark();
		growth_repressor=new hallmark();
		apoptosis=new hallmark();
		replicative_immortality=new hallmark();
		angiogenesis=new hallmark();
		metastasis=new hallmark();
		metabolism=new hallmark();
		immune_destruction=new hallmark();
		genome_instability=new hallmark();
		tumor_promoting_inflammation=new hallmark();
	}

	public hallmark getProliferation() {
		return proliferation;
	}

	public hallmark getGrowthRepressor() {
		return growth_repressor;
	}
	
	public hallmark getApoptosis() {
		return apoptosis;
	}
	
	public hallmark getReplicativeImmortality() {
		return replicative_immortality;
	}
	
	public hallmark getAngiogenesis() {
		return angiogenesis;
	}
	
	public hallmark getMetastasis() {
		return metastasis;
	}

	public hallmark getMetabolism() {
		return metabolism;
	}
	
	public hallmark getImmuneDestruction() {
		return immune_destruction;
	}
	
	public hallmark getGenomeInstability() {
		return genome_instability;
	}
	
	public hallmark getTumorPromotingInflammation() {
		return tumor_promoting_inflammation;
	}
}