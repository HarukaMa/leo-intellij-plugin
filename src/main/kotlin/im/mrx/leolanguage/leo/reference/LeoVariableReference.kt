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

package im.mrx.leolanguage.leo.reference

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.ResolveCache
import com.intellij.psi.util.PsiTreeUtil
import im.mrx.leolanguage.leo.psi.LeoBlock
import im.mrx.leolanguage.leo.psi.LeoFinalizer
import im.mrx.leolanguage.leo.psi.LeoFunctionLikeDeclaration
import im.mrx.leolanguage.leo.psi.LeoVariableOrFreeConstant

class LeoVariableReference(element: LeoVariableOrFreeConstant) : LeoReferenceBase<LeoVariableOrFreeConstant>(element) {

//    constructor(element: LeoVariableOrFreeConstant, textRange: TextRange) : super(element, textRange)

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, Resolver, false, false)
    }


    object Resolver : ResolveCache.Resolver {

        override fun resolve(ref: PsiReference, incompleteCode: Boolean): PsiElement? {
            val element = ref.element
            var block = PsiTreeUtil.getParentOfType(element, LeoBlock::class.java)
            while (block != null) {
                block.variableDeclarationList.forEach {
                    if (it.name == element.text) {
                        return it
                    }
                }
                block = PsiTreeUtil.getParentOfType(block, LeoBlock::class.java)
            }
            val finalizer = PsiTreeUtil.getParentOfType(element, LeoFinalizer::class.java)
            if (finalizer != null) {
                finalizer.functionParameterList?.functionParameterList?.forEach {
                    if (it.name == element.text) {
                        return it
                    }
                }
            } else {
                val function = PsiTreeUtil.getParentOfType(element, LeoFunctionLikeDeclaration::class.java)
                    ?: error("Variable outside of functions")
                function.functionParameterList?.functionParameterList?.forEach {
                    if (it.name == element.text) {
                        return it
                    }
                }
            }

            return null
        }

    }

}