// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import im.mrx.leolanguage.psi.impl.*;

public interface LeoTypes {

  IElementType ADDITIVE_ADD_EXPRESSION = new LeoElementType("ADDITIVE_ADD_EXPRESSION");
  IElementType ADDITIVE_SUB_EXPRESSION = new LeoElementType("ADDITIVE_SUB_EXPRESSION");
  IElementType ADDRESS_TYPE = new LeoElementType("ADDRESS_TYPE");
  IElementType AFFINE_GROUP_LITERAL = new LeoElementType("AFFINE_GROUP_LITERAL");
  IElementType ARITHMETIC_TYPE = new LeoElementType("ARITHMETIC_TYPE");
  IElementType ASSERT_CALL = new LeoElementType("ASSERT_CALL");
  IElementType ASSERT_EQUAL_CALL = new LeoElementType("ASSERT_EQUAL_CALL");
  IElementType ASSERT_NOT_EQUAL_CALL = new LeoElementType("ASSERT_NOT_EQUAL_CALL");
  IElementType ASSIGNMENT_OPERATOR = new LeoElementType("ASSIGNMENT_OPERATOR");
  IElementType ASSIGNMENT_STATEMENT = new LeoElementType("ASSIGNMENT_STATEMENT");
  IElementType ASSOCIATED_CONSTANT = new LeoElementType("ASSOCIATED_CONSTANT");
  IElementType ATOMIC_LITERAL = new LeoElementType("ATOMIC_LITERAL");
  IElementType BINARY_OPERATOR_CALL = new LeoElementType("BINARY_OPERATOR_CALL");
  IElementType BLOCK = new LeoElementType("BLOCK");
  IElementType BOOLEAN_TYPE = new LeoElementType("BOOLEAN_TYPE");
  IElementType BRANCH = new LeoElementType("BRANCH");
  IElementType CIRCUIT_COMPONENT_DECLARATION = new LeoElementType("CIRCUIT_COMPONENT_DECLARATION");
  IElementType CIRCUIT_COMPONENT_DECLARATIONS = new LeoElementType("CIRCUIT_COMPONENT_DECLARATIONS");
  IElementType CIRCUIT_COMPONENT_EXPRESSION = new LeoElementType("CIRCUIT_COMPONENT_EXPRESSION");
  IElementType CIRCUIT_COMPONENT_INITIALIZER = new LeoElementType("CIRCUIT_COMPONENT_INITIALIZER");
  IElementType CIRCUIT_DECLARATION = new LeoElementType("CIRCUIT_DECLARATION");
  IElementType CIRCUIT_EXPRESSION = new LeoElementType("CIRCUIT_EXPRESSION");
  IElementType CONDITIONAL_CONJUNCTIVE_EXPRESSION = new LeoElementType("CONDITIONAL_CONJUNCTIVE_EXPRESSION");
  IElementType CONDITIONAL_DISJUNCTIVE_EXPRESSION = new LeoElementType("CONDITIONAL_DISJUNCTIVE_EXPRESSION");
  IElementType CONDITIONAL_STATEMENT = new LeoElementType("CONDITIONAL_STATEMENT");
  IElementType CONDITIONAL_TERNARY_EXPRESSION = new LeoElementType("CONDITIONAL_TERNARY_EXPRESSION");
  IElementType CONJUNCTIVE_EXPRESSION = new LeoElementType("CONJUNCTIVE_EXPRESSION");
  IElementType CONSOLE_STATEMENT = new LeoElementType("CONSOLE_STATEMENT");
  IElementType CONSTANT_DECLARATION = new LeoElementType("CONSTANT_DECLARATION");
  IElementType DECLARATION = new LeoElementType("DECLARATION");
  IElementType DISJUNCTIVE_EXPRESSION = new LeoElementType("DISJUNCTIVE_EXPRESSION");
  IElementType EQUALITY_EXPRESSION = new LeoElementType("EQUALITY_EXPRESSION");
  IElementType EXCLUSIVE_DISJUNCTIVE_EXPRESSION = new LeoElementType("EXCLUSIVE_DISJUNCTIVE_EXPRESSION");
  IElementType EXPONENTIAL_EXPRESSION = new LeoElementType("EXPONENTIAL_EXPRESSION");
  IElementType EXPRESSION = new LeoElementType("EXPRESSION");
  IElementType FIELD_TYPE = new LeoElementType("FIELD_TYPE");
  IElementType FREE_FUNCTION_CALL = new LeoElementType("FREE_FUNCTION_CALL");
  IElementType FUNCTION_ARGUMENTS = new LeoElementType("FUNCTION_ARGUMENTS");
  IElementType FUNCTION_DECLARATION = new LeoElementType("FUNCTION_DECLARATION");
  IElementType FUNCTION_PARAMETER = new LeoElementType("FUNCTION_PARAMETER");
  IElementType FUNCTION_PARAMETERS = new LeoElementType("FUNCTION_PARAMETERS");
  IElementType GROUP_COORDINATE = new LeoElementType("GROUP_COORDINATE");
  IElementType GROUP_TYPE = new LeoElementType("GROUP_TYPE");
  IElementType INEQUALITY_EXPRESSION = new LeoElementType("INEQUALITY_EXPRESSION");
  IElementType INTEGER_TYPE = new LeoElementType("INTEGER_TYPE");
  IElementType LITERAL = new LeoElementType("LITERAL");
  IElementType LOOP_STATEMENT = new LeoElementType("LOOP_STATEMENT");
  IElementType MULTIPLICATIVE_DIV_EXPRESSION = new LeoElementType("MULTIPLICATIVE_DIV_EXPRESSION");
  IElementType MULTIPLICATIVE_MOD_EXPRESSION = new LeoElementType("MULTIPLICATIVE_MOD_EXPRESSION");
  IElementType MULTIPLICATIVE_MUL_EXPRESSION = new LeoElementType("MULTIPLICATIVE_MUL_EXPRESSION");
  IElementType NAMED_TYPE = new LeoElementType("NAMED_TYPE");
  IElementType ORDERING_GREATER_EXPRESSION = new LeoElementType("ORDERING_GREATER_EXPRESSION");
  IElementType ORDERING_GREATER_OR_EQUAL_EXPRESSION = new LeoElementType("ORDERING_GREATER_OR_EQUAL_EXPRESSION");
  IElementType ORDERING_LESS_EXPRESSION = new LeoElementType("ORDERING_LESS_EXPRESSION");
  IElementType ORDERING_LESS_OR_EQUAL_EXPRESSION = new LeoElementType("ORDERING_LESS_OR_EQUAL_EXPRESSION");
  IElementType PRIMARY_EXPRESSION = new LeoElementType("PRIMARY_EXPRESSION");
  IElementType PRIMITIVE_TYPE = new LeoElementType("PRIMITIVE_TYPE");
  IElementType RECORD_DECLARATION = new LeoElementType("RECORD_DECLARATION");
  IElementType RETURN_STATEMENT = new LeoElementType("RETURN_STATEMENT");
  IElementType SCALAR_TYPE = new LeoElementType("SCALAR_TYPE");
  IElementType SHIFT_LEFT_EXPRESSION = new LeoElementType("SHIFT_LEFT_EXPRESSION");
  IElementType SHIFT_RIGHT_EXPRESSION = new LeoElementType("SHIFT_RIGHT_EXPRESSION");
  IElementType SIGNED_TYPE = new LeoElementType("SIGNED_TYPE");
  IElementType STATEMENT = new LeoElementType("STATEMENT");
  IElementType STATIC_FUNCTION_CALL = new LeoElementType("STATIC_FUNCTION_CALL");
  IElementType STRING_TYPE = new LeoElementType("STRING_TYPE");
  IElementType TUPLE_COMPONENT_EXPRESSION = new LeoElementType("TUPLE_COMPONENT_EXPRESSION");
  IElementType TUPLE_EXPRESSION = new LeoElementType("TUPLE_EXPRESSION");
  IElementType TUPLE_TYPE = new LeoElementType("TUPLE_TYPE");
  IElementType TYPE = new LeoElementType("TYPE");
  IElementType UNARY_MINUS_EXPRESSION = new LeoElementType("UNARY_MINUS_EXPRESSION");
  IElementType UNARY_NOT_EXPRESSION = new LeoElementType("UNARY_NOT_EXPRESSION");
  IElementType UNARY_OPERATOR_CALL = new LeoElementType("UNARY_OPERATOR_CALL");
  IElementType UNSIGNED_TYPE = new LeoElementType("UNSIGNED_TYPE");
  IElementType VARIABLE_DECLARATION = new LeoElementType("VARIABLE_DECLARATION");
  IElementType VARIABLE_OR_FREE_CONSTANT = new LeoElementType("VARIABLE_OR_FREE_CONSTANT");

