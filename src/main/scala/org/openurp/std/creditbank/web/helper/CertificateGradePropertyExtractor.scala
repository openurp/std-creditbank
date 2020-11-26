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

/** 证书成绩输出辅助类
 * 转换属性如下：
 * <table  class="striped" style="text-align:left">
 * <thead><tr><td>属性</td><td>标题</td><td>备注</td></tr></thead>
 * <tbody>
 * <tr><td>1. 姓名</td><td>grade.std.user.name</td><td></td></tr>
 * <tr><td>2. 身份证号</td><td>grade.std.person.code</td><td></td></tr>
 * <tr><td>3. 转换学校代码</td><td>schoolCode</td><td>本学校所在的学分银行分配的代码</td></tr>
 * <tr><td>4. 原课程来源代码</td><td>original.course.code</td><td>01 课程类 02 证书类</td></tr>
 * <tr><td>5. 原专业名称</td><td>grade.major.name</td><td>不涉及专业,待补</td></tr>
 * <tr><td>6. 原课程名称</td><td>grade.subject.name</td><td></td></tr>
 * <tr><td>7. 原办学机构</td><td>grade.subject.institutionName</td><td></td></tr>
 * <tr><td>8. 原教育层次代码</td><td>original.level.code</td><td></td></tr>
 * <tr><td>9. 原教育类别代码</td><td>original.project.category.code</td><td>30 成人 31 普高 32 网络 33 中职 34 自考</td></tr>
 * <tr><td>10. 原学分</td><td>original.course.credits</td><td>以0代替</td></tr>
 * <tr><td>11. 原学时</td><td>original.course.creditHours</td><td>以0代替</td></tr>
 * <tr><td>12. 原成绩</td><td>grade.scoreText</td><td></td></tr>
 * <tr><td>13. 获得时间</td><td>grade.acquiredOn</td<td></td></tr>
 * <tr><td>14. 转换后教育层次代码</td><td>grade.std.level.code</td><td>20 本科 21 专科 22 专升本 23 高起本</td></tr>
 * <tr><td>15. 转换后课程代码</td><td>course.code</td><td></td></tr>
 * <tr><td>16. 转换后课程名称</td><td>course.name</td><td></td></tr>
 * <tr><td>17. 转换后学分</td><td>course.credits</td><td></td></tr>
 * <tr><td>18. 转换时间</td><td>grade.convertOn</td><td></td></tr>
 * <tr><td>19. 转换后专业</td><td>grade.std.state.major.name</td><td></td></tr>
 * </tbody>
 * </table>
 */
class CertificateGradePropertyExtractor(schoolCode: String) extends DefaultPropertyExtractor {

  private val yearMonth = DateTimeFormatter.ofPattern("YYYYMM")

  override def getPropertyValue(target: Object, property: String): Any = {
    val data = target.asInstanceOf[CertificateData]
    property match {
      case "original.course.code" => "02"
      case "original.level.code" => "20" //由于获取证书的层次未知，目前暂定20
      case "original.project.category.code" => data.grade.std.project.category.code
      case "original.course.credits" => "0"
      case "original.course.creditHours" => "0"
      case "grade.major.name" => "待补"
      case "schoolCode" => schoolCode
      case "grade.convertOn" => LocalDate.ofInstant(data.grade.updatedAt, ZoneId.systemDefault()).format(yearMonth)
      case "grade.acquiredOn" => data.grade.acquiredOn.format(yearMonth)
      case _ => super.getPropertyValue(target, property)
    }
  }
}
