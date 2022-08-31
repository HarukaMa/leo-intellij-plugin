package im.mrx.leolanguage.aleo;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static im.mrx.leolanguage.aleo.psi.AleoTypes.*;

%%

%class AleoLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode
%eof{  return;
%eof}

%state STRING_STATE

EOL=\R
WHITE_SPACE=\s+

COMMENT=\/\/.*
ADDRESS=aleo1[ac-hj-np-z02-9]{58}
BASE_REGISTER=r[0-9]+
BOOLEAN=true|false
IDENTIFIER=[A-Za-z_][A-Za-z0-9_]*
STRING=\"([\x00-\x21\x23-\x5b\u005d-\uffff]|\\\"|\\\\)*\"
NUMERAL=[0-9]+

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  ";"                { return SEMICOLON; }
  ":"                { return COLON; }
  "."                { return DOT; }
  "as"               { return AS; }

  "\""               { yybegin(STRING_STATE); }

  {COMMENT}          { return COMMENT; }
  {ADDRESS}          { return ADDRESS; }
  {BASE_REGISTER}    { return BASE_REGISTER; }
  {BOOLEAN}          { return BOOLEAN; }
  {IDENTIFIER}       { return IDENTIFIER; }
  {STRING}           { return STRING; }
  {NUMERAL}          { return NUMERAL; }

}

<STRING_STATE> {
  {STRING}           { yybegin(YYINITIAL); return STRING; }
}

[^] { return BAD_CHARACTER; }
