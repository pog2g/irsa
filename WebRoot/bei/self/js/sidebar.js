var skins_list = $("<ul />", {
	"class": 'list-unstyled clearfix'
});
var skin_blue =
	$("<li />", {
		style: "float:left; width: 50%; padding: 5px;"
	})
	.append("<a href='javascript:void(0);' data-bodyskin='skin-blue' data-btnskin='btn-primary' data-bgskin='blue' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>" +
		"<div><span style='display:block; width: 20%; float: left; height: 7px; background: #002646;'></span><span style='display:block; width: 80%; float: left; height: 7px;background: #002646'></span></div>" +
		"<div><span style='display:block; width: 20%; float: left; height: 20px; background: #002646;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>" +
		"</a>" +
		"<p class='text-center no-margin' style='color: #000000;font-size: 10px;padding-top: 5px'>默认</p>");
skins_list.append(skin_blue);
var skin_red =
	$("<li />", {
		style: "float:left; width: 50%; padding: 5px;"
	})
	.append("<a href='javascript:void(0);' data-bodyskin='skin-red' data-btnskin='btn-danger' data-bgskin='red' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>" +
		"<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-red-active'></span><span class='bg-red' style='display:block; width: 80%; float: left; height: 7px;'></span></div>" +
		"<div><span style='display:block; width: 20%; float: left; height: 20px;' class='bg-red'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>" +
		"</a>" +
		"<p class='text-center no-margin'  style='color: #000000;font-size: 10px;padding-top: 5px'>中国红</p>");
skins_list.append(skin_red);

function changeSkin(bodySkin, btnSkin, bgSkin) {
	$.each(bodySkins, function(index, value) {
		$("body").removeClass(value);
	});
	$("body").addClass(bodySkin);
	store('body_skin', bodySkin);

	$.each(btnSkins, function(index, value) {
		$(".btn-skin").removeClass(value);
	});
	$(".btn-skin").addClass(btnSkin);
	store('btn_skin', btnSkin);

	$.each(bgSkins, function(index, value) {
		$(".logo-skin").removeClass("text-" + value);
	});
	$(".logo-skin").addClass("text-" + bgSkin);
	$(".content-wrapper, .right-side").css("background-image", "url(self/img/bg-" + bgSkin + ".png)");
	store('bg_skin', bgSkin);
	return false;
}

function store(name, val) {
	if(typeof(Storage) !== "undefined") {
		localStorage.setItem(name, val);
	}
}

function get(name) {
	if(typeof(Storage) !== "undefined") {
		return localStorage.getItem(name);
	}
	return "";
}

function setup() {
	$("#tab_skin").append(skins_list);
	var bodySkin = get('body_skin');
	if(bodySkin == null || bodySkin == "" || $.inArray(bodySkin, bodySkins) == -1) {
		bodySkin = "skin-blue";
	}
	btnSkin = get('btn_skin');
	if(btnSkin == null || btnSkin == "" || $.inArray(btnSkin, btnSkins) == -1) {
		btnSkin = "btn-primary";
	}
	bgSkin = get('bg_skin');
	if(bgSkin == null || bgSkin == "" || $.inArray(bgSkin, bgSkins) == -1) {
		bgSkin = "blue";
	}
	changeSkin(bodySkin, btnSkin, bgSkin);
	$("[data-bodyskin]").on('click', function(e) {
		if(bgSkin == "green") {
			$("#switchEyes").bootstrapSwitch("state", true);
		} else {
			$("#switchEyes").bootstrapSwitch("state", false);
		}
		changeSkin($(this).data('bodyskin'), $(this).data('btnskin'), $(this).data('bgskin'));
	});

	if(bgSkin == "green") {
		$("#switchEyes").bootstrapSwitch("state", true);
	} else {
		$("#switchEyes").bootstrapSwitch("state", false);
	}
}

var btnSkin = "";

var bodySkins = [
	"skin-blue",
	"skin-black",
	"skin-red",
	"skin-yellow",
	"skin-purple",
	"skin-green",
	"skin-blue-light",
	"skin-black-light",
	"skin-red-light",
	"skin-yellow-light",
	"skin-purple-light",
	"skin-green-light"
];

var btnSkins = [
	"btn-primary",
	"bg-black",
	"btn-danger",
	"btn-warning",
	"bg-purple",
	"btn-success",
];

var bgSkins = [
	"blue",
	"green",
	"red",
	"yellow",
	"purple",
];

$("#switchEyes").bootstrapSwitch("size", "mini");
$("#switchEyes").on("switchChange.bootstrapSwitch", function(e, state) {
	if(state) {
		store('bodyskin', 'skin-green');
		store('btnskin', 'btn-success');
		store('bgSkins', 'green');
		changeSkin('skin-green', 'btn-success', 'green');
	} else {
		store('bodyskin', 'skin-blue');
		store('btnskin', 'btn-primary');
		store('bgSkins', 'blue');
		changeSkin('skin-blue', 'btn-primary', 'blue');
	}
})

setup();