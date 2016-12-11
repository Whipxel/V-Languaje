public class Token {
	private TokenType type;
	private String data;

	public Token(TokenType type, String data) {
		this.type = type;
		this.data = data;
	}

	public TokenType getType() {
		return type;
	}

	public String getData() {
		return data;
	}

	public String toString() {
		return String.format("(%s  \"%s\")", type.getName(), data);
	}
}
