/*
 * Copyright (c) 2022 Haruka Ma
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

package im.mrx.leolanguage.aleo.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import im.mrx.leolanguage.aleo.AleoLexerAdapter
import im.mrx.leolanguage.aleo.psi.AleoTypes

class AleoSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return AleoLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> {
        val key = when (tokenType) {
            AleoTypes.KEYWORD -> KEYWORD
            AleoTypes.INSTRUCTION_NAME -> INSTRUCTION_NAME
            AleoTypes.ADDRESS -> ADDRESS
            AleoTypes.BOOLEAN -> KEYWORD
            AleoTypes.IDENTIFIER -> IDENTIFIER
            AleoTypes.SEMICOLON -> SEMICOLON
            AleoTypes.DOT -> DOT
            AleoTypes.AS -> KEYWORD
            AleoTypes.BASE_REGISTER -> REGISTER
            AleoTypes.STRING -> STRING
            AleoTypes.COMMENT -> COMMENT
            AleoTypes.ARITHMETIC -> NUMBER
            TokenType.BAD_CHARACTER -> BAD_CHARACTER

            else -> null
        } ?: return TextAttributesKey.EMPTY_ARRAY
        return arrayOf(key)
    }


    companion object {
        val KEYWORD =
            TextAttributesKey.createTextAttributesKey("ALEO_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val IDENTIFIER =
            TextAttributesKey.createTextAttributesKey("ALEO_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val NUMBER = TextAttributesKey.createTextAttributesKey("ALEO_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val ADDRESS = TextAttributesKey.createTextAttributesKey("ALEO_ADDRESS", DefaultLanguageHighlighterColors.NUMBER)
        val STRING = TextAttributesKey.createTextAttributesKey("ALEO_STRING", DefaultLanguageHighlighterColors.STRING)
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("ALEO_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("ALEO_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
        val SEMICOLON =
            TextAttributesKey.createTextAttributesKey("ALEO_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
        val DOT = TextAttributesKey.createTextAttributesKey("ALEO_DOT", DefaultLanguageHighlighterColors.DOT)

        //        val COMMA = TextAttributesKey.createTextAttributesKey("ALEO_COMMA", DefaultLanguageHighlighterColors.COMMA)
//        val AS = TextAttributesKey.createTextAttributesKey("ALEO_AS", DefaultLanguageHighlighterColors.KEYWORD)
        val REGISTER =
            TextAttributesKey.createTextAttributesKey("ALEO_REGISTER", DefaultLanguageHighlighterColors.CONSTANT)
        val INSTRUCTION_NAME = TextAttributesKey.createTextAttributesKey(
            "ALEO_INSTRUCTION_NAME",
            DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
    }
}