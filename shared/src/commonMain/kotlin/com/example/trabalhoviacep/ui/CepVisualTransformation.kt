package com.example.trabalhoviacep.ui

import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.AnnotatedString


class CepVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter { it.isDigit() }.take(8)

        val formatted = if (digits.length > 5) {
            "${digits.substring(0, 5)}-${digits.substring(5)}"
        } else {
            digits
        }

        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = object : OffsetMapping {

                override fun originalToTransformed(offset: Int): Int {
                    return when {
                        offset <= 5 -> offset
                        else -> offset + 1
                    }
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return when {
                        offset <= 5 -> offset
                        else -> offset - 1
                    }
                }
            }
        )
    }
}