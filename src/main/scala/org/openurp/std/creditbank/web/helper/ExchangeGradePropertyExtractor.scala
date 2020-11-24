/*
 * OpenURP, Agile University Resource Planning Solution.
 *
 * Copyright © 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful.
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
