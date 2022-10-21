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
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.stub.IndexUtils

class LeoTypeReference(element: LeoNamedType) : LeoReferenceBase<LeoNamedType>(element) {

    override fun resolve(): PsiElement? {
        return ResolveCache.getInstance(element.project).resolveWithCaching(this, Resolver, false, false)
    }

    object Resolver : ResolveCache.Resolver {

        override fun resolve(ref: PsiReference, incompleteCode: Boolean): PsiElement? {
            val element = ref.element as LeoNamedType
            LeoUtils.getProgramChildrenOfTypeInFile(element.containingFile, LeoStructLikeDeclaration::class.java)
                .forEach {
                    if (it.name == element.text) {
                        return it
                    }
                }

            // try imported files
            // locator is required for function parameter
            if (element.parent.elementType == LeoTypes.FUNCTION_PARAMETER) {
                element.locator?.let {
                    val file = LeoUtils.getImportFile(it.containingFile, it.programId.text) ?: return null
                    IndexUtils.getNamedElementFromFile(it.identifier?.text ?: "", file)?.let { el ->
                        if (el is LeoRecordDeclaration) {
                            return el
                        }
                    }
                }
            } else {
                // plain type name is required for other locations
                PsiTreeUtil.getChildrenOfType(element.containingFile, LeoImportDeclaration::class.java)?.forEach {
                    val file =
                        LeoUtils.getImportFile(element.containingFile, it.importProgramId?.text ?: return@forEach)
                            ?: return@forEach
                    IndexUtils.getNamedElementKeysFromFile(file).find { key -> key == element.text }?.let { key ->
                        IndexUtils.getNamedElementFromFile(key, file)?.let { el ->
                            if (el is LeoRecordDeclaration) {
                                return el
                            }
                        }
                    }
                }
            }

            return null
        }

    }

}