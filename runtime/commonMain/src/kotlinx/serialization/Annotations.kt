/*
 * Copyright 2017-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */

@file:Suppress("NO_EXPLICIT_VISIBILITY_IN_API_MODE_WARNING") // Parameters of annotations should probably be ignored, too

package kotlinx.serialization

import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.*
import kotlinx.serialization.modules.*
import kotlin.reflect.*

/**
 * The main entry point to the serialization process.
 * Applying [Serializable] to the Kotlin class instructs the serialization plugin to automatically generate implementation of [KSerializer]
 * for the current class, that can be used to serialize and deserialize the class.
 * The generated serializer can be accessed with `T.serializer()` extension function on the class companion,
 * both are generated by the plugin as well.
 *
 * ```
 * @Serializable
 * class MyData(val myData: AnotherData, val intProperty: Int, ...)
 *
 * // Produces JSON string using the generated serializer
 * val jsonString = Json.encodeToJson(MyData.serializer(), instance)
 * ```
 *
 * Additionally, the user-defined serializer can be specified using [with] parameter:
 * ```
 * @Serializable(with = MyAnotherDataCustomSerializer::class)
 * class MyAnotherData(...)
 *
 * MyAnotherData.serializer() // <- returns MyAnotherDataCustomSerializer
 * ```
 *
 * For annotated properties, specifying [with] parameter is mandatory and can be used to override
 * serializer on the use-site without affecting the rest of the usages:
 * ```
 * @Serializable // By default is serialized as 3 byte components
 * class RgbPixel(val red: Short, val green: Short, val blue: Short)
 *
 * @Serializable
 * class RgbExample(
 *     @Serializable(with = RgbAsHexString::class) p1: RgpPixel, // Serialize as HEX string, e.g. #FFFF00
 *     @Serializable(with = RgbAsSingleInt::class) p2: RgpPixel, // Serialize as single integer, e.g. 16711680
 *     p3: RgpPixel // Serialize as 3 short components, e.g. { "red": 255, "green": 255, "blue": 0 }
 * )
 * ```
 * In this example, each pixel will be serialized using different data representation.
 *
 * For classes with generic type parameters, `serializer()` function requires one additional argument per each generic type parameter:
 * ```
 * @Serializable
 * class Box<T>(value: T)
 *
 * Box.serializer() // Doesn't compile
 * Box.serializer(Int.serializer()) // Returns serializer for Box<Int>
 * Box.serializer(Box.serializer(Int.serializer())) // Returns serializer for Box<Box<Int>>
 * ```
 *
 * ### Implementation details
 *
 * In order to generate `serializer` function that is not a method on the particular instance, the class should have a companion object, either named or unnamed.
 * Companion object is generated by the plugin if it is not declared, effectively exposing both companion and `serializer()` method to class ABI.
 * If companion object already exists, only `serializer` method will be generated.
 *
 * @see UseSerializers
 * @see Serializer
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.TYPE)
//@Retention(AnnotationRetention.RUNTIME) // Runtime is the default retention, also see KT-41082
public annotation class Serializable(
    val with: KClass<out KSerializer<*>> = KSerializer::class // Default value indicates that auto-generated serializer is used
)

/**
 * Instructs the serialization plugin to turn this class into serializer for specified class [forClass].
 * However, it would not be used automatically. To apply it on particular class or property,
 * use [Serializable] or [UseSerializers], or [Contextual] with runtime registration.
 *
 * `@Serializer(forClass)` is experimental and unstable feature that can be changed in future releases.
 * Changes may include additional constraints on classes and objects marked with this annotation,
 * behavioural changes and even serialized shape of the class.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
@ExperimentalSerializationApi
public annotation class Serializer(
    val forClass: KClass<*> //  target class to create serializer for
)

/**
 * Overrides the name of a class or a property in the corresponding [SerialDescriptor].
 * Names and serial names are used by text-based serial formats in order to encode the name of the class or
 * the name of the property, e.g. by [Json].
 *
 * By default, [SerialDescriptor.serialName] and [SerialDescriptor.getElementName]
 * are associated with fully-qualified name of the target class and the name of the property respectively.
 * Applying this annotation changes the visible name to the given [value]:
 *
 * ```
 * package foo
 *
 * @Serializable // RegularName.serializer().descriptor.serialName is "foo.RegularName"
 * class RegularName(val myInt: Int)
 *
 * @Serializable
 * @SerialName("CustomName") // Omit package from name that is used for diagnostic and polymorphism
 * class CustomName(@SerialName("int") val myInt: Int)
 *
 * // Prints "{"myInt":42}"
 * println(Json.encodeToString(RegularName(42)))
 * // Prints "{"int":42}"
 * println(Json.encodeToString(CustomName(42)))
 * ```
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
public annotation class SerialName(val value: String)

/**
 * Indicates that property must be present during deserialization process, despite having a default value.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
public annotation class Required

/**
 * Marks this property invisible for the whole serialization process, including [serial descriptors][SerialDescriptor].
 * Transient properties should have default values.
 */
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.BINARY)
public annotation class Transient

