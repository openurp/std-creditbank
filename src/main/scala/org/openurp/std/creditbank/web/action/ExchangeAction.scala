package org.openurp.std.creditbank.web.action

import java.time.ZoneId

import org.beangle.data.dao.OqlBuilder
import org.beangle.data.transfer.exporter.ExportSetting
import org.beangle.webmvc.api.view.View
import org.beangle.webmvc.entity.action.{EntityAction, ExportSupport}
import org.openurp.edu.extern.model.ExchangeGrade
import org.openurp.edu.web.ProjectSupport
import org.openurp.std.creditbank.web.helper.{ExchangeGradeData, ExchangeGradePropertyExtractor}

class ExchangeAction extends EntityAction[ExchangeGrade] with ExportSupport[ExchangeGrade] with ProjectSupport {

  def index: View = {
    put("departments", getDeparts)
    put("project", getProject)
    forward()
  }

  def search: View = {
    val builder = getQueryBuilder
    builder.limit(getPageLimit)
    put("exchangeGrades", entityDao.search(builder))
    forward()
  }

  override protected def getQueryBuilder: OqlBuilder[ExchangeGrade] = {
    val builder = super.getQueryBuilder
    getDate("fromAt") foreach { fromAt =>
      builder.where("exchangeGrade.updatedAt >= :fromAt", fromAt.atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    getDate("toAt") foreach { toAt =>
      builder.where(" exchangeGrade.updatedAt <= :toAt", toAt.plusDays(1).atTime(0, 0, 0).atZone(ZoneId.systemDefault()).toInstant)
    }
    builder.where("exists (from exchangeGrade.courses ec)")
    builder
  }

  override def configExport(setting: ExportSetting): Unit = {
    setting.context.extractor = new ExchangeGradePropertyExtractor()
    val data = entityDao.search(getQueryBuilder.limit(null))
    val rs = data.flatMap { g =>
      g.courses map (c => new ExchangeGradeData(g, c))
    }
    setting.context.put("items", rs)
  }
}
