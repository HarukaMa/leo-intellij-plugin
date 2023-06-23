/*
 * Copyright (c) 2022-2023 Haruka Ma
 * This file is part of Leo / Aleo IntelliJ plugin.
 *
 * Leo / Aleo IntelliJ plugin is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * Leo / Aleo IntelliJ plugin is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Leo / Aleo IntelliJ plugin. If not, see <https://www.gnu.org/licenses/>.
 */

package im.mrx.leolanguage.leo.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.TokenType.WHITE_SPACE
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import im.mrx.leolanguage.leo.LeoUtils
import im.mrx.leolanguage.leo.inspection.quickfix.ProgramIdFix
import im.mrx.leolanguage.leo.psi.*
import im.mrx.leolanguage.leo.psi.LeoTypes.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.math.BigInteger

class LeoCompilerInspection : LocalInspectionTool() {

    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        return Visitor(holder)
    }
}


data class CoreFunction(
    val parameterCount: Int,
    val parameterTypes: List<List<String>>,
    val returnType: String,
)

val BHPInputDataType = listOf(
    "address", "bool", "field", "group", "i8", "i16", "i32", "i64", "i128",
    "u8", "u16", "u32", "u64", "u128", "scalar", "string", "struct",
)

val PED64InputDataType = listOf(
    "bool", "i8", "i16", "i32", "u8", "u16", "u32", "string", "struct",
)

val PED128InputDataType = listOf(
    "bool", "i8", "i16", "i32", "i64", "u8", "u16", "u32", "u64", "string", "struct",
)

val PSDInputDataType = listOf(
    "field", "i8", "i16", "i32", "i64", "i128", "u8", "u16", "u32", "u64", "u128", "scalar", "string", "struct",
)

fun getHashFunctions(inputDataType: List<List<String>>): List<Pair<String, CoreFunction>> {
    return listOf(
        Pair("hash_to_address", CoreFunction(1, inputDataType, "address")),
        Pair("hash_to_field", CoreFunction(1, inputDataType, "field")),
        Pair("hash_to_group", CoreFunction(1, inputDataType, "group")),
        Pair("hash_to_i8", CoreFunction(1, inputDataType, "i8")),
        Pair("hash_to_i16", CoreFunction(1, inputDataType, "i16")),
        Pair("hash_to_i32", CoreFunction(1, inputDataType, "i32")),
        Pair("hash_to_i64", CoreFunction(1, inputDataType, "i64")),
        Pair("hash_to_i128", CoreFunction(1, inputDataType, "i128")),
        Pair("hash_to_u8", CoreFunction(1, inputDataType, "u8")),
        Pair("hash_to_u16", CoreFunction(1, inputDataType, "u16")),
        Pair("hash_to_u32", CoreFunction(1, inputDataType, "u32")),
        Pair("hash_to_u64", CoreFunction(1, inputDataType, "u64")),
        Pair("hash_to_u128", CoreFunction(1, inputDataType, "u128")),
        Pair("hash_to_scalar", CoreFunction(1, inputDataType, "scalar")),
    )
}

fun getCommitFunctions(inputDataType: List<List<String>>): List<Pair<String, CoreFunction>> {
    return listOf(
        Pair("commit_to_address", CoreFunction(2, inputDataType, "address")),
        Pair("commit_to_field", CoreFunction(2, inputDataType, "field")),
        Pair("commit_to_group", CoreFunction(2, inputDataType, "group")),
    )
}

val BHPFunctions =
    (getHashFunctions(listOf(BHPInputDataType)) + getCommitFunctions(listOf(BHPInputDataType))).associate { it }

val PED64Functions =
    (getHashFunctions(listOf(PED64InputDataType)) + getCommitFunctions(listOf(PED64InputDataType))).associate { it }

val PED128Functions =
    (getHashFunctions(listOf(PED128InputDataType)) + getCommitFunctions(listOf(PED128InputDataType))).associate { it }

val PSDFunctions = getHashFunctions(listOf(PSDInputDataType)).associate { it }

val MappingFunctions = mapOf(
    Pair("get", CoreFunction(2, listOf(listOf("mapping"), listOf("pass")), "string")),
    Pair("get_or_use", CoreFunction(3, listOf(listOf("mapping"), listOf("pass"), listOf("pass")), "string")),
    Pair("set", CoreFunction(3, listOf(listOf("mapping"), listOf("pass"), listOf("pass")), "string")),
)

val coreFunctions = mapOf(
    Pair("BHP256", BHPFunctions),
    Pair("BHP512", BHPFunctions),
    Pair("BHP768", BHPFunctions),
    Pair("BHP1024", BHPFunctions),
    Pair("Pedersen64", PED64Functions),
    Pair("Pedersen128", PED128Functions),
    Pair("Poseidon2", PSDFunctions),
    Pair("Poseidon4", PSDFunctions),
    Pair("Poseidon8", PSDFunctions),
    Pair("Mapping", MappingFunctions),
)

private class Visitor(private val holder: ProblemsHolder) : LeoVisitor() {

