package samples

import br.nom.penha.bruno.helper.ExcelHelper
import org.apache.poi.ss.usermodel.{Sheet, Workbook}
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import java.io.{File, FileInputStream}

@RunWith(classOf[JUnitRunner])
class ExcelHelperTest extends FlatSpec with Matchers {

  "ExcelHelper" should "read Excel file correctly" in {
    /*
firstName	lastName	phoneNumber	datee	    formula
Name1	    Surname1	355696564113	4/11/2021	‡
Name2	    Surname2	5646513512	    4/12/2021	FALSO
Name3	    Surname3	355696564113	4/11/2021	717039641738
     */
    val fis = new FileInputStream(new File("src/test/resources/tests/test_file.xlsx"))
    val workbook: Workbook = new XSSFWorkbook(fis)
    val sheet: Sheet = workbook.getSheetAt(0)

    val excelHelper = new ExcelHelper()
    val javaOutput = excelHelper.readExcel(sheet)
    val scalaOutput: Map[Int, List[String]] = javaOutput.map { case (k, v) => (k.toInt, v.toList) }.toMap

    scalaOutput.size should be (4)

    scalaOutput(0) should contain ("firstName")
    scalaOutput(0) should contain ("lastName")

    scalaOutput(1) should contain ("Name1")
    scalaOutput(1) should contain ("Surname1")

    workbook.close()
    fis.close()
  }
}
