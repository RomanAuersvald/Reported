function showElementaris(e) {
    document.getElementById(e).style.display = "block";
}
function hideElementaris(e) {
    document.getElementById(e).style.display = "none";
}

function showOngoingEntryInfo(data) {
    var FinalProject;
    // call getongoing
    console.log("show info")
    let status = data.responseStatus;
    console.log(status)
    //check the response to make sure it's ok
    if (status == "Ok") {
        var response = data.response;
        console.log(response)
        if (response != null){
            if (response.project != null)
                FinalProject = response.finalProject;

            showElementaris("ongoingEntry");
            showElementaris("stopEntry");
            let projectNameTaskName = response.project.name + " - " + response.task.taskName;
            $('#taskID').val(response.task.id);
            //set the input field values
            console.log(projectNameTaskName);
            document.getElementById('projectNameTaskName').innerHTML
                = projectNameTaskName
            //$('#projectNameTaskName').val(projectNameTaskName);
        }else{
            console.log("no data objects 1");
            hideElementaris("ongoingEntry");
            hideElementaris("stopEntry");
        }
    }else{
        console.log("no data objects 2");
        hideElementaris("ongoingEntry");
        hideElementaris("stopEntry");
    }




}