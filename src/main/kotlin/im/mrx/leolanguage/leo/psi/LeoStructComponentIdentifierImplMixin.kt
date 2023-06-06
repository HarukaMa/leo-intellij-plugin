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

package im.mrx.leolanguage.leo.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import im.mrx.leolanguage.leo.reference.LeoStructComponentReference

abstract class LeoStructComponentIdentifierImplMixin(node: ASTNode) : ASTWrapperPsiElement(node),
    LeoStructComponentIdentifier {

    override fun getReference(): PsiReference? {
        return LeoStructComponentReference(this)
    }

    override fun referenceNameElement(): PsiElement? {
        return this.identifier
    }

    override val typeElement: PsiElement?
        get() {
            when (parent) {
                is LeoStructComponentExpression -> {
                    val expression =
                        (parent as? LeoStructComponentExpression)?.expression ?: return null
                    val reference = when (expression) {
                        is LeoStructComponentExpression -> {
                            expression.lastChild.reference?.resolve() ?: return null
                        }

                        is LeoPrimaryExpression -> {
                            expression.variable?.reference?.resolve() ?: return null
                        }

                        else -> {
                            return null
                        }
                    }
                    if (reference is LeoTypedElement) {
                        return (reference.namedType ?: reference.tupleType ?: reference.unitType)?.reference?.resolve()
                    }
                    for (child in reference.children) {
                        if (child is LeoNamedType) {
                            return child.reference?.resolve()
                        }
                    }
                }

                is LeoStructComponentInitializer -> {
                    val expression = parent.parent as? LeoStructExpression ?: return null
                    return expression.structExpressionIdentifier.reference?.resolve()
                }
            }

            return null
        }

}