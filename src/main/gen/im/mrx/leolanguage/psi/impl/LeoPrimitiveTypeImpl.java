// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static im.mrx.leolanguage.psi.LeoTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import im.mrx.leolanguage.psi.*;

public class LeoPrimitiveTypeImpl extends ASTWrapperPsiElement implements LeoPrimitiveType {

  public LeoPrimitiveTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitPrimitiveType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoAddressType getAddressType() {
    return findChildByClass(LeoAddressType.class);
  }

  @Override
  @Nullable
  public LeoArithmeticType getArithmeticType() {
    return findChildByClass(LeoArithmeticType.class);
  }

  @Override
  @Nullable
  public LeoBooleanType getBooleanType() {
    return findChildByClass(LeoBooleanType.class);
  }

  @Override
  @Nullable
  public LeoStringType getStringType() {
    return findChildByClass(LeoStringType.class);
  }

}
