function getParameter(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if(r != null) {
		return unescape(r[2]);
	}
	return "";
}

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

function initFormList() {
	$("#arrow_left").on("click", function() {
		$("#arrow_left").hide();
		$("#form_list").show();
	});

	$("#arrow_right").on("click", function() {
		$("#arrow_left").show();
		$("#form_list").hide();
	});
}

/**
 * 提示窗口
 */
function showMsg(msg) {
	$("#modal_msg").modal("show");
	$("#font_msg").html(msg);
}

function getStateLabel(state, isProcess) {
	isProcess = isProcess || false;
	var label = "";
	switch(state) {
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
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #00c0ef;color: #fff'>批准补正</label>";
			break;
		case "3":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #39cccc;color: #fff'>批准告知</label>";
			break;
		case "4":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #155e63;color: #fff'>批准转办</label>";
			break;
		case "5":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #76b39d;color: #fff'>批准转送</label>";
			break;
		case "6":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #db456f;color: #fff'>批准不予受理</label>";
			break;
		case "7":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #cd5555;color: #fff'>逾期未补正</label>";
			break;
		case "8":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #882042;color: #fff'>受理前放弃</label>";
			break;
		case "9":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #f39c12;color: #fff'>批准受理待答复</label>";
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
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #7971ea;color: #fff'>批准延期</label>";
			break;
		case "14":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #cc99f9;color: #fff'>批准中止</label>";
			break;
		case "20":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #7a4579;color: #fff'>批准听证</label>";
			break;
		case "17":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #35d0ba;color: #fff'>支持（批准变更）</label>";
			break;
		case "21":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #45b7b7;color: #fff'>支持（批准撤销）</label>";
			break;
		case "22":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #00a8b5;color: #fff'>支持（批准确认违法）</label>";
			break;
		case "23":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #00bbf0;color: #fff'>支持（批准责令履行）</label>";
			break;
		case "24":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #448ef6;color: #fff'>支持（批准确认无效）</label>";
			break;
		case "25":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #2f89fc;color: #fff'>支持（批准调解与决定）</label>";
			break;
		case "26":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #0060ca;color: #fff'>支持（批准其他决定）</label>";
			break;
		case "18":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #f73859;color: #fff'>否定（批准驳回）</label>";
			break;
		case "19":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #d81b60;color: #fff'>否定（批准维持）</label>";
			break;
		case "15":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #f9499e;color: #fff'>终止（自愿撤回）</label>";
			break;
		case "16":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #8b104e;color: #fff'>终止（法定或其他）</label>";
			break;
		case "32":
			label = "<label class='label label-default' style='border-radius: 0px;background-color: #3c8dbc;color: #fff'>部分被申请人答复</label>";
			break;
		default:
			break;
	}
	return label;
}

function getActId() {
	var actId = "";
	$.ajax({
		async: false,
		type: "POST",
		url: "get_act_id.do",
		dataType: "JSON",
		success: function(data) {
			if(data.result != 1) {
				showMsg(data.error_msg);
				return;
			}
			actId = data.data;
		},
		error: function(xhr, type) {
			showMsg("系统繁忙");
		}
	});
	return actId;
}

function initAccount() {
	$.ajax({
		type: "POST",
		url: "get_object",
		dataType: "JSON",
		success: function(data) {
			if(data.result == 1) {
				//用户信息
				$("#span_nickname").html(data.data.name);
				return;
			}
			//window.location.href = "login.html";
		},
		error: function(xhr, type) {
			//window.location.href = "login.html";
		}
	})
}

function initUpload(loadingId, elementId, childClass, url) {
	$("#" + elementId).fileupload({
		url: url,
		dataType: "json",
		add: function(e, data) {
			$("#" + loadingId).show();
			data.submit();
		},
		done: function(e, data) {
			$("#" + loadingId).hide();
			if(data.result.result == "1") {
				initData4Files("ul_" + elementId, childClass, data.result.data);
				if(url.indexOf("temp") != -1) {
					onTempDelete();
				}
			} else {
				showMsg("上传失败！" + data.result.error_msg);
			}
		},
		progressall: function(e, data) {}
	})
}

