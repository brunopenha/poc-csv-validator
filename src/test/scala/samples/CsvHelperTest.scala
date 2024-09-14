package samples

import br.nom.penha.bruno.CsvFileGenerator
import br.nom.penha.bruno.helper.CsvHelper.RichString
import org.apache.commons.csv.CSVFormat
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

import java.io.{BufferedReader, FileReader}
import java.nio.file.{Files, Paths}
import scala.collection.JavaConverters._

@RunWith(classOf[JUnitRunner])
class CsvHelperTest extends FlatSpec with Matchers with BeforeAndAfterEach {

  val csvGenerator = new CsvFileGenerator()
  val testFileNameWithHeader = "src/test/resources/tests/generated_test_file_with_header.csv"
  val testFileNameWithoutHeader = "src/test/resources/tests/generated_test_file_without_header.csv"

  "CSV File" should "have correct object types and formats for each line ignoring headers" in {

    val in = new FileReader("src/test/resources/tests/test_file.csv")
    val records = CSVFormat.RFC4180.withDelimiter(',').withFirstRecordAsHeader.parse(in).iterator().asScala

    val dateFormat = "dd-MM-yyyy"

    for (record <- records) {
      // Column 1 should be a string
      record.get(0) shouldBe a[String]
      // Column 2 should be an integer
      record.get(1).toIntOption should not be empty
      // Column 3 should be a double
      record.get(2).toDoubleOption should not be empty
      // Column 4 should be a date with format dd-MM-yyyy
      record.get(3).toDateOption(dateFormat) should not be empty
    }

    in.close()
  }

  "CSV File Without Header" should "have correct object types and formats for each line ignoring headers" in {
    /*
a,0,0.0,01-11-2024
abcde,1,1.01,01-11-2024
a-b-c,999,999.0,01-11-2024
     */
    val in = new FileReader("src/test/resources/tests/test_file_without_header.csv")
    val records = CSVFormat.RFC4180.withSkipHeaderRecord.parse(in).iterator().asScala

    val dateFormat = "dd-MM-yyyy"

    for (record <- records) {
      // Column 1 should be a string
      record.get(0) shouldBe a[String]
      // Column 2 should be an integer
      record.get(1).toIntOption should not be empty
      // Column 3 should be a double
      record.get(2).toDoubleOption should not be empty
      // Column 4 should be a date with format dd-MM-yyyy
      record.get(3).toDateOption(dateFormat) should not be empty
    }

    in.close()
  }

  it should "generate and test CSV file with random values with header" in {
    // Generate random data and write to CSV with header
    val testData = csvGenerator.generateRandomData(10, includeHeader = true)
    csvGenerator.writeCsvFile(testFileNameWithHeader, testData)

    var reader: BufferedReader = null
    try {
      // Test the generated CSV file with header
      reader = new BufferedReader(new FileReader(testFileNameWithHeader))
      val lines = reader.lines().iterator().asScala.toSeq

      // Skip header line
      val records = lines.tail

      val dateFormat = "dd-MM-yyyy"

      for (line <- records) {
        val values = line.split(",")
        // Column 1 should be a string
        values(0) shouldBe a[String]
        // Column 2 should be an integer
        values(1).toIntOption should not be empty
        // Column 3 should be a double
        values(2).toDoubleOption should not be empty
        // Column 4 should be a date with format dd-MM-yyyy
        values(3).toDateOption(dateFormat) should not be empty
      }
    } finally {
      if (reader != null) reader.close()
    }
  }

  it should "generate and test CSV file with random values without header" in {
    // Generate random data and write to CSV without header
    val testData = csvGenerator.generateRandomData(10, includeHeader = false)
    csvGenerator.writeCsvFile(testFileNameWithoutHeader, testData)

    var reader: BufferedReader = null
    try {
      // Test the generated CSV file without header
      reader = new BufferedReader(new FileReader(testFileNameWithoutHeader))
      val records = reader.lines().iterator().asScala.toSeq

      val dateFormat = "dd-MM-yyyy"

      for (line <- records) {
        val values = line.split(",")
        // Column 1 should be a string
        values(0) shouldBe a[String]
        // Column 2 should be an integer
        values(1).toIntOption should not be empty
        // Column 3 should be a double
        values(2).toDoubleOption should not be empty
        // Column 4 should be a date with format dd-MM-yyyy
        values(3).toDateOption(dateFormat) should not be empty
      }
    } finally {
      if (reader != null) reader.close()
    }
  }

  override def beforeEach(): Unit = {
    // Delete test files if they exist before each test
    Files.deleteIfExists(Paths.get(testFileNameWithHeader))
    Files.deleteIfExists(Paths.get(testFileNameWithoutHeader))
  }

  override def afterEach(): Unit = {
    // Clean up test files after each test
    Files.deleteIfExists(Paths.get(testFileNameWithHeader))
    Files.deleteIfExists(Paths.get(testFileNameWithoutHeader))
  }
}
