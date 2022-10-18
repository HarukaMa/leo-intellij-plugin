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

package im.mrx.leolanguage.leo.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import im.mrx.leolanguage.aleo.AleoIcons
import javax.swing.Icon

class LeoColorSettingsPage : ColorSettingsPage {
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> {
        return arrayOf(
            AttributesDescriptor("Keyword", LeoSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Identifier", LeoSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("String", LeoSyntaxHighlighter.STRING),
            AttributesDescriptor("Comments//Block comment", LeoSyntaxHighlighter.BLOCK_COMMENT),
            AttributesDescriptor("Comments//Line comment", LeoSyntaxHighlighter.LINE_COMMENT),
            AttributesDescriptor("Number", LeoSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Braces and Operators//Parenthesis", LeoSyntaxHighlighter.PARENS),
            AttributesDescriptor("Braces and Operators//Braces", LeoSyntaxHighlighter.BRACES),
            AttributesDescriptor("Braces and Operators//Brackets", LeoSyntaxHighlighter.BRACKETS),
            AttributesDescriptor("Braces and Operators//Comma", LeoSyntaxHighlighter.COMMA),
            AttributesDescriptor("Braces and Operators//Semicolon", LeoSyntaxHighlighter.SEMICOLON),
            AttributesDescriptor("Braces and Operators//Dot", LeoSyntaxHighlighter.DOT),

            AttributesDescriptor("Annotation", LeoHighlightingAnnotator.ANNOTATION_KEY),
            AttributesDescriptor("Local variable", LeoHighlightingAnnotator.VARIABLE_DECLARATION_KEY),
            AttributesDescriptor("Records//Record declaration", LeoHighlightingAnnotator.RECORD_DECLARATION_KEY),
            AttributesDescriptor("Records//Record component", LeoHighlightingAnnotator.STRUCT_COMPONENT_KEY),
            AttributesDescriptor("Functions//Function declaration", LeoHighlightingAnnotator.FUNCTION_DECLARATION_KEY),
            AttributesDescriptor("Functions//Function call", LeoHighlightingAnnotator.FREE_FUNCTION_CALL_KEY),
            AttributesDescriptor("Functions//Static function call", LeoHighlightingAnnotator.STATIC_FUNCTION_CALL_KEY),
            AttributesDescriptor("Mappings//Mapping declaration", LeoHighlightingAnnotator.MAPPING_DECLARATION_KEY),
            AttributesDescriptor("Parameters//Function parameter", LeoHighlightingAnnotator.FUNCTION_PARAMETER_KEY),

            )
    }

    override fun getColorDescriptors(): Array<ColorDescriptor> {
        return ColorDescriptor.EMPTY_ARRAY
    }

    override fun getDisplayName(): String {
        return "Leo"
    }

    override fun getIcon(): Icon {
        return AleoIcons.FILE
    }

    override fun getHighlighter(): SyntaxHighlighter {
        return LeoSyntaxHighlighter()
    }

    override fun getDemoText(): String {
        return """
            // The 'test' main function.
            <ANNOTATION>@program</ANNOTATION>
            function <FUNCTION_DECLARATION>main</FUNCTION_DECLARATION>(public <FUNCTION_PARAMETER>a</FUNCTION_PARAMETER>: u32, <FUNCTION_PARAMETER>b</FUNCTION_PARAMETER>: u32) -> u32 {
                let c: u32 = <FUNCTION_PARAMETER>a</FUNCTION_PARAMETER> + <FUNCTION_PARAMETER>b</FUNCTION_PARAMETER>;
                return c;
            }
        """.trimIndent()
    }

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey> {
        return mapOf(
            "ANNOTATION" to LeoHighlightingAnnotator.ANNOTATION_KEY,
            "FUNCTION_PARAMETER" to LeoHighlightingAnnotator.FUNCTION_PARAMETER_KEY,
            "FUNCTION_DECLARATION" to LeoHighlightingAnnotator.FUNCTION_DECLARATION_KEY
        ).toMutableMap()
    }
}