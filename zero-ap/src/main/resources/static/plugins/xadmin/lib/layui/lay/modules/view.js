var iframe = document.getElementById("view");
var iframeContentWindow = iframe.contentWindow;
var iframeLayui;
var apView;

var data = JSON.parse($('#data').html());
String.prototype.bool = function () {
    return (/^true$/i).test(this);
};

//iframe初始化完回调
if (iframe.attachEvent) {
    iframe.attachEvent("onload", function () {
        initIFrame();
        iframeCallback(iframeLayui);
    });
} else {
    iframe.onload = function () {
        initIFrame();
        iframeCallback(iframeLayui);
    };
}

/**
 * 初始化iframe大小、数据
 */
function initIFrame() {
    iframeLayui = iframeContentWindow.layui;
    iframe.height = document.documentElement.clientHeight;
}

/**
 * iframe回调
 * @param layui
 */
function iframeCallback(layui) {
    layui.use(['layer', 'laydate', 'laypage', 'laytpl', 'layedit', 'form', 'upload', 'transfer', 'tree', 'table', 'element', 'rate', 'colorpicker',
        'slider', 'carousel', 'flow', 'util', 'code', 'jquery', 'mobile', 'view'], function () {
        var apTable = layui.table;
        var apForm = layui.form;

        //table表单数据
        var cols = data.fieldData || [];
        var where = {};
        var url = data.requestUrl || '';
        var totalRow = (typeof (data.totalRow) !== 'undefined' && data.totalRow.bool()) || false;
        var page = (typeof (data.page) !== 'undefined' && data.page.bool()) || false;

        var searchData = data.searchData || [];
        var analysisData = data.analysisData || [];
        var buttonData = data.buttonData || {};
        var beforeData = data.beforeData || "";

        apView.setTitle(data.title || '');
        apView.addButton(buttonData);

        //初始筛选条件
        initSearchData(searchData, where);
        //初始化列数据-样式格式
        initCols(cols);

        //数据加载前事件
        eval(beforeData);

        //数据加载
        apTable.render({
            id: 'tableData',
            elem: '#tableData',
            method: 'post',
            url: url,
            where: where,
            totalRow: totalRow,
            page: page,
            toolbar: '#toolbar',
            cols: [cols],
            response: {
                statusCode: 200
            },
            parseData: function (res) {
                //返回数据转换
                var result = {};
                result.code = res.code;
                result.msg = res.msg;
                result.count = res.count;
                result.data = res.data;
                for (var index in analysisData) {
                    if (analysisData.hasOwnProperty(index)) {
                        var o = analysisData[index];
                        result[o.layuiReceive] = res[o.returnData];
                    }
                }
                return result;
            }
        });

        //重写搜索提交按钮
        apForm.on('submit(search)', function (data) {
            where = data.field;
            //执行重载
            apTable.reload('tableData', {
                url: url,
                page: page,
                where: where
            });
            return false;
        });
    });
}

/**
 * 初始化搜索条件
 * @param searchData 搜索数据
 * @param where 请求条件数据
 */
function initSearchData(searchData, where) {
    if (searchData.length > 0) {
        apView.addSearch(searchData);
    }
    for (var key in searchData) {
        if (searchData.hasOwnProperty(key)) {
            var search = searchData[key];
            where[search.searchId] = search.value || '';
        }
    }
}

/**
 * 初始化列数据-样式格式
 *
 * @param cols 列数据
 */
function initCols(cols) {
    for (var i in cols) {
        if (cols.hasOwnProperty(i)) {
            var data = cols[i];
            var others = data.others;
            if (others !== '' && others !== null && typeof (others) !== 'undefined') {
                var json = $.parseJSON(others);
                for (var key in json) {
                    if (json.hasOwnProperty(key)) {
                        data[key] = json[key];
                    }
                }
            }
        }
    }
}

layui.define([], function (exports) {
    var obj = {
        /**
         * 设置标题
         * @param title 标题名字
         */
        setTitle: function (title) {
            iframeContentWindow.document.getElementById("title").innerHTML = title;
        },

        /**
         * 获取iframe里面的layui
         * @param iframeId iframe id
         * @returns {r|r} document
         */
        getIframeLayui: function (iframeId) {
            return document.getElementById(iframeId).contentWindow.layui;
        },

        /**
         * 设置iframe，初始化apView操作
         * @param iframeId id
         */
        setIframeLayui: function (iframeId) {
            iframeLayui = document.getElementById(iframeId).contentWindow.layui;
        },

        /**
         * 动态添加工具栏按钮
         *
         * @param buttonData   按钮数据 [{'buttonId':'save','name':'保存'}]
         */
        addButton: function (buttonData) {
            var toolbar = $('#view').contents().find('#toolbar');
            var button = '<div class="layui-btn-container">';
            for (var key in buttonData) {
                if (buttonData.hasOwnProperty(key)) {
                    var data = buttonData[key];
                    button += '<button class="layui-btn layui-btn-sm" lay-event="' + data.buttonId + '">' + data.name + '</button>';
                }
            }
            button += '</div>';
            toolbar.append(button);
        },

        /**
         * 添加搜索
         * @param searchData 搜索条件数据
         */
        addSearch: function (searchData) {
            iframeLayui.use(['laytpl', 'form'], function () {
                var laytpl = iframeLayui.laytpl;
                var form = iframeLayui.form;
                var getTpl = iframeContentWindow.document.getElementById("searchHtml").innerHTML;
                var view = iframeContentWindow.document.getElementById('searchForm');
                laytpl(getTpl).render(searchData, function (html) {
                    view.innerHTML = html;
                });
                //重新渲染表单搜索
                form.render();
            })
        }
    };
    //输出接口
    exports('view', obj);
});