package br.nom.penha.bruno.helper

import java.time.LocalDate
import java.time.format.{DateTimeFormatter, DateTimeParseException}

object CsvHelper {
  implicit class RichString(val s: String) extends AnyVal {
    def toIntOption: Option[Int] =
      try {
        Some(s.toInt)
      } catch {
        case _: NumberFormatException => None
      }

    def toDoubleOption: Option[Double] =
      try {
        Some(s.toDouble)
      } catch {
        case _: NumberFormatException => None
      }

    def toDateOption(format: String): Option[LocalDate] =
      try {
        val formatter = DateTimeFormatter.ofPattern(format)
        Some(LocalDate.parse(s, formatter))
      } catch {
        case _: DateTimeParseException => None
      }
  }
}