package hu.gabe.kosu.compiler

import org.jetbrains.kotlin.DeprecatedForRemovalCompilerApi
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrSimpleType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.isCollection
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

@OptIn(UnsafeDuringIrConstructionAPI::class)
class KosuPropagationIrTransformer(
    private val pluginContext: IrPluginContext,
    private val messageCollector: MessageCollector = MessageCollector.NONE,
) : IrElementTransformerVoidWithContext() {

    private val iKosuVisitable = pluginContext.referenceClass(ClassId.fromString("hu/gabe/kosu/child/IKosuVisitable"))
    private val kosuChildClass = pluginContext.referenceClass(ClassId.fromString("hu/gabe/kosu/child/KosuChild"))
    private val listClass = pluginContext.referenceClass(ClassId.fromString("kotlin/collections/List"))
    private val mutableListOfs =
        pluginContext.referenceFunctions(CallableId(FqName("kotlin.collections"), Name.identifier("mutableListOf")))
    private val listAdds = pluginContext.referenceFunctions(
        CallableId(
            FqName("kotlin.collections"),
            FqName("MutableList"),
            Name.identifier("add")
        )
    )
    private val listAddAlls = pluginContext.referenceFunctions(
        CallableId(
            FqName("kotlin.collections"),
            FqName("MutableList"),
            Name.identifier("addAll")
        )
    )
    private val kosuVisitableAnnotation = FqName("hu.gabe.kosu.annotations.KosuVisitable")
    private val ignoreAnnotation = FqName("hu.gabe.kosu.annotations.Ignore")

    private val mutableListOf =
        mutableListOfs.single { symbol -> symbol.owner.parameters.none { it.kind == IrParameterKind.Regular || it.kind == IrParameterKind.Context } }
    private val listAdd =
        listAdds.single { symbol -> symbol.owner.parameters.filter { it.kind == IrParameterKind.Regular || it.kind == IrParameterKind.Context }.size == 1 }
    private val listAddAll =
        listAddAlls.first { symbol -> symbol.owner.parameters.first { it.kind == IrParameterKind.Regular || it.kind == IrParameterKind.Context }.type.isCollection() }

    @OptIn(DeprecatedForRemovalCompilerApi::class)
    private fun implementKosuVisitable(declaration: IrClass) {
        declaration.superTypes += iKosuVisitable!!.defaultType

        val getVisitableChildrenFun = declaration.addFunction {
            name = Name.identifier("getVisitableChildren")
            returnType = pluginContext.irBuiltIns.collectionClass.defaultType
            modality = Modality.OPEN
        }.apply {
            overriddenSymbols += iKosuVisitable.functions.single { it.owner.name.asString() == "getVisitableChildren" }
            dispatchReceiverParameter = declaration.thisReceiver!!.copyTo(this)
        }

        getVisitableChildrenFun.body = DeclarationIrBuilder(pluginContext, getVisitableChildrenFun.symbol).irBlockBody {
            // val children = mutableListOf<Any>()
            val childrenList = irTemporary(irCall(mutableListOf).apply {
                typeArguments[0] = pluginContext.irBuiltIns.anyType
            })

            // Iterate over all properties of the class
            for (prop in declaration.properties) {
                if (prop.hasAnnotation(ignoreAnnotation)) continue

                val propType = prop.getter!!.returnType
                if (prop.backingField == null)
                    continue
                val fieldValue =
                    irGetField(irGet(getVisitableChildrenFun.dispatchReceiverParameter!!), prop.backingField!!)


                +irIfThen(
                    type = context.irBuiltIns.unitType,
                    condition = irNotEquals(irGet(irTemporary(fieldValue)), irNull()),
                    thenPart = irBlock {
                        // if (prop is KosuChild) { children.add(prop) }
                        if (propType.isSubtypeOfClass(kosuChildClass!!)) {
                            +irCall(listAdd).apply {
                                dispatchReceiver = irGet(childrenList)
                                putValueArgument(0, fieldValue)
                            }
                        }
                        // if (prop is List<*>) { children.addAll(prop) }
                        else {
                            if (propType.isSubtypeOfClass(listClass!!)) {
                                val typeArg = (propType as? IrSimpleType)?.arguments?.singleOrNull() as? IrType
                                // messageCollector.report(CompilerMessageSeverity.INFO, typeArg?.classFqName?.asString() ?: "null")

                                if (typeArg != null && typeArg.isSubtypeOfClass(kosuChildClass))
                                    +irCall(listAddAll).apply {
                                        dispatchReceiver = irGet(childrenList)
                                        putValueArgument(0, fieldValue)
                                    }
                            }
                        }
                    })
            }

            // return children
            +irReturn(irGet(childrenList))
        }
    }

    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (!declaration.hasAnnotation(kosuVisitableAnnotation)) {
            return super.visitClassNew(declaration)
        }

        implementKosuVisitable(declaration)

        return super.visitClassNew(declaration)
    }
}