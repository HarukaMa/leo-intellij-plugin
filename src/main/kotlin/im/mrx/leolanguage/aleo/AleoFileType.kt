package im.mrx.leolanguage.aleo

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class AleoFileType : LanguageFileType(AleoLanguage.INSTANCE) {

    companion object {
        @Suppress("unused")
        val INSTANCE = AleoFileType()
    }

    override fun getName(): String {
        return "Aleo"
    }

    override fun getDescription(): String {
        return "Aleo instructions"
    }

    override fun getDefaultExtension(): String {
        return "aleo"
    }

    override fun getIcon(): Icon {
        return AleoIcons.FILE
    }

}