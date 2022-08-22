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

import im.mrx.leolanguage.parser.LeoParser
import im.mrx.leolanguage.psi.LeoFile
import im.mrx.leolanguage.psi.LeoTypes

class LeoParserDefinition : ParserDefinition {

    companion object {
        val FILE = IFileElementType(LeoLanguage.INSTANCE)
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