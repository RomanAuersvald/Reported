//from the ajax call
function showEntryProgress(data) {
    //$('#icoWarning').hide();
    let status = data.responseStatus;

    if (status == "Ok") {
        let url = origin + "/task/getOngoingTaskEntry";
        $.get(url, showOngoingEntryInfo);
        window.location.reload(true)
    }else{
        console.log("fucked")
    }
}

function startTaskEntry() {
    let url = origin + "/task/startTaskEntry/" + taskID;
    console.log(url)
    $.get(url, showEntryProgress);

}

function startTaskEntry2(id) {
    let url = origin + "/task/startTaskEntry/" + id;
    console.log(url)
    $.get(url, showEntryProgress);

}


function endTaskEntry(){
    let tID = $('#taskID').val()
    var url = ""
    if (tID != ""){
        console.log("fucked 3")
        console.log(tID)
        url = origin + "/task/endTaskEntry/" + tID;
    }else{
        console.log("fucked 4")
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
    }else{
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
