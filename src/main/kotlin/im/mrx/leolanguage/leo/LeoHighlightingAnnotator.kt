/*


 */

package im.mrx.leolanguage.leo

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.psi.LeoTypes.*

class LeoHighlightingAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (holder.isBatchMode) {
            return
        }
        val attribute = when (element) {

            is LeafPsiElement -> highlight_leaf(element)
            else -> null
        } ?: return

        holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
            .textAttributes(attribute)
            .create()
    }

    private fun highlight_leaf(element: PsiElement): TextAttributesKey? {
        return when (element.elementType) {
            ANNOTATION -> ANNOTATION_KEY
            IDENTIFIER -> highlight_identifier(element)
            else -> null
        }
    }

    private fun highlight_identifier(element: PsiElement): TextAttributesKey? {
        val parent = element.parent

        return when (parent.elementType) {
            FUNCTION_DECLARATION -> FUNCTION_DECLARATION_KEY
            FUNCTION_PARAMETER -> FUNCTION_PARAMETER_KEY
            else -> null
        }
    }

    companion object {
        val ANNOTATION_KEY =
            TextAttributesKey.createTextAttributesKey("LEO_ANNOTATION", DefaultLanguageHighlighterColors.METADATA)
        val FUNCTION_DECLARATION_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_FUNCTION_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION
        )
        val FUNCTION_PARAMETER_KEY = TextAttributesKey.createTextAttributesKey(
            "LEO_FUNCTION_PARAMETER",
            DefaultLanguageHighlighterColors.PARAMETER
        )
    }
}