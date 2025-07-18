[#ftl]
[@b.head/]
  [@b.grid items=externGrades var="externGrade"]
    [@b.gridbar]
       bar.addItem("导出", action.exportData("grade.externStudent.std.name:姓名,grade.externStudent.std.person.code:身份证号,schoolCode:转换学校代码,original.course.code:原课程来源代码,grade.externStudent.majorName:原专业名称,grade.courseName:原课程名称,grade.externStudent.school.name:原办学机构,grade.externStudent.level.code:原教育层次代码,grade.externStudent.category.code:原教育类别代码,grade.credits:原学分,grade.creditHours:原学时,grade.scoreText:原成绩,grade.acquiredIn:获得年月,grade.externStudent.std.level.code:转换后教育层次代码,course.code:转换后课程代码,course.name:转换后课程名称,course.defaultCredits:转换后学分,grade.convertOn:转换时间,grade.externStudent.std.state.major.name:转换后专业名称", "xls", "fileName=学分银行成绩-外校成绩"), "excel.png");
    [/@]
    [@b.row]
      [@b.col title="序号" width="4%"]${externGrade_index+1}[/@]
      [@b.col title="姓名" property="externStudent.std.name" width="11%"/]
      [@b.col title="身份证号" property="externStudent.std.person.code"  width="14%"]<span style="font-size:0.7em">${externGrade.externStudent.std.person.code}</span>[/@]
      [@b.col title="原办学机构" property="externStudent.school.name"  width="16%"]
         <span title="${externGrade.externStudent.school.code}" style="font-size:0.7em">${externGrade.externStudent.school.name}</span>
      [/@]
      [@b.col title="原专业" property="externStudent.majorName" width="16%"/]
      [@b.col title="原成绩" property="scoreText" width="6%"/]
      [@b.col title="原学分" property="credits" width="6%"/]
      [@b.col title="获得年月" property="acquiredIn" width="8%"]${externGrade.acquiredIn?string("yyyyMM")}[/@]
      [@b.col title="转换后课程" sortable="false" width="21%"]
        [#if externGrade.exempts?size >0 ]
        <span style="font-size:0.8em">[#list externGrade.exempts as c]${c.name} ${c.defaultCredits}分 [#if c_has_next]<br>[/#if][/#list]</span>
        [#else]--[/#if]
      [/@]
      [@b.col title="转换时间" property="updatedAt" width="8%"]${externGrade.updatedAt?string("yyyyMM")}[/@]
    [/@]
  [/@]
[@b.foot/]
