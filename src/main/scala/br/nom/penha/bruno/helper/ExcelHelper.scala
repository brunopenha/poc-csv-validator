package br.nom.penha.bruno.helper

import org.apache.poi.ss.usermodel.{CellType, DateUtil, Row, Sheet}

class ExcelHelper {
  def readExcel(sheet: Sheet): Map[Int, List[String]] = {
    (0 to sheet.getLastRowNum).map { rowIndex =>
      rowIndex -> convertRowToCellValues(rowIndex, sheet)
    }.toMap
  }

  private def convertRowToCellValues(rowIndex: Int, sheet: Sheet): List[String] = {
    val row = sheet.getRow(rowIndex)
    if (row == null) {
      // If there are no cells in the row, return an empty list
      List[String]()
    } else {
      (0 until row.getLastCellNum).map { cellIndex =>
        convertCellToStringValue(cellIndex, row)
      }.toList
    }
  }

  private def convertCellToStringValue(cellIndex: Int, row: Row): String = {
    val cell = row.getCell(cellIndex)
    if (cell == null) {
      // If the cell is not created, return an empty string
      ""
    } else {
      cell.getCellType match {
        case CellType.STRING  => cell.getRichStringCellValue.getString
        case CellType.NUMERIC =>
          if (DateUtil.isCellDateFormatted(cell)) {
            cell.getDateCellValue.toString
          } else {
            cell.getNumericCellValue.toString
          }
        case CellType.BOOLEAN => cell.getBooleanCellValue.toString
        case CellType.FORMULA => cell.getCellFormula
        case _                      => " "
      }
    }
  }
}
