[#ftl]
[@b.head/]
  [@b.toolbar title="校内课程成绩"/]
  <div class="search-container">
    <div class="search-panel">
        [@b.form title="ui.searchForm" name="searchForm" action="!search" target="grades" theme="search"]
          <input type="hidden" value="grade.std.user.code" name="orderBy"/>
          [@b.select label="毕业批次" name="sessionId" items=sessions required="true" /]
          [@b.textfields names="grade.std.user.code;学号,grade.std.user.name;姓名,grade.std.state.grade;年级,grade.std.state.department.name;学院名称,grade.std.state.squad.code;班级代码,grade.std.state.squad.name;班级名称,grade.course.code;课程代码,grade.course.name;课程名称"/]
        [/@]
    </div>
    <div class="search-list">[@b.div id="grades"]正在查询中...[/@]</div>
  </div>
  <script>
    [#if sessions?size>0]
    $(function() {
      $(document).ready(function() {
        bg.form.submit(document.searchForm, "${b.url("!search")}", "grades");
      });
    });
    [/#if]
  </script>
[@b.foot/]
