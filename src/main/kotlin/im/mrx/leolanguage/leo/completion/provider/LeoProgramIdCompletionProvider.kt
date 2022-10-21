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
import com.intellij.util.ProcessingContext
import im.mrx.leolanguage.leo.completion.LeoCompletionProvider
import im.mrx.leolanguage.leo.psi.LeoProgramDeclaration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object LeoProgramIdCompletionProvider : LeoCompletionProvider() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val file = parameters.originalFile
        val name = if (file.containingDirectory.name == "src") {
            val json = file.containingDirectory.parentDirectory?.findFile("program.json") ?: return
            Json.parseToJsonElement(json.text).jsonObject["program"]?.jsonPrimitive?.content ?: return
        } else {
            file.name.replace(".leo", ".aleo")
        }
        result.addElement(LookupElementBuilder.create(name))
    }


    override val elementPattern: ElementPattern<PsiElement>
        // root scope
        get() = psiElement().withParent(LeoProgramDeclaration::class.java)
}