function initData4Files(parentId, childClass, data, isNotRename) {
	isNotRename = isNotRename || false;
	$("." + childClass).remove();
	$.each(data, function(index, value) {
		var icon = getFileIcon(value.type);
		var btnEye = "";
		if(value.type == "1") {
			btnEye = "<a class='btn btn-default btn-xs' href='" + value.url + "' target='_blank'><i class='fa fa-eye'></i> 预览</a> ";
		} else {
			btnEye = "<a class='btn btn-default btn-xs' href='" + value.url + "' download='" + value.real_name + "." + value.ext + "'><i class='fa fa-eye'></i> 预览</a> ";
		}
		var btnRename = "";
		//		if(!isNotRename) {
		//			btnRename = "<button type='button' class='btn btn-default btn-xs' data-toggle='modal' data-target='#modal_rename' data-id='" + value.id + "' data-name='" + value.real_name + "'><i class='fa fa-edit'></i> 重命名</button> ";
		//		}
		var btnDelete = "";
		if(value.url.indexOf("temp") != -1) {
			btnDelete = "<button type='button' class='btn btn-default btn-xs btn-delete-temp' data-id='" + value.id + "'><i class='fa fa-trash-o'></i> 删除</button>";
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
	})
}

function getFileIcon(type) {
	var icon = "";
	if(type == "1") {
		icon = "fa-file-image-o";
	} else if(type == "2") {
		icon = "fa-file-word-o";
	} else if(type == "3") {
		icon = " fa-file-excel-o";
	} else if(type == "4") {
		icon = "fa-file-pdf-o";
	} else if(type == "5") {
		icon = "fa-file-video-o";
	} else if(type == "6") {
		icon = "fa-file-archive-o";
	} else {
		icon = "fa-file-o";
	}
	return icon;
}

function initLabels(elmentId, data, split) {
	$("#" + elmentId).empty();
	var labelArr = data.split(split);
	$.each(labelArr, function(index, value) {
		if(value != null && value.trim() != "") {
			var $label = $("<font class='bg-gray as-label'>" + value + "</font>");
			$("#" + elmentId).append($label);
		}
	});
}

function onTempDelete() {
	$(".btn-delete-temp").off().on("click", function() {
		var id = $(this).data("id");
		$.ajax({
			url: "temp/delete",
			type: "post",
			dataType: "json",
			data: {
				id: id
			},
			success: function(data) {
				if(data.result != 1) {
					showMsg(data.error_msg);
					return;
				}
				$("#" + id).remove();
			},
			error: function(xhr, type) {
				showMsg("系统繁忙");
			}
		});
	})
}

function getCases(loadingId, casesId) {
	$("#" + loadingId).show();
	$.ajax({
		url: "cases/get_object",
		type: "post",
		dataType: "json",
		data: {
			id: casesId,
		},
		success: function(data) {
			$("#" + loadingId).hide();
			if(data.result != 1) {
				showMsg(data.error_msg);
				return;
			}

			if(data.data.state == "1" || data.data.state == "9" || data.data.state == "11") {
				isOver = data.data.is_over;
				limitTime = data.data.limit_time;
				showTime();
			} else if(data.data.state == "12") {
				isOver = data.data.is_over;
				limitTime = data.data.limit_time;
				showTime();
			}

			$("#label_year_no").html(data.data.year_no);
			$("#label_no").html(data.data.no);
			$("#label_state").html(getStateLabel(data.data.state));
			$("#label_type").html(data.data.label_type);
			$("#label_apply_time").html(data.data.apply_time);
			$("#label_apply_mode").html(data.data.label_apply_mode);
			$("#label_administrative_category").html(data.data.label_administrative_category);
			$("#label_reason").html(data.data.label_reason);
			$("#label_apply").html(data.data.apply);
			if(data.data.third_party != null && data.data.third_party != "") {
				$("#form_third_party").show();
				$("#label_third_party").html(data.data.third_party);
			} else {
				$("#form_third_party").hide();
			}
			$("#label_defendant").html(data.data.defendant);
			$("#label_specific_behavior").html(data.data.specific_behavior);
			$("#label_apply_request").html(data.data.apply_request);
			$("#label_facts_reasons").html(data.data.facts_reasons);
			$("#label_create_time").html(data.data.create_time);
			$("#label_last_time").html(data.data.last_time);
			initLabels("labels", data.data.labels, "，");

			$("#label_defendant_reply").html(data.data.defendant_reply);
			$("#label_defendant_remark").html(data.data.defendant_remark);
		},
		error: function(xhr, type) {
			$("#loading_info").hide();
			showMsg("系统繁忙");
		}
	});
}

function initElement(casesPersonnelType) {
	var element = "";
	if(casesPersonnelType == "1") {
		element = "apply";
	} else if(casesPersonnelType == "2") {
		element = "defendant";
	} else if(casesPersonnelType == "3") {
		element = "third_party";
	} else if(casesPersonnelType == "4") {
		element = "apply_agent";
	} else if(casesPersonnelType == "5") {
		element = "third_party_agent";
	} else if(casesPersonnelType == "6") {
		element = "defendant_agent";
	} else if(casesPersonnelType == "7") {
		element = "apply_third_party";
	}
	return element;
}

function getCasesPersonnelList(loadingId, casesId, casesPersonnelType) {
	$("#" + loadingId).show();
	$.ajax({
		url: "cases_personnel/get_list?type=" + casesPersonnelType + "&cases_id=" + casesId,
		type: "post",
		dataType: "json",
		success: function(data) {
			$("#" + loadingId).hide();
			if(data.result != 1) {
				showMsg(data.error_msg);
				return;
			}

			var element = initElement(casesPersonnelType);
			initData4Personnel("row_" + element, element, data.data);
		},
		error: function(xhr, type) {
			$("#" + loadingId).hide();
			showMsg("系统繁忙");
		}
	});
}

function initData4Personnel(parentId, childClassName, data) {
	$("." + childClassName).remove();
	if(data == null || data.length == 0) {
		return;
	}
	$.each(data, function(index, value) {
		if(typeof value.id_no === "undefined") {
			var file1 = "";
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
			
			var $div = "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' class='" + childClassName + "' id='" + value.id + "'>" +
				"<div class='row'>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-bank margin-r-5'></i>  被申请人名称</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.name + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-map-marker margin-r-5'></i>  被申请人住所</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.address + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-6'>" +
				"<strong style='font-size: 13px'><i class='fa fa-balance-scale margin-r-5'></i>  被申请人类型</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.label_type + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-6'>" +
				"<strong><i class='fa fa-black-tie margin-r-5'></i>  法定代表人</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.legal_person + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-book margin-r-5'></i>  社会统一信用代码证</strong>" +
				"<p style='margin: 10px 0 0;'>" + file1 + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-book margin-r-5'></i>  法定代表人证明书</strong>" +
				"<p style='margin: 10px 0 0;'>" + file2 + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-book margin-r-5'></i>  法定代表人证件信息</strong>" +
				"<p style='margin: 10px 0 0;'>" + file3 + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-12'>" +
				"<strong><i class='fa fa-user margin-r-5'></i>  委托代理人</strong>" +
				"<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
				"</div>" +
				"</div>";
			$("#" + parentId).append($div);

			if(value.agent_list != null && value.agent_list.length > 0) {
				$.each(value.agent_list, function(index1, value1) {
					$child = $("<button class='btn btn-default bg-gray as-btn' data-toggle='modal' data-target='#agentViewModal' data-temp='0' " +
						"data-id='" + value1.id + "' data-idfilenames='" + value1.agent_id_file_names + "' data-idfiles='" + value1.agent_id_files + "' " + 
						"data-agentfile='" + value1.agent_file + "' data-agentfilename='" + value1.agent_file_name + "' " +
						"data-name='" + value1.name + "' data-unit_name='" + value1.unit_name + "' data-gender='" + value1.label_gender + "' data-birthday='" + value1.birthday + "' " +
						"data-phone='" + value1.phone + "' data-contact='" + value1.contact + "' data-address='" + value1.province + value1.city + value1.county + value1.address + "' id='" + value1.id + "'><i class='fa fa-user'></i> " + value1.name + "</button>");
					$("#personnel_agent_" + value.id).append($child);
				})
			}
		} else {
			var dName = "";
			var dLast = "";

			var domicile = "&nbsp;"
			if(value.domicile != null && value.domicile != "") {
				domicile = value.domicile;
			}
			var birthday = "&nbsp;"
			if(value.birthday != null && value.birthday != "") {
				birthday = value.birthday;
			}
			var contact = "&nbsp;"
			if(value.contact != null && value.contact != "") {
				contact = value.contact;
			}
			if(value.type == "1") {
				var legalPerson = "&nbsp;"
				if(value.legal_person != null && value.legal_person != "") {
					legalPerson = value.legal_person;
				}
				dName = "<div class='col-lg-12'>" +
					"<strong><i class='fa fa-user margin-r-5'></i>  姓名</strong>" +
					"<p style='margin: 10px 0 0;'><a href='" + value.id_file + "' target='_blank'>" + value.name + "</a></p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-6'>" +
					"<strong><i class='fa fa-user margin-r-5'></i>  英文名/其他语言名/化名/曾用名</strong>" +
					"<p style='margin: 10px 0 0;'>" + legalPerson + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-6'>" +
					"<strong><i class='fa fa-user margin-r-5'></i>  户籍所在地</strong>" +
					"<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>";

				dLast += "<div class='col-lg-12'>" +
					"<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
					"<p style='margin: 10px 0 0;'>" + value.province + value.city + value.county + value.address + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>";
			} else if(value.type == "2") {
				var labelUnitIdType = "&nbsp;";
				if(value.label_unit_id_type != null && value.label_unit_id_type != "") {
					labelUnitIdType = value.label_unit_id_type;
				}
				var unitIdNo = "&nbsp;";
				if(value.unit_id_no != null && value.unit_id_no != "") {
					unitIdNo = value.unit_id_no;
				}
				var unitContact = "&nbsp;";
				if(value.unit_contact != null && value.unit_contact != "") {
					unitContact = value.unit_contact;
				}
				var legalPerson = "&nbsp;";
				if(value.legal_person != null && value.legal_person != "") {
					legalPerson = value.legal_person;
				}
				dName = "<div class='col-lg-12'>" +
					"<strong><i class='fa fa-bank margin-r-5'></i>  法人组织名称</strong>" +
					"<p style='margin: 10px 0 0;'>" + value.name + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-4'>" +
					"<strong><i class='fa fa-briefcase margin-r-5'></i>  单位证件类型</strong>" +
					"<p style='margin: 10px 0 0;'>" + labelUnitIdType + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-4'>" +
					"<strong><i class='fa fa-barcode margin-r-5'></i>  单位证件号码</strong>" +
					"<p style='margin: 10px 0 0;'>" + unitIdNo + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-4'>" +
					"<strong><i class='fa fa-phone margin-r-5'></i>  单位联系方式</strong>" +
					"<p style='margin: 10px 0 0;'>" + unitContact + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-12'>" +
					"<strong><i class='fa fa-map-marker margin-r-5'></i>  通讯地址</strong>" +
					"<p style='margin: 10px 0 0;'>" + value.province + value.city + value.county + value.address + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-6'>" +
					"<strong><i class='fa fa-user margin-r-5'></i>  法定代表人</strong>" +
					"<p style='margin: 10px 0 0;'><a href='" + value.id_file + "' target='_blank'>" + legalPerson + "</a></p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>" +
					"<div class='col-lg-6'>" +
					"<strong><i class='fa fa-home margin-r-5'></i>  户籍所在地</strong>" +
					"<p style='margin: 10px 0 0;'>" + domicile + "</p>" +
					"<hr style='margin: 10px 0;'>" +
					"</div>";
			}

			dLast += "<div class='col-lg-12'>" +
				"<strong><i class='fa fa-map-marker margin-r-5'></i>  委托代理人</strong>" +
				"<p style='margin: 10px 0 0;' id='personnel_agent_" + value.id + "'></p>" +
				"</div>";
			var $div = "<div style='border: 1px solid #eee;padding: 15px; margin-bottom: 10px;' class='" + childClassName + "' id='" + value.id + "'>" +
				"<div class='row'>" +
				dName +
				"<div class='col-lg-6'>" +
				"<strong><i class='fa fa-intersex margin-r-5'></i>  性别</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.label_gender + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-6'>" +
				"<strong><i class='fa fa-calendar margin-r-5'></i>  出生日期</strong>" +
				"<p style='margin: 10px 0 0;'>" + birthday + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-6'>" +
				"<strong><i class='fa fa-mobile margin-r-5'></i>  手机号码</strong>" +
				"<p style='margin: 10px 0 0;'>" + value.phone + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				"<div class='col-lg-6'>" +
				"<strong><i class='fa fa-phone margin-r-5'></i>  其他联系方式</strong>" +
				"<p style='margin: 10px 0 0;'>" + contact + "</p>" +
				"<hr style='margin: 10px 0;'>" +
				"</div>" +
				dLast +
				"</div>" +
				"</div>";
			$("#" + parentId).append($div);

			if(value.agent_list != null && value.agent_list.length > 0) {
				$.each(value.agent_list, function(index1, value1) {
					$child = $("<button class='btn btn-default bg-gray as-btn' data-toggle='modal' data-target='#agentViewModal' data-temp='0' " +
						"data-id='" + value1.id + "' data-idfilenames='" + value1.agent_id_file_names + "' data-idfiles='" + value1.agent_id_files + "' " + 
						"data-agentfile='" + value1.agent_file + "' data-agentfilename='" + value1.agent_file_name + "' " +
						"data-name='" + value1.name + "' data-unit_name='" + value1.unit_name + "' data-gender='" + value1.label_gender + "' data-birthday='" + value1.birthday + "' " +
						"data-phone='" + value1.phone + "' data-contact='" + value1.contact + "' data-address='" + value1.province + value1.city + value1.county + value1.address + "' id='" + value1.id + "'><i class='fa fa-user'></i> " + value1.name + "</button>");
					$("#personnel_agent_" + value.id).append($child);
				})
			}
		}
	})
}

function initToolBar(casesId) {
	//进度查询
	$("#modal_step").on("show.bs.modal", function(e) {
		$.ajax({
			url: "cases_step/get_list",
			type: "post",
			dataType: "json",
			data: {
				cases_id: casesId
			},
			success: function(data) {
				if(data.result != 1) {
					showMsg(data.error_msg);
					return;
				}

				$(".li-step").remove();
				if(data.data != null && data.data.length > 0) {
					$.each(data.data, function(index, value) {
						var lebal = getStateLabel(value.state, true);
						var label_account = "";
						if(value.label_account != null && value.label_account != "") {
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
			error: function(xhr, type) {
				showMsg("系统繁忙");
			}
		});
	});
	//涉案文书
	$("#modal_document").on("show.bs.modal", function() {
		getFileList("loading_document", "table_cases_file_6", "cases-file-6", "cases_file/files?mode=6&cases_id=" + casesId, false, true);
	})
}

function getFileList(loadingId, parentId, childClass, url, isLabel, isDocument) {
	isLabel = isLabel || false;
	isDocument = isDocument || false;
	$("#" + loadingId).show();
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
			if(isLabel) {
				initData4FileLabels(parentId, childClass, data.data);
				return;
			}
			if(isDocument) {
				initData4Document(parentId, childClass, data.data);
				return;
			}
			initData4Files(parentId, childClass, data.data);
			if(url.indexOf("temp") != -1) {
				onTempDelete();
			}
		},
		error: function(xhr, type) {
			$("#" + loadingId).hide();
			showMsg("系统繁忙");
		}
	});
}

function initData4FileLabels(parentId, childClass, data) {
	$("." + childClass).remove();
	$.each(data, function(index, value) {
		var icon = getFileIcon(value.type);
		if(value.type == "1") {
			var $li = $("<a href='" + value.url + "' target='_blank' class='btn bg-gray as-btn'><i class='fa " + icon + "'></i> " + value.real_name + "</a>");
			$("#" + parentId).append($li);
		} else {
			var $li = $("<a href='" + value.url + "' download='" + value.real_name + "." + value.ext + "' class='btn bg-gray as-btn'><i class='fa " + icon + "'></i> " + value.real_name + "</a>");
			$("#" + parentId).append($li);
		}

	})
}

function initData4Document(parentId, childClass, data) {
	$("." + childClass).remove();
	$.each(data, function(index, value) {
		var $tr = $("<tr class='" + childClass + "'>" +
			"<td>" + value.real_name + "</td>" +
			"<td style='width: 100px;'>" +
			"<button class='btn btn-xs btbtn-default btn-view' data-id='" + value.id + "'>预览</button> " +
			"<a class='btn btn-xs btn-default' href='" + value.url + "' download='" + value.real_name + "." + value.ext + "'>下载</a>" +
			"</td>" +
			"</tr>");
		$("#" + parentId).append($tr);
	})
	initEvent4Document();
}

function initEvent4Document() {
	$(".btn-view").on("click", function() {
		initModal4View("loading_document", $(this).data("id"));
	})
}

function initModal4View(loadingId, id, type) {
	type = type || "1";
	var url = "";
	if(type == "1") {
		url = "cases_file/get_object";
	} else if(type == "2") {
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
		success: function(data) {
			$("#" + loadingId).hide();
			if(data.result != 1) {
				showMsg(data.error_msg);
				return;
			}
			if(type == "1") {
				$("#font_content").html(data.data.html);
			} else {
				$("#font_content").html(data.data.content);
			}
			$("#modal_view").modal("show");
		},
		error: function(xhr, type) {
			$("#" + loadingId).hide();
			showMsg("系统繁忙");
		}
	});
}

function initModal4AgentView() {
	$("#agentViewModal").on("show.bs.modal", function(event) {
		var button = $(event.relatedTarget);
		if(button.data("temp") == "1") {
			$("#div_delete_agent").show();
			$("#btn_agent_delete").data("id", button.data("id"));
			$("#btn_agent_delete").data("agent", "1");
		} else {
			$("#div_delete_agent").hide();
		}
		$("#label_agent_name").html(button.data("name"));
		var unit_name = "&nbsp;"
		if(button.data("unit_name") != null && button.data("unit_name") != "") {
			unit_name = button.data("unit_name");
		}
		$("#label_agent_unit_name").html(unit_name);
		$("#label_agent_gender").html(button.data("gender"));
		var birthday = "&nbsp;"
		if(button.data("birthday") != null && button.data("birthday") != "") {
			birthday = button.data("birthday");
		}
		$("#label_agent_birthday").html(birthday);
		$("#label_agent_phone").html(button.data("phone"));
		var contact = "&nbsp;"
		if(button.data("contact") != null && button.data("contact") != "") {
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
			$.each(nameArr, function(index, value) {
				var $file = $("<a class='bg-gray as-btn' href='" + fileArr[index] + "' target='_blank'><i class='fa fa-image'></i> " + nameArr[index] + "</a>")
				$("#label_agent_id_file").append($file);
			});
		}
		$("#label_agent_file").html("<a class='bg-gray as-btn' href='" + button.data("agentfile") + "' target='_blank'>" + button.data("agentfilename") + "</a>");
	})
}

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
	if(hour < 10) {
		hour = "0" + hour;
	}
	if(minute < 10) {
		minute = "0" + minute;
	}
	if(second < 10) {
		second = "0" + second;
	}

	limitTime -= 1000;

	// 显示时间
	if(isOver == "0") {
		$(".callout-info").show();
		$(".callout-danger").hide();
	} else if(isOver == "1") {
		$(".callout-info").hide();
		$(".callout-danger").show();
	}
	$(".font-day").html(day);
	$(".font-hour").html(hour);
	$(".font-minute").html(minute);
	$(".font-second").html(second);

	setTimeout("showTime()", 1000);
};