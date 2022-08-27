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

package im.mrx.leolanguage.leo

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import im.mrx.leolanguage.leo.psi.LeoFile
import im.mrx.leolanguage.leo.psi.LeoTypes
import im.mrx.leolanguage.leo.stub.LeoFileStubType

class LeoParserDefinition : ParserDefinition {

    companion object {
        val FILE = LeoFileStubType.INSTANCE
        val WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENT = TokenSet.create(LeoTypes.END_OF_LINE_COMMENT, LeoTypes.BLOCK_COMMENT)
        val STRING = TokenSet.create(LeoTypes.STRING_LITERAL)
    }

    override fun createLexer(project: Project?): Lexer {
        return LeoLexerAdapter()
    }

    override fun createParser(project: Project?): PsiParser {
        return LeoParser()
    }

    override fun getFileNodeType(): IFileElementType {
        return FILE
    }

    override fun getWhitespaceTokens(): TokenSet {
        return WHITESPACE
    }

    override fun getCommentTokens(): TokenSet {
        return COMMENT
    }

    override fun getStringLiteralElements(): TokenSet {
        return STRING
    }

    override fun createElement(node: ASTNode?): PsiElement {
        return LeoTypes.Factory.createElement(node)
    }

    override fun createFile(viewProvider: FileViewProvider): PsiFile {
        return LeoFile(viewProvider)
    }
}