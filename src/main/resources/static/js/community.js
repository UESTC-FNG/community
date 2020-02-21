//提交回复
function post(){
    var questionId = $("#question_id").val();
    var content = $("#comment_textarea").val();
  comment2target(questionId,1,content);
}

//展开二级评论窗口
function collapseComments(e){
    var id=e.getAttribute("data-id");
    var comments=$("#comment-"+id);
    if (comments.hasClass("in")){
        comments.removeClass("in");
        e.classList.remove('active');
    }else {
        var subCommentContainer = $("#comment-" + id);
        if (subCommentContainer.children().length != 1) {
            //展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        } else {
            $.getJSON("/comment/" + id, function (data) {
                $.each(data.data.reverse(), function (index, commentDTO) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": commentDTO.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": commentDTO.user.name
                    })).append($("<div/>", {
                        "html": commentDTO.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        "html": moment(commentDTO.gmtCreate).format("YYYY-MM-DD")
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

function comment2target(targetId,type,content){
    if (!content){
        alert("回复内容不能为空~~~~~");
        return ;
    }
    $.ajax({
        type:"POST",
        url:"/comment",
        contentType:'application/json',
        data:JSON.stringify({
            "parentId":targetId,
            "content":content,
            "type":type
        }),
        success:function(response){
            if (response.code== 200){
                window.location.reload();
            }else if (response.code == 2003){
                var a= confirm(response.message);
                if (a){
                    window.open("https://github.com/login/oauth/authorize?client_id=06aae69b0a59da4c7f98&redirect_uri=http://localhost:8080/callback&scope=user&state=1");
                    window.localStorage.setItem("closable",true);
                }
            }else{
                console.log(response.message);
            }
        },
        dataType:"json"
    });


}

function comment(e){
    var commentId = e.getAttribute("data-id");
    var content=$("#input-"+commentId).val();
    comment2target(commentId,2,content)

}


function selectTag(value){
    var val =value.getAttribute("data-tag");
    var previous=$("#tag").val();
    if (previous){
        if (previous.indexOf(val)!=-1){
            return ;
        }else{
            $("#tag").val(previous+','+val);
        }

    }else{
        $("#tag").val(val);
    }
}

function showSelectTag(){
$("#selectTag").show();
}

function displaySelectTag(){
    $("#selectTag").hide();

    onblur="displaySelectTag()"
}