lexer grammar CoolLexer;

tokens { ERROR } 

@header{
    package cool.lexer;	
}

@members{
    private void raiseError(String msg) {
        setText(msg);
        setType(ERROR);
    }
}

/*
 * Keywords
 */
CLASS : [Cc][Ll][Aa][Ss][Ss];
ELSE : [Ee][Ll][Ss][Ee];
FALSE : 'f'[Aa][Ll][Ss][Ee];
FI : [Ff][Ii];
IF : [Ii][Ff];
IN : [Ii][Nn];
INHERITS : [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss];
ISVOID : [Ii][Ss][Vv][Oo][Ii][Dd];
LET : [Ll][Ee][Tt];
LOOP : [Ll][Oo][Oo][Pp];
POOL : [Pp][Oo][Oo][Ll];
THEN : [Tt][Hh][Ee][Nn];
WHILE : [Ww][Hh][Ii][Ll][Ee];
CASE : [Cc][Aa][Ss][Ee];
ESAC : [Ee][Ss][Aa][Cc];
NEW : [Nn][Ee][Ww];
OF : [Oo][Ff];
NOT : [Nn][Oo][Tt];
TRUE : 't'[Rr][Uu][Ee];

fragment LETTER : [a-zA-Z];
/*
 * Type identifiers
 */
TYPE : [A-Z](LETTER | '_' | DIGIT)*;

/*
 * Identifier
 */
ID : [a-z](LETTER | '_' | DIGIT)*;

/*
 * Integer
 */
fragment DIGIT : [0-9];
INT: DIGIT+;

/*
 * Operators and special characters
 */
SEMI : ';';
COMMA : ',';
ASSIGN : '<-';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
PLUS : '+';
MINUS : '-';
MULT : '*';
DIV : '/';
COMPLEMENT: '~';
EQUAL : '=';
LT : '<';
LE : '<=';
COLON: ':';
AT: '@';
DOT: '.';
IMPLIES: '=>';
STRING : '"' ('\\"' | '\\' NEW_LINE | .)*? ('"' {
    String s = getText();
    s = s.substring(1, s.length() - 1);
    s = s.replaceAll("\\\\t", "\t");
    s = s.replaceAll("\\\\n", "\n");
    s = s.replaceAll("\\\\b", "\b");
    s = s.replaceAll("\\\\f", "\f");
    s = s.replaceAll("(\\\\)(.*)", "$2");
    if (s.length() > 1024) {
        raiseError("String constant too long");
        return;
    }
    if (s.indexOf('\u0000') != -1) {
        raiseError("String contains null character");
        return;
    }
    setText(s);
}
    | NEW_LINE { raiseError("Unterminated string constant"); }
    | EOF { raiseError("EOF in string constant"); }
);

/*
 * Comments
 */
fragment NEW_LINE : '\r'? '\n';
LINE_COMMENT
    : '--' .*? (NEW_LINE | EOF) -> skip
    ;

BLOCK_COMMENT
    : '(*'
      (BLOCK_COMMENT | .)*?
      ('*)' { skip(); } | EOF { raiseError("EOF in comment"); })
    ;

BAD_COMMENT: '*)' { raiseError("Unmatched *)"); };

WS
    :   [ \n\f\r\t]+ -> skip
    ;

INVALID_CHARACTER: . { raiseError("Invalid character: " + getText()); };
