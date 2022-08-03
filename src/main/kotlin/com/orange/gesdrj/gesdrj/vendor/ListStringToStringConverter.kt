package com.orange.gesdrj.gesdrj.vendor

import javax.persistence.AttributeConverter
import javax.persistence.Converter

@Converter
class ListStringToStringConverter : AttributeConverter<List<String>?, String?> {

    override fun convertToDatabaseColumn(attribute: List<String>?): String? = attribute?.joinToString(",")

    override fun convertToEntityAttribute(dbData: String?): List<String>? = dbData?.split(",")
}