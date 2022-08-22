// This is a generated file. Not intended for manual editing.
package im.mrx.leolanguage.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static im.mrx.leolanguage.psi.LeoTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class LeoParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(ADDITIVE_ADD_EXPRESSION, ADDITIVE_SUB_EXPRESSION, BINARY_OPERATOR_CALL, CIRCUIT_COMPONENT_EXPRESSION,
      CIRCUIT_EXPRESSION, CONDITIONAL_CONJUNCTIVE_EXPRESSION, CONDITIONAL_DISJUNCTIVE_EXPRESSION, CONDITIONAL_TERNARY_EXPRESSION,
      CONJUNCTIVE_EXPRESSION, DISJUNCTIVE_EXPRESSION, EQUALITY_EXPRESSION, EXCLUSIVE_DISJUNCTIVE_EXPRESSION,
      EXPONENTIAL_EXPRESSION, EXPRESSION, INEQUALITY_EXPRESSION, MULTIPLICATIVE_DIV_EXPRESSION,
      MULTIPLICATIVE_MOD_EXPRESSION, MULTIPLICATIVE_MUL_EXPRESSION, ORDERING_GREATER_EXPRESSION, ORDERING_GREATER_OR_EQUAL_EXPRESSION,
      ORDERING_LESS_EXPRESSION, ORDERING_LESS_OR_EQUAL_EXPRESSION, PRIMARY_EXPRESSION, SHIFT_LEFT_EXPRESSION,
      SHIFT_RIGHT_EXPRESSION, TUPLE_COMPONENT_EXPRESSION, TUPLE_EXPRESSION, UNARY_MINUS_EXPRESSION,
      UNARY_NOT_EXPRESSION, UNARY_OPERATOR_CALL),
  };

  /* ********************************************************** */
  // "address"
  public static boolean address_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "address_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADDRESS_TYPE, "<address type>");
    r = consumeToken(b, "address");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "(" group_coordinate "," group_coordinate ")group"
  public static boolean affine_group_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "affine_group_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, AFFINE_GROUP_LITERAL, "<affine group literal>");
    r = consumeToken(b, "(");
    r = r && group_coordinate(b, l + 1);
    r = r && consumeToken(b, COMMA);
    r = r && group_coordinate(b, l + 1);
    r = r && consumeToken(b, ")group");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // integer_type | field_type | group_type | scalar_type
  public static boolean arithmetic_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "arithmetic_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ARITHMETIC_TYPE, "<arithmetic type>");
    r = integer_type(b, l + 1);
    if (!r) r = field_type(b, l + 1);
    if (!r) r = group_type(b, l + 1);
    if (!r) r = scalar_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "assert" "(" expression ")"
  public static boolean assert_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSERT_CALL, "<assert call>");
    r = consumeToken(b, "assert");
    r = r && consumeToken(b, "(");
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "assert_eq" "(" expression "," expression [ "," ] ")"
  public static boolean assert_equal_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_equal_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSERT_EQUAL_CALL, "<assert equal call>");
    r = consumeToken(b, "assert_eq");
    r = r && consumeToken(b, "(");
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, COMMA);
    r = r && expression(b, l + 1, -1);
    r = r && assert_equal_call_5(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "," ]
  private static boolean assert_equal_call_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_equal_call_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // "assert_neq" "(" expression "," expression [ "," ] ")"
  public static boolean assert_not_equal_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_not_equal_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSERT_NOT_EQUAL_CALL, "<assert not equal call>");
    r = consumeToken(b, "assert_neq");
    r = r && consumeToken(b, "(");
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, COMMA);
    r = r && expression(b, l + 1, -1);
    r = r && assert_not_equal_call_5(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "," ]
  private static boolean assert_not_equal_call_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assert_not_equal_call_5")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // "="
  //                     | "+="
  //                     | "-="
  //                     | "*="
  //                     | "/="
  //                     | "%="
  //                     | "**="
  //                     | "<<="
  //                     | ">>="
  //                     | "&="
  //                     | "|="
  //                     | "^="
  //                     | "&&="
  //                     | "||="
  public static boolean assignment_operator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_operator")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_OPERATOR, "<assignment operator>");
    r = consumeToken(b, "=");
    if (!r) r = consumeToken(b, "+=");
    if (!r) r = consumeToken(b, "-=");
    if (!r) r = consumeToken(b, "*=");
    if (!r) r = consumeToken(b, "/=");
    if (!r) r = consumeToken(b, "%=");
    if (!r) r = consumeToken(b, "**=");
    if (!r) r = consumeToken(b, "<<=");
    if (!r) r = consumeToken(b, ">>=");
    if (!r) r = consumeToken(b, "&=");
    if (!r) r = consumeToken(b, "|=");
    if (!r) r = consumeToken(b, "^=");
    if (!r) r = consumeToken(b, "&&=");
    if (!r) r = consumeToken(b, "||=");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expression assignment_operator expression ";"
  public static boolean assignment_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assignment_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASSIGNMENT_STATEMENT, "<assignment statement>");
    r = expression(b, l + 1, -1);
    r = r && assignment_operator(b, l + 1);
    p = r; // pin = 2
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // named_type "::" identifier
  public static boolean associated_constant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "associated_constant")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ASSOCIATED_CONSTANT, "<associated constant>");
    r = named_type(b, l + 1);
    r = r && consumeToken(b, "::");
    r = r && consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // numeric-literal
  //                | boolean-literal
  //                | address-literal
  //                | string-literal
  public static boolean atomic_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "atomic_literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ATOMIC_LITERAL, "<atomic literal>");
    r = consumeToken(b, NUMERIC_LITERAL);
    if (!r) r = consumeToken(b, BOOLEAN_LITERAL);
    if (!r) r = consumeToken(b, ADDRESS_LITERAL);
    if (!r) r = consumeToken(b, STRING_LITERAL);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "{" statement* "}"
  public static boolean block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BLOCK, "<block>");
    r = consumeToken(b, "{");
    p = r; // pin = 1
    r = r && report_error_(b, block_1(b, l + 1));
    r = p && consumeToken(b, "}") && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // statement*
  private static boolean block_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "block_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!statement(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "block_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // "bool"
  public static boolean boolean_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "boolean_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, BOOLEAN_TYPE, "<boolean type>");
    r = consumeToken(b, "bool");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "if" expression block
  public static boolean branch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "branch")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, BRANCH, "<branch>");
    r = consumeToken(b, "if");
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier ":" type
  public static boolean circuit_component_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_declaration")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && consumeToken(b, ":");
    r = r && type(b, l + 1);
    exit_section_(b, m, CIRCUIT_COMPONENT_DECLARATION, r);
    return r;
  }

  /* ********************************************************** */
  // circuit_component_declaration
  //                                  *( "," circuit_component_declaration )
  //                                  [ "," ]
  public static boolean circuit_component_declarations(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_declarations")) return false;
    if (!nextTokenIs(b, "<circuit component declarations>", COMMA, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CIRCUIT_COMPONENT_DECLARATIONS, "<circuit component declarations>");
    r = circuit_component_declarations_0(b, l + 1);
    r = r && circuit_component_declarations_1(b, l + 1);
    r = r && circuit_component_declarations_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // circuit_component_declaration
  //                                  *
  private static boolean circuit_component_declarations_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_declarations_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!circuit_component_declaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "circuit_component_declarations_0", c)) break;
    }
    return true;
  }

  // "," circuit_component_declaration
  private static boolean circuit_component_declarations_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_declarations_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && circuit_component_declaration(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean circuit_component_declarations_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_declarations_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // identifier
  //                               | identifier ":" expression
  public static boolean circuit_component_initializer(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_initializer")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    if (!r) r = circuit_component_initializer_1(b, l + 1);
    exit_section_(b, m, CIRCUIT_COMPONENT_INITIALIZER, r);
    return r;
  }

  // identifier ":" expression
  private static boolean circuit_component_initializer_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_component_initializer_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && consumeToken(b, ":");
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // "circuit" identifier
  //                       "{" circuit_component_declarations "}"
  public static boolean circuit_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CIRCUIT_DECLARATION, "<circuit declaration>");
    r = consumeToken(b, "circuit");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, "{")) && r;
    r = p && report_error_(b, circuit_component_declarations(b, l + 1)) && r;
    r = p && consumeToken(b, "}") && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier "{" circuit_component_initializer
  //                                     ( "," circuit_component_initializer )*
  //                                     [ "," ] "}"
  public static boolean circuit_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_expression")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && consumeToken(b, "{");
    r = r && circuit_component_initializer(b, l + 1);
    r = r && circuit_expression_3(b, l + 1);
    r = r && circuit_expression_4(b, l + 1);
    r = r && consumeToken(b, "}");
    exit_section_(b, m, CIRCUIT_EXPRESSION, r);
    return r;
  }

  // ( "," circuit_component_initializer )*
  private static boolean circuit_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_expression_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!circuit_expression_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "circuit_expression_3", c)) break;
    }
    return true;
  }

  // "," circuit_component_initializer
  private static boolean circuit_expression_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_expression_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && circuit_component_initializer(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean circuit_expression_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "circuit_expression_4")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // end-of-line-comment | block-comment
  static boolean comment(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "comment")) return false;
    if (!nextTokenIs(b, "", BLOCK_COMMENT, END_OF_LINE_COMMENT)) return false;
    boolean r;
    r = consumeToken(b, END_OF_LINE_COMMENT);
    if (!r) r = consumeToken(b, BLOCK_COMMENT);
    return r;
  }

  /* ********************************************************** */
  // branch
  //                       | branch "else" block
  //                       | branch "else" conditional_statement
  public static boolean conditional_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONDITIONAL_STATEMENT, "<conditional statement>");
    r = branch(b, l + 1);
    if (!r) r = conditional_statement_1(b, l + 1);
    if (!r) r = conditional_statement_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // branch "else" block
  private static boolean conditional_statement_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_statement_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = branch(b, l + 1);
    r = r && consumeToken(b, "else");
    r = r && block(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // branch "else" conditional_statement
  private static boolean conditional_statement_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_statement_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = branch(b, l + 1);
    r = r && consumeToken(b, "else");
    r = r && conditional_statement(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // assert_call
  //              | assert_equal_call
  //              | assert_not_equal_call
  static boolean console_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "console_call")) return false;
    boolean r;
    r = assert_call(b, l + 1);
    if (!r) r = assert_equal_call(b, l + 1);
    if (!r) r = assert_not_equal_call(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // "console" "." console_call ";"
  public static boolean console_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "console_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSOLE_STATEMENT, "<console statement>");
    r = consumeToken(b, "console");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, DOT));
    r = p && report_error_(b, console_call(b, l + 1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // "const" identifier ":" type "=" expression ";"
  public static boolean constant_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "constant_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONSTANT_DECLARATION, "<constant declaration>");
    r = consumeToken(b, "const");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, ":")) && r;
    r = p && report_error_(b, type(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "=")) && r;
    r = p && report_error_(b, expression(b, l + 1, -1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // function_declaration
  //             | circuit_declaration
  //             | record_declaration
  //             | comment
  public static boolean declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "declaration")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, DECLARATION, "<declaration>");
    r = function_declaration(b, l + 1);
    if (!r) r = circuit_declaration(b, l + 1);
    if (!r) r = record_declaration(b, l + 1);
    if (!r) r = comment(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "field"
  public static boolean field_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FIELD_TYPE, "<field type>");
    r = consumeToken(b, "field");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // declaration*
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    while (true) {
      int c = current_position_(b);
      if (!declaration(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "file", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // identifier function_arguments
  public static boolean free_function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "free_function_call")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    r = r && function_arguments(b, l + 1);
    exit_section_(b, m, FREE_FUNCTION_CALL, r);
    return r;
  }

  /* ********************************************************** */
  // "(" [ expression ( "," expression )* [ "," ] ] ")"
  public static boolean function_arguments(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_ARGUMENTS, "<function arguments>");
    r = consumeToken(b, "(");
    r = r && function_arguments_1(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ expression ( "," expression )* [ "," ] ]
  private static boolean function_arguments_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments_1")) return false;
    function_arguments_1_0(b, l + 1);
    return true;
  }

  // expression ( "," expression )* [ "," ]
  private static boolean function_arguments_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && function_arguments_1_0_1(b, l + 1);
    r = r && function_arguments_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( "," expression )*
  private static boolean function_arguments_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_arguments_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_arguments_1_0_1", c)) break;
    }
    return true;
  }

  // "," expression
  private static boolean function_arguments_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean function_arguments_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_arguments_1_0_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // annotation* "function" identifier
  //                        "(" [ function_parameters ] ")" "->" type
  //                        block
  public static boolean function_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_DECLARATION, "<function declaration>");
    r = function_declaration_0(b, l + 1);
    r = r && consumeToken(b, "function");
    p = r; // pin = 2
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, "(")) && r;
    r = p && report_error_(b, function_declaration_4(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, ")")) && r;
    r = p && report_error_(b, consumeToken(b, "->")) && r;
    r = p && report_error_(b, type(b, l + 1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // annotation*
  private static boolean function_declaration_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration_0")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, ANNOTATION)) break;
      if (!empty_element_parsed_guard_(b, "function_declaration_0", c)) break;
    }
    return true;
  }

  // [ function_parameters ]
  private static boolean function_declaration_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_declaration_4")) return false;
    function_parameters(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [ "public" | "constant" | "const" ]
  //                      identifier ":" type
  public static boolean function_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_PARAMETER, "<function parameter>");
    r = function_parameter_0(b, l + 1);
    r = r && consumeToken(b, IDENTIFIER);
    r = r && consumeToken(b, ":");
    r = r && type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "public" | "constant" | "const" ]
  private static boolean function_parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter_0")) return false;
    function_parameter_0_0(b, l + 1);
    return true;
  }

  // "public" | "constant" | "const"
  private static boolean function_parameter_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameter_0_0")) return false;
    boolean r;
    r = consumeToken(b, "public");
    if (!r) r = consumeToken(b, "constant");
    if (!r) r = consumeToken(b, "const");
    return r;
  }

  /* ********************************************************** */
  // function_parameter ( "," function_parameter )* [ "," ]
  public static boolean function_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameters")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FUNCTION_PARAMETERS, "<function parameters>");
    r = function_parameter(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, function_parameters_1(b, l + 1));
    r = p && function_parameters_2(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( "," function_parameter )*
  private static boolean function_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameters_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!function_parameters_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "function_parameters_1", c)) break;
    }
    return true;
  }

  // "," function_parameter
  private static boolean function_parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameters_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && function_parameter(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ "," ]
  private static boolean function_parameters_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "function_parameters_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // ( [ "-" ] numeral ) | "+" | "-" | "_"
  public static boolean group_coordinate(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_coordinate")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUP_COORDINATE, "<group coordinate>");
    r = group_coordinate_0(b, l + 1);
    if (!r) r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    if (!r) r = consumeToken(b, "_");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ "-" ] numeral
  private static boolean group_coordinate_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_coordinate_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = group_coordinate_0_0(b, l + 1);
    r = r && consumeToken(b, NUMERAL);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "-" ]
  private static boolean group_coordinate_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_coordinate_0_0")) return false;
    consumeToken(b, "-");
    return true;
  }

  /* ********************************************************** */
  // product-group-literal | affine_group_literal
  static boolean group_literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_literal")) return false;
    boolean r;
    r = consumeToken(b, PRODUCT_GROUP_LITERAL);
    if (!r) r = affine_group_literal(b, l + 1);
    return r;
  }

  /* ********************************************************** */
  // "group"
  public static boolean group_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "group_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, GROUP_TYPE, "<group type>");
    r = consumeToken(b, "group");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // unsigned_type | signed_type
  public static boolean integer_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "integer_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INTEGER_TYPE, "<integer type>");
    r = unsigned_type(b, l + 1);
    if (!r) r = signed_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // atomic_literal | affine_group_literal
  public static boolean literal(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, LITERAL, "<literal>");
    r = atomic_literal(b, l + 1);
    if (!r) r = affine_group_literal(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "for" identifier ":" type
  //                  "in" expression ".." [ "=" ] expression
  //                  block
  public static boolean loop_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOOP_STATEMENT, "<loop statement>");
    r = consumeToken(b, "for");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, ":")) && r;
    r = p && report_error_(b, type(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "in")) && r;
    r = p && report_error_(b, expression(b, l + 1, -1)) && r;
    r = p && report_error_(b, consumeToken(b, "..")) && r;
    r = p && report_error_(b, loop_statement_7(b, l + 1)) && r;
    r = p && report_error_(b, expression(b, l + 1, -1)) && r;
    r = p && block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ "=" ]
  private static boolean loop_statement_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_statement_7")) return false;
    consumeToken(b, "=");
    return true;
  }

  /* ********************************************************** */
  // primitive_type | identifier
  public static boolean named_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "named_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, NAMED_TYPE, "<named type>");
    r = primitive_type(b, l + 1);
    if (!r) r = consumeToken(b, IDENTIFIER);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // boolean_type | arithmetic_type | address_type | string_type
  public static boolean primitive_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primitive_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, PRIMITIVE_TYPE, "<primitive type>");
    r = boolean_type(b, l + 1);
    if (!r) r = arithmetic_type(b, l + 1);
    if (!r) r = address_type(b, l + 1);
    if (!r) r = string_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "record" identifier
  //                      "{" circuit_component_declarations "}"
  public static boolean record_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RECORD_DECLARATION, "<record declaration>");
    r = consumeToken(b, "record");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, "{")) && r;
    r = p && report_error_(b, circuit_component_declarations(b, l + 1)) && r;
    r = p && consumeToken(b, "}") && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // "return" expression ";"
  public static boolean return_statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "return_statement")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, RETURN_STATEMENT, "<return statement>");
    r = consumeToken(b, "return");
    p = r; // pin = 1
    r = r && report_error_(b, expression(b, l + 1, -1));
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // "scalar"
  public static boolean scalar_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "scalar_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SCALAR_TYPE, "<scalar type>");
    r = consumeToken(b, "scalar");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "i8" | "i16" | "i32" | "i64" | "i128"
  public static boolean signed_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "signed_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIGNED_TYPE, "<signed type>");
    r = consumeToken(b, "i8");
    if (!r) r = consumeToken(b, "i16");
    if (!r) r = consumeToken(b, "i32");
    if (!r) r = consumeToken(b, "i64");
    if (!r) r = consumeToken(b, "i128");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // return_statement
  //           | variable_declaration
  //           | constant_declaration
  //           | conditional_statement
  //           | loop_statement
  //           | assignment_statement
  //           | console_statement
  //           | block
  public static boolean statement(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "statement")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATEMENT, "<statement>");
    r = return_statement(b, l + 1);
    if (!r) r = variable_declaration(b, l + 1);
    if (!r) r = constant_declaration(b, l + 1);
    if (!r) r = conditional_statement(b, l + 1);
    if (!r) r = loop_statement(b, l + 1);
    if (!r) r = assignment_statement(b, l + 1);
    if (!r) r = console_statement(b, l + 1);
    if (!r) r = block(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // named_type "::" identifier function_arguments
  public static boolean static_function_call(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "static_function_call")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STATIC_FUNCTION_CALL, "<static function call>");
    r = named_type(b, l + 1);
    r = r && consumeToken(b, "::");
    r = r && consumeToken(b, IDENTIFIER);
    r = r && function_arguments(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "string"
  public static boolean string_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "string_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STRING_TYPE, "<string type>");
    r = consumeToken(b, "string");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "(" [ expression { "," expression }+ [ "," ] ] ")"
  public static boolean tuple_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TUPLE_EXPRESSION, "<tuple expression>");
    r = consumeToken(b, "(");
    r = r && tuple_expression_1(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ expression { "," expression }+ [ "," ] ]
  private static boolean tuple_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression_1")) return false;
    tuple_expression_1_0(b, l + 1);
    return true;
  }

  // expression { "," expression }+ [ "," ]
  private static boolean tuple_expression_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = expression(b, l + 1, -1);
    r = r && tuple_expression_1_0_1(b, l + 1);
    r = r && tuple_expression_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { "," expression }+
  private static boolean tuple_expression_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tuple_expression_1_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!tuple_expression_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tuple_expression_1_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // "," expression
  private static boolean tuple_expression_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean tuple_expression_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_expression_1_0_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // "(" [ type { "," type }+ [ "," ] ] ")"
  public static boolean tuple_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TUPLE_TYPE, "<tuple type>");
    r = consumeToken(b, "(");
    r = r && tuple_type_1(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ type { "," type }+ [ "," ] ]
  private static boolean tuple_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type_1")) return false;
    tuple_type_1_0(b, l + 1);
    return true;
  }

  // type { "," type }+ [ "," ]
  private static boolean tuple_type_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type(b, l + 1);
    r = r && tuple_type_1_0_1(b, l + 1);
    r = r && tuple_type_1_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // { "," type }+
  private static boolean tuple_type_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = tuple_type_1_0_1_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!tuple_type_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "tuple_type_1_0_1", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // "," type
  private static boolean tuple_type_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean tuple_type_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "tuple_type_1_0_2")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  /* ********************************************************** */
  // named_type | tuple_type
  public static boolean type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE, "<type>");
    r = named_type(b, l + 1);
    if (!r) r = tuple_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "u8" | "u16" | "u32" | "u64" | "u128"
  public static boolean unsigned_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unsigned_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, UNSIGNED_TYPE, "<unsigned type>");
    r = consumeToken(b, "u8");
    if (!r) r = consumeToken(b, "u16");
    if (!r) r = consumeToken(b, "u32");
    if (!r) r = consumeToken(b, "u64");
    if (!r) r = consumeToken(b, "u128");
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // "let" identifier ":" type "=" expression ";"
  public static boolean variable_declaration(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_declaration")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VARIABLE_DECLARATION, "<variable declaration>");
    r = consumeToken(b, "let");
    p = r; // pin = 1
    r = r && report_error_(b, consumeToken(b, IDENTIFIER));
    r = p && report_error_(b, consumeToken(b, ":")) && r;
    r = p && report_error_(b, type(b, l + 1)) && r;
    r = p && report_error_(b, consumeToken(b, "=")) && r;
    r = p && report_error_(b, expression(b, l + 1, -1)) && r;
    r = p && consumeToken(b, SEMICOLON) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // identifier
  public static boolean variable_or_free_constant(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "variable_or_free_constant")) return false;
    if (!nextTokenIs(b, IDENTIFIER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENTIFIER);
    exit_section_(b, m, VARIABLE_OR_FREE_CONSTANT, r);
    return r;
  }

  /* ********************************************************** */
  // Expression root: expression
  // Operator priority table:
  // 0: BINARY(conditional_ternary_expression)
  // 1: BINARY(conditional_disjunctive_expression)
  // 2: BINARY(conditional_conjunctive_expression)
  // 3: BINARY(equality_expression) BINARY(inequality_expression)
  // 4: BINARY(ordering_less_expression) BINARY(ordering_greater_expression) BINARY(ordering_less_or_equal_expression) BINARY(ordering_greater_or_equal_expression)
  // 5: BINARY(exclusive_disjunctive_expression)
  // 6: BINARY(disjunctive_expression)
  // 7: BINARY(conjunctive_expression)
  // 8: BINARY(shift_left_expression) BINARY(shift_right_expression)
  // 9: BINARY(additive_add_expression) BINARY(additive_sub_expression)
  // 10: BINARY(multiplicative_mul_expression) BINARY(multiplicative_div_expression) BINARY(multiplicative_mod_expression)
  // 11: BINARY(exponential_expression)
  // 12: PREFIX(unary_not_expression) PREFIX(unary_minus_expression)
  // 13: POSTFIX(tuple_component_expression) POSTFIX(circuit_component_expression) POSTFIX(unary_operator_call) BINARY(binary_operator_call)
  // 14: ATOM(primary_expression)
  public static boolean expression(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression")) return false;
    addVariant(b, "<expression>");
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<expression>");
    r = unary_minus_expression(b, l + 1);
    if (!r) r = unary_not_expression(b, l + 1);
    if (!r) r = primary_expression(b, l + 1);
    p = r;
    r = r && expression_0(b, l + 1, g);
    exit_section_(b, l, m, null, r, p, null);
    return r || p;
  }

  public static boolean expression_0(PsiBuilder b, int l, int g) {
    if (!recursion_guard_(b, l, "expression_0")) return false;
    boolean r = true;
    while (true) {
      Marker m = enter_section_(b, l, _LEFT_, null);
      if (g < 0 && consumeTokenSmart(b, "?")) {
        r = report_error_(b, expression(b, l, -1));
        r = conditional_ternary_expression_1(b, l + 1) && r;
        exit_section_(b, l, m, CONDITIONAL_TERNARY_EXPRESSION, r, true, null);
      }
      else if (g < 1 && consumeTokenSmart(b, "||")) {
        r = expression(b, l, 1);
        exit_section_(b, l, m, CONDITIONAL_DISJUNCTIVE_EXPRESSION, r, true, null);
      }
      else if (g < 2 && consumeTokenSmart(b, "&&")) {
        r = expression(b, l, 2);
        exit_section_(b, l, m, CONDITIONAL_CONJUNCTIVE_EXPRESSION, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "==")) {
        r = expression(b, l, 3);
        exit_section_(b, l, m, EQUALITY_EXPRESSION, r, true, null);
      }
      else if (g < 3 && consumeTokenSmart(b, "!=")) {
        r = expression(b, l, 3);
        exit_section_(b, l, m, INEQUALITY_EXPRESSION, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, "<")) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, ORDERING_LESS_EXPRESSION, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, ">")) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, ORDERING_GREATER_EXPRESSION, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, "<=")) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, ORDERING_LESS_OR_EQUAL_EXPRESSION, r, true, null);
      }
      else if (g < 4 && consumeTokenSmart(b, ">=")) {
        r = expression(b, l, 4);
        exit_section_(b, l, m, ORDERING_GREATER_OR_EQUAL_EXPRESSION, r, true, null);
      }
      else if (g < 5 && consumeTokenSmart(b, "^")) {
        r = expression(b, l, 5);
        exit_section_(b, l, m, EXCLUSIVE_DISJUNCTIVE_EXPRESSION, r, true, null);
      }
      else if (g < 6 && consumeTokenSmart(b, "|")) {
        r = expression(b, l, 6);
        exit_section_(b, l, m, DISJUNCTIVE_EXPRESSION, r, true, null);
      }
      else if (g < 7 && consumeTokenSmart(b, "&")) {
        r = expression(b, l, 7);
        exit_section_(b, l, m, CONJUNCTIVE_EXPRESSION, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, "<<")) {
        r = expression(b, l, 8);
        exit_section_(b, l, m, SHIFT_LEFT_EXPRESSION, r, true, null);
      }
      else if (g < 8 && consumeTokenSmart(b, ">>")) {
        r = expression(b, l, 8);
        exit_section_(b, l, m, SHIFT_RIGHT_EXPRESSION, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "+")) {
        r = expression(b, l, 9);
        exit_section_(b, l, m, ADDITIVE_ADD_EXPRESSION, r, true, null);
      }
      else if (g < 9 && consumeTokenSmart(b, "-")) {
        r = expression(b, l, 9);
        exit_section_(b, l, m, ADDITIVE_SUB_EXPRESSION, r, true, null);
      }
      else if (g < 10 && consumeTokenSmart(b, "*")) {
        r = expression(b, l, 10);
        exit_section_(b, l, m, MULTIPLICATIVE_MUL_EXPRESSION, r, true, null);
      }
      else if (g < 10 && consumeTokenSmart(b, "/")) {
        r = expression(b, l, 10);
        exit_section_(b, l, m, MULTIPLICATIVE_DIV_EXPRESSION, r, true, null);
      }
      else if (g < 10 && consumeTokenSmart(b, "%")) {
        r = expression(b, l, 10);
        exit_section_(b, l, m, MULTIPLICATIVE_MOD_EXPRESSION, r, true, null);
      }
      else if (g < 11 && consumeTokenSmart(b, "**")) {
        r = expression(b, l, 11);
        exit_section_(b, l, m, EXPONENTIAL_EXPRESSION, r, true, null);
      }
      else if (g < 13 && parseTokensSmart(b, 0, DOT, NUMERAL)) {
        r = true;
        exit_section_(b, l, m, TUPLE_COMPONENT_EXPRESSION, r, true, null);
      }
      else if (g < 13 && parseTokensSmart(b, 0, DOT, IDENTIFIER)) {
        r = true;
        exit_section_(b, l, m, CIRCUIT_COMPONENT_EXPRESSION, r, true, null);
      }
      else if (g < 13 && unary_operator_call_0(b, l + 1)) {
        r = true;
        exit_section_(b, l, m, UNARY_OPERATOR_CALL, r, true, null);
      }
      else if (g < 13 && binary_operator_call_0(b, l + 1)) {
        r = report_error_(b, expression(b, l, 13));
        r = binary_operator_call_1(b, l + 1) && r;
        exit_section_(b, l, m, BINARY_OPERATOR_CALL, r, true, null);
      }
      else {
        exit_section_(b, l, m, null, false, false, null);
        break;
      }
    }
    return r;
  }

  // ":" expression
  private static boolean conditional_ternary_expression_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "conditional_ternary_expression_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ":");
    r = r && expression(b, l + 1, -1);
    exit_section_(b, m, null, r);
    return r;
  }

  public static boolean unary_minus_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_minus_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, "-");
    p = r;
    r = p && expression(b, l, 12);
    exit_section_(b, l, m, UNARY_MINUS_EXPRESSION, r, p, null);
    return r || p;
  }

  public static boolean unary_not_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_not_expression")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, null);
    r = consumeTokenSmart(b, "!");
    p = r;
    r = p && expression(b, l, 12);
    exit_section_(b, l, m, UNARY_NOT_EXPRESSION, r, p, null);
    return r || p;
  }

  // "." identifier "(" ")"
  private static boolean unary_operator_call_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "unary_operator_call_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, DOT, IDENTIFIER);
    r = r && consumeToken(b, "(");
    r = r && consumeToken(b, ")");
    exit_section_(b, m, null, r);
    return r;
  }

  // "." identifier "("
  private static boolean binary_operator_call_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_operator_call_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokensSmart(b, 0, DOT, IDENTIFIER);
    r = r && consumeToken(b, "(");
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ] ")"
  private static boolean binary_operator_call_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_operator_call_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = binary_operator_call_1_0(b, l + 1);
    r = r && consumeToken(b, ")");
    exit_section_(b, m, null, r);
    return r;
  }

  // [ "," ]
  private static boolean binary_operator_call_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "binary_operator_call_1_0")) return false;
    consumeToken(b, COMMA);
    return true;
  }

  // literal
  //                    | variable_or_free_constant
  //                    | associated_constant
  //                    | "(" expression ")"
  //                    | free_function_call
  //                    | static_function_call
  //                    | tuple_expression
  //                    | circuit_expression
  public static boolean primary_expression(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, PRIMARY_EXPRESSION, "<primary expression>");
    r = literal(b, l + 1);
    if (!r) r = variable_or_free_constant(b, l + 1);
    if (!r) r = associated_constant(b, l + 1);
    if (!r) r = primary_expression_3(b, l + 1);
    if (!r) r = free_function_call(b, l + 1);
    if (!r) r = static_function_call(b, l + 1);
    if (!r) r = tuple_expression(b, l + 1);
    if (!r) r = circuit_expression(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // "(" expression ")"
  private static boolean primary_expression_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary_expression_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokenSmart(b, "(");
    r = r && expression(b, l + 1, -1);
    r = r && consumeToken(b, ")");
    exit_section_(b, m, null, r);
    return r;
  }

}
