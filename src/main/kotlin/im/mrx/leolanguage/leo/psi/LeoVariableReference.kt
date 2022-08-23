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

package im.mrx.leolanguage.leo.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil

class LeoVariableReference(element: LeoVariableOrFreeConstant) : PsiReferenceBase<LeoVariableOrFreeConstant>(element) {

//    constructor(element: LeoVariableOrFreeConstant, textRange: TextRange) : super(element, textRange)

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, Resolver, false, false)
    }

    override fun getRangeInElement(): TextRange {
        return ElementManipulators.getValueTextRange(element)
    }


    object Resolver : ResolveCache.Resolver {

        override fun resolve(ref: PsiReference, incompleteCode: Boolean): PsiElement? {
            val element = ref.element
            var block = PsiTreeUtil.getParentOfType(element, LeoBlock::class.java)
            while (block != null) {
                block.variableDeclarationList.let {
                    for (variable in it) {
                        if (variable.name == element.text) {
                            return variable
                        }
                    }
                }
                block = PsiTreeUtil.getParentOfType(block, LeoBlock::class.java)
            }
            val function = PsiTreeUtil.getParentOfType(element, LeoFunctionDeclaration::class.java)
                ?: error("Variable outside of functions")
            function.functionParameters!!.functionParameterList.let {
                for (parameter in it) {
                    if (parameter.name == element.text) {
                        return parameter
                    }
                }
            }

            return null
        }

    }

}