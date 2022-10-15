// Generated from D:/UPB/CPL/Idea/Types-demo\CPLangLexer.g4 by ANTLR 4.8
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CPLangLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.8", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		IF=1, THEN=2, ELSE=3, FI=4, COLON=5, SEMI=6, LBRACE=7, RBRACE=8, TYPE=9, 
		PLUS=10, INT=11, ID=12, REAL=13, STRING=14, BLOCK_COMMENT=15, WS=16;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "THEN", "ELSE", "FI", "COLON", "SEMI", "LBRACE", "RBRACE", "TYPE", 
			"PLUS", "DIGIT", "INT", "LETTER", "ID", "DIGITS", "FRACTION", "EXPONENT", 
			"REAL", "STRING", "BLOCK_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'then'", "'else'", "'fi'", "':'", "';'", "'{'", "'}'", 
			null, "'+'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "THEN", "ELSE", "FI", "COLON", "SEMI", "LBRACE", "RBRACE", 
			"TYPE", "PLUS", "INT", "ID", "REAL", "STRING", "BLOCK_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public CPLangLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "CPLangLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 18:
			STRING_action((RuleContext)_localctx, actionIndex);
			break;
		}
	}
	private void STRING_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0:
			 System.out.println("there are no strings in CPLang, but shhh.."); 
			break;
		}
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\22\u00a1\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\3\2\3\2\3\2\3\3\3\3\3\3"+
		"\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3"+
		"\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\5\nQ\n\n\3\13\3\13\3\f"+
		"\3\f\3\r\6\rX\n\r\r\r\16\rY\3\16\3\16\3\17\3\17\5\17`\n\17\3\17\3\17\3"+
		"\17\7\17e\n\17\f\17\16\17h\13\17\3\20\6\20k\n\20\r\20\16\20l\3\21\3\21"+
		"\5\21q\n\21\5\21s\n\21\3\22\3\22\5\22w\n\22\3\22\5\22z\n\22\3\23\3\23"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\7\24\u0084\n\24\f\24\16\24\u0087\13\24"+
		"\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\7\25\u0091\n\25\f\25\16\25\u0094"+
		"\13\25\3\25\3\25\3\25\3\25\3\25\3\26\6\26\u009c\n\26\r\26\16\26\u009d"+
		"\3\26\3\26\4\u0085\u0092\2\27\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13"+
		"\25\f\27\2\31\r\33\2\35\16\37\2!\2#\2%\17\'\20)\21+\22\3\2\6\3\2\62;\4"+
		"\2C\\c|\4\2--//\5\2\13\f\17\17\"\"\2\u00ac\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\31\3\2\2\2\2\35\3\2\2\2\2%\3\2\2\2\2\'"+
		"\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\3-\3\2\2\2\5\60\3\2\2\2\7\65\3\2\2\2\t"+
		":\3\2\2\2\13=\3\2\2\2\r?\3\2\2\2\17A\3\2\2\2\21C\3\2\2\2\23P\3\2\2\2\25"+
		"R\3\2\2\2\27T\3\2\2\2\31W\3\2\2\2\33[\3\2\2\2\35_\3\2\2\2\37j\3\2\2\2"+
		"!r\3\2\2\2#y\3\2\2\2%{\3\2\2\2\'\177\3\2\2\2)\u008b\3\2\2\2+\u009b\3\2"+
		"\2\2-.\7k\2\2./\7h\2\2/\4\3\2\2\2\60\61\7v\2\2\61\62\7j\2\2\62\63\7g\2"+
		"\2\63\64\7p\2\2\64\6\3\2\2\2\65\66\7g\2\2\66\67\7n\2\2\678\7u\2\289\7"+
		"g\2\29\b\3\2\2\2:;\7h\2\2;<\7k\2\2<\n\3\2\2\2=>\7<\2\2>\f\3\2\2\2?@\7"+
		"=\2\2@\16\3\2\2\2AB\7}\2\2B\20\3\2\2\2CD\7\177\2\2D\22\3\2\2\2EF\7K\2"+
		"\2FG\7p\2\2GQ\7v\2\2HI\7D\2\2IJ\7q\2\2JK\7q\2\2KQ\7n\2\2LM\7E\2\2MN\7"+
		"j\2\2NO\7c\2\2OQ\7t\2\2PE\3\2\2\2PH\3\2\2\2PL\3\2\2\2Q\24\3\2\2\2RS\7"+
		"-\2\2S\26\3\2\2\2TU\t\2\2\2U\30\3\2\2\2VX\5\27\f\2WV\3\2\2\2XY\3\2\2\2"+
		"YW\3\2\2\2YZ\3\2\2\2Z\32\3\2\2\2[\\\t\3\2\2\\\34\3\2\2\2]`\5\33\16\2^"+
		"`\7a\2\2_]\3\2\2\2_^\3\2\2\2`f\3\2\2\2ae\5\33\16\2be\7a\2\2ce\5\27\f\2"+
		"da\3\2\2\2db\3\2\2\2dc\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\36\3\2\2"+
		"\2hf\3\2\2\2ik\5\27\f\2ji\3\2\2\2kl\3\2\2\2lj\3\2\2\2lm\3\2\2\2m \3\2"+
		"\2\2np\7\60\2\2oq\5\37\20\2po\3\2\2\2pq\3\2\2\2qs\3\2\2\2rn\3\2\2\2rs"+
		"\3\2\2\2s\"\3\2\2\2tv\7g\2\2uw\t\4\2\2vu\3\2\2\2vw\3\2\2\2wx\3\2\2\2x"+
		"z\5\37\20\2yt\3\2\2\2yz\3\2\2\2z$\3\2\2\2{|\5\37\20\2|}\5!\21\2}~\5#\22"+
		"\2~&\3\2\2\2\177\u0085\7$\2\2\u0080\u0081\7^\2\2\u0081\u0084\7$\2\2\u0082"+
		"\u0084\13\2\2\2\u0083\u0080\3\2\2\2\u0083\u0082\3\2\2\2\u0084\u0087\3"+
		"\2\2\2\u0085\u0086\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u0088\3\2\2\2\u0087"+
		"\u0085\3\2\2\2\u0088\u0089\7$\2\2\u0089\u008a\b\24\2\2\u008a(\3\2\2\2"+
		"\u008b\u008c\7\61\2\2\u008c\u008d\7,\2\2\u008d\u0092\3\2\2\2\u008e\u0091"+
		"\5)\25\2\u008f\u0091\13\2\2\2\u0090\u008e\3\2\2\2\u0090\u008f\3\2\2\2"+
		"\u0091\u0094\3\2\2\2\u0092\u0093\3\2\2\2\u0092\u0090\3\2\2\2\u0093\u0095"+
		"\3\2\2\2\u0094\u0092\3\2\2\2\u0095\u0096\7,\2\2\u0096\u0097\7\61\2\2\u0097"+
		"\u0098\3\2\2\2\u0098\u0099\b\25\3\2\u0099*\3\2\2\2\u009a\u009c\t\5\2\2"+
		"\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009b\3\2\2\2\u009d\u009e"+
		"\3\2\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\b\26\3\2\u00a0,\3\2\2\2\22\2"+
		"PY_dflprvy\u0083\u0085\u0090\u0092\u009d\4\3\24\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}