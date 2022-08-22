// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface LeoPrimaryExpression extends LeoExpression {

  @Nullable
  LeoAssociatedConstant getAssociatedConstant();

  @Nullable
  LeoExpression getExpression();

  @Nullable
  LeoFreeFunctionCall getFreeFunctionCall();

  @Nullable
  LeoLiteral getLiteral();

  @Nullable
  LeoStaticFunctionCall getStaticFunctionCall();

  @Nullable
  LeoVariableOrFreeConstant getVariableOrFreeConstant();

}
