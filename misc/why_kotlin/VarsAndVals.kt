class VarsAndVals {
	var var1 = 0
	private var var2 = "variable string"

	val val1 = 0
	private val val2 = "value string"

	fun getting() {
		val local1 = var1
		val local2 = var2

		val local3 = val1
		val local4 = val2
	}

	fun setting() {
		var1 = 100
		var2 = "new value"
	}
}