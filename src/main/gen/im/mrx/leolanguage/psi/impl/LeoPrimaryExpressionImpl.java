// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static im.mrx.leolanguage.psi.LeoTypes.*;
import im.mrx.leolanguage.psi.*;

public class LeoPrimaryExpressionImpl extends LeoExpressionImpl implements LeoPrimaryExpression {

  public LeoPrimaryExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitPrimaryExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoAssociatedConstant getAssociatedConstant() {
    return findChildByClass(LeoAssociatedConstant.class);
  }

  @Override
  @Nullable
  public LeoExpression getExpression() {
    return findChildByClass(LeoExpression.class);
  }

  @Override
  @Nullable
  public LeoFreeFunctionCall getFreeFunctionCall() {
    return findChildByClass(LeoFreeFunctionCall.class);
  }

  @Override
  @Nullable
  public LeoLiteral getLiteral() {
    return findChildByClass(LeoLiteral.class);
  }

  @Override
  @Nullable
  public LeoStaticFunctionCall getStaticFunctionCall() {
    return findChildByClass(LeoStaticFunctionCall.class);
  }

  @Override
  @Nullable
  public LeoVariableOrFreeConstant getVariableOrFreeConstant() {
    return findChildByClass(LeoVariableOrFreeConstant.class);
  }

}
