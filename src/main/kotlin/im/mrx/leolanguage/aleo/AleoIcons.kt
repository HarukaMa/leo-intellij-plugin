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

package im.mrx.leolanguage.aleo

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader

class AleoIcons {
    companion object {
        val FILE = IconLoader.getIcon("/icons/aleo.svg", AleoIcons::class.java)
        val RECORD = IconLoader.getIcon("/icons/record.svg", AleoIcons::class.java)
        val FUNCTION = AllIcons.Nodes.Function
        val CIRCUIT = IconLoader.getIcon("/icons/circuit.svg", AleoIcons::class.java)
        val CLOSURE = IconLoader.getIcon("/icons/closure.svg", AleoIcons::class.java)
        val INTERFACE = IconLoader.getIcon("/icons/interface.svg", AleoIcons::class.java)
        val CIRCUIT_COMPONENT = AllIcons.Nodes.Field
    }
}