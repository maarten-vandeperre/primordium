package com.primordium.core.domain.templates

data class TemplateDefinition(
    val name: String,
    val description: String? = null,
    val version: String,
    val fields: List<TemplateDefinitionField> = emptyList()
)

data class TemplateDefinitionField(
    val name: String,
    val description: String? = null,
    val required: Boolean,
    val subFields: List<TemplateDefinitionField> = emptyList()
)