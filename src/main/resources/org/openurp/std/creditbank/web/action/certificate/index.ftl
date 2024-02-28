[#ftl]
[@b.head/]
[@b.toolbar title="证书成绩" /]
<div class="search-container">
  <div class="search-panel">
            [@b.form name="certificateGradesearchForm" action="!search" title="ui.searchForm" target="certificateGradeList" theme="search"]
                <input type="hidden" name="orderBy" value="certificateGrade.updatedAt desc"/>
                [@b.textfield name="certificateGrade.std.code" label="学号"/]
                [@b.textfield name="certificateGrade.std.name" label="姓名"/]
                [@b.textfield name="certificateGrade.std.state.grade" label="年级"/]
                [@b.select name="certificateGrade.std.state.department.id" label="院系" items=departments?sortBy(["code"]) empty="..." /]
                [@b.textfield name="certificateGrade.std.state.squad.name" label="班级名称"/]
                [@b.select name="certificateGrade.certificate.category.id" id="categoryId" label="考试类型" items=certificateCategories empty="..."/]
                [@b.select name="certificateGrade.certificate.id" id="certificateId" label="科目" items=certificates empty="..." /]
                [@b.date label="考试日期" name="certificateGrade.acquiredOn"/]
                [@b.date id="fromAt" label="录入从" name="fromAt" format="yyyy-MM-dd" maxDate="#F{$dp.$D(\\'toAt\\')}"/]
                [@b.date id="toAt" label="录入到" name="toAt" format="yyyy-MM-dd" minDate="#F{$dp.$D(\\'fromAt\\')}"/]
            [/@]
  </div>
  <div class="search-list">
            [@b.div id="certificateGradeList" href="!search" /]
  </div>
</div>
[@b.foot/]
