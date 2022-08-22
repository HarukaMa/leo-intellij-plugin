package im.mrx.leolanguage

import com.intellij.lexer.FlexAdapter

class LeoLexerAdapter : FlexAdapter(LeoLexer(null))