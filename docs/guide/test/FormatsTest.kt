// This file was automatically generated from formats.md by Knit tool. Do not edit.
package example.test

import org.junit.Test
import kotlinx.knit.test.*

class FormatsTest {
    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{BF}dnameukotlinx.serializationhlanguagefKotlin{FF}",
            "Project(name=kotlinx.serialization, language=Kotlin)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "Project(name=kotlinx.serialization)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{BF}etype2D{01}{02}{03}{04}etype4{9F}{05}{06}{07}{08}{FF}{FF}",
            "Data(type2=[1, 2, 3, 4], type4=[5, 6, 7, 8])"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{0A}{15}kotlinx.serialization{12}{06}Kotlin",
            "Project(name=kotlinx.serialization, language=Kotlin)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{0A}{15}kotlinx.serialization{1A}{06}Kotlin",
            "Project(name=kotlinx.serialization, language=Kotlin)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{08}{01}{10}{03}{1D}{03}{00}{00}{00}"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{08}{01}{08}{02}{08}{03}",
            "Data(a=[1, 2, 3], b=[])"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "0a03546f6d1203313233",
            "0a054a657272791a03373839",
            "Data(name=Tom, phone=HomePhone(number=123))",
            "Data(name=Jerry, phone=WorkPhone(number=789))"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "syntax = \"proto2\";",
            "",
            "",
            "// serial name 'example.exampleFormats09.SampleData'",
            "message SampleData {",
            "  required int64 amount = 1;",
            "  optional string description = 2;",
            "  // WARNING: a default value decoded when value is missing",
            "  optional string department = 3;",
            "}",
            ""
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "name = kotlinx.serialization",
            "owner.name = kotlin"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "[kotlinx.serialization, kotlin, 9000]"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "[kotlinx.serialization, kotlin, 9000]",
            "Project(name=kotlinx.serialization, owner=User(name=kotlin), votes=9000)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "[kotlinx.serialization, kotlin, 9000]",
            "Project(name=kotlinx.serialization, owner=User(name=kotlin), votes=9000)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "[kotlinx.serialization, 2, kotlin, jetbrains, 9000]",
            "Project(name=kotlinx.serialization, owners=[User(name=kotlin), User(name=jetbrains)], votes=9000)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "[kotlinx.serialization, !!, kotlin, NULL]",
            "Project(name=kotlinx.serialization, owner=User(name=kotlin), votes=null)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{00}{15}kotlinx.serialization{00}{06}Kotlin",
            "Project(name=kotlinx.serialization, language=Kotlin)"
        )
    }

    @Test
    fun testExampleFormats01() {
        captureOutput("ExampleFormats01") { example.exampleFormats01.main() }.verifyOutputLines(
            "{00}{15}kotlinx.serialization{04}{0A}{0B}{0C}{0D}",
            "Project(name=kotlinx.serialization, attachment=[10, 11, 12, 13])"
        )
    }
}
