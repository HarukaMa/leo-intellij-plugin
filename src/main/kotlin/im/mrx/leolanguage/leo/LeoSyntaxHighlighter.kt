package im.mrx.leolanguage.leo

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import im.mrx.leolanguage.psi.LeoTypes
import org.jetbrains.annotations.NotNull

class LeoSyntaxHighlighter : SyntaxHighlighterBase() {
    @NotNull
    override fun getHighlightingLexer(): Lexer {
        return LeoLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey?> {
        val key = when (tokenType) {
            LeoTypes.KEYWORD -> KEYWORD
            LeoTypes.IDENTIFIER -> IDENTIFIER
            LeoTypes.STRING_LITERAL -> STRING
            LeoTypes.NUMERIC_LITERAL -> NUMBER
            LeoTypes.BLOCK_COMMENT -> BLOCK_COMMENT
            LeoTypes.END_OF_LINE_COMMENT -> LINE_COMMENT
            LeoTypes.PARENS -> PARENS
            LeoTypes.BRACKETS -> BRACKETS
            LeoTypes.BRACES -> BRACES
            LeoTypes.SEMICOLON -> SEMICOLON
            LeoTypes.DOT -> DOT
            LeoTypes.COMMA -> COMMA
            TokenType.BAD_CHARACTER -> BAD_CHARACTER

            else -> null
        } ?: return TextAttributesKey.EMPTY_ARRAY
        return arrayOf(key)
    }


    companion object {
        val KEYWORD = TextAttributesKey.createTextAttributesKey("LEO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val IDENTIFIER =
            TextAttributesKey.createTextAttributesKey("LEO_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val NUMBER = TextAttributesKey.createTextAttributesKey("LEO_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val STRING = TextAttributesKey.createTextAttributesKey("LEO_STRING", DefaultLanguageHighlighterColors.STRING)
        val BLOCK_COMMENT = TextAttributesKey.createTextAttributesKey(
            "LEO_BLOCK_COMMENT",
            DefaultLanguageHighlighterColors.BLOCK_COMMENT
        )
        val LINE_COMMENT =
            TextAttributesKey.createTextAttributesKey("LEO_LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("LEO_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
        val PARENS =
            TextAttributesKey.createTextAttributesKey("LEO_PARENS", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACES = TextAttributesKey.createTextAttributesKey("LEO_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val BRACKETS =
            TextAttributesKey.createTextAttributesKey("LEO_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val SEMICOLON =
            TextAttributesKey.createTextAttributesKey("LEO_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val DOT = TextAttributesKey.createTextAttributesKey("LEO_DOT", DefaultLanguageHighlighterColors.DOT)
        val COMMA = TextAttributesKey.createTextAttributesKey("LEO_COMMA", DefaultLanguageHighlighterColors.COMMA)

    }
}