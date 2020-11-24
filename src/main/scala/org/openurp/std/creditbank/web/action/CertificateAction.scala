package org.openurp.std.creditbank.web.action

import java.time.ZoneId

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportSetting
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.{EntityAction, ExportSupport}
import org.openurp.edu.extern.code.model.{CertificateCategory, CertificateSubject}
import org.openurp.edu.extern.model.CertificateGrade
import org.openurp.edu.web.ProjectSupport
import org.openurp.std.creditbank.web.helper.{CertificateData, CertificateGradePropertyExtractor, CourseGradePropertyExtractor}

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
    setting.context.extractor = new CertificateGradePropertyExtractor()
    val data = entityDao.search(getQueryBuilder.limit(null))
    val rs = data.flatMap { g =>
      g.courses map (c => new CertificateData(g, c))
    }
    setting.context.put("items", rs)
  }
}
