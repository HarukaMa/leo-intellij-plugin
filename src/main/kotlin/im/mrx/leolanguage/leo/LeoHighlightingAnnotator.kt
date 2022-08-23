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

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.LeoAnnotation
import im.mrx.leolanguage.leo.psi.LeoDeclaration
import im.mrx.leolanguage.leo.psi.LeoFunctionDeclaration
import im.mrx.leolanguage.leo.psi.LeoTypes.*

class LeoHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) {
            return
        }
        val attribute = when (element) {
            is LeoAnnotation -> ANNOTATION_KEY
            is LeafPsiElement -> highlightLeaf(element)
            else -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(attribute)
            .create()
    }

    private fun highlightLeaf(element: PsiElement): TextAttributesKey? {
        return when (element.elementType) {
            IDENTIFIER -> highlightIdentifier(element)
            else -> null
        }
    }

    private fun highlightIdentifier(element: PsiElement): TextAttributesKey? {
        val parent = element.parent

        return when (parent.elementType) {
            FUNCTION_DECLARATION -> FUNCTION_DECLARATION_KEY
            FUNCTION_PARAMETER -> FUNCTION_PARAMETER_KEY
            RECORD_DECLARATION -> RECORD_DECLARATION_KEY
            VARIABLE_DECLARATION -> VARIABLE_DECLARATION_KEY
            FREE_FUNCTION_CALL -> FREE_FUNCTION_CALL_KEY
            STATIC_FUNCTION_CALL -> STATIC_FUNCTION_CALL_KEY
            CIRCUIT_COMPONENT_DECLARATION -> CIRCUIT_COMPONENT_KEY
            CIRCUIT_COMPONENT_IDENTIFIER -> CIRCUIT_COMPONENT_KEY
            CIRCUIT_COMPONENT_INITIALIZER -> CIRCUIT_COMPONENT_KEY
            VARIABLE_OR_FREE_CONSTANT -> highlightFunctionParameter(element)
            NAMED_TYPE -> highlightRecordName(element)
            CIRCUIT_EXPRESSION -> highlightRecordName(element)
            else -> null
        }
    }

    private fun highlightFunctionParameter(element: PsiElement): TextAttributesKey? {
        val function = PsiTreeUtil.getParentOfType(element, LeoFunctionDeclaration::class.java) ?: return null
        val parameters = function.functionParameters ?: return null
        for (parameter in parameters.functionParameterList) {
            if (parameter.name == element.text) {
                return FUNCTION_PARAMETER_KEY
            }
        }
        return null
    }

    private fun highlightRecordName(element: PsiElement): TextAttributesKey? {
        val file = element.containingFile
        val declarations =
            PsiTreeUtil.getChildrenOfType(file.originalElement, LeoDeclaration::class.java) ?: return null
        for (declaration in declarations) {
            declaration.recordDeclaration?.let {
                if (it.name == element.text) {
                    return RECORD_DECLARATION_KEY
                }
            }
        }
        return null
    }

    companion object {
        val ANNOTATION_KEY =
            TextAttributesKey.createTextAttributesKey("LEO_ANNOTATION", DefaultLanguageHighlighterColors.METADATA)
        val FUNCTION_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_FUNCTION_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
        val FUNCTION_PARAMETER_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_FUNCTION_PARAMETER",
            DefaultLanguageHighlighterColors.PARAMETER
        )
        val RECORD_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_RECORD_DECLARATION", DefaultLanguageHighlighterColors.INTERFACE_NAME
        )
        val VARIABLE_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_VARIABLE_DECLARATION", DefaultLanguageHighlighterColors.LOCAL_VARIABLE
        )
        val FREE_FUNCTION_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_FREE_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        val STATIC_FUNCTION_CALL_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_STATIC_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL
        )
        val CIRCUIT_COMPONENT_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_CIRCUIT_COMPONENT_DECLARATION", DefaultLanguageHighlighterColors.INSTANCE_FIELD
        )
    }
}