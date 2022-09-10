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
import im.mrx.leolanguage.leo.psi.LeoDeclaration
import im.mrx.leolanguage.leo.psi.LeoFunctionIdentifier

class LeoFunctionReference(element: LeoFunctionIdentifier) : LeoReferenceBase<LeoFunctionIdentifier>(element) {

//    constructor(element: LeoVariableOrFreeConstant, textRange: TextRange) : super(element, textRange)

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, Resolver, false, false)
    }


    object Resolver : ResolveCache.Resolver {

        override fun resolve(ref: PsiReference, incompleteCode: Boolean): PsiElement? {
            val element = ref.element as LeoFunctionIdentifier
            PsiTreeUtil.getChildrenOfType(element.containingFile, LeoDeclaration::class.java)?.forEach {
                if (it.functionDeclaration?.name == element.text) {
                    return it.functionDeclaration
                }
            }
            return null
        }

    }

}