

/* Image Rollover */
function changeImage() {
    var image = document.getElementById('headImage');
    if (image.src.match("img/index/logo.png")) {
        image.src = "img/index/logo_hover.png";
    } else {
        image.src = "img/index/logo.png";
    }
}

/* Form Verification */
// this function is used for search verification */
function pasuser(form) {
    if (!form.code.value == "") { 
        if (form.cat.value  == "") { 
		
        } else {
            alert(form.code.value+"Invalid Password");
        }
    } else {  
		alert("code = "+form.code.value);
    }
}// end function

/* New Window */

//9b copy clicked data
function copyText() {
  /* Alert the copied text */
  if(confirm("Copy: " + "kyle_white@ncirl.ie" + " ?") == true) {
	  navigator.clipboard.writeText("kyle.white_ncirl.ie");
  }
}