  IElementType ADDRESS_LITERAL = new LeoTokenType("address-literal");
  IElementType ANNOTATION = new LeoTokenType("annotation");
  IElementType ASCII = new LeoTokenType("ascii");
  IElementType ASCII_CHARACTER_ESCAPE = new LeoTokenType("ascii-character-escape");
  IElementType BACKSLASH_ESCAPE = new LeoTokenType("backslash-escape");
  IElementType BLOCK_COMMENT = new LeoTokenType("block-comment");
  IElementType BOOLEAN_LITERAL = new LeoTokenType("boolean-literal");
  IElementType BRACES = new LeoTokenType("braces");
  IElementType BRACKETS = new LeoTokenType("brackets");
  IElementType CARRIAGE_RETURN = new LeoTokenType("carriage-return");
  IElementType CARRIAGE_RETURN_ESCAPE = new LeoTokenType("carriage-return-escape");
  IElementType CHARACTER = new LeoTokenType("character");
  IElementType COMMA = new LeoTokenType(",");
  IElementType DECIMAL_DIGIT = new LeoTokenType("decimal-digit");
  IElementType DOT = new LeoTokenType(".");
  IElementType DOUBLE_QUOTE = new LeoTokenType("\"");
  IElementType DOUBLE_QUOTE_ESCAPE = new LeoTokenType("double-quote-escape");
  IElementType END_OF_LINE_COMMENT = new LeoTokenType("end-of-line-comment");
  IElementType FIELD_LITERAL = new LeoTokenType("field-literal");
  IElementType HEXADECIMAL_DIGIT = new LeoTokenType("hexadecimal-digit");
  IElementType HORIZONTAL_TAB = new LeoTokenType("\\t");
  IElementType HORIZONTAL_TAB_ESCAPE = new LeoTokenType("horizontal-tab-escape");
  IElementType IDENTIFIER = new LeoTokenType("identifier");
  IElementType INTEGER_LITERAL = new LeoTokenType("integer-literal");
  IElementType KEYWORD = new LeoTokenType("keyword");
  IElementType LETTER = new LeoTokenType("letter");
  IElementType LINE_FEED_ESCAPE = new LeoTokenType("line-feed-escape");
  IElementType LOWERCASE_LETTER = new LeoTokenType("lowercase-letter");
  IElementType NOT_DOUBLE_QUOTE_OR_BACKSLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN = new LeoTokenType("not-double-quote-or-backslash-or-line-feed-or-carriage-return");
  IElementType NOT_LINE_FEED_OR_CARRIAGE_RETURN = new LeoTokenType("not-line-feed-or-carriage-return");
  IElementType NOT_STAR_OR_LINE_FEED_OR_CARRIAGE_RETURN = new LeoTokenType("not-star-or-line-feed-or-carriage-return");
  IElementType NOT_STAR_OR_SLASH_OR_LINE_FEED_OR_CARRIAGE_RETURN = new LeoTokenType("not-star-or-slash-or-line-feed-or-carriage-return");
  IElementType NULL_CHARACTER_ESCAPE = new LeoTokenType("null-character-escape");
  IElementType NUMERAL = new LeoTokenType("numeral");
  IElementType NUMERIC_LITERAL = new LeoTokenType("numeric-literal");
  IElementType OCTAL_DIGIT = new LeoTokenType("octal-digit");
  IElementType PARENS = new LeoTokenType("parens");
  IElementType PRODUCT_GROUP_LITERAL = new LeoTokenType("product-group-literal");
  IElementType SAFE_NONASCII = new LeoTokenType("safe-nonascii");
  IElementType SCALAR_LITERAL = new LeoTokenType("scalar-literal");
  IElementType SEMICOLON = new LeoTokenType(";");
  IElementType SIGNED_LITERAL = new LeoTokenType("signed-literal");
  IElementType SIMPLE_CHARACTER_ESCAPE = new LeoTokenType("simple-character-escape");
  IElementType SINGLE_QUOTE = new LeoTokenType("'");
  IElementType SINGLE_QUOTE_ESCAPE = new LeoTokenType("single-quote-escape");
  IElementType SPACE = new LeoTokenType(" ");
  IElementType STRING_LITERAL = new LeoTokenType("string-literal");
  IElementType SYMBOL = new LeoTokenType("symbol");
  IElementType UNICODE_CHARACTER_ESCAPE = new LeoTokenType("unicode-character-escape");
  IElementType UNSIGNED_LITERAL = new LeoTokenType("unsigned-literal");
  IElementType UPPERCASE_LETTER = new LeoTokenType("uppercase-letter");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
      if (type == ADDITIVE_ADD_EXPRESSION) {
        return new LeoAdditiveAddExpressionImpl(node);
      }
      else if (type == ADDITIVE_SUB_EXPRESSION) {
        return new LeoAdditiveSubExpressionImpl(node);
      }
      else if (type == ADDRESS_TYPE) {
        return new LeoAddressTypeImpl(node);
      }
      else if (type == AFFINE_GROUP_LITERAL) {
        return new LeoAffineGroupLiteralImpl(node);
      }
      else if (type == ARITHMETIC_TYPE) {
        return new LeoArithmeticTypeImpl(node);
      }
      else if (type == ASSERT_CALL) {
        return new LeoAssertCallImpl(node);
      }
      else if (type == ASSERT_EQUAL_CALL) {
        return new LeoAssertEqualCallImpl(node);
      }
      else if (type == ASSERT_NOT_EQUAL_CALL) {
        return new LeoAssertNotEqualCallImpl(node);
      }
      else if (type == ASSIGNMENT_OPERATOR) {
        return new LeoAssignmentOperatorImpl(node);
      }
      else if (type == ASSIGNMENT_STATEMENT) {
        return new LeoAssignmentStatementImpl(node);
      }
      else if (type == ASSOCIATED_CONSTANT) {
        return new LeoAssociatedConstantImpl(node);
      }
      else if (type == ATOMIC_LITERAL) {
        return new LeoAtomicLiteralImpl(node);
      }
      else if (type == BINARY_OPERATOR_CALL) {
        return new LeoBinaryOperatorCallImpl(node);
      }
      else if (type == BLOCK) {
        return new LeoBlockImpl(node);
      }
      else if (type == BOOLEAN_TYPE) {
        return new LeoBooleanTypeImpl(node);
      }
      else if (type == BRANCH) {
        return new LeoBranchImpl(node);
      }
      else if (type == CIRCUIT_COMPONENT_DECLARATION) {
        return new LeoCircuitComponentDeclarationImpl(node);
      }
      else if (type == CIRCUIT_COMPONENT_DECLARATIONS) {
        return new LeoCircuitComponentDeclarationsImpl(node);
      }
      else if (type == CIRCUIT_COMPONENT_EXPRESSION) {
        return new LeoCircuitComponentExpressionImpl(node);
      }
      else if (type == CIRCUIT_COMPONENT_INITIALIZER) {
        return new LeoCircuitComponentInitializerImpl(node);
      }
      else if (type == CIRCUIT_DECLARATION) {
        return new LeoCircuitDeclarationImpl(node);
      }
      else if (type == CIRCUIT_EXPRESSION) {
        return new LeoCircuitExpressionImpl(node);
      }
      else if (type == CONDITIONAL_CONJUNCTIVE_EXPRESSION) {
        return new LeoConditionalConjunctiveExpressionImpl(node);
      }
      else if (type == CONDITIONAL_DISJUNCTIVE_EXPRESSION) {
        return new LeoConditionalDisjunctiveExpressionImpl(node);
      }
      else if (type == CONDITIONAL_STATEMENT) {
        return new LeoConditionalStatementImpl(node);
      }
      else if (type == CONDITIONAL_TERNARY_EXPRESSION) {
        return new LeoConditionalTernaryExpressionImpl(node);
      }
      else if (type == CONJUNCTIVE_EXPRESSION) {
        return new LeoConjunctiveExpressionImpl(node);
      }
      else if (type == CONSOLE_STATEMENT) {
        return new LeoConsoleStatementImpl(node);
      }
      else if (type == CONSTANT_DECLARATION) {
        return new LeoConstantDeclarationImpl(node);
      }
      else if (type == DECLARATION) {
        return new LeoDeclarationImpl(node);
      }
      else if (type == DISJUNCTIVE_EXPRESSION) {
        return new LeoDisjunctiveExpressionImpl(node);
      }
      else if (type == EQUALITY_EXPRESSION) {
        return new LeoEqualityExpressionImpl(node);
      }
      else if (type == EXCLUSIVE_DISJUNCTIVE_EXPRESSION) {
        return new LeoExclusiveDisjunctiveExpressionImpl(node);
      }
      else if (type == EXPONENTIAL_EXPRESSION) {
        return new LeoExponentialExpressionImpl(node);
      }
      else if (type == FIELD_TYPE) {
        return new LeoFieldTypeImpl(node);
      }
      else if (type == FREE_FUNCTION_CALL) {
        return new LeoFreeFunctionCallImpl(node);
      }
      else if (type == FUNCTION_ARGUMENTS) {
        return new LeoFunctionArgumentsImpl(node);
      }
      else if (type == FUNCTION_DECLARATION) {
        return new LeoFunctionDeclarationImpl(node);
      }
      else if (type == FUNCTION_PARAMETER) {
        return new LeoFunctionParameterImpl(node);
      }
      else if (type == FUNCTION_PARAMETERS) {
        return new LeoFunctionParametersImpl(node);
      }
      else if (type == GROUP_COORDINATE) {
        return new LeoGroupCoordinateImpl(node);
      }
      else if (type == GROUP_TYPE) {
        return new LeoGroupTypeImpl(node);
      }
      else if (type == INEQUALITY_EXPRESSION) {
        return new LeoInequalityExpressionImpl(node);
      }
      else if (type == INTEGER_TYPE) {
        return new LeoIntegerTypeImpl(node);
      }
      else if (type == LITERAL) {
        return new LeoLiteralImpl(node);
      }
      else if (type == LOOP_STATEMENT) {
        return new LeoLoopStatementImpl(node);
      }
      else if (type == MULTIPLICATIVE_DIV_EXPRESSION) {
        return new LeoMultiplicativeDivExpressionImpl(node);
      }
      else if (type == MULTIPLICATIVE_MOD_EXPRESSION) {
        return new LeoMultiplicativeModExpressionImpl(node);
      }
      else if (type == MULTIPLICATIVE_MUL_EXPRESSION) {
        return new LeoMultiplicativeMulExpressionImpl(node);
      }
      else if (type == NAMED_TYPE) {
        return new LeoNamedTypeImpl(node);
      }
      else if (type == ORDERING_GREATER_EXPRESSION) {
        return new LeoOrderingGreaterExpressionImpl(node);
      }
      else if (type == ORDERING_GREATER_OR_EQUAL_EXPRESSION) {
        return new LeoOrderingGreaterOrEqualExpressionImpl(node);
      }
      else if (type == ORDERING_LESS_EXPRESSION) {
        return new LeoOrderingLessExpressionImpl(node);
      }
      else if (type == ORDERING_LESS_OR_EQUAL_EXPRESSION) {
        return new LeoOrderingLessOrEqualExpressionImpl(node);
      }
      else if (type == PRIMARY_EXPRESSION) {
        return new LeoPrimaryExpressionImpl(node);
      }
      else if (type == PRIMITIVE_TYPE) {
        return new LeoPrimitiveTypeImpl(node);
      }
      else if (type == RECORD_DECLARATION) {
        return new LeoRecordDeclarationImpl(node);
      }
      else if (type == RETURN_STATEMENT) {
        return new LeoReturnStatementImpl(node);
      }
      else if (type == SCALAR_TYPE) {
        return new LeoScalarTypeImpl(node);
      }
      else if (type == SHIFT_LEFT_EXPRESSION) {
        return new LeoShiftLeftExpressionImpl(node);
      }
      else if (type == SHIFT_RIGHT_EXPRESSION) {
        return new LeoShiftRightExpressionImpl(node);
      }
      else if (type == SIGNED_TYPE) {
        return new LeoSignedTypeImpl(node);
      }
      else if (type == STATEMENT) {
        return new LeoStatementImpl(node);
      }
      else if (type == STATIC_FUNCTION_CALL) {
        return new LeoStaticFunctionCallImpl(node);
      }
      else if (type == STRING_TYPE) {
        return new LeoStringTypeImpl(node);
      }
      else if (type == TUPLE_COMPONENT_EXPRESSION) {
        return new LeoTupleComponentExpressionImpl(node);
      }
      else if (type == TUPLE_EXPRESSION) {
        return new LeoTupleExpressionImpl(node);
      }
      else if (type == TUPLE_TYPE) {
        return new LeoTupleTypeImpl(node);
      }
      else if (type == TYPE) {
        return new LeoTypeImpl(node);
      }
      else if (type == UNARY_MINUS_EXPRESSION) {
        return new LeoUnaryMinusExpressionImpl(node);
      }
      else if (type == UNARY_NOT_EXPRESSION) {
        return new LeoUnaryNotExpressionImpl(node);
      }
      else if (type == UNARY_OPERATOR_CALL) {
        return new LeoUnaryOperatorCallImpl(node);
      }
      else if (type == UNSIGNED_TYPE) {
        return new LeoUnsignedTypeImpl(node);
      }
      else if (type == VARIABLE_DECLARATION) {
        return new LeoVariableDeclarationImpl(node);
      }
      else if (type == VARIABLE_OR_FREE_CONSTANT) {
        return new LeoVariableOrFreeConstantImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
