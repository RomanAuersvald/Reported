//from the ajax call
function getClientInfo(data) {
    $('#icoWarning').hide();
    let status = data.responseStatus;

    //check the response to make sure it's ok
    if (status == "Ok") {
        let response = data.response;

        //get the JSON data
        let ico = response.ico;
        let companyName = response.companyName;
        let firstName = response.firstName;
        let lastName = response.lastName;
        let dic = response.dic;

        //set the input field values
        $('#firstName').val(firstName);
        $('#lastName').val(lastName);
        $('#companyName').val(companyName);
        $('#ico').val(ico);
        $('#dic').val(dic);
    }else{
        $('#icoWarning').show();
        //set the input field values
        $('#firstName').val("");
        $('#lastName').val("");
        $('#companyName').val("");
        $('#dic').val("");
        // ico val necháváme pro editaci
    }
}

function verifyClient() {
    let employeeId = $('#ico').val();
    if (employeeId != "" && employeeId.length == 8){
        $('#icoWarning').hide();
        let url = "./getClientFromICO/" + employeeId;
        $.get(url, getClientInfo);
    }else{
        $('#icoWarning').show();
    }
}

function fillClientAddress(){
    let employeeId = $('#clientICO').val();
    let url = "./getClientAddressFromICO/" + employeeId;
    $.get(url, getClientAddressInfo);
}

//from the ajax call
function getClientAddressInfo(data) {
    $('#addressWarning').hide();
    let status = data.responseStatus;

    //check the response to make sure it's ok
    if (status == "Ok") {
        let response = data.response;

        //get the JSON data
        let street = response.street;
        let city = response.city;
        let postCode = response.postCode;

        //set the input field values
        $('#street').val(street);
        $('#city').val(city);
        $('#postCode').val(postCode);
    }else{
        $('#addressWarning').show();
    }
}