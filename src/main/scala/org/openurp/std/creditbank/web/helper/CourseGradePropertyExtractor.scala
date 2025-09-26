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

package org.openurp.std.creditbank.web.helper

import org.beangle.commons.bean.DefaultPropertyExtractor
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.EntityDao
import org.openurp.base.std.model.{Graduate, Student}
import org.openurp.edu.grade.model.CourseGrade

import java.time.format.DateTimeFormatter

/**
 * 转换属性如下：
 * <table  class="striped" style="text-align:left">
 * <thead><tr><td>属性</td><td>标题</td><td>备注</td></tr></thead>
 * <tbody>
 * <tr><td>1. 姓名</td><td>std.name</td><td></td></tr>
 * <tr><td>2. 身份证号</td><td>std.person.code</td><td></td></tr>
 * <tr><td>3. 专业名称</td><td>std.state.major.name</td><td></td></tr>
 * <tr><td>4. 课程名称</td><td>course.name</td><td></td></tr>
 * <tr><td>5. 教育层次代码</td><td>std.level.code</td><td>85 本科 86 专科 783 专升本 784 高起本</td></tr>
 * <tr><td>6. 教育类别代码</td><td>std.project.category.code</td><td>321 成人 322 普高 1221 网络 1381 中职 1421 自考</td></tr>
 * <tr><td>7. 学分</td><td>course.defaultCredits</td><td></td></tr>
 * <tr><td>8. 学时</td><td>course.creditHours</td><td></td></tr>
 * <tr><td>9. 成绩</td><td>scoreText</td><td></td></tr>
 * <tr><td>10. 获得时间</td><td>semester.beginOn</td><td></td></tr>
 * <tr><td>11. 届别</td><td>graduation.year</td><td></td></tr>
 * <tr><td>12. 毕业结业肄业</td><td>graduation.educationResult.code</td><td>1261 毕业,1262 结业</td></tr>
 * <tr><td>13. 毕业季</td><td>graduation.season</td><td>1321 春,1322 秋</td></tr>
 * <tr><td>14. 备注</td><td>remark</td><td></td></tr>
 * <tr><td>15. 学号</td><td>std.code</td><td></td></tr>
 * </tbody>
 * </table>
 *
 * @param entityDao
 */
class CourseGradePropertyExtractor(entityDao: EntityDao) extends DefaultPropertyExtractor {

  private val graduateData = Collections.newMap[Student, Graduate]
  private val yearMonth = DateTimeFormatter.ofPattern("yyyyMM")

  override def get(target: Object, property: String): Any = {
    val grade = target.asInstanceOf[CourseGrade]
    if ("semester.beginOn".equals(property)) {
      grade.semester.endOn.format(yearMonth)
    } else if (property.startsWith("graduation.")) {
      val graduation = graduateData.get(grade.std) match {
        case g@Some(_) => g
        case None =>
          val rs = entityDao.findBy(classOf[Graduate], "std", List(grade.std)).headOption
          rs foreach { r => graduateData.put(grade.std, r) }
          rs
      }
      graduation match {
        case None => ""
        case Some(g) =>
          property match {
            case "graduation.year" => g.graduateOn.map(_.getYear.toString).getOrElse("")
            case "graduation.educationResult.code" => g.result.code
            case "graduation.season" =>
              if g.graduateOn.isEmpty then ""
              else if (g.graduateOn.get.getMonth.getValue <= 6) "1321" else "1322"
          }
      }
    } else if (property == "remark") {
      ""
    } else if (property == "course.defaultCredits") {
      FloatTrunc.trunc(grade.course.defaultCredits)
    } else {
      super.get(target, property)
    }
  }
}
