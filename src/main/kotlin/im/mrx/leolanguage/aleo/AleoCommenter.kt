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

import com.intellij.lang.Commenter

class AleoCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "//"
    override fun getBlockCommentPrefix(): String? = null
    override fun getBlockCommentSuffix(): String? = null
    override fun getCommentedBlockCommentPrefix(): String? = null
    override fun getCommentedBlockCommentSuffix(): String? = null
}