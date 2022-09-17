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
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*

object LeoCircuitExpressionIdentifierCompletionProvider : LeoCompletionProvider() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        // we should have known the exact type of the expression
        val declaration = PsiTreeUtil.getParentOfType(parameters.position, LeoVariableDeclaration::class.java) ?: return
        val type = declaration.namedType ?: return
        PsiTreeUtil.getChildrenOfType(parameters.originalFile, LeoDeclaration::class.java)?.forEach {
            if (it.firstChild is LeoRecordDeclaration || it.firstChild is LeoCircuitDeclaration) {
                val typeName =
                    (it.firstChild as? LeoRecordDeclaration)?.name ?: (it.firstChild as? LeoCircuitDeclaration)?.name
                    ?: return@forEach
                if (typeName == type.text) {
                    result.addElement(
                        LookupElementBuilder.create(
                            (it.firstChild as? LeoCircuitDeclaration) ?: it.firstChild as LeoRecordDeclaration
                        )
                    )
                }
            }
        }
    }

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement(LeoTypes.IDENTIFIER).and(
            // we have to go long way to detect if we should provide record type completion
            // we are trying to find the circuit expression identifier here:
            psiElement()
                // the identifier is part of variable declaration...
                .withSuperParent(
                    3, psiElement(LeoVariableDeclaration::class.java)
                        // ... and the variable type...
                        .withChild(
                            psiElement(LeoNamedType::class.java)
                                // ... is a record type
                                .referencing(
                                    PlatformPatterns.or(
                                        psiElement(LeoCircuitDeclaration::class.java),
                                        psiElement(LeoRecordDeclaration::class.java)
                                    )
                                )
                        )
                )
            // TODO: circuit expression outside of variable declaration?
        )
}