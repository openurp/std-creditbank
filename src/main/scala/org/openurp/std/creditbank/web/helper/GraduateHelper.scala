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

import org.beangle.data.dao.EntityDao
import org.openurp.base.std.model.{Graduate, Student}

/** 获取学生的毕业信息
 * @param entityDao
 */
class GraduateHelper(entityDao: EntityDao) {

  private var graduate: Graduate = _

  def get(std: Student, property: String): String = {
    if (null == graduate || graduate.std != std) {
      this.graduate = entityDao.findBy(classOf[Graduate], "std", List(std)).headOption.orNull
    }
    if (graduate == null) {
      property match {
        case "graduate.year" => std.graduateOn.getYear.toString
        case "graduate.educationResult.code" => CodeHelper.getInschoolCode
        case "graduate.season" => ""
      }
    } else {
      property match {
        case "graduate.year" => graduate.graduateOn.map(_.getYear.toString).getOrElse("")
        case "graduate.educationResult.code" => graduate.result.code
        case "graduate.season" => CodeHelper.getSeason(graduate.graduateOn)
      }
    }
  }
}
