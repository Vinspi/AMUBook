// Toutes les variables et méthodes prefixées par 'sr_' sont liées aux SearchResults

var sr_last_active = "sr_all";

$(document).ready(function(){

    $("#sr_all").click(function(){sr_switch("sr_all");});
    $("#sr_nom").click(function(){sr_switch("sr_nom");});
    $("#sr_prenom").click(function(){sr_switch("sr_prenom");});
    $("#sr_activite").click(function(){sr_switch("sr_activite");});
});

function sr_switch(newval){
    sr_switchActive(newval);
    if(newval == "sr_all"){
        sr_switchNoneHidden();
    }
    else{
        sr_switchAllHidden();
        $("#"+newval+"_content").removeClass("hidden");
    }
}

function sr_switchActive(newval){
    $("#"+newval+"_item").addClass("active");
    $("#"+sr_last_active+"_item").removeClass("active");
    sr_last_active = newval;
}

function sr_switchNoneHidden(){
    $("#sr_nom_content").removeClass("hidden");
    $("#sr_prenom_content").removeClass("hidden");
    $("#sr_activite_content").removeClass("hidden");
}
function sr_switchAllHidden(){
    $("#sr_nom_content").addClass("hidden");
    $("#sr_prenom_content").addClass("hidden");
    $("#sr_activite_content").addClass("hidden");
}