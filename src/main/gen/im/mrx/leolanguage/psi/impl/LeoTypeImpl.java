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

public class LeoTypeImpl extends ASTWrapperPsiElement implements LeoType {

  public LeoTypeImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull LeoVisitor visitor) {
    visitor.visitType(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof LeoVisitor) accept((LeoVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public LeoNamedType getNamedType() {
    return findChildByClass(LeoNamedType.class);
  }

  @Override
  @Nullable
  public LeoTupleType getTupleType() {
    return findChildByClass(LeoTupleType.class);
  }

}
