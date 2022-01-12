grammar Calculator;

WS    : [ \n\t]+ -> skip;
LPAREN: '(';
RPAREN: ')';
PLUS  : '+';
MINUS : '-';
MUL   : '*';
DIV   : '/';
MOD   : '%';
POWER : '^';
COMMA : ',';
ASSIGN: ':=';
ID    : [a-zA-Z_][a-zA-Z0-9_]*;
NUMBER: [0-9]+'.'?[0-9]*;

start: statement EOF;

statement: exp                     # Expression
          | var=ID ASSIGN expr=exp # VariableDef
          | fname=ID LPAREN ID (COMMA ID)* RPAREN ASSIGN expr=exp # FunctionDef
          ;

// Only direct left recursion is allowed: https://stackoverflow.com/a/41183746/3779986
exp: LPAREN expr=exp RPAREN                        # Parentheses
   | MINUS operand=exp                             # Minus
   | PLUS operand=exp                              # Plus
   | left=exp operator=POWER right=exp             # Operator
   | left=exp operator=(MUL | DIV | MOD) right=exp # Operator
   | left=exp operator=(PLUS | MINUS) right=exp    # Operator
   | name=ID LPAREN exp (COMMA exp)* RPAREN        # Function
   | ID                                            # Variable
   | NUMBER                                        # Number
;