    override fun visitProgramBlock(o: LeoProgramBlock) {
        run {
            val transitions = PsiTreeUtil.getChildrenOfType(o, LeoTransitionDeclaration::class.java) ?: return@run
            if (transitions.size > 31) {
                holder.registerProblem(
                    o,
                    "[ETYC0372052]: The number of transitions exceeds the maximum. snarkVM allows up to 31 transitions within a single program.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        run {
            val mappings = PsiTreeUtil.getChildrenOfType(o, LeoMappingDeclaration::class.java) ?: return@run
            if (mappings.size > 31) {
                holder.registerProblem(
                    o,
                    "[ETYC0372072]: The number of mappings exceeds the maximum. snarkVM allows up to 31 mappings within a single program.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitImportProgramId(o: LeoImportProgramId) {
        // Compiler #2
        run {
            val ref = o.reference?.resolve() != null
            if (!ref) {
                holder.registerProblem(
                    o,
                    "[ECMP0376002]: Attempted to import a file that does not exist `${o.text}`.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }

        // Parser #23
        run {
            val programId = o.programId
            val extension = programId.text.split(".").last()
            if (extension != "leo") {
                holder.registerProblem(
                    programId,
                    "[EPAR0370023]: Invalid import call to non-leo file.\n\nOnly imports of Leo `.leo` files are currently supported.",
                    ProblemHighlightType.GENERIC_ERROR,
                    TextRange(programId.text.indexOf('.') + 1, programId.text.length),
                )
            }
        }
    }

    override fun visitProgramDeclaration(o: LeoProgramDeclaration) {
        // Compiler #5
        run {
            val programName = o.programId?.text?.split(".")?.first() ?: return@run
            val file = o.containingFile.originalFile
            val scope = if (file.name == "main.leo" && (file.containingDirectory?.name ?: "") == "src") {
                val json = file.containingDirectory.parentDirectory?.findFile("program.json") ?: return@run
                Json.parseToJsonElement(json.text).jsonObject["program"]?.jsonPrimitive?.content?.split(".")
                    ?.first() ?: return@run
            } else {
                file.name.split(".").first()
            }
            if (programName != scope) {
                holder.registerProblem(
                    o.programId!!,
                    "[ECMP0376005]: The program scope name `${programName}` must match `${scope}`.",
                    ProblemHighlightType.GENERIC_ERROR,
                    ProgramIdFix(o.programId!!, scope)
                )
            }
        }

        // Parser #29
        run {
            val networkIdentifier = o.programId?.text?.split(".")?.last() ?: return@run
            if (networkIdentifier != "aleo") {
                holder.registerProblem(
                    o.programId!!,
                    "[EPAR0370028]: Invalid network identifier. The only supported identifier is `aleo`.",
                    ProblemHighlightType.GENERIC_ERROR,
                    TextRange(o.programId!!.text.indexOf('.') + 1, o.programId!!.text.length),
                )
            }
        }
    }

    override fun visitAnnotation(o: LeoAnnotation) {
        // Parser #24
        run {
            var child = o.firstChild
            while (child != null) {
                if (child is PsiWhiteSpace) {
                    holder.registerProblem(
                        o,
                        "[EPAR0370024]: Illegal spacing in the annotation declaration.\n\nRemove whitespace between the `@` symbol and the identifier.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                    break
                }
                child = child.nextSibling
            }
        }
        // Type checker #28
        run {
            holder.registerProblem(
                o,
                "[ETYC0372028]: Unknown annotation: `${o.text}`.",
                ProblemHighlightType.GENERIC_ERROR
            )
        }
    }

    override fun visitAssignmentStatement(o: LeoAssignmentStatement) {
        // Type checker #0
        run {
            val expression = o.expressionList.first()
            if (expression.elementType != VARIABLE) {
                holder.registerProblem(
                    expression,
                    "[ETYC0372000]: invalid assignment target",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }

        // Type checker #3
        run {
            val variable = o.expressionList.first().firstChild
            variable.reference?.resolve()?.let { resolved ->
                val element = resolved as? LeoTypedElement ?: return@run
                val type = LeoUtils.typeToString(element) ?: return@run
                val value = o.expressionList.last()
                val actualType = getExpressionType(value)
                checkTYC3(value, type, actualType)
            }
        }
    }

    override fun visitReturnStatement(o: LeoReturnStatement) {
        // Type checker #3
        run {
            val function =
                PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) ?: PsiTreeUtil.getParentOfType(
                    o,
                    LeoFunctionLikeDeclaration::class.java
                ) ?: return@run
            val type = LeoUtils.typeToString(function) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
        // Type checker #35
        run {
            if (o.finalizeLiteral != null) {
                fun registerTYC35Problem() {
                    holder.registerProblem(
                        o,
                        "[ETYC0372036]: Cannot use a `finalize` statement without a `finalize` block.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }

                val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java)
                if (transition == null) {
                    registerTYC35Problem()
                } else {
                    transition.finalizer ?: registerTYC35Problem()
                }
            }
        }
        // Type checker #42
        run {
            val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) ?: return@run
            val finalizer = transition.finalizer ?: return@run
            val params = finalizer.functionParameterList?.functionParameterList?.size ?: return@run
            val actualParams = o.functionArguments?.expressionList?.size ?: return@run
            if (params != actualParams) {
                holder.registerProblem(
                    o,
                    "[ETYC0372042]: `finalize` expected `$params` args, but got `$actualParams`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitVariableLikeDeclaration(o: LeoVariableLikeDeclaration) {
        // Type checker #3
        run {
            val type = LeoUtils.typeToString(o) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitLoopStatement(o: LeoLoopStatement) {
        // Type checker #3
        run {
            val type = LeoUtils.typeToString(o) ?: return@run
            if (o.expressionList.size != 2) {
                return
            }
            val firstType = getExpressionType(o.expressionList[0])
            val secondType = getExpressionType(o.expressionList[1])
            checkTYC3(o.expressionList[0], type, firstType)
            checkTYC3(o.expressionList[1], type, secondType)
        }

        run {
            if (o.expressionList.size != 2) {
                return
            }
            val firstExpression = o.expressionList[0]
            val secondExpression = o.expressionList[1]
            fun checkTYC49(expression: LeoExpression) {
                (expression as? LeoPrimaryExpression).let {
                    if (it?.numericLiteral == null) {
                        holder.registerProblem(
                            expression,
                            "[ETYC0372049]: Loop bound must be a literal.",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                }
            }
            checkTYC49(firstExpression)
            checkTYC49(secondExpression)
        }
    }

    override fun visitFreeFunctionCall(o: LeoFreeFunctionCall) {
        // Type checker #3, #6
        run {
            val types = LeoUtils.functionParameterListToTypeString(
                (o.locator ?: o.functionIdentifier ?: return@run).reference?.resolve() as? LeoFunctionLikeDeclaration
                    ?: return@run
            ).split(", ").toMutableList()
            types.remove("")
            o.functionArguments.expressionList.forEachIndexed { index, expression ->
                val type = types.getOrNull(index) ?: return@forEachIndexed
                val actualType = getExpressionType(expression)
                checkTYC3(expression, type, actualType)
            }
            val args = types.size
            val actualArgs = o.functionArguments.expressionList.size
            if (args != actualArgs) {
                holder.registerProblem(
                    o,
                    "[ETYC0372006]: Call expected `${args}` args, but got `${actualArgs}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #5
        run {
            o.functionIdentifier?.let {
                if (it.text in listOf("assert", "assert_eq", "assert_neq")) {
                    return@let
                }
                it.reference?.resolve() ?: run {
                    holder.registerProblem(
                        it,
                        "[ETYC0372005]: Unknown function `${it.text}`",
                        ProblemHighlightType.ERROR
                    )
                }
            }
            o.locator?.let {
                it.reference?.resolve() ?: run {
                    it.identifier?.let { identifier ->
                        holder.registerProblem(
                            identifier,
                            "[ETYC0372005]: Unknown function `${identifier.text}`",
                            ProblemHighlightType.ERROR
                        )
                    }
                }
            }
        }
        // Type checker #47
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) == null
                && o.locator == null
                && o.functionIdentifier?.reference?.resolve() as? LeoInlineDeclaration == null
            ) {
                holder.registerProblem(
                    o,
                    "[ETYC0372047]: Only `inline` can be called from a `function` or `inline`.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #48
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) != null
                && o.locator == null
                && o.functionIdentifier?.reference?.resolve() as? LeoTransitionDeclaration != null
            ) {
                holder.registerProblem(
                    o,
                    "[ETYC0372048]: Cannot call a local transition function from a transition function.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitAdditiveAddExpression(o: LeoAdditiveAddExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitAdditiveSubExpression(o: LeoAdditiveSubExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeMulExpression(o: LeoMultiplicativeMulExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeDivExpression(o: LeoMultiplicativeDivExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitMultiplicativeModExpression(o: LeoMultiplicativeModExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitEqualityExpression(o: LeoEqualityExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitInequalityExpression(o: LeoInequalityExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitConjunctiveExpression(o: LeoConjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitDisjunctiveExpression(o: LeoDisjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitExclusiveDisjunctiveExpression(o: LeoExclusiveDisjunctiveExpression) {
        // Type checker #3
        run {
            val type = getExpressionType(o.expressionList.first())
            val actualType = getExpressionType(o.expressionList.last())
            checkTYC3(o.expressionList.last(), type, actualType)
        }
    }

    override fun visitVariable(o: LeoVariable) {
        // Type checker #5
        run {
            o.reference?.resolve() ?: run {
                holder.registerProblem(
                    o,
                    "[ETYC0372005]: Unknown variable `${o.identifier.text}`",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitStructExpression(o: LeoStructExpression) {
        // Type checker #5, #12, #13
        run {
            val identifier = o.structExpressionIdentifier
            val declaration = identifier.reference?.resolve() as? LeoStructLikeDeclaration
            if (declaration == null) {
                holder.registerProblem(
                    identifier,
                    "[ETYC0372005]: Unknown struct `${identifier.text}`",
                    ProblemHighlightType.ERROR
                )
                return@run
            }
            val memberCount =
                declaration.structComponentDeclarations?.structComponentDeclarationList?.size ?: return@run
            val actualMemberCount = o.structComponentInitializerList.size
            if (memberCount != actualMemberCount) {
                holder.registerProblem(
                    o,
                    "[ETYC0372012]: Struct expected `${memberCount}` members, but got `${actualMemberCount}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            val members = declaration.structComponentDeclarations!!.structComponentDeclarationList.map { it.name }
            val actualMembers = o.structComponentInitializerList.map { it.structComponentIdentifier.identifier.text }
            members.forEach { member ->
                if (!actualMembers.contains(member)) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372013]: Struct initialization expression for `${identifier.text}` is missing member `${member}`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitStructComponentInitializer(o: LeoStructComponentInitializer) {
        // Type checker #3
        run {
            val declaration =
                o.structComponentIdentifier.reference?.resolve() as? LeoStructComponentDeclaration ?: return@run
            val type = LeoUtils.typeToString(declaration) ?: return@run
            val actualType = getExpressionType(o.expression ?: return@run)
            checkTYC3(o.expression!!, type, actualType)
        }
    }

    override fun visitPrimaryExpression(o: LeoPrimaryExpression) {
        // Type checker #8
        run {
            if (o.firstChild is LeafPsiElement && o.firstChild.elementType != LPAREN) {
                val type = getExpressionType(o)
                if (type == "pass" || type == "unknown") {
                    return@run
                }
                if (type !in listOf("u8", "u16", "u32", "u64", "u128", "i8", "i16", "i32", "i64", "i128")) {
                    return@run
                }
                val number = o.firstChild.text.replace(type, "")
                if (!checkNumberRange(number, type)) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372008]: The value $number is not a valid `${type}`",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitUnaryMinusExpression(o: LeoUnaryMinusExpression) {
        // Type checker #8
        run {
            val type = getExpressionType(o.expression ?: return@run)
            if (type == "pass" || type == "unknown") {
                return@run
            }
            if (type !in listOf("u8", "u16", "u32", "u64", "u128", "i8", "i16", "i32", "i64", "i128")) {
                return@run
            }
            val number = o.expression!!.text.replace(type, "")
            if (!checkNumberRange("-$number", type)) {
                holder.registerProblem(
                    o,
                    "[ETYC0372008]: The value -$number is not a valid `${type}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitAssociatedFunctionCall(o: LeoAssociatedFunctionCall) {

        // Type checker #6, #9, #46
        run {
            fun registerTYC9Problem() {
                holder.registerProblem(
                    o.namedType,
                    "[ETYC0372009]: ${o.namedType.text}::${o.identifier.text} is not a valid core function.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }

            val coreStruct = o.namedType.identifier?.text
            if (coreStruct == null) {
                registerTYC9Problem()
                return@run
            }
            val function = o.identifier.text
            if (function !in (coreFunctions[coreStruct] ?: mapOf())) {
                registerTYC9Problem()
                return@run
            }
            val args = (coreFunctions[coreStruct] ?: mapOf())[function]?.parameterCount ?: run inner@{
                registerTYC9Problem()
                return@run
            }
            val actualArgs = o.functionArguments.expressionList.size
            if (actualArgs != args) {
                holder.registerProblem(
                    o,
                    "[ETYC0372006]: Call expected `${args}` args, but got `${actualArgs}`",
                    ProblemHighlightType.GENERIC_ERROR
                )
                return@run
            }

            val expectedTypes = coreFunctions[coreStruct]!![function]!!.parameterTypes
            for (i in 0 until args) {
                val arg = o.functionArguments.expressionList[i]
                val type = getExpressionType(arg)
                checkTYC7(arg, expectedTypes[i], type)
            }

            if (o.namedType.text == "Mapping") {
                @Suppress("NAME_SHADOWING")
                val args = o.functionArguments.expressionList
                val mapping = (args[0] as? LeoPrimaryExpression)?.variable ?: return@run
                val mappingDeclaration = mapping.reference?.resolve() as? LeoMappingDeclaration
                if (mappingDeclaration != null) {
                    val mappingTypes = mappingDeclaration.mappingTypeList
                    val keyType = LeoUtils.typeToString(mappingTypes[0]) ?: return@run
                    val valueType = LeoUtils.typeToString(mappingTypes[1]) ?: return@run
                    val actualKeyType = getExpressionType(args[1])
                    val actualValueType = getExpressionType(args[2])
                    checkTYC7(args[1], listOf(keyType), actualKeyType)
                    checkTYC7(args[2], listOf(valueType), actualValueType)
                }
            }
        }
    }

    override fun visitMethodCall(o: LeoMethodCall) {
        // Parser #21
        run {
            val unaryList = listOf(
                "abs",
                "abs_wrapped",
                "double",
                "inv",
                "neg",
                "not",
                "square",
                "square_root"
            )
            val binaryList = listOf(
                "add",
                "add_wrapped",
                "and",
                "div",
                "div_wrapped",
                "eq",
                "gte",
                "gt",
                "lte",
                "lt",
                "mod",
                "mul",
                "mul_wrapped",
                "nand",
                "neq",
                "nor",
                "or",
                "pow",
                "pow_wrapped",
                "rem",
                "rem_wrapped",
                "shl",
                "shl_wrapped",
                "shr",
                "shr_wrapped",
                "sub",
                "sub_wrapped",
                "xor"
            )
            val operator = o.identifier.text
            val argsSize = o.functionArguments.expressionList.size
            if (argsSize == 0 && operator in unaryList) {
                return@run
            }
            if (argsSize == 1 && operator in binaryList) {
                return@run
            }
            if (argsSize == 1 && operator == "get") {
                return@run
            }
            if (argsSize == 2 && operator in listOf("get_or_use", "set")) {
                return@run
            }
            holder.registerProblem(
                o,
                "[EPAR0370021]: The type of ${o.expression.text} has no associated function $operator that takes $argsSize argument(s).",
                ProblemHighlightType.GENERIC_ERROR
            )
        }
    }

    override fun visitStructLikeDeclaration(o: LeoStructLikeDeclaration) {
        // Type checker #15, #16
        run {
            val name = o.identifier?.text ?: return@run
            val members = o.structComponentDeclarations?.structComponentDeclarationList ?: return@run
            if (members.size != members.distinct().size) {
                if (o is LeoStructDeclaration) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372015]: Struct $name defined with more than one member with the same name.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                } else {
                    holder.registerProblem(
                        o,
                        "[ETYC0372016]: Record $name defined with more than one member with the same name.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitNamedType(o: LeoNamedType) {
        // Type checker #17
        run {
            if (o.namedPrimitiveType != null || o.identifier != null) {
                return@run
            }
            o.reference?.resolve() ?: o.locator?.reference?.resolve() ?: run {
                holder.registerProblem(
                    o,
                    "[ETYC0372017]: The type `${o.text}` is not found in the current scope.",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitStructComponentExpression(o: LeoStructComponentExpression) {
        // Type checker #18
        run check@{
            val identifier = o.structComponentIdentifier
            identifier.reference?.resolve() ?: run {
                val struct = (identifier.typeElement as? LeoNamedElement)?.name ?: return@check
                holder.registerProblem(
                    identifier,
                    "[ETYC0372018]: Variable ${identifier.text} is not a member of struct $struct.",
                    ProblemHighlightType.ERROR
                )
            }
        }
    }

    override fun visitRecordDeclaration(o: LeoRecordDeclaration) {
        // Type checker #19, #20
        run {
            val members = o.structComponentDeclarations?.structComponentDeclarationList ?: return@run
            val memberNames = members.map { it.name }
            for ((variable, type) in listOf(Pair("owner", "address"))) {
                if (variable !in memberNames) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372019]: The `record` type requires the variable `$variable: $type`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                } else {
                    val member = members.find { it.name == variable } ?: continue
                    val memberType = LeoUtils.typeToString(member) ?: continue
                    if (memberType != type) {
                        holder.registerProblem(
                            member,
                            "[ETYC0372020]: The field `$variable` in a `record` must have type `$type`.",
                            ProblemHighlightType.GENERIC_ERROR
                        )
                    }
                }
            }
        }
    }

    override fun visitExpression(o: LeoExpression) {
        // Type checker #3, #21
        run {
            if (o is LeoOrderingExpression) {
                val type = getExpressionType(o.expressionList.first())
                val actualType = getExpressionType(o.expressionList.last())
                checkTYC3(o.expressionList.last(), type, actualType)
                if (type == "address" || actualType == "address") {
                    var symbol = o.expressionList.first().nextSibling
                    while (symbol.elementType != SYMBOL) {
                        symbol = symbol.nextSibling
                        if (symbol == null) {
                            return@run
                        }
                    }
                    holder.registerProblem(
                        o,
                        "[ETYC0372021]: Comparison `${symbol.text}` is not supported for the address type.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitTupleComponentExpression(o: LeoTupleComponentExpression) {
        // Type checker #24
        run {
            val tuple = o.expression
            val tupleType = getExpressionType(tuple)
            val index = o.numeral.text.toIntOrNull() ?: return@run
            val tupleSize = tupleType.split(", ").size
            if (index >= tupleSize) {
                holder.registerProblem(
                    o.numeral,
                    "[ETYC0372024]: Tuple index `$index` out of range for a tuple with length `$tupleSize`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitFunctionParameter(o: LeoFunctionParameter) {
        // Type checker #25
        run {
            if (o.tupleType != null) {
                holder.registerProblem(
                    o.tupleType!!,
                    "[ETYC0372025]: Tuples are only allowed as function return types.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) != null) {
                if (o.mode != null && o.mode!!.text != "public") {
                    holder.registerProblem(
                        o.mode!!,
                        "[ETYC0372032]: An input to a finalize block must be public.\n\nUse a `public` modifier to the input variable declaration or remove the visibility modifier entirely.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #41
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) != null) {
                if (o.mode != null && o.mode!!.text.contains("const")) {
                    holder.registerProblem(
                        o.mode!!,
                        "[ETYC0372041]: Transition functions cannot have constant inputs.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
    }

    override fun visitFunctionDeclaration(o: LeoFunctionDeclaration) {
        // Type checker #29
        run {
            o.functionParameterList?.functionParameterList?.forEach { parameter ->
                if (parameter.mode != null) {
                    holder.registerProblem(
                        parameter.firstChild!!,
                        "[ETYC0372028]: Standard functions cannot have modes associated with their inputs.\n\nConsider removing the mode or using the keyword `transition` instead of `function`.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        run {
            if (o.mode != null && o.mode!!.text == "constant") {
                holder.registerProblem(
                    o.mode!!,
                    "[ETYC0372040]: A returned value cannot be a constant.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitTransitionDeclaration(o: LeoTransitionDeclaration) {
        run {
            if (o.mode != null && o.mode!!.text == "constant") {
                holder.registerProblem(
                    o.mode!!,
                    "[ETYC0372040]: A returned value cannot be a constant.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitBlock(o: LeoBlock) {
        // Type checker #26
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoLoopStatement::class.java) != null) {
                return@run
            }
            val statements = o.children
            var returnIndex = statements.size
            statements.forEachIndexed { index, statement ->
                if (statement is LeoReturnStatement) {
                    returnIndex = index
                }
                if (index > returnIndex) {
                    holder.registerProblem(
                        statement,
                        "[ETYC0372025]: Cannot reach the following statement.\n\nRemove the unreachable code.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #27, #36
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoLoopStatement::class.java) == null) {
                return@run
            }
            PsiTreeUtil.findChildOfType(o, LeoReturnStatement::class.java)?.let {
                holder.registerProblem(
                    o,
                    "[ETYC0372026]: Loop body contains a return statement or always returns.\n\nRemove the code in the loop body that always returns.",
                    ProblemHighlightType.GENERIC_ERROR
                )
                if (it.finalizeLiteral != null) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372037]: Loop body contains a finalize statement.\n\nRemove the finalize statement.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #33
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) == null) {
                return@run
            }
            PsiTreeUtil.findChildOfType(o, LeoReturnStatement::class.java)?.let {
                if (it.finalizeLiteral != null) {
                    holder.registerProblem(
                        o,
                        "[ETYC0372034]: A finalize block cannot contain a finalize statement.",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
        // Type checker #34
        run {
            if (PsiTreeUtil.getParentOfType(o, LeoFinalizer::class.java) != null) {
                return@run
            }
            fun registerTYC35Problem(e: PsiElement) {
                holder.registerProblem(
                    e,
                    "[ETYC0372035]: This statement must be inside a finalize block.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            for (statement in o.children) {
                if (statement is LeoExpressionStatement) {
                    when (val expression = statement.expression) {
                        is LeoMethodCall -> {
                            val primary = expression.expression as? LeoPrimaryExpression ?: return@run
                            val source = primary.variable?.reference?.resolve() ?: return@run
                            if (source is LeoMappingDeclaration && expression.identifier.text in coreFunctions["Mapping"]!!) {
                                registerTYC35Problem(statement)
                            }
                        }

                        is LeoAssociatedFunctionCall -> {
                            if (expression.namedType.text == "Mapping") {
                                registerTYC35Problem(statement)
                            }
                        }
                    }
                }
            }
        }
        // Type checker #38, #44
        run {
            val function = o.parent as? LeoFunctionLikeDeclaration ?: return@run

            val (hasReturn, hasFinalizer) = traverseBlock(o)
            if (LeoUtils.typeToString(function) != null && !hasReturn) {
                holder.registerProblem(
                    o,
                    "[ETYC0372038]: Function must return a value.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            if (function is LeoTransitionDeclaration && function.finalizer != null && !hasFinalizer) {
                holder.registerProblem(
                    o,
                    "[ETYC0372044]: Function must contain a `finalize` statement on all execution paths.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitStructComponentDeclaration(o: LeoStructComponentDeclaration) {
        // Type checker #25
        run {
            if (o.tupleType != null) {
                holder.registerProblem(
                    o.tupleType!!,
                    "[ETYC0372025]: Tuples are only allowed as function return types.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #30
        run {
            if ((o.namedType?.reference?.resolve() as? LeoRecordDeclaration) != null) {
                holder.registerProblem(
                    o.namedType!!,
                    "[ETYC0372029]: A struct or record cannot contain another record.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitMappingDeclaration(o: LeoMappingDeclaration) {
        // Type checker #31
        run {
            fun registerTYC30Problem(element: PsiElement, component: String, type: String) {
                holder.registerProblem(
                    element,
                    "[ETYC0372030]: A mapping's $component cannot be a $type",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
            o.mappingTypeList.first().let { key ->
                val source = key.namedType?.reference?.resolve()
                if (source as? LeoMappingDeclaration != null) {
                    registerTYC30Problem(key, "key", "mapping")
                } else if (source as? LeoRecordDeclaration != null) {
                    registerTYC30Problem(key, "key", "record")
                }
                if (key.tupleType != null) {
                    registerTYC30Problem(key, "key", "tuple")
                }
            }
            o.mappingTypeList.last().let { value ->
                val source = value.namedType?.reference?.resolve()
                if (source as? LeoMappingDeclaration != null) {
                    registerTYC30Problem(value, "value", "mapping")
                } else if (source as? LeoRecordDeclaration != null) {
                    registerTYC30Problem(value, "value", "record")
                }
                if (value.tupleType != null) {
                    registerTYC30Problem(value, "value", "tuple")
                }
            }
        }
    }

    override fun visitFinalizer(o: LeoFinalizer) {
        run {
            if (o.mode != null && o.mode!!.text != "public") {
                holder.registerProblem(
                    o.mode!!,
                    "[ETYC0372033]: An output from a finalize block must be public.\n\nUse a `public` modifier to the output type declaration or remove the visibility modifier entirely.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #39
        run {
            if ((o.block?.children?.size ?: return@run) == 0) {
                holder.registerProblem(
                    o,
                    "[ETYC0372039]: A finalize block cannot be empty.",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
        // Type checker #45
        run {
            val transition = PsiTreeUtil.getParentOfType(o, LeoTransitionDeclaration::class.java) ?: return@run
            val transitionName = transition.name ?: return@run
            val finalizerName = o.name ?: return@run
            if (finalizerName != transitionName) {
                holder.registerProblem(
                    o.identifier!!,
                    "[ETYC0372045]: `finalize` name `$finalizerName` does not match function name `$transitionName`",
                    ProblemHighlightType.GENERIC_ERROR
                )
            }
        }
    }

    override fun visitConsoleStatement(o: LeoConsoleStatement) {
        run {
            holder.registerProblem(
                o,
                "[EPAR0370032]: `console` statements are not yet supported.\n\nConsider using `assert`, `assert_eq`, or `assert_neq` instead.",
                ProblemHighlightType.GENERIC_ERROR
            )
        }
    }

    private fun traverseBlock(block: LeoBlock): Pair<Boolean, Boolean> {
        var hasReturn = false
        var hasFinalize = false
        block.children.forEach { statement ->
            when (statement) {
                is LeoReturnStatement -> {
                    hasReturn = true
                    if (statement.finalizeLiteral != null) {
                        hasFinalize = true
                    }
                }

                is LeoBlock -> {
                    val (blockHasReturn, blockHasFinalize) = traverseBlock(statement)
                    hasReturn = hasReturn || blockHasReturn
                    hasFinalize = hasFinalize || blockHasFinalize
                }

                is LeoConditionalStatement -> {
                    val (blockHasReturn, blockHasFinalize) = traverseConditionalStatement(statement)
                    hasReturn = hasReturn || blockHasReturn
                    hasFinalize = hasFinalize || blockHasFinalize
                }
            }
        }
        return Pair(hasReturn, hasFinalize)
    }

    private fun traverseConditionalStatement(statement: LeoConditionalStatement): Pair<Boolean, Boolean> {
        val (hasReturn, hasFinalize) = traverseBlock(statement.branch.block ?: return Pair(false, false))
        var elseHasReturn = false
        var elseHasFinalize = false
        if (statement.conditionalStatement != null) {
            val (blockHasReturn, blockHasFinalize) = traverseConditionalStatement(statement.conditionalStatement!!)
            elseHasReturn = blockHasReturn
            elseHasFinalize = blockHasFinalize
        } else if (statement.block != null) {
            val (blockHasReturn, blockHasFinalize) = traverseBlock(statement.block!!)
            elseHasReturn = blockHasReturn
            elseHasFinalize = blockHasFinalize
        }
        return Pair(hasReturn && elseHasReturn, hasFinalize && elseHasFinalize)
    }

    private fun checkNumberRange(number: String, type: String): Boolean {
        val value = number.toBigInteger()
        val lower: BigInteger
        val upper: BigInteger
        val bits = type.substring(1).toInt()
        if (type.contains('u')) {
            lower = BigInteger.ZERO
            upper = BigInteger.TWO.pow(bits) - BigInteger.ONE
        } else {
            lower = BigInteger.TWO.pow(bits - 1).unaryMinus()
            upper = BigInteger.TWO.pow(bits - 1) - BigInteger.ONE
        }
        return value in lower..upper
    }

    private fun checkTYC3(o: PsiElement, type: String, actualType: String) {
        if (actualType != "pass") {
            if (actualType.contains('|')) {
                val actualTypes = actualType.split('|')
                actualTypes.filter { it != type }.forEach { registerTYC3Problem(o, type, it) }
                actualTypes.filter { it == "unknown" }.forEach { _ -> registerTYC4Problem(o) }
            } else if (actualType == "unknown") {
                registerTYC4Problem(o)
            } else if (actualType != type) {
                registerTYC3Problem(o, type, actualType)
            }
        }
    }

    private fun registerTYC3Problem(value: PsiElement, expected: String, actual: String) {
        holder.registerProblem(
            value,
            "[ETYC0372003]: Expected type `${expected}` but type `${actual}` was found",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    private fun registerTYC4Problem(value: PsiElement) {
        holder.registerProblem(
            value,
            "[ETYC0372004]: Could not determine the type of `${value.text}`\n\nThis might be a bug in the language plugin. Please report this issue.",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    private fun registerTYC7Problem(value: PsiElement, expected: String, actual: String) {
        holder.registerProblem(
            value,
            "[ETYC0372007]: Expected one type from `${expected}`, but got `${actual}`",
            ProblemHighlightType.GENERIC_ERROR
        )
    }

    private fun checkTYC7(o: PsiElement, type: List<String>, actualType: String) {
        if ("pass" !in type && actualType != "pass") {
            if (actualType !in type) {
                registerTYC7Problem(o, type.joinToString(", "), actualType)
            }
        }
    }

    private fun getExpressionType(element: PsiElement): String {
        return when (element) {
            is LeoPrimaryExpression -> getPrimaryExpressionType(element)

            is LeoTupleComponentExpression -> getTupleComponentType(element)
            is LeoStructComponentExpression -> getStructComponentType(element)
            is LeoAssociatedFunctionCall -> "pass" // TODO check types

            is LeoUnaryNotExpression -> getExpressionType(element.expression ?: return "pass")
            is LeoUnaryMinusExpression -> getExpressionType(element.expression ?: return "pass")

            is LeoExponentialExpression -> getExpressionType(element.expressionList.first())

            is LeoMultiplicativeMulExpression -> getExpressionType(element.expressionList.first())
            is LeoMultiplicativeDivExpression -> getExpressionType(element.expressionList.first())
            is LeoMultiplicativeModExpression -> getExpressionType(element.expressionList.first())

            is LeoAdditiveAddExpression -> getExpressionType(element.expressionList.first())
            is LeoAdditiveSubExpression -> getExpressionType(element.expressionList.first())

            is LeoShiftLeftExpression -> getExpressionType(element.expressionList.first())
            is LeoShiftRightExpression -> getExpressionType(element.expressionList.first())

            is LeoConjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoDisjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoExclusiveDisjunctiveExpression -> getExpressionType(element.expressionList.first())

            is LeoOrderingLessExpression -> "bool"
            is LeoOrderingGreaterExpression -> "bool"
            is LeoOrderingLessOrEqualExpression -> "bool"
            is LeoOrderingGreaterOrEqualExpression -> "bool"

            is LeoEqualityExpression -> "bool"
            is LeoInequalityExpression -> "bool"

            is LeoConditionalConjunctiveExpression -> "bool"

            is LeoConditionalDisjunctiveExpression -> "bool"

            is LeoConditionalTernaryExpression -> getTernaryType(element)

            else -> element.elementType.toString()
        }
    }

    private fun getTupleType(element: LeoTupleExpression): String {
        val types = element.expressionList.map { getExpressionType(it) }
        return "(${types.joinToString(", ")})"
    }

    private fun getPrimaryExpressionType(expression: LeoPrimaryExpression): String {
        return when (val exp = expression.firstChild) {
            is LeoAssociatedFunctionCall -> {
                val coreStruct = exp.namedType.identifier?.text ?: return "pass"
                val function = exp.identifier.text
                if (exp.namedType.text == "Mapping") {
                    val mappingDeclaration =
                        (exp.functionArguments.expressionList[0] as LeoPrimaryExpression).variable?.reference?.resolve() as? LeoMappingDeclaration
                            ?: return "unknown"
                    if (mappingDeclaration.mappingTypeList.size != 2)
                        return "unknown"
                    return LeoUtils.typeToString(mappingDeclaration.mappingTypeList[1]) ?: "unknown"
                }
                coreFunctions[coreStruct]?.get(function)?.returnType ?: "pass"
            }

            is LeoAssociatedConstant -> "pass" // TODO check types
            is LeoSelfCaller -> "address"
            is LeoBlockHeight -> "u32"

            is LeoFreeFunctionCall ->
                (exp.functionIdentifier?.reference?.resolve() as? LeoTypedElement)?.let {
                    LeoUtils.typeToString(it) ?: "unknown"
                } ?: "pass"

            is LeoTupleExpression -> getTupleType(exp)
            is LeoStructExpression -> exp.structExpressionIdentifier.reference?.resolve()?.let {
                exp.structExpressionIdentifier.text
            } ?: "pass"

            is LeoVariable -> exp.reference?.resolve()?.let {
                if (it is LeoMappingDeclaration)
                    "mapping"
                else
                    LeoUtils.typeToString(it as? LeoTypedElement ?: return@let "unknown") ?: "unknown"
            } ?: "pass"

            else ->
                if (exp.elementType == LPAREN) {
                    var realExp = exp.nextSibling
                    while (realExp.elementType == WHITE_SPACE) {
                        realExp = realExp.nextSibling
                    }
                    getExpressionType(realExp)
                } else if (exp is LeafPsiElement) {
                    getLiteralType(exp)
                } else {
                    exp.elementType.toString()
                }
        }
    }

    private fun getLiteralType(literal: PsiElement): String {
        return when (literal.elementType) {
            ADDRESS_LITERAL -> "address"
            BOOLEAN_LITERAL -> "bool"
            NUMERIC_LITERAL -> getNumericLiteralType(literal)
            STRING_LITERAL -> "string"
            else -> "unknown"
        }
    }

    private fun getNumericLiteralType(literal: PsiElement): String {
        val text = literal.text
        val regex = Regex("^[0-9]+([ui](?>8|16|32|64|128)|field|group|scalar)")
        val match = regex.find(text) ?: return "unknown"
        return match.groups[1]?.value ?: "unknown"
    }

    private fun getTupleComponentType(tuple: LeoTupleComponentExpression): String {
        val tupleIndex = tuple.numeral.text.toInt()
        val tupleType = getExpressionType(tuple.expression)
        if (tupleType.startsWith("(") && tupleType.endsWith(")")) {
            val types = tupleType.substring(1, tupleType.length - 1).split(",")
            if (tupleIndex < types.size) {
                return types[tupleIndex].trim()
            }
        }
        return "pass"
    }

    private fun getStructComponentType(struct: LeoStructComponentExpression): String {
        return struct.structComponentIdentifier.reference?.resolve()?.let { resolved ->
            if (resolved is LeoTypedElement) {
                LeoUtils.typeToString(resolved) ?: "unknown"
            } else {
                "unknown"
            }
        } ?: "pass"
    }

    private fun getTernaryType(ternary: LeoConditionalTernaryExpression): String {
        if (ternary.expressionList.size != 3) {
            return "unknown"
        }
        val firstType = getExpressionType(ternary.expressionList[1])
        val secondType = getExpressionType(ternary.expressionList[2])
        return "$firstType|$secondType"
    }
}