package im.mrx.leolanguage.leo;

import com.intellij.psi.tree.IElementType;
import com.intellij.lexer.FlexLexer;

import static im.mrx.leolanguage.leo.psi.LeoTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%class LeoLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

//ASCII=[\u0000-\u007F]
//SAFE_NONASCII=[\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
//CHARACTER=[\u0000-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
//LINE_FEED=\n
//CARRIAGE_RETURN=\r
//LINE_TERMINATOR=\n|\r|\r\n
WHITESPACE=(\ |\t|\n|\r|\r\n)+
//NOT_LINE_FEED_OR_CARRIAGE_RETURN=[\u0000-\u0009\u000B-\u000C\u000E-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
//NOT_STAR_OR_LINE_FEED_OR_CARRIAGE_RETURN=[\u0000-\u0009\u000B-\u000C\u000E-\u0029\u002B-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
//NOT_STAR_OR_SLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN=[\u0000-\u0009\u000B-\u000C\u000E-\u0029\u002B-\u002E\u0030-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
//NOT_DOUBLE_QUOTE_OR_BACKSLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN=[\u0000-\u0009\u000B-\u000C\u000E-\u0021\u0023-\u005B\u005D-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]
LINE_COMMENT=\/\/[\u0000-\u0009\u000B-\u000C\u000E-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]*
BLOCK_COMMENT=\/[*]([^*]|[*][^/])*([*]\/)?
KEYWORD=address|async|bool|console|constant|else|field|finalize|for|function|group|i8|i16|i32|i64|i128|if|import|in|inline|let|mapping|private|program|public|record|return|scalar|string|struct|then|transition|u8|u16|u32|u64|u128|self
CORE_STRUCT=BHP256|BHP512|BHP768|BHP1024|Pedersen64|Pedersen128|Poseidon2|Poseidon4|Poseidon8|Mapping
//UPPERCASE_LETTER=[A_Z]
//LOWERCASE_LETTER=[a_z]
//LETTER=[a_zA_Z]
//DECIMAL_DIGIT=[0-9]
//OCTAL_DIGIT=[0-7]
//HEXADECIMAL_DIGIT=[0-9a_f]
IDENTIFIER=[a-zA-Z][a-zA-Z0-9_]*
NUMERAL=[0-9]+
//UNSIGNED_LITERAL=[0-9]+u(8|16|32|64|128)
//SIGNED_LITERAL=[0-9]+i(8|16|32|64|128)
//FIELD_LITERAL=[0-9]+field
//PRODUCT_GROUP_LITERAL=[0-9]+group
//SCALAR_LITERAL=[0-9]+scalar
BOOLEAN_LITERAL=true|false
ADDRESS_LITERAL=aleo1[ac-hj-np-z02-9]{58}
//SINGLE_QUOTE_ESCAPE=\'
//DOUBLE_QUOTE_ESCAPE=\\\"
//BACKSLASH_ESCAPE=\\
//LINE_FEED_ESCAPE=\\n
//CARRIAGE_RETURN_ESCAPE=\\r
//HORIZONTAL_TAB_ESCAPE=\\t
//NULL_CHARACTER_ESCAPE=\\0
//SIMPLE_CHARACTER_ESCAPE=(\'|\\\"|\\|\\n|\\r|\\t|\\0)
//ASCII_CHARACTER_ESCAPE=\\x[0-7][0-9a_f]
//UNICODE_CHARACTER_ESCAPE=\\u\{[0-9a_f]{1,6}}
STRING_LITERAL=\"([\u0000-\u0009\u000B-\u000C\u000E-\u0021\u0023-\u005B\u005D-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]|\'|\\\"|\\|\\n|\\r|\\t|\\0|\\x[0-7][0-9a_f]|\\u\{[0-9a_f]{1,6}})*\"
//INTEGER_LITERAL=[0-9]+[ui](8|16|32|64|128)
NUMERIC_LITERAL=[0-9]+([ui](8|16|32|64|128)|field|group|scalar)
//ATOMIC_LITERAL=[0-9]+([ui](8|16|32|64|128)|field|group|scalar)|true|false|aleo1[a_z0-9]{58}|\"([\u0000-\u0009\u000B-\u000C\u000E-\u0021\u0023-\u005B\u005D-\u007F\u0080-\u2029\u202F-\u2065\u2070-\uD7FF\uE000-\uFFFF]|\'|\\\"|\\|\\n|\\r|\\t|\\0|\\x[0-7][0-9a_f]|\\u\{[0-9a_f]{1,6}})*\"
//ANNOTATION=\@[a-zA-Z][a-zA-Z0-9_]*
SYMBOL=\!|&&|\|\||==|\!=|<|<=|>|>=|&|\||\^|<<|>>|\+|-|\*|%|\*\*|=|\+=|-=|\*=|"/"=|%=|\*\*=|<<=|>>=|&=|\|=|\^=|&&=|\|\|=|\(|\)|\[|]|\{|}|,|\.|\.\.|;|:|::|\?|->|_|=>|\)group
BRACKETS=[\[\]]

%state NUMERIC
%state STRING

