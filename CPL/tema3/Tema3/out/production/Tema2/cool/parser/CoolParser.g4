parser grammar CoolParser;

options {
    tokenVocab = CoolLexer;
}

@header{
    package cool.parser;
}

formal
    : name=ID COLON type=TYPE
    ;

feature
    : name=ID LPAREN (formals+=formal (COMMA formals+=formal)*)? RPAREN COLON type=TYPE
        LBRACE expression=expr RBRACE                       # functionDefinition
    | name=ID COLON type=TYPE (ASSIGN expression=expr)?     # fieldDefinition
    ;

classDefinition
    : CLASS name=TYPE (INHERITS inheritName=TYPE)? LBRACE (feature SEMI)* RBRACE
    ;

program
    : (classDefinition SEMI)* EOF
    ;

local
    : name=ID COLON type=TYPE (ASSIGN expression=expr)?
    ;

caseBranch
    : name=ID COLON type=TYPE IMPLIES expression=expr SEMI
    ;

expr
    : expression=expr (AT type=TYPE)? DOT name=ID
        LPAREN (callArgs+=expr (COMMA callArgs+=expr)*)? RPAREN         # dispatchFunctionCall
    | name=ID LPAREN (callArgs+=expr (COMMA callArgs+=expr)*)? RPAREN   # functionCall
    | IF cond=expr THEN ifBranch=expr ELSE elseBranch=expr FI           # ifExpression
    | WHILE cond=expr LOOP expression=expr POOL                         # whileExpression
    | LET variables+=local (COMMA variables+=local)*
        IN expression=expr                                              # letExpression
    | CASE expression=expr OF caseBranch+ ESAC                          # caseExpression
    | LBRACE (expr SEMI)+ RBRACE                                        # blockExpression
    | NEW type=TYPE                                                     # newExpression
    | COMPLEMENT expression=expr                                        # complementExpression
    | ISVOID expression=expr                                            # isVoidExpression
    | LPAREN expression=expr RPAREN                                     # parenthesisExpression
    | left=expr op=(MULT | DIV) right=expr                              # multiplyDivisionExpression
    | left=expr op=(PLUS | MINUS) right=expr                            # plusMinusExpression
    | left=expr op=(LT | LE | EQUAL) right=expr                         # relationalExpression
    | NOT expression=expr                                               # notExpression
    | variable=ID ASSIGN expression=expr                                # assignExpression
    | ID                                                                # id
    | INT                                                               # int
    | STRING                                                            # string
    | TRUE                                                              # true
    | FALSE                                                             # false
    ;
