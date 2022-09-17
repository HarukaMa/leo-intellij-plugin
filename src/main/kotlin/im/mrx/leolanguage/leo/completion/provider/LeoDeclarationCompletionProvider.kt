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
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.LeoDeclaration
import im.mrx.leolanguage.leo.psi.LeoFile

object LeoDeclarationCompletionProvider : LeoCompletionProvider() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        listOf("function", "record", "circuit", "mapping").forEach {
            result.addElement(
                LookupElementBuilder
                    .create(it)
                    .withInsertHandler { ctx, _ ->
                        ctx.document.insertString(ctx.selectionEndOffset, " ")
                        EditorModificationUtil.moveCaretRelatively(ctx.editor, 1)
                    }
            )
        }
    }


    override val elementPattern: ElementPattern<PsiElement>
        get() = PlatformPatterns.or(
            // start of declaration
            psiElement().withParent(
                psiElement(PsiErrorElement::class.java).withParent(LeoFile::class.java)
            ),
            // end of function declaration, possible `finalize`
            psiElement().withParent(
                psiElement(PsiErrorElement::class.java).withParent(
                    LeoDeclaration::class.java
                )
            )
        )
}