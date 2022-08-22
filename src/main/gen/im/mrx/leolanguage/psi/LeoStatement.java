// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoStatement extends PsiElement {

  @Nullable
  LeoAssignmentStatement getAssignmentStatement();

  @Nullable
  LeoBlock getBlock();

  @Nullable
  LeoConditionalStatement getConditionalStatement();

  @Nullable
  LeoConsoleStatement getConsoleStatement();

  @Nullable
  LeoConstantDeclaration getConstantDeclaration();

  @Nullable
  LeoLoopStatement getLoopStatement();

  @Nullable
  LeoReturnStatement getReturnStatement();

  @Nullable
  LeoVariableDeclaration getVariableDeclaration();

}
