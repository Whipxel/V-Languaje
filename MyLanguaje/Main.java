class Main{

	public static void main(String Args[]) {
		Lexer lex = new vLexer();
		Parser par = new Parser();
		lex.lex("test.v",true,true);
		//System.out.println(lex.getTokens().get(1).getData());
		par.parse(lex.getTokens());
	}
}
