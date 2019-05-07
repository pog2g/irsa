function getParameter(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return unescape(r[2]);
    }
    return "";
}

/*function checkRadio(a){
	if(a=='1'){
		$("#label_apply_name").html("姓名");
		$("#label_apply_legal_person").html("英文名/其他语种名/化名/曾用名");
		$('#label_apply_nature1').css('position','');
		$('#label_apply_nature1').css('z-index','0');
		$('#apply_ws_address1').css('position','');
		$('#apply_ws_address1').css('z-index','0');
		$('#c1').css('position','');
	    $('#c1').css('z-index','0');
		$("input[name='apply_type_1']").iCheck("check");
		$("#apply_type_2").removeAttr("checked");
	}else if (a=='2'){
		$("#label_apply_name").html("法人组织名称");
		$("#label_apply_legal_person").html("单位名称");
		 //$('#label_apply_nature1').css('position','absolute');
		//$('#label_apply_nature1').css('z-index','-100');
		 $('#apply_ws_address1').css('position','absolute');
		 $('#apply_ws_address1').css('z-index','-100');
		/!*$("#s1").css("display",'none');
		$("#s2").css("display",'none');
		$("#s3").css("display",'none');
		$("#s4").css("display",'none');
		$("#s5").css("display",'none');
		$("#s6").css("display",'none');
		$("#s7").css("display",'none');*!/
		 $("#s1").css("display",'show');
			$("#s2").css("display",'show');
			$("#s3").css("display",'show');
			$("#s4").css("display",'show');
			$("#s5").css("display",'show');
			$("#s6").css("display",'show');
			$("#s7").css("display",'show');
		   $('#c1').css('position','');
		    //$('#c1').css('z-index','-100');
		/!*$("input[name='apply_type']:last").iCheck("disable");
		$("input[name='apply_type']").iCheck("check");*!/
		$("input[name='apply_type_2']").iCheck("check");
		$("#apply_type_1").removeAttr("checked");
	}
}*/


var isOver = "";
var limitTime = "";

function showTime() {
    var distance = limitTime;
    var day, hour, minute, second;
    day = Math.floor(distance / 86400000)
    distance -= day * 86400000;
    hour = Math.floor(distance / 3600000)
    distance -= hour * 3600000;
    minute = Math.floor(distance / 60000)
    distance -= minute * 60000;
    second = Math.floor(distance / 1000)
    // 时分秒为单数时、前面加零站位
    if (hour < 10) {
        hour = "0" + hour;
    }
    if (minute < 10) {
        minute = "0" + minute;
    }
    if (second < 10) {
        second = "0" + second;
    }

    limitTime -= 1000;

    // 显示时间
    if (isOver == "0") {
        $(".callout-info").show();
        $(".callout-danger").hide();
    } else if (isOver == "1") {
        $(".callout-info").hide();
        $(".callout-danger").show();
    }
    $(".font-day").html(day);
    $(".font-hour").html(hour);
    $(".font-minute").html(minute);
    $(".font-second").html(second);

    setTimeout("showTime()", 1000);
};

$(".todo-list").sortable({
    placeholder: "sort-highlight",
    handle: ".handle",
    forcePlaceholderSize: true,
    zIndex: 999999
});

/**
 * 初始化单/复选框
 * */
$("input[type='radio'].minimal, input[type='checkbox'].minimal").iCheck({
    checkboxClass: "icheckbox_minimal-blue",
    radioClass: "iradio_minimal-blue"
});
$('input[type="radio"].flat, input[type="checkbox"].flat').iCheck({
    checkboxClass: 'icheckbox_flat-blue',
    radioClass: 'iradio_flat-blue'
});

/**
 * 初始化总复选框
 */
function initCheckBox4Total(parentClassName, childClassName) {
    $("." + parentClassName).iCheck("uncheck");
    $("." + parentClassName).on("ifChecked", function () {
        $("." + childClassName).iCheck("check");
    });
    $("." + parentClassName).on("ifUnchecked", function () {
        $("." + childClassName).iCheck("uncheck");
    });
}

function initCheckBoxByClassName(className) {
    $("." + className).iCheck({
        checkboxClass: "icheckbox_minimal-blue",
        radioClass: "iradio_minimal-blue"
    });
}

/**
 * 加载数据Select2
 */
function initSelect2(elementId) {
    $("#" + elementId).empty();
    $("#" + elementId).select2({
        minimumResultsForSearch: Infinity,
        data: [{
            id: -1,
            text: "请选择"
        }]
    });
}

function initSelect2WithData(elementId, data) {
    $("#" + elementId).empty();
    $("#" + elementId).select2({
        minimumResultsForSearch: Infinity,
        data: data
    });
}

function onChange4Province(provinceId, cityId, cityUrl, countyId) {
    $("#" + provinceId).on("change", function (e) {
        initSelect2(countyId);

        var id = $("#" + provinceId + " option:selected").val();
        if (id == "-1") {
            initSelect2(cityId);
        } else {
            // /console/apply_request 地址不存在（输入姓名后点击空白地方会导致调用该方法，从而报404）
            // loadData4City(cityId, cityUrl + "?parent=" + id);
        }
    });
}

function onChange4City(cityId, countyId, countyUrl) {
    $("#" + cityId).on("change", function (e) {
        var id = $("#" + cityId + " option:selected").val();
        if (id == "-1") {
            initSelect2(countyId);
        } else {
            loadData4County(countyId, countyUrl + "?parent=" + id);
        }
    });
}

var cityId = "";

