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

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.EntityDao
import org.beangle.data.transfer.exporter.DefaultPropertyExtractor
import org.openurp.base.edu.model.Student
import org.openurp.edu.grade.course.model.CourseGrade
import org.openurp.std.info.model.Graduation

/**
 * 转换属性如下：
 * <table  class="striped" style="text-align:left">
 * <thead><tr><td>属性</td><td>标题</td><td>备注</td></tr></thead>
 * <tbody>
 * <tr><td>1. 学号</td><td>std.user.code</td><td></td></tr>
 * <tr><td>2. 姓名</td><td>std.user.name</td><td></td></tr>
 * <tr><td>3. 身份证号</td><td>std.person.code</td><td></td></tr>
 * <tr><td>4. 专业名称</td><td>std.state.major.name</td><td></td></tr>
 * <tr><td>5. 课程名称</td><td>course.name</td><td></td></tr>
 * <tr><td>6. 教育层次代码</td><td>std.level.code</td><td>20 本科 21 专科 22 专升本 23 高起本</td></tr>
 * <tr><td>7. 教育类别代码</td><td>std.project.category.code</td><td>30 成人 31 普高 32 网络 33 中职 34 自考</td></tr>
 * <tr><td>8. 学分</td><td>course.credits</td><td></td></tr>
 * <tr><td>9. 学时</td><td>course.creditHours</td><td></td></tr>
 * <tr><td>10. 成绩</td><td>scoreText</td><td></td></tr>
 * <tr><td>11. 获得时间</td><td>semester.beginOn</td><td></td></tr>
 * <tr><td>12. 届别</td><td>graduation.year</td><td></td></tr>
 * <tr><td>13. 毕业结业肄业</td><td>graduation.educationResult.code</td><td>40 毕业,41 结业</td></tr>
 * <tr><td>14. 毕业季</td><td>graduation.season</td><td>50 春,51 秋</td></tr>
 * <tr><td>15. 备注</td><td>remark</td><td></td></tr>
 * </tbody>
 * </table>
 *
 * @param entityDao
 */
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
            case "graduation.year" => g.graduateOn.getYear
            case "graduation.educationResult.code" => g.educationResult.code
            case "graduation.season" => if (g.graduateOn.getMonth.getValue <= 6) "50" else "51"
          }
      }
    } else if (property == "remark") {
      ""
    } else if (property == "course.credits") {
      FloatTrunc.trunc(grade.course.credits)
    } else {
      super.getPropertyValue(target, property)
    }
  }
}
