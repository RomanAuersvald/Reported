

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

            $(".ongoingEntry").show();
            $(".stopEntry").show();
            let projectNameTaskName = response.project.name + " - " + response.task.taskName;
            $('#taskID').val(response.task.id);
            //set the input field values
            console.log(projectNameTaskName)
            document.getElementById('projectNameTaskName').innerHTML
                = projectNameTaskName
            //$('#projectNameTaskName').val(projectNameTaskName);
        }else{
            console.log("no data objects")
            $(".ongoingEntry").hide();
            $(".stopEntry").hide();
        }
    }else{
        console.log("no data objects")
        $(".ongoingEntry").hide();
        $(".stopEntry").hide();
    }




}