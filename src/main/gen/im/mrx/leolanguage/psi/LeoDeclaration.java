// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoDeclaration extends PsiElement {

  @Nullable
  LeoCircuitDeclaration getCircuitDeclaration();

  @Nullable
  LeoFunctionDeclaration getFunctionDeclaration();

  @Nullable
  LeoRecordDeclaration getRecordDeclaration();

  @Nullable
  PsiElement getBlockComment();

  @Nullable
  PsiElement getEndOfLineComment();

}
