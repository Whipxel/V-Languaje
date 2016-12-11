public class vLexer extends Lexer {
	protected void initializeTokenTypes() {
		tokenTypes.add(new TokenType("INICIO", "START"));
		tokenTypes.add(new TokenType("IF", "IF"));
		tokenTypes.add(new TokenType("ENDIF", "ENDIF"));
		tokenTypes.add(new TokenType("WHILE", "WHILE"));
		tokenTypes.add(new TokenType("ENDWHILE", "ENDWHILE"));
		tokenTypes.add(new TokenType("FIN", "END"));
		tokenTypes.add(new TokenType("INPUT", "INPUT"));
		tokenTypes.add(new TokenType("PRINT", "PRINT"));
		tokenTypes.add(new TokenType("STRING", "\".*\""));
		tokenTypes.add(new TokenType("NUMERO", "-?[0-9]+(\\.([0-9]+))?"));
		tokenTypes.add(new TokenType("OPERADOR", "[*|/|+|-]"));
		tokenTypes.add(new TokenType("ASSIGN", "="));
		tokenTypes.add(new TokenType("COMPARADOR", "[<|>|#]"));
		tokenTypes.add(new TokenType("VARIABLE", "[a-zA-Z][a-z-A-Z0-9]*"));
		tokenTypes.add(new TokenType("SPACE", "[ \t\f\r\n]+"));
		tokenTypes.add(new TokenType("ERROR", ".+"));
	}

	protected boolean isIdentifier(String s) {
		if (s.equals("IDENTIFIER"))
			return true;

		return false;
	}
}

