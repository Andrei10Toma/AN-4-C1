// Generated from D:/UPB/CPL/Idea/Symbols-demo\CPLangLexer.g4 by ANTLR 4.8
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
		IF=1, THEN=2, ELSE=3, FI=4, SEMI=5, LBRACE=6, RBRACE=7, TYPE=8, PLUS=9, 
		INT=10, ID=11, REAL=12, STRING=13, BLOCK_COMMENT=14, WS=15;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"IF", "THEN", "ELSE", "FI", "SEMI", "LBRACE", "RBRACE", "TYPE", "PLUS", 
			"DIGIT", "INT", "LETTER", "ID", "DIGITS", "FRACTION", "EXPONENT", "REAL", 
			"STRING", "BLOCK_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'if'", "'then'", "'else'", "'fi'", "';'", "'{'", "'}'", "'Int'", 
			"'+'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "IF", "THEN", "ELSE", "FI", "SEMI", "LBRACE", "RBRACE", "TYPE", 
			"PLUS", "INT", "ID", "REAL", "STRING", "BLOCK_COMMENT", "WS"
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
		case 17:
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\21\u0094\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t"+
		"\3\n\3\n\3\13\3\13\3\f\6\fK\n\f\r\f\16\fL\3\r\3\r\3\16\3\16\5\16S\n\16"+
		"\3\16\3\16\3\16\7\16X\n\16\f\16\16\16[\13\16\3\17\6\17^\n\17\r\17\16\17"+
		"_\3\20\3\20\5\20d\n\20\5\20f\n\20\3\21\3\21\5\21j\n\21\3\21\5\21m\n\21"+
		"\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\7\23w\n\23\f\23\16\23z\13\23"+
		"\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\7\24\u0084\n\24\f\24\16\24\u0087"+
		"\13\24\3\24\3\24\3\24\3\24\3\24\3\25\6\25\u008f\n\25\r\25\16\25\u0090"+
		"\3\25\3\25\4x\u0085\2\26\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\2"+
		"\27\f\31\2\33\r\35\2\37\2!\2#\16%\17\'\20)\21\3\2\6\3\2\62;\4\2C\\c|\4"+
		"\2--//\5\2\13\f\17\17\"\"\2\u009d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2"+
		"\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3"+
		"\2\2\2\2\27\3\2\2\2\2\33\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)"+
		"\3\2\2\2\3+\3\2\2\2\5.\3\2\2\2\7\63\3\2\2\2\t8\3\2\2\2\13;\3\2\2\2\r="+
		"\3\2\2\2\17?\3\2\2\2\21A\3\2\2\2\23E\3\2\2\2\25G\3\2\2\2\27J\3\2\2\2\31"+
		"N\3\2\2\2\33R\3\2\2\2\35]\3\2\2\2\37e\3\2\2\2!l\3\2\2\2#n\3\2\2\2%r\3"+
		"\2\2\2\'~\3\2\2\2)\u008e\3\2\2\2+,\7k\2\2,-\7h\2\2-\4\3\2\2\2./\7v\2\2"+
		"/\60\7j\2\2\60\61\7g\2\2\61\62\7p\2\2\62\6\3\2\2\2\63\64\7g\2\2\64\65"+
		"\7n\2\2\65\66\7u\2\2\66\67\7g\2\2\67\b\3\2\2\289\7h\2\29:\7k\2\2:\n\3"+
		"\2\2\2;<\7=\2\2<\f\3\2\2\2=>\7}\2\2>\16\3\2\2\2?@\7\177\2\2@\20\3\2\2"+
		"\2AB\7K\2\2BC\7p\2\2CD\7v\2\2D\22\3\2\2\2EF\7-\2\2F\24\3\2\2\2GH\t\2\2"+
		"\2H\26\3\2\2\2IK\5\25\13\2JI\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2\2M\30"+
		"\3\2\2\2NO\t\3\2\2O\32\3\2\2\2PS\5\31\r\2QS\7a\2\2RP\3\2\2\2RQ\3\2\2\2"+
		"SY\3\2\2\2TX\5\31\r\2UX\7a\2\2VX\5\25\13\2WT\3\2\2\2WU\3\2\2\2WV\3\2\2"+
		"\2X[\3\2\2\2YW\3\2\2\2YZ\3\2\2\2Z\34\3\2\2\2[Y\3\2\2\2\\^\5\25\13\2]\\"+
		"\3\2\2\2^_\3\2\2\2_]\3\2\2\2_`\3\2\2\2`\36\3\2\2\2ac\7\60\2\2bd\5\35\17"+
		"\2cb\3\2\2\2cd\3\2\2\2df\3\2\2\2ea\3\2\2\2ef\3\2\2\2f \3\2\2\2gi\7g\2"+
		"\2hj\t\4\2\2ih\3\2\2\2ij\3\2\2\2jk\3\2\2\2km\5\35\17\2lg\3\2\2\2lm\3\2"+
		"\2\2m\"\3\2\2\2no\5\35\17\2op\5\37\20\2pq\5!\21\2q$\3\2\2\2rx\7$\2\2s"+
		"t\7^\2\2tw\7$\2\2uw\13\2\2\2vs\3\2\2\2vu\3\2\2\2wz\3\2\2\2xy\3\2\2\2x"+
		"v\3\2\2\2y{\3\2\2\2zx\3\2\2\2{|\7$\2\2|}\b\23\2\2}&\3\2\2\2~\177\7\61"+
		"\2\2\177\u0080\7,\2\2\u0080\u0085\3\2\2\2\u0081\u0084\5\'\24\2\u0082\u0084"+
		"\13\2\2\2\u0083\u0081\3\2\2\2\u0083\u0082\3\2\2\2\u0084\u0087\3\2\2\2"+
		"\u0085\u0086\3\2\2\2\u0085\u0083\3\2\2\2\u0086\u0088\3\2\2\2\u0087\u0085"+
		"\3\2\2\2\u0088\u0089\7,\2\2\u0089\u008a\7\61\2\2\u008a\u008b\3\2\2\2\u008b"+
		"\u008c\b\24\3\2\u008c(\3\2\2\2\u008d\u008f\t\5\2\2\u008e\u008d\3\2\2\2"+
		"\u008f\u0090\3\2\2\2\u0090\u008e\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092"+
		"\3\2\2\2\u0092\u0093\b\25\3\2\u0093*\3\2\2\2\21\2LRWY_ceilvx\u0083\u0085"+
		"\u0090\4\3\23\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}