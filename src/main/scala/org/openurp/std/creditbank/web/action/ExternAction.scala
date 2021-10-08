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
package org.openurp.std.creditbank.web.action

import java.time.ZoneId
import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportSetting
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.{EntityAction, ExportSupport}
import org.openurp.starter.edu.helper.ProjectSupport
import org.openurp.edu.extern.model.ExternGrade
import org.openurp.std.creditbank.web.helper.{ExternGradeData, ExternGradePropertyExtractor}

class ExternAction extends EntityAction[ExternGrade] with ExportSupport[ExternGrade] with ProjectSupport {

  def index: View = {
    put("departments", getDeparts)
    put("project", getProject)
    forward()
  }

  def search: View = {
    val builder = getQueryBuilder
    builder.limit(getPageLimit)
    put("externGrades", entityDao.search(builder))
    forward()
  }

  override protected def getQueryBuilder: OqlBuilder[ExternGrade] = {
    val builder = super.getQueryBuilder
    getDate("fromAt") foreach { fromAt =>
      builder.where("externGrade.updatedAt >= :fromAt", fromAt.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    getDate("toAt") foreach { toAt =>
      builder.where(" externGrade.updatedAt <= :toAt", toAt.plusDays(1).atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    builder.where("exists (from externGrade.courses ec)")
    builder
  }

  override def configExport(setting: ExportSetting): Unit = {
    val project = getProject
    val schoolCode = project.properties.getOrElse("std.creditbank.schooCode", "")
    setting.context.extractor = new ExternGradePropertyExtractor(schoolCode)
    val data = entityDao.search(getQueryBuilder.limit(null))
    val rs = data.flatMap { g =>
      g.courses map (c => new ExternGradeData(g, c))
    }
    setting.context.put("items", rs)
  }
}
