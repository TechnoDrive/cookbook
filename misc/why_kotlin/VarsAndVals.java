public class VarsAndVals {
	private int var1 = 0;
	private String var2 = "variable string";
	
	private final int val1 = 0;
	private final String val2 = "value string";
	
	public int getVar1() {
		return var1;
	}
	
	public void setVar1(int var1) {
		this.var1 = var1;
	}
	
	private String getVar2() {
		return var2;
	}
	
	private void setVar2(String string) {
		this.var2 = string;
	}
	
	public int getVal1() {
		return val1;
	}
	
	private String getVar2() {
		return var2;
	}
	
	public void getting() {
		final int local1 = getVar1();
		final String local2 = getVar2();
		
		final int local3 = getVal1();
		final String local4 = getVal2();
	}
	
	public void setting() {
		setVar1(100);
		setVar2("new value");
	}
}