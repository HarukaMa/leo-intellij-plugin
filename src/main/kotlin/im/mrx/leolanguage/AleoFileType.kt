package im.mrx.leolanguage

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

class AleoFileType : LanguageFileType(AleoLanguage.INSTANCE) {

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