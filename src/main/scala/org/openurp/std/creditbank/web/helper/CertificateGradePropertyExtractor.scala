package org.openurp.std.creditbank.web.helper

import java.time.{LocalDate, ZoneId}
import java.time.format.DateTimeFormatter

import org.beangle.data.transfer.exporter.DefaultPropertyExtractor

class CertificateGradePropertyExtractor extends DefaultPropertyExtractor {

  private val yearMonth = DateTimeFormatter.ofPattern("YYYYMM")

  override def getPropertyValue(target: Object, property: String): Any = {
    val data = target.asInstanceOf[CertificateData]
    property match {
      case "original.course.code" => "02"
      case "original.level.code" => "20"
      case "original.project.category.code" => ""
      case "original.course.credits" => ""
      case "original.course.creditHours" => ""
      case "grade.convertOn"=> LocalDate.ofInstant(data.grade.updatedAt,ZoneId.systemDefault()).format(yearMonth)
      case "grade.acquiredOn" => data.grade.acquiredOn.format(yearMonth)
      case _ => super.getPropertyValue(target, property)
    }
  }
}
