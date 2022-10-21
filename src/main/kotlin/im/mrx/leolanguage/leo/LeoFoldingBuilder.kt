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
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.leo.psi.LeoBlock
import im.mrx.leolanguage.leo.psi.LeoStructLikeDeclaration
import im.mrx.leolanguage.leo.psi.LeoVisitor

class LeoFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val descriptors = ArrayList<FoldingDescriptor>()
        val visitor = FoldingVisitor(descriptors)
        PsiTreeUtil.processElements(root) {
            it.accept(visitor)
            true
        }
        return descriptors.toTypedArray()
    }

    private class FoldingVisitor(val descriptors: ArrayList<FoldingDescriptor>) : LeoVisitor() {
        override fun visitStructLikeDeclaration(o: LeoStructLikeDeclaration) {
            val block = o.structComponentDeclarations ?: return

            descriptors.add(FoldingDescriptor(block, block.textRange))
        }

        override fun visitBlock(o: LeoBlock) {
            descriptors.add(FoldingDescriptor(o, o.textRange))
        }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return "{ ... }"
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
        return false
    }

}