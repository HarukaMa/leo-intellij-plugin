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
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.stub.IndexUtils

object LeoTypeCompletionProvider : LeoCompletionProvider() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val arrayList = arrayListOf<LeoNamedElement>()
        val file = parameters.originalFile
        arrayList.addAll(
            LeoUtils.getProgramChildrenOfTypeInFile(
                file,
                LeoStructLikeDeclaration::class.java
            )
        )
        // disallow external records on struct component declaration
        if (PsiTreeUtil.getTopmostParentOfType(
                parameters.position,
                LeoStructComponentDeclaration::class.java
            ) == null
        ) {
            PsiTreeUtil.getChildrenOfType(file, LeoImportDeclaration::class.java)?.forEach {
                val filename = it.importProgramId?.text ?: return@forEach
                val importedFile = LeoUtils.getImportFile(file, filename) ?: return@forEach
                IndexUtils.getNamedElementKeysFromFile(importedFile).forEach inner@{ key ->
                    val element = IndexUtils.getNamedElementFromFile(key, importedFile) ?: return@inner
                    if (element is LeoRecordDeclaration) {
                        arrayList.add(element)
                    }
                }
            }
        }
        // disallow recursive struct component declaration
        PsiTreeUtil.getTopmostParentOfType(parameters.position, LeoStructLikeDeclaration::class.java)?.let {
            arrayList.removeIf { element ->
                element.name == it.name
            }
        }

        listOf(
            "u8",
            "u16",
            "u32",
            "u64",
            "u128",
            "i8",
            "i16",
            "i32",
            "i64",
            "i128",
            "bool",
            "field",
            "group",
            "scalar",
            "address",
            "string"
        ).forEach {
            result.addElement(
                LookupElementBuilder.create(it)
            )
        }
        arrayList.forEach {
            val name = it.name ?: "<BUG IN PLUGIN>"
            result.addElement(
                LookupElementBuilder
                    .create(name)
                    .withPsiElement(it as PsiElement)
                    .withIcon(if (it is LeoStructDeclaration) AleoIcons.STRUCT else AleoIcons.RECORD)
                    .withInsertHandler { context, _ ->
                        if (it.containingFile != file && PsiTreeUtil.getTopmostParentOfType(
                                parameters.position,
                                LeoFunctionParameter::class.java
                            ) != null
                        ) {
                            EditorModificationUtil.moveCaretRelatively(context.editor, -name.length)
                            EditorModificationUtil.insertStringAtCaret(context.editor, it.containingFile.name + "/")
                            EditorModificationUtil.moveCaretRelatively(context.editor, name.length)
                            EditorModificationUtil.insertStringAtCaret(context.editor, ".record")
                        }
                    }
            )
        }
    }

    override val elementPattern: ElementPattern<PsiElement>
        get() = psiElement(LeoTypes.IDENTIFIER).and(
            psiElement().withParent(LeoNamedType::class.java)
        )
}