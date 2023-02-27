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

package im.mrx.leolanguage.leo.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.aleo.AleoIcons
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*

object LeoStructComponentCompletionProvider : LeoCompletionProvider() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val element = parameters.position
        // token.owner
        PsiTreeUtil.getParentOfType(element, LeoStructComponentExpression::class.java)?.let {
            val expression = it.expression as? LeoPrimaryExpression ?: return@let
            val declaration = expression.variable?.reference?.resolve() ?: return@let
            val namedType =
                (declaration as? LeoVariableDeclaration)?.namedType ?: (declaration as? LeoFunctionParameter)?.namedType
                ?: return@let
            val type = namedType.reference?.resolve() ?: return@let
            val componentList =
                (type as? LeoStructLikeDeclaration)?.structComponentDeclarations?.structComponentDeclarationList
                    ?: return@let
            addElement(result, componentList, false)
        }
        // Token { owner }
        PsiTreeUtil.getParentOfType(element, LeoStructExpression::class.java)?.let {
            val type = it.structExpressionIdentifier.reference?.resolve() ?: return@let
            val componentList =
                (type as? LeoStructLikeDeclaration)?.structComponentDeclarations?.structComponentDeclarationList
                    ?: return@let
            addElement(result, componentList, true)
        }

    }

    private fun addElement(
        result: CompletionResultSet,
        list: List<LeoStructComponentDeclaration>,
        insertColon: Boolean
    ) {
        list.forEach { component ->
            val type = LeoUtils.typeToString(component)
            result.addElement(
                LookupElementBuilder
                    .create(component)
                    .withIcon(AleoIcons.STRUCT_COMPONENT)
                    .withTypeText(type)
                    .withInsertHandler { context, _ ->
                        if (insertColon) {
                            val content = ": "
                            context.document.insertString(context.selectionEndOffset, content)
                            EditorModificationUtil.moveCaretRelatively(context.editor, content.length)
                        }
                    }
            )
        }
    }

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement().withParent(LeoStructComponentIdentifier::class.java)

}