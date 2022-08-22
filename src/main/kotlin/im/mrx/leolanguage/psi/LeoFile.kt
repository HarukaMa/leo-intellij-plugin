package im.mrx.leolanguage.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import im.mrx.leolanguage.LeoFileType
import im.mrx.leolanguage.LeoLanguage

class LeoFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, LeoLanguage.INSTANCE) {
    override fun getFileType(): FileType = LeoFileType.INSTANCE

    override fun toString(): String {
        return "Leo File"
    }
}