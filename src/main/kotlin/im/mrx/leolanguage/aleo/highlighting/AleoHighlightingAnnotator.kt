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

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.aleo.psi.AleoDefinition
import im.mrx.leolanguage.aleo.psi.AleoTypes.*

class AleoHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) {
            return
        }
        val attribute = when (element) {
            is LeafPsiElement -> highlightLeaf(element, holder)
            else -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(attribute)
            .create()
    }

    private fun highlightLeaf(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        return when (element.elementType) {
            IDENTIFIER -> highlightIdentifier(element, holder)
            else -> null
        }
    }

    private fun highlightIdentifier(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val parent = element.parent

        return when (parent.elementType) {
            INTERFACE -> INTERFACE_KEY
            PLAIN_TYPE -> highlightInterfaceName(element, holder)
            else -> null
        }
    }

    private fun highlightInterfaceName(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val file = element.containingFile
        PsiTreeUtil.getChildrenOfType(file, AleoDefinition::class.java)?.forEach { definition ->
            definition.`interface`?.let {
                if (it.name == element.text) {
                    return INTERFACE_KEY
                }
            }
            definition.record?.let {
                if (it.name == element.text) {
                    return INTERFACE_KEY
                }
            }
        }

        holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference: ${element.text}").create()
        return null
    }

    companion object {
        val INTERFACE_KEY =
            TextAttributesKey.createTextAttributesKey("ALEO_INTERFACE", DefaultLanguageHighlighterColors.INTERFACE_NAME)
    }
}