/**
 * Meta-annotation that commands the compiler plugin to handle the annotation as serialization-specific.
 * Serialization-specific annotations are preserved in the [SerialDescriptor] and can be retrieved
 * during serialization process with [SerialDescriptor.getElementAnnotations].
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.BINARY)
@ExperimentalSerializationApi
public annotation class SerialInfo

/**
 * Commands to use [ContextualSerializer] on an annotated property or type usage.
 * If used on a file, commands using [ContextualSerializer] for all listed KClasses.
 *
 * @param [forClasses] Classes to use ContextualSerializer for in the current file.
 */
@Deprecated(
    "This annotation had several meanings and was split in two for readability: use @Contextual on properties and @UseContextualSerialization on files.",
    ReplaceWith("Contextual"),
    level = DeprecationLevel.ERROR
)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FILE, AnnotationTarget.TYPE)
@Retention(AnnotationRetention.BINARY)
public annotation class ContextualSerialization(vararg val forClasses: KClass<*>)

/**
 * Instructs the plugin to use [ContextSerializer] on a given property or type.
 * Context serializer is usually used when serializer for type can only be found in runtime.
 * It is also possible to apply [ContextSerializer] to every property of the given type,
 * using file-level [UseContextualSerialization] annotation.
 *
 * @see ContextSerializer
 * @see UseContextualSerialization
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE)
public annotation class Contextual

/**
 * Instructs the plugin to use [ContextSerializer] for every type in the current file that is listed in the [forClasses].
 *
 * @see Contextual
 * @see ContextSerializer
 */
@Target(AnnotationTarget.FILE)
public annotation class UseContextualSerialization(vararg val forClasses: KClass<*>)

/**
 *  Adds [serializerClasses] to serializers resolving process inside the plugin.
 *  Each of [serializerClasses] must implement [KSerializer].
 *
 *  Inside the file with this annotation, for each given property
 *  of type `T` in some serializable class, this list would be inspected for the presence of `KSerializer<T>`.
 *  If such serializer is present, it would be used instead of default.
 *
 *  Main use-case for this annotation is not to write @Serializable(with=SomeSerializer::class)
 *  on each property with custom serializer.
 *
 *  Serializers from this list have higher priority than default, but lesser priority than
 *  serializers defined on the property itself, such as [Serializable] (with=...) or [Contextual].
 */
@Target(AnnotationTarget.FILE)
@Retention(AnnotationRetention.BINARY)
public annotation class UseSerializers(vararg val serializerClasses: KClass<out KSerializer<*>>)

/**
 * Instructs the serialization plugin to use [PolymorphicSerializer] on an annotated property or type usage.
 * When used on class, replaces its serializer with [PolymorphicSerializer] everywhere.
 *
 * This annotation is applied automatically to interfaces and serializable abstract classes
 * and can be applied to open classes in addition to [Serializable] for the sake of simplicity.
 *
 * Does not affect sealed classes, because they are gonna be serialized with subclasses automatically
 * with special compiler plugin support which would be added later.
 */
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.TYPE, AnnotationTarget.CLASS)
//@Retention(AnnotationRetention.RUNTIME) // Runtime is the default retention, also see KT-41082
public annotation class Polymorphic

/**
 * Marks declarations that are still **experimental** in kotlinx.serialization, which means that the design of the
 * corresponding declarations has open issues which may (or may not) lead to their changes in the future.
 * Roughly speaking, there is a chance that those declarations will be deprecated in the near future or
 * the semantics of their behavior may change in some way that may break some code.
 *
 * By default, the following categories of API are experimental:
 *
 * * Writing 3rd-party serialization formats
 * * Writing non-trivial custom serializers
 * * Implementing [SerialDescriptor] interfaces
 * * Not-yet-stable serialization formats that require additional polishing
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS)
@RequiresOptIn(level = RequiresOptIn.Level.WARNING)
public annotation class ExperimentalSerializationApi

/**
 * Public API marked with this annotation is effectively **internal**, which means
 * it should not be used outside of `kotlinx.serialization`.
 * Signature, semantics, source and binary compatibilities are not guaranteed for this API
 * and will be changed without any warnings or migration aids.
 * If you cannot avoid using internal API to solve your problem, please report your use-case to serialization's issue tracker.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION, AnnotationTarget.TYPEALIAS)
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
public annotation class InternalSerializationApi
