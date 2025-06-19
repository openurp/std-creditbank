[#ftl]
[@b.head/]
[@b.grid items=certificateGrades var="certificateGrade"]
    [@b.gridbar]
      bar.addItem("导出", action.exportData("grade.std.name:姓名,grade.std.person.code:身份证号,schoolCode:转换学校代码,"+
                         "original.course.code:原课程来源代码,grade.major.name:原专业名称,grade.certificate.name:原课程名称,"+
                         "grade.certificate.institutionName:原办学机构,original.level.code:原教育层次代码,"+
                         "original.project.category.code:原教育类别代码,original.course.defaultCredits:原学分,"+
                         "original.course.creditHours:原学时,grade.scoreText:原成绩,grade.acquiredIn:获得时间,"+
                         "grade.std.level.code:转换后教育层次代码,course.code:转换后课程代码,course.name:转换后课程名称,"+
                         "course.defaultCredits:转换后学分,grade.convertOn:转换时间,grade.std.state.major.name:转换后专业",
                          "xls", "fileName=学分银行成绩-证书成绩"), "excel.png");
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="std.code" title="学号" width="13%"/]
        [@b.col property="std.name" title="姓名" width="11%"/]
        [@b.col property="certificate.name" title="考试科目" width="25%"/]
        [@b.col property="scoreText" title="成绩" width="7%"]
          <span [#if !(certificateGrade.passed)]style="color:red"[/#if]>
            ${(certificateGrade.scoreText)!}
          </span>
        [/@]
        [@b.col property="std.state.department.name" title="院系"  width="15%"/]
        [@b.col property="certificateNo" title="证书编号"  width="15%"]${(certificateGrade.certificateNo)!"--"}[/@]
        [@b.col property="updatedAt" title="录入时间"  width="12%"]${(certificateGrade.updatedAt?string("yy-MM-dd HH:mm"))!"--"}[/@]
        [@b.col title="免修" sortable="false"  width="40px"][#if certificateGrade.exempts?size>0]${certificateGrade.exempts?size}[/#if][/@]
    [/@]
[/@]
[@b.foot/]
