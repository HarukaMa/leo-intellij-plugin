/*
 * Copyright (c) 2023 Haruka Ma
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
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.util.elementType
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.LeoPrimaryExpression
import im.mrx.leolanguage.leo.psi.LeoReturnStatement
import im.mrx.leolanguage.leo.psi.LeoVariable

object LeoReturnFinalizeCompletionProvider : LeoCompletionProvider() {

    // Check prev sibling
    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement().withParent(psiElement(LeoVariable::class.java).withParent(psiElement(LeoPrimaryExpression::class.java)))

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        var element = parameters.position.parent.parent.parent
        while (element.prevSibling != null && element.prevSibling.elementType == WHITE_SPACE) {
            element = element.prevSibling
        }
        if (element.prevSibling is LeoReturnStatement) {
            result.addElement(
                LookupElementBuilder.create("then finalize()")
            )
        }
    }


}