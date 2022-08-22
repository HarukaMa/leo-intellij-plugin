package im.mrx.leolanguage.leo

import com.intellij.lexer.FlexAdapter

class LeoLexerAdapter : FlexAdapter(LeoLexer(null))