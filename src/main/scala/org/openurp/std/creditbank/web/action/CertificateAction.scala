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
import org.openurp.edu.extern.code.model.{CertificateCategory, CertificateSubject}
import org.openurp.edu.extern.model.CertificateGrade
import org.openurp.edu.web.ProjectSupport
import org.openurp.std.creditbank.web.helper.{CertificateData, CertificateGradePropertyExtractor}

class CertificateAction extends EntityAction[CertificateGrade] with ExportSupport[CertificateGrade] with ProjectSupport {

  def index: View = {
    put("certificateSubjects", getCodes(classOf[CertificateSubject]))
    put("certificateCategories", getCodes(classOf[CertificateCategory]))
    put("departments", getDeparts)
    put("project", getProject)
    forward()
  }

  def search: View = {
    val builder = getQueryBuilder
    builder.limit(getPageLimit)
    put("certificateGrades", entityDao.search(builder))
    forward()
  }

  override protected def getQueryBuilder: OqlBuilder[CertificateGrade] = {
    val builder = super.getQueryBuilder
    getFloat("from") foreach { from =>
      builder.where("certificateGrade.score >=:F", from)
    }
    getFloat("to") foreach { to =>
      builder.where("certificateGrade.score <=:T", to)
    }

    getDate("fromAt") foreach { fromAt =>
      builder.where("certificateGrade.updatedAt >= :fromAt", fromAt.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    getDate("toAt") foreach { toAt =>
      builder.where(" certificateGrade.updatedAt <= :toAt", toAt.plusDays(1).atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    builder.where("exists (from certificateGrade.courses ec)")
    builder
  }

  override def configExport(setting: ExportSetting): Unit = {
    val project = getProject
    val schoolCode = project.properties.getOrElse("std.creditbank.schooCode", "")
    setting.context.extractor = new CertificateGradePropertyExtractor(schoolCode)
    val data = entityDao.search(getQueryBuilder.limit(null))
    val rs = data.flatMap { g =>
      g.courses map (c => new CertificateData(g, c))
    }
    setting.context.put("items", rs)
  }
}
