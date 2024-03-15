package com.primordium.core.domain.templates

data class Template (
    val name: String,
    val version: String,
    val fields: List<TemplateDefinitionField> = emptyList()
)

data class TemplateField(
    val name: String,
    val value: String,
    val subFields: List<TemplateDefinitionField> = emptyList()
)