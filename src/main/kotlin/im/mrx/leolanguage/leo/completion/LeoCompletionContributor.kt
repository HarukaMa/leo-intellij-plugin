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

package im.mrx.leolanguage.leo.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import im.mrx.leolanguage.leo.completion.provider.*

class LeoCompletionContributor : CompletionContributor() {

    init {
        extend(LeoTypeCompletionProvider)
        extend(LeoStructExpressionIdentifierCompletionProvider)
        extend(LeoStructComponentCompletionProvider)
        extend(LeoVariableCompletionProvider)
        extend(LeoStatementCompletionProvider)
        extend(LeoFunctionCompletionProvider)
        extend(LeoDeclarationCompletionProvider)
        extend(LeoAnnotationCompletionProvider)
        extend(LeoMappingCompletionProvider)
        extend(LeoRootCompletionProvider)
        extend(LeoImportCompletionProvider)
    }

    private fun extend(p: LeoCompletionProvider) {
        extend(CompletionType.BASIC, p.elementPattern, p)
    }

    // for completion pattern debugging
    @Suppress("RedundantOverride")
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
//        println(parameters.position.elementType)
//        println(parameters.position.text)
//        println(parameters.position.parent.elementType)
//        println(parameters.position.parent.text)
        super.fillCompletionVariants(parameters, result)
    }

}