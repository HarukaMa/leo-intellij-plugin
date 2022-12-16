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

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

abstract class LeoIdentifierItemImplMixin(node: ASTNode) : LeoNamedElementImpl(node), LeoIdentifierItem,
    LeoTypedElement {

    private fun getType(): Any? {
        val identifierItem = this as LeoIdentifierItem
        val declaration =
            PsiTreeUtil.getParentOfType(identifierItem, LeoVariableLikeDeclaration::class.java) ?: return null
        val identifiers = declaration.identifierOrIdentifiers ?: return null
        val identifierItems = identifiers.identifierItemList
        val identifierCount = identifierItems.size
        if (identifierCount == 1) {
            return declaration.namedType ?: declaration.tupleType ?: declaration.unitType
        }
        // so it must be multiple declaration
        if (declaration.tupleType == null || declaration.tupleType?.children?.size != identifierCount) {
            return null
        }
        val identifierIndex = identifierItems.indexOf(identifierItem)
        declaration.tupleType?.children?.get(identifierIndex)?.let {
            return it
        }
        return null
    }

    override val namedType: LeoNamedType?
        get() {
            val type = getType()
            return if (type is LeoNamedType) {
                type
            } else {
                null
            }
        }

    override val tupleType: LeoTupleType?
        get() {
            val type = getType()
            return if (type is LeoTupleType) {
                type
            } else {
                null
            }
        }

    override val unitType: LeoUnitType?
        get() {
            val type = getType()
            return if (type is LeoUnitType) {
                type
            } else {
                null
            }
        }

}