package com.primordium.configuration.quarkuscli.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.coreutils.functional.Response.Companion.success
import com.primordium.core.domain.templates.Template
import com.primordium.core.domain.templates.TemplateField

object TemplateUtils {
    private const val UNKNOWN_VALUE = "value-not-set"

    fun toTemplate(content: String): Response<Template> {
        val factory = YAMLFactory()
        val mapper = ObjectMapper(factory)
        val yaml = mapper.readTree(content)
        return success(
            Template(
                name = yaml.get("name")?.textValue() ?: UNKNOWN_VALUE,
                version = yaml.get("version")?.textValue() ?: UNKNOWN_VALUE,
                fields = processArrayNode(yaml.get("fields"))
            )
        )
    }

    private fun processArrayNode(yaml: JsonNode?): List<TemplateField> {
        return if (yaml != null && yaml is ArrayNode) {
            yaml.map { toField(it) }
        } else {
            emptyList()
        }
    }

    private fun toField(yaml: JsonNode): TemplateField {
        return TemplateField(
            name = yaml.get("name")?.textValue() ?: UNKNOWN_VALUE,
            value = yaml.get("value")?.textValue(),
            subFields = processArrayNode(yaml.get("subFields"))
        )
    }
}