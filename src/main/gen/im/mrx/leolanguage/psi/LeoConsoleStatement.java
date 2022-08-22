// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoConsoleStatement extends PsiElement {

  @Nullable
  LeoAssertCall getAssertCall();

  @Nullable
  LeoAssertEqualCall getAssertEqualCall();

  @Nullable
  LeoAssertNotEqualCall getAssertNotEqualCall();

}
