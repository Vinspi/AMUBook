// Toutes les variables et méthodes prefixées par 'sr_' sont liées aux SearchResults

var sr_last_active = "sr_all";

$(document).ready(function(){
    $("#sr_all").click(function(){sr_switch("sr_all");});
    $("#sr_nom").click(function(){sr_switch("sr_nom");});
    $("#sr_prenom").click(function(){sr_switch("sr_prenom");});
    $("#sr_activite").click(function(){sr_switch("sr_activite");});

    sr_switch("sr_all");
});

function sr_switch(newval){
    sr_switchActive(newval);
    sr_switchAllHidden();
    $("#"+newval+"_content").removeClass("hidden");
}

function sr_switchActive(newval){
    $("#"+sr_last_active+"_item").removeClass("active");
    $("#"+newval+"_item").addClass("active");
    sr_last_active = newval;
}

function sr_switchAllHidden(){
    $("#sr_all_content").addClass("hidden");
    $("#sr_nom_content").addClass("hidden");
    $("#sr_prenom_content").addClass("hidden");
    $("#sr_activite_content").addClass("hidden");
}