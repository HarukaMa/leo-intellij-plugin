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

package im.mrx.leolanguage.leo.stub

import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.psi.impl.*


fun factory(name: String): LeoSymbolStubType<*> {
    return when (name) {
        "STRUCT_DECLARATION" -> LeoSymbolStubType<LeoStructDeclaration>(
            "STRUCT_DECLARATION",
            ::LeoStructDeclarationImpl
        )

        "RECORD_DECLARATION" -> LeoSymbolStubType<LeoRecordDeclaration>(
            "RECORD_DECLARATION",
            ::LeoRecordDeclarationImpl
        )

        "FUNCTION_DECLARATION" -> LeoSymbolStubType<LeoFunctionDeclaration>(
            "FUNCTION_DECLARATION",
            ::LeoFunctionDeclarationImpl
        )

        "TRANSITION_DECLARATION" -> LeoSymbolStubType<LeoTransitionDeclaration>(
            "TRANSITION_DECLARATION",
            ::LeoTransitionDeclarationImpl
        )

        "STRUCT_COMPONENT_DECLARATION" -> LeoSymbolStubType<LeoStructComponentDeclaration>(
            "STRUCT_COMPONENT_DECLARATION",
            ::LeoStructComponentDeclarationImpl
        )

        "MAPPING_DECLARATION" -> LeoSymbolStubType<LeoMappingDeclaration>(
            "MAPPING_DECLARATION",
            ::LeoMappingDeclarationImpl
        )

        else -> error("Unknown symbol $name")
    }
}