package greed.transform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CodeByLine {
	private List<String> lines;

	public CodeByLine() {
		lines = new ArrayList<String>();
	}

	public List<String> getLines() {
		return lines;
	}

	public static CodeByLine fromInputStream(InputStream stream) throws IOException {
		return innerCreate(new BufferedReader(new InputStreamReader(stream)));
	}

	public static CodeByLine fromString(String code) throws IOException {
		return innerCreate(new BufferedReader(new StringReader(code)));
	}

	private static CodeByLine innerCreate(BufferedReader reader) throws IOException {
		CodeByLine cbl = new CodeByLine();
		String l;
		while ((l = reader.readLine()) != null)
			cbl.getLines().add(l);
		reader.close();
		return cbl;
	}
}
