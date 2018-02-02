package main.kotlin.com.gmail.arhamjsiddiqui.runebot

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.FileSystems
import java.nio.file.Files
import kotlin.reflect.KClass

/**
 * Called YAMLParse to not conflict with jackson's YAMLParser.
 *
 * @author Arham 4
 */
object YAMLParse {
    private val mapper = let {
        val mapper = ObjectMapper(YAMLFactory())
        mapper.registerModule(KotlinModule())
        mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
        mapper
    }

    /**
     * Takes in a data class (with ::class) and parses it by the fileName provided, returning the appropriate class
     * originally provided with parsed data.
     */
    fun <T: Any> parseDto(fileName: String, dto: KClass<T>): T {
        return Files.newBufferedReader(FileSystems.getDefault().getPath(fileName)).use { mapper.readValue(it, dto.java) }
    }
}
