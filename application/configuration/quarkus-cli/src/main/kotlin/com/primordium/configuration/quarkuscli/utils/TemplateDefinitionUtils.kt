package com.primordium.configuration.quarkuscli.utils

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.primordium.core.coreutils.functional.Response
import com.primordium.core.domain.templates.TemplateDefinition
import com.primordium.core.domain.templates.TemplateDefinitionField

object TemplateDefinitionUtils {
    private const val UNKNOWN_VALUE = "value-not-set"

    fun toTemplateDefinition(content: String): Response<TemplateDefinition> {
        val factory = YAMLFactory()
        val mapper = ObjectMapper(factory)
        val yaml = mapper.readTree(content)
        return Response.success(
            TemplateDefinition(
                name = yaml.get("name")?.textValue() ?: UNKNOWN_VALUE,
                description = yaml.get("description")?.textValue(),
                version = yaml.get("version")?.textValue() ?: UNKNOWN_VALUE,
                fields = processArrayNode(yaml.get("fields"))
            )
        )
    }

    private fun processArrayNode(yaml: JsonNode?): List<TemplateDefinitionField> {
        return if (yaml != null && yaml is ArrayNode) {
            yaml.map { toField(it) }
        } else {
            emptyList()
        }
    }

    private fun toField(yaml: JsonNode): TemplateDefinitionField {
        return TemplateDefinitionField(
            name = yaml.get("name")?.textValue() ?: UNKNOWN_VALUE,
            description = yaml.get("description")?.textValue(),
            required = yaml.get("required")?.booleanValue() ?: false,
            subFields = processArrayNode(yaml.get("subFields"))
        )
    }
}