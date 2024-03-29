/*
 * Copyright (c) 2022-2023 Haruka Ma
 * This file is part of Leo / Aleo IntelliJ plugin.
 *
 * Leo / Aleo IntelliJ plugin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Leo / Aleo IntelliJ plugin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Leo / Aleo IntelliJ plugin. If not, see <https://www.gnu.org/licenses/>.
 */

package im.mrx.leolanguage.leo.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import im.mrx.leolanguage.leo.LeoLexerAdapter
import im.mrx.leolanguage.leo.psi.LeoTypes
import org.jetbrains.annotations.NotNull

class LeoSyntaxHighlighter : SyntaxHighlighterBase() {
    @NotNull
    override fun getHighlightingLexer(): Lexer {
        return LeoLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        val key = when (tokenType) {
            LeoTypes.KEYWORD -> KEYWORD
            LeoTypes.MODE -> KEYWORD
            LeoTypes.BOOLEAN_LITERAL -> KEYWORD
            LeoTypes.IDENTIFIER -> IDENTIFIER
            LeoTypes.STRING_LITERAL -> STRING
            LeoTypes.NUMERIC_LITERAL -> NUMBER
            LeoTypes.BLOCK_COMMENT -> BLOCK_COMMENT
            LeoTypes.LINE_COMMENT -> LINE_COMMENT
            LeoTypes.LPAREN -> PARENS
            LeoTypes.RPAREN -> PARENS
            LeoTypes.BRACKETS -> BRACKETS
            LeoTypes.LBRACE -> BRACES
            LeoTypes.RBRACE -> BRACES
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