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

package im.mrx.leolanguage.leo.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.CodeInsightColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.psi.LeoTypes.*

class LeoHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) {
            return
        }
        val attribute = when (element) {
            is LeoAnnotation -> ANNOTATION_KEY
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
            FUNCTION_DECLARATION -> FUNCTION_DECLARATION_KEY
            FINALIZER -> FUNCTION_DECLARATION_KEY
            FUNCTION_PARAMETER -> FUNCTION_PARAMETER_KEY
            RECORD_DECLARATION -> RECORD_DECLARATION_KEY
            CIRCUIT_DECLARATION -> RECORD_DECLARATION_KEY
            VARIABLE_DECLARATION -> VARIABLE_DECLARATION_KEY
            STATIC_FUNCTION_CALL -> STATIC_FUNCTION_CALL_KEY
            CIRCUIT_COMPONENT_DECLARATION -> CIRCUIT_COMPONENT_KEY
            CIRCUIT_COMPONENT_INITIALIZER -> CIRCUIT_COMPONENT_KEY
            ASSERT_CALL -> STATIC_FUNCTION_CALL_KEY
            ASSERT_EQUAL_CALL -> STATIC_FUNCTION_CALL_KEY
            ASSERT_NOT_EQUAL_CALL -> STATIC_FUNCTION_CALL_KEY
            MAPPING_DECLARATION -> MAPPING_DECLARATION_KEY

            CIRCUIT_COMPONENT_IDENTIFIER -> highlightCircuitComponentWithReference(element, holder)
            VARIABLE_OR_FREE_CONSTANT -> highlightVariable(element, holder)
            NAMED_TYPE -> highlightRecordName(element, holder)
            CIRCUIT_EXPRESSION_IDENTIFIER -> highlightRecordName(element, holder)
            FUNCTION_IDENTIFIER -> highlightFunctionCall(element, holder)
            else -> null
        }
    }

    private fun highlightVariable(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val finalizer = PsiTreeUtil.getParentOfType(element, LeoFinalizer::class.java)
        if (finalizer != null) {
            finalizer.functionParameters?.functionParameterList?.forEach {
                if (it.name == element.text) {
                    return FUNCTION_PARAMETER_KEY
                }
            }
        } else {
            val function = PsiTreeUtil.getParentOfType(element, LeoFunctionDeclaration::class.java) ?: return null
            val parameters = function.functionParameters ?: return null
            for (parameter in parameters.functionParameterList) {
                if (parameter.name == element.text) {
                    return FUNCTION_PARAMETER_KEY
                }
            }
        }
        (element.parent as LeoVariableOrFreeConstant).reference?.resolve()?.let {
            if (it is LeoVariableDeclaration) {
                return VARIABLE_DECLARATION_KEY
            }
        }
        annotateError(holder, "Unresolved variable reference: ${element.text}")
        return null
    }

    private fun highlightRecordName(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val file = element.containingFile
        PsiTreeUtil.getChildrenOfType(file.originalElement, LeoRecordDeclaration::class.java)?.forEach {
            if (it.name == element.text) {
                return RECORD_DECLARATION_KEY
            }
        }
        PsiTreeUtil.getChildrenOfType(file.originalElement, LeoCircuitDeclaration::class.java)?.forEach {
            if (it.name == element.text) {
                return RECORD_DECLARATION_KEY
            }
        }
        if (element.parent.elementType == CIRCUIT_EXPRESSION_IDENTIFIER) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved circuit / record reference: ${element.text}")
                .textAttributes(REFERENCE_ERROR_KEY)
                .create()
        }
        // TODO: named_type -> identifier but not user-defined type?
        annotateError(holder, "Unresolved reference: ${element.text}")
        return null
    }

    private fun highlightCircuitComponentWithReference(
        element: PsiElement,
        holder: AnnotationHolder
    ): TextAttributesKey? {
        val circuitComponent = element.parent as LeoCircuitComponentIdentifier
        val reference = circuitComponent.reference ?: return null
        reference.resolve()?.let {
            return CIRCUIT_COMPONENT_KEY
        }

        annotateError(holder, "Unresolved circuit component reference: ${element.text}")
        return null
    }

    private fun highlightFunctionCall(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val functionCall = element.parent as LeoFunctionIdentifier
        val reference = functionCall.reference ?: return null
        reference.resolve()?.let {
            return FREE_FUNCTION_CALL_KEY
        }

        annotateError(holder, "Unresolved function reference: ${element.text}")
        return null
    }

    private fun annotateError(holder: AnnotationHolder, message: String) {
        holder.newAnnotation(HighlightSeverity.ERROR, message)
            .textAttributes(REFERENCE_ERROR_KEY)
            .create()
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
            "LEO_CIRCUIT_COMPONENT_DECLARATION", DefaultLanguageHighlighterColors.STATIC_FIELD
        )
        val REFERENCE_ERROR_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_WRONG_REFERENCE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES
        )
        val MAPPING_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_MAPPING_DECLARATION", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
        )
    }
}