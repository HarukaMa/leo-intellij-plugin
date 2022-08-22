package im.mrx.leolanguage

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
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
            AttributesDescriptor("Functions//Function declaration", LeoHighlightingAnnotator.FUNCTION_DECLARATION_KEY),
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
                let c: u32 = a + b;
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