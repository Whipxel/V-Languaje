public class TokenType {
	private String name;
	private String pattern;

	public TokenType(String name, String pattern) {
		this.name = name;
		this.pattern = pattern;
	}

	public String getName() {
		return name;
	}

	public String getPattern() {
		return pattern;
	}
}
