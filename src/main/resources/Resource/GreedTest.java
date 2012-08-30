	static void runTestcase(int cs) {
		switch (cs) {
			// Your custom testcase goes here
			case -1:
				break;

${<foreach Examples e}
			case ${e.Num}: {
${<foreach e.Input in}
${<if !in.Param.Type.Array}
				${in.Param.Type.Primitive} ${in.Param.Name} = ${in};
${<else}
				${in.Param.Type.Primitive} ${in.Param.Name}[] = new ${in.Param.Type} {${foreach in.ValueList v ,}
					${v}${end}
				};
${<end}
${<end}
${<if !e.Output.Param.Type.Array}
				${e.Output.Param.Type.Primitive} expected = ${e.Output};
${<else}
				${e.Output.Param.Type.Primitive} expected[] = new ${e.Output.Param.Type} {${foreach e.Output.ValueList v ,}
					${v}${end}
				};
${<end}
				doTest(${foreach e.Input in , }${in.Param.Name}${end}, expected, cs);
				break;
			}
${<end}
			default: break;
		}
	}

	static void doTest(${Method.Params}, ${Method.ReturnType} expected, int caseNo) {
${<if RecordRuntime}
		long startTime = System.currentTimeMillis();
${<end}
		Throwable exception = null;
		${ClassName} instance = new ${ClassName}();
		${Method.ReturnType} result = ${Method.ReturnType;ZeroValue};
		try {
			result = instance.${Method.Name}(${foreach Method.Params par , }${par.Name}${end});
		}
		catch (Throwable e) { exception = e; }
${<if RecordRuntime}
		double elapsed = (System.currentTimeMillis() - startTime) / 1000.0;
${<end}

		nAll++;
		boolean isExample = caseNo >= 0 && caseNo < nExample;
		if (isExample)
			System.err.print(String.format("   Example #%d ...", caseNo));
		else
			System.err.print(String.format("   Custom  #%d ...", nCustom++));

		if (exception != null) {
			System.err.println("RUNTIME ERROR!");
			exception.printStackTrace();
		}
		else if (${if Method.ReturnType.Array}equals(result, expected)${else}${if Method.ReturnType.String}expected.equals(result)${else}${if Method.ReturnType.RealNumber}Math.abs(result - expected) < 1e-9${else}result == expected${end}${end}${end}) {
			System.err.println("PASSED! "${if RecordTime} + String.format("(%.2f seconds)", elapsed)${end});
			nPassed++;
		}
		else {
			System.err.println("FAILED! "${if RecordTime} + String.format("(%.2f seconds)", elapsed)${end});
			System.err.println("            Expected: " + ${if Method.ReturnType.Array}toString(expected)${else}expected${end});
			System.err.println("            Received: " + ${if Method.ReturnType.Array}toString(result)${else}result${end});
		}
	}

	static int nExample = ${NumOfExamples}, nCustom = 0;
	static int nAll = 0, nPassed = 0;

${<if ReturnsArray}
	static boolean equals(${Method.ReturnType} a, ${Method.ReturnType} b) {
		if (a.length != b.length) return false;
		for (int i = 0; i < a.length; ++i) if (${if Method.ReturnType.String}a[i] == null || b[i] == null || !a[i].equals(b[i])${else}${if Method.ReturnType.RealNumber}Math.abs(a[i] - b[i]) > 1e-9${else}a[i] != b[i]${end}${end}) return false;
		return true;
	}

	static String toString(${Method.ReturnType} arr) {
		StringBuffer sb = new StringBuffer();
		sb.append("[ ");
		for (int i = 0; i < arr.length; ++i) {
			if (i > 0) sb.append(", ");
			sb.append(arr[i]);
		}
		return sb.toString() + " ]";
	}
${<end}
	public static void main(String[] args){
		System.err.println("${Problem.Name} (${Problem.Score} Points)");
		System.err.println();
		if (args.length == 0)
			for (int i = 0; i < nExample; ++i) runTestcase(i);
		else
			for (int i = 0; i < args.length; ++i) runTestcase(Integer.parseInt(args[i]));
		System.err.println(String.format("%nPassed %d/%d", nPassed, nAll));
	}
