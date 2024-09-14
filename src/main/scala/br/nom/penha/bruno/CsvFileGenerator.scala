package br.nom.penha.bruno

import java.io.{BufferedWriter, FileWriter}
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

class CsvFileGenerator {

  private val random = new Random()
  private val dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")

  def generateRandomData(rows: Int, includeHeader: Boolean): Seq[String] = {
    val header = "column1,column2,column3,column4"
    val data = for (_ <- 1 to rows) yield {
      s"${randomString(5)}" +
        s",${random.nextInt(1000)}" +
        s",${random.nextDouble() * 1000}" +
        s",${LocalDate.now().format(dateFormat)}"
    }
    if (includeHeader) header +: data else data
  }

  def randomString(length: Int): String = {
    random.alphanumeric.filter(_.isLetterOrDigit).take(length).mkString
  }

  def writeCsvFile(filename: String, data: Seq[String]): Unit = {
    val out = new BufferedWriter(new FileWriter(filename))

    try {
      data.foreach(line => {
        out.write(line)
        out.newLine()
      })
    } finally {
      out.flush()
      out.close()
    }
  }
}