%%
<YYINITIAL> {

  "\""                                                                 { yybegin(STRING); return DOUBLE_QUOTE; }
  "'"                                                                  { return SINGLE_QUOTE; }
  "."                                                                  { return DOT; }
  ","                                                                  { return COMMA; }
  ";"                                                                  { return SEMICOLON; }
  ":"                                                                  { return COLON; }
  "::"                                                                 { return DOUBLE_COLON; }
  "@"                                                                  { return AT; }
  "{"                                                                  { return LBRACE; }
  "}"                                                                  { return RBRACE; }
  "("                                                                  { return LPAREN; }
  ")"                                                                  { return RPAREN; }
  "/"                                                                  { return SLASH; }
  "-"                                                                  { return DASH; }
  ")group"                                                             { return GROUP_END; }

  {NUMERAL}                                                            { yybegin(NUMERIC); }
//  {ASCII}                                                              { return ASCII; }
//  {SAFE_NONASCII}                                                      { return SAFE_NONASCII; }
//  {CHARACTER}                                                          { return CHARACTER; }
//  {LINE_FEED}                                                          { return LINE_FEED; }
//  {CARRIAGE_RETURN}                                                    { return CARRIAGE_RETURN; }
//  {LINE_TERMINATOR}                                                    { return LINE_TERMINATOR; }
  {WHITESPACE}                                                         { return WHITE_SPACE; }
//  {NOT_LINE_FEED_OR_CARRIAGE_RETURN}                                   { return NOT_LINE_FEED_OR_CARRIAGE_RETURN; }
//  {NOT_STAR_OR_LINE_FEED_OR_CARRIAGE_RETURN}                           { return NOT_STAR_OR_LINE_FEED_OR_CARRIAGE_RETURN; }
//  {NOT_STAR_OR_SLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN}                  { return NOT_STAR_OR_SLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN; }
//  {NOT_DOUBLE_QUOTE_OR_BACKSLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN}      { return NOT_DOUBLE_QUOTE_OR_BACKSLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN; }
  {LINE_COMMENT}                                                       { return LINE_COMMENT; }
  {BLOCK_COMMENT}                                                      { return BLOCK_COMMENT; }
  {BOOLEAN_LITERAL}                                                    { return BOOLEAN_LITERAL; }
  {ADDRESS_LITERAL}                                                    { return ADDRESS_LITERAL; }
  {KEYWORD}                                                            { return KEYWORD; }
  {CORE_STRUCT}                                                        { return CORE_STRUCT; }
//  {UPPERCASE_LETTER}                                                   { return UPPERCASE_LETTER; }
//  {LOWERCASE_LETTER}                                                   { return LOWERCASE_LETTER; }
//  {LETTER}                                                             { return LETTER; }
//  {DECIMAL_DIGIT}                                                      { return DECIMAL_DIGIT; }
//  {OCTAL_DIGIT}                                                        { return OCTAL_DIGIT; }
//  {HEXADECIMAL_DIGIT}                                                  { return HEXADECIMAL_DIGIT; }
//  {ANNOTATION}                                                         { return ANNOTATION; }
  {IDENTIFIER}                                                         { return IDENTIFIER; }
//  {NUMERAL}                                                            { return NUMERAL; }
//  {UNSIGNED_LITERAL}                                                   { return UNSIGNED_LITERAL; }
//  {SIGNED_LITERAL}                                                     { return SIGNED_LITERAL; }
//  {FIELD_LITERAL}                                                      { return FIELD_LITERAL; }
//  {PRODUCT_GROUP_LITERAL}                                              { return PRODUCT_GROUP_LITERAL; }
//  {SCALAR_LITERAL}                                                     { return SCALAR_LITERAL; }
//  {SINGLE_QUOTE_ESCAPE}                                                { return SINGLE_QUOTE_ESCAPE; }
//  {DOUBLE_QUOTE_ESCAPE}                                                { return DOUBLE_QUOTE_ESCAPE; }
//  {BACKSLASH_ESCAPE}                                                   { return BACKSLASH_ESCAPE; }
//  {LINE_FEED_ESCAPE}                                                   { return LINE_FEED_ESCAPE; }
//  {CARRIAGE_RETURN_ESCAPE}                                             { return CARRIAGE_RETURN_ESCAPE; }
//  {HORIZONTAL_TAB_ESCAPE}                                              { return HORIZONTAL_TAB_ESCAPE; }
//  {NULL_CHARACTER_ESCAPE}                                              { return NULL_CHARACTER_ESCAPE; }
//  {SIMPLE_CHARACTER_ESCAPE}                                            { return SIMPLE_CHARACTER_ESCAPE; }
//  {ASCII_CHARACTER_ESCAPE}                                             { return ASCII_CHARACTER_ESCAPE; }
//  {UNICODE_CHARACTER_ESCAPE}                                           { return UNICODE_CHARACTER_ESCAPE; }
  {STRING_LITERAL}                                                     { return STRING_LITERAL; }
//  {INTEGER_LITERAL}                                                    { return INTEGER_LITERAL; }
  {NUMERIC_LITERAL}                                                    { return NUMERIC_LITERAL; }
//  {ATOMIC_LITERAL}                                                     { return ATOMIC_LITERAL; }
  {BRACKETS}                                                           { return BRACKETS; }
  {SYMBOL}                                                             { return SYMBOL; }

}

<NUMERIC> {
    {NUMERIC_LITERAL}                                                            { yybegin(YYINITIAL); return NUMERIC_LITERAL; }
    {NUMERAL}                                                                    { return NUMERAL; }
    [^]                                                                         { yybegin(YYINITIAL); yypushback(1); return NUMERAL;}
}

<STRING> {
    {STRING_LITERAL}                                                            { yybegin(YYINITIAL); return STRING_LITERAL; }
}


[^] { return BAD_CHARACTER; }
