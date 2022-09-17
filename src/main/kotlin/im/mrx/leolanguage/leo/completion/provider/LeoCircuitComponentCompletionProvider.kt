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

package im.mrx.leolanguage.leo.completion.provider

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*

object LeoCircuitComponentCompletionProvider : LeoCompletionProvider() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val element = parameters.position
        // token.owner
        PsiTreeUtil.getParentOfType(element, LeoCircuitComponentExpression::class.java)?.let {
            val expression = it.expression as? LeoPrimaryExpression ?: return@let
            val declaration = expression.variableOrFreeConstant?.reference?.resolve() ?: return@let
            val namedType =
                (declaration as? LeoVariableDeclaration)?.namedType ?: (declaration as? LeoFunctionParameter)?.namedType
                ?: return@let
            val type = namedType.reference?.resolve() ?: return@let
            val componentList =
                (type as? LeoRecordDeclaration)?.circuitComponentDeclarations?.circuitComponentDeclarationList
                    ?: (type as? LeoCircuitDeclaration)?.circuitComponentDeclarations?.circuitComponentDeclarationList
                    ?: return@let
            componentList.forEach { component ->
                result.addElement(LookupElementBuilder.create(component))
            }
        }
        // Token { owner }
        PsiTreeUtil.getParentOfType(element, LeoCircuitExpression::class.java)?.let {
            val type = it.circuitExpressionIdentifier.reference?.resolve() ?: return@let
            val componentList =
                (type as? LeoRecordDeclaration)?.circuitComponentDeclarations?.circuitComponentDeclarationList
                    ?: (type as? LeoCircuitDeclaration)?.circuitComponentDeclarations?.circuitComponentDeclarationList
                    ?: return@let
            componentList.forEach { component ->
                result.addElement(LookupElementBuilder.create(component))
            }
        }

    }

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement().withParent(LeoCircuitComponentIdentifier::class.java)

}