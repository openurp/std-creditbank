package org.openurp.std.creditbank.web.helper

import java.time.format.DateTimeFormatter
import java.time.{LocalDate, ZoneId}

import org.beangle.data.transfer.exporter.DefaultPropertyExtractor

class ExchangeGradePropertyExtractor extends DefaultPropertyExtractor {

  private val yearMonth = DateTimeFormatter.ofPattern("YYYYMM")

  override def getPropertyValue(target: Object, property: String): Any = {
    val data = target.asInstanceOf[ExchangeGradeData]
    property match {
      case "grade.courseCode" => "03"
      case "grade.creditHours" => ""
      case "grade.convertOn" => LocalDate.ofInstant(data.grade.updatedAt, ZoneId.systemDefault()).format(yearMonth)
      case "grade.acquiredOn" => data.grade.acquiredOn.format(yearMonth)
      case _ => super.getPropertyValue(target, property)
    }
  }
}
