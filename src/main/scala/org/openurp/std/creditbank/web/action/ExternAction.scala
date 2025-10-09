/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.std.creditbank.web.action

import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.beangle.doc.transfer.exporter.ExportContext
import org.beangle.webmvc.support.ActionSupport
import org.beangle.webmvc.support.action.{EntityAction, ExportSupport}
import org.beangle.webmvc.view.View
import org.openurp.base.model.Project
import org.openurp.edu.extern.model.ExternGrade
import org.openurp.starter.web.support.ProjectSupport
import org.openurp.std.creditbank.web.helper.{ExternGradeData, ExternGradePropertyExtractor}

import java.time.ZoneId

class ExternAction extends ActionSupport, EntityAction[ExternGrade], ExportSupport[ExternGrade], ProjectSupport {

  var entityDao: EntityDao = _

  def index(): View = {
    given project: Project = getProject

    put("departments", getDeparts)
    put("project", project)
    forward()
  }

  def search(): View = {
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
    builder.where("exists (from externGrade.exempts ec)")
    builder
  }

  override def configExport(context: ExportContext): Unit = {
    given project: Project = getProject

    val schoolCode = getConfig("std.creditbank.schooCode", "")
    context.extractor = new ExternGradePropertyExtractor(entityDao, schoolCode)
    val data = entityDao.search(getQueryBuilder.limit(null))
    val rs = data.flatMap { g =>
      g.exempts map (c => new ExternGradeData(g, c))
    }
    context.put("items", rs)
  }
}
