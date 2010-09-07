$(document).ready(function(){
    var t = $('#tablesorter-demo');
    var isManager = $('#isManager').attr('value');
    if (isManager == 'false') {
      return;
    }

    $.uiTableEdit(t , {
        editDone : function(val,orig_text,ev, td){
            if (val == orig_text)
                return;
                
            var key = $(td).parent().find(".id").text();
            // var thead = jQuery("thead>tr>",t);
            var thead = $("#tablesorter-demo thead tr");
            var row = $(td).parent().children();

            var tdAttr = $(td).attr("name");

            //alert(val + ':' + orig_text + ':' + tdAttr + ':' + row);
            var data = [];
            data.push("id="+key);
            data.push("oldValue="+orig_text);
            data.push("newValue="+val);
            data.push("colName="+tdAttr);
            var datajoin =  data.join("&");

            /*
                row.each(
                    function(nr){
                        var key = thead.eq(nr).text().replace(" ","_");
                        var onerow = key + "=" + escape(jQuery(this).text())
                        data.push(onerow);
                        //alert(onerow);
                    }
                );

                var datajoin =  data.join("&");
                alert(datajoin);
                */
            $.ajax({
                type: "POST",
                url: "/timeentries/tableedit",
                data: datajoin,
                success: function(msg){
                    alert(msg);
                }
            });
        }
    }
    ); // returns t

}); 