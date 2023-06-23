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

//EOL=\R
WHITE_SPACE=\s+

LINE_COMMENT=\/\/.*
BLOCK_COMMENT=\/[*]([^*]|[*][^/])*([*]\/)?
KEYWORD=const|constant|public|private|address|boolean|field|group|i8|i16|i32|i64|i128|u8|u16|u32|u64|u128|scalar|string|true|false|input|output|as|into|record|owner|function|struct|closure|program|aleo|self|storage|mapping|key|value|global|block|return|break|assert|continue|let|if|else|while|for|switch|case|default|match|enum|union|trait|impl|type
INSTRUCTION_NAME=cast|call|abs|abs\.w|double|inv|neg|not|square|sqrt|hash\.bhp256|hash\.bhp512|hash\.bhp768|hash\.bhp1024|hash\.ped64|hash\.ped128|hash\.psd2|hash\.psd4|hash\.psd8|hash_many\.psd2|hash_many\.psd4|hash_many\.psd8|add|add\.w|sub|sub\.w|mul|mul\.w|div|div\.w|rem|rem\.w|mod|pow|pow\.w|shl|shl\.w|shr|shr\.w|and|or|xor|nand|nor|gt|gte|lt|lte|is\.eq|is\.neq|commit\.bhp256|commit\.bhp512|commit\.bhp768|commit\.bhp1024|commit\.ped64|commit\.ped128|ternary|assert.eq|assert.neq|contains|get|get\.or_use|set|remove|rand\.chacha|position|branch\.eq|branch\.neq
ENTRY_VISIBILITY=public|private|constant
ADDRESS_LITERAL=aleo1[ac-hj-np-z02-9]{58}
BASE_REGISTER=r[0-9]+
BOOLEAN_LITERAL=true|false
LOWERCASE_IDENTIFIER=[a-z_][a-z0-9_]*
IDENTIFIER=[A-Za-z_][A-Za-z0-9_]*
STRING_LITERAL=\"([\x00-\x21\x23-\x5b\u005d-\uffff]|\\\"|\\\\)*\"
ARITHMETIC_LITERAL=-?[0-9]+([ui](8|16|32|64|128)|field|group|scalar)
NUMERAL=[0-9]+
BRACKETS=[\[\]]

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }

  ";"                { return SEMICOLON; }
  ":"                { return COLON; }
  "."                { return DOT; }
  "/"                { return SLASH; }
  "as"               { return AS; }

  "\""               { yybegin(STRING_STATE); }

  {LINE_COMMENT}     { return LINE_COMMENT; }
  {BLOCK_COMMENT}    { return BLOCK_COMMENT; }
  {ENTRY_VISIBILITY} { return ENTRY_VISIBILITY; }
  {BOOLEAN_LITERAL}  { return BOOLEAN_LITERAL; }
  {KEYWORD}          { return KEYWORD; }
  {INSTRUCTION_NAME} { return INSTRUCTION_NAME; }
  {ADDRESS_LITERAL}  { return ADDRESS_LITERAL; }
  {BASE_REGISTER}    { return REGISTER; }
  {LOWERCASE_IDENTIFIER} { return LOWERCASE_IDENTIFIER; }
  {IDENTIFIER}       { return IDENTIFIER; }
  {STRING_LITERAL}   { return STRING_LITERAL; }
  {ARITHMETIC_LITERAL} { return ARITHMETIC_LITERAL; }
  {NUMERAL}          { return NUMERAL; }
  {BRACKETS}         { return BRACKETS; }

}

<STRING_STATE> {
  {STRING_LITERAL}      { yybegin(YYINITIAL); return STRING_LITERAL; }
}

[^] { return BAD_CHARACTER; }
