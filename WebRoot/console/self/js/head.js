var header = $("<a href='index.html' class='logo'>" +
	"<span class='logo-mini'>" +
	"<img src='self/img/logo-new-mini.png' style='width: 100%;padding: 8px 0'/>" +
	"</span>" +
	"<span class='logo-lg'>" +
	"<img src='self/img/logo-new.png' style='width: 50%;'/>" +
	"</span>" +
	"</a>" +
	"<nav class='navbar navbar-static-top' data-role='navigation' id='topAnchor'>" +
	"<a href='#' class='sidebar-toggle' data-toggle='offcanvas' data-role='button' style='padding: 22px;'>" +
	"<span class='sr-only'>Toggle navigation</span>" +
	"</a>" +
	"<div class='weather' id='weather' style='color: #fff;width: 35vh;height: 60px;overflow: hidden;float: left;padding-left: 10px;padding-top: 5px'>" +
	"<iframe width='420' scrolling='no' height='60' frameborder='0' allowtransparency='true' src='//i.tianqi.com/index.php?c=code&id=12&color=%23FFFFFF&icon=1&py=zhengzhou&num=1&site=12'></iframe>" +
	"</div>" +
	"<marquee behavior='' direction='left'>" +
	"<p style='text-decoration: underline;color: #eaeaea;'>法眼慧心智能研判平台</p>" +
	"</marquee>" +
	"<div class='navbar-custom-menu'>" +
	"<ul class='nav navbar-nav'>" +
	"<li style='padding: 20px 10px 0 15px'>" +
	"<input type='checkbox' data-on-color='success' data-on-text='开' data-off-text='关' data-label-text='护眼模式' data-label-width='70' id='switchEyes'/>" +
	"</li>" +
	"<li class='dropdown'>" +
	"<a href='#' class='dropdown-toggle' data-toggle='dropdown' style='padding: 22px 10px;'><i class='fa fa-dashboard'></i> 个性化</a>" +
	"<div class='dropdown-menu'><div id='tab_skin'></div></div>" +
	"</li>" +
	"<li class='dropdown user user-menu'>" +
	"<a href='#' class='dropdown-toggle' data-toggle='dropdown' style='padding: 22px 10px;'>" +
	"<i class='fa fa-user-circle-o'></i>" +
	"<span class='hidden-xs' id='span_nickname'></span>" +
	"</a>" +
	"<ul class='dropdown-menu'>" +
	"<li class='user-header'>" +
	"<img src='res/dist/img/avatar5.png' class='img-circle' alt='User Image' style='height: 80px;width: 80px;display: -none;'>" +
	"<p>" +
	"<small style='margin-top: -4px;margin-bottom: 3px;'>" +
	"<label class='label label-info' style='border-radius: 0px;padding: 0.1em 0.6em;'><font id='font_department'></font></label><label class='label label-default' style='border-radius: 0px;padding: 0.1em 0.6em;'><font id='font_role'></font></label>" +
	"</small>" +
	"<small><font id='font_nickname'></font> / <font id='font_phone'></font></small>" +
	"<small>上次登录时间：<font id='font_last_time'></font></small>" +
	"<small>上次登录IP：<font id='font_last_ip'></font></small>" +
	"</p>" +
	"</li>" +
	"</ul>" +
	"</li>" +
	"<li><a href='login.html' style='padding: 22px 10px;'><i class='fa fa-sign-out'></i> 退出</a></li>" +
	"</ul>" +
	"</div>" +
	"</nav>");
$(".main-header").append(header);

var footer = "<div class='pull-right hidden-xs'><b>版本</b> 2.0</div><strong>版权所有 2014-2018 法眼慧心网 豫ICP备18003333号-1 / 技术支持：13523565413</strong>";
$(".main-footer").append(footer);