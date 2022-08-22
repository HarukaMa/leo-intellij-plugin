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

public class LeoArithmeticTypeImpl extends ASTWrapperPsiElement implements LeoArithmeticType {

  public LeoArithmeticTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitArithmeticType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoFieldType getFieldType() {
    return findChildByClass(LeoFieldType.class);
  }

  @Override
  @Nullable
  public LeoGroupType getGroupType() {
    return findChildByClass(LeoGroupType.class);
  }

  @Override
  @Nullable
  public LeoIntegerType getIntegerType() {
    return findChildByClass(LeoIntegerType.class);
  }

  @Override
  @Nullable
  public LeoScalarType getScalarType() {
    return findChildByClass(LeoScalarType.class);
  }

}
