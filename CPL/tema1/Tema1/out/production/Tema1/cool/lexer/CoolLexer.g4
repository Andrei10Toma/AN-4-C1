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
SELF_TYPE : 'SELF_TYPE';

/*
 * Identifier
 */
SELF: 'self';
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
STRING : '"' ('\\"' | .)*? '"' {
    String s = getText();
    String s_without_quotes = s.substring(1, s.length() - 1);
    setText(s_without_quotes);
};

WS
    :   [ \n\f\r\t]+ -> skip
    ;
