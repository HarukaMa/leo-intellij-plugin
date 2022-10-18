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
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.aleo.AleoIcons
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.LeoFunctionIdentifier
import im.mrx.leolanguage.leo.psi.LeoFunctionLikeDeclaration

object LeoFunctionCompletionProvider : LeoCompletionProvider() {
    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement().withParent(LeoFunctionIdentifier::class.java)

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        addFunctions(parameters, result)
    }

    fun addFunctions(parameters: CompletionParameters, result: CompletionResultSet) {
        val element = parameters.position
        LeoUtils.getProgramChildrenOfTypeInFile(element.containingFile, LeoFunctionLikeDeclaration::class.java)
            .forEach { function ->
                result.addElement(
                    LookupElementBuilder
                        .create(function.name ?: "<BUG IN PLUGIN>")
                        .withPsiElement(function)
                        .withIcon(if (LeoUtils.functionIsTransition(function)) AleoIcons.TRANSITION else AleoIcons.FUNCTION)
                        .withTypeText(LeoUtils.typeToString(function))
                        .withTailText("(${LeoUtils.functionParameterListToString(function)})")
                        .withInsertHandler { context, _ ->
                            val seq = context.document.charsSequence
                            var offset = context.tailOffset
                            while (seq[offset] == ' ' || seq[offset] == '\t') {
                                offset += 1
                            }
                            if (seq[offset] != '(') {
                                context.document.insertString(offset, "();")
                                EditorModificationUtil.moveCaretRelatively(context.editor, -2)
                            }
                        }
                )
            }
    }

}