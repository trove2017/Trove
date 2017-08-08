package constants;


public class integerConstant 
{
	/* SVM settings */	
	protected final static Integer defaultWMCNumSteps = 10;
	protected final static Integer defaultXValidationFolds = 10;
	protected final static int SCALE = 20;
	
	public integerConstant() {
		
	}
	
	public int getDefaultWMCNumSteps() {
		return defaultWMCNumSteps;
	}
	public int getDefaultXValidationFolds() {
		return defaultXValidationFolds;
	}
	public int getScale() {
		return SCALE;
	}
}