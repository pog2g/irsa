var modalApply = "<div class='modal fade' id='modal_add_apply'>" +
            "<div class="modal-dialog modal-lg">" +
             "   <div class="modal-content">" +
              "      <div class="modal-header">" +
               "         <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>" +
                "        <h4 class="modal-title"><i class="fa fa-plus"></i> 添加 - 申请人</h4>" +
                 "   </div>" +
                  "  <div class="modal-body">" +
                   "     <div class="box box-solid no-shadow no-margin" style="border: 1px solid #eee">" +
                    "        <div class="box-body" style="border-bottom: 1px solid #eee;">" +
                     "           <div class="form-group" style="margin-bottom: 5px;">" +
                      "              <label>请输入证件号码</label>" +
                       "             <select class="select2 form-control" style="width: 100%;" id="search_apply"></select>" +
                        "        </div>" +
                         "   </div>" +
                          "  <div class="box-body">" +
                           "     <div class="row">" +
                            "        <div class="col-lg-3">" +
                             "           <div class="form-group">" +
                              "              <label>类型</label>" +
                               "             <div class="form-control">" +
                                "                <label style="padding-left: 7px"><input type="radio" class="minimal" id="apply_type_1"" +
                                 "                   name="apply_type" value="1" checked="checked"> 个人</label>" +
                                  "              <label style="padding-left: 7px"><input type="radio" class="minimal" id="apply_type_2"" +
                                   "                 name="apply_type" value="2"> 法人组织</label>" +
                                    "        </div>" +
                                     "   </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">" +
                                     "   <div class="form-group">" +
                                     "       <label>证件类型</label>" +
                                     "       <select class="form-control" style="width: 100%;" id="apply_id_type" name="apply_id_type"></select>" +
                                     "   </div>" +
                                    "</div>" +
                                    "<div class="col-lg-6">" +
                                    "    <div class="form-group">" +
                                    "        <label>证件号码</label>" +
                                    "        <input class="form-control" id="apply_id_no" name="apply_id_no" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-6">" +
                                    "    <div class="form-group">" +
                                    "        <label id="label_apply_name"></label>" +
                                    "        <input class="form-control" id="apply_name" name="apply_name" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-6">" +
                                    "    <div class="form-group">" +
                                    "        <label id="label_apply_legal_person"></label>" +
                                    "        <input class="form-control" id="apply_legal_person" name="apply_legal_person" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">" +
                                    "    <div class="form-group">" +
                                    "        <label>性别</label>" +
                                    "        <div class="form-control">" +
                                    "            <label style="padding-left: 7px"><input type="radio" class="minimal" id="apply_gender_1" name="apply_gender" value="1" checked="checked"> 男</label>" +
                                    "            <label style="padding-left: 7px"><input type="radio" class="minimal" id="apply_gender_2" name="apply_gender" value="2"> 女</label>" +
                                    "        </div>" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">" +
                                    "    <div class="form-group">" +
                                    "        <label>出生日期 <small>（格式：2018-01-01）</small></label>" +
                                    "        <input class="form-control" id="apply_birthday" name="apply_birthday" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">" +
                                    "    <div class="form-group">" +
                                    "        <label>手机号码</label>" +
                                    "        <input class="form-control" id="apply_phone" name="apply_phone" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">" +
                                    "    <div class="form-group">" +
                                    "        <label>其他联系方式</label>" +
                                    "        <input class="form-control" id="apply_contact" name="apply_contact" />" +
                                    "    </div>" +
                                    "</div>" +
                                    "<div class="col-lg-3">
                                    "    <div class="form-group">
                                    "        <label>邮编</label>
                                    "        <input class="form-control" id="apply_zip_code" name="apply_zip_code" />
                                    "    </div>
                                    "</div>
                                    "<div class="col-lg-9">
                                    "    <div class="form-group">
                                    "        <label>联系地址</label>
                                    "        <div class="input-group">
                                    "            <div class="input-group-addon">省</div>
                                    "            <select style="width: 100%;" id="apply_province" name="apply_province"></select>
                                    "            <div class="input-group-addon">市</div>
                                    "            <select class="select2" style="width: 100%;" id="apply_city" name="apply_city"></select>
                                    "            <div class="input-group-addon">区/县</div>
                                    "            <select class="select2" style="width: 100%;" id="apply_county" name="apply_county"></select>
                                    "        </div>
                                    "    </div>
                                    "</div>
                                    "<div class="col-lg-12">
                                        "<div class="form-group">
                                       "     <label>详细地址</label>
                                      "      <input class="form-control" id="apply_address" name="apply_address" />
                                     "   </div>" +
                                    "</div>" +
                                    "<div class="col-lg-12">" +
                                        "<div class="form-group no-margin">" +
                                            "<label>证件信息</label>" +
                                            "<div style="border: 1px solid #d2d6de;padding: 10px 0 0 10px; ">" +
         "                                       <ul class="mailbox-attachments clearfix" id="ul_apply_file">" +
         "                                           <li style="width: auto;">" +
         "                                               <div class="mailbox-attachment-info">" +
        "                                                    <div class="mailbox-attachment-name" style="overflow: hidden;height: 25px;"><i class="fa fa-paperclip"></i> 选择文件</div>" +
        "                                                    <button class="btn btn-primary btn-skin btn-xs btn-file">" +
        "                                                    <span class="mailbox-attachment-size">
        "                                                        <i class="fa fa-plus"></i> 添加" +
        "                                                        <input type="file" id="apply_file" />" +
        "                                                    </button>" +
        "                                                </span>" +
        "                                                </div>" +
        "                                            </li>" +
        "                                        </ul>" +
        "                                    </div>" +
        "                                </div>" +
        "                            </div>" +
        "                        </div>" +
        "                    </div>" +
        "                </div>" +
        "           </div>" +
        "            <div class="modal-footer">" +
        "                <button type="button" class="btn btn-primary btn-skin" id="btn_clear_apply">清空</button>" +
        "                <button type="button" class="btn btn-primary btn-skin" id="btn_submit_apply">确定</button>" +
        "                <button type="button" class="btn btn-primary btn-skin" id="btn_next_apply">下一个</button>" +
        "            </div>" +
        "            <div class="loading" style="display: none" id="loading_apply">" +
        "                <i class="fa fa-spin fa-refresh loading-icon"></i>" +
        "            </div>" +
        "        </div>" +
        "    </div>" +
        "</div>";
        
        <div class="modal fade" id="modal_add_agent">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title"><i class="fa fa-plus"></i> 添加 - 委托代理人</h4>
                    </div>
                    <div class="modal-body">
                        <div class="box box-solid no-shadow" style="border: 1px solid #eee;margin-bottom: 10px;">
                            <div class="box-body">
                                <div class="form-group" style="margin-bottom: 5px;">
                                    <label>委托人</label>
                                    <select class="form-control" style="width: 100%;" id="agent_client"></select>
                                </div>
                            </div>
                        </div>
                        <div class="box box-solid no-shadow no-margin" style="border: 1px solid #eee;">
                            <div class="box-body" style="border-bottom: 1px solid #eee;">
                                <div class="form-group" style="margin-bottom: 5px;">
                                    <label>请输入证件号码</label>
                                    <select class="form-control" style="width: 100%;" id="search_agent"></select>
                                </div>
                            </div>
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>姓名</label>
                                            <input class="form-control" id="agent_name" name="agent_name" />
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>证件类型</label>
                                            <select class="form-control" style="width: 100%;" id="agent_id_type" name="agent_id_type"></select>
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>证件号码</label>
                                            <input class="form-control" id="agent_id_no" name="agent_id_no" />
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>性别</label>
                                            <div class="form-control">
                                                <label style="padding-left: 7px"><input type="radio" class="minimal" name="agent_gender"
                                                    value="1" checked="checked"> 男</label>
                                                <label style="padding-left: 7px"><input type="radio" class="minimal" name="agent_gender"
                                                    value="2"> 女</label>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>出生日期 <small>（格式：2018-01-01）</small></label>
                                            <input class="form-control" id="agent_birthday" name="agent_birthday" />
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>手机号码</label>
                                            <input class="form-control" id="agent_phone" name="agent_phone" />
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>其他联系方式</label>
                                            <input class="form-control" id="agent_contact" name="agent_contact" />
                                        </div>
                                    </div>
                                    <div class="col-lg-3">
                                        <div class="form-group">
                                            <label>邮编</label>
                                            <input class="form-control" id="agent_zip_code" name="agent_zip_code" />
                                        </div>
                                    </div>
                                    <div class="col-lg-9">
                                        <div class="form-group">
                                            <label>联系地址</label>
                                            <div class="input-group">
                                                <div class="input-group-addon">省</div>
                                                <select class="select2" style="width: 100%;" id="agent_province"></select>
                                                <div class="input-group-addon">市</div>
                                                <select class="select2" style="width: 100%;" id="agent_city"></select>
                                                <div class="input-group-addon">区/县</div>
                                                <select class="select2" style="width: 100%;" id="agent_county"></select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>详细地址</label>
                                            <input class="form-control" id="agent_address" name="agent_address" />
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>工作单位</label>
                                            <input class="form-control" id="agent_unit_name" name="agent_unit_name" />
                                        </div>
                                    </div>
                                    <div class="col-lg-12">
                                        <div class="form-group no-margin">
                                            <label>证件信息</label>
                                            <div style="border: 1px solid #d2d6de;padding: 10px 0 0 10px;">
                                                <ul class="mailbox-attachments clearfix" id="ul_agent_file">
                                                    <li style="width: auto;">
                                                        <div class="mailbox-attachment-info">
                                                            <div class="mailbox-attachment-name" style="overflow: hidden;height: 25px;"><i class="fa fa-paperclip"></i> 选择文件</div>
                                                            <span class="mailbox-attachment-size">
                                                            <button class="btn btn-primary btn-skin btn-xs btn-file">
                                                                <i class="fa fa-plus"></i> 添加
                                                                <input type="file" id="agent_file" />
                                                            </button>
                                                        </span>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary btn-skin" id="btn_clear_agent">清空</button>
                        <button type="button" class="btn btn-primary btn-skin" id="btn_submit_agent">确定</button>
                        <button type="button" class="btn btn-primary btn-skin" id="btn_next_agent">下一个</button>
                    </div>
                    <div class="loading" style="display: none" id="loading_agent">
                        <i class="fa fa-spin fa-refresh loading-icon"></i>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal fade" id="modal_add_defendant">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title"><i class="fa fa-plus"></i> 添加 - 被申请人</h4>
                    </div>
                    <div class="modal-body">
                        <div class="box box-solid no-shadow no-margin" style="border: 1px solid #eee;">
                            <div class="box-body" style="border-bottom: 1px solid #eee;">
                                <div class="form-group" style="margin-bottom: 5px;">
                                    <label>请输入被申请人名称</label>
                                    <select class="form-control" style="width: 100%;" id="search_defendant"></select>
                                </div>
                            </div>
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>被申请人类型</label>
                                            <select class="form-control" style="width: 100%;" id="defendant_type" name="defendant_type"></select>
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>被申请人名称</label>
                                            <input class="form-control" id="defendant_name" name="defendant_name" />
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>被申请人住所</label>
                                            <input class="form-control" id="defendant_address" name="defendant_address" />
                                        </div>
                                    </div>
                                    <div class="col-lg-6">
                                        <div class="form-group">
                                            <label>法定代表人</label>
                                            <input class="form-control" id="defendant_legal_person" name="defendant_legal_person" />
                                        </div>
                                    </div>
                                    <div class="col-lg-12">
                                        <div class="form-group">
                                            <label>社会统一信用代码证</label>
                                            <div style="border: 1px solid #d2d6de;padding: 10px 0 0 10px;">
                                                <ul class="mailbox-attachments clearfix" id="ul_defendant_file_1">
                                                    <li style="width: auto;">
                                                        <div class="mailbox-attachment-info">
                                                            <div class="mailbox-attachment-name" style="overflow: hidden;height: 25px;"><i class="fa fa-paperclip"></i> 选择文件</div>
                                                            <span class="mailbox-attachment-size">
                                                            <button class="btn btn-primary btn-skin btn-xs btn-file">
                                                                <i class="fa fa-plus"></i> 添加
                                                                <input type="file" id="defendant_file_1" />
                                                            </button>
                                                        </span>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-12">
                                        <div class="form-group">
                                            <label>法定代表人证明书</label>
                                            <div style="border: 1px solid #d2d6de;padding: 10px 0 0 10px;">
                                                <ul class="mailbox-attachments clearfix" id="ul_defendant_file_2">
                                                    <li style="width: auto;">
                                                        <div class="mailbox-attachment-info">
                                                            <div class="mailbox-attachment-name" style="overflow: hidden;height: 25px;"><i class="fa fa-paperclip"></i> 选择文件</div>
                                                            <span class="mailbox-attachment-size">
                                                            <button class="btn btn-primary btn-skin btn-xs btn-file">
                                                                <i class="fa fa-plus"></i> 添加
                                                                <input type="file" id="defendant_file_2" />
                                                            </button>
                                                        </span>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-lg-12">
                                        <div class="form-group no-margin">
                                            <label>法定代表人证件信息</label>
                                            <div style="border: 1px solid #d2d6de;padding: 10px 0 0 10px;">
                                                <ul class="mailbox-attachments clearfix" id="ul_defendant_file_3">
                                                    <li style="width: auto;">
                                                        <div class="mailbox-attachment-info">
                                                            <div class="mailbox-attachment-name" style="overflow: hidden;height: 25px;"><i class="fa fa-paperclip"></i> 选择文件</div>
                                                            <span class="mailbox-attachment-size">
                                                            <button class="btn btn-primary btn-skin btn-xs btn-file">
                                                                <i class="fa fa-plus"></i> 添加
                                                                <input type="file" id="defendant_file_3" />
                                                            </button>
                                                        </span>
                                                        </div>
                                                    </li>
                                                </ul>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary btn-skin" id="btn_clear_defendant">清空</button>
                        <button type="button" class="btn btn-primary btn-skin" id="btn_submit_defendant">确定</button>
                    </div>
                    <div class="loading" style="display: none" id="loading_defendant">
                        <i class="fa fa-spin fa-refresh loading-icon"></i>
                    </div>
                </div>
            </div>
        </div>


$("body").append();
