// Generated from /home/damien/IdeaProjects/NaissanceE_mod/grammar/Calculator.g4 by ANTLR 4.9.2
package net.darmo_creations.naissancee.calculator.antlr4_parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CalculatorLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, LPAREN=2, RPAREN=3, PLUS=4, MINUS=5, MUL=6, DIV=7, MOD=8, POWER=9, 
		COMMA=10, ASSIGN=11, ID=12, NUMBER=13;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"WS", "LPAREN", "RPAREN", "PLUS", "MINUS", "MUL", "DIV", "MOD", "POWER", 
			"COMMA", "ASSIGN", "ID", "NUMBER"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, "'('", "')'", "'+'", "'-'", "'*'", "'/'", "'%'", "'^'", "','", 
			"':='"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "WS", "LPAREN", "RPAREN", "PLUS", "MINUS", "MUL", "DIV", "MOD", 
			"POWER", "COMMA", "ASSIGN", "ID", "NUMBER"
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


	public CalculatorLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Calculator.g4"; }

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

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\17N\b\1\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\6\2\37\n\2\r\2\16\2 \3\2\3\2\3\3\3\3"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f"+
		"\3\f\3\f\3\r\3\r\7\r<\n\r\f\r\16\r?\13\r\3\16\6\16B\n\16\r\16\16\16C\3"+
		"\16\5\16G\n\16\3\16\7\16J\n\16\f\16\16\16M\13\16\2\2\17\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\3\2\6\4\2\13\f\"\"\5"+
		"\2C\\aac|\6\2\62;C\\aac|\3\2\62;\2R\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2"+
		"\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23"+
		"\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\3\36\3\2"+
		"\2\2\5$\3\2\2\2\7&\3\2\2\2\t(\3\2\2\2\13*\3\2\2\2\r,\3\2\2\2\17.\3\2\2"+
		"\2\21\60\3\2\2\2\23\62\3\2\2\2\25\64\3\2\2\2\27\66\3\2\2\2\319\3\2\2\2"+
		"\33A\3\2\2\2\35\37\t\2\2\2\36\35\3\2\2\2\37 \3\2\2\2 \36\3\2\2\2 !\3\2"+
		"\2\2!\"\3\2\2\2\"#\b\2\2\2#\4\3\2\2\2$%\7*\2\2%\6\3\2\2\2&\'\7+\2\2\'"+
		"\b\3\2\2\2()\7-\2\2)\n\3\2\2\2*+\7/\2\2+\f\3\2\2\2,-\7,\2\2-\16\3\2\2"+
		"\2./\7\61\2\2/\20\3\2\2\2\60\61\7\'\2\2\61\22\3\2\2\2\62\63\7`\2\2\63"+
		"\24\3\2\2\2\64\65\7.\2\2\65\26\3\2\2\2\66\67\7<\2\2\678\7?\2\28\30\3\2"+
		"\2\29=\t\3\2\2:<\t\4\2\2;:\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\32\3"+
		"\2\2\2?=\3\2\2\2@B\t\5\2\2A@\3\2\2\2BC\3\2\2\2CA\3\2\2\2CD\3\2\2\2DF\3"+
		"\2\2\2EG\7\60\2\2FE\3\2\2\2FG\3\2\2\2GK\3\2\2\2HJ\t\5\2\2IH\3\2\2\2JM"+
		"\3\2\2\2KI\3\2\2\2KL\3\2\2\2L\34\3\2\2\2MK\3\2\2\2\b\2 =CFK\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}