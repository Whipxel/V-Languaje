import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Lexer {
	protected ArrayList<TokenType> tokenTypes = new ArrayList<TokenType>();
	protected ArrayList<Token> tokens = new ArrayList<Token>();

	abstract protected void initializeTokenTypes();

	private String readSourceFile(String fileName) {
		String input = "";

		try {
			FileReader reader = new FileReader(fileName);
			int character;

			while ((character = reader.read()) != -1)
				input += (char) character;

			reader.close();
			return input;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	abstract protected boolean isIdentifier(String s);
	
	public ArrayList<Token> getTokens() {
		return tokens;
	}

	public String lex(String input, boolean inputIsFile, boolean debug) {
		initializeTokenTypes();

		StringBuffer tokenPatternsBuffer = new StringBuffer();

		for (TokenType tt : tokenTypes)
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tt.getName(), tt.getPattern()));

		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

		if (inputIsFile)
			input = readSourceFile(input);
		
		if (input == null)
			return "No source file or empty file";

		Matcher matcher = tokenPatterns.matcher(input);

		while (matcher.find()) {
			for (TokenType tt: tokenTypes) {
				if (matcher.group("SPACE") != null)
					continue;
				else if (matcher.group(tt.getName()) != null) {
					if (tt.getName().equals("ERROR")) {
						return "Invalid token: " + matcher.group(tt.getName());
					}

					tokens.add(new Token(tt, matcher.group(tt.getName())));

					break;
				}
			}
		}
		
		if (debug) {
			System.out.println("********** Tokens **********");
			System.out.println();
			
			for (Token t: tokens)
				System.out.println("(" + t.getType().getName() + "," + t.getData() + ")");
		
			System.out.println();
		}
		
		return "";
	}
}
