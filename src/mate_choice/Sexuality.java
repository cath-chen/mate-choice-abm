package mate_choice;

public enum Sexuality {
	LESBIAN (1.0),
    GAY (2.0),
    STRAIGHT_M (3.0),
    STRAIGHT_F (4.0),
    BI_M (5.0),
    BI_F (6.0);
    
    
	private final double id;
	Sexuality(double id){
		this.id =id;
	}
	
	public double id() {
		return id;
	}
}
