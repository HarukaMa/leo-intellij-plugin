/*
 * Copyright (c) 2022-2023 Haruka Ma
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
import im.mrx.leolanguage.leo.psi.*

class LeoVariableReference(element: LeoVariable) : LeoReferenceBase<LeoVariable>(element) {

//    constructor(element: LeoVariableOrFreeConstant, textRange: TextRange) : super(element, textRange)

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, Resolver, false, false)
    }


    object Resolver : ResolveCache.Resolver {

        override fun resolve(ref: PsiReference, incompleteCode: Boolean): PsiElement? {
            val element = ref.element
            var block = PsiTreeUtil.getParentOfType(element, LeoBlock::class.java)
            while (block != null) {
                block.variableDeclarationList.forEach { vd ->
                    PsiTreeUtil.findChildrenOfType(vd, LeoIdentifierItem::class.java).forEach { id ->
                        if (id.name == element.text) {
                            return id
                        }
                    }
                }
                block = PsiTreeUtil.getParentOfType(block, LeoBlock::class.java)
            }
            PsiTreeUtil.getParentOfType(element, LeoLoopStatement::class.java)?.let {
                if (it.identifier?.text == element.text) {
                    return it
                }
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

            if (PsiTreeUtil.getParentOfType(element, LeoMethodCall::class.java) != null) {
                val program = PsiTreeUtil.getParentOfType(element, LeoProgramBlock::class.java) ?: return null
                program.mappingDeclarationList.forEach { md ->
                    if (md.identifier?.text == element.text) {
                        return md
                    }
                }
            }

            val assoc = PsiTreeUtil.getParentOfType(element, LeoAssociatedFunctionCall::class.java)
            if (assoc != null && assoc.namedType.text == "Mapping") {
                val program = PsiTreeUtil.getParentOfType(element, LeoProgramBlock::class.java) ?: return null
                program.mappingDeclarationList.forEach { md ->
                    if (md.identifier?.text == element.text) {
                        return md
                    }
                }
            }

            return null
        }

    }

}