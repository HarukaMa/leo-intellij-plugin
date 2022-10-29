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
            TRANSITION_DECLARATION -> FUNCTION_DECLARATION_KEY
            FINALIZER -> FUNCTION_DECLARATION_KEY
            FUNCTION_PARAMETER -> FUNCTION_PARAMETER_KEY
            RECORD_DECLARATION -> RECORD_DECLARATION_KEY
            STRUCT_DECLARATION -> RECORD_DECLARATION_KEY
            VARIABLE_DECLARATION -> VARIABLE_DECLARATION_KEY
            STATIC_FUNCTION_CALL -> STATIC_FUNCTION_CALL_KEY
            STRUCT_COMPONENT_DECLARATION -> STRUCT_COMPONENT_KEY
            STRUCT_COMPONENT_INITIALIZER -> STRUCT_COMPONENT_KEY
            ASSERT_CALL -> STATIC_FUNCTION_CALL_KEY
            ASSERT_EQUAL_CALL -> STATIC_FUNCTION_CALL_KEY
            ASSERT_NOT_EQUAL_CALL -> STATIC_FUNCTION_CALL_KEY
            MAPPING_DECLARATION -> MAPPING_DECLARATION_KEY

            STRUCT_COMPONENT_IDENTIFIER -> highlightStructComponentWithReference(element, holder)
            VARIABLE_OR_FREE_CONSTANT -> highlightVariable(element, holder)
            NAMED_TYPE -> highlightRecordName(element, holder)
            STRUCT_EXPRESSION_IDENTIFIER -> highlightRecordName(element, holder)
            FUNCTION_IDENTIFIER -> highlightFunctionCall(element, holder)
            LOCATOR -> highlightLocator(element, holder)
            else -> null
        }
    }

    private fun highlightVariable(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val finalizer = PsiTreeUtil.getParentOfType(element, LeoFinalizer::class.java)
        if (finalizer != null) {
            finalizer.functionParameterList?.functionParameterList?.forEach {
                if (it.name == element.text) {
                    return FUNCTION_PARAMETER_KEY
                }
            }
        } else {
            val function = PsiTreeUtil.getParentOfType(element, LeoFunctionLikeDeclaration::class.java) ?: return null
            val parameters = function.functionParameterList ?: return null
            for (parameter in parameters.functionParameterList) {
                if (parameter.name == element.text) {
                    return FUNCTION_PARAMETER_KEY
                }
            }
        }
        (element.parent as? LeoReferenceElement)?.reference?.resolve()?.let {
            return VARIABLE_DECLARATION_KEY
        }
        annotateError(holder, "Unresolved variable reference: ${element.text}")
        return null
    }

    private fun highlightRecordName(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        (element.parent as? LeoReferenceElement)?.reference?.resolve()?.let {
            return RECORD_DECLARATION_KEY
        }
        if (element.parent.elementType == STRUCT_EXPRESSION_IDENTIFIER) {
            holder.newAnnotation(HighlightSeverity.ERROR, "Unresolved struct / record reference: ${element.text}")
                .textAttributes(REFERENCE_ERROR_KEY)
                .create()
        }
        // TODO: named_type -> identifier but not user-defined type?
        annotateError(holder, "Unresolved reference: ${element.text}")
        return null
    }

    private fun highlightStructComponentWithReference(
        element: PsiElement,
        holder: AnnotationHolder
    ): TextAttributesKey? {
        val structComponent = element.parent as LeoStructComponentIdentifier
        val reference = structComponent.reference ?: return null
        reference.resolve()?.let {
            return STRUCT_COMPONENT_KEY
        }

        annotateError(holder, "Unresolved struct component reference: ${element.text}")
        return null
    }

    private fun highlightFunctionCall(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val functionCall = element.parent as LeoFunctionIdentifier
        functionCall.reference?.resolve()?.let {
            return FREE_FUNCTION_CALL_KEY
        }

        annotateError(holder, "Unresolved function reference: ${element.text}")
        return null
    }

    private fun highlightLocator(element: PsiElement, holder: AnnotationHolder): TextAttributesKey? {
        val locator = element.parent as LeoLocator
        locator.reference?.resolve()?.let {
            when (it) {
                is LeoFunctionDeclaration -> return FREE_FUNCTION_CALL_KEY
                is LeoTransitionDeclaration -> return FREE_FUNCTION_CALL_KEY
                is LeoRecordDeclaration -> return RECORD_DECLARATION_KEY
                else -> return@let
            }
        }

        annotateError(holder, "Unresolved locator reference: ${element.text}")
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
        val STRUCT_COMPONENT_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_STRUCT_COMPONENT_DECLARATION", DefaultLanguageHighlighterColors.STATIC_FIELD
        )
        val REFERENCE_ERROR_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_WRONG_REFERENCE", CodeInsightColors.WRONG_REFERENCES_ATTRIBUTES
        )
        val MAPPING_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_MAPPING_DECLARATION", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE
        )
    }
}