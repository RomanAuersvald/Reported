//from the ajax call
function showEntryProgress(data) {
    let status = data.responseStatus;

    if (status == "Ok") {
        let url = origin + "/task/getOngoingTaskEntry";
        $.get(url, showOngoingEntryInfo);
        window.location.reload(true)
    }
}

function startTaskEntry2(id) {
    let url = origin + "/task/startTaskEntry/" + id;
    console.log(url)
    $.get(url, showEntryProgress);

}

function endTaskEntry2(id){
    let url = origin + "/task/endTaskEntry/" + id;
    console.log(url)
    $.get(url, hideEntryProhress);
}


function endTaskEntry(){
    let tID = $('#taskID').val()
    var url = ""
    if (tID != ""){
        console.log(tID)
        url = origin + "/task/endTaskEntry/" + tID;
    }else{
        url = origin + "/task/endTaskEntry/" + taskID;
    }

    $.get(url, hideEntryProhress);
}

//from the ajax call
function hideEntryProhress(data) {
    let status = data.responseStatus;
    if (status == "Ok") {
        let url = origin + "/task/getOngoingTaskEntry";
        $.get(url, showOngoingEntryInfo);
        window.location.reload(true)
    }
}

function showStopButton(){
    $('#btnStop').show();
    $('#btnStart').hide();
}

function showStartButton(){
    $('#btnStop').hide();
    $('#btnStart').show();
}