function loadData4City(elementId, url) {
    $("#" + elementId).empty(); //清空数据
    $.ajax({
        async: false,
        type: "POST",
        url: url,
        dataType: "JSON",
        success: function (data) {
            if (data.result == 1) {
                $("#" + elementId).select2({
                    minimumResultsForSearch: Infinity,
                    data: data.data,
                });
                if (cityId != null && cityId != "") {
                    $("#" + elementId).val(cityId).trigger("change");
                }
                cityId = "";
                return;
            }
            showMsg(data.error_msg);
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    })
}

var countyId = "";

function loadData4County(elementId, url) {
    $("#" + elementId).empty(); //清空数据
    $.ajax({
        async: false,
        type: "POST",
        url: url,
        dataType: "JSON",
        success: function (data) {
            if (data.result == 1) {
                $("#" + elementId).select2({
                    minimumResultsForSearch: Infinity,
                    data: data.data,
                });
                if (countyId != null && countyId != "") {
                    $("#" + elementId).val(countyId).trigger("change");
                }
                countyId = "";
                return;
            }
            showMsg(data.error_msg);
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    })
}

function loadData4Select2(elementId, url, selected, isWithSearch) {
    isWithSearch = isWithSearch || false;
    selected = selected || "";
    $("#" + elementId).empty(); //清空数据
    $.ajax({
        async: false,
        type: "POST",
        url: url,
        dataType: "JSON",
        success: function (data) {
            if (data.result == 1) {
                if (isWithSearch) {
                    $("#" + elementId).select2({
                        data: data.data,
                        matcher: matchCustom,
                    });
                } else {
                    $("#" + elementId).select2({
                        minimumResultsForSearch: Infinity,
                        data: data.data,
                    });
                }
                if (selected != null && selected != "") {
                    $("#" + elementId).val(selected).trigger("change");
                }
                return;
            }
            showMsg(data.error_msg);
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    })
}

function matchCustom(params, data) {
    if ($.trim(params.term) === '') {
        return data;
    }
    if (typeof data.text === 'undefined') {
        return null;
    }
    if (data.text.indexOf(params.term) > -1) {
        var modifiedData = $.extend({}, data, true);
        //modifiedData.text += ' (matched)';
        return modifiedData;
    }
    return null;
}

function onChange4Select(parentId, childId, url) {
    $("#" + parentId).on("change", function (e) {
        var id = $("#" + parentId + " option:selected").val();
        if (id == "-1") {
            initSelect2(childId);
            // 目前系统没有/console/apply_request接口(当输入姓名后输入框失去焦点就会调用该接口导致报404错误)
        } else if (url != "apply_request") {
            loadData4Select2(childId, url + "?parent=" + id);
        }
    });
}

function initSelect4Ajax(elementId, url) {
    $("#" + elementId).select2({
        language: "zh-CN",
        inputMessage: "请输入证件号码",
        delay: 250,
        ajax: {
            url: url,
            dataType: "json",
            data: function (params) {
                return {
                    key: params.term,
                    page: params.page || 1,
                };
            },
            processResults: function (data, params) {
                params.page = params.page || 1;
                return {
                    results: data.data.data_list,
                    pagination: {
                        more: (params.page * 10) < data.data.data_total
                    }
                };
            },
            cache: true
        },
        minimumInputLength: 1,
        escapeMarkup: function (markup) {
            return markup;
        },
        templateResult: formatPersonnel,
        templateSelection: formatRepoSelection
    });
}

function formatRepoSelection(repo) {
    return repo.name || repo.text;
}

function formatPersonnel(repo) {

    if (repo.loading) {
        return repo.name;
    }
    if (typeof repo.id_no === 'undefined') {
        var markup = "<div style='border-bottom: 1px solid #eee;padding-bottom: 10px'>" +
            "<div style='float: left;width: 220px;text-align: right;'>被申请人名称</div><div style='margin-left: 240px;'>" + repo.name + "</div>" +
            "<div style='float: left;width: 220px;text-align: right;'>被申请人住所</div><div style='margin-left: 240px;'>" + repo.unit_abode + "</div>" +
            "</div>";
        return markup;
    }
    var div_name = "";
    var div_unit_name = "";
    if (typeof repo.type !== 'undefined') {
        if (addApplyType == "1") {
            div_name = "<div style='float: left;width: 220px;text-align: right;'>姓名</div><div style='margin-left: 240px;'>" + repo.name + "</div>";
        } else if (addApplyType == "2") {

            var legalPersonType = "&nbsp;";
            if (repo.legal_person_type == "1") {
                legalPersonType = "法定代表人";
            } else {
                legalPersonType = "负责人";
            }
            div_name = "<div style='float: left;width: 220px;text-align: right;'>法人组织名称</div><div style='margin-left: 240px;'>" + repo.unit_name + "</div>" +
                "<div style='float: left;width: 220px;text-align: right;'>" + legalPersonType + "</div><div style='margin-left: 240px;'>" + repo.name + "</div>";
        }
    } else {
        div_name = "<div style='float: left;width: 220px;text-align: right;'>姓名</div><div style='margin-left: 240px;'>" + repo.name + "</div>";
        if (repo.unit_name != null && repo.unit_name != "" && repo.unit_name != "无") {
            div_unit_name = "<div style='float: left;width: 220px;text-align: right;'>工作单位</div><div style='margin-left: 240px;'>" + repo.unit_name + "</div>";
        }
    }

    //搜索下拉信息展示
    var markup =
        "<div style='border-bottom: 1px solid #eee;padding-bottom: 10px'>" +
        div_name +
        "<div style='float: left;width: 220px;text-align: right;'>通讯地址</div><div style='margin-left: 240px;'>" + repo.abode + "</div>" +
        div_unit_name +
        "</div>";
    return markup;
}

/**
 * 加载数据Radio
 */
function loadData4Choose(type, parentId, childName, url, selected) {
    $.ajax({
        type: "POST",
        url: url,
        dataType: "JSON",
        success: function (data) {
            if (data.result == 1) {
                $.each(data.data, function (index, value) {
                    var $choose = $("<label style='padding-right: 7px;' class='" + childName + "'>" +
                        "<input type='" + type + "' class='minimal' name='" + childName + "' id='" + childName + "_" + value.id + "' value='" + value.id + "'>&nbsp;" + value.text + "</label>");
                    $("#" + parentId).append($choose);
                });
                initCheckBoxByClassName(childName);
                if (selected != null && selected != "") {
                    $("#" + childName + "_" + selected).iCheck("check");
                }
                return;
            }
            showMsg(data.error_msg);
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

/**时间*/
function initDatetimePicker(elementId) {
    $("#" + elementId).datetimepicker({
        language: 'zh-CN',
        format: "yyyy-mm-dd",
        weekStart: 1,
        todayBtn: 0,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 1
    });
}

/***/
function createCKEditor(elementId, height) {
    var editor = CKEDITOR.replace(elementId, {
        resize_enabled: false,
        removePlugins: 'elementspath',
        //简体中文
        language: "zh-cn",
        //高度
        height: height,
        //工具栏设置
        toolbar: [
            //['Outdent','Indent','NumberedList','BulletedList','Outdent','Indent','Blockquote'],
            //['Bold','Italic','Underline','Strike','Subscript','Superscript'],
            //['Source','Maximize','Save','NewPage','Preview','Templates', 'Print'],
            //['Cut','Copy','Paste','PasteText','PasteFromWord'],
            //['Undo','Redo','Find','Replace','RemoveFormat','Table','HorizontalRule','SelectAll'],
            //['Link','Unlink','Anchor'],
            //['SpecialChar','Image'],
            //['Styles','Format'],
        ]
    });
    return editor;
}

function createCKEditor4Content(elementId, height) {
    var editor = CKEDITOR.replace(elementId, {
        resize_enabled: false,
        removePlugins: 'elementspath',
        //简体中文
        language: "zh-cn",
        //高度
        height: height,
        //工具栏设置
        toolbar: [
            ['JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock', 'Format'],
        ]
    });
    return editor;
}

/**
 * 对Date的扩展，将 Date 转化为指定格式的String
 * 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
 * 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
 */
Date.prototype.format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/**
 * 提示窗口
 */
function showMsg(msg) {
    $("#modal_msg").modal("show");
    $("#font_msg").html(msg);
}

/**
 * 初始化
 */
function initAccount(webpage) {
    webpage = webpage || null;
    $.ajax({
        type: "POST",
        url: "get_account",
        dataType: "JSON",
        success: function (data) {
            if (data.result == 1) {
                //用户信息
                $("#span_nickname").html(data.data.nickname);
                $("#font_role").html(data.data.role);
                $("#font_nickname").html(data.data.nickname);
                $("#font_department").html(data.data.department);
                $(".font-department").html(data.data.department);
                $("#font_phone").html(data.data.phone);
                $("#font_last_ip").html(data.data.last_ip);
                $("#font_last_time").html(data.data.last_time);
                //菜单
                var $home = $("<li id='li_home'><a href='index.html'><i class='fa fa-home'></i> <span>首页</span></a></li>");
                $("#ul_menu").append($home);
                if (webpage == -1) {
                    $("#li_home").addClass("active");
                }

                $.each(data.data.competence, function (index, value) {
                    var $li = $("<li class='treeview' id='" + value.id + "'>" +
                        "<a href='#'><i class='fa " + value.icon + "'></i><span>" + value.text + "</span><i class='fa fa-angle-left pull-right'></i></a>" +
                        "<ul class='treeview-menu' id='ul_" + value.id + "'></ul>" +
                        "</li>");
                    $("#ul_menu").append($li);

                    $.each(value.children, function (index1, value1) {
                        $li_child = $("<li id='" + value1.id + "'><a href='" + value1.url + "'><i class='fa " + value1.icon + "'></i> " + value1.text + "</a></li>");
                        $("#ul_" + value.id).append($li_child);

                        if (value1.id == webpage) {
                            $("#" + value1.id).addClass("active");
                            $("#" + value.id).addClass("active");
                        }
                    });
                });

                //				$.each(data.data.department_number, function(index, value) {
                //					$(".font-title-" + value.state).html(value.document_title);
                //					$(".font-number-" + value.state + "-1").html(value.number_1);
                //					$(".font-number-" + value.state + "-2").html(value.number_2);
                //					$(".font-number-" + value.state + "-3").html(value.number_3);
                //				})
                return;
            }
            //window.location.href = "login.html";
        },
        error: function (xhr, type) {
            //window.location.href = "login.html";
        }
    });
}

function getActId() {
    var actId = "";
    $.ajax({
        async: false,
        type: "POST",
        url: "get_act_id.do",
        dataType: "JSON",
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            actId = data.data;
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
    return actId;
}

function getFileIcon(type) {
    var icon = "";
    if (type == "1") {
        icon = "fa-file-image-o";
    } else if (type == "2") {
        icon = "fa-file-word-o";
    } else if (type == "3") {
        icon = " fa-file-excel-o";
    } else if (type == "4") {
        icon = "fa-file-pdf-o";
    } else if (type == "5") {
        icon = "fa-file-video-o";
    } else if (type == "6") {
        icon = "fa-file-archive-o";
    } else {
        icon = "fa-file-o";
    }
    return icon;
}

function initData4Files(parentId, childClass, data, isNotRename) {
    isNotRename = isNotRename || false;
    $("." + childClass).remove();
    $.each(data, function (index, value) {
        var icon = getFileIcon(value.type);
        var btnEye = "";
        if (value.type == "1") {
            btnEye = "<a class='btn btn-default btn-xs' href='" + value.url + "' target='_blank'><i class='fa fa-eye'></i> 预览</a> ";
        } else {
            btnEye = "<a class='btn btn-default btn-xs' href='" + value.url + "' download='" + value.real_name + "." + value.ext + "'><i class='fa fa-eye'></i> 预览</a> ";
        }
        var btnRename = "";
        if (!isNotRename) {
            btnRename = "<button class='btn btn-default btn-xs' data-toggle='modal' data-target='#modal_rename' data-id='" + value.id + "' data-name='" + value.real_name + "'><i class='fa fa-edit'></i> 重命名</button> ";
        }
        var btnDelete = "";
        if (value.url.indexOf("temp") != -1) {
            btnDelete = "<button class='btn btn-default btn-xs btn-delete-temp' data-id='" + value.id + "'><i class='fa fa-trash-o'></i> 删除</button>";
        }
        var $li = $("<li class='" + childClass + "' style='width: auto;' id='" + value.id + "'>" +
            "<div class='mailbox-attachment-info'>" +
            "<div class='mailbox-attachment-name' style='overflow: hidden;height: 25px;'><i class='fa " + icon + "'></i> <font id='font_" + value.id + "'>" + value.real_name + "</font></div>" +
            "<span class='mailbox-attachment-size'>" +
            btnRename + btnEye + btnDelete +
            "</span>" +
            "</div>" +
            "</li>");
        $("#" + parentId).append($li);
    });
}

function initData4Document(parentId, childClass, data) {
    $("." + childClass).remove();
    $.each(data, function (index, value) {
        var $tr = $("<tr class='" + childClass + "'>" +
            "<td>" + value.real_name + "</td>" +
            "<td style='width: 100px;'>" +
            "<button class='btn btn-xs btbtn-default btn-view' data-id='" + value.id + "'>预览</button> " +
            "<a class='btn btn-xs btn-default' href='" + value.url + "' download='" + value.real_name + "." + value.ext + "'>下载</a>" +
            "</td>" +
            "</tr>");
        $("#" + parentId).append($tr);
    });
    initEvent4Document();
}

function initData4FileLabels(parentId, childClass, data) {
    $("." + childClass).remove();
    $.each(data, function (index, value) {
        var icon = getFileIcon(value.type);
        if (value.type == "1") {
            var $li = $("<a href='" + value.url + "' target='_blank' class='btn bg-gray as-btn'><i class='fa " + icon + "'></i> " + value.real_name + "</a>");
            $("#" + parentId).append($li);
        } else {
            var $li = $("<a href='" + value.url + "' download='" + value.real_name + "." + value.ext + "' class='btn bg-gray as-btn'><i class='fa " + icon + "'></i> " + value.real_name + "</a>");
            $("#" + parentId).append($li);
        }

    });
}

function onTempDelete() {
    $(".btn-delete-temp").off().on("click", function () {
        var id = $(this).data("id");
        $.ajax({
            url: "temp/delete",
            type: "post",
            dataType: "json",
            data: {
                id: id
            },
            success: function (data) {
                if (data.result != 1) {
                    showMsg(data.error_msg);
                    return;
                }

                $("#" + id).remove();
            },
            error: function (xhr, type) {
                showMsg("系统繁忙");
            }
        });
    });
}

function initUpload(loadingId, elementId, childClass, url) {
    $("#" + elementId).fileupload({
        url: url,
        dataType: "json",
        add: function (e, data) {
            $("#" + loadingId).show();
            data.submit();
        },
        done: function (e, data) {
            $("#" + loadingId).modal("hide");
            $('.modal-backdrop').remove();
            if (data.result.result == "1") {
                initData4Files("ul_" + elementId, childClass, data.result.data);
                if (url.indexOf("temp") != -1) {
                    onTempDelete();
                }
                $("#" + loadingId).hide();
            } else {
                showMsg("上传失败！" + data.result.error_msg);
                $("#" + loadingId).hide();
            }
        },
        progressall: function (e, data) {
        }
    });
}

function getFileList(loadingId, parentId, childClass, url, isLabel, isDocument) {
    isLabel = isLabel || false;
    isDocument = isDocument || false;
    $("#" + loadingId).show();
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        success: function (data) {
            $("#" + loadingId).hide();
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            if (isLabel) {
                initData4FileLabels(parentId, childClass, data.data);
                return;
            }
            if (isDocument) {
                initData4Document(parentId, childClass, data.data);
                return;
            }
            initData4Files(parentId, childClass, data.data);
            if (url.indexOf("temp") != -1) {
                onTempDelete();
            }
        },
        error: function (xhr, type) {
            $("#" + loadingId).hide();
            showMsg("系统繁忙");
        }
    });
}

var identityTypeArr = [{
    id: -1,
    text: "请选择"
}, {
    id: 1,
    text: "律师"
}, {
    id: 2,
    text: "法律工作者"
}, {
    id: 3,
    text: "港澳台律师"
}, {
    id: 4,
    text: "军队律师"
}];


//代理人类型
var agentTypeArr = [{
    id: 1,
    text: "近亲属"
}, {
    id: 2,
    text: "律师/法律工作者"
}, {
    id: 3,
    text: "单位工作人员"
}, {
    id: 4,
    text: "其他公民"
}];


var searchStateArr1 = [{
    id: -1,
    text: "请选择"
}, {
    id: 1,
    text: "立案申请"
}, {
    id: 2,
    text: "限期补正"
}];
var searchStateArr2 = [{
    id: -1,
    text: "请选择"
}, {
    id: 3,
    text: "告知"
}, {
    id: 4,
    text: "转办"
}, {
    id: 5,
    text: "转送"
}, {
    id: 6,
    text: "不予受理"
}, {
    id: 7,
    text: "逾期未补正"
}, {
    id: 8,
    text: "主动放弃申请"
}];
var searchStateArr3 = [{
    id: -1,
    text: "请选择"
}, {
    id: 9,
    text: "待被申请人答复"
}, {
    id: 11,
    text: "被申请人已答复"
}];
var searchStateArr4 = [{
    id: -1,
    text: "请选择"
}, {
    id: 12,
    text: "受理"
}, {
    id: 13,
    text: "延期"
}, {
    id: 14,
    text: "中止"
}, {
    id: 20,
    text: "听证"
}];
var searchStateArr5 = [{
    id: -1,
    text: "请选择"
}, {
    id: 17,
    text: "支持（变更）"
}, {
    id: 21,
    text: "支持（撤销）"
}, {
    id: 22,
    text: "支持（确认违法）"
}, {
    id: 23,
    text: "支持（责令限期履行）"
}, {
    id: 24,
    text: "支持（确认无效）"
}, {
    id: 25,
    text: "支持（调解达成生效）"
}, {
    id: 26,
    text: "支持（其他决定）"
}, {
    id: 18,
    text: "否定（驳回）"
}, {
    id: 19,
    text: "否定（维持）"
}, {
    id: 15,
    text: "终止（撤回）"
}, {
    id: 16,
    text: "终止（其他/自定义）"
}];
var searchStateArr6 = [{
    id: -1,
    text: "请选择"
}, {
    id: 3,
    text: "告知"
}, {
    id: 4,
    text: "转办"
}, {
    id: 5,
    text: "转送"
}, {
    id: 6,
    text: "不予受理"
}, {
    id: 7,
    text: "逾期未补正"
}, {
    id: 8,
    text: "主动放弃申请"
}, {
    id: 17,
    text: "支持（变更）"
}, {
    id: 21,
    text: "支持（撤销）"
}, {
    id: 22,
    text: "支持（确认违法）"
}, {
    id: 23,
    text: "支持（责令限期履行）"
}, {
    id: 24,
    text: "支持（确认无效）"
}, {
    id: 25,
    text: "支持（调解达成生效）"
}, {
    id: 26,
    text: "支持（其他决定）"
}, {
    id: 18,
    text: "否定（驳回）"
}, {
    id: 19,
    text: "否定（维持）"
}, {
    id: 15,
    text: "终止（撤回）"
}, {
    id: 16,
    text: "终止（其他/自定义）"
}];
var searchStateArr = [{
    id: -1,
    text: "请选择"
}, {
    id: 1,
    text: "立案申请"
}, {
    id: 2,
    text: "限期补正"
}, {
    id: 3,
    text: "告知"
}, {
    id: 4,
    text: "转办"
}, {
    id: 5,
    text: "转送"
}, {
    id: 6,
    text: "不予受理"
}, {
    id: 7,
    text: "逾期未补正"
}, {
    id: 8,
    text: "主动放弃申请"
}, {
    id: 9,
    text: "待被申请人答复"
}, {
    id: 11,
    text: "被申请人已答复"
}, {
    id: 12,
    text: "受理"
}, {
    id: 13,
    text: "延期"
}, {
    id: 14,
    text: "中止"
}, {
    id: 20,
    text: "听证"
}, {
    id: 17,
    text: "支持（变更）"
}, {
    id: 21,
    text: "支持（撤销）"
}, {
    id: 22,
    text: "支持（确认违法）"
}, {
    id: 23,
    text: "支持（责令限期履行）"
}, {
    id: 24,
    text: "支持（确认无效）"
}, {
    id: 25,
    text: "支持（调解达成生效）"
}, {
    id: 26,
    text: "支持（其他决定）"
}, {
    id: 18,
    text: "否定（驳回）"
}, {
    id: 19,
    text: "否定（维持）"
}, {
    id: 15,
    text: "终止（撤回）"
}, {
    id: 16,
    text: "终止（其他/自定义）"
}];

function getStateLabel(state, isProcess) {
    isProcess = isProcess || false;
    var label = "";
    switch (state) {
        case "-1":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #ec9e69;color: #fff'>申请时间</label>";
            break;
        case "0":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #feb062;color: #fff'>录入时间</label>";
            break;
        case "1":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #3c8dbc;color: #fff'>立案申请</label>";
            break;
        case "2":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #00c0ef;color: #fff'>限期补正</label>";
            break;
        case "3":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #39cccc;color: #fff'>告知</label>";
            break;
        case "4":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #155e63;color: #fff'>受理转办</label>";
            break;
        case "5":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #76b39d;color: #fff'>转送</label>";
            break;
        case "6":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #db456f;color: #fff'>不予受理</label>";
            break;
        case "7":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #cd5555;color: #fff'>逾期未补正</label>";
            break;
        case "8":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #882042;color: #fff'>受理前放弃</label>";
            break;
        case "9":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #f39c12;color: #fff'>受理待答复</label>";
            break;
        case "10":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #ea4c4c;color: #fff'>被申请人未答复</label>";
            break;
        case "11":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #00a65a;color: #fff'>被申请人已答复</label>";
            break;
        case "12":
            if (isProcess) {
                label = "<label class='label label-default' style='border-radius: 0px;background-color: #605ca8;color: #fff'>受理</label>";
            } else {
                label = "<label class='label label-default' style='border-radius: 0px;background-color: #605ca8;color: #fff'>审理中</label>";
            }
            break;
        case "13":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #7971ea;color: #fff'>延期</label>";
            break;
        case "14":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #cc99f9;color: #fff'>中止</label>";
            break;
        case "20":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #7a4579;color: #fff'>听证</label>";
            break;
        case "17":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #35d0ba;color: #fff'>支持（变更）</label>";
            break;
        case "21":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #45b7b7;color: #fff'>支持（撤销）</label>";
            break;
        case "22":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #00a8b5;color: #fff'>支持（确认违法）</label>";
            break;
        case "23":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #00bbf0;color: #fff'>支持（责令履行）</label>";
            break;
        case "24":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #448ef6;color: #fff'>支持（确认无效）</label>";
            break;
        case "25":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #2f89fc;color: #fff'>支持（调解与决定）</label>";
            break;
        case "26":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #0060ca;color: #fff'>支持（其他决定）</label>";
            break;
        case "18":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #f73859;color: #fff'>否定（驳回）</label>";
            break;
        case "19":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #d81b60;color: #fff'>否定（维持）</label>";
            break;
        case "15":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #f9499e;color: #fff'>终止（自愿撤回）</label>";
            break;
        case "16":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #8b104e;color: #fff'>终止（法定或其他）</label>";
            break;
        case "30":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #3c8dbc;color: #fff'>规范性文件审查中止</label>";
            break;
        case "32":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #3c8dbc;color: #fff'>部分被申请人答复</label>";
            break;
        default:
            break;
    }
    return label;
}

function getNumberStateLabel(state) {
    var label = "";
    switch (state) {
        case "2":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #00c0ef;color: #fff'>限期补正</label>";
            break;
        case "3":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #39cccc;color: #fff'>告知</label>";
            break;
        case "4":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #155e63;color: #fff'>转办</label>";
            break;
        case "5":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #76b39d;color: #fff'>转送</label>";
            break;
        case "6":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #db456f;color: #fff'>不予受理</label>";
            break;
        case "7":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #cd5555;color: #fff'>逾期未补正</label>";
            break;
        case "8":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #882042;color: #fff'>受理前放弃</label>";
            break;
        case "9":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #605ca8;color: #fff'>受理（申请人）</label>";
            break;
        case "10":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #605ca8;color: #fff'>受理（被请人）</label>";
            break;
        case "11":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #605ca8;color: #fff'>受理（第三人）</label>";
            break;
        case "13":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #7971ea;color: #fff'>延期</label>";
            break;
        case "14":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #cc99f9;color: #fff'>中止</label>";
            break;
        case "15":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #f9499e;color: #fff'>终止（自愿撤回）</label>";
            break;
        case "16":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #8b104e;color: #fff'>终止（法定或其他）</label>";
            break;
        case "17":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #35d0ba;color: #fff'>支持</label>";
            break;
        case "18":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #f73859;color: #fff'>否定（驳回）</label>";
            break;
        case "19":
            label = "<label class='label label-default' style='border-radius: 0px;background-color: #d81b60;color: #fff'>否定（维持）</label>";
            break;
        default:
            break;
    }
    return label;
}

//初始化页面
function initPage(web) {
    var mode = "";
    initAccount(web);
    switch (web) {
        case "713361638c624b768441b491b41dfe99":
            document.title = "受理程序";
            $(".content-header").html("<h1>受理系统 <small>受理程序</small></h1>");
            initSelect2WithData("search_state", searchStateArr1);
            mode = "1";
            break;
        case "75727481862a4f0489ecf0ca3b44ed66":
            document.title = "受理结案";
            $(".content-header").html("<h1>受理系统 <small>受理结案</small></h1>");
            initSelect2WithData("search_state", searchStateArr2);
            mode = "2";
            break;
        case "33db7d5556d24b3ba0e5f633e5e6d574":
            document.title = "中转程序";
            $(".content-header").html("<h1>中转系统 <small>中转程序</small></h1>");
            initSelect2WithData("search_state", searchStateArr3);
            mode = "3";
            break;
        case "739ad75846a44afba6f9af2a225a528b":
            document.title = "审理程序";
            $(".content-header").html("<h1>审理系统 <small>审理程序</small></h1>");
            initSelect2WithData("search_state", searchStateArr4);
            mode = "4";
            break;
        case "d201edbb5e7c430dae556e0baee311ff":
            document.title = "审理结案";
            $(".content-header").html("<h1>审理系统 <small>审理结案</small></h1>");
            initSelect2WithData("search_state", searchStateArr5);
            mode = "5";
            break;
        case "bbabbb25f8ae47009a1084fe9735f220":
            document.title = "领导审批";
            $(".content-header").html("<h1>领导审批 <small>审理系统</small></h1>");
            initSelect2WithData("search_state", searchStateArr5);
            mode = "5";
            break;
        case "e511b81c542547d3a2caf6b3fb8bc093":
            document.title = "一般审批";
            $(".content-header").html("<h1>一般审批 <small>审批系统</small></h1>");
            initSelect2WithData("search_state", searchStateArr5);
            mode = "5";
            break;
        case "946fe85e9529458d9ef4dbb2c79d11f8":
            document.title = "特殊审批";
            $(".content-header").html("<h1>特殊审批 <small>审批系统</small></h1>");
            initSelect2WithData("search_state", searchStateArr5);
            mode = "5";
            break;
        case "a763219a68e34df5bea394229456ef47":
            document.title = "录入列表";
            $(".content-header").html("<h1>录入系统 <small>录入列表</small></h1>");
            initSelect2WithData("search_state", searchStateArr);
            mode = "0";
            break;
        case "2f5ade4990e1462ba01fa094dd54536b":
            document.title = "查阅下载";
            $(".content-header").html("<h1>电子券宗库 <small>查阅下载</small></h1>");
            initSelect2WithData("search_state", searchStateArr6);
            mode = "6";
            break;
        case "9ba73156e5604c8b9cefe341a7f697fb":
            document.title = "生成卷宗";
            $(".content-header").html("<h1>电子券宗库 <small>整理生成</small></h1>");
            initSelect2WithData("search_state", searchStateArr6);
            mode = "6";
            break;
    }
    return mode;
}

function initLabels(elmentId, data, split) {
    $("#" + elmentId).empty();
    var labelArr = data.split(split);
    $.each(labelArr, function (index, value) {
        if (value != null && value.trim() != "") {
            var $label = $("<font class='bg-gray as-label'>" + value + "</font>");
            $("#" + elmentId).append($label);
        }
    });
}

var yearNoArr = [{
    id: "-1",
    text: "请选择"
}, {
    id: "2019",
    text: "2019"
}, {
    id: "2018",
    text: "2018"
}, {
    id: "2017",
    text: "2017"
},]

function initModal(casesPersonnelType) {
    var element = "";
    if (casesPersonnelType == "1") {
        element = "apply";
    } else if (casesPersonnelType == "2") {
        element = "defendant";
    } else if (casesPersonnelType == "3" || casesPersonnelType == "7") {
        element = "third_party";
    } else if (casesPersonnelType == "73") {
        element = "apply_third_party";
    } else {
        element = "agent";
    }
    return element;
}

function initElement4Row(casesPersonnelType) {
    var element = "";
    if (casesPersonnelType == "1") {
        element = "apply";
    } else if (casesPersonnelType == "2") {
        element = "defendant";
    } else if (casesPersonnelType == "3") {
        element = "third_party";
    } else if (casesPersonnelType == "4") {
        element = "apply_agent";
    } else if (casesPersonnelType == "5") {
        element = "third_party_agent";
    } else if (casesPersonnelType == "6") {
        element = "defendant_agent";
    } else if (casesPersonnelType == "7") {
        /*element = "apply_third_party";*/
        element = "advice_third_party"
    }
    return element;
}

//柱状
function initChart4Bar(elementId, data, title) {
    title = title || "";
    var configPie = {
        type: "bar",
        data: {
            datasets: [{
                data: data.data_list,
                backgroundColor: data.color_list,
            }],
            labels: data.label_list
        },
        options: {
            responsive: true,
            title: {
                display: (title != null && title != ""),
                text: title
            },
        }
    };

    var ctxPie = document.getElementById(elementId).getContext("2d");
    new Chart(ctxPie, configPie);
}

//水平柱状
function initChart4Bar2(elementId, data, title) {
    title = title || "";
    var configPie = {
        type: "bar",
        data: {
            datasets: [{
                data: data.data_list,
                backgroundColor: data.color_list,
            }],
            labels: data.label_list
        },
        options: {
            responsive: true,
            title: {
                display: (title != null && title != ""),
                text: title
            },
            yAxis: {
                type: 'category'
            }
        }
    };
    var ctxPie = document.getElementById(elementId).getContext("2d");

    new Chart(ctxPie, configPie);
}

//初始化案件当事人资料信息
function initData4ApplyWithReference(data) {
    $('#apply-tab').empty();
    $('#apply-info').empty();

    $.each(data.data, function (index, value) {
        //个人
        if (value.type == "1") {

            //法人
        } else if (value.type == "2") {

        }
        var otherName = "无";
        if (value.other_name != null && value.other_name != "") {
            otherName = value.other_name;
        }
        var nature = "&nbsp;"
        if (value.nature != null && value.nature != "") {
            nature = value.nature;
        }
        var domicile = "&nbsp;"
        if (value.domicile != null && value.domicile != "") {
            domicile = value.domicile;
        }
        var birthday = "&nbsp;"
        if (value.birthday != null && value.birthday != "") {
            birthday = value.birthday;
        }
        var contact = "&nbsp;"
        if (value.contact != null && value.contact != "") {
            contact = value.contact;
        }
        var phone = "&nbsp;"
        if (value.phone != null && value.phone != "") {
            phone = value.phone;
        }
        var gender = "保密";
        if (value.gender == "1") {
            gender = "男";
        } else if (value.gender == "2") {
            gender = "女";
        }
        var applyInfo =

            "<div class='tab-pane' id='" + index + "'>" +
            "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' id='" + value.id + "'>" +
            "<div class=row>" +
            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-user margin-r-5'></i>  6英文名/其他语种名/化名/曾用名</strong>" +
            "<p style='margin: 10px 0 0;'>" + otherName + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-home margin-r-5'></i>  出生日期</strong>" +
            "<p style='margin: 10px 0 0;'>" + birthday + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-home margin-r-5'></i>  性别</strong>" +
            "<p style='margin: 10px 0 0;'>" + gender + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-home margin-r-5'></i>  联系电话</strong>" +
            "<p style='margin: 10px 0 0;'>" + value.phone + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-user margin-r-5'></i>  民族</strong>" +
            "<p style='margin: 10px 0 0;'>" + nature + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +

            "<div class='col-lg-6'>" +
            "<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-12'>" +
            //TODO 数据库字段缺少
            "<strong><i class='fa fa-map-marker margin-r-5'></i>  法律文书送达地址</strong>" +
            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
            "<hr style='margin: 10px 0;'>" +
            "</div>" +
            "<div class='col-lg-12'>" +
            "<div class='row'>" +
            "<div class='col-lg-6'><div id='apply_pie" + value.id + "' style='height: 300px;'></div></div>" +
            "<div class='col-lg-6'><div id='apply_pie3" + value.id + "' style='height: 300px;'></div></div>" +
            "<div class='col-lg-6'><div id='apply_pie2" + value.id + "' style='height: 300px;'></div></div>" +
            "<div class='col-lg-6'><div id='apply_bar2" + value.id + "' style='height: 300px;'></div></div>" +
            "</div>" +
            "</div>" +

            "<div class='overlay' id='loading_row_apply'></div>" +
            "</div>" +
            "</div>" +
            "</div>";


        var apply_data_pie = [
            {value: 335, name: '变更'},
            {value: 310, name: '撤销'},
            {value: 234, name: '确认违法'},
            {value: 135, name: '责令履行'},
            {value: 25, name: '一并赔偿'},
            {value: 124, name: '规范性文件审查'},
            {value: 340, name: '其他'},
        ];

        var apply_data_pie2 = [
            {value: 335, name: '公安行政管理-治安管理'},
            {value: 310, name: '公安行政管理-消防管理'},
            {value: 234, name: '公安行政管理-道路交通管理'},
            {value: 135, name: '公安行政管理-森林公安管理'},
            {value: 25, name: '公安行政管理-其他'},
            {value: 124, name: '资源行政管理-土地行政管理'},
            {value: 340, name: '资源行政管理-林业行政管理'},
        ];

        var apply_data_pie3 = [
            {value: 335, name: '不履行法定职责'},
            {value: 310, name: '行政处罚'},
            {value: 234, name: '行政强制'},
            {value: 135, name: '政务信息公开'},
            {value: 25, name: '投诉举报处理'},
            {value: 124, name: '行政裁决'},
            {value: 340, name: '行政确认'},
        ];

        var apply_data_title = ['变更', '撤销', '确认违法', '责任履行', '一并赔偿', '规范性文件审查', '其他'];
        var apply_data_value = [10, 40, 25, 90, 125, 16, 72];

        $("#apply-tab").append("<li><a href='#" + index + "' data-toggle='tab'>" + value.name + "</a></li>");
        $("#apply-info").append(applyInfo);
        //TODO 图表
        initEcharts4PieNest("apply_pie" + value.id, "行政管理类别及案由分析", "行政管理类别", apply_data_pie2, "案由", apply_data_pie3);
        initEcharts4Pie("apply_pie3" + value.id, "请求类别", "请求类别", apply_data_pie, false);
        /*initEcharts4Bar("apply_bar" + value.id, "案由数量", "案由数量", apply_data_title, apply_data_value, true);*/
        initEcharts4Pie("apply_pie2" + value.id, "受理结案状态数量", "受理结案状态数量", apply_data_pie, true);
        initEcharts4Bar("apply_bar2" + value.id, "当事人排名", "当事人排名", apply_data_title, apply_data_value, false);

    });
    $('#apply-tab li:first').addClass('active');
    $('#apply-info div:first').addClass('active');
}

//初始化添加当事人信息展示
function initPersonnelInfo(parentId, data) {
    $.each(data, function (index, value) {
        var $div = '';

        var otherName = "无";
        if (value.other_name != "" && value.other_name != null) {
            otherName = value.other_name;
        }

        var nature = "未知";
        if (value.nature != "" && value.nature != null) {
            nature = value.nature;
        }

        if (value.unit_name == "" || value.unit_name == null) {
            $div = "<div class='row'>" +
                "				<div class='col-lg-6'>" +
                "					<strong><i class='fa fa-user margin-r-5'></i>  英文名/其他语种名/化名/曾用名</strong>" +
                "					<p style='margin: 10px 0 0;'>" + otherName + "</p>" +
                "					<hr style='margin: 10px 0;'>" +
                "				</div>" +
                "               <div class='col-lg-6'>" +
                "                   <strong><i class='fa fa-user margin-r-5'></i>  民族</strong>" +
                "                   <p style='margin: 10px 0 0;'>" + nature + "</p>" +
                "                   <hr style='margin: 10px 0;'>" +
                "               </div>" +
                "				<div class='col-lg-6'>" +
                "					<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
                "					<p style='margin: 10px 0 0;'>" + value.domicile + "</p>" +
                "					<hr style='margin: 10px 0;'> " +
                "				</div>" +
                "				<div class='col-lg-12'>" +
                "					<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
                "					<p style='margin: 10px 0 0;'>" + value.abode + "</p>" +
                "					<hr style='margin: 10px 0;'>" +
                "				</div>" +
                "				<div class='col-lg-6'>" +
                "					<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-delete' data-id='" + value.id + "' data-del = '" + applyId + "'><i class='fa fa-trash-o'></i> 删除</button>" +
                "				</div>" +
                "				<div class='col-lg-6'>" +
                "					<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-agent' data-id='" + value.id + "' data-name='" + value.name + "' data-client='1'><i class='fa fa-plus'></i> 添加代理人</button>" +
                "				</div>" +
                "</div>";

        } else {
            $div = "<div class='row'>" +
                "				<div class='col-lg-6'>" +
                "					<strong><i class='fa fa-user margin-r-5'></i>  单位名称</strong>" +
                "					<p style='margin: 10px 0 0;'>" + value.unit_name + "</p>" +
                "					<hr style='margin: 10px 0;'>" +
                "				</div>" +
                "               <div class='col-lg-6'>" +
                "                   <strong><i class='fa fa-user margin-r-5'></i>  法人</strong>" +
                "                   <p style='margin: 10px 0 0;'>" + value.name + "</p>" +
                "                   <hr style='margin: 10px 0;'>" +
                "               </div>" +
                "				<div class='col-lg-6'>" +
                "					<strong><i class='fa fa-home margin-r-5'></i>  单位住址</strong>" +
                "					<p style='margin: 10px 0 0;'>" + value.unit_abode + "</p>" +
                "					<hr style='margin: 10px 0;'> " +
                "				</div>" +
                "				<div class='col-lg-12'>" +
                "					<strong><i class='fa fa-map-marker margin-r-5'></i>  单位联系方式</strong>" +
                "					<p style='margin: 10px 0 0;'>" + value.unit_contact + "</p>" +
                "					<hr style='margin: 10px 0;'>" +
                "				</div>" +
                "				<div class='col-lg-6'>" +
                "					<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-delete' data-id='" + value.id + "'><i class='fa fa-trash-o'></i> 删除</button>" +
                "				</div>" +
                "				<div class='col-lg-6'>" +
                "					<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-agent' data-id='" + value.id + "' data-name='" + value.unit_name + "' data-client='1'><i class='fa fa-plus'></i> 添加代理人</button>" +
                "				</div>" +
                "</div>";
        }

        $("#" + parentId).children().html($div);


    });
}

//初始化人员数据
function initData4Personnel(isTemp, parentId, childClassName, data, isReference) {

    isReference = isReference || "1";
    $("." + childClassName).remove();
    $.each(data, function (index, value) {
        // 被申请人
        if (typeof value.id_no === "undefined") {
            var last = "";
            // 临时
            if (isTemp) {
                last = "<div class='col-lg-12'>" +
                    "<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-delete' data-id='" + value.id + "'><i class='fa fa-trash-o'></i> 删除</button>" +
                    "</div>";
            } else { // 正式
                // 带研判参考
                var i = 0;
                if (isReference == "1") {
                    last = "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-user margin-r-5'></i>  委托代理人</strong>" +
                        "<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-clipboard margin-r-5'></i>  涉案总数</strong>" +
                        "<p style='margin: 10px 0 0;'>" + value.defendant_cases_number + "</p>" +
                        "</div>" +
                        "<div class='col-lg-12'><div class='row'>" +
                        "<div  id='defendant-pie' style='width:380px;height: 300px;margin:0;float: left'>1</div>" +
                        "<div  id='defendant-bar' style='width:380px;height: 300px;margin:0;float: right'>2</div>" +
                        "<div  id='defendant-pie2' style='width:380px;height: 300px;margin:0;float: left'>3</div>" +
                        "<div  id='defendant-bar2' style='width:380px;height: 300px;margin:0;float: right'>4</div>" +
                        "</div></div>";
                } else { // 不带研判参考
                    last = "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-user margin-r-5'></i>  委托代理人1</strong>" +
                        "<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
                        "</div>";
                }
            }

            var file1 = "&nbsp;";
            if (value.file_name_1 != null && value.file_name_1 != "") {
                file1 = "<a class='btn bg-gray as-btn' href='" + value.file_1 + "' target='_blank'><i class='fa fa-file-image-o'></i> " + value.file_name_1 + "</a>";
            }
            var file2 = "";
            if (value.file_name_2 != null && value.file_name_2 != "") {
                file2 = "<a class='btn bg-gray as-btn' href='" + value.file_2 + "' target='_blank'><i class='fa fa-file-image-o'></i> " + value.file_name_2 + "</a>";
            }
            var file3 = "";
            if (value.file_name_3 != null && value.file_name_3 != "") {
                file3 = "<a class='btn bg-gray as-btn' href='" + value.file_3 + "' target='_blank'><i class='fa fa-file-image-o'></i> " + value.file_name_3 + "</a>";
            }

            if (value.name != null && value.name != "") {
                $('#defendant-tab').append("<li><a href='#" + value.id + "' data-toggle='tab' role='tab' aria-expanded='true'>" + value.name + "</a></li>");
            }

            var $div = "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' class='" + childClassName + "' id='" + value.id + "'>" +
                "<div class='row'>" +
                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-bank margin-r-5'></i>  被申请人名称</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.name + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-balance-scale margin-r-5'></i>  被申请人类型</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.label_type + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-black-tie margin-r-5'></i>  法定代表人</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.name + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-black-tie margin-r-5'></i>  职务</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.duty + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-12'>" +
                "<strong><i class='fa fa-map-marker margin-r-5'></i>  被申请人住所</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.unit_abode + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +


                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-book margin-r-5'></i>  行政主体证明</strong>" +
                "<p style='margin: 10px 0 0;'>" + file1 + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-6'>" +
                "<strong><i class='fa fa-book margin-r-5'></i>  单位联系方式</strong>" +
                "<p style='margin: 10px 0 0;'>" + value.unit_contact + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                last +
                "</div></div>";
            if (value.name != null && value.name != "") {
                $('#defendant-info').append("<div class='tab-pane' id='" + value.id + "'>" + $div + "</div>");
            }

            if (!isTemp) {
                if (value.defendant_cases_number != null && value.defendant_cases_number > 0 && isReference == "1") {
                    if (value.name != null && value.name != "") {
                        var defendant_data_pie = [
                            {value: 335, name: '变更'},
                            {value: 310, name: '撤销'},
                            {value: 234, name: '确认违法'},
                            {value: 135, name: '责任履行'},
                            {value: 25, name: '一并赔偿'},
                            {value: 124, name: '规范性文件审查'},
                            {value: 340, name: '其他'},
                        ];

                        var defendant_data_title = ['变更', '撤销', '确认违法', '责任履行', '一并赔偿', '规范性文件审查', '其他'];
                        var defendant_data_value = [10, 40, 25, 90, 125, 16, 72];

                        initEcharts4Pie("defendant-pie", "请求类别", "请求类别", defendant_data_pie, false);
                        initEcharts4Bar("defendant-bar", "案由数量", "案由数量", defendant_data_title, defendant_data_value, true);
                        initEcharts4Pie("defendant-pie2", "受理结案状态数量", "受理结案状态数量", defendant_data_pie, true);
                        initEcharts4Bar("defendant-bar2", "当事人排名", "当事人排名", defendant_data_title, defendant_data_value, false);
                    }
                }

                if (value.agent_list != null && value.agent_list.length > 0) {
                    $.each(value.agent_list, function (index1, value1) {
                        $child = $("<button class='btn btn-default bg-gray as-btn' data-toggle='modal' data-target='#agentViewModal' data-temp='0' " +
                            "data-id='" + value1.id + "' data-idfilenames='" + value1.agent_id_file_names + "' data-idfiles='" + value1.agent_id_files + "' " +
                            "data-agentfile='" + value1.agent_file + "' data-agentfilename='" + value1.agent_file_name + "' " +
                            "data-name='" + value1.name + "' data-unit_name='" + value1.unit_name + "' data-gender='" + value1.label_gender + "' data-birthday='" + value1.birthday + "' " +
                            "data-phone='" + value1.phone + "' data-contact='" + value1.contact + "' data-address='" + value1.province + value1.city + value1.county + value1.address + "' id='" + value1.id + "'><i class='fa fa-user'></i> " + value1.name + "</button>");
                        $("#personnel_agent_" + value.id).append($child);
                    })
                }
            }
        } else { // 添加申请人 第三人点击按钮展示人员信息模块
            var dName = "";
            var dLast = "";

            var domicile = "&nbsp;"
            if (value.domicile != null && value.domicile != "") {
                domicile = value.domicile;
            }
            var birthday = "&nbsp;"
            if (value.birthday != null && value.birthday != "") {
                birthday = value.birthday;
            }
            var contact = "&nbsp;"
            if (value.contact != null && value.contact != "") {
                contact = value.contact;
            }
            var phone = "&nbsp;"
            if (value.phone != null && value.phone != "") {
                phone = value.phone;
            }
            var nature = "&nbsp;"
            if (value.nature != null && value.nature != "") {
                nature = value.nature;
            }

            var otherName = "&nbsp;"
            if (value.other_name != null && value.other_name != "") {
                otherName = value.other_name;
            }
            if (value.type == "1") {
                dName =
                    "<div class='col-lg-6'>" +
                    "<strong><i class='fa fa-user margin-r-5'></i>  5英文名/其他语种名/化名/曾用名</strong>" +
                    "<p style='margin: 10px 0 0;'>" + otherName + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    "<div class='col-lg-6'>" +
                    "<strong><i class='fa fa-user margin-r-5'></i>  民族</strong>" +
                    "<p style='margin: 10px 0 0;'>" + nature + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    "<div class='col-lg-6'>" +
                    "<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
                    "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>";

                dLast += "<div class='col-lg-12'>" +
                    "<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
                    "<p style='margin: 10px 0 0;'>" + value.abode + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>";

            } else if (value.type == "2") {

                var labelUnitIdType = "&nbsp;";
                if (value.label_unit_id_type != null && value.label_unit_id_type != "") {
                    labelUnitIdType = value.label_unit_id_type;
                }
                var unitIdNo = "&nbsp;";
                if (value.unit_id_no != null && value.unit_id_no != "") {
                    unitIdNo = value.unit_id_no;
                }
                var unitContact = "&nbsp;";
                if (value.unit_contact != null && value.unit_contact != "") {
                    unitContact = value.unit_contact;
                }
                var unitName = "&nbsp;";
                if (value.unit_name != null && value.unit_name != "") {
                    unitName = value.unit_name;
                }

                var unitAbode = "&nbsp;";
                if (value.unit_abode != null && value.unit_abode != "") {
                    unitAbode = value.unit_abode;
                }

                dName = "<div class='col-lg-4' style='margin-top:20px;'>" +
                    "<strong><i class='fa fa-briefcase margin-r-5'></i>  单位名称</strong>" +
                    "<p style='margin: 10px 0 0;'>" + unitName + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    '<div class="col-lg-4" style="margin-top:20px;">' +
                    '<strong><i class="fa fa-briefcase margin-r-5"></i>证件类型</strong>' +
                    "<p style='margin: 10px 0 0;'>" + value.label_id_type + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    '<div class="col-lg-4" style="margin-top:20px;">' +
                    '<strong><i class="fa fa-briefcase margin-r-5"></i>证件号码</strong>' +
                    "<p style='margin: 10px 0 0;'>" + value.id_no + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    "<div class='col-lg-4'>" +
                    "<strong><i class='fa fa-phone margin-r-5'></i>  单位联系方式</strong>" +
                    "<p style='margin: 10px 0 0;'>" + unitContact + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    "<div class='col-lg-4'>" +
                    "<strong><i class='fa fa-phone margin-r-5'></i>  单位住所</strong>" +
                    "<p style='margin: 10px 0 0;'>" + unitAbode + "</p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>";

                var legalPersonName = ""
                if (value.name != null && value.name != "") {
                    legalPersonName = "<div class='col-lg-4'>" +
                        "<strong><i class='fa fa-phone margin-r-5'></i>  法定代表人</strong>" +
                        "<p style='margin: 10px 0 0;'>" + value.name + "</p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>";
                    dName += legalPersonName;
                }
            }
            if (isTemp) {
                dLast += "<div class='col-lg-12'>" +
                    "<strong><i class='fa fa-map-marker margin-r-5'></i>  委托代理人2</strong>" +
                    "<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
                    "<hr style='margin: 10px 0;'>" +
                    "</div>" +
                    "<div class='col-lg-6'>" +
                    "<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-delete' data-id='" + value.id + "'><i class='fa fa-trash-o'></i> 删除</button>" +
                    "</div>" +
                    "<div class='col-lg-6'>" +
                    "<button class='btn btn-block btn-skin " + btnSkin + " btn-personnel-agent' data-id='" + value.id + "' data-name='" + value.name + "' data-client='1'><i class='fa fa-plus'></i> 添加代理人</button>" +
                    "</div>";
            } else {
                if (isReference == "1") {
                    dLast += "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-map-marker margin-r-5'></i>  委托代理人</strong>" +
                        "<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-folder-open margin-r-5'></i>  相关案件</strong>" +
                        "<div style='margin: 10px 0 0;line-height: 2rem;' id='personnel_cases_" + value.id + "'></div>" +
                        "</div>" +
                        "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-clipboard margin-r-5'></i>  涉案分析</strong>" +
                        "<div class='row'>" +
                        "<!-- 左上开始 --><div class='col-lg-6'>" +
                        "<div id='apply-pie' style='height: 400px'>1</div>" +
                        "</div><!-- 左上结束 -->" +
                        "<!-- 右上开始 --><div class='col-lg-6'>" +
                        "<div id='apply-bar' style='height: 400px'>2</div>" +
                        "</div><!-- 右上结束 -->" +
                        "<!-- 左下开始 --><div class='col-lg-6'>" +
                        "<div id='apply-pie2' style='height: 400px'>3</div>" +
                        "</div><!-- 左下结束 -->" +
                        "<!-- 右下开始 --><div class='col-lg-6'>" +
                        "<div id='apply-bar2' style='height: 400px'>4</div>" +
                        "</div><!-- 右下结束 -->" +
                        "</div>" +
                        "</div>";


                } else {
                    dLast += "<div class='col-lg-12'>" +
                        "<strong><i class='fa fa-map-marker margin-r-5'></i>  委托代理人</strong>" +
                        "<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
                        "</div>";
                }
            }
            var gender = "&nbsp;";
            if (value.gender == "1") {
                gender = "男";
            } else if (value.gender == "2") {
                gender = "女";
            }

            var $div = "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' class='" + childClassName + "' id='" + value.id + "'>" +
                "<div class='row'>" +
                dName +
                "<div class='col-lg-2'>" +
                "<strong><i class='fa fa-intersex margin-r-5'></i>  性别</strong>" +
                "<p style='margin: 10px 0 0;'>" + gender + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-3'>" +
                "<strong><i class='fa fa-calendar margin-r-5'></i>  出生日期</strong>" +
                "<p style='margin: 10px 0 0;'>" + birthday + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-3'>" +
                "<strong><i class='fa fa-mobile margin-r-5'></i>  手机号码</strong>" +
                "<p style='margin: 10px 0 0;'>" + phone + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                "<div class='col-lg-4'>" +
                "<strong><i class='fa fa-phone margin-r-5'></i>  其他联系方式</strong>" +
                "<p style='margin: 10px 0 0;'>" + contact + "</p>" +
                "<hr style='margin: 10px 0;'>" +
                "</div>" +
                dLast +
                "</div>" +
                "</div>";
            $("#" + parentId).append($div);

            if (!isTemp && typeof value.type !== "undefined" && isReference == "1") {
                if (value.cases_list != null && value.cases_list.length > 0) {
                    $.each(value.cases_list, function (index1, value1) {
                        $child = $("<a class='btn bg-gray as-btn' href='audit_case.html?view=1&cases=" + value1.id + "'><i class='fa fa-folder-o'></i> " + value1.title + "</a>");
                        $("#personnel_cases_" + value.id).append($child);
                    })
                }
            }
            if (value.agent_list != null && value.agent_list.length > 0) {
                $.each(value.agent_list, function (index1, value1) {
                    var temp = "0";
                    if (isTemp) {
                        temp = "1";
                    }
                    $child = $("<button class='btn btn-default bg-gray as-btn' data-toggle='modal' data-target='#agentViewModal' data-temp='" + temp + "' " +
                        "data-id='" + value1.id + "' data-idfilenames='" + value1.agent_id_file_names + "' data-idfiles='" + value1.agent_id_files + "' " +
                        "data-agentfile='" + value1.agent_file + "' data-agentfilename='" + value1.agent_file_name + "' " +
                        "data-name='" + value1.name + "' data-unit_name='" + value1.unit_name + "' data-gender='" + value1.label_gender + "' data-birthday='" + value1.birthday + "' " +
                        "data-phone='" + value1.phone + "' data-contact='" + value1.contact + "' data-address='" + value1.province + value1.city + value1.county + value1.address + "' id='" + value1.id + "'><i class='fa fa-user'></i> " + value1.name + "</button>");
                    $("#personnel_agent_" + value.id).append($child);
                })
            }
        }
    })
    $('#defendant-tab li:first').addClass('active');
    $('#defendant-info div:first').addClass('active');
}

//初始化文书数据
function initData4PersonnelFont(parentId, childClassName, data) {
    $(".childClassName").remove();
    $.each(data, function (index, value) {
        if (typeof value.id_no === 'undefined') {
            var $font = $("<div class='" + childClassName + "' style='text-indent: 2em;' id='font_" + value.id + "'>被申请人：" +
                value.name + "。</div>");
            $("#" + parentId).append($font);
            return;
        }
        var title = "";
        var name = "";
        var unit_name = "";
        if (typeof value.type !== 'undefined') {

            title = "申请人：";
            var genderVal = "";
            if (value.gender == "1") {
                genderVal = "男";
            } else {
                genderVal = "女";
            }
            if (value.type == "1") {
                name = value.name + "，" + genderVal + "，";
            } else if (value.type == "2") {
                var legalPersonType = "";
                if (value.legal_person_type == "1") {
                    legalPersonType = "法定代表人";
                } else {
                    legalPersonType = "负责人";
                }
                name = value.unit_name + "。 <p style='text-indent: 2em;'>住&nbsp;&nbsp;所：" + value.unit_abode + "。</p><p style='text-indent: 2em;'> " + legalPersonType + ":" + value.name + "</p>";
            }
        } else {
            title = "委托代理人：";
            name = value.name;
            unit_name = "，工作单位，" + value.unit_name;
        }

        var agent_cases_type = $("#font_cases_type").html();
        var apply_legal_person = getValues("apply_legal_person");

        if (addApplyType == "1") {
            var $font = "";
            $font = $("<div class='" + childClassName + "' style='text-indent: 2em;' id='font_" + value.id + "'>" + title +
                name + value.birthday.substring(0, 4) + "年" + value.birthday.substring(5, 7) + "月" + value.birthday.substring(8, 10) + "日出生1，户籍所在地" + value.domicile + "。</div>");

            $("#" + parentId).append($font);
        } else {
            var $font = $("<div class='" + childClassName + "' style='text-indent: 2em;' id='font_" + value.id + "'>申请人：" + name + "</div>");
            $("#" + parentId).append($font);
        }
        if (agent_cases_type.indexOf("其他申请行政复议") != -1) {
            var third_party_type = $('input:radio[name="apply_type"]:checked').val();
            if (third_party_type == "2") {
                var apply_name = getValues("apply_name");
                var apply_address = getValues("apply_address");
                var fzrname = $("#label_third_party_legal_person").val();
                var zjcontent = "<div class='" + childClassName + "' style='text-indent: 2em;' id='font_" + value.id + "'>申请人：" + apply_name + ",地址" + apply_address + ",负责人：" + fzrname + "。</div>";
                $("#" + parentId).append(zjcontent);
            } else {
                $font = $("<div class='" + childClassName + "' style='text-indent: 2em;' id='font_" + value.id + "'>" + title +
                    name + "，" + value.label_gender + "，（" + value.nature + "）" + value.nature + "族，" + value.birthday.substring(0, 4) + "年" + value.birthday.substring(5, 7) + "月" + value.birthday.substring(8, 10) + "日出生，户籍所在地" + value.domicile + "。</div>");
                $("#" + parentId).append($font);
            }
        }
    })
}


//删除当事人按钮事件
//getCasesPersonnelList(isTemp, loadingId, casesId, casesPersonnelType, isReference)
function onDelete4Personel(casesId) {
    $(".btn-personnel-delete").off().on("click", function () {
        var agent = $(this).data("agent");

        var id = $(this).data("id");

        var btnId = $(this).parent().parent().parent().parent().attr('id');
        var newBtnId = btnId.substring(4, btnId.length);


        var delId = $(this).data("del");
        if (agent != undefined) {
            $("#" + id).remove();
            $("#font_" + id).remove();
            $("#font_" + agentId).remove();
        } else {
            $("#" + newBtnId).remove();
            $("#" + btnId).remove();
            $("#" + id).remove();
            $("#font_" + id).remove();
        }
        $.ajax({
            url: "cases_personnel_temp/delete",
            type: "post",
            dataType: "json",
            data: {
                id: id
            },
            success: function (data) {
                if (data.result != 1) {
                    showMsg(data.error_msg);
                    return;
                }

                if (agent == "1") {
                    $("#agentViewModal").modal("hide");
                }
            },
            error: function (xhr, type) {
                showMsg("系统繁忙");
            }
        });
    })

    //添加代理人按钮事件
    $(".btn-personnel-agent").off().on("click", function () {
        client = $(this).data("client");
        clientId = $(this).data("id");
        initModal4Agent(casesId);
        $("#modal_add_agent").modal("show");
    })
}

//TODO 获取案件的申请人
function getPersonnelInfoList(loadingId, casesId, casesPersonnelType) {
    $("#" + loadingId).show();
    $.ajax({
        url: "cases_personnel/get_cases_apply?personnelType=" + casesPersonnelType + "&casesId=" + casesId,
        type: "post",
        dataType: "json",
        success: function (data) {
            $("#" + loadingId).hide();
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            if (data.data == null || data.data.length == 0) {
                return;
            }
            initData4ApplyWithReference(data);
        },
        error: function (xhr, type) {
            $("#" + loadingId).hide();
            showMsg("系统繁忙");
        }
    });
}


function getCasesPersonnelList(isTemp, loadingId, casesId, casesPersonnelType, isReference, id) {
    isReference = isReference || "1";
    $("#" + loadingId).show();
    var url = "";
    if (isTemp) {
        url = "cases_personnel_temp/get_list?type=" + casesPersonnelType + "&cases_id=" + casesId;
    } else {
        url = "cases_personnel/get_list?type=" + casesPersonnelType + "&cases_id=" + casesId;
    }
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        success: function (data) {
            $("#" + loadingId).hide();
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            if (data.data == null || data.data.length == 0) {
                return;
            }
            var element = initElement4Row(casesPersonnelType);
            /*function initData4Personnel(isTemp, parentId, childClassName, data, isReference);*/
            initPersonnelInfo("row_" + id, data.data);
            initData4Personnel(isTemp, "row_" + element, element, data.data, isReference);
            if (isTemp) {
                initData4PersonnelFont("font_" + element, element, data.data);
                onDelete4Personel(casesId);
            }
        },
        error: function (xhr, type) {
            $("#" + loadingId).hide();
            showMsg("系统繁忙");
        }
    });
}


/*function getCasesPersonnelList2(isTemp, loadingId, casesId, casesPersonnelType, isReference,newId) {

	isReference = isReference || "1";
	$("#" + loadingId).show();
	var url = "";
	if(isTemp) {
		url = "cases_personnel_temp/get_list?type=" + casesPersonnelType + "&cases_id=" + casesId;
	} else {
		url = "cases_personnel/get_list?type=" + casesPersonnelType + "&cases_id=" + casesId;
	}

	$.ajax({
		url: url,
		type: "post",
		dataType: "json",
		success: function(data) {
			$("#" + loadingId).hide();
			if(data.result != 1) {
				showMsg(data.error_msg);
				return;
			}

			if(data.data == null || data.data.length == 0) {
				return;
			}
			var element = initElement4Row(casesPersonnelType);
			initData4Personnel(isTemp, newId, newId, data.data, isReference);
			if(isTemp) {
				initData4PersonnelFont("font_" + element, element, data.data);
				onDelete4Personel(casesId,isTemp, loadingId,casesPersonnelType, isReference);
			}
		},
		error: function(xhr, type) {
			$("#" + loadingId).hide();
			showMsg("系统繁忙");
		}
	});
}*/


function savePersonnel(isTemp, isNext, saveLoadingId, getLoadingId, casesId, casesPersonnelType, isReload) {
    isReload = isReload || false;
    $("#" + saveLoadingId).show();
    var url = "";
    var data = {};
    var name = $("#apply_name").val();
    var third_party_name = $("#third_party_name").val();
    var defendant_name = $("#defendant_unit_name").val();

    //申请人
    if (casesPersonnelType == "1") {
        url = "party/save";

        data = {
            id: applyId,
            cases_id: casesId,
            personnel_id: $("#apply_personnel_id").val(),
            type: addApplyType,
            name: name,
            other_name: $("#other_name").val(),
            nature: $("#apply_nature").val(),
            gender: $("input[name='apply_gender']:checked").val(),
            birthday: $("#apply_birthday").val(),
            id_type_id: $("#apply_id_type").val(),
            id_no: $("#apply_id_no").val(),
            phone: $("#apply_phone").val(),
            domicile: $("#apply_domicile").val(),
            zip_code: $("#apply_zip_code").val(),
            contact: $("#apply_contact").val(),
            abode: $("#abode").val(),

            unit_name: $("#apply_unit_name").val(),
            unit_contact: $("#apply_unit_contact").val(),
            unit_id_type_id: $("#apply_unit_id_type").val(),
            unit_id_no: $("#apply_unit_id_no").val(),
            unit_abode: $("#apply_unit_abode").val(),
            legal_person_type: $("input[name='Legal_person']:checked").val()
        }
        //TODO 保存被申请人至案件当事人
    } else if (casesPersonnelType == "2") {
        url = "defendant/save";
        data = {
            id: defendantId,
            cases_id: casesId,
            personnel_id: $("#defendant_personnel_id").val(),
            type: $("#defendant_type").val(),
            unit_name: $("#defendant_unit_name").val(),
            unit_abode: $("#defendant_unit_abode").val(),
            unit_content: $("#defendant_unit_content").val(),
            name: $("#defendant_name").val(),
            legal_person_type: $("input[name='defendant_legal_person_type']:checked").val(),
            duty: $("#defendant_duty").val()
        }
        //保存正式第三人至当事人
    } else if (casesPersonnelType == "3") {
        url = "third_party/save";
        data = {
            id: thirdPartyId,
            cases_id: casesId,
            cases_personnel_type: casesPersonnelType,
            personnel_id: $("#third_party_personnel_id").val(),
            type: addApplyType,
            name: $("#third_party_name").val(),
            legal_person: $("#third_party_legal_person").val(),
            gender: $("input[name='third_party_gender']:checked").val(),
            birthday: $("#third_party_birthday").val(),
            phone: $("#third_party_phone").val(),
            contact: $("#third_party_contact").val(),
            zip_code: $("#third_party_zip_code").val(),
            province_id: $("#third_party_province").val(),
            city_id: $("#third_party_city").val(),
            county_id: $("#third_party_county").val(),
            address: $("#third_party_address").val(),
            id_type_id: $("#third_party_id_type").val(),
            id_no: $("#third_party_id_no").val(),
            unit_id_type_id: $("#third_party_unit_id_type").val(),
            unit_id_no: $("#third_party_unit_id_no").val(),
            unit_contact: $("#third_party_unit_contact").val(),
            domicile: $("#third_party_domicile").val(),
            nature: $("#third_party_nature").val(),
            ws_address: $("#apply_ws_address").val(),
        }

        //保存建议第三人至当事人
    } else if (casesPersonnelType == "7") {

        url = "third_party/save";
        data = {
            id: thirdPartyId,
            cases_id: casesId,
            cases_personnel_type: "7",
            personnel_id: $("#third_party_personnel_id").val(),
            type: $("input[name='apply_third_party_type']:checked").val(),
            unit_name: $("#third_party_unit_name").val(),
            unit_content: $("#third_party_unit_contact").val(),
            unit_id_type: $("#third_party_unit_id_type").val(),
            unit_id_no: $("#third_party_unit_id_no").val(),
            unit_abode: $("#third_party_unit_abode").val(),
            legal_person_type: $("input[name='third_party_Legal_person']:checked").val(),
            name: $("#third_party_name").val(),
            other_name: $("#third_party_other_name").val(),
            nature: $("#third_party_nature").val(),
            gender: $("input[name='third_party_gender']:checked").val(),
            birthday: $("#third_party_birthday").val(),
            id_type: $("#third_party_id_type").val(),
            id_no: $("#third_party_id_no").val(),
            phone: $("#third_party_phone").val(),
            domicile: $("#third_party_domicile").val(),
            zip_code: $("#third_party_zip_code").val(),
            content: $("#third_party_contact").val(),
            abode: $("#third_party_abode").val()
        }
        //TODO 保存代理人
    } else {
        url = "agent/save";
        data = {
            id: agentId,
            cases_id: casesId,
            client_id: $("#agent_client").val(),
            personnel_id: $("#agent_personnel_id").val(),
            name: $("#agent_name").val(),
            nature: $("#agent_nature").val(),
            gender: $("input[name='agent_gender']:checked").val(),
            birthday: $("#agent_birthday").val(),
            idTypeId: $("#agent_id_type").val(),
            idNo: $("#agent_id_no").val(),
            phone: $("#agent_phone").val(),
            abode: $("#agent_address").val(),
            unit_name: $("#agent_unit_name").val(),
            identity: $("#agent_identity").val(),
            kinsfolk: $("#agent_kinsfolk").val(),
            type: $("#agent_type").val(),
        }

        //文书预览委托代理人信息
        var agent_cases_type = $("#font_cases_type").html();
        var agent_name = $("#agent_name").val();
        var agent_birthday = $("#agent_birthday").val();
        var agent_gender = $("input[name='agent_gender']:checked").val();
        var agent_address = $("#agent_address").val();
        var agent_unit_name = $("#agent_unit_name").val();
        var agent_content = $("#font_apply_agent").html();
        var agent_identity = $("#agent_identity option:checked").text();

        var content = "";

        //委托代理人文书
        if (agentTypeVal == "1") {
            content += "<div  style='text-indent: 2em;' id='font_" + agentId + "'>委托代理人：" + agent_name + "," + (agent_gender == '1' ? '男' : '女') + "," + agent_birthday.substring(0, 4);
            content += "年" + agent_birthday.substring(5, 7) + "月" + agent_birthday.substring(8, 10) + "日出生，户籍所在地" + (agent_address == '' ? "未知" : agent_address) + "。<br/></div>";
        } else if (agentTypeVal == "2") {
            content += "<div  style='text-indent: 2em;' id='font_" + agentId + "'>委托代理人：" + agent_name + "," + agent_unit_name + agent_identity + "。<br/></div>";
        } else if (agentTypeVal == "3") {
            content += "<div style='text-indent: 2em;' id='font_" + agentId + "'>委托代理人：" + agent_name + ", 该单位工作人员。<br/></div>";
        } else if (agentTypeVal == "4") {
            content += "<div  style='text-indent: 2em;' id='font_" + agentId + "'>委托代理人：" + agent_name + "," + (agent_gender == '1' ? '男' : '女') + "," + agent_birthday.substring(0, 4);
            content += "年" + agent_birthday.substring(5, 7) + "月" + agent_birthday.substring(8, 10) + "日出生，户籍所在地" + (agent_address == '' ? "未知" : agent_address) + "。<br/></div>";
        }

        if (agent_content != "") {
            agent_content += content;
        } else {
            agent_content = content;
            //$("#font_apply_agent").append(content);
        }

        // if (agent_cases_type.indexOf("个人") != -1 || agent_cases_type.indexOf("法人组织") != -1) {
        //     $("#font_apply_agent").html("");
        //     $("#font_apply_agent").append(agent_content);
        // }
    }
    $.ajax({
        url: url,
        type: "post",
        dataType: "json",
        data: data,
        success: function (result) {
            // ajax回调后就关闭加载框
            $("#" + saveLoadingId).hide();
            if (result.result == 1) {
                // 成功回调关闭模态框
                $("#loading_apply").hide();
                //追加当事人按钮
                if (casesPersonnelType == "1") {
                    $("#" + saveLoadingId).hide();
                    $("#append_apply_btn").append("<a class='btn btn-xs btn-primary' role='button' id='" + applyId + "' data-toggle='collapse' href='#row_" + applyId + "' aria-expanded='false' aria-controls='row_" + applyId + "'>" + name + "</a> ");
                    $("#apply_info").append("<div class='collapse' id='row_" + applyId + "'>" +
                        "                        <div class='well'></div>" +
                        "                    </div>");

                } else if (casesPersonnelType == "7") {
                    var oldId = $("#addThirdPersonDiv div:last-child").attr("id");
                    var num = Number(oldId.charAt(oldId.length - 1)) + 1;
                    var newId = "row_third_party_" + num;
                    $("#" + saveLoadingId).hide();
                    $("#thirdpartyappend_element_btn").append("<button class='btn btn-xs btn-primary btn-skin btn-space' type='button' data-toggle='collapse' data-target='#" + newId + "' aria-expanded='false' aria-controls='" + newId + "'>" + third_party_name + "</button>");
                    $("#addThirdPersonDiv").append("<div class='collapsing' style='padding-bottom: 0px;' id='" + newId + "'></div>");
                } else if (casesPersonnelType == "2") {
                    $("#append_defendant_btn").append("<a class='btn btn-xs btn-primary' role='button' id='" + defendantId + "' data-toggle='collapse' href='#row_" + defendantId + "' aria-expanded='false' aria-controls='row_" + defendantId + "'>" + defendant_name + "</a> ");
                    $("#defendant_info").append("<div class='collapse' id='row_" + defendantId + "'>" +
                        "                        <div class='well'></div>" +
                        "                    </div>");
                }
            }
            if (result.result != 1) {
                // 失败回调显示错误信息，但不关闭模态框
                showMsg(result.error_msg);
                return true;
            }

            var element = initModal(casesPersonnelType);
            if (isNext) {
                if (casesPersonnelType == "1") {
                    clearModal4Apply();
                } else if (casesPersonnelType == "2") {
                    clearModal4Defendant();
                } else if (casesPersonnelType == "3" || casesPersonnelType == "7") {
                    clearModal4ThirdParty();
                } else if (casesPersonnelType == "73") {
                    clearModal4ApplyThirdParty();
                } else {
                    clearModal4Agent();
                }
            } else {
                $('#modal_add_' + element).modal("hide");
                $('#modal_add_agent2').modal("hide");
            }


            if (casesPersonnelType == "4") {
                /*getCasesPersonnelList(isTemp, loadingId, casesId, casesPersonnelType, isReference)*/
                getCasesPersonnelList(isTemp, getLoadingId, casesId, "1");
            } else if (casesPersonnelType == "5") {
                getCasesPersonnelList(isTemp, getLoadingId, casesId, "3");
            } else if (casesPersonnelType == "6") {
                getCasesPersonnelList(isTemp, getLoadingId, casesId, "2");
            } else {
                getCasesPersonnelList(isTemp, getLoadingId, casesId, casesPersonnelType, null, data.id);
            }

            if (isReload) {
                getCases("loading_info", casesId, true, true, true);
            }
        },
        error: function (xhr, type) {
            $("#" + saveLoadingId).hide();
            showMsg("系统繁忙");
        }
    });
}

function getValues(name) {
    var s = document.getElementsByName(name);
    var val = '';
    for (var i = 0; i < s.length; i++) {
        if (s[i].value != '' && s[i].value != '-1') {
            val = s[i].value;
        }
    }
    return val;
}

var casesType = "";

//初始化案件类型，并根据申请人类型展示不同表单及高亮表单
function initCasesType() {
    $(".div-cases-type").hide();
    $(".div-info").show();
    $("#arrow_left").show();
    if (casesType == "1") {
        $("#font_cases_type").html("个人申请行政复议");
        $("#col_no").show();
        $("#col_no_more").hide();

        $(".case-type-2").hide();
        $("#blend_type_radio").hide();
        $("#person-radio").attr("checked", "checked");
        $("#legal-radio").removeAttr("checked");
        $(".form-unit").addClass("hidden");
        $(".form-third-unit").addClass("hidden");
        $(".person-needs").addClass("has-error");
    } else if (casesType == "2") {
        $("#font_cases_type").html("多人申请行政复议");
        $("#col_no").hide();
        $("#col_no_more").show();

        $(".case-type-2").show();
        $("#blend_type_radio").hide();
        $("#person-radio").attr("checked", "checked");
        $("#legal-radio").removeAttr("checked");
        $(".form-unit").addClass("hidden");
        $(".form-third-unit").addClass("hidden");
        $(".person-needs").addClass("has-error");
    } else if (casesType == "3") {
        $("#font_cases_type").html("法人组织申请行政复议");
        $("#col_no").show();
        $("#col_no_more").hide();
        $(".case-type-2").hide();
        $("#blend_type_radio").hide();
        $("#legal-radio").attr("checked", "checked");
        $("#person-radio").removeAttr("checked");
        $(".form-unit").removeClass("hidden");
        $(".form-third-unit").removeClass("hidden");
        $(".legal-needs").addClass("has-error");
    } else {
        $("#font_cases_type").html("其他申请行政复议");
        $("#col_no").hide();
        $("#col_no_more").show();
        //其他申请行政复议表头备注
        $("#modal_add_apply_title").append("<br/><span id='modal_add_apply_mark_word' style='color:#DB192B;font-size:10px'>*	请注意录入顺序应当以一名或多名申请人在前，一个或多个法人或其他组织在后</span>");

        $(".case-type-2").hide();
        $("#blend_type_radio").show();
        $(".form-unit").addClass("hidden");
        $(".form-third-unit").addClass("hidden");
        $(".person-needs").addClass("has-error");
        $("#person-radio").on("ifChecked", function () {
            $(".form-unit").addClass("hidden");
            $(".legal-needs").removeClass("has-error");
            $(".person-needs").addClass("has-error");
            $("#label_apply_name").text("姓名");
        });
        $("#legal-radio").on("ifChecked", function () {
            $(".form-unit").removeClass("hidden");
            $(".person-needs").removeClass("has-error");
            $(".legal-needs").addClass("has-error");
            $("#label_apply_name").text("法定代表人");
        });
    }
}

var applyId = "";
var addApplyType = "";//录入人员类型 1：个人 2：法人组织

//清除申请人表单
function clearModal4Apply() {
    $("#search_apply").select2("val", "");

    $("#apply_personnel_id").val(null);

    $("#apply_unit_name").val(null);
    $("#apply_unit_contact").val(null);
    $("#apply_unit_id_type").val("-1").trigger("change");

    $("#apply_unit_id_no").val(null);
    $("#apply_unit_abode").val(null);
    $("input[name='Legal_person']:first").iCheck("check");

    $("#apply_name").val(null);
    $("#other_name").val(null);
    $("#apply_nature").val(null);
    $("input[name='apply_gender']:first").iCheck("check");
    $("#apply_birthday").val(null);
    $("#apply_id_type").val("-1").trigger("change");
    $("#apply_id_no").val(null);
    $("#apply_phone").val(null);
    $("#apply_domicile").val(null);
    $("#apply_zip_code").val(null);
    $("#apply_contact").val(null);
    $("#abode").val(null);

    $(".li-apply-file").remove();

    applyId = getActId();
    initUpload("loading_apply", "apply_file", "li-apply-file", "temp/img?res=" + applyId);
}

//初始化添加申请人模态框
function initModal4Apply() {
    //初始化搜索框
    initSelect4Ajax("search_apply", "party/get_choose_list?type=" + addApplyType);
    //初始化单位类型下拉框
    loadData4Select2("apply_unit_id_type", "unit_id_type/get_choose_list");
    //初始化日期选取框
    initDatetimePicker("apply_birthday");
    //初始化城市选择
    loadData4Select2("apply_province", "region/get_choose_list_4_parent");
    // 注册城市切换监听
    onChange4Province("apply_province", "apply_city", "region/get_choose_list_by_parent", "apply_county");
    onChange4City("apply_city", "apply_county", "region/get_choose_list_by_parent");
    //初始化申请人证件类型下拉框
    loadData4Select2("apply_id_type", "id_type/get_choose_list");

    //清空模态框
    clearModal4Apply();
}

//初始化添加人员表单
function initModalEvent4Apply() {
    $("#btn_clear_apply").on("click", function () {
        clearModal4Apply();
    });

    $("#search_apply").on("select2:select", function (e) {
        $("#apply_personnel_id").val(e.params.data.id);
        $("#apply_type_" + e.params.data.type).iCheck("check");

        //个人
        $("#apply_name").val(e.params.data.name);
        $("#other_name").val(e.params.data.other_name);
        $("#apply_nature").val(e.params.data.nature);
        $("#apply_gender_" + e.params.data.gender).iCheck("check");
        $("#apply_birthday").val(e.params.data.birthday);
        $("#apply_id_type").val(e.params.data.id_type_id).trigger("change");
        $("#apply_id_no").val(e.params.data.id_no);
        $("#apply_phone").val(e.params.data.phone);
        $("#apply_domicile").val(e.params.data.domicile);
        $("#apply_zip_code").val(e.params.data.zip_code);
        $("#apply_contact").val(e.params.data.contact);
        $("#abode").val(e.params.data.abode);

        //法人组织
        $("#apply_unit_name").val(e.params.data.unit_name);
        $("#apply_unit_contact").val(e.params.data.unit_contact);
        if (e.params.data.unit_id_type_id != null && e.params.data.unit_id_type_id != "") {
            $("#apply_unit_id_type").val(e.params.data.unit_id_type_id).trigger("change");
        } else {
            $("#apply_unit_id_type").val("-1").trigger("change");
        }
        $("#apply_unit_id_no").val(e.params.data.unit_id_no);
        $("#apply_unit_abode").val(e.params.data.unit_abode);
        $("input:radio[name=Legal_person][value=" + e.params.data.legal_person_type + "]").attr("checked", true);

        var data = [{
            id: e.params.data.id_file_id,
            url: e.params.data.id_file,
            type: e.params.data.id_file_type,
            real_name: e.params.data.id_file_name,
            ext: e.params.data.id_file_ext
        }];
        initData4Files("ul_apply_file", "li-apply-file", data, true);

        $("#search_apply").select2("val", "");
    });
    // TODO 搜索被申请人请求，被申请人
    onChange4Select("apply_name", "apply_id", "apply_request");

    $("input[name='apply_type']").on("ifChanged", function () {
        $("#apply_unit_id_type").val("-1").trigger("change");
        $("#apply_unit_id_no").val(null);
        $("#apply_unit_contact").val(null);
        if ($("input[name='apply_type']:checked").val() == "1") {
            $("#label_apply_name").html("姓名");
            $("#label_apply_legal_person").html("英文名/其他语种名/化名/曾用名");
            $(".form-unit").hide();
        } else {
            $("#label_apply_name").html("法人组织名称");
            $("#label_apply_legal_person").html("法定代表人");
            $(".form-unit").show();
        }
    })
}

function clearModal4ApplyRepresentative() {
    $("#search_apply_representative").select2("val", "");
}

function initModal4ApplyRepresentative(casesId) {
    loadData4Select2("search_apply_representative", "cases_personnel_temp/get_choose_list_4_apply?cases_id=" + casesId, null, true);

    clearModal4ApplyRepresentative();
}

function initModalEvent4ApplyRepresentative() {
    $("#btn_clear_apply_representative").on("click", function () {
        clearModal4ApplyRepresentative();
    })
}

function savePersonnel4ApplyRepresentative(casesId) {
    $("#btn_submit_apply_representative").on("click", function () {
        $("#loading_apply_representative").show();
        var ids = $("#search_apply_representative").val();
        if (ids == null || ids.length == 0) {
            $("#loading_apply_representative").hide();
            showMsg("请选择申请人");
            return;
        }
        $.ajax({
            type: "POST",
            url: "cases_personnel_temp/set_representative",
            dataType: "JSON",
            data: {
                ids: ids.join(","),
            },
            success: function (data) {
                $("#loading_apply_representative").hide();
                if (data.result == 1) {
                    $("#modal_add_apply_representative").modal("hide");

                    getCasesPersonnelList4Representative(casesId, true);
                    return;
                }
                showMsg(data.error_msg);
            },
            error: function (xhr, type) {
                $("#loading_apply_representative").hide();
                showMsg("系统繁忙");
            }
        });
    });
}

function getCasesPersonnelList4Representative(casesId, isTemp) {
    $("#loading_row_apply_representative").show();
    isTemp = isTemp || false;
    var url = "";
    if (isTemp) {
        url = "cases_personnel_temp/get_list_4_representative";
    } else {
        url = "cases_personnel/get_list_4_representative";
    }
    $.ajax({
        type: "POST",
        url: url,
        dataType: "JSON",
        data: {
            cases_id: casesId,
        },
        success: function (data) {
            $("#loading_row_apply_representative").hide();
            if (data.result == 1) {
                $(".apply-representative").remove();
                if (data.data == null || data.data.length == 0) {
                    return;
                }
                //点击人员按钮显示概述信息框
                $.each(data.data, function (index, value) {
                    var dName = "";
                    //如果申请人类型为个人
                    if (value.type == "1") {

                        var domicile = "&nbsp;";
                        if (value.domicile != null && value.domicile != "") {
                            domicile = value.domicile;
                        }

                        if (value.nature != null && value.nature != "") {
                            domicile = value.nature;
                        }
                        if (value.other_name != null && value.other_name != "") {
                            otherName = value.other_name;
                        }

                        dName = "<div class='col-lg-12'>" +
                            "<strong><i class='fa fa-user margin-r-5'></i>  姓名</strong>" +
                            "<p style='margin: 10px 0 0;'><a href='" + value.id_file + "' target='_blank'>" + value.name + "</a></p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>" +
                            "<div class='col-lg-6'>" +
                            "<strong><i class='fa fa-user margin-r-5'></i>  4英文名/其他语种名/化名/曾用名</strong>" +
                            "<p style='margin: 10px 0 0;'>" + otherName + "</p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>" +
                            "<div class='col-lg-6'>" +
                            "<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
                            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>";
                        //如果申请人类型为法人组织
                    } else if (value.type == "2") {

                        var domicile = "&nbsp;";
                        if (value.domicile != null && value.domicile != "") {
                            domicile = value.domicile;
                        }
                        var otherName = "&nbsp;";
                        if (value.other_name != null && value.other_name != "") {
                            otherName = value.other_name;
                        }
                        dName = "<div class='col-lg-12'>" +
                            "<strong><i class='fa fa-user margin-r-5'></i>  姓名</strong>" +
                            "<p style='margin: 10px 0 0;'><a href='" + value.id_file + "' target='_blank'>" + value.name + "</a></p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>" +
                            "<div class='col-lg-6'>" +
                            "<strong><i class='fa fa-user margin-r-5'></i>  3英文名/其他语种名/化名/曾用名</strong>" +
                            "<p style='margin: 10px 0 0;'>" + legalPerson + "</p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>" +
                            "<div class='col-lg-6'>" +
                            "<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
                            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>";
                    }
                    var birthday = "&nbsp;"
                    if (value.birthday != null && value.birthday != "") {
                        birthday = value.birthday;
                    }
                    var contact = "&nbsp;"
                    if (value.contact != null && value.contact != "") {
                        contact = value.contact;
                    }
                    var dLast = "";
                    if (isTemp) {
                        dLast = "<div class='col-lg-12'>" +
                            "<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
                            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
                            "<hr style='margin: 10px 0;'>" +
                            "</div>" +
                            "<div class='col-lg-12'>" +
                            "<button class='btn btn-block btn-skin " + btnSkin + " btn-representative-delete' data-id='" + value.personnel_id + "'><i class='fa fa-trash-o'></i> 删除</button>" +
                            "</div>";
                    } else {
                        dLast = "<div class='col-lg-12'>" +
                            "<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
                            "<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
                            "</div>";
                    }
                    var $div = "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' class='apply-representative' id='" + value.personnel_id + "'>" +
                        "<div class='row'>" +
                        dName +
                        "<div class='col-lg-2'>" +
                        "<strong><i class='fa fa-intersex margin-r-5'></i>  性别</strong>" +
                        "<p style='margin: 10px 0 0;'>" + value.gender + "</p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        "<div class='col-lg-3'>" +
                        "<strong><i class='fa fa-calendar margin-r-5'></i>  出生日期</strong>" +
                        "<p style='margin: 10px 0 0;'>" + birthday + "</p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        "<div class='col-lg-3'>" +
                        "<strong><i class='fa fa-mobile margin-r-5'></i>  手机号码</strong>" +
                        "<p style='margin: 10px 0 0;'>" + value.phone + "</p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        "<div class='col-lg-4'>" +
                        "<strong><i class='fa fa-phone margin-r-5'></i>  其他联系方式</strong>" +
                        "<p style='margin: 10px 0 0;'>" + contact + "</p>" +
                        "<hr style='margin: 10px 0;'>" +
                        "</div>" +
                        dLast +
                        "</div>" +
                        "</div>";
                    $("#row_apply_representative").append($div);
                })
                if (isTemp) {
                    initEvent4DeleteRepresentative(casesId);
                }
                return;
            }
            showMsg(data.error_msg);
        },
        error: function (xhr, type) {
            $("#loading_row_apply_representative").hide();
            showMsg("系统繁忙");
        }
    });
}

function initEvent4DeleteRepresentative(casesId) {
    $(".btn-representative-delete").off().on("click", function () {
        $.ajax({
            type: "POST",
            url: "cases_personnel_temp/delete_representative",
            dataType: "JSON",
            data: {
                id: $(this).data("id"),
            },
            success: function (data) {
                if (data.result == 1) {
                    getCasesPersonnelList4Representative(casesId, true);
                    return;
                }
                showMsg(data.error_msg);
            },
            error: function (xhr, type) {
                $("#loading_apply_representative").hide();
                showMsg("系统繁忙");
            }
        });
    })
}

var thirdPartyId = "";

function clearModal4ThirdParty() {
    $("#search_third_party").select2("val", "");

    $("#third_personnel_id").val(null);
    $("#third_party_name").val(null);
    $("#third_party_legal_person").val(null);
    $("input[name='third_party_gender']:first").iCheck("check");
    $("#third_party_birthday").val(null);
    $("#third_party_phone").val(null);
    $("#third_party_contact").val(null);
    $("#third_party_zip_code").val(null);
    $("#third_party_province").val("-1").trigger("change");
    $("#third_party_address").val(null);
    $("#third_party_id_type").val("-1").trigger("change");
    $("#third_party_id_no").val(null);
    $(".li-third-party-file").remove();
    $("#third_party_unit_id_type").val("-1").trigger("change");
    $("#third_party_unit_id_no").val(null);
    $("#third_party_unit_contact").val(null);
    $("#third_party_domicile").val(null);

    thirdPartyId = getActId();
    initUpload("loading_third_party", "third_party_file", "li-third-party-file", "temp/img?res=" + thirdPartyId);
}

function initModal4ThirdParty() {
    if (casesPersonnelType == "7") {
        $("#title_third_party").html("（申请人建议追加）第三人");
    } else if (casesPersonnelType == "3") {
        $("#title_third_party").html("（第三人");
    }

    initSelect4Ajax("search_third_party", "third_party/get_choose_list");

    loadData4Select2("third_party_unit_id_type", "unit_id_type/get_choose_list");
    initDatetimePicker("third_party_birthday");
    loadData4Select2("third_party_province", "region/get_choose_list_4_parent");
    // 注册城市切换监听
    onChange4Province("third_party_province", "third_party_city", "region/get_choose_list_by_parent", "third_party_county");
    onChange4City("third_party_city", "third_party_county", "region/get_choose_list_by_parent");
    loadData4Select2("third_party_id_type", "id_type/get_choose_list");

    clearModal4ThirdParty();
}

function initModalEvent4ThirdParty() {
    $("#btn_clear_third_party").on("click", function () {
        clearModal4ThirdParty();
    })

    $("#search_third_party").on("select2:select", function (e) {
        $("#third_party_personnel_id").val(e.params.data.id);
        $("#third_party_type_" + e.params.data.type).iCheck("check");
        $("#third_party_name").val(e.params.data.name);
        $("#third_party_legal_person").val(e.params.data.legal_person);
        $("#third_party_gender_" + e.params.data.gender).iCheck("check");
        $("#third_party_birthday").val(e.params.data.birthday);
        $("#third_party_phone").val(e.params.data.phone);
        $("#third_party_contact").val(e.params.data.contact);
        $("#third_party_zip_code").val(e.params.data.zip_code);
        countyId = e.params.data.county_id;
        cityId = e.params.data.city_id;
        $("#third_party_province").val(e.params.data.province_id).trigger("change");
        $("#third_party_address").val(e.params.data.address);
        $("#third_party_id_type").val(e.params.data.id_type_id).trigger("change");
        $("#third_party_id_no").val(e.params.data.id_no);

        if (e.params.data.unit_id_type_id != null && e.params.data.unit_id_type_id != "") {
            $("#third_party_unit_id_type").val(e.params.data.unit_id_type_id).trigger("change");
        } else {
            $("#third_party_unit_id_type").val("-1").trigger("change");
        }
        $("#third_party_unit_id_no").val(e.params.data.unit_id_no);
        $("#third_party_unit_contact").val(e.params.data.unit_contact);
        $("#third_party_domicile").val(e.params.data.domicile);

        var data = [{
            id: e.params.data.id_file_id,
            url: e.params.data.id_file,
            type: e.params.data.id_file_type,
            real_name: e.params.data.id_file_name,
            ext: e.params.data.id_file_ext
        }];
        initData4Files("ul_third_party_file", "li-third-party-file", data, true);

        $("#search_third_party").select2("val", "");
    });

    $("input[name='third_party_type']").on("ifChanged", function () {
        $("#third_party_unit_id_type").val("-1").trigger("change");
        $("#third_party_unit_id_no").val(null);
        $("#third_party_unit_contact").val(null);
        if ($("input[name='third_party_type']:checked").val() == "1") {
            $("#label_third_party_name").html("姓名");
            $("#label_third_party_legal_person").html("英文名/其他语种名/化名/曾用名");
            $(".form-third-party-unit").hide();
        } else {
            $("#label_third_party_name").html("法人组织名称");
            $("#label_third_party_legal_person").html("法定代表人");
            $(".form-third-party-unit").show();
        }
    })
}

var applyThirdPartyId = "";

function clearModal4ApplyThirdParty() {
    $("#search_apply_third_party").val("-1").trigger("change");

    $("#apply_third_personnel_id").val(null);
    $("#apply_third_party_name").val(null);
    $("#apply_third_party_legal_person").val(null);
    $("input[name='apply_third_party_gender']:first").iCheck("check");
    $("#apply_third_party_birthday").val(null);
    $("#apply_third_party_phone").val(null);
    $("#apply_third_party_contact").val(null);
    $("#apply_third_party_zip_code").val(null);
    $("#apply_third_party_province").val("-1").trigger("change");
    $("#apply_third_party_address").val(null);
    $("#apply_third_party_id_type").val("-1").trigger("change");
    $("#apply_third_party_id_no").val(null);
    $(".li-apply-third-party-file").remove();
    $("#apply_third_party_unit_id_type").val("-1").trigger("change");
    $("#apply_third_party_unit_id_no").val(null);
    $("#apply_third_party_unit_contact").val(null);
    $("#apply_third_party_domicile").val(null);

    applyThirdPartyId = getActId();
    initUpload("loading_apply_third_party", "apply_third_party_file", "li-apply-third-party-file", "temp/img?res=" + applyThirdPartyId);
}

function initModal4ApplyThirdParty(casesId) {
    loadData4Select2("search_apply_third_party", "cases_personnel/get_choose_list_4_apply_third_party?cases_id=" + casesId);

    loadData4Select2("apply_third_party_unit_id_type", "unit_id_type/get_choose_list");
    initDatetimePicker("apply_third_party_birthday");
    loadData4Select2("apply_third_party_province", "region/get_choose_list_4_parent");
    loadData4Select2("apply_third_party_id_type", "id_type/get_choose_list");

    clearModal4ApplyThirdParty();
}

function initModalEvent4ApplyThirdParty() {
    $("#btn_clear_apply_third_party").on("click", function () {
        clearModal4ApplyThirdParty();
    })

    $("#search_apply_third_party").on("select2:select", function (e) {
        if (e.params.data.id == "-1") {
            return;
        }
        $("#apply_third_party_personnel_id").val(e.params.data.id);
        $("#apply_third_party_type_" + e.params.data.type).iCheck("check");
        $("#apply_third_party_name").val(e.params.data.name);
        $("#apply_third_party_legal_person").val(e.params.data.legal_person);
        $("#apply_third_party_gender_" + e.params.data.gender).iCheck("check");
        $("#apply_third_party_birthday").val(e.params.data.birthday);
        $("#apply_third_party_phone").val(e.params.data.phone);
        $("#apply_third_party_contact").val(e.params.data.contact);
        $("#apply_third_party_zip_code").val(e.params.data.zip_code);
        if (e.params.data.province_id != null && e.params.data.province_id != "") {
            countyId = e.params.data.county_id;
            cityId = e.params.data.city_id;
            $("#apply_third_party_province").val(e.params.data.province_id).trigger("change");
        }
        $("#apply_third_party_address").val(e.params.data.address);
        if (e.params.data.id_type_id != null && e.params.data.id_type_id != "") {
            $("#apply_third_party_id_type").val(e.params.data.id_type_id).trigger("change");
        }
        $("#apply_third_party_id_no").val(e.params.data.id_no);

        if (e.params.data.unit_id_type_id != null && e.params.data.unit_id_type_id != "") {
            $("#apply_third_party_unit_id_type").val(e.params.data.unit_id_type_id).trigger("change");
        } else {
            $("#apply_third_party_unit_id_type").val("-1").trigger("change");
        }
        $("#apply_third_party_unit_id_no").val(e.params.data.unit_id_no);
        $("#apply_third_party_unit_contact").val(e.params.data.unit_contact);
        $("#apply_third_party_domicile").val(e.params.data.domicile);

        if (e.params.data.id_file_id != null && e.params.data.id_file_id != "") {
            var data = [{
                id: e.params.data.id_file_id,
                url: e.params.data.id_file,
                type: e.params.data.id_file_type,
                real_name: e.params.data.id_file_name,
                ext: e.params.data.id_file_ext
            }];
            initData4Files("ul_apply_third_party_file", "li-apply-third-party-file", data, true);
        }
    });

    onChange4Province("apply_third_party_province", "apply_third_party_city", "region/get_choose_list_by_parent", "apply_third_party_county");
    onChange4City("apply_third_party_city", "apply_third_party_county", "region/get_choose_list_by_parent");

    $("input[name='apply_third_party_type']").on("ifChanged", function () {
        if ($("input[name='apply_third_party_type']:checked").val() == "1") {
            $("#label_apply_third_party_name").html("姓名");
            $("#label_apply_third_party_legal_person").html("英文名/其他语种名/化名/曾用名");
            $(".form-apply-third-party-unit").hide();
        } else {
            $("#label_apply_third_party_name").html("法人组织名称");
            $("#label_apply_third_party_legal_person").html("法定代表人");
            $(".form-apply-third-party-unit").show();
        }
    })
}

var agentId = "";

//清空代理人输入框
function clearModal4Agent() {
    $("#search_agent").select2("val", "");
    $("#agent_personnel_id").val(null);
    $("#agent_name").val(null);
    $("#agent_type").val("1").trigger("change");
    $("#agent_unit_name").val(null);
    $("input[name='agent_gender']:first").iCheck("check");
    $("#agent_birthday").val(null);
    $("#agent_phone").val(null);
    $("#agent_kinsfolk").val();
    $("#agent_identity").val("-1").trigger("change");
    $("#agent_address").val(null);
    $("#agent_id_type").val("-1").trigger("change");
    $("#agent_id_no").val(null);

    $(".li-agent-file-1").remove();
    $(".li-agent-file-2").remove();
    $(".li-agent-file-3").remove();
    $(".li-agent-file-4").remove();
    $(".li-agent-file-5").remove();
    $(".li-agent-file-6").remove();
    $(".li-agent-file-7").remove();
    $(".li-agent-file-8").remove();
    $(".li-agent-file-9").remove();
    $(".li-agent-file-10").remove();
    $(".li-agent-file-11").remove();


    agentId = getActId();
    initUpload("loading_agent", "agent_file_1", "li-agent-file-1", "temp/imgs?mode=1&res=" + agentId);
    initUpload("loading_agent", "agent_file_2", "li-agent-file-2", "temp/img?mode=2&res=" + agentId);


    initUpload("loading_agent", "agent_file_3", "li-agent-file-3", "temp/imgs?mode=4&res=" + agentId);
    initUpload("loading_agent", "agent_file_4", "li-agent-file-4", "temp/img?mode=5&res=" + agentId);
    initUpload("loading_agent", "agent_file_5", "li-agent-file-5", "temp/imgs?mode=6&res=" + agentId);
    initUpload("loading_agent", "agent_file_6", "li-agent-file-6", "temp/img?mode=7&res=" + agentId);
    initUpload("loading_agent", "agent_file_7", "li-agent-file-7", "temp/imgs?mode=8&res=" + agentId);
    initUpload("loading_agent", "agent_file_8", "li-agent-file-8", "temp/img?mode=9&res=" + agentId);
    initUpload("loading_agent", "agent_file_9", "li-agent-file-9", "temp/imgs?mode=10&res=" + agentId);
    initUpload("loading_agent", "agent_file_10", "li-agent-file-10", "temp/img?mode=11&res=" + agentId);
    initUpload("loading_agent", "agent_file_11", "li-agent-file-11", "temp/imgs?mode=12&res=" + agentId);
}

var agentTypeVal = "1";

function initModal4Agent(casesId) {

    initSelect2WithData("agent_type", agentTypeArr);
    initSelect2WithData("agent_identity", identityTypeArr);
    //初始化城市选择
    loadData4Select2("agent_province", "region/get_choose_list_4_parent");
    // 注册城市切换监听
    onChange4Province("agent_province", "agent_city", "region/get_choose_list_by_parent", "agent_county");
    onChange4City("agent_city", "agent_county", "region/get_choose_list_by_parent");
    //agentTypeVal: 1、近亲属  2、律师/法律工作者  3、单位工作人员  4、其他公民
    $("#agent_type").on("select2:select", function (e) {
        agentTypeVal = String($("#agent_type").find("option:selected").val());
        if (agentTypeVal == "" || agentTypeVal == null) {
            agentTypeVal = "1";
        }
        if (agentTypeVal == "1") {
            $("#agent_unit_name_div").hide();
            $("#agent_kinsfolk_div").show();
            $("#agent_identity_div").hide();
            $("#agent_id_type_div").show();
            $("#address_title").html("户籍所在地");
            $("#other_file").html("亲属关系证明材料");
        } else if (agentTypeVal == "2") {
            $("#agent_unit_name_div").show();
            $("#agent_kinsfolk_div").hide();
            $("#agent_identity_div").show();
            $("#agent_id_type_div").hide();
            $("#address_title").html("联系地址");
            $("#other_file").html("单位公函");
        } else if (agentTypeVal == "3") {
            $("#agent_unit_name_div").show();
            $("#agent_kinsfolk_div").hide();
            $("#agent_identity_div").hide();
            $("#agent_id_type_div").show();
            $("#address_title").html("户籍所在地");
            $("#other_file").html("单位公函");
        } else if (agentTypeVal == "4") {
            $("#agent_unit_name_div").show();
            $("#agent_kinsfolk_div").hide();
            $("#agent_identity_div").hide();
            $("#agent_id_type_div").show();
            $("#address_title").html("户籍所在地");
            $("#other_file").html("当事人所在社区或单位以及有关社会团体推荐书");
        }
    });

    if (agentTypeVal == "1") {
        $("#agent_unit_name_div").hide();
        $("#agent_kinsfolk_div").show();
        $("#agent_identity_div").hide();
        $("#agent_id_type_div").show();
        $("#address_title").html("户籍所在地");
        $("#other_file").html("亲属关系证明材料");
    } else if (agentTypeVal == "2") {
        $("#agent_unit_name_div").show();
        $("#agent_kinsfolk_div").hide();
        $("#agent_identity_div").show();
        $("#agent_id_type_div").show();
        $("#address_title").html("联系地址");
        $("#other_file").html("单位公函");
    } else if (agentTypeVal == "3") {
        $("#agent_id_type_div").hide();
        $("#agent_unit_name_div").show();
        $("#agent_kinsfolk_div").hide();
        $("#agent_identity_div").hide();

        $("#address_title").html("户籍所在地");
        $("#other_file").html("单位公函");
    } else if (agentTypeVal == "4") {
        $("#agent_unit_name_div").show();
        $("#agent_kinsfolk_div").hide();
        $("#agent_identity_div").hide();
        $("#agent_id_type_div").show();
        $("#address_title").html("户籍所在地");
        $("#other_file").html("当事人所在社区或单位以及有关社会团体推荐书");
    }

    if (client == "2") {
        $("#box_client").show();
        loadData4Select2("agent_client", "cases_personnel/get_choose_list_4_client?type=2&cases_id=" + casesId);

        $("#title_add_agent").html("被申请人委托代理人");
    } else if (client == "3") {
        $("#box_client").show();
        loadData4Select2("agent_client", "cases_personnel/get_choose_list_4_client?type=3&cases_id=" + casesId);

        $("#title_add_agent").html("第三人委托代理人");
    } else {
        if (clientId == null || clientId == "") {
            $("#box_client").show();
            loadData4Select2("agent_client", "cases_personnel/get_choose_list_4_client?type=1&cases_id=" + casesId);
        } else {
            $("#box_client").show();
            var data = [{
                id: clientId,
                text: "申请人"
            }]
            initSelect2WithData("agent_client", data);
        }
        $("#title_add_agent").html("申请人委托(法定)代理人");
    }

    initSelect4Ajax("search_agent", "agent/get_choose_list");
    initDatetimePicker("agent_birthday");
    loadData4Select2("agent_id_type", "id_type/get_choose_list");

    clearModal4Agent();
}

//代理人输入框回填
function initModalEvent4Agent() {
    $("#btn_clear_agent").on("click", function () {
        clearModal4Agent();
    })


    $("#search_agent").on("select2:select", function (e) {
        $("#agent_personnel_id").val(e.params.data.id);

        $("#agent_name").val(e.params.data.name);
        $("#agent_unit_name").val(e.params.data.unit_name);
        // TODO
        $("#agent_gender" + e.params.data.gender).iCheck("check");
        $("#agent_birthday").val(e.params.data.birthday);
        $("#agent_phone").val(e.params.data.phone);
        $("#agent_kinsfolk").val(e.params.data.kinsfolk);
        $("#agent_identity").val(e.params.data.identity);
        $("#agent_address").val(e.params.data.address);
        $("#agent_id_type").val(e.params.data.id_type_id).trigger("change");
        $("#agent_id_no").val(e.params.data.id_no);

        var dataList = [];
        if (e.params.data.agent_file_list != null && e.params.data.agent_file_list.length > 0) {
            $.each(e.params.data.agent_file_list, function (index, value) {
                var data = {
                    id: value.id,
                    url: value.file_url,
                    type: value.type,
                    real_name: value.real_name,
                    ext: value.ext
                }
                dataList.push(data);
            });
        }
        initData4Files("ul_agent_file_1", "li-agent-file-1", dataList, true);

        $("#search_agent").select2("val", "");
    });

    /*$("#search_agent").on("select2:select", function(e) {
		var i=$('#agent_client').val();
		i=parseInt(i)+1;
		$("#agent_personnel_id").val(e.params.data.id);
		$("#agent_name"+i).val(e.params.data.name);
		$("#agent_unit_name"+i).val(e.params.data.unit_name);
		if(e.params.data.label_gender=='女'){
			$("#nv"+i ).val('2').iCheck("check");
		}else if(e.params.data.label_gender=='男'){
			$("#nan"+i ).val('1').iCheck("check");
		}
		$("#agent_birthday"+i ).val(e.params.data.birthday);
		$("#agent_phone"+i ).val(e.params.data.phone);
		$("#agent_contact"+i ).val(e.params.data.contact);
		$("#agent_zip_code"+i ).val(e.params.data.zip_code);
		countyId = e.params.data.county_id;
		cityId = e.params.data.city_id;
		$("#agent_province"+i ).val(e.params.data.province_id).trigger("change");
		$("#agent_address"+i ).val(e.params.data.address);
		$("#agent_id_type"+i ).val(e.params.data.id_type_id).trigger("change");
		$("#agent_id_no"+i ).val(e.params.data.id_no);

		var dataList = [];
		if(e.params.data.agent_file_list != null && e.params.data.agent_file_list.length > 0) {
			$.each(e.params.data.agent_file_list, function(index, value) {
				var data = {
					id: value.id,
					url: value.file_url,
					type: value.type,
					real_name: value.real_name,
					ext: value.ext
				}
				dataList.push(data);
			});
		}
		initData4Files("ul_agent_file_1", "li-agent-file-1", dataList, true);

		$("#search_agent").select2("val", "");
	});*/

    onChange4Province("agent_province", "agent_city", "region/get_choose_list_by_parent", "agent_county");
    onChange4City("agent_city", "agent_county", "region/get_choose_list_by_parent");
    onChange4Province("agent_province2", "agent_city2", "region/get_choose_list_by_parent", "agent_county2");
    onChange4City("agent_city2", "agent_county2", "region/get_choose_list_by_parent");
    onChange4Province("agent_province3", "agent_city3", "region/get_choose_list_by_parent", "agent_county3");
    onChange4City("agent_city3", "agent_county3", "region/get_choose_list_by_parent");
    onChange4Province("agent_province4", "agent_city4", "region/get_choose_list_by_parent", "agent_county4");
    onChange4City("agent_city4", "agent_county4", "region/get_choose_list_by_parent");
}

//代理人概况信息弹窗
function initModal4AgentView() {
    $("#agentViewModal").on("show.bs.modal", function (event) {
        var button = $(event.relatedTarget);
        if (button.data("temp") == "1") {
            $("#div_delete_agent").show();
            $("#btn_agent_delete").data("id", button.data("id"));
            $("#btn_agent_delete").data("agent", "1");
        } else {
            $("#div_delete_agent").hide();
        }
        $("#label_agent_name").html(button.data("name"));
        var unit_name = "&nbsp;"
        if (button.data("unit_name") != null && button.data("unit_name") != "") {
            unit_name = button.data("unit_name");
        }
        $("#label_agent_unit_name").html(unit_name);
        $("#label_agent_gender").html(button.data("gender"));
        var birthday = "&nbsp;"
        if (button.data("birthday") != null && button.data("birthday") != "") {
            birthday = button.data("birthday");
        }
        $("#label_agent_birthday").html(birthday);
        var phone = "&nbsp;"
        if (button.data("phone") != null && button.data("phone") != "") {
            phone = button.data("phone");
        }
        $("#label_agent_phone").html(phone);
        var contact = "&nbsp;"
        if (button.data("contact") != null && button.data("contact") != "") {
            contact = button.data("contact");
        }
        $("#label_agent_contact").html(contact);
        $("#label_agent_address").html(button.data("address"));

        $("#label_agent_id_file").empty();
        var idFileNames = button.data("idfilenames");
        var idFiles = button.data("idfiles");
        if (idFileNames != null && idFileNames != "") {
            var nameArr = idFileNames.split("@");
            var fileArr = idFiles.split("@");
            $.each(nameArr, function (index, value) {
                var $file = $("<a class='bg-gray as-btn' href='" + fileArr[index] + "' target='_blank'><i class='fa fa-image'></i> " + nameArr[index] + "</a>")
                $("#label_agent_id_file").append($file);
            });
        }
        $("#label_agent_file").html("<a class='bg-gray as-btn' href='" + button.data("agentfile") + "' target='_blank'><i class='fa fa-image'></i> " + button.data("agentfilename") + "</a>");
    })
}

var defendantId = "";

//清空被申请人的相关信息
function clearModal4Defendant() {
    $("#search_defendant").select2("val", "");

    $("#defendant_personnel_id").val(null);
    $("#defendant_type").val("-1").trigger("change");
    $("#defendant_unit_name").val(null);
    $("#defendant_unit_abode").val(null);
    $("#defendant_duty").val(null);
    $("#defendant_name").val(null);
    $("#defendant_unit_content").val(null);

    $("input[name='defendant_legal_person_type']").get(0).checked = true;

    defendantId = getActId();
    initUpload("loading_defendant", "defendant_file_1", "li-defendant-file-1", "temp/img?mode=1&res=" + defendantId);
    initUpload("loading_defendant", "defendant_file_2", "li-defendant-file-2", "temp/img?mode=2&res=" + defendantId);
    initUpload("loading_defendant", "defendant_file_3", "li-defendant-file-3", "temp/img?mode=3&res=" + defendantId);
}

function initModal4Defendant() {
    initSelect4Ajax("search_defendant", "defendant/get_choose_list");

    loadData4Select2("defendant_type", "defendant_type/get_choose_list");

    clearModal4Defendant();
}

//添加当事人Modal框搜索数据回填
function initModalEvent4Defendant() {
    $("#btn_clear_defendant").on("click", function () {
        initModal4Defendant();
    });

    $("#search_defendant").on("select2:select", function (e) {
        $("#defendant_personnel_id").val(e.params.data.id);
        $("#defendant_type").val(e.params.data.type).trigger("change");
        $("#defendant_unit_name").val(e.params.data.unit_name);
        $("#defendant_unit_abode").val(e.params.data.unit_abode);
        $("#defendant_unit_content").val(e.params.data.unit_contact);
        $("#defendant_name").val(e.params.data.name);
        if (1 == e.params.data.legalPersonType) {
            $("input[name='defendant_legal_person_type']").get(0).checked = true;
        } else if (2 == e.params.data.legalPersonType) {
            $("input[name='defendant_legal_person_type']").get(1).checked = true;
        }
        $("#defendant_duty").val(e.params.data.duty);

        if (e.params.data.file_id_1 != null && e.params.data.file_id_1 != "") {
            var data1 = [{
                id: e.params.data.file_id_1,
                url: e.params.data.file_1,
                type: e.params.data.file_type_1,
                real_name: e.params.data.file_name_1,
                ext: e.params.data.file_ext_1
            }];
            initData4Files("ul_defendant_file_1", "li-defendant-file-1", data1, true);
        }
        if (e.params.data.file_id_2 != null && e.params.data.file_id_2 != "") {
            var data2 = [{
                id: e.params.data.file_id_2,
                url: e.params.data.file_2,
                type: e.params.data.file_type_2,
                real_name: e.params.data.file_name_2,
                ext: e.params.data.file_ext_2
            }];
            initData4Files("ul_defendant_file_2", "li-defendant-file-2", data2, true);
        }
        if (e.params.data.file_id_3 != null && e.params.data.file_id_3 != "") {
            var data3 = [{
                id: e.params.data.file_id_3,
                url: e.params.data.file_3,
                type: e.params.data.file_type_3,
                real_name: e.params.data.file_name_3,
                ext: e.params.data.file_ext_3
            }];
            initData4Files("ul_defendant_file_3", "li-defendant-file-3", data3, true);
        }

        $("#search_defendant").select2("val", "");
    });
}

function initFormList() {
    $("#arrow_left").on("click", function () {
        $("#arrow_left").hide();
        $("#form_list").show();
    });

    $("#arrow_right").on("click", function () {
        $("#arrow_left").show();
        $("#form_list").hide();
    });
}

var casesPersonnelType = "";

function initToolBar(casesId, isReload) {
    isReload = isReload || false;
    //进度查询
    $("#modal_step").on("show.bs.modal", function (e) {
        $.ajax({
            url: "cases_step/get_list",
            type: "post",
            dataType: "json",
            data: {
                cases_id: casesId
            },
            success: function (data) {
                if (data.result != 1) {
                    showMsg(data.error_msg);
                    return;
                }

                $(".li-step").remove();
                if (data.data != null && data.data.length > 0) {
                    $.each(data.data, function (index, value) {
                        var lebal = getStateLabel(value.state, true);
                        var label_account = "";
                        if (value.label_account != null && value.label_account != "") {
                            label_account = "<div class='pull-right'><label class='label label-info' style='border-radius: 0px;'>" + value.label_department + "</label>" +
                                "<label class='label label-default' style='border-radius: 0px;'>" + value.label_account + "</label><div>";
                        }
                        var $li = $("<li class='li-step'>" +
                            "<i class='fa fa-clock-o'></i>" +
                            "<div class='timeline-item no-shadow' style='border: 1px solid #eee;'>" +
                            "<div class='timeline-header' style='border-bottom: 0px;'>" +
                            lebal + "<label class='label label-default' style='border-radius: 0px;'>" + value.create_time + "</label>" + label_account +
                            "</div>" +
                            "</div>" +
                            "</li>");
                        $("#ul_step").append($li);
                    });
                }
            },
            error: function (xhr, type) {
                showMsg("系统繁忙");
            }
        });
    });
    //证据目录
    $("#modal_file").on("show.bs.modal", function () {
        initUpload("loading_file", "cases_file_1", "cases-file-1", "cases_file/upload?mode=1&res=" + casesId);
        getFileList("loading_file", "ul_cases_file_1", "cases-file-1", "cases_file/files?mode=1&cases_id=" + casesId);

        initUpload("loading_file", "cases_file_2", "cases-file-2", "cases_file/upload?mode=2&res=" + casesId);
        getFileList("loading_file", "ul_cases_file_2", "cases-file-2", "cases_file/files?mode=2&cases_id=" + casesId);

        initUpload("loading_file", "cases_file_3", "cases-file-3", "cases_file/upload?mode=3&res=" + casesId);
        getFileList("loading_file", "ul_cases_file_3", "cases-file-3", "cases_file/files?mode=3&cases_id=" + casesId);
    })
    //涉案文书
    $("#modal_document").on("show.bs.modal", function () {
        getFileList("loading_document", "table_cases_file_6", "cases-file-6", "cases_file/files?mode=6&cases_id=" + casesId, false, true);
    })
    //追加当事人
    $("#modal_party").on("show.bs.modal", function () {
        casesPersonnelType = "";
    });
    $(".btn-add-personnel").on("click", function () {
        casesPersonnelType = $(this).data("type");
        $("#modal_party").modal("hide");
    });
    $("#modal_party").on("hidden.bs.modal", function () {
        if (casesPersonnelType == "1") {
            initModal4Apply();
            $("#modal_add_apply").modal("show");
        } else if (casesPersonnelType == "3") {
            initModal4ThirdParty();
            $("#modal_add_third_party").modal("show");
        } else if (casesPersonnelType == "4") {
            client = "1";
            clientId = "";
            initModal4Agent(casesId);
            $("#modal_add_agent").modal("show");
        } else if (casesPersonnelType == "5") {
            client = "3";
            clientId = "";
            initModal4Agent(casesId);
            $("#modal_add_agent").modal("show");
        } else if (casesPersonnelType == "6") {
            client = "2";
            clientId = "";
            initModal4Agent(casesId);
            $("#modal_add_agent").modal("show");
        } else if (casesPersonnelType == "7") {
            initModal4ApplyThirdParty(casesId);
            $("#modal_add_apply_third_party").modal("show");
        }
    });
    $(".btn-apply-third-party").on("click", function () {
        casesPersonnelType = "7";
        initModal4ApplyThirdParty(casesId);
        $("#modal_add_apply_third_party").modal("show");
    });
    initSubmit4Personnel(casesId, false, isReload);
    //电子卷宗
    $("#modal_dossier").on("show.bs.modal", function () {
        initUpload("loading_cases_file", "cases_file", "cases-file", "cases_file/upload?mode=5&res=" + casesId);
        getFileList("loading_cases_file", "ul_cases_file", "cases-file", "cases_file/files?mode=5&cases_id=" + casesId);
    });
    initModal4Rename();
}

var client = "";
var clientId = "";

function initSubmit4Personnel(casesId, isTemp, isReload) {

    isTemp = isTemp || false;
    isReload = isReload || false;
    // 申请人
    $("#btn_submit_apply").on("click", function () {
        savePersonnel(isTemp, false, "loading_apply", "loading_row_apply", casesId, "1", isReload);
    })
    //  建议第三人
    $("#btn_submit_advice_third_party").on("click", function () {
        /*savePersonnel(isTemp, isNext, saveLoadingId, getLoadingId, casesId, casesPersonnelType, isReload)*/
        savePersonnel(isTemp, false, "loading_advice_third_party", "loading_row_third_party", casesId, "7", isReload);
    })
    // 第三人
    /*$("#btn_submit_third_party").on("click", function() {
		savePersonnel(isTemp, false, "loading_third_party", "loading_row_third_party", casesId, "7", isReload);
	})*/

    // 委托代理人
    $("#btn_submit_agent").on("click", function () {
        if (client == "1") {
            savePersonnel(isTemp, false, "loading_agent", "table_loading", casesId, "4", isReload);
            return;
        }
        if (client == "2") {
            savePersonnel(isTemp, false, "loading_agent", "table_loading", casesId, "6", isReload);
            return;
        }
        if (client == "3") {
            savePersonnel(isTemp, false, "loading_agent", "table_loading", casesId, "5", isReload);
            return;
        }
    })

    // 被申请人
    $("#btn_submit_defendant").on("click", function () {
        savePersonnel(isTemp, false, "loading_defendant", "table_loading", casesId, "2", isReload);
    })
}

//获取案件备注列表
function getCasesRemarkList(casesId) {
    $.ajax({
        url: "cases_remark/get_list",
        type: "post",
        dataType: "json",
        data: {
            cases_id: casesId
        },
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            $(".tr-remark").remove();
            if (data.data != null && data.data.length > 0) {
                $.each(data.data, function (index, value) {
                    var btn = "";
                    if (value.editable == "1") {
                        btn = "<button class='btn btn-xs btn-default' data-toggle='modal' data-target='#modal_remark' data-id='" + value.id + "' data-remark='" + value.remark + "'>编辑</button> " +
                            "<button class='btn btn-xs btn-default btn-remark-delete' data-id='" + value.id + "'>删除</button>";
                    }
                    var $tr = $("<tr class='tr-remark'>" +
                        "<td>" + value.remark + "</td> " +
                        "<td><label class='label label-info' style='border-radius: 0px;'>" + value.label_department + "</label><label class='label label-default' style='border-radius: 0px;'>" + value.label_account + "</label></td> " +
                        "<td>" + value.create_time + "</td>" +
                        "<td>" +
                        btn +
                        "</td>" +
                        "</tr>");
                    $("#table_remark").append($tr);
                });

                initEvent4Remark(casesId);
            }
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

//初始化请求类别复选框
function initCheckBox4RequestGateGategory() {
    $("#request_category").empty();
    $.ajax({
        url: "request_category/get_list",
        type: "post",
        dataType: "json",
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            $.each(data.data, function (index, value) {

                if (value.text == "规范性文件审查") {
                    $("#request_category").append("<input type='checkbox' id='normativeDocYes' name='request_category_opt' class='minimal checkbox-request-gategory' value='" + (index + 1) + "' /> <span  style='margin: 0 5px;vertical-align: middle'>" + value.text + "</span>");
                } else {
                    $("#request_category").append("<input type='checkbox' name='request_category_opt' class='minimal checkbox-request-gategory' value='" + (index + 1) + "' /> <span  style='margin: 0 5px;vertical-align: middle'>" + value.text + "</span>");
                }
            });
            //重新初始化复选框使样式生效
            initCheckBoxByClassName("checkbox-request-gategory");
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

function initEvent4Remark(casesId) {
    $(".btn-remark-delete").on("click", function () {
        $.ajax({
            url: "cases_remark/delete",
            type: "post",
            dataType: "json",
            data: {
                id: $(this).data("id")
            },
            success: function (data) {
                if (data.result != 1) {
                    showMsg(data.error_msg);
                    return;
                }
                getCasesRemarkList(casesId);
            },
            error: function (xhr, type) {
                showMsg("系统繁忙");
            }
        });
    })
}

//TODO
function initView() {
    $(".btn-handle").each(function () {
        $(this).hide();
    })
}

var state = "";

function getCases(loadingId, casesId, isWithDetail, isWithToolbar, isWithDocument, mode, isView) {
    isWithDetail = isWithDetail || false;
    isWithToolbar = isWithToolbar || false;
    isWithDocument = isWithDocument || false;
    isView = isView || false;
    mode = mode || "1";
    var isWithPersonnel = "";
    if (isWithDocument) {
        isWithPersonnel = "1";
    }
    $("#" + loadingId).show();
    $.ajax({
        url: "cases/get_object",
        type: "post",
        dataType: "json",
        data: {
            id: casesId,
            is_with_personnel: isWithPersonnel
        },
        success: function (data) {
            $("#" + loadingId).hide();
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            if (isView) {
                $(".box-handle").hide();
                initView();
            } else {
                $(".box-handle").show();
                // audit_case.html 处理按钮
                if (data.data.state < 12) {
                    $(".btn-process-pre-1").show();
                    $(".btn-process-pre-3").hide();
                } else {
                    $(".btn-process-pre-1").hide();
                    $(".btn-process-pre-3").show();
                }

                // 请求追加第三人处理按钮
                $(".btn-apply-third-party").show();
                if (data.data.apply_third_party == null || data.data.apply_third_party == "") {
                    $(".btn-apply-third-party").attr("disabled", true);
                } else {
                    $(".btn-apply-third-party").attr("disabled", false);
                }

                // case_process_pre.html 处理按钮
                if (mode == "1") {
                    $(".process-pre-mode-1").show();
                    $(".process-pre-mode-31").hide();
                    $(".process-pre-mode-32").hide();
                } else if (mode == "31") {
                    $(".process-pre-mode-1").hide();
                    $(".process-pre-mode-31").show();
                    $(".process-pre-mode-32").hide();
                } else if (mode == "32") {
                    $(".process-pre-mode-1").hide();
                    $(".process-pre-mode-31").hide();
                    $(".process-pre-mode-32").show();
                }

                if (data.data.state == "1" || data.data.state == "9" || data.data.state == "11" || data.data.state == "12") {
                    isOver = data.data.is_over;
                    limitTime = data.data.limit_time;
                    showTime();
                }
            }

            $("#label_year_no").html(data.data.year_no);
            $("#label_no").html(data.data.no);
            $("#label_state").html(getStateLabel(data.data.state));
            $("#label_type").html(data.data.label_type);
            $("#label_apply_time").html(data.data.apply_time);
            $("#label_apply_mode").html(data.data.label_apply_mode);
            $("#label_administrative_category").html(data.data.label_administrative_category);
            $("#label_reason").html(data.data.label_reason);
            if (isWithDetail) {
                $("#label_apply").html(data.data.apply);
                //				if(data.data.third_party != null && data.data.third_party != "") {
                //					$("#form_third_party").show();
                //					$("#label_third_party").html(data.data.third_party);
                //				} else {
                //					$("#form_third_party").hide();
                //				}
                $("#label_defendant").html(data.data.defendant);
                $("#label_specific_behavior").html(data.data.specific_behavior);
                $("#label_apply_request").html(data.data.apply_request);
                $("#label_facts_reasons").html(data.data.facts_reasons);
                $("#label_create_time").html(data.data.create_time);
                $("#label_last_time").html(data.data.last_time);
                initLabels("labels", data.data.labels, "，");

                $("#label_defendant_reply").html(data.data.defendant_reply);
                $("#label_defendant_remark").html(data.data.defendant_remark);

                if (data.data.type == "2") {
                    $(".case-type-2").show();
                } else {
                    $(".case-type-2").hide();
                }
            }
            if (isWithToolbar) {
                casesType = data.data.type;
                initModalEvent4Apply();
                initModalEvent4ApplyThirdParty();
                initModalEvent4ThirdParty();
                initModalEvent4Agent();
                if (data.data.state == "3" || data.data.state == "4" || data.data.state == "5" || data.data.state == "6" || data.data.state == "7" || data.data.state == "8" ||
                    data.data.state == "17" || data.data.state == "21" || data.data.state == "22" || data.data.state == "23" || data.data.state == "24" || data.data.state == "25" || data.data.state == "26" ||
                    data.data.state == "18" || data.data.state == "19" || data.data.state == "15" || data.data.state == "16") {
                    initView();
                }
            }
            if (isWithDocument) {
                if (data.data.type == "1") {
                    $(".font-apply-type").html("你");
                } else if (data.data.type == "3") {
                    $(".font-apply-type").html("你单位");
                } else {
                    $(".font-apply-type").html("你们");
                }
                $(".font-year-no").html(data.data.year_no);
                $(".font-no").html(data.data.no);
                $(".font-apply").html(data.data.apply);
                $(".font-third-party").html(data.data.third_party);
                $(".font-defendant").html(data.data.defendant);
                $(".font-reason").html(data.data.specific_behavior);
                $(".font-apply-time").html(formatDate(data.data.apply_time));
                //				if(data.data.third_party == null || data.data.third_party == "") {
                //					$(".process-state-9-3").hide();
                //				} else {
                //					if(state == "9") {
                //						$(".process-state-9-3").show();
                //					}
                //				}
                $.each(data.data.apply_list, function (index, value) {
                    var name = "";
                    if (value.type == "1") {
                        if (value.other_name != null && value.other_name != "" && value.other_name != "无") {
                            name += value.name + "，英文名/其他语种名/化名/曾用名，" + value.other_name;
                        }
                    } else {
                        name += value.unit_name;
                    }
                    var birthdars = value.birthday.split("-");
                    if (birthdars.length == 3) {
                        birthdars = birthdars[0] + "年" + birthdars[1] + "月" + birthdars[2] + "日";
                    }
                    var $apply = $("<div style='text-indent: 2em;'>申请人：" + name + "," + value.label_gender + value.nature
                        + "族," + birthdars + "出生,户籍所在地" + value.domicile + "。</div>");
                    $(".div-apply").append($apply);
                })
                $.each(data.data.third_party_list, function (index, value) {
                    var name = value.name;
                    if (value.type == "1") {
                        if (value.legal_person != null && value.legal_person != "" && value.legal_person != "无") {
                            name += "，1英文名/其他语种名/化名/曾用名，" + value.legal_person;
                        }
                    } else {
                        name += "，法定代表人（负责人），" + value.legal_person;
                    }
                    var zipCode = "";
                    if (value.zip_code != null && value.zip_code != "" && value.zip_code != "无") {
                        zipCode = "，邮编，" + value.zip_code
                    }
                    var $third = $("<div style='text-indent: 2em;'>第三人：" + name + "，证件号码，" + value.id_no +
                        "，手机号码，" + value.phone + zipCode +
                        "，通讯地址，" + value.province + value.city + value.county + value.address + "。</div>")
                    $(".div-third-party").append($third);
                })
                //				if (data.data.agent_list != null && data.data.agent_list.length > 0) {
                //					$.each(data.data.agent_list, function(index, value) {
                //						var $agent = $("<div style='text-indent: 2em;'>委托代理人：" + value.name + "，证件号码，" + value.id_no +
                //							"，手机号码，" + value.phone + "，邮编，" + value.zip_code +
                //							"，联系地址，" + value.province + value.city + value.county +
                //							"，详细地址，" + value.address + "，工作单位，" + value.unit_name + "。</div>")
                //						$(".div-agent").append($agent);
                //					})
                //				}
                $.each(data.data.defendant_list, function (index, value) {
                    var $defendant = $("<div style='text-indent: 2em;'>被申请人：" + value.name + "。</div>" +
                        "<div style='text-indent: 2em;'>住所：" + value.address + "。</div>" +
                        "<div style='text-indent: 2em;'>负责人(法定代表人)：" + value.legal_person + "。</div>"
                    )
                    $(".div-defendant").append($defendant);
                })
            }
        },
        error: function (xhr, type) {
            $("#loading_info").hide();
            showMsg("系统繁忙");
        }
    });
}

function initEvent4Document() {
    $(".btn-view").off().on("click", function () {
        initModal4View("loading_document", $(this).data("id"));
    })
}

/*function initChart4Pie(elementId, data, title) {
	console.log("饼状图里面的data==="+ JSON.stringify(data));
	title = title || "";
	var configPie = {
		type: "pie",
		data: {
			datasets: [{
				data: data.data_list,
				backgroundColor: data.color_list,
			}],
			labels: data.label_list
		},
		options: {
			responsive: true,
			title: {
				display: (title != null && title != ""),
				text: title
			},
		}
	};

	var ctxPie = document.getElementById(elementId).getContext("2d");
	new Chart(ctxPie, configPie);
}*/

function initModal4View(loadingId, id, type, reference) {
    type = type || "1";
    reference = reference || "0"
    var url = "";
    if (type == "1") {
        url = "cases_file/get_object";
    } else if (type == "2") {
        url = "regulations/get_object";
    } else {
        url = "litigation/get_object";
    }
    $("#" + loadingId).show();
    $.ajax({
        type: "POST",
        url: url,
        dataType: "JSON",
        data: {
            id: id,
        },
        success: function (data) {
            $("#" + loadingId).hide();
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            if (reference == "0") {
                if (type == "1") {
                    $("#font_content").html(data.data.html);
                } else {
                    $("#font_content").html(data.data.content);
                }
                $("#modal_view").modal("show");
                return;
            }
            $("#div_reference_content").html(data.data.content);
            $("#div_reference").css("width", $("#div_switch").css("width"));
            $("#div_switch").hide();
            $("#div_reference").show();

            $("#btn_close_reference").off().on("click", function () {
                $("#div_switch").show();
                $("#div_reference").hide();
            })
        },
        error: function (xhr, type) {
            $("#" + loadingId).hide();
            showMsg("系统繁忙");
        }
    });
}

function formatDate(date) {
    var arr = date.split("-");
    var year = arr[0];
    var month = arr[1].replace(/^0+/, "");
    var day = arr[2].replace(/^0+/, "");
    return year + "年" + month + "月" + day + "日"
}

function initModal4Rename() {
    //重命名文件
    $('#modal_rename').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        $("#rename_id").val(button.data("id"));
        $(".rename-name").html(button.data("name"));
        $("#rename_name").val(null);
    })
    $("#btn_submit_rename").on("click", function () {
        var data = $("#form_rename").serialize();
        $.ajax({
            url: "temp/rename",
            type: "post",
            dataType: "json",
            data: data,
            success: function (data) {
                if (data.result != 1) {
                    showMsg(data.error_msg);
                    return;
                }
                $("#font_" + $("#rename_id").val()).html($("#rename_name").val());
                $('#modal_rename').modal("hide");
            },
            error: function (xhr, type) {
                showMsg("系统繁忙");
            }
        });
        return false;
    })
}

var editorMap = new Map();

function initContextMenu(state) {
    var items = {};
    if (state == "2") {
        items = {
            "input_2_1": {
                name: "复制到：需补正的材料"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "6") {
        items = {
            "input_6": {
                name: "复制到：不予受理理由和依据"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "8") {
        items = {
            "input_8_1": {
                name: "复制到：主动放弃申请的事由"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "14") {
        items = {
            "input_14_1": {
                name: "复制到：中止审查的事由"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "15") {
        items = {
            "input_15_1": {
                name: "复制到：终止审查的事由"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "16") {
        items = {
            "input_16_1": {
                name: "复制到：终止审查的事由"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "17") {
        items = {
            "input_17_1": {
                name: "复制到：申请人请求"
            },
            "input_17_2": {
                name: "复制到：申请人称"
            },
            "input_17_3": {
                name: "复制到：被申请人称"
            },
            "input_17_4": {
                name: "复制到：证据分析"
            },
            "input_17_5": {
                name: "复制到：经审理查明"
            },
            "input_17_6": {
                name: "复制到：本机关认为"
            },
            "input_17_7": {
                name: "复制到：决定"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "18") {
        items = {
            "input_18_1": {
                name: "复制到：申请人请求"
            },
            "input_18_2": {
                name: "复制到：申请人称"
            },
            "input_18_3": {
                name: "复制到：被申请人称"
            },
            "input_18_4": {
                name: "复制到：证据分析"
            },
            "input_18_5": {
                name: "复制到：经审理查明"
            },
            "input_18_6": {
                name: "复制到：本机关认为"
            }
        }
        createContextMenu(items);
        return;
    }
    if (state == "19") {
        items = {
            "input_19_1": {
                name: "复制到：申请人请求"
            },
            "input_19_2": {
                name: "复制到：申请人称"
            },
            "input_19_3": {
                name: "复制到：被申请人称"
            },
            "input_19_4": {
                name: "复制到：证据分析"
            },
            "input_19_5": {
                name: "复制到：经审理查明"
            },
            "input_19_6": {
                name: "复制到：本机关认为"
            }
        }
        createContextMenu(items);
        return;
    }
}

function createContextMenu(items) {
    $.contextMenu({
        selector: '.div-reference',
        callback: function (key, options) {
            executeContextMenu(key);
        },
        items: items
    });
}

function executeContextMenu(key) {
    var selected = window.getSelection();
    if (selected == null || selected == "") {
        return;
    }
    if ("input_6" == key) {
        var content = $("#input_6").val();
        $("#" + key).val(content + selected);
    } else {
        var editor = editorMap.get("editor_" + key);
        var content = editor.getData();
        editor.setData(content + selected);
    }
}

var isReference = "0";

function initTabs4Refence(casesId, reference) {
    isReference = reference
    initTab4Litigation(casesId);
    $(".btn-close-reference").on("click", function () {
        $(".div-reference").hide();
    })
    initData4Regulation();
}

function initTab4Litigation(casesId) {
    loadData4CheckBox("div_cases_labels", "cases-label", "cases/labels?cases_id=" + casesId)
    initData4Litigation();
}

function loadData4CheckBox(parentId, name, url) {
    $.ajax({
        type: "POST",
        url: url,
        dataType: "JSON",
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            $("." + name).remove(); // 清除数据
            $.each(data.data, function (index, value) {
                var $checkbox = $("<label style='padding: 0 5px;'><input type='checkbox' class='minimal " + name + "' name='" + name + "' id='" + name + "_" + value.id + "' value='" + value.id + "' data-label='" + value.text + "'> " + value.text + "</label>");
                $("#" + parentId).append($checkbox);
            })
            initCheckBoxByClassName(name);

            if (name == "cases-label") {
                onEvent4CasesLabel();
            }
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

function onEvent4CasesLabel() {
    $(".cases-label").on("ifChanged", function () {
        pageLitigation = 1;
        initData4Litigation();
    })
}

var pageLitigation = 1; //当前页
var pageTotalLitigation = 1; //总页数

//加载数据
function initData4Litigation() {
    var keyArr = [];
    $(".cases-label:checked").each(function () {
        keyArr.push($(this).data("label"));
    })
    $.ajax({
        type: "POST",
        url: "litigation/get_list",
        dataType: "JSON",
        data: {
            page: pageLitigation,
            search_key: keyArr.join(","),
        },
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            $(".tr-data-litigation").remove(); //清除数据
            var dataTotal = data.data.data_total;
            if (dataTotal == 0) {
                $(".page-info-litigation").attr("disabled", true);
                return;
            }
            pageTotalLitigation = data.data.page_total;
            $("#btn_page_total_litigation").html(pageLitigation + "/" + pageTotalLitigation);
            $("#btn_data_total_litigation").html("总数：" + dataTotal);
            $.each(data.data.data_list, function (index, value) {
                $td_data = $("<tr class='tr-data-litigation'>" +
                    "<td><a style='cursor: pointer' class='btn-reference' data-id='" + value.id + "' data-type='3' data-reference='" + isReference + "'>" + value.title + "</a></td>" +
                    "</tr>");
                $("#table_data_litigation").append($td_data);
            });

            if (pageLitigation == 1) {
                if (pageLitigation == pageTotalLitigation) {
                    $(".page-info-litigation").attr("disabled", true);
                } else {
                    $("#btn_home_page_litigation").attr("disabled", true); //首页
                    $("#btn_upper_page_litigation").attr("disabled", true); //上一页

                    $("#btn_next_page_litigation").attr("disabled", false); //下一页
                    $("#btn_last_page_litigation").attr("disabled", false); //末页
                }
            } else {
                if (pageLitigation == pageTotalLitigation) {
                    $("#btn_home_page_litigation").attr("disabled", false); //首页
                    $("#btn_upper_page_litigation").attr("disabled", false); //上一页

                    $("#btn_next_page_litigation").attr("disabled", true); //下一页
                    $("#btn_last_page_litigation").attr("disabled", true); //末页
                } else {
                    $(".page-info-litigation").attr("disabled", false);
                }
            }
            initEvent4Reference();
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

//首页
$("#btn_home_page_litigation").click(function () {
    pageLitigation = 1;
    initData4Litigation();
})
//上一页
$("#btn_upper_page_litigation").click(function () {
    pageLitigation--;
    initData4Litigation();
})
//下一页
$("#btn_next_page_litigation").click(function () {
    pageLitigation++;
    initData4Litigation();
})
//末页
$("#btn_last_page_litigation").click(function () {
    pageLitigation = pageTotalLitigation;
    initData4Litigation();
})

function initEvent4Reference() {
    $(".btn-reference").off().on("click", function () {
        initModal4View("loading_view", $(this).data("id"), $(this).data("type"), $(this).data("reference"));
    })
}

var pageRegulation = 1; //当前页
var pageTotalRegulation = 1; //总页数

//加载数据
function initData4Regulation() {
    $.ajax({
        type: "POST",
        url: "regulations/get_list",
        dataType: "JSON",
        data: {
            page: pageRegulation,
            search_key: $("#search_key_regulation").val(),
        },
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }

            $(".tr-data-regulation").remove(); //清除数据
            var dataTotal = data.data.data_total;
            if (dataTotal == 0) {
                $(".page-info-regulation").attr("disabled", true);
                return;
            }
            pageTotalRegulation = data.data.page_total;
            $("#btn_page_total_regulation").html(pageRegulation + "/" + pageTotalRegulation);
            $("#btn_data_total_regulation").html("总数：" + dataTotal);
            $.each(data.data.data_list, function (index, value) {
                $td_data = $("<tr class='tr-data-regulation'>" +
                    "<td><a style='cursor: pointer' class='btn-reference' data-id='" + value.id + "' data-type='2' data-reference='" + isReference + "'>" + value.title + "</a></td>" +
                    "</tr>");
                $("#table_data_regulation").append($td_data);
            });

            if (pageRegulation == 1) {
                if (pageRegulation == pageTotalRegulation) {
                    $(".page-info-regulation").attr("disabled", true);
                } else {
                    $("#btn_home_page_regulation").attr("disabled", true); //首页
                    $("#btn_upper_page_regulation").attr("disabled", true); //上一页

                    $("#btn_next_page_regulation").attr("disabled", false); //下一页
                    $("#btn_last_page_regulation").attr("disabled", false); //末页
                }
            } else {
                if (pageRegulation == pageTotalRegulation) {
                    $("#btn_home_page_regulation").attr("disabled", false); //首页
                    $("#btn_upper_page_regulation").attr("disabled", false); //上一页

                    $("#btn_next_page_regulation").attr("disabled", true); //下一页
                    $("#btn_last_page_regulation").attr("disabled", true); //末页
                } else {
                    $(".page-info-regulation").attr("disabled", false);
                }
            }
            initEvent4Reference();
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}

//首页
$("#btn_home_page_regulation").click(function () {
    pageRegulation = 1;
    initData4Regulation();
})
//上一页
$("#btn_upper_page_regulation").click(function () {
    pageRegulation--;
    initData4Regulation();
})
//下一页
$("#btn_next_page_regulation").click(function () {
    pageRegulation++;
    initData4Regulation();
})
//末页
$("#btn_last_page_regulation").click(function () {
    pageRegulation = pageTotalRegulation;
    initData4Regulation();
})
//查询
$("#btn_search_regulation").click(function () {
    pageRegulation = 1;
    initData4Regulation();
})

function getDocumentHtml(casesId, state, elementId) {
    var actId = "";
    $.ajax({
        async: false,
        type: "POST",
        url: "department_document_number/get_document_html",
        dataType: "JSON",
        data: {
            cases_id: casesId,
            state: state
        },
        success: function (data) {
            if (data.result != 1) {
                showMsg(data.error_msg);
                return;
            }
            $("#" + elementId).html(data.data);
        },
        error: function (xhr, type) {
            showMsg("系统繁忙");
        }
    });
}


//初始化Echarts-Pie图表
function initEcharts4Pie(elementId, title, series_name, data, isDoughnut) {
    var isDoughnut = isDoughnut || false;
    var dom = document.getElementById(elementId);
    var myChart = echarts.init(dom);
    var app = {};
    var colorList = ['#37A2DA', '#32C5E9', '#67E0E3', '#9FE6B8', '#FFDB5C', '#FF9F7F', '#FB7293', '#E062AE', '#E690D1'];
    option = null;
    if (isDoughnut) {
        app.title = '环形图';

        option = {
            color: colorList,
            title: {
                text: title, //主标题
                //subtext: '纯属虚构', //副标题
                x: 'center' //标题位置
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b}: {c} ({d}%)"
            },
            /*legend: {
                orient: 'vertical',
                x: 'left',
                data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
            },*/
            series: [
                {
                    name: series_name,
                    type: 'pie',
                    radius: ['50%', '70%'],
                    avoidLabelOverlap: false,
                    label: {
                        normal: {
                            show: false,
                            position: 'center'
                        },
                        emphasis: {
                            show: true,
                            textStyle: {
                                fontSize: '20',
                                fontWeight: 'bold'
                            }
                        }
                    },
                    labelLine: {
                        normal: {
                            show: false
                        }
                    },
                    data: data,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        ;
    } else {
        option = {
            color: colorList,
            title: {
                text: title, //主标题
                //subtext: '纯属虚构', //副标题
                x: 'center' //标题位置
            },
            tooltip: {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            /*legend: {  //侧边展示栏
                orient: 'vertical',
                left: 'left',
                data: ['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
            },*/
            series: [
                {
                    name: series_name,
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: data,
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    }
                }
            ]
        };
        ;
    }

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

    /*窗口自适应，关键代码*/
    window.addEventListener("resize", function () {
        myChart.resize();
    });
}


function initEcharts4Bar(elementId, title, series_name, data_title, data_value, isLevel) {
    var isLevel = isLevel || false;
    var dom = document.getElementById(elementId);
    var myChart = echarts.init(dom);
    var app = {};
    option = null;
    app.title = '坐标轴刻度与标签对齐';

    //水平柱状图
    if (isLevel) {
        option = {
            color: ['#3398DB'],
            title: {
                text: title, //主标题
                //subtext: '纯属虚构', //副标题
                x: 'center' //标题位置
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [

                {
                    type: 'value'
                }
            ],
            yAxis: [
                {
                    type: 'category',
                    data: data_title,
                    axisTick: {
                        alignWithLabel: true
                    }
                }
            ],
            series: [
                {
                    name: series_name,
                    type: 'bar',
                    barWidth: '50%',
                    data: data_value,
                }
            ]
        };
        ;
        //垂直柱状图
    } else {
        option = {
            color: ['#3398DB'],
            title: {
                text: title, //主标题
                //subtext: 'subtext', //副标题
                x: 'center' //标题位置
            },
            tooltip: {
                trigger: 'axis',
                axisPointer: {            // 坐标轴指示器，坐标轴触发有效
                    type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: data_title,
                    axisTick: {
                        alignWithLabel: true
                    },
                    axisLabel: {
                        //垂直柱状图文字垂直排列
                        formatter: function (value) {
                            return value.split("").join("\n");
                        }
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: series_name,
                    type: 'bar',
                    barWidth: '30%',
                    barCategoryGap: '50%',
                    data: data_value,
                    itemStyle: {
                        normal: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#83bff6'},
                                    {offset: 0.5, color: '#188df0'},
                                    {offset: 1, color: '#188df0'}
                                ]
                            )
                        },
                        emphasis: {
                            color: new echarts.graphic.LinearGradient(
                                0, 0, 0, 1,
                                [
                                    {offset: 0, color: '#2378f7'},
                                    {offset: 0.7, color: '#2378f7'},
                                    {offset: 1, color: '#83bff6'}
                                ]
                            )
                        }
                    },
                }
            ]
        };
        ;
    }

    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }

    /*窗口自适应，关键代码*/
    window.addEventListener("resize", function () {
        myChart.resize();
    });
}

function initEcharts4PieNest(elementId, title, outerRing_name, outerRing_data, inner_name, inner_data) {
    var dom = document.getElementById(elementId);
    var myChart = echarts.init(dom);
    var app = {};
    var colorList = ['#37A2DA', '#32C5E9', '#67E0E3', '#9FE6B8', '#FFDB5C', '#FF9F7F', '#FB7293', '#E062AE', '#E690D1'];
    option = null;
    app.title = '嵌套环形图';

    option = {
        color: colorList,
        title: {
            text: title, //主标题
            //subtext: '纯属虚构', //副标题
            x: 'center' //标题位置
        },
        tooltip: {
            trigger: 'item',
            formatter: "{a} <br/>{b}: {c} ({d}%)"
        },
        /*legend: {
            orient: 'vertical',
            x: 'left',
            data:['直达','营销广告','搜索引擎','邮件营销','联盟广告','视频广告','百度','谷歌','必应','其他']
        },*/
        series: [
            {
                name: outerRing_name,
                type: 'pie',
                selectedMode: 'single',
                radius: [0, '30%'],

                label: {
                    normal: {
                        position: 'inner',
                        show: false
                    }
                },
                labelLine: {
                    normal: {
                        show: false
                    }
                },
                /* data:[
                     {value:335, name:'直达', selected:true},
                     {value:679, name:'营销广告'},
                     {value:1548, name:'搜索引擎'}
                 ]*/
                data: outerRing_data
            },
            {
                name: inner_name,
                type: 'pie',
                radius: ['40%', '55%'],
                /*                label: {
                    normal: {
                        formatter: '{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  ',
                        backgroundColor: '#eee',
                        borderColor: '#aaa',
                        borderWidth: 1,
                        borderRadius: 4,
                        // shadowBlur:3,
                        // shadowOffsetX: 2,
                        // shadowOffsetY: 2,
                        // shadowColor: '#999',
                        // padding: [0, 7],
                        rich: {
                            a: {
                                color: '#999',
                                lineHeight: 22,
                                align: 'center'
                            },
                            // abg: {
                            //     backgroundColor: '#333',
                            //     width: '100%',
                            //     align: 'right',
                            //     height: 22,
                            //     borderRadius: [4, 4, 0, 0]
                            // },
                            hr: {
                                borderColor: '#aaa',
                                width: '100%',
                                borderWidth: 0.5,
                                height: 0
                            },
                            b: {
                                fontSize: 16,
                                lineHeight: 33
                            },
                            per: {
                                color: '#eee',
                                backgroundColor: '#334455',
                                padding: [2, 4],
                                borderRadius: 2
                            }
                        }
                    }
                },*/
                data: inner_data
            }
        ]
    };
    ;
    if (option && typeof option === "object") {
        myChart.setOption(option, true);
    }
    /*窗口自适应，关键代码*/
    window.addEventListener("resize", function () {
        myChart.resize();
    });

}