package org.openurp.std.creditbank.web.helper

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.EntityDao
import org.beangle.data.transfer.exporter.DefaultPropertyExtractor
import org.openurp.edu.base.model.Student
import org.openurp.edu.grade.course.model.CourseGrade
import org.openurp.std.info.model.Graduation

class CourseGradePropertyExtractor(entityDao: EntityDao) extends DefaultPropertyExtractor {

  private val graduateData = Collections.newMap[Student, Graduation]
  private val yearMonth = DateTimeFormatter.ofPattern("YYYYMM")

  override def getPropertyValue(target: Object, property: String): Any = {
    val grade = target.asInstanceOf[CourseGrade]
    if ("semester.beginOn".equals(property)) {
      grade.semester.endOn.format(yearMonth)
    } else if (property.startsWith("graduation.")) {
      val graduation = graduateData.get(grade.std) match {
        case g@Some(_) => g
        case None =>
          val rs = entityDao.findBy(classOf[Graduation], "std", List(grade.std)).headOption
          rs foreach { r => graduateData.put(grade.std, r) }
          rs
      }
      graduation match {
        case None => ""
        case Some(g) =>
          property match {
            case "graduation.graduateOn" => g.graduateOn.getYear
            case "graduation.educationResult.code" => g.educationResult.code
          }
      }
    } else {
      super.getPropertyValue(target, property)
    }
  }
}
