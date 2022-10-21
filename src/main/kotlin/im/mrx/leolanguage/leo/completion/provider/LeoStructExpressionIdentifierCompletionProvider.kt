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
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.aleo.AleoIcons
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*

object LeoStructExpressionIdentifierCompletionProvider : LeoCompletionProvider() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        // we should have known the exact type of the expression
        val declaration = PsiTreeUtil.getParentOfType(parameters.position, LeoVariableDeclaration::class.java) ?: return
        val type = declaration.namedType ?: return
        val element = (type.reference?.resolve() as? LeoNamedElement) ?: return
        result.addElement(
            LookupElementBuilder
                .create(element.name ?: "?")
                .withIcon(if (element is LeoRecordDeclaration) AleoIcons.RECORD else AleoIcons.STRUCT)
                .withInsertHandler { ctx, _ ->
                    ctx.document.insertString(ctx.selectionEndOffset, " {}")
                    EditorModificationUtil.moveCaretRelatively(ctx.editor, 2)
                }
        )
    }

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement(LeoTypes.IDENTIFIER).and(
            // we have to go long way to detect if we should provide record type completion
            // we are trying to find the struct expression identifier here:
            psiElement()
                // the identifier is part of variable declaration...
                .withSuperParent(
                    3, psiElement(LeoVariableDeclaration::class.java)
                        // ... and the variable type...
                        .withChild(
                            psiElement(LeoNamedType::class.java)
                                // ... is a record type
                                .referencing(psiElement(LeoStructLikeDeclaration::class.java))
                        )
                )
            // TODO: struct expression outside of variable declaration?
        )
}