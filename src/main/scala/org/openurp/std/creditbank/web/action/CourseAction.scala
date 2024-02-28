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
import org.beangle.data.transfer.exporter.ExportContext
import org.beangle.web.action.support.ActionSupport
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.{EntityAction, ExportSupport}
import org.openurp.base.std.model.Graduate
import org.openurp.code.edu.model.CourseTakeType
import org.openurp.edu.grade.model.CourseGrade
import org.openurp.std.creditbank.web.helper.CourseGradePropertyExtractor
import org.openurp.std.graduation.model.GraduateBatch

/**
 * 校内课程成绩学分银行查看和导出
 *
 * @author zhouqi 2019年10月11日
 * @author duantihua 2020年11月24日
 */
class CourseAction extends ActionSupport, EntityAction[CourseGrade], ExportSupport[CourseGrade] {

  var entityDao: EntityDao = _

  def index(): View = {
    val squery = OqlBuilder.from(classOf[GraduateBatch], "s").orderBy("s.graduateOn desc")
    put("batches", entityDao.search(squery))
    forward()
  }

  def search(): View = {
    val builder = getQueryBuilder
    builder.limit(getPageLimit)
    put("grades", entityDao.search(builder))
    forward()
  }

  protected override def getQueryBuilder: OqlBuilder[CourseGrade] = {
    val builder = OqlBuilder.from(classOf[CourseGrade], "grade")
    populateConditions(builder)
    val batchId = getLongId("batch")
    val batch = entityDao.get(classOf[GraduateBatch], batchId)
    put("graduateBatch", batch)
    val hql1 = new StringBuilder
    hql1.append("exists (")
    hql1.append("  from ").append(classOf[Graduate].getName).append(" g")
    hql1.append(" where g.std = grade.std and g.result is not null")
    hql1.append("   and g.graduateOn = :graduateOn")
    hql1.append(")")
    builder.where(hql1.toString, batch.graduateOn)
    builder.where("grade.courseTakeType.id != :courseTakeTypeId", CourseTakeType.Exemption)
    val hql2 = new StringBuilder
    hql2.append("not exists (")
    hql2.append("  from ").append(classOf[CourseGrade].getName).append(" grade2")
    hql2.append(" where grade.std = grade2.std")
    hql2.append("   and grade.course = grade2.course")
    hql2.append("   and grade.id != grade2.id")
    hql2.append("   and (")
    hql2.append("         coalesce(grade2.score, 0) = coalesce(grade.score, 0) and grade2.id > grade.id")
    hql2.append("         or")
    hql2.append("         coalesce(grade2.score, 0) > coalesce(grade.score, 0)")
    hql2.append("       )")
    hql2.append(")")
    builder.where(hql2.toString)

    builder.where("grade.passed=true")
    val orderBy = get("orderBy") match {
      case Some(o) => o + ",grade.id"
      case None => "grade.id"
    }
    builder.orderBy(orderBy)
    builder
  }

  override def configExport(context: ExportContext): Unit = {
    context.extractor = new CourseGradePropertyExtractor(entityDao)
    context.put("items", entityDao.search(getQueryBuilder.limit(null)))
  }
}
