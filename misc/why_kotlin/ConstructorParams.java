public class ConstructorParams {
	private final String val1;
	private int var1;
	
	public ConstructorParams(String val1, int var1) {
		this.val1 = val1;
		this.var1 = var1;
	}
	
	public int getVar1() {
		return var1;
	}
	
	public String getVal1() {
		return val1;
	}
	
	public void setVar1(int var1) {
		this.var1 = var1;
	}
}