package atividade1;


%%

/* N�o altere as configura��es a seguir */

%line
%column
%unicode
//%debug
%public
%standalone
%class Minijava
%eofclose

/* Insira as regras l�xicas abaixo */

intl =  0|[1-9][0-9]*
ints = [0-9]
flinha = \r|\n|\r\n
letra = [a-z]|[A-Z]
idpart = {letra}|{ints}
id = (_|{letra})(_|{idpart})*
ws = {flinha} | [\ \t\f]
wso = [\ \t\f]
comlinha = "//"[^\r\n]*
ops = "&&" | "<" | "==" | "!=" | "+" | "-" | "*" | "!"
delim = "." | "," | "=" | "(" | ")" | "[" | "]" | "{" | "}"
endlim = ";"{wso}*?

%xstates COMENT LEX 

%%
<COMENT>{
	~"*/" {yybegin(YYINITIAL);}
	. {throw new RuntimeException("Comentario sem fim." + " Na linha: " + (yyline+1) + ", coluna: " + (yycolumn+1));}
}
<LEX>{
	{wso}+{id} {yybegin(YYINITIAL);}
	. {throw new RuntimeException("Tipo basico inicializado sem variavel." + " Na linha: " + (yyline+1) + ", coluna: " + (yycolumn+1));}
}
{comlinha} {} //comentario de linha
"/*" {yybegin(COMENT);} //testar o comentário especifico
{ws} {}
boolean {yybegin(LEX);}
class {}
public {}
extends {} 
static {}
void {}
main {}
String / [^"[]"] {yybegin(LEX);}
int / [^"[]"] {yybegin(LEX);}
String {}
int {} //int array prob
while {}
if {}
else {}
return {}
length {}
true {}
false {}
this {}
new {}
{id} {}
{ops} {}
System.out.println {}
{intl} {} 
{endlim}$ {} //; apenas no fim de linha (mesmo que tenham espaços em branco)
{delim} {}
. { throw new RuntimeException("Caractere ilegal! " + " na linha: " + (yyline+1) + ", coluna: " + (yycolumn+1)); }