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

package im.mrx.leolanguage.leo.formatting

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.formatter.FormatterUtil
import im.mrx.leolanguage.leo.LeoLanguage
import im.mrx.leolanguage.leo.psi.LeoBlock
import im.mrx.leolanguage.leo.psi.LeoCircuitComponentDeclarations
import im.mrx.leolanguage.leo.psi.LeoCircuitExpression
import im.mrx.leolanguage.leo.psi.LeoTypes

class LeoFormattingBlock(
    private val node: ASTNode,
    private val wrap: Wrap?,
    private val indent: Indent?,
    private val alignment: Alignment?,
    private val ctx: FormattingContext
) : ASTBlock {

    override fun getNode(): ASTNode = node
    override fun getTextRange(): TextRange = node.textRange
    override fun getSubBlocks(): MutableList<Block> = _subBlocks
    private val _subBlocks: MutableList<Block> by lazy { getChildren() }
    override fun getWrap(): Wrap? = wrap
    override fun getIndent(): Indent? = indent
    override fun getAlignment(): Alignment? = alignment

    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        return SpacingBuilder(ctx.codeStyleSettings, LeoLanguage.INSTANCE)
            .before(LeoTypes.COLON).spaces(0)
            .after(LeoTypes.COLON).spaces(1)
            .before(LeoTypes.LBRACE).spaces(1)
            .after(LeoTypes.LBRACE).spaces(1)
            .before(LeoTypes.RBRACE).spaces(1)
            .around(LeoTypes.SYMBOL).spaces(1)
            .after(LeoTypes.COMMA).spaces(1)
            .before(LeoTypes.BLOCK).spaces(1)
            .after(LeoTypes.LPAREN).spaces(0)
            .before(LeoTypes.RPAREN).spaces(0)
            .getSpacing(this, child1, child2)
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        println(newChildIndex)
        println(node)
        if (node.psi is LeoBlock || node.psi is LeoCircuitExpression || node.psi is LeoCircuitComponentDeclarations) {
//            if (newChildIndex > 0) {
            return ChildAttributes(Indent.getNormalIndent(), null)
//            }
        }
        return ChildAttributes(Indent.getNoneIndent(), null)
    }

    override fun isIncomplete(): Boolean = _isIncomplete
    private val _isIncomplete: Boolean by lazy { FormatterUtil.isIncomplete(node) }

    override fun isLeaf(): Boolean = node.firstChildNode == null


    private fun getChildren(): MutableList<Block> {
        val children = mutableListOf<Block>()
        node.getChildren(null).filter { it !is PsiWhiteSpace }.forEach {
            children.add(LeoFormattingBlock(it, wrap, getChildIndent(it), alignment, ctx))
        }
        return children
    }

    private fun getChildIndent(child: ASTNode): Indent {
        if (node.psi is LeoBlock || node.psi is LeoCircuitExpression || node.psi is LeoCircuitComponentDeclarations) {
            if (child.elementType != LeoTypes.LBRACE && child.elementType != LeoTypes.RBRACE) {
                return Indent.getNormalIndent()
            }
            return Indent.getNoneIndent()
        }
        return Indent.getNoneIndent()
    }
}