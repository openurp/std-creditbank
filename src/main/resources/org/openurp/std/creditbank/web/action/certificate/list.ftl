[#ftl]
[@b.head/]
[@b.grid items=certificateGrades var="certificateGrade"]
    [@b.gridbar]
      bar.addItem("导出", action.exportData("grade.std.user.name:姓名,grade.std.person.code:身份证号,schoolCode:转换学校代码,original.course.code:原课程来源代码,grade.major.name:原专业名称,grade.subject.name:原课程名称,grade.subject.institutionName:原办学机构,original.level.code:原教育层次代码,original.project.category.code:原教育类别代码,original.course.defaultCredits:原学分,original.course.creditHours:原学时,grade.scoreText:原成绩,grade.acquiredOn:获得时间,grade.std.level.code:转换后教育层次代码,course.code:转换后课程代码,course.name:转换后课程名称,course.credits:转换后学分,grade.convertOn:转换时间,grade.std.state.major.name:转换后专业", "xls", "fileName=学分银行成绩-证书成绩"), "excel.png");
    [/@]
    [@b.row]
        [@b.boxcol/]
        [@b.col property="std.user.code" title="学号" width="13%"/]
        [@b.col property="std.user.name" title="姓名" width="11%"/]
        [@b.col property="subject.name" title="考试科目" width="25%"/]
        [@b.col property="scoreText" title="成绩" width="7%"]
          <span [#if !(certificateGrade.passed)]style="color:red"[/#if]>
            ${(certificateGrade.scoreText)!}
          </span>
        [/@]
        [@b.col property="std.state.department.name" title="院系"  width="15%"/]
        [@b.col property="certificate" title="证书编号"  width="15%"]${(certificateGrade.certificate)!"--"}[/@]
        [@b.col property="updatedAt" title="录入时间"  width="12%"]${(certificateGrade.updatedAt?string("yy-MM-dd HH:mm"))!"--"}[/@]
        [@b.col title="免修" sortable="false"  width="40px"][#if certificateGrade.courses?size>0]${certificateGrade.courses?size}[/#if][/@]
    [/@]
[/@]

[@b.form name="certificateGradeListForm" target="certificateGradeList" action="!index"]
        <input type="hidden" name="configId" id="configId" />
        <input type="hidden" name="params" value="${b.paramstring}" />
[/@]

<script>
    //导出校外考试成绩
    function exportData(){
        var certificateGradeIds = bg.input.getCheckBoxValues("certificateGrade.id");
        var form = action.getForm();
        if (certificateGradeIds) {
            bg.form.addInput(form,"certificateGradeIds",certificateGradeIds);
        }else{
            if(!confirm("是否导出查询条件内的所有数据?")) return;
                if(""!=action.page.paramstr){
                  bg.form.addHiddens(form,action.page.paramstr);
                  bg.form.addParamsInput(form,action.page.paramstr);
                }
            bg.form.addInput(form,"certificateGradeIds","");
        }
        bg.form.addInput(form,"certificateGradeIds",bg.input.getCheckBoxValues("certificateGrade.id"));
        bg.form.addInput(form,"keys","std.user.code,std.name,subject.category.name,subject.name,score,scoreText,std.department.name,std.major.name,std.grade,certificate,acquiredOn,examNo,updatedAt,courseGradeSize");
        bg.form.addInput(form,"titles","学号,姓名,考试类型,考试科目,分数,成绩,院系,专业,年级,证书编号,考试日期,准考证号,录入时间,已认定课数");
        bg.form.addInput(form,"fileName","校外考试成绩数据");
        bg.form.submit(form,"${b.url('!exportData')}","_self");
    }
</script>
[@b.foot/]
