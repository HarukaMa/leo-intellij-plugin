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
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.aleo.AleoIcons
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*

object LeoVariableCompletionProvider : LeoCompletionProvider() {

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement().withParent(LeoVariable::class.java)

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        addVariablesInScope(parameters, result)
        LeoFunctionCompletionProvider.addFunctions(parameters, result)
    }

    fun addVariablesInScope(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.position
        var statement: PsiElement? = if (element.parent is LeoBlock) {
            element
        } else {
            PsiTreeUtil.getParentOfType(element, LeoStatement::class.java)
        } ?: return
        while (statement != null) {
            (statement as? LeoVariableDeclaration)?.let {
                result.addElement(
                    LookupElementBuilder
                        .create(it)
                        .withIcon(AleoIcons.VARIABLE)
                        .withTypeText(LeoUtils.typeToString(it))
                )
            }
            statement = statement.prevSibling
        }
        val finalizer = PsiTreeUtil.getParentOfType(element, LeoFinalizer::class.java)
        if (finalizer != null) {
            finalizer.functionParameterList?.functionParameterList?.forEach {
                addElement(result, it)
            }
        } else {
            val functionDeclaration = PsiTreeUtil.getParentOfType(element, LeoFunctionLikeDeclaration::class.java)
            functionDeclaration?.functionParameterList?.functionParameterList?.forEach {
                addElement(result, it)
            }
        }
    }

    private fun addElement(result: CompletionResultSet, element: LeoFunctionParameter) {
        result.addElement(
            LookupElementBuilder
                .create(element)
                .withIcon(AleoIcons.FUNCTION_PARAMETER)
                .withTypeText(LeoUtils.typeToString(element))
        )
    }
}
