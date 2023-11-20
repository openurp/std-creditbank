[#ftl]
[@b.head/]
  [@b.toolbar title="校内课程成绩"/]
  <div class="search-container">
    <div class="search-panel">
        [@b.form title="ui.searchForm" name="searchForm" action="!search" target="grades" theme="search"]
          <input type="hidden" value="grade.std.code" name="orderBy"/>
          [@b.select label="毕业批次" name="batchId" items=batches required="true" /]
          [@b.textfields names="grade.std.code;学号,grade.std.name;姓名,grade.std.state.grade.code;年级"/]
          [@b.textfields names="grade.std.state.department.name;学院名称,grade.std.state.squad.name;班级名称,grade.course.code;课程代码,grade.course.name;课程名称"/]
        [/@]
    </div>
    <div class="search-list">[@b.div id="grades"]正在查询中...[/@]</div>
  </div>
  <script>
    [#if batches?size>0]
    $(function() {
      $(document).ready(function() {
        bg.form.submit(document.searchForm, "${b.url("!search")}", "grades");
      });
    });
    [/#if]
  </script>
[@b.foot